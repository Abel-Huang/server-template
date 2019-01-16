package cn.abelib.data.search;

import cn.abelib.common.constant.BusinessConstant;
import cn.abelib.common.constant.ProductBusiness;
import cn.abelib.common.constant.StatusConstant;
import cn.abelib.common.result.Response;
import cn.abelib.common.tools.JsonUtil;
import cn.abelib.shop.dao.CategoryDao;
import cn.abelib.shop.dao.ProductDao;
import cn.abelib.shop.pojo.Product;
import cn.abelib.data.mq.JmsProducer;
import cn.abelib.data.mq.ProductMessage;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.Topic;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author abel
 * @date 2018/4/9
 */
@Slf4j
@Service
public class ProductSearchServiceImpl implements ProductSearchService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private TransportClient esClient;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private JmsProducer jmsProducer;
    @Autowired
    private Topic productTopic;

    /**
     *  异步接受消息队列的请求  消费消息队列中的任务
     *  TODO 目前Active消息队列无法接收消息  需要研究一下
     *  TODO 暂时是能使用同步的方式进行索引的操作
     * @param message
     */
    @JmsListener(destination = "shopping.topic")
    public void receiverMessage(String message){
        System.err.println("Hello, world");
        // 判断消息是否为空
        if (message == null){
            log.error("Message is null");
            return;
        }
        log.debug("receive message {}", message);
        ProductMessage productMessage = JsonUtil.str2Obj(message, ProductMessage.class);
        if (productMessage == null || productMessage.getOperation() == null){
            log.error("productMessage is null");
            return;
        }
        switch (productMessage.getOperation()){
            case ProductMessage.INDEX:
                this.createOrUpdateIndex(productMessage);
                break;
            case ProductMessage.REMOVE:
                this.removeIndex(productMessage);
                break;
            default:
                log.warn("Message not support {}", message);
                break;
        }
    }

    /**
     * 异步构建索引
      * @param message
     */
    private void createOrUpdateIndex(ProductMessage message){
        // 判断消息是否为空
        if (message == null){
            log.error("Message is null {}", message);
            return;
        }
        Integer productId = message.getProductId();
        // 通过数据库查询
        Product product = productDao.selectById(productId);
        if (product == null){
            log.error("Index product {} does not exist!", productId);
            this.index(productId, message.getRetry() + 1);
            return;
        }
        this.commonIndex(product);
    }

    /**
     *  同步构建索引
     * @param productId
     */
    public void syncIndex(Integer productId){
        // 通过数据库查询
        Product product = productDao.selectById(productId);
        if (product == null) {
            log.error("Index product {} does not exist!", productId);
            return;
        }
        this.commonIndex(product);
    }

    /**
     *  无论是同步还是异步都需要使用这个进行构建索引
     * @param product
     */
    private void commonIndex(Product product){
        // 构造商品模板
        ProductIndexTemplate productTemplate =assembleTemplate(product);
        if (productTemplate == null){
            log.error("Assemble product index {} failed!", product.getId());
            return;
        }
        // 构建查询
        SearchRequestBuilder requestBuilder = esClient.prepareSearch(BusinessConstant.Search.PRODUCT_SEARCH_INDEX)
                .setTypes(BusinessConstant.Search.PRODUCT_SEARCH_TYPE)
                .setQuery(QueryBuilders.termQuery(ProductBusiness.PRODUCT_ID, product.getId()));
        SearchResponse response = requestBuilder.get();
        log.debug("Search response: ", response);

        boolean success;
        long totalHit = response.getHits().getTotalHits();
        if (totalHit == 0){
            // 说明当前索引不存在 需要新建一个
            success = create(productTemplate);
        }else if (totalHit == 1){
            // 说明当前索引存在一个 需要更新
            String esId = response.getHits().getAt(0).getId();
            success = update(esId, productTemplate);
        }else {
            success = deleteAndCreate(totalHit, productTemplate);
        }
        if (!success){
            log.error("Index product {} failed", product.getId());
        }
        log.debug("Index product {} success", product.getId());
    }

    private void index(Integer productId, int retry){
        if (retry > ProductMessage.MAX_RETRY){
            log.error("Retry index max times(3) for productId {}", productId);
            return;
        }
        ProductMessage message = new ProductMessage(productId, ProductMessage.INDEX, retry);
        jmsProducer.sendMessage(productTopic, JsonUtil.obj2Str(message));
    }

    @Override
    public void index(Integer productId) {
        this.index(productId, 0);
    }

    /**
     *  组装ProductIndexTemplate
     * @param product
     * @return
     */
    private ProductIndexTemplate assembleTemplate(Product product){
        String categoryName = categoryDao.selectNameByCategoryId(product.getCategoryId());
        ProductIndexTemplate productTemplate = new ProductIndexTemplate();
        productTemplate.setId(product.getId());
        productTemplate.setCategoryId(product.getCategoryId());
        productTemplate.setCategoryName(categoryName);

        productTemplate.setName(product.getName());
        productTemplate.setSubTitle(product.getSubTitle());
        productTemplate.setDetail(product.getDetail());

        productTemplate.setPrice(product.getPrice());
        productTemplate.setStock(product.getStock());
        productTemplate.setCreateTime(product.getCreateTime());
        productTemplate.setUpdateTime(product.getUpdateTime());
        return productTemplate;
    }

    private void removeIndex(ProductMessage message){
        // 判断消息是否为空
        if (message == null){
            log.error("Message is null {}", message);
            return;
        }
        Integer productId = message.getProductId();
        Long deleted = syncRemove(productId);
        if (deleted <= 0){
            this.remove(productId, message.getRetry() + 1);
        }
    }

    /**
     *  同步方式移除索引
     * @param productId
     * @return
     */
    @Override
    public Long syncRemove(Integer productId){
        DeleteByQueryRequestBuilder builder = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(esClient)
                .filter(QueryBuilders.termQuery(ProductBusiness.PRODUCT_ID, productId))
                .source(BusinessConstant.Search.PRODUCT_SEARCH_INDEX);

        log.debug("Delete by query for house: " + builder);
        BulkByScrollResponse response = builder.get();
        long deleted = response.getDeleted();
        log.debug("Delete total {}", deleted);
        return deleted;
    }

    private void remove(Integer productId, int retry){
        if (retry > ProductMessage.MAX_RETRY){
            log.error("Retry remove max times(3) for productId {}", productId);
            return;
        }
        ProductMessage message = new ProductMessage(productId, ProductMessage.REMOVE, retry);
        jmsProducer.sendMessage(productTopic, JsonUtil.obj2Str(message));
    }

    @Override
    public void remove(Integer productId) {
        this.remove(productId, 0);
    }

    // 创建
    private boolean create(ProductIndexTemplate productIndexTemplate){
        IndexResponse indexResponse = esClient.prepareIndex(BusinessConstant.Search.PRODUCT_SEARCH_INDEX, BusinessConstant.Search.PRODUCT_SEARCH_TYPE)
                .setSource(JsonUtil.obj2Str(productIndexTemplate), XContentType.JSON)
                .get();
        log.debug("Create index with product：{}", productIndexTemplate.getId());
        if (indexResponse.status() == RestStatus.CREATED){
            return true;
        }else {
            return false;
        }
    }

    // 更新
    private boolean update(String esId, ProductIndexTemplate productIndexTemplate){
        UpdateResponse indexResponse = esClient.prepareUpdate(BusinessConstant.Search.PRODUCT_SEARCH_INDEX, BusinessConstant.Search.PRODUCT_SEARCH_TYPE, esId)
                .setDoc(JsonUtil.obj2Str(productIndexTemplate), XContentType.JSON)
                .get();
        log.debug("Update index with product：{}", productIndexTemplate.getId());
        if (indexResponse.status() == RestStatus.OK){
            return true;
        }else {
            return false;
        }
    }

    // 删除然后创建
    private boolean deleteAndCreate(long totalHit, ProductIndexTemplate productIndexTemplate){
        DeleteByQueryRequestBuilder builder = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(esClient)
                .filter(QueryBuilders.termQuery(ProductBusiness.PRODUCT_ID, productIndexTemplate.getId()))
                .source(BusinessConstant.Search.PRODUCT_SEARCH_INDEX);

        log.debug("Delete by query for house: " + builder);
        BulkByScrollResponse response = builder.get();
        long deleted = response.getDeleted();
        if (deleted != totalHit){
            log.warn("Need delete {}, but actually {}", totalHit, deleted);
            return false;
        }
        return create(productIndexTemplate);
    }

    /**
     *  简单条件查询
     * @param searchCondition
     * @return
     */
    @Override
    public Response<ProductSearchResult> query(ProductSearchCondition searchCondition){
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // 过滤
//        boolQuery.filter(
//                QueryBuilders.termQuery(ProductBusiness.CATEGPORY_NAME, searchCondition.getCategoryName())
//        );

        // 范围查询
        ProductSearchBlock priceBlock = ProductSearchBlock.matchPrice(searchCondition.getPriceBlock());
        if (!ProductSearchBlock.ALL.equals(priceBlock)){
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(ProductBusiness.PRODUCT_PRICE);
            if (priceBlock.getMax() > 0){
                rangeQueryBuilder.lte(priceBlock.getMax());
            }
            if (priceBlock.getMin() > 0){
                rangeQueryBuilder.lte(priceBlock.getMin());
            }
            boolQuery.filter(rangeQueryBuilder);
        }

        ProductSearchBlock stickBlock = ProductSearchBlock.matchStock(searchCondition.getStockBlock());
        if (!ProductSearchBlock.ALL.equals(stickBlock)){
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(ProductBusiness.PRODUCT_STOCK);
            if (stickBlock.getMax() > 0){
                rangeQueryBuilder.lte(stickBlock.getMax());
            }
            if (stickBlock.getMin() > 0){
                rangeQueryBuilder.lte(stickBlock.getMin());
            }
            boolQuery.filter(rangeQueryBuilder);
        }

        // 多个查询
        boolQuery.must(QueryBuilders.multiMatchQuery(searchCondition.getKeywords(),
                ProductBusiness.SUB_TITLE,
                ProductBusiness.PRODUCT_DETAIL
        ));

        // todo 中文分词以及查询条件
        SearchRequestBuilder builder = esClient.prepareSearch(BusinessConstant.Search.PRODUCT_SEARCH_INDEX)
                .setTypes(BusinessConstant.Search.PRODUCT_SEARCH_TYPE)
                .setQuery(boolQuery)
//                .addSort(
//                        ProductSort.getSortKey(searchCondition.getOrderBy()),
//                        SortOrder.fromString(searchCondition.getOrder())
//                )
                .setFrom(searchCondition.getStart())
                .setSize(searchCondition.getSize());
        log.debug(builder.toString());

        List<Integer> productList = Lists.newArrayList();
        SearchResponse response = builder.get();
        if (response.status() != RestStatus.OK){
            log.warn("Search status is not ok for :" + builder);
            return Response.failed(StatusConstant.QUERY_PRODUCT_FAILED);
        }
        productList = Arrays.asList(response.getHits().getHits())
                .parallelStream()
                .map(hit -> Ints.tryParse(String.valueOf(hit.getSource().get(ProductBusiness.PRODUCT_ID))))
                .collect(Collectors.toList());
        // 返回的结果是查询到的商品Id列表
        return Response.success(StatusConstant.GENERAL_SUCCESS,
                new ProductSearchResult(response.getHits().totalHits, productList));
    }
}

package cn.abelib.shop.service.search;

import cn.abelib.shop.common.constant.BusinessConstant;
import cn.abelib.shop.common.constant.ProductBusiness;
import cn.abelib.shop.common.tools.JsonUtil;
import cn.abelib.shop.dao.CategoryDao;
import cn.abelib.shop.dao.ProductDao;
import cn.abelib.shop.pojo.Product;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by abel on 2018/4/9.
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

    @Override
    public void index(Integer productId) {
        Product product = productDao.selectById(productId);
        if (product == null){
            log.error("Index product {} does not exist!", productId);
            return;
        }
        ProductIndexTemplate productTemplate =assembleTemplate(product);
        if (productTemplate == null){
            log.error("Assemble product index {} failed!", productId);
            return;
        }
        if (esClient == null){
            log.debug("failed!");
            return;
        }
        SearchRequestBuilder requestBuilder = esClient.prepareSearch(BusinessConstant.Search.PRODUCT_SEARCH_INDEX)
                .setTypes(BusinessConstant.Search.PRODUCT_SEARCH_INDEX)
                .setQuery(QueryBuilders.termQuery(ProductBusiness.PRODUCT_ID, productId));
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
            log.error("Index product {} failed", productId);
        }
        log.debug("Index product {} success", productId);
    }

    /**
     *  组装ProductIndexTemplate
     * @param product
     * @return
     */
    private ProductIndexTemplate assembleTemplate(Product product){
        String categoryName = categoryDao.selectNameByCategoryId(product.getCategoryId());
        ProductIndexTemplate productTemplate = new ProductIndexTemplate();
        productTemplate.setProductId(product.getId());
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

    @Override
    public void remove(Integer productId) {

    }

    // 创建
    private boolean create(ProductIndexTemplate productIndexTemplate){
        IndexResponse indexResponse = esClient.prepareIndex(BusinessConstant.Search.PRODUCT_SEARCH_INDEX, BusinessConstant.Search.PRODUCT_SEARCH_TYPE)
                .setSource(JsonUtil.obj2Str(productIndexTemplate), XContentType.JSON)
                .get();
        log.debug("Create index with product：{}", productIndexTemplate.getProductId());
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
        log.debug("Update index with product：{}", productIndexTemplate.getProductId());
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
                .filter(QueryBuilders.termQuery(ProductBusiness.PRODUCT_ID, productIndexTemplate.getProductId()))
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
}

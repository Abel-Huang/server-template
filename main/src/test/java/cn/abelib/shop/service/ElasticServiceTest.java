package cn.abelib.shop.service;

import cn.abelib.shop.dao.ProductDao;
import cn.abelib.data.search.ProductSearchCondition;
import cn.abelib.data.search.ProductSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by abel on 2018/3/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticServiceTest {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductSearchService productSearchService;

    @Test
    public void testIndex(){
        productSearchService.syncIndex(2);
    }

    @Test
    public void testRemove(){
        productSearchService.syncRemove(1);
    }

    @Test
    public void testQuery(){
        ProductSearchCondition condition = new ProductSearchCondition();
        condition.setKeywords("beginner");
        List<Integer> productIds = productSearchService.query(condition).getBody().getProductIds();
        productIds.parallelStream().forEach(System.err::println);
    }
}

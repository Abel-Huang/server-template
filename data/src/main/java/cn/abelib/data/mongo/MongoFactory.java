package cn.abelib.data.mongo;

import cn.abelib.common.constant.StatusConstant;
import cn.abelib.common.exception.GlobalException;
import cn.abelib.shop.config.MongoConfig;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 *
 * @author abel
 * @date 2018/2/6
 *  MongoDB 的配置类
 */
@Service
public class MongoFactory {
    @Autowired
    private MongoConfig mongoConfig;

    /**
     *  MongoDB的工厂方法
     *
     */
    @Bean
    public MongoDatabase MongoDBFactory(){
        MongoDatabase mongoDatabase;
        try {
            MongoClient mongoClient = new MongoClient(mongoConfig.getHost(), mongoConfig.getPort());
            mongoDatabase = mongoClient.getDatabase(mongoConfig.getDb());
        }catch (Exception e){
            e.printStackTrace();
            throw new GlobalException(StatusConstant.GENERAL_SERVER_ERROR);
        }
        return mongoDatabase;
    }
}

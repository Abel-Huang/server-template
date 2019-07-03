package cn.abelib.st.core.data.mongo;

import cn.abelib.st.core.exception.GlobalException;
import cn.abelib.st.core.config.MongoConfig;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author abel
 * @date 2018/2/6
 *  MongoDB 的配置类
 */
public class MongoFactory {
    @Autowired
    private MongoConfig mongoConfig;

    /**
     *  MongoDB的工厂方法
     *
     */

    public MongoDatabase MongoDBFactory(){
        MongoDatabase mongoDatabase;
        try {
            MongoClient mongoClient = new MongoClient(mongoConfig.getHost(), mongoConfig.getPort());
            mongoDatabase = mongoClient.getDatabase(mongoConfig.getDb());
        }catch (Exception e){
            throw new GlobalException(null);
        }
        return mongoDatabase;
    }
}

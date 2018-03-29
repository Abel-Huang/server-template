package cn.abelib.shop.mongo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by abel on 2018/2/5.
 *  MongoDB配置类
 */
@Component
@ConfigurationProperties(prefix = "mongo")
public class MongoConfig {
    private String host;
    private int port;
    private String db;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }
}

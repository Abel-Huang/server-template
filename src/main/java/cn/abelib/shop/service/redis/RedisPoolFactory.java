package cn.abelib.shop.service.redis;

import cn.abelib.shop.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by abel on 2018/2/1.
 */
@Service
public class RedisPoolFactory {
    @Autowired
    private RedisConfig redisConfig;

    /**
     *  JedisPool 的工厂方法
     * @return
     */
    @Bean
    public JedisPool JedisPoolFactory(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        poolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
        // 注意这里是ms
        poolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait() * 1000);
        poolConfig.setTestOnBorrow(redisConfig.isTestOnBorrow());
        poolConfig.setTestOnReturn(redisConfig.isTestOnReturn());

        //Redis默认有16个数据库
        JedisPool  jedisPool = new JedisPool(poolConfig, redisConfig.getHost(), redisConfig.getPort(),
                redisConfig.getTimeout() * 1000, redisConfig.getPassword(), 0);
        return jedisPool;
    }
}

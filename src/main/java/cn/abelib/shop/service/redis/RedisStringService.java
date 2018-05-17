package cn.abelib.shop.service.redis;

import cn.abelib.shop.common.cache.KeyPrefix;
import cn.abelib.shop.common.tools.JsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by abel on 2018/1/31.
 *  对应Redis的String
 */
@Service
@Slf4j
public class RedisStringService {
    @Autowired
    private JedisPool jedisPool;

    /**
     * get
     * @param keyPrefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix keyPrefix, String key, Class<T> clazz){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 给key加上前缀
            String realKey = keyPrefix.getPrefix() + key;
            String str = jedis.get(realKey);
            T t = JsonUtil.str2Obj(str, clazz);
            log.info("get key:{} value:{}", key, t.toString());
            return t;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     *  不带前缀 并且不在内部进行序列化的get方法
     * @param key
     * @return
     */
    public String get(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String value = jedis.get(key);
            log.info("get key:{} value:{}", key, value);
            return value;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * set
     * @param keyPrefix
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix keyPrefix, String key, T t){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = JsonUtil.obj2Str(t);
            if (str == null || str.length() <= 0)
                return false;
            // 给key加上前缀
            String realKey = keyPrefix.getPrefix() + key;
            int seconds = keyPrefix.expire();
            // 判断是否需要设置过期时间
            if (seconds <= 0) {
                jedis.set(realKey, str);
            } else {
                jedis.setex(realKey, seconds, str);
            }
            log.info("set key:{} value:{}", key, t.toString());
            return true;
        }finally {
            returnResource(jedis);
        }
    }

    /**
     *  不带前缀 并且内部不进行序列化的set方法
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public boolean set(String key, int expire, String value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value))
                return false;
            // 判断是否需要设置过期时间
            if (expire <= 0) {
                jedis.set(key, value);
            } else {
                jedis.setex(key, expire, value);
            }
            log.info("set key:{} value:{}", key, value);
            return true;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * exists
     * @param keyPrefix
     * @param key
     * @return
     */
    public boolean exists(KeyPrefix keyPrefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 给key加上前缀
            String realKey = keyPrefix.getPrefix() + key;
            return this.exists(realKey);
        }finally {
            returnResource(jedis);
        }
    }

    /**
     * exists
     * @param key
     * @return
     */
    public boolean exists(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(key);
        }finally {
            returnResource(jedis);
        }
    }

    /**
     *  增加值
     * @param keyPrefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> long incr(KeyPrefix keyPrefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 给key加上前缀
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.incr(realKey);
        }finally {
            returnResource(jedis);
        }
    }

    public <T> long decr(KeyPrefix keyPrefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 给key加上前缀
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.decr(realKey);
        }finally {
            returnResource(jedis);
        }
    }

    /**
     *  给key修改生存时间
     * @param key
     * @return
     */
    public boolean expire(String key, int expire){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            long ret =  jedis.expire(key, expire);
            log.info("get key:{} result:{}", key, ret);
            return ret > 0;
        }finally {
            returnResource(jedis);
        }
    }


    /**
     *  删除
     * @param keyPrefix
     * @param key
     * @return
     */
    public boolean delete(KeyPrefix keyPrefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 给key加上前缀
            String realKey = keyPrefix.getPrefix() + key;
            long ret =  jedis.del(realKey);
            return ret > 0;
        }finally {
            returnResource(jedis);
        }
    }

    /**
     *  不带前缀的删除
     * @param key
     * @return
     */
    public boolean delete(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            long ret =  jedis.del(key);
            return ret > 0;
        }finally {
            returnResource(jedis);
        }
    }

    /**
     *  使用 Gson 字符串转为对象,
     *  todo 这两个方法将被JsonUtil代替，或者直接被移除
     * @param str
     * @return T
     */
    @SuppressWarnings("unchecked")
    private <T> T stringToBean(String str,  Class<T> clazz){
        if ( str == null || str.length() <= 0 || clazz == null)
            return null;
        if (clazz == int.class || clazz == Integer.class){
            return (T) Integer.valueOf(str);
        }else if (clazz == long.class || clazz == Long.class){
            return (T) Long.valueOf(str);
        }else if (clazz == String.class){
            return (T) str;
        }else {
            return new Gson().fromJson(str, clazz);
        }
    }

    /**
     *  使用 Gson 将对象转为字符串
     *  todo
     * @param t
     * @param <T>
     * @return String
     */
    private <T> String beanToString(T  t){
        if ( t == null)
            return null;
        Class<?> clazz = t.getClass();
        if (clazz == int.class || clazz == Integer.class){
            return t + "";
        }else if (clazz == long.class || clazz == Long.class){
            return t + "";
        }else if (clazz == String.class){
            return (String) t;
        }else {
            return new Gson().toJson(t);
        }
    }

    /**
     *  使用完将连接池归还给RedisPool
     * @param jedis
     */
    private void returnResource(Jedis jedis){
        if ( jedis != null){
            jedis.close();
        }
    }
}

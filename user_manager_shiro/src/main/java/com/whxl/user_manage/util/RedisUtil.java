package com.whxl.user_manage.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created By 朱立松 on 2019/1/16
 */
@Component
public class RedisUtil {

    //设置默认过期时间为2分钟
    private static final long DEFAULT_TIMEOUT = 2;

    @Autowired
    private RedisTemplate redisTemplate;

    public Boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 返回给定key的剩余生存时间，单位为秒
     * @param key
     * @return
     */
    public long ttl(String key){
        return redisTemplate.getExpire(key);
    }

    /**
     * expire设置过期时间，单位为秒
     * @param key
     * @param timeout
     */
    public void expire(String key, long timeout){
        redisTemplate.expire(key, timeout, TimeUnit.MINUTES);
    }

    /**
     * 设置令key增加对应数值
     * @param key
     * @param delta
     * @return
     */
    public long incre(String key, long delta){
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 根据给定模式，返回对应的key集合
     * @param patteren
     * @return
     */
    public Set<String> keys(String patteren){
        return redisTemplate.keys(patteren);
    }

    /**
     * 删除指定的key
     * @param key
     */
    public void del(String key){
        redisTemplate.delete(key);
    }

    /**
     * 为key设置一个新值，生存时间为默认生存时间
     * @param key
     * @param value
     */
    public void set(String key, Object value){
        redisTemplate.opsForValue().set(key, value, DEFAULT_TIMEOUT, TimeUnit.MINUTES);

    }

    /**d
     * 设置一个新的key，有生存时间
     * @param key
     * @param value
     * @param timeout
     */
    public void set(String key, String value, long timeout){
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MINUTES);
    }

    /**
     * 根据key得到value
     * @param key
     * @return
     */
    public Object get(String key){
        Object value = redisTemplate.opsForValue().get(key);
        if(value != null){
            //刷新过期时间
            redisTemplate.opsForValue().set(key, value);
        }
        return value;
    }

    /**
     * 将hash表中的key中的域field的值设为value
     * @param key
     * @param field
     * @param value
     */
    public void hset(String key, String field, Object value){
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 返回hash表中给定域的field的值
     * @param key
     * @param field
     * @return
     */
    public Object hget(String key, String field){
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * hash删除
     * @param key
     * @param fields
     */
    public void hdel(String key, Object ... fields){
        redisTemplate.opsForHash().delete(key, fields);
    }

    public Map<Object, Object> hgetAll(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 移除并返回列表中key的头元素
     * @param key
     * @return
     */
    public Object lpop(String key){
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 实现命令：RPush key value，将一个值value插入到列表key的表尾
     * @param key
     * @param value
     * @return
     */
    public long rpush(String key, Object value){
        return redisTemplate.opsForList().rightPush(key, value);
    }


    public static void main(String[] args) {
        RedisUtil redisUtil = new RedisUtil();
    }




}

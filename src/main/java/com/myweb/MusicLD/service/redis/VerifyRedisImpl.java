package com.myweb.MusicLD.service.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class VerifyRedisImpl extends BaseRedisServiceImpl<String,String,String> implements VerifyRedisService {

    @Value("${spring.application.security.jwt.refresh-token.expiration}")
    private long expiration;

    public VerifyRedisImpl(RedisTemplate<String, String> redisTemplate, HashOperations<String, String, String> hashOperations) {
        super(redisTemplate, hashOperations);
    }

    private String getKeyFrom(String userName) {
        return String.format("verify:%s", userName);
    }

    @Override
    public void clearByVerify(String userName) {
        String key = this.getKeyFrom(userName);
        super.delete(key);
    }

    @Override
    public String getVerify(String userName) {
        String key = this.getKeyFrom(userName);
        return super.get(key);
    }

    @Override
    public void saveVerify(String userName, String otp) {
        String key = this.getKeyFrom(userName);
        super.set(key, otp);
        super.setTimeToLive(key, expiration);
    }
}

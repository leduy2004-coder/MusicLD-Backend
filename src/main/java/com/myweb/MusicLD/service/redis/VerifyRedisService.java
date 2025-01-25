package com.myweb.MusicLD.service.redis;

public interface VerifyRedisService extends BaseRedisService<String,String,String>{
    void clearByVerify(String userName);
    String getVerify(String userName);
    void saveVerify(String userName, String otp);
}

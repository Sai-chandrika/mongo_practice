package com.example.mongo.mongo_practice.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author chandrika.g
 * user
 * @ProjectName security_practice_code
 * @since 30-10-2023
 */
@Service
public class RedisToken implements TokenBlacklistRepository{
    private final RedisTemplate<String,String> redisTemplate;
    private static final String BLACKLIST_KEY_PREFIX = "redis_token:";
    private static final long TOKEN_EXPIRATION_SECONDS = 86400;

    public RedisToken(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void addTokenToBlacklist(String user,String token) {
        redisTemplate.opsForValue().set(user + token, "blacklisted", TOKEN_EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }

    @Override
    public boolean isTokenBlacklisted(String user,String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(user + token));
    }

    @Override
    public void removeTokenFromBlacklist(String token) {
        redisTemplate.delete(token);
    }
}

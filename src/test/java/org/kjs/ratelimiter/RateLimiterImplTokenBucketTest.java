package org.kjs.ratelimiter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kjs.ratelimiter.algorithm.Limiter;
import org.kjs.ratelimiter.algorithm.tokenbucket.FixedTimeRefreshStrategy;
import org.kjs.ratelimiter.algorithm.tokenbucket.TokenBucket;
import org.kjs.ratelimiter.impl.RateLimiterImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
class RateLimiterImplTokenBucketTest {
    private RateLimiter rateLimiter;
    private Limiter limiter;
    @Autowired
    private FixedTimeRefreshStrategy fixedTimeRefreshStrategy;

    @BeforeEach
    void init() {
        this.limiter = new TokenBucket(new ConcurrentHashMap<>(), this.fixedTimeRefreshStrategy);
        this.rateLimiter = new RateLimiterImpl(this.limiter);
    }

    @Test
    void testIsAllowedNegative() {
        for (int i = 0; i < 10; i++) {
            this.rateLimiter.isAllowed("test");
        }
        Assertions.assertFalse(this.rateLimiter.isAllowed("test"));
    }

    @Test
    void testIsAllowed() {
        Assertions.assertTrue(this.rateLimiter.isAllowed("test"));
    }
}

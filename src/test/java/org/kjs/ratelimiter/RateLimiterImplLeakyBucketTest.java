package org.kjs.ratelimiter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kjs.ratelimiter.algorithm.Limiter;
import org.kjs.ratelimiter.algorithm.leakybucket.Consumer;
import org.kjs.ratelimiter.algorithm.leakybucket.LeakyBucketService;
import org.kjs.ratelimiter.impl.RateLimiterImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ConcurrentHashMap;

@ExtendWith(MockitoExtension.class)
class RateLimiterImplLeakyBucketTest {
    private RateLimiter rateLimiter;
    private Limiter limiter;
    @Mock
    private Consumer consumer;

    @BeforeEach
    void init() {
        this.limiter = new LeakyBucketService(consumer, new ConcurrentHashMap<>());
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

package org.kjs.ratelimiter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kjs.ratelimiter.algorithm.tokenbucket.Bucket;
import org.kjs.ratelimiter.algorithm.tokenbucket.FixedTimeRefreshStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: Karanjot Singh
 * User:karanjotsingh
 * Date:2024-01-01
 * Time:00:10
 */
@SpringBootTest
class FixedTimeRefreshStrategyTest {
    @Autowired
    private FixedTimeRefreshStrategy fixedTimeRefreshStrategy;

    @BeforeEach
    void init() {
    }

    @Test
    void testCheck() throws InterruptedException {
        this.fixedTimeRefreshStrategy.check("test", Bucket.builder().availableTokens(new AtomicInteger(10)).capacity(10).lastFilledOn(Instant.now()).build());
    }


    @Test
    void testCheckWithDuplicateEntry() throws InterruptedException {
        Bucket bucket = Bucket.builder().availableTokens(new AtomicInteger(10)).capacity(10).lastFilledOn(Instant.now()).build();
        this.fixedTimeRefreshStrategy.check("test", bucket);
        Thread.sleep(1000);
        this.fixedTimeRefreshStrategy.check("test", bucket);
    }

    @Test
    void testCheckWithException() throws InterruptedException {
        this.fixedTimeRefreshStrategy.check(null, null);
    }
}

package org.kjs.ratelimiter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kjs.ratelimiter.algorithm.tokenbucket.Bucket;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

@ExtendWith(MockitoExtension.class)
class BucketTest {
    private Bucket bucket;
    private Instant now;

    @BeforeEach
    public void init() {
        this.now = Instant.now();
        this.bucket = Bucket.builder().capacity(10).availableTokens(new AtomicInteger(10)).lastFilledOn(this.now).build();
    }

    @Test
    void testGetCapacity() {
        Assertions.assertEquals(10, this.bucket.getCapacity(), "Expected capacity is not equal");
    }

    @Test
    void testCanFulfillRequest() {
        Assertions.assertTrue(this.bucket.canFulfillRequest(), "This request should able to fulfill as we have enough tokens available");
    }

    @Test
    void testCanFulfillRequestWithZeroToken() {
        this.bucket = Bucket.builder().capacity(10).availableTokens(new AtomicInteger(0)).lastFilledOn(Instant.now()).build();
        Assertions.assertFalse(this.bucket.canFulfillRequest(), "We have no tokens available as per initialization");
    }

    @Test
    void testCanFulfillRequestWithNegativeToken() {
        this.bucket = Bucket.builder().capacity(10).availableTokens(new AtomicInteger(-10)).lastFilledOn(Instant.now()).build();
        Assertions.assertFalse(this.bucket.canFulfillRequest(), "We have negative token as per initialization");
    }

    @Test
    void testConsume() {
        Assertions.assertTrue(this.bucket.consume(), "We have enough token to consume from request");
        Assertions.assertEquals(9, this.bucket.getAvailableTokens().get(), "After the consumption we got one less token available");
    }


    @Test
    void testConsumeNegative() {
        this.bucket = Bucket.builder().capacity(10).availableTokens(new AtomicInteger(0)).lastFilledOn(Instant.now()).build();
        Assertions.assertFalse(this.bucket.consume(), "There are no token available for consumption");
    }

    @Test
    void testRefillTokenWhenItHasMaxCapacity() {
        this.bucket.refillToken(3);
        Assertions.assertEquals(10, this.bucket.getAvailableTokens().get(), "As adding 3 more tokens it should have count of 10");
    }


    @Test
    void testRefillToken() {
        for (int i = 0; i < 5; i++) {
            this.bucket.consume();
        }
        this.bucket.refillToken(3);
        Assertions.assertEquals(8, this.bucket.getAvailableTokens().get(), "As adding 3 more tokens after utilizing 5 it should have count of 8");
    }

    @Test
    void testLastRefillOn() {
        Assertions.assertEquals(this.now, this.bucket.getLastFilledOn(), "There should not be any changes in this object in getter");
    }

    @Test
    void testSetLastRefillOn() {
        this.now = Instant.now();
        this.bucket.setLastFilledOn(this.now);
        Assertions.assertEquals(this.now, this.bucket.getLastFilledOn(), "There should not be any changes in this object in setter");
    }

    @Test
    void testMultiRequestFlow() throws InterruptedException {
        this.bucket = Bucket.builder().capacity(100).availableTokens(new AtomicInteger(100)).lastFilledOn(Instant.now()).build();
        AtomicInteger countTrue = new AtomicInteger();
        AtomicInteger countFalse = new AtomicInteger();
        Thread[] threads = new Thread[10000];
        for (int i = 0; i < 10000; i++) {
            threads[i] = new Thread(() -> {
                boolean consumed = this.bucket.consume();
                if (consumed) {
                    countTrue.getAndIncrement();
                } else {
                    countFalse.getAndIncrement();
                }
            });
        }

        for (int i = 0; i < 10000; i++) {
            threads[i].start();
        }

        for (int i = 0; i < 10000; i++) {
            threads[i].join();
        }

        Assertions.assertEquals(0, this.bucket.getAvailableTokens().get(), "Even in multiple threads it should not have utilized more then token available");
        Assertions.assertEquals(10000 - 100, countFalse.get(), "only 100 times we should consume the request");
        Assertions.assertEquals(100, countTrue.get(), "only 100 times we should consume the request");
    }
}

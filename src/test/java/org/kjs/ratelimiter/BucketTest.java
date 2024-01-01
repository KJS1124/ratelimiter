package org.kjs.ratelimiter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kjs.ratelimiter.algorithm.Bucket;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

@ExtendWith(MockitoExtension.class)
class BucketTest {
    private Bucket bucket;

    @BeforeEach
    public void init() {
        this.bucket = Bucket.builder().capacity(10).availableTokens(10).lastFilledOn(Instant.now()).build();
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
        this.bucket = Bucket.builder().capacity(10).availableTokens(0).lastFilledOn(Instant.now()).build();
        Assertions.assertFalse(this.bucket.canFulfillRequest(), "We have no tokens available as per initialization");
    }

    @Test
    void testCanFulfillRequestWithNegativeToken() {
        this.bucket = Bucket.builder().capacity(10).availableTokens(-10).lastFilledOn(Instant.now()).build();
        Assertions.assertFalse(this.bucket.canFulfillRequest(), "We have negative token as per initialization");
    }

    @Test
    void testConsume() {
        Assertions.assertTrue(this.bucket.consume(), "We have enough token to consume from request");
        Assertions.assertEquals(9, this.bucket.getAvailableTokens(), "After the consumption we got one less token available");
    }


    @Test
    void testConsumeNegative() {
        this.bucket = Bucket.builder().capacity(10).availableTokens(0).lastFilledOn(Instant.now()).build();
        Assertions.assertFalse(this.bucket.consume(), "There are no token available for consumption");
    }
}

package org.kjs.ratelimiter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kjs.ratelimiter.impl.RateLimiterImpl;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RateLimiterImplTest {
    @Spy
    RateLimiter rateLimiter;

    @BeforeEach
    public void init() {
    }

    @Test
    public void isAllowedNegativeTest() {
        Assertions.assertEquals(false, this.rateLimiter.isAllowed("test"));
    }
}

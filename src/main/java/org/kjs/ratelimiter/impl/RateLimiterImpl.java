package org.kjs.ratelimiter.impl;

import lombok.RequiredArgsConstructor;
import org.kjs.ratelimiter.RateLimiter;
import org.kjs.ratelimiter.algorithm.Limiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterImpl implements RateLimiter {
    private final Limiter limiter;

    public RateLimiterImpl(@Autowired @Qualifier("tokenBucket")Limiter limiter) {
        this.limiter = limiter;
    }

    @Override
    public boolean isAllowed(String uniqueId) {
        return this.limiter.isAllowed(uniqueId);
    }
}

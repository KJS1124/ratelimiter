package org.kjs.ratelimiter.impl;

import lombok.RequiredArgsConstructor;
import org.kjs.ratelimiter.RateLimiter;
import org.kjs.ratelimiter.algorithm.Limiter;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RateLimiterImpl implements RateLimiter {
    private final Limiter limiter;

    @Override
    public boolean isAllowed(String uniqueId) {
        return this.limiter.isAllowed(uniqueId);
    }
}

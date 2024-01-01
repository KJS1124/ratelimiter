package org.kjs.ratelimiter;

public interface RateLimiter {
    boolean isAllowed(String uniqueId);
}

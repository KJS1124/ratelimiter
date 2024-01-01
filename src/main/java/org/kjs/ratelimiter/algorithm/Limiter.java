package org.kjs.ratelimiter.algorithm;

public interface Limiter {
    boolean isAllowed(String uniqueId);
}

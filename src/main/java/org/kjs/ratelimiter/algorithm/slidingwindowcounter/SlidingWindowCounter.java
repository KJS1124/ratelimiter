package org.kjs.ratelimiter.algorithm.slidingwindowcounter;

import lombok.RequiredArgsConstructor;
import org.kjs.ratelimiter.algorithm.Limiter;
import org.kjs.ratelimiter.algorithm.slidingwindowcounter.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Author: Karanjot Singh
 * User:karanjotsingh
 * Date:2024-01-12
 * Time:15:30
 */
@Service
@RequiredArgsConstructor
public class SlidingWindowCounter implements Limiter {
    private static final Integer MAX_LIMIT = 10;
    private static final Integer WINDOW_SIZE_IN_MILLI_SECONDS = 60 * 1000;
    @Qualifier("ConcurrentHashMap")
    private final Map<String, User> userMapping;

    @Override
    public boolean isAllowed(String uniqueId) {
        userMapping.computeIfAbsent(uniqueId, key ->
                User.builder().uniqueId(uniqueId).windows(new ConcurrentLinkedDeque<>()).allowedRequest(MAX_LIMIT).windowTime(WINDOW_SIZE_IN_MILLI_SECONDS).build()
        );
        return userMapping.get(uniqueId).consume();
    }
}

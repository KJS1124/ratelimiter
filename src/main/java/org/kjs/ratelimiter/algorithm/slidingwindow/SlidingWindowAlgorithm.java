package org.kjs.ratelimiter.algorithm.slidingwindow;

import lombok.RequiredArgsConstructor;
import org.kjs.ratelimiter.algorithm.Limiter;
import org.kjs.ratelimiter.algorithm.leakybucket.Bucket;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Author: Karanjot Singh
 * User:karanjotsingh
 * Date:2024-01-09
 * Time:16:52
 */
@RequiredArgsConstructor
public class SlidingWindowAlgorithm implements Limiter {
    private static final Integer MAX_LIMIT = 10;
    private static final Integer WINDOW_SIZE_IN_MILLI_SECONDS = 60 * 1000;
    @Qualifier("ConcurrentHashMap")
    private final Map<String, User> userMapping;

    @Override
    public boolean isAllowed(String uniqueId) {
        userMapping.computeIfAbsent(uniqueId, key ->
                User.builder().uniqueId(uniqueId).timeStampLogs(new ConcurrentLinkedQueue<Instant>()).maxLimit(MAX_LIMIT).windowTime(WINDOW_SIZE_IN_MILLI_SECONDS).build()
        );
        return userMapping.get(uniqueId).consume();
    }
}

package org.kjs.ratelimiter.algorithm.leakybucket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kjs.ratelimiter.algorithm.Limiter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Author: Karanjot Singh
 * User:karanjotsingh
 * Date:2024-01-01
 * Time:18:48
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LeakyBucketService implements Limiter {
    private static final Integer MAX_REQUEST_ALLOWED = 3;
    private static final Integer CONSUMPTION_RATE_PER_MIN = 30;
    private static final Integer CONSUMPTION_TIME_PER_MESSAGE = 60 / CONSUMPTION_RATE_PER_MIN;
    private final Consumer consumer;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Qualifier("ConcurrentHashMap")
    private final Map<String, Bucket> userBucketMapping;

    @Override
    public boolean isAllowed(String uniqueId) {
        userBucketMapping.computeIfAbsent(uniqueId, key -> {
            Bucket bucket = Bucket.builder().queue(new ConcurrentLinkedQueue<>()).uniqueId(uniqueId).maxSize(MAX_REQUEST_ALLOWED).build();
            this.addUserQueueToScheduler(bucket);
            return bucket;
        });

        return this.userBucketMapping.get(uniqueId).consume();
    }

    public boolean addUserQueueToScheduler(Bucket bucket) {
        try {
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                var request = bucket.getQueue().poll();
                this.consumer.handleRequest(request);
            }, CONSUMPTION_TIME_PER_MESSAGE, CONSUMPTION_TIME_PER_MESSAGE, TimeUnit.SECONDS);
            return true;
        } catch (Exception exception) {
            log.warn("Not able to schedule the leaky bucket scheduler for {}", bucket);
            return false;
        }
    }
}

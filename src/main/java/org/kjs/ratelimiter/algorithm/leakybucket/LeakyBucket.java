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
public class LeakyBucket implements Limiter {
    private static final Integer MAX_REQUEST_ALLOWED = 3;
    private static final Integer CONSUMPTION_RATE_PER_MIN = 30;
    private static final Integer CONSUMPTION_TIME_PER_MESSAGE = 60 / CONSUMPTION_RATE_PER_MIN;
    private final Consumer consumer;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Qualifier("ConcurrentHashMap")
    private final Map<String, Queue<Request>> userQueue;

    @Override
    public boolean isAllowed(String uniqueId) {
        userQueue.computeIfAbsent(uniqueId, key -> {
            Queue<Request> queue = new ConcurrentLinkedQueue<>();
            this.addUserQueueToScheduler(queue);
            return queue;
        });

        if (userQueue.get(uniqueId).size() <= MAX_REQUEST_ALLOWED) {
            userQueue.get(uniqueId).add(new Request(uniqueId));
            return true;
        }
        return false;
    }

    public boolean addUserQueueToScheduler(Queue<Request> queue) {
        try {
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                var request = queue.poll();
                this.consumer.handleRequest(request);
            }, CONSUMPTION_TIME_PER_MESSAGE, CONSUMPTION_TIME_PER_MESSAGE, TimeUnit.SECONDS);
            return true;
        } catch (Exception exception) {
            log.warn("Not able to schedule the leaky bucket scheduler for {}", queue);
            return false;
        }
    }
}

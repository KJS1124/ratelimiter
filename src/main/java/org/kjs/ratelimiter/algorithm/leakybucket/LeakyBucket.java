package org.kjs.ratelimiter.algorithm.leakybucket;

import lombok.RequiredArgsConstructor;
import org.kjs.ratelimiter.algorithm.Limiter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Author: Karanjot Singh
 * User:karanjotsingh
 * Date:2024-01-01
 * Time:18:48
 */
@Service
@RequiredArgsConstructor
public class LeakyBucket implements Limiter {
    private static final Integer MAX_REQUEST_ALLOWED = 3;

    @Qualifier("ConcurrentHashMap")
    private final Map<String, Queue<Request>> userQueue;
    @Override
    public boolean isAllowed(String uniqueId) {
        if(!userQueue.containsKey(uniqueId)) {
            userQueue.put(uniqueId, new ConcurrentLinkedQueue<>());
        }

        if(userQueue.get(uniqueId).size() <= MAX_REQUEST_ALLOWED) {
            userQueue.get(uniqueId).add(new Request(uniqueId));
            return true;
        }
        return false;
    }
}

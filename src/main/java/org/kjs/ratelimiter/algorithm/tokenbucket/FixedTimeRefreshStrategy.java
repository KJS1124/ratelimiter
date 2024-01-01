package org.kjs.ratelimiter.algorithm.tokenbucket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Author: Karanjot Singh
 * User:karanjotsingh
 * Date:2023-12-31
 * Time:18:55
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FixedTimeRefreshStrategy implements RefreshStrategy {
    @Qualifier("ConcurrentHashMap")
    private final Map<String, Bucket> userBuckets;
    @Value("${refreshStrategy.fixedTime.windowTime}")
    private Integer WINDOW_TIME_SECONDS;
    @Value("${refreshStrategy.tokenRatePerMinute}")
    private Integer TOKEN_PER_WINDOW;
    private ScheduledExecutorService executorService = Executors
            .newSingleThreadScheduledExecutor();

    @Override
    public boolean check(String uniqueId, Bucket bucket) {
        try {
            if (!this.userBuckets.containsKey(uniqueId)) {
                this.userBuckets.put(uniqueId, bucket);
                executorService.scheduleAtFixedRate(() -> {
                    bucket.refillToken(TOKEN_PER_WINDOW);
                    bucket.setLastFilledOn(Instant.now());
                }, WINDOW_TIME_SECONDS, WINDOW_TIME_SECONDS, TimeUnit.MILLISECONDS);
            } else {
                return false;
            }
        } catch (Exception exception) {
            log.warn("Getting exception while adding bucket {} to pool", uniqueId, exception);
            return false;
        }
        return true;
    }
}



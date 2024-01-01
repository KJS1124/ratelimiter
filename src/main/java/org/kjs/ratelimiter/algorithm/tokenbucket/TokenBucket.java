package org.kjs.ratelimiter.algorithm.tokenbucket;

import lombok.RequiredArgsConstructor;
import org.kjs.ratelimiter.algorithm.Limiter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class TokenBucket implements Limiter {
    private static final Integer LIMIT = 10;
    @Qualifier("ConcurrentHashMap")
    private final Map<String, Bucket> userBuckets;
    private final RefreshStrategy refreshStrategy;

    @Override
    public boolean isAllowed(String uniqueId) {
        this.userBuckets.computeIfAbsent(uniqueId, k ->
                Bucket.builder()
                        .availableTokens(new AtomicInteger(LIMIT))
                        .capacity(LIMIT)
                        .lastFilledOn(Instant.now())
                        .build()
        );
        this.refreshStrategy.check(uniqueId, this.userBuckets.get(uniqueId));
        return this.userBuckets.get(uniqueId).consume();
    }
}

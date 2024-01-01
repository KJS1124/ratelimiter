package org.kjs.ratelimiter.algorithm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenBucket implements Limiter{
    private static Integer LIMIT = 10;
    private static Integer WINDOW_TIME_SECONDS = 60 * 1000;
    private Map<String, Bucket> userBuckets;

    @Override
    public boolean isAllowed(String uniqueId) {
        if(!this.userBuckets.containsKey(uniqueId)) {
            this.userBuckets.put(uniqueId, Bucket.builder().availableTokens(LIMIT).capacity(LIMIT).lastFilledOn(Instant.now()).build());
        }
        return this.userBuckets.get(uniqueId).consume();
    }
}

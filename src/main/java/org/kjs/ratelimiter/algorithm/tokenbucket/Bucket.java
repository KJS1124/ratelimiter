package org.kjs.ratelimiter.algorithm.tokenbucket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: Karanjot Singh
 */
@Builder
@Getter
@AllArgsConstructor
public class Bucket {
    private int capacity;
    private AtomicInteger availableTokens;
    private Instant lastFilledOn;

    public boolean canFulfillRequest() {
        return availableTokens.get() > 0;
    }

    public synchronized boolean consume() {
        var currentToken = 0;
        do {
            currentToken = this.availableTokens.get();
            if (!this.canFulfillRequest()) {
                return false;
            }
        } while (!availableTokens.compareAndSet(currentToken, currentToken - 1));
        return true;
    }

    public void refillToken(int token) {
        if (this.getAvailableTokens().get() + token <= capacity) {
            this.setAvailableTokens(this.getAvailableTokens().get() + token);
        } else {
            this.setAvailableTokens(capacity);
        }
    }

    public void setLastFilledOn(Instant time) {
        this.lastFilledOn = time;
    }

    private void setAvailableTokens(int availableTokens) {
        this.availableTokens.set(availableTokens);
    }
}

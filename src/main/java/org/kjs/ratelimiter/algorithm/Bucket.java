package org.kjs.ratelimiter.algorithm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
/**
 * Author: Karanjot Singh
 */
@Builder
@Getter
@AllArgsConstructor
public class Bucket {
    private int capacity;
    private int availableTokens;
    private Instant lastFilledOn;

    public boolean canFulfillRequest() {
        return availableTokens > 0;
    }

    public boolean consume() {
        if (this.canFulfillRequest()) {
            this.setAvailableTokens(this.getAvailableTokens() - 1);
            return true;
        }
        return false;
    }

    private void setAvailableTokens(int availableTokens) {
        this.availableTokens = availableTokens;
    }
}

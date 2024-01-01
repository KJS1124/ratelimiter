package org.kjs.ratelimiter.algorithm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Builder
@Getter
@Setter
public class Bucket {
    private int capacity;
    private int availableTokens;
    private Instant lastFilledOn;

   public boolean canFulfillRequest() {
        if (availableTokens > 0) return true;
        return false;
    }

    public boolean consume() {
        if(this.canFulfillRequest()) {
            this.setAvailableTokens(this.getAvailableTokens() -1);
            return true;
        }
        return false;
    }
}

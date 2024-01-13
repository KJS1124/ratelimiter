package org.kjs.ratelimiter.algorithm.slidingwindow;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Queue;

/**
 * Author: Karanjot Singh
 * User:karanjotsingh
 * Date:2024-01-09
 * Time:16:53
 */
@Builder
@Getter
@Setter
public class User {
    private String uniqueId;
    private Queue<Instant> timeStampLogs;
    private int maxLimit;
    private int windowTime;

    private boolean removeFirstRequest() {
        if(timeStampLogs.size() > maxLimit && Instant.now().toEpochMilli() - timeStampLogs.peek().toEpochMilli() > windowTime) {
            timeStampLogs.poll();
            return true;
        }
        return false;
    }

    private void addEntryToTimeStamp() {
        this.timeStampLogs.add(Instant.now());
    }

    public boolean consume() {
        while(removeFirstRequest());

        this.addEntryToTimeStamp();
        if(this.getTimeStampLogs().size()<=maxLimit) {
            return true;
        }
        return false;
    }
}

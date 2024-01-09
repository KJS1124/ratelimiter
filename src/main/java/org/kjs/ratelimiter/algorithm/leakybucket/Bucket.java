package org.kjs.ratelimiter.algorithm.leakybucket;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Queue;

/**
 * Author: Karanjot Singh
 * User:karanjotsingh
 * Date:2024-01-07
 * Time:22:17
 */
@Builder
@Getter
@Setter
@ToString
public class Bucket {
    private String uniqueId;
    private Queue<Request> queue;
    private Integer maxSize;

    public boolean consume() {
        if (this.getQueue().size() <= maxSize) {
            this.getQueue().add(new Request(uniqueId));
            return true;
        }
        return false;
    }
}

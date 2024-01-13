package org.kjs.ratelimiter.algorithm.slidingwindowcounter;

import lombok.experimental.UtilityClass;

import java.time.Instant;

/**
 * Author: Karanjot Singh
 * User:karanjotsingh
 * Date:2024-01-12
 * Time:15:58
 */
@UtilityClass
public class RequestCalculator {
    public static int getRequest(Window left, Window right, long windowSize) {
        if(right == null) {
            return left.getCounter();
        }

        if(left == null) {
            return right.getCounter();
        }
        return (int) (right.getCounter() + (((Instant.now().toEpochMilli() - left.getWindowTime().toEpochMilli())/ windowSize) * left.getCounter()));
    }
}

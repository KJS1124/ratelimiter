package org.kjs.ratelimiter.algorithm.slidingwindowcounter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Author: Karanjot Singh
 * User:karanjotsingh
 * Date:2024-01-12
 * Time:15:31
 */
@Getter
@Setter
@Builder
public class Window {
    private int counter;
    private Instant windowTime;
}

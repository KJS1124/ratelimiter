package org.kjs.ratelimiter.algorithm.slidingwindowcounter;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.Deque;
import java.util.Queue;

/**
 * Author: Karanjot Singh
 * User:karanjotsingh
 * Date:2024-01-12
 * Time:15:53
 */
@Getter
@Setter
@SuperBuilder
public class User {
    private String uniqueId;
    private Deque<Window> windows;
    private long windowTime;
    private int allowedRequest;

    public synchronized boolean consume() {
        while (windows.size() > 2) {
            windows.pollFirst();
        }
        shouldAddWindow();
        Window left = windows.peekFirst();
        Window right = windows.size() > 1 ? windows.peekLast() : null;
        if (RequestCalculator.getRequest(left, right, windowTime) < this.getAllowedRequest()) {
            windows.peekLast().setCounter(windows.peekLast().getCounter() + 1);
            return true;
        }
        return false;
    }

    private void shouldAddWindow() {
        if(this.windows.size() == 0) {
            this.windows.add(new Window(0, Instant.now()));
        }

        if(Instant.now().toEpochMilli() - windows.peekLast().getWindowTime().toEpochMilli() >= windowTime) {
            windows.add(new Window(0, Instant.now()));
        }
    }
}

package org.kjs.ratelimiter.algorithm.leakybucket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Author: Karanjot Singh
 * User:karanjotsingh
 * Date:2024-01-02
 * Time:21:06
 */
@Service
@Slf4j
public class Consumer {
    public void handleRequest(Request request) {
        log.info("Consuming Request for user {}", request.getIdentifier());
    }
}

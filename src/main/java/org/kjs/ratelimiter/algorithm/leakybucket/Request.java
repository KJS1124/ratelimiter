package org.kjs.ratelimiter.algorithm.leakybucket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Author: Karanjot Singh
 * User:karanjotsingh
 * Date:2024-01-01
 * Time:18:55
 */
@AllArgsConstructor
@Getter
@Setter
public class Request {
    private String identifier;
}

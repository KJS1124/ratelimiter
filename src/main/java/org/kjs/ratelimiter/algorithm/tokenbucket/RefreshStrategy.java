package org.kjs.ratelimiter.algorithm.tokenbucket;

/**
 * Author: Karanjot Singh
 * User:karanjotsingh
 * Date:2023-12-31
 * Time:18:54
 */
public interface RefreshStrategy {
    boolean check(String uniqueId, Bucket bucket);
}

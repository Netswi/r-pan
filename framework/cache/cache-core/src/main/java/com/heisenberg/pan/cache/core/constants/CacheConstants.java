package com.heisenberg.pan.cache.core.constants;

/**
 * 服务端公用缓存常量
 */
public interface CacheConstants {

    /**
     * 公用的缓存name
     * 由于该缓存框架大部分复用Spring的Cache模块，所以使用统一的缓存名称
     * 该框架不支持多缓存的使用，只支持多缓存的使用
     */
    String R_PAN_CACHE_NAME = "R_PAN_CACHE";


}

package com.heisenberg.pan.cache.caffeine.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "com.heisenberg.pan.cache.caffeine")
public class CaffeineCacheProperties {
    /**
     * 初始容量
     */
    private Integer initCacheCapacity = 256;
    /**
     * 缓存最大容量，超过之后会按照recently or very often（最近最少）策略做缓存剔除
     */
    private Long maxCacheCapacity = 10000L;

    /**
     * 是否允许空值null作为缓存的value
     */
    private Boolean allowNullValue = Boolean.TRUE;
}

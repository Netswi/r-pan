package com.heisenberg.pan.server.common.utils;

import com.heisenberg.pan.core.constants.RPanConstants;

import java.util.Objects;

/**
 * 用户id存储工具类
 */
public class UserIdUtil {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程的userid
     * @param userId
     */
    public static void set(Long userId){
        threadLocal.set(userId);
    }
    /**
     * 获取当前线程的userid
     *
     */
    public static Long get(){
        Long userId = threadLocal.get();
        if (Objects.isNull(userId)){
            return RPanConstants.ZERO_LONG;
        }
        return userId;
    }
}

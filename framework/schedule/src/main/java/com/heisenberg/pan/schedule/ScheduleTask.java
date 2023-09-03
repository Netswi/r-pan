package com.heisenberg.pan.schedule;

/**
 * 自定义业务逻辑执行器接口
 */
public interface ScheduleTask extends Runnable{

    /**
     * 获取执行器名称
     *
     * @return
     */
    String getName();
}

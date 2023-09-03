package com.heisenberg.pan.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.concurrent.ScheduledFuture;

/**
 * 任务执行器 开发任务结果保存实体 定时任务和结果的缓存对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleTaskHolder implements Serializable {
    private ScheduleTask scheduleTask;

    private ScheduledFuture scheduledFuture;

    private static final long serialVersionUID = 1444488140009722221L;
}

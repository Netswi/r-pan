package com.heisenberg.pan.schedule;

import com.heisenberg.pan.core.exception.RPanException;
import com.heisenberg.pan.core.exception.RPanFrameWorkException;
import com.heisenberg.pan.core.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
@Slf4j
public class ScheduleManager {
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    /**
     * 内部正在执行的定时任务缓存
     */
    private Map<String, ScheduleTaskHolder> cache = new ConcurrentHashMap<>();
    /**
     * 启动定时任务
     *
     * @param scheduleTask
     * @param cron
     */
    public String startTask(ScheduleTask scheduleTask,String cron){
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.schedule(scheduleTask, new CronTrigger(cron));
        String key = UUIDUtil.getUUID();
        ScheduleTaskHolder scheduleTaskHolder = new ScheduleTaskHolder(scheduleTask, scheduledFuture);
        cache.put(key, scheduleTaskHolder);
        log.info("{}启动成功！唯一标识为: {}", scheduleTask.getName(), key);
        return key;
    }
    /**
     * 停止定时任务
     *
     * @param key
     */
    public void stopTask(String key){
        if (StringUtils.isBlank(key)){
            return;
        }
        ScheduleTaskHolder cacheValue = cache.get(key);
        if (Objects.isNull(cacheValue)){
            return;
        }
        if (cacheValue instanceof ScheduleTaskHolder) {
            (cacheValue).getScheduledFuture().cancel(true);
            log.info("{}停止成功！唯一标识为: {}", (cacheValue).getScheduleTask().getName(), key);
        }
    }

    /**
     * 更改定时任务
     * @param key
     * @param cron
     * @return
     */
    public String changeTask(String key,String cron){
        if (StringUtils.isBlank(key)){
            throw new RPanFrameWorkException("标识不能为空");
        }
        ScheduleTaskHolder cacheValue = cache.get(key);
        if (Objects.isNull(cacheValue)){
            throw new RPanFrameWorkException(key + "标识不存在");
        }
        if (cacheValue instanceof ScheduleTaskHolder) {
            stopTask(key);
            return startTask( cacheValue.getScheduleTask(), cron);
        }
        throw new RPanFrameWorkException(key + "标识不存在");
    }

}

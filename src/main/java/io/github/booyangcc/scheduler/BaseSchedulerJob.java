package io.github.booyangcc.scheduler;

import io.github.booyangcc.scheduler.config.SchedulerJobConfig;
import org.quartz.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: 杨勃
 * @Date: 2024/5/21 09:46
 * @Description: BaseSchedulerJob 基础定时任务，所有定时任务需集成该类，并通过config自动注入到bean
 */
public abstract class BaseSchedulerJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(BaseSchedulerJob.class);

    /**
     * 定时任务的配置
     */
    private SchedulerJobConfig schedulerJobConfig;

    public BaseSchedulerJob(SchedulerJobConfig schedulerJobConfig) {
        this.schedulerJobConfig = schedulerJobConfig;
    }

    public SchedulerJobConfig getSchedulerJobConfig() {
        return schedulerJobConfig;
    }

    public void setSchedulerJobConfig(SchedulerJobConfig schedulerJobConfig) {
        this.schedulerJobConfig = schedulerJobConfig;
    }

    public void setSchedulerJobConfigCron(String cron) {
        if (this.schedulerJobConfig == null) {
            return;
        }
        this.schedulerJobConfig.setCronExpression(cron);
    }
}

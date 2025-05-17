package io.github.booyangcc.scheduler;

import io.github.booyangcc.scheduler.config.SchedulerJobConfig;
import io.github.booyangcc.scheduler.constant.SchedulerActionEnum;
import io.github.booyangcc.scheduler.vo.Res;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.TimeZone;

import static java.lang.System.exit;

/**
 * @Author: 杨勃
 * @Date: 2024/5/17 14:49
 * @Description:
 */


public class SchedulerManager {
    private static final Logger log = LoggerFactory.getLogger(SchedulerManager.class);

    /**
     * 调度器
     */
    private final Scheduler scheduler;
    /**
     * 调度任务
     */
    private List<BaseSchedulerJob> schedulerJobs;

    public SchedulerManager(Scheduler scheduler, List<BaseSchedulerJob> schedulerJobs) {
        this.scheduler = scheduler;
        this.schedulerJobs = schedulerJobs;
    }

    /**
     * 在所有bean初始化完成以后调用，获取注入基于BaseSchedulerJob的所有bean 初始化定时任务
     *
     * @param event ContextRefreshedEvent
     */
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("schedulerManager schedule job start!");
        schedulerJobs.forEach(schedulerJob -> {
            SchedulerJobConfig schedulerJobConfig = schedulerJob.getSchedulerJobConfig();
            try {
                scheduleJob(schedulerJobConfig);
            } catch (Exception e) {
                if (schedulerJobConfig != null && schedulerJobConfig.getIsErrExit()) {
                    log.error("scheduler job init err, exit! jobL: {}", schedulerJobConfig, e);
                    exit(1);
                } else {
                    log.error("scheduler job init err, skip! jobL: {}", schedulerJobConfig, e);
                }
            }
        });
        log.info("schedulerManager schedule job end!");
    }

    /**
     * 调度任务
     *
     * @param schedulerJobConfig quartz注册任务和Trigger
     * @throws SchedulerException 调度错误
     * @throws ClassNotFoundException 注册的类没找到
     */
    public void scheduleJob(SchedulerJobConfig schedulerJobConfig) throws SchedulerException, ClassNotFoundException {
        String jobName = schedulerJobConfig.getJobName();
        String jobGroup = schedulerJobConfig.getJobGroup();
        String jobCronExpression = schedulerJobConfig.getCronExpression();

        if (scheduler.checkExists(new JobKey(jobName, jobGroup))) {
            rescheduleJob(schedulerJobConfig);
            return;
        }

        Class<? extends Job> qtzJobClass = schedulerJobConfig.getJobClass();
        if (qtzJobClass == null) {
            Class<?> jobClass = Class.forName(schedulerJobConfig.getJobClassName());
            if (!Job.class.isAssignableFrom(jobClass)) {
                throw new SchedulerException("Job class [" + schedulerJobConfig.getJobClassName()
                    + "] is not a subclass of" + " org.quartz.Job");
            }
            qtzJobClass = jobClass.asSubclass(Job.class);
        }

        JobDetail jobDetail = JobBuilder.newJob(qtzJobClass).withIdentity(jobName, jobGroup).build();
        Trigger trigger =
            TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
                .withSchedule(CronScheduleBuilder.cronSchedule(jobCronExpression)
                    .withMisfireHandlingInstructionDoNothing().inTimeZone(TimeZone.getTimeZone("Asia/Shanghai")))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
        log.info("schedule job {} started.", schedulerJobConfig);
    }

    /**
     * 重新调度任务
     *
     * @param schedulerJobConfig 任务配置
     */
    public Res<Void> rescheduleJob(SchedulerJobConfig schedulerJobConfig) {
        String jobName = schedulerJobConfig.getJobName();
        String jobGroup = schedulerJobConfig.getJobGroup();
        String jobCronExpression = schedulerJobConfig.getCronExpression();

        // 暂停任务
        try {
            operate(jobName, jobGroup, SchedulerActionEnum.PAUSE_ACTION.getCode());
        }catch (Exception e) {
            return Res.error(e);
        }
        try {
            log.warn("job {}.{} already exists, updating its schedule...", jobGroup, jobName);
            TriggerKey triggerKey = new TriggerKey(jobName, jobGroup);
            Trigger oldTrigger = scheduler.getTrigger(triggerKey);
            if (oldTrigger == null) {
                log.warn("job {}.{} currentTrigger not exist", jobGroup, jobName);
                return Res.error("job " + jobName + " currentTrigger not exist");
            }

            Trigger newTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
                .withSchedule(
                    CronScheduleBuilder.cronSchedule(jobCronExpression).withMisfireHandlingInstructionDoNothing())
                .startAt(oldTrigger.getStartTime()).endAt(oldTrigger.getEndTime()).build();
            // 重新安排任务
            scheduler.rescheduleJob(oldTrigger.getKey(), newTrigger);
            log.info("updated schedule for job {}.{}", jobGroup, jobName);
        } catch (SchedulerException e) {
            log.error("job {} rescheduleJob error", schedulerJobConfig, e);
            return Res.error(e);
        }

        // 恢复任务
        try {
            operate(jobName, jobGroup, SchedulerActionEnum.RESUME_ACTION.getCode());
        }catch (Exception e) {
            return Res.error(e);
        }
        return Res.success();
    }

    /**
     * 暂停任务
     *
     * @param jobName 任务名称
     * @param jobGroup 任务分组
     * @param action 操作
     * @return 暂停结果
     */
    public boolean operate(String jobName, String jobGroup, String action) throws Exception {
        log.info("operate job {}.{} action: {} ", jobName, jobGroup, action);

        try {
            if (!scheduler.checkExists(new JobKey(jobName, jobGroup))) {
                log.error("job {}.{} does not exist", jobName, jobGroup);
                throw new Exception("job " + jobGroup + "." + jobName + " does not exist: ");
            }

            JobKey jobKey = new JobKey(jobName, jobGroup);
            switch (SchedulerActionEnum.fromCode(action)) {
                case PAUSE_ACTION:
                    scheduler.pauseJob(jobKey);
                    break;
                case RESUME_ACTION:
                    scheduler.resumeJob(jobKey);
                    break;
                case DELETE_ACTION:
                    scheduler.deleteJob(jobKey);
                    break;
                default:
                    throw new Exception(action + " not supported; pause, resume, delete supported");
            }
            return true;
        } catch (SchedulerException e) {
            log.info("operate job {}.{} action: {} failed", jobName, jobGroup, action, e);
            throw new Exception("scheduler operate error", e);
        }
    }
}

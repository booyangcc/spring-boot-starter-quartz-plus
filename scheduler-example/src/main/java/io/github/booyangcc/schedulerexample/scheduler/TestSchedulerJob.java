package io.github.booyangcc.schedulerexample.scheduler;

import io.github.booyangcc.scheduler.BaseSchedulerJob;
import io.github.booyangcc.scheduler.config.SchedulerJobConfig;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * @Author: 杨勃
 * @Date: 2025/5/17 14:58
 * @Description:
 */
@Component
public class TestSchedulerJob extends BaseSchedulerJob {
    // SchedulerJobConfig 配置定时任务执行时间以及执行类
    public TestSchedulerJob() {
        // 通过jobName， jobGroup唯一确定一个任务
        super(new SchedulerJobConfig("testJob", "testGroup", "*/10 * * * * ?", TestSchedulerJob.class));
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 业务代码
        System.out.println(" job executed");
    }

}
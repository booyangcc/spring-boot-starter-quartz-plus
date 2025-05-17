package io.github.booyangcc.scheduler.lister;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @Author: 杨勃
 * @Date: 2024/10/31 13:52
 * @Description: 日志lister添加日志tracnceId
 */
public class LogJobLister implements JobListener {
    private static final Logger log = LoggerFactory.getLogger(LogJobLister.class);

    @Override
    public String getName() {
        return "logJobLister";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        MDC.put("trace_id", UUID.randomUUID().toString().replaceAll("-", ""));
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {

    }
}

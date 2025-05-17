package io.github.booyangcc.scheduler.config;

import org.quartz.Job;

/**
 * @Author: 杨勃
 * @Date: 2024/5/17 16:36
 * @Description: 调度任务配置文件
 */

public class SchedulerJobConfig {

    /**
     * 调度任务名称
     */
    private String jobName;

    /**
     * 任务分组
     */
    private String jobGroup;

    /**
     * cron 表达式
     */
    private String cronExpression;

    /**
     * 加载的名称
     */
    private String jobClassName;

    /**
     * 加载的类
     */
    private Class<? extends Job> jobClass;


    /**
     * 调度任务错误是否退出
     */
    private Boolean isErrExit = true;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public Class<? extends Job> getJobClass() {
        return jobClass;
    }

    public void setJobClass(Class<? extends Job> jobClass) {
        this.jobClass = jobClass;
    }

    public Boolean getIsErrExit() {
        return isErrExit;
    }

    public void setIsErrExit(Boolean errExit) {
        isErrExit = errExit;
    }


    public SchedulerJobConfig(String jobName, String jobGroup, String cronExpression,
                              Class<? extends Job> jobClass) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.cronExpression = cronExpression;
        this.jobClass = jobClass;
    }

    public SchedulerJobConfig(String jobName, String jobGroup, String cronExpression,
                              String jobClassName) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.cronExpression = cronExpression;
        this.jobClassName = jobClassName;
    }

    @Override
    public String toString() {
        return "SchedulerJobConfig{" + "job ='" + jobGroup + "." + jobName + '\'' + ", cronExpression='"
                + cronExpression + '\'' + ", jobClassName='" + jobClassName + '\'' + ", jobClass=" + jobClass
                + ", isErrExit=" + isErrExit + '}';
    }
}

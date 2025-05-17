package io.github.booyangcc.scheduler.vo;

import jakarta.validation.constraints.NotEmpty;

/**
 * @Author: 杨勃
 * @Date: 2024/5/21 10:57
 * @Description:
 */
public class OperateJobVO {
    /**
     * 任务名称
     */
    @NotEmpty(message = "jobName must not be empty")
    private String jobName;
    /**
     * 任务分组
     */
    @NotEmpty(message = "jobGroup must not be empty")
    private String jobGroup;
    /**
     * 任务动作
     */

    @NotEmpty(message = "action must not be empty")
    private String action;

    public OperateJobVO() {}

    public OperateJobVO(String jobName, String action, String jobGroup) {
        this.jobName = jobName;
        this.action = action;
        this.jobGroup = jobGroup;
    }

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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}

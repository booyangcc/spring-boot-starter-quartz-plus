package io.github.booyangcc.scheduler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * # 定时器名称 org.quartz.scheduler.instanceName quartz: instanceName: quartzScheduler instanceId: AUTO # 实例id
 * jobStoreClass: org.springframework.scheduling.quartz.LocalDataSourceJobStore # JobStore的实现类，用于持久化任务和触发器。
 * clusterCheckinInterval: 2000 # 集群检查间隔（毫秒） isClustered: true # 是否集群。 threadPoolClass:
 * org.quartz.simpl.SimpleThreadPool # 线程池实现类 threadCount: 5 # 线程池中的线程数量 threadPriority: 5 # 线程优先级
 * overwriteExistingJobs： false
 *
 * @Author: 杨勃
 * @Date: 2024/5/10 09:44
 * @Description: RestTemplate配置类
 */

@Component
@ConfigurationProperties(prefix = "quartz.plus")
public class SchedulerProperties {

    /**
     * 定时器名称 org.quartz.scheduler.instanceName
     */
    @Value("${spring.application.name}-instance")
    private String instanceName;
    /**
     * 实例id org.quartz.scheduler.instanceId
     */
    private String instanceId = "AUTO";
    /**
     * JobStore的实现类，用于持久化任务和触发器。 org.quartz.jobStore.class
     */
    private String jobStoreClass = "org.springframework.scheduling.quartz.LocalDataSourceJobStore";
    /**
     * 集群检查间隔（毫秒）。 org.quartz.jobStore.clusterCheckinInterval
     */
    private String clusterCheckinInterval = "10000";
    /**
     * 是否集群。 org.quartz.jobStore.isClustered
     */
    private String isClustered = "true";
    /**
     * 线程池实现类 org.quartz.threadPool.class
     */
    private String threadPoolClass = "org.quartz.simpl.SimpleThreadPool";
    /**
     * 线程池中的线程数量 org.quartz.threadPool.threadCount
     */
    private String threadCount = "5";
    /**
     * 线程优先级 org.quartz.threadPool.threadPriority
     */
    private String threadPriority = "5";
    /**
     * 覆盖已存在任务
     */
    private Boolean overwriteExistingJobs = false;

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getJobStoreClass() {
        return jobStoreClass;
    }

    public void setJobStoreClass(String jobStoreClass) {
        this.jobStoreClass = jobStoreClass;
    }

    public String getClusterCheckinInterval() {
        return clusterCheckinInterval;
    }

    public void setClusterCheckinInterval(String clusterCheckinInterval) {
        this.clusterCheckinInterval = clusterCheckinInterval;
    }

    public String getIsClustered() {
        return isClustered;
    }

    public void setIsClustered(String isClustered) {
        this.isClustered = isClustered;
    }

    public String getThreadPoolClass() {
        return threadPoolClass;
    }

    public void setThreadPoolClass(String threadPoolClass) {
        this.threadPoolClass = threadPoolClass;
    }

    public String getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(String threadCount) {
        this.threadCount = threadCount;
    }

    public String getThreadPriority() {
        return threadPriority;
    }

    public void setThreadPriority(String threadPriority) {
        this.threadPriority = threadPriority;
    }

    public Boolean getOverwriteExistingJobs() {
        return overwriteExistingJobs;
    }

    public void setOverwriteExistingJobs(Boolean overwriteExistingJobs) {
        this.overwriteExistingJobs = overwriteExistingJobs;
    }
}

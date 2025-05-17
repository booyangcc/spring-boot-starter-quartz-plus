package io.github.booyangcc.scheduler.config;


import io.github.booyangcc.scheduler.BaseSchedulerJob;
import io.github.booyangcc.scheduler.SchedulerManager;
import io.github.booyangcc.scheduler.controller.SchedulerController;
import io.github.booyangcc.scheduler.lister.LogJobLister;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

/**
 * @Author: 杨勃
 * @Date: 2024/5/21 11:11
 * @Description:
 */
@Configuration
@EnableConfigurationProperties({SchedulerProperties.class})
public class SchedulerConfig {
    private static final Logger log = LoggerFactory.getLogger(SchedulerConfig.class);

    /**
     * 定时任务配置
     */
    private final SchedulerProperties schedulerProperties;

    @Autowired
    public SchedulerConfig(SchedulerProperties schedulerProperties) {
        this.schedulerProperties = schedulerProperties;
    }

    private transient AutowireCapableBeanFactory beanFactory;


    @Bean
    public SchedulerManager newSchedulerManager(Scheduler scheduler, List<BaseSchedulerJob> schedulerJobs) throws Exception {
        addListeners(scheduler);
        return new SchedulerManager(scheduler, schedulerJobs);
    }

    @Bean
    public SchedulerController newSchedulerController(SchedulerManager schedulerManager) {
        return new SchedulerController(schedulerManager);
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(new SpringBeanJobFactory());
        factory.setDataSource(dataSource);
        factory.setQuartzProperties(newProperties());
        factory.setApplicationContextSchedulerContextKey("applicationContext");
        factory.setOverwriteExistingJobs(schedulerProperties.getOverwriteExistingJobs());
        factory.setStartupDelay(10);
        return factory;
    }

    public Properties newProperties() {
        Properties props = new Properties();
        props.put("org.quartz.scheduler.instanceName", schedulerProperties.getInstanceName());
        props.put("org.quartz.scheduler.instanceId", schedulerProperties.getInstanceId());

        props.put("org.quartz.jobStore.class", schedulerProperties.getJobStoreClass());
        props.put("org.quartz.jobStore.clusterCheckinInterval", schedulerProperties.getClusterCheckinInterval());
        props.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
        props.put("org.quartz.jobStore.isClustered", schedulerProperties.getIsClustered());

        props.put("org.quartz.threadPool.class", schedulerProperties.getThreadPoolClass());
        props.put("org.quartz.threadPool.threadCount", schedulerProperties.getThreadCount());
        props.put("org.quartz.threadPool.threadPriority", schedulerProperties.getThreadPriority());
        return props;
    }

    /**
     * 添加定时任务lister
     *
     * @param scheduler 调度器
     */
    private void addListeners(Scheduler scheduler) throws Exception {
        try {
            scheduler.getListenerManager().addJobListener(new LogJobLister());
            log.info("添加定时任务日志成功");
        } catch (Exception e) {
            throw new Exception("scheduler add lister error", e);
        }
    }
}

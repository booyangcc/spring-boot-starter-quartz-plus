# spring-boot-starter-quartz-plus

一款对quartz进行封装的轻量级的分布式定时任务组件

## 引入

```xml

<dependency>
    <groupId>io.github.booyangcc</groupId>
    <artifactId>spring-boot-starter-quartz-plus</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## 配置

1、不做任何配置直接启动首次启动需要配置
```yaml
spring:
  # 用来初始化表 初始化的时候需要，其他时候可以删掉
  quartz:
    job-store-type: jdbc
    jdbc:
      initialize-schema: always
```
2、自定义配置，覆盖默认配置
```yaml
spring:
  # 用来初始化表 初始化的时候需要，其他不需要
  quartz:
    job-store-type: jdbc
    jdbc:
      initialize-schema: always
#  默认配置
    plus:
        instanceName: quartzScheduler 
        instanceId: AUTO # 实例id
        jobStoreClass: org.springframework.scheduling.quartz.LocalDataSourceJobStore # JobStore的实现类，用于持久化任务和触发器。
        clusterCheckinInterval: 2000 # 集群检查间隔（毫秒）
        isClustered: true # 是否集群。
        threadPoolClass: org.quartz.simpl.SimpleThreadPool # 线程池实现类
        threadCount: 5 # 线程池中的线程数量
        threadPriority: 5    # 线程优先级
        overwriteExistingJobs: false  # 是否覆盖已存在任务
```

## 使用

### 创建定时任务

所有任务必须继承 `io.github.booyangcc.scheduler.BaseSchedulerJob`，并自动注入到Spring即可
范例：

```java
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
```

## api调用

### 远程部署任务

```http
POST {{host}}/scheduler/schedule
Content-Type: application/json

{
  "jobName": "testJob",
  "jobGroup": "testGroup",
  "jobClassName": "com.github.booyangcc.example.scheduler.TestSchedulerJob",
  "cronExpression": "*/10 * * * * ?"
}
```

### 远程更改部署配置

比如更改执行间隔

```http
POST {{host}}/scheduler/reschedule
Content-Type: application/json

{
  "jobName": "testJob",
  "jobGroup": "testGroup",
  "cronExpression": "*/30 * * * * ?"
}
```

### 操作任务

`action` 有 `pause`停止，`delete`删除，`resume`重启三种
比如暂停

```http
POST {{host}}/scheduler/operate
Content-Type: application/json

{
  "jobName": "testJob",
  "jobGroup": "testGroup",
"action": "pause"
}
```

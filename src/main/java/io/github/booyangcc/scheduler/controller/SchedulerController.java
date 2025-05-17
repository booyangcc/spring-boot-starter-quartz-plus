package io.github.booyangcc.scheduler.controller;

import io.github.booyangcc.scheduler.SchedulerManager;
import io.github.booyangcc.scheduler.config.SchedulerJobConfig;
import io.github.booyangcc.scheduler.vo.OperateJobVO;
import io.github.booyangcc.scheduler.vo.Res;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: 杨勃
 * @Date: 2024/5/17 14:59
 * @Description:
 */

@RestController
@RequestMapping("/scheduler")
@Tag(name = "定时任务调度接口")
public class SchedulerController {
    private static final Logger log = LoggerFactory.getLogger(SchedulerController.class);
    /**
     * 调度管理器
     */
    private final SchedulerManager schedulerManager;

    @Autowired
    public SchedulerController(SchedulerManager schedulerManager) {
        this.schedulerManager = schedulerManager;
    }

    @Operation(summary = "调度定时任务")
    @PostMapping("/schedule")
    public Res<Void> scheduleJob(@RequestBody SchedulerJobConfig schedulerJobConfig) {
       try {
           schedulerManager.scheduleJob(schedulerJobConfig);
       }catch (Exception e) {
           log.error("scheduleJob error:", e);
           return Res.error(e.getMessage());
       }
        return Res.success();
    }

    @Operation(summary = "重新调度定时任务")
    @PostMapping("/reschedule")
    public Res<Void> rescheduleJob(@RequestBody SchedulerJobConfig schedulerJobConfig) {
        return schedulerManager.rescheduleJob(schedulerJobConfig);
    }

    @PostMapping("/operate")
    @Operation(summary = "操作任务，有pause停止，delete删除，resume重启三种")
    public Res<Void> operateJob(@RequestBody OperateJobVO operateJobVO) {
        try {
            Boolean isSuccess = schedulerManager.operate(operateJobVO.getJobName(), operateJobVO.getJobGroup(), operateJobVO.getAction());

            log.info("operate job {} result:{}", operateJobVO, isSuccess);
            if (!isSuccess) {
                return Res.error("操作失败");
            }
        }catch (Exception e) {
            return Res.error(e.getMessage());
        }

        return Res.success();
    }
}

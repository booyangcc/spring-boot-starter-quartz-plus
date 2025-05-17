package io.github.booyangcc.scheduler.constant;

/**
 * @Author: 杨勃
 * @Date: 2024/5/17 15:31
 * @Description: 定时任务操作类型
 */

public enum SchedulerActionEnum {
    /**
     * 暂停
     */
    PAUSE_ACTION("pause", "停止任务"),
    /**
     * 恢复
     */
    RESUME_ACTION("resume", "恢复任务"),
    /**
     * 删除
     */
    DELETE_ACTION("delete", "删除任务"),
    ;

    /**
     * 操作类型
     */
    private final String code;
    /**
     * 操作名称
     */
    private final String name;

    SchedulerActionEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static SchedulerActionEnum fromCode(String code) throws Exception {
        for (SchedulerActionEnum action : values()) {
            if (action.getCode().equalsIgnoreCase(code)) {
                return action;
            }
        }
        throw new Exception("invalid operate");
    }
}

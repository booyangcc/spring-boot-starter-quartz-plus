package io.github.booyangcc.scheduler.vo;

/**
 * @Author: 杨勃
 * @Date: 2025/5/17 10:57
 * @Description:
 * @param <T>
 */

public class Res<T> {
    private int code;
    private String message;
    private T data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Res(int code, String message, T data) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static <T> Res<T> success() {
        return new Res<>(200, "success", null);
    }

    public static <T> Res<T> success(T data) {
        return new Res<>(200, "success", data);
    }

    public static <T> Res<T> success(String message, T data) {
        return new Res<>(200, message, data);
    }

    public static <T> Res<T> error(int code, String message) {
        return new Res<>(code, message, null);
    }

    public static <T> Res<T> error(String message) {
        return new Res<>(500, message, null);
    }

    public static <T> Res<T> error(Exception e) {
        return new Res<>(500, e.getMessage(), null);
    }
}
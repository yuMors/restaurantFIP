package com.aurora.domain.vo;

import com.aurora.enums.StatusCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.aurora.enums.StatusCodeEnum.*;

/**
 * 返回格式封装类
 * @author 30676
 * @date 2023-03-31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("all")
public class ResultVo<T> {
    /**国旗*/
    private Boolean flag;
    /**代码*/
    private Integer code;
    /**消息*/
    private String message;
    /**数据*/
    private T data;

    public static <T> ResultVo<T> ok() {
        return resultVo(true, SUCCESS.getCode(), SUCCESS.getDesc(), null);
    }


    public static <T> ResultVo<T> ok(T data) {
        return resultVo(true, SUCCESS.getCode(), SUCCESS.getDesc(), data);
    }

    /**
     * @param data    数据
     * @param message 消息
     */
    public static <T> ResultVo<T> ok(T data, String message) {
        return resultVo(true, SUCCESS.getCode(), message, data);
    }

    /**
     * 失败
     */
    public static <T> ResultVo<T> fail() {
        return resultVo(false, FAIL.getCode(), FAIL.getDesc(), null);
    }

    /**
     * 失败
     * @param statusCodeEnum 状态码枚举
     */
    public static <T> ResultVo<T> fail(StatusCodeEnum statusCodeEnum) {
        return resultVo(false, statusCodeEnum.getCode(), statusCodeEnum.getDesc(), null);
    }

    public static <T> ResultVo<T> fail(String message) {
        return resultVo(false, message);
    }

    public static <T> ResultVo<T> fail(T data) {
        return resultVo(false, FAIL.getCode(), FAIL.getDesc(), data);
    }

    public static <T> ResultVo<T> fail(T data, String message) {
        return resultVo(false, FAIL.getCode(), message, data);
    }

    public static <T> ResultVo<T> fail(Integer code, String message) {
        return resultVo(false, code, message, null);
    }


    private static <T> ResultVo<T> resultVo(Boolean flag, String message) {
        return ResultVo.<T>builder()
                .flag(flag)
                .code(flag ? SUCCESS.getCode() : FAIL.getCode())
                .message(message).build();
    }

    /**
     * 因此签证官
     *
     * @param flag    国旗
     * @param code    代码
     * @param message 消息
     * @param data    数据
     * @return {@link ResultVo}<{@link T}>
     */
    private static <T> ResultVo<T> resultVo(Boolean flag, Integer code, String message, T data) {
        return ResultVo.<T>builder()
                .flag(flag)
                .code(code)
                .message(message)
                .data(data).build();
    }

}

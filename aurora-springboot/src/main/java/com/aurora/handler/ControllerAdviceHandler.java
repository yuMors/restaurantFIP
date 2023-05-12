package com.aurora.handler;

import com.aurora.domain.vo.ResultVo;
import com.aurora.enums.StatusCodeEnum;
import com.aurora.exception.BizException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;



@RestControllerAdvice
public class ControllerAdviceHandler {

    @ExceptionHandler(value = BizException.class)
    public ResultVo<?> errorHandler(BizException e) {
        return ResultVo.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVo<?> errorHandler(MethodArgumentNotValidException e) {
        return ResultVo.fail(StatusCodeEnum.VALID_ERROR.getCode(), Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResultVo<?> errorHandler(Exception e) {
        e.printStackTrace();
        return ResultVo.fail(StatusCodeEnum.SYSTEM_ERROR.getCode(), StatusCodeEnum.SYSTEM_ERROR.getDesc());
    }

}

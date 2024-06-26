package com.zigaai.core.handler;

import com.zigaai.core.exception.*;
import com.zigaai.core.model.common.Res;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Set;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class BaseExceptionHandler {

    private static final String COMMA = ",";

    /**
     * 统一处理请求参数校验(json)
     *
     * @param e ConstraintViolationException
     * @return CommonResponse
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Res<Void> handlerMethodArgumentNotValidException(BindException e) {
        StringBuilder message = new StringBuilder();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            message.append(error.getDefaultMessage()).append(COMMA);
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        if (log.isDebugEnabled()) {
            log.debug(message.toString());
        }
        return Res.badRequest(message.toString(), null);
    }

    /**
     * 统一处理请求参数校验(普通传参)
     *
     * @param e ConstraintViolationException
     * @return CommonResponse
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Res<Void> handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            message.append(violation.getMessage()).append(COMMA);
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        if (log.isDebugEnabled()) {
            log.debug(message.toString());
        }
        return Res.badRequest(message.toString(), null);
    }

    @ExceptionHandler(value = MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Res<Void> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        return Res.badRequest(e.getRequestPartName() + "不可为空, 请检查");
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Res<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        if (log.isDebugEnabled()) {
            log.debug(e.getLocalizedMessage());
        }
        return Res.badRequest(e.getParameterName() + ": 为必填值", null);
    }

    @ExceptionHandler(value = BizIllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Res<Void> handleServiceArgumentException(BizIllegalArgumentException e) {
        if (log.isDebugEnabled()) {
            log.debug("请求参数错误: ", e);
        }
        return Res.fail(e.getLocalizedMessage());
    }

    @ExceptionHandler(value = {
            LoginIllegalArgumentException.class,
            RefreshTokenExpiredException.class,
            JwtInvalidException.class,
            JwtExpiredException.class
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Res<Void> handleAuthenticationException(AuthenticationException e) {
        if (log.isDebugEnabled()) {
            log.debug("认证错误: ", e);
        }
        if (e instanceof RefreshTokenExpiredException) {
            return Res.needLogin(e.getLocalizedMessage());
        }
        return Res.unauthorized(e.getLocalizedMessage());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Res<Void> handleAccessDeniedException(AccessDeniedException e) {
        return Res.forbidden("当前用户无权访问");
    }

    @ExceptionHandler({NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Res<Void> handleNoResourceFoundException(NoResourceFoundException e) {
        return Res.notFound("Not Found");
    }

    @ExceptionHandler(value = StatusRuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Res<Void> handleRpcException(StatusRuntimeException e) {
        if (Status.UNKNOWN.getCode().equals(e.getStatus().getCode())
                || Status.INVALID_ARGUMENT.getCode().equals(e.getStatus().getCode())) {
            log.info("rpc服务调用失败: ", e);
            return Res.unknownError(e.getStatus().getDescription());
        }
        log.error("rpc服务调用失败: ", e);
        return Res.unknownError("rpc服务调用失败(" + e.getLocalizedMessage() + "), 请联系管理员处理");
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Res<Void> handleAllException(Exception e) {
        log.error("服务器内部错误: ", e);
        return Res.unknownError("未知错误, 请联系管理员处理");
    }

    @ExceptionHandler(value = BizException.class)
    @ResponseStatus(HttpStatus.OK)
    public Res<Void> handleServiceException(BizException e) {
        if (log.isDebugEnabled()) {
            log.debug("业务异常: ", e);
        }
        return Res.fail(e.getLocalizedMessage());
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Res<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return Res.methodNotAllowed("不支持的请求: " + e.getMethod());
    }

}

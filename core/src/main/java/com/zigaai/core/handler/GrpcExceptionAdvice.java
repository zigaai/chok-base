package com.zigaai.core.handler;

import com.zigaai.core.exception.BizIllegalArgumentException;
import com.zigaai.core.exception.BizException;
import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class GrpcExceptionAdvice {

    @GrpcExceptionHandler
    public Status handleBizException(BizException e) {
        return Status.UNKNOWN.withDescription(e.getMessage()).withCause(e);
    }

    @GrpcExceptionHandler
    public Status handleBizIllegalArgumentException(BizIllegalArgumentException e) {
        return Status.INVALID_ARGUMENT.withDescription(e.getMessage()).withCause(e);
    }

}

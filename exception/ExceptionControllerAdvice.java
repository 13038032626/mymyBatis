package com.example.demo.exception;
import com.example.demo.BaseResult;
import com.example.demo.RespCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class ExceptionControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler(DeInsertException.class)
    public BaseResult diException(DeInsertException e) {
        return BaseResult.failBecauseOfDeinsert(e);
    }


    @ExceptionHandler(ParamFormatException.class)
    public BaseResult pfException(ParamFormatException e) {
        return BaseResult.failBecauseOfParam(e);
    }


    @ExceptionHandler(Exception.class)
    public BaseResult unknowException(Exception e) {
        logger.error("全局异常", e);
        return BaseResult.failBecauseOfUnknown(e);
    }
}

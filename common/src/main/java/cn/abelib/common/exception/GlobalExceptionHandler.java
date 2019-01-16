package cn.abelib.common.exception;

import cn.abelib.common.constant.StatusConstant;
import cn.abelib.common.result.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * @author abel
 * @date 2018/2/6
 *  全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Response<String> exceptionHandler(HttpServletRequest request, Exception e){
        if (e instanceof GlobalException){
            GlobalException ge = (GlobalException) e;
            log.error("{} Exception", request.getRequestURI(), e);
            return Response.failed(ge.getMeta());
        }
        else if (e instanceof BindException){
            BindException ex = (BindException) e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            log.error("{} Exception", request.getRequestURI(), msg);
            return Response.failed(StatusConstant.PRAM_BIND_ERROR.fillArgs(msg));
        }
        log.error("{} Exception", request.getRequestURI(), e);
        return Response.failed(StatusConstant.GENERAL_SERVER_ERROR);
    }
}

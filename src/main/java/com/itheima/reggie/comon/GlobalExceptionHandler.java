package com.itheima.reggie.comon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
@Slf4j
@ResponseBody
@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
       log.error(ex.getMessage());
       if(ex.getMessage().contains("Duplicate entry")){
           String[] split = ex.getMessage().split(" ");
           String msg =split[2]+"已存在";
           return R.error(msg);
       }
       return R.error("未知错误");
    }

    /**
     * 自定义异常
     * @param ex
     * @return
     */
    @ExceptionHandler(CostomException.class)
    public R<String> exceptionHandler(CostomException ex){
        log.error(ex.getMessage());

        return R.error(ex.getMessage());
    }


}

package com.itheima.reggie.comon;

/**
 * 自定义业务异常类
 */
public class CostomException extends RuntimeException{
    public CostomException(String message) {
        super(message);
    }
}

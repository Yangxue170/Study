package com.learn.log.dubbo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * DubboExceptionDto
 * @author
 */
@Getter
@Setter
@NoArgsConstructor
public class DubboExceptionDto {

    private String interfaceName;
    private String methodName;
    private Date createdTime;
    private String exceptionType;
    private Throwable throwable;
    private Long cost;

    /**
     * 构造方法
     * @param interfaceName 接口名
     * @param methodName 方法名
     * @param exceptionType 异常类型
     * @param throwable 异常
     * @param cost 耗时
     */
    public DubboExceptionDto(String interfaceName, String methodName, 
            String exceptionType, Throwable throwable, Long cost) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.exceptionType = exceptionType;
        this.throwable = throwable;
        this.cost = cost;
        this.createdTime = new Date();
    }

}

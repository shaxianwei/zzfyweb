/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.event.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 实现描述：TTS接口交互日志记录
 * 
 * @author qiang.dong
 * @version v1.0.0
 * @see
 * @since 2013-9-22 上午10:49:13
 */
@Aspect
@Component
public class ApiLogAspect {

    private final static Logger logger = LoggerFactory.getLogger(ApiLogAspect.class);

    /**
     * 针对我们访问外部接口记录日志
     * 
     * @param joinPoint
     * @return
     * @throws Throwable
     */
//    @Around("execution (* cn.zzfyip.search.service.api.client.impl.HttpApiClient.*(..)) ")
//    public Object clientExchangeLog(ProceedingJoinPoint joinPoint) throws Throwable {
//        ApiLogAspect.logger.info("clientExchangeLog start");
//        Object responseObject = null;
//        // 交互起始时间
//        Date startTime = new Date();
//
//        Object result = null;
//        try {
//            // 执行目标方法
//            result = joinPoint.proceed();
//        } finally {
//            try {
//                responseObject = apiLogger.pClientExchangeLog(joinPoint, startTime, result);
//                ApiLogAspect.logger.info("clientExchangeLog end");
//            } catch (Exception e) {
//                ApiLogAspect.logger.error("ttsExchangeLog记录出错", e);
//                warningNotice.sendDevNotice(e, "ttsExchangeLog记录出错", null, null, 1);
//            }
//        }
//        return responseObject;
//    }
    
}

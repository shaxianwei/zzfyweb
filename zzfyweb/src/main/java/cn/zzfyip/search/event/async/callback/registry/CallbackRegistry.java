/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.event.async.callback.registry;

import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import com.google.common.collect.Maps;
import cn.zzfyip.search.event.async.callback.EventCallback;

/**
 * 实现描述：处理器回调注册
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-8-5 下午7:55:26
 */

@SuppressWarnings("rawtypes")
public class CallbackRegistry {

    private final static Logger logger = LoggerFactory.getLogger(CallbackRegistry.class);

    private static Map<String, EventCallback> processorMap = Maps.newConcurrentMap();

    private static final String ON_CALLBACK = "onCallback";

    /**
     * 处理回调任务
     * 
     * @param event
     * @throws Exception
     */
    public static void callBack(Object event) throws Exception {
        EventCallback processor = CallbackRegistry.processorMap.get(event.getClass().getName());

        if (processor == null) {
            return;
        }

        Method method = ReflectionUtils
                .findMethod(processor.getClass(), CallbackRegistry.ON_CALLBACK, event.getClass());

        if (method == null) {
            return;
        }

        if (!method.isAccessible()) {
            ReflectionUtils.makeAccessible(method);
        }

        ReflectionUtils.invokeMethod(method, processor, event);
    }

    /**
     * 注册任务对象和回调处理器
     * 
     * @param clazz
     * @param callback
     */
    public static void register(Class<?> clazz, EventCallback callback) {
        EventCallback preCallBack = CallbackRegistry.processorMap.get(clazz.getName());
        if (preCallBack != null) {
            CallbackRegistry.logger.debug(clazz.getName() + " already has callback : "
                    + preCallBack.getClass().getName());
        } else {
            CallbackRegistry.processorMap.put(clazz.getName(), callback);
        }
    }

    /**
     * 取消注册
     * 
     * @param clazz
     */
    public static void unregister(Class<?> clazz) {
        CallbackRegistry.processorMap.remove(clazz.getName());
    }
}

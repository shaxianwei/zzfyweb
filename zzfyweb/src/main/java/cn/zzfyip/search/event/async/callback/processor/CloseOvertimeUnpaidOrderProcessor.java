/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.event.async.callback.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import cn.zzfyip.search.event.async.callback.EventCallback;
import cn.zzfyip.search.event.async.callback.event.CloseOvertimeUnpaidOrderEvent;
import cn.zzfyip.search.event.async.callback.registry.CallbackRegistry;

/**
 * 
 * 实现描述：关闭超时支付订单处理器
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-12-18 下午6:16:10
 */
@Component
public class CloseOvertimeUnpaidOrderProcessor implements EventCallback<CloseOvertimeUnpaidOrderEvent>,
        InitializingBean {

    private final static Logger logger = LoggerFactory.getLogger(CloseOvertimeUnpaidOrderProcessor.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        // 注册事件和处理器的关系
        CallbackRegistry.register(CloseOvertimeUnpaidOrderEvent.class, this);
    }

    @Override
    public void onCallback(CloseOvertimeUnpaidOrderEvent event) {
        logger.info("can't cancel order displayId:{}", event.getDisplayId());
    }
}

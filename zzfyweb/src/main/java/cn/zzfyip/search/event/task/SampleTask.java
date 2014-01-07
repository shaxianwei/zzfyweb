/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.event.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 实现描述：Sample Task
 * 
 * @author shawn.chang
 * @version v1.0.0
 * @see
 * @since 2013-9-24 下午6:16:30
 */
@Component
public class SampleTask {

    private static final Logger logger = LoggerFactory.getLogger(SampleTask.class);

    public void helloWorld() {
        SampleTask.logger.info("Hello World!");
    }
}

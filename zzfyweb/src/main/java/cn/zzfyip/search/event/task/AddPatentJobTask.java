/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.event.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.zzfyip.search.service.search.PatentNoLoadService;

/**
 * 实现描述：Sample Task
 * 
 * @author shawn.chang
 * @version v1.0.0
 * @see
 * @since 2013-9-24 下午6:16:30
 */
@Component
public class AddPatentJobTask {

    @Autowired
    private PatentNoLoadService patentNoLoadService;
    public void addPatentRecordJob() {
    	patentNoLoadService.createAddPatentRecord();
    }
}

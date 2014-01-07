/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.controller.anonymous;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.zzfyip.search.common.support.StandardJsonObject;
import cn.zzfyip.search.service.search.PatentNoLoadService;
import cn.zzfyip.search.utils.DateUtils;

/**
 * 
 * 实现描述：匿名用户订单相关数据接口
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-10-16 下午3:30:29
 */
@Controller("toolboxController")
@RequestMapping("/toolbox")
public class ToolboxController {

	@Autowired
	PatentNoLoadService patentNoLoadService;
	
    @ResponseBody
    @RequestMapping(params = "method=createAddPatentRecord", method = { RequestMethod.GET })
    public StandardJsonObject createAddPatentRecord()
            throws Exception {
    	patentNoLoadService.createAddPatentRecord();
        return StandardJsonObject.newCorrectJsonObject();
    }
    @ResponseBody
    @RequestMapping(params = "method=createAddPatentRecordByBeginDateAndEndDate", method = { RequestMethod.GET })
    public StandardJsonObject createAddPatentRecordByBeginDateAndEndDate(String beginDateString,String endDateString)
    		throws Exception {
    	Date beginDate = DateUtils.convertDate(beginDateString);
    	Date endDate = DateUtils.convertDate(endDateString);
    	patentNoLoadService.createAddPatentRecordByBeginDateAndEndDate(beginDate,endDate);
    	return StandardJsonObject.newCorrectJsonObject();
    }
    @ResponseBody
    @RequestMapping(params = "method=addUnsearchedPatentRecordToPatentMain", method = { RequestMethod.GET })
    public StandardJsonObject addUnsearchedPatentRecordToPatentMain()
    		throws Exception {
    	patentNoLoadService.addUnsearchedPatentRecordToPatentMain();
    	return StandardJsonObject.newCorrectJsonObject();
    }

}

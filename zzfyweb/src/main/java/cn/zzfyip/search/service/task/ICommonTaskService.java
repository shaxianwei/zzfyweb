/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.service.task;

/**
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-9-16 下午7:56:11
 */
public interface ICommonTaskService {

    /**
     * 同步度假数据库数据
     */
    public void loadUndoneEventsIntoAsyncBuffer();

    /**
     * 同步主站景点信息
     */
    public void syncProductSight();

    /**
     * 同步度假数据库数据
     */
    public void syncVacationData();
}

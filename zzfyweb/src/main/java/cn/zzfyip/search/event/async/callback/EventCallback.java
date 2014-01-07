/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.event.async.callback;

/**
 * 实现描述：处理器回调接口
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-8-5 下午7:55:59
 */
public interface EventCallback<T> {

    /**
     * 默认的回调方法
     * 
     * @param event
     */
    public void onCallback(T event);
}

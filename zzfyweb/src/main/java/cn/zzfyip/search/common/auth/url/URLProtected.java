/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.common.auth.url;

/**
 * 实现描述：基于url的匹配
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-8-2 下午12:09:07
 */
public interface URLProtected {

    public boolean check(String requestUrl);
}

/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.common.auth;

import cn.zzfyip.search.common.auth.url.URLProtected;

/**
 * 实现描述：权限保护
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-8-2 下午12:01:58
 */
public class AuthorizeProtected {

    private URLProtected urlProtected;

    public URLProtected getUrlProtected() {
        return urlProtected;
    }

    public void setUrlProtected(URLProtected urlProtected) {
        this.urlProtected = urlProtected;
    }

}

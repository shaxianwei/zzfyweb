/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.common.auth.url;

import org.apache.oro.text.regex.Pattern;

/**
 * 实现描述：封装url pattern匹配
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-8-2 下午12:12:27
 */
public class URLPatternHolder {

    private Pattern compiledPattern;
    private String url;

    public Pattern getCompiledPattern() {
        return compiledPattern;
    }

    public String getUrl() {
        return url;
    }

    public void setCompiledPattern(Pattern compiledPattern) {
        this.compiledPattern = compiledPattern;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

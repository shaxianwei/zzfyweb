/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.common.auth.url;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-8-2 下午12:11:52
 */
public class URLProtectedImpl implements URLProtected {

    private static final Logger logger = LoggerFactory.getLogger(URLProtectedImpl.class);
    private List<URLPatternHolder> urlProtectedList = new ArrayList<URLPatternHolder>();

    public URLProtectedImpl() {
    }

    public URLProtectedImpl(List<URLPatternHolder> urlProtectedList) {
        this.urlProtectedList = urlProtectedList;
    }

    public void addUrlProtected(URLPatternHolder urlProtected) {
        this.urlProtectedList.add(urlProtected);
    }

    @Override
    public boolean check(String requestUrl) {
        if (StringUtils.isBlank(requestUrl)) {
            return false;
        }

        if (URLProtectedImpl.logger.isDebugEnabled()) {
            URLProtectedImpl.logger.debug("DEBUG ## Converted URL to lowercase, from: '" + requestUrl + "'; to: '"
                    + requestUrl + "'");
        }

        PatternMatcher matcher = new Perl5Matcher();

        Iterator<URLPatternHolder> iter = urlProtectedList.iterator();

        while (iter.hasNext()) {
            URLPatternHolder holder = iter.next();

            if (matcher.matches(requestUrl, holder.getCompiledPattern())) {
                if (URLProtectedImpl.logger.isDebugEnabled()) {
                    URLProtectedImpl.logger.debug("DEBUG ## Candidate is: '" + requestUrl + "'; pattern is "
                            + holder.getCompiledPattern().getPattern() + "; matched=true");
                }
                return true;
            }
        }
        return false;
    }

    public List<URLPatternHolder> getUrlProtectedList() {
        return urlProtectedList;
    }

    public void setUrlProtectedList(List<URLPatternHolder> urlProtectedList) {
        this.urlProtectedList = urlProtectedList;
    }
}

/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.common.paginator.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.zzfyip.search.common.paginator.Page;
import cn.zzfyip.search.common.paginator.PageContext;

/**
 * 实现描述：
 * 
 * @author reeboo
 * @version v1.0.0
 * @since 2013年9月23日 下午3:11:22
 */
@Component
public class PageInterceptor extends HandlerInterceptorAdapter {

    /**
     * 
     * 结束请求后，将线程中的分页信息清除
     * 
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     * @author reeboo
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        // 清除线程分页信息
        PageContext.clear();
    }

    /**
     * 
     * 获取request中分页信息，并设置到PageContext中，后续数据库查询从PageContext中获取分页数据
     * 
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     * @author reeboo
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 默认分页参数
        int pageNoValue = Page.DEFAULT_PAGE_NO;
        int perPageValue = Page.DEFAULT_PER_PAGE_SIZE;

        // 如果请求设定，则使用之
        String pageNo = request.getParameter(Page.PARAMETER_PAGE_NO);
        String perPageSize = request.getParameter(Page.PARAMETER_PER_PAGE_SIZE);
        if (!StringUtils.isEmpty(pageNo) && StringUtils.isNumeric(pageNo)) {
            pageNoValue = Integer.valueOf(pageNo).intValue();
        }
        if (!StringUtils.isEmpty(perPageSize) && StringUtils.isNumeric(perPageSize)) {
            perPageValue = Integer.valueOf(perPageSize).intValue();
        }

        // 设置线程分页信息
        PageContext.setPage(new Page(pageNoValue, perPageValue));
        return true;
    }

}

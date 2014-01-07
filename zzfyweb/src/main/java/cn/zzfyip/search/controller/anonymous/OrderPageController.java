/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.controller.anonymous;

import org.springframework.stereotype.Controller;

/**
 * 
 * 实现描述：匿名用户订单相关页面
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-10-16 下午3:30:29
 */
@Controller("anonymousOrderPageController")
public class OrderPageController {

    /**
     * 订单列表跳转页
     */
//    @RequestMapping(value = "order/list")
//    public void order(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        if (CookieUserContext.isLogin()) {
//            response.sendRedirect("/website/order/list");
//        } else if (CookieUserContext.getAuthorizeType().isToken()) {
//            authorityService.writeTokenCookieInMenpiaoDomain(response);
//            response.sendRedirect("/token/order/list");
//        } else {
//            StringBuilder loginUrlbuilder = new StringBuilder();
//            loginUrlbuilder.append(OrderPageController.USER_LOGIN_URL);
//            loginUrlbuilder.append("?ret=");
//            if (org.apache.commons.lang.StringUtils.containsIgnoreCase(request.getServletPath(), "order/list")) {
//                loginUrlbuilder.append(TextUtils.urlEncodeUTF8("http://piao.zzfyip.cn/order/myorder.htm"));
//            } else {
//                loginUrlbuilder.append(TextUtils.urlEncodeUTF8("http://"));
//                loginUrlbuilder.append(TextUtils.urlEncodeUTF8(request.getHeader("host")));
//                loginUrlbuilder.append(TextUtils.urlEncodeUTF8(request.getServletPath()));
//                loginUrlbuilder.append(TextUtils.urlEncodeUTF8("?"));
//                loginUrlbuilder.append(TextUtils.urlEncodeUTF8(request.getQueryString()));
//            }
//            response.sendRedirect(loginUrlbuilder.toString());
//        }
//    }
}

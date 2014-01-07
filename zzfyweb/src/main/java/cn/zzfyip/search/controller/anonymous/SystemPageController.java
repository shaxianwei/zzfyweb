/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.controller.anonymous;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <pre>
 * 实现描述：TTS系统级别的页面跳转
 * 1、'\'欢迎页面跳转到home页，替代welcome-file-list功能
 * 2、404跳转页面
 * 3、500跳转页面
 * 4、startCheck给Jenkins检查启动状态
 * </pre>
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-10-16 下午3:27:41
 */
@Controller
public class SystemPageController {

    @RequestMapping(value = "/error")
    public ModelAndView error() throws Exception {
        ModelAndView modelView = new ModelAndView();
        modelView.setViewName("anonymous/error");
        return modelView;
    }

    @RequestMapping(value = "/notfound")
    public ModelAndView notFound() throws Exception {
        ModelAndView modelView = new ModelAndView();
        modelView.setViewName("anonymous/notFound");
        return modelView;
    }

    @RequestMapping(value = "/")
    public void root(HttpServletResponse response) throws Exception {
        response.sendRedirect("/home");
    }

}

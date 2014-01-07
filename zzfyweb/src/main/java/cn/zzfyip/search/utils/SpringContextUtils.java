package cn.zzfyip.search.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 
 * 实现描述：获取spring容器，以访问容器中定义的其他bean
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-8-15 下午10:47:09
 */
@Component
@Lazy(false)
public class SpringContextUtils implements ApplicationContextAware {

    // Spring应用上下文环境
    private static ApplicationContext applicationContext;

    /**
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return SpringContextUtils.applicationContext;
    }

    /**
     * 获取对象,通过Class类型拿到实例对象，前提实例名字和类名一致且第一个字符为小写
     * 
     * @param targetClass
     * @return
     * @throws BeansException
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> targetClass) throws BeansException {
        String clazzName = StringUtils.substringAfterLast(targetClass.getName(), ".");
        String instanceName = clazzName.substring(0, 1).toLowerCase() + clazzName.substring(1);
        return (T) SpringContextUtils.applicationContext.getBean(instanceName);
    }

    /**
     * 获取对象 这里重写了bean方法，起主要作用
     * 
     * @param name
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException
     */
    public static Object getBean(String name) throws BeansException {
        return SpringContextUtils.applicationContext.getBean(name);
    }

    /**
     * @param applicationContext
     */
    private static void initialize(ApplicationContext applicationContext) {
        SpringContextUtils.applicationContext = applicationContext;
    }

    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     * 
     * @param applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtils.initialize(applicationContext);
    }

}
/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.common.thread;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实现描述：命名线程工厂
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-8-5 下午7:48:18
 */
public class NamedThreadFactory implements ThreadFactory {

    final private static String DEFAULT_NAME = "tts-worker";
    private static final Logger logger = LoggerFactory.getLogger(NamedThreadFactory.class);
    final static UncaughtExceptionHandler uncaughtExceptionHandler = new UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            NamedThreadFactory.logger.error("from " + t.getName(), e);
        }
    };
    final private boolean daemon;
    final private ThreadGroup group;
    final private String name;

    final private AtomicInteger threadNumber = new AtomicInteger(0);

    public NamedThreadFactory() {
        this(NamedThreadFactory.DEFAULT_NAME, true);
    }

    public NamedThreadFactory(String name) {
        this(name, true);
    }

    public NamedThreadFactory(String name, boolean daemon) {
        this.name = name;
        this.daemon = daemon;
        SecurityManager s = System.getSecurityManager();
        group = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, name + "-" + threadNumber.getAndIncrement(), 0);
        t.setDaemon(daemon);
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }

        t.setUncaughtExceptionHandler(NamedThreadFactory.uncaughtExceptionHandler);
        return t;
    }

}

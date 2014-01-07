/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.event.async;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import cn.zzfyip.search.common.thread.NamedThreadFactory;
import cn.zzfyip.search.event.async.callback.registry.CallbackRegistry;

/**
 * 实现描述：事件对象的异步处理器
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-8-5 下午7:49:34
 */
@Component
public class AsyncEventListener implements InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(AsyncEventListener.class);

    private static final BlockingQueue<Object> blockingBuffer = new LinkedBlockingQueue<Object>();
    private static final DelayQueue<Delayed> delayBuffer = new DelayQueue<Delayed>();

    private ExecutorService blockingBufferDispatcher;
    private ExecutorService delayBufferDispatcher;
    private ExecutorService eventExecutor;

    private int ququeSize = 1000;
    private int coreSize = 30;

    @Override
    public void afterPropertiesSet() throws Exception {
        eventExecutor = new ThreadPoolExecutor(coreSize, 2 * coreSize, 5, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(ququeSize), new NamedThreadFactory("event-executor", false),
                new ThreadPoolExecutor.CallerRunsPolicy());
        blockingBufferDispatcher = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory(
                "blocking-buffer-dispatcher", true));
        delayBufferDispatcher = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory(
                "delay-buffer-dispatcher", true));

        consumeBuffer();
    }

    /**
     * 将异步处理事件丢到阻塞队列中
     * 
     * @param eventObject 被处理的事件对象
     * @return
     */
    public static boolean putInBlockingBuffer(Object eventObject) {
        boolean success = false;
        try {
            success = AsyncEventListener.blockingBuffer.offer(eventObject);
        } catch (Exception e) {
            AsyncEventListener.logger.error(
                    "ERROR ## cat't put event message into blocking queue since exception happens : " + eventObject, e);
        }
        if (!success) {
            AsyncEventListener.logger.error("ERROR ## cant't put event message into blocking queue since timeout : "
                    + eventObject);
        }
        return success;
    }

    /**
     * 将异步处理事件丢到延迟队列中
     * 
     * @param eventObject 被处理的事件对象，必须继承Delayed
     * @return
     */
    public static boolean putInDelayBuffer(Delayed eventObject) {
        boolean success = false;
        try {
            success = AsyncEventListener.delayBuffer.offer(eventObject);
        } catch (Exception e) {
            AsyncEventListener.logger.error(
                    "ERROR ## cat't put event message into delay queue since exception happens : " + eventObject, e);
        }
        if (!success) {
            AsyncEventListener.logger.error("ERROR ## cant't put event message into delay queue since timeout : "
                    + eventObject);
        }
        return success;
    }

    /**
     * 消费任务队列
     */
    private void consumeBuffer() {
        if (eventExecutor == null) {
            throw new NestableRuntimeException("event executor is null. please check it ");
        }

        blockingBufferDispatcher.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Object eventObject = AsyncEventListener.blockingBuffer.take();
                        eventExecutor.execute(new EventConsumer(eventObject));
                    } catch (InterruptedException e) {
                        AsyncEventListener.logger.warn("WARN ## dispatcher was interrupted");
                    }
                }
            }
        });

        delayBufferDispatcher.execute(new Runnable() {
            @Override
            public void run() {
                long wait = 1 * 1000 * 1000000L;
                do {
                    try {
                        if (Thread.interrupted()) {
                            throw new InterruptedException();
                        }

                        Delayed eventObject = delayBuffer.peek();
                        if (eventObject == null) {
                            LockSupport.parkNanos(wait); // 1 second
                            continue;
                        }

                        eventObject = delayBuffer.take();
                        eventExecutor.execute(new EventConsumer(eventObject));
                    } catch (InterruptedException e) {
                        logger.warn("close trade master was interrupted", e);
                        break;
                    } catch (Exception e) {
                        logger.error("close trade master happens error,  seniorTrade", e);
                    }

                } while (true);
            }
        });
    }

    /**
     * <pre>
     * Spring容器关闭时自动调用，用来关闭线程池和持久化任务队列。
     * 但是不要强依赖这里的持久化，如果直接线上kill -9线程，则无效.
     * 更好的解决办法是每次启动容器时，自动加载需要处理的对象放入任务队列。
     * 
     * <pre>
     */
    @Override
    public void destroy() throws Exception {
        if (eventExecutor != null) {
            eventExecutor.shutdown();
        }
        if (!AsyncEventListener.blockingBuffer.isEmpty()) {
            // 持久化队列中的对象
        }
    }

    /**
     * <pre>
     * 实现描述：消费者内部类
     * 持有回调注册器，利用发射自动回调对应处理器方法
     * </pre>
     * 
     * @author simon
     * @version v1.0.0
     * @see
     * @since 2013-8-6 下午4:13:49
     */
    protected static class EventConsumer implements Runnable {

        private final Object eventObject;

        EventConsumer(Object eventObject) {
            this.eventObject = eventObject;
        }

        @Override
        public void run() {
            try {
                CallbackRegistry.callBack(eventObject);
            } catch (Exception e) {
                AsyncEventListener.logger.error("ERROR ## while try to callback on message receive, message : "
                        + eventObject, e);
            }
        }
    }

    /**
     * 删除阻塞事件对象
     * 
     * @param eventObject
     * @return
     */
    public static boolean removeBlockingEvent(Object eventObject) {
        if (AsyncEventListener.blockingBuffer.contains(eventObject)) {
            return AsyncEventListener.blockingBuffer.remove(eventObject);
        }
        return true;
    }

    /**
     * 删除延迟事件对象
     * 
     * @param eventObject
     * @return
     */
    public static boolean removeDelayEvent(Delayed eventObject) {
        if (AsyncEventListener.delayBuffer.contains(eventObject)) {
            return AsyncEventListener.delayBuffer.remove(eventObject);
        }
        return true;
    }

}

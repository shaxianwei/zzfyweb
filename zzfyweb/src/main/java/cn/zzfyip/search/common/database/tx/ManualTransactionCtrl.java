/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.common.database.tx;

import java.util.Deque;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import com.google.common.collect.Lists;
import cn.zzfyip.search.common.exception.BusinessException;

/**
 * 实现描述：手动事务控制
 * 
 * @author chaoyi.he
 * @version v1.0.0
 * @see
 * @since 2013年10月24日 上午10:53:18
 */
@Component
public class ManualTransactionCtrl {

    private static final ThreadLocal<List<Runnable>> afterCompletionActionsHolder = new ThreadLocal<List<Runnable>>();
    private static final Logger logger = LoggerFactory.getLogger(ManualTransactionCtrl.class);
    private static final ThreadLocal<Deque<DefaultTransactionStatus>> transactionStatusHolder = new ThreadLocal<Deque<DefaultTransactionStatus>>();
    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * 将一段数据库操作放到事务的结束后，外部执行，无论该事务是提交还是回滚，这个操作都会执行
     * 
     * @param action
     */
    public void afterCompletion(Runnable action) {
        if (ManualTransactionCtrl.transactionStatusHolder.get() == null) {
            // 当前不在事务中，直接执行
            action.run();
            return;
        }
        // 在事务中，则将任务放到队列中，延迟执行，无论事务提交或回滚
        List<Runnable> afterCompletionActions = ManualTransactionCtrl.afterCompletionActionsHolder.get();
        if (afterCompletionActions == null) {
            afterCompletionActions = Lists.newArrayList();
            ManualTransactionCtrl.afterCompletionActionsHolder.set(afterCompletionActions);
        }
        afterCompletionActions.add(action);
    }

    /**
     * 手动提交一个事务 必须在事务中
     */
    public void commit() {
        transactionManager.commit(currentTransactionStatus());
    }

    private DefaultTransactionStatus currentTransactionStatus() {
        Deque<DefaultTransactionStatus> statusDeque = ManualTransactionCtrl.transactionStatusHolder.get();
        if (statusDeque == null || statusDeque.size() == 0) {
            // 事务外不能获得这个状态
            throw new IllegalStateException("transaction not found");
        }
        return statusDeque.peek();
    }

    private void execute(TransactionCallback action, DefaultTransactionDefinition txDef) throws BusinessException {
        TransactionStatus status = transactionManager.getTransaction(txDef);
        if (status instanceof DefaultTransactionStatus) {
            // 一个栈结构，支持嵌套事务
            pushDownTransactionStatus((DefaultTransactionStatus) status);
        }
        try {
            action.doInTransaction();
            if (!status.isCompleted()) {
                // 有可能在事务中手动提交，所以先判断是否完成，再提交
                transactionManager.commit(status);
            }
        } catch (RuntimeException ex) {
            // Transactional code threw application exception -> rollback
            rollbackOnException(status, ex);
            throw ex;
        } catch (Error err) {
            // Transactional code threw error -> rollback
            rollbackOnException(status, err);
            throw err;
        } catch (BusinessException ex) {
            // Transactional code threw unexpected exception -> rollback
            rollbackOnException(status, ex);
            throw ex;
        } finally {
            if (status instanceof DefaultTransactionStatus && popUpTransactionStatus()) {
                // 栈空，顶层事务，在事务外延迟执行任务
                List<Runnable> afterCompletionActions = ManualTransactionCtrl.afterCompletionActionsHolder.get();
                if (afterCompletionActions != null) {
                    ManualTransactionCtrl.afterCompletionActionsHolder.remove();
                    for (Runnable act : afterCompletionActions) {
                        act.run();
                    }
                }
            }
        }
    }

    private boolean popUpTransactionStatus() {
        Deque<DefaultTransactionStatus> statusDeque = ManualTransactionCtrl.transactionStatusHolder.get();
        statusDeque.pop();
        if (statusDeque.size() == 0) {
            ManualTransactionCtrl.transactionStatusHolder.remove();
            // 栈空
            return true;
        }
        return false;
    }

    private void pushDownTransactionStatus(DefaultTransactionStatus status) {
        Deque<DefaultTransactionStatus> statusDeque = ManualTransactionCtrl.transactionStatusHolder.get();
        if (statusDeque == null) {
            statusDeque = Lists.newLinkedList();
            ManualTransactionCtrl.transactionStatusHolder.set(statusDeque);
        }
        statusDeque.push(status);
    }

    /**
     * 创建一个事务传播为PROPAGATION_REQUIRED类型的事务块
     * 
     * @param action
     * @throws BusinessException
     */
    public void required(TransactionCallback action) throws BusinessException {
        execute(action, new DefaultTransactionDefinition());
    }

    /**
     * 创建一个事务传播为PROPAGATION_REQUIRES_NEW类型的事务块
     * 
     * @param action
     * @throws BusinessException
     */
    public void requiresNew(TransactionCallback action) throws BusinessException {
        execute(action, new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
    }

    private void rollbackOnException(TransactionStatus status, Throwable ex) throws TransactionException {
        try {
            if (status instanceof DefaultTransactionStatus) {
                DefaultTransactionStatus defStatus = (DefaultTransactionStatus) status;
                if (defStatus.hasSavepoint()) {
                    // 如果有保存点，则回滚到该点，并提交
                    defStatus.rollbackToHeldSavepoint();
                    transactionManager.commit(status);
                    return;
                }
            }
            transactionManager.rollback(status);
        } catch (TransactionSystemException ex2) {
            ManualTransactionCtrl.logger.error("Application exception overridden by rollback exception", ex);
            ex2.initApplicationException(ex);
            throw ex2;
        } catch (RuntimeException ex2) {
            ManualTransactionCtrl.logger.error("Application exception overridden by rollback exception", ex);
            throw ex2;
        } catch (Error err) {
            ManualTransactionCtrl.logger.error("Application exception overridden by rollback error", ex);
            throw err;
        }
    }

    /**
     * 创建一个事务中的保存点，之后可以部分回滚到这一个点 必须在事务中
     */
    public void savepoint() {
        currentTransactionStatus().createAndHoldSavepoint();
    }

}

/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.common.database.mybatis;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

/**
 * 实现描述：TODO 类描述待完成
 * 
 * @author chaoyi.he
 * @version v1.0.0
 * @see
 * @since 2013年11月14日 下午8:49:46
 */
public class MyBatisExceptionTranslator implements PersistenceExceptionTranslator {

    private final DataSource dataSource;

    private SQLExceptionTranslator exceptionTranslator;

    /**
     * Creates a new {@code DataAccessExceptionTranslator} instance.
     * 
     * @param dataSource DataSource to use to find metadata and establish which error codes are usable.
     * @param exceptionTranslatorLazyInit if true, the translator instantiates internal stuff only the first time will
     *            have the need to translate exceptions.
     */
    public MyBatisExceptionTranslator(DataSource dataSource, boolean exceptionTranslatorLazyInit) {
        this.dataSource = dataSource;

        if (!exceptionTranslatorLazyInit) {
            this.initExceptionTranslator();
        }
    }

    /**
     * Initializes the internal translator reference.
     */
    private synchronized void initExceptionTranslator() {
        if (this.exceptionTranslator == null) {
            this.exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException e) {
        if (e instanceof PersistenceException) {
            // Batch exceptions come inside another PersistenceException
            // recursion has a risk of infinite loop so better make another if
            if (e.getCause() instanceof PersistenceException) {
                e = (PersistenceException) e.getCause();
            }
            if (e.getCause() instanceof SQLException) {
                this.initExceptionTranslator();
                return this.exceptionTranslator.translate(e.getMessage() + "\n", null, (SQLException) e.getCause());
            }
            if (e.getCause() instanceof DataAccessException) {
                return (DataAccessException) e.getCause();
            }
            return new MyBatisSystemException(e);
        }
        return null;
    }

}

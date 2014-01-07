/*
 * Copyright 2013 Belleing.com All right reserved. This software is the confidential and proprietary information of
 * Belleing.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Belleing.com.
 */
package cn.zzfyip.search.common.paginator.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zzfyip.search.common.paginator.dialect.Dialect;

/**
 * 实现描述：TODO 类描述待完成
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-9-26 下午10:28:24
 */
public class SQLHelper {

    private final static Logger logger = LoggerFactory.getLogger(SQLHelper.class);

    public static BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(),
                boundSql.getParameterObject());
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    /**
     * 查询总纪录数
     * 
     * @param sql SQL语句
     * @param mappedStatement mapped
     * @param parameterObject 参数
     * @param boundSql boundSql
     * @param dialect database dialect
     * @return 总记录数
     * @throws java.sql.SQLException sql查询错误
     */
    public static int getCount(final String sql, final MappedStatement mappedStatement, final Object parameterObject,
            final BoundSql boundSql, Dialect dialect) throws SQLException {

        final String countSql = dialect.getSelectCountString(sql);
        SQLHelper.logger.debug("Total count SQL [{}] ", countSql);
        SQLHelper.logger.debug("Total count Parameters: {} ", parameterObject);

        Connection connection = null;
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {

            connection = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
            countStmt = connection.prepareStatement(countSql);

            final BoundSql countBoundSql = SQLHelper.copyFromBoundSql(mappedStatement, boundSql, countSql);

            DefaultParameterHandler handler = new DefaultParameterHandler(mappedStatement, parameterObject,
                    countBoundSql);
            handler.setParameters(countStmt);

            rs = countStmt.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            SQLHelper.logger.debug("Total count: {}", count);
            return count;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } finally {
                try {
                    if (countStmt != null) {
                        countStmt.close();
                    }
                } finally {
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                }
            }
        }
    }

}
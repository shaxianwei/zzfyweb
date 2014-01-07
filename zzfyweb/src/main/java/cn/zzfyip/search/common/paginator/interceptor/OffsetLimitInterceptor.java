/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.common.paginator.interceptor;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zzfyip.search.common.paginator.Page;
import cn.zzfyip.search.common.paginator.dialect.Dialect;
import cn.zzfyip.search.common.paginator.helper.SQLHelper;

/**
 * 实现描述：分页插件实现
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-9-6 下午1:34:31
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class }) })
public class OffsetLimitInterceptor implements Interceptor {

    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    private final static Logger logger = LoggerFactory.getLogger(OffsetLimitInterceptor.class);
    private static int MAPPED_STATEMENT_INDEX = 0;
    private static int PARAMETER_INDEX = 1;

    private static int ROWBOUNDS_INDEX = 2;

    private Dialect dialect;

    // see: MapperBuilderAssistant
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        Builder builder = new Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuffer keyProperties = new StringBuffer();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }

        // setStatementTimeout()
        builder.timeout(ms.getTimeout());

        // setStatementResultMap()
        builder.parameterMap(ms.getParameterMap());

        // setStatementResultMap()
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());

        // setStatementCache()
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    private MappedStatement copyFromNewSql(MappedStatement ms, BoundSql boundSql, String sql) {
        BoundSql newBoundSql = SQLHelper.copyFromBoundSql(ms, boundSql, sql);
        return copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
    }

    /*
     * (non-Javadoc)
     * @see org.apache.ibatis.plugin.Interceptor#intercept(org.apache.ibatis.plugin.Invocation)
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        final Object[] queryArgs = invocation.getArgs();
        final MappedStatement ms = (MappedStatement) queryArgs[OffsetLimitInterceptor.MAPPED_STATEMENT_INDEX];
        final Object parameter = queryArgs[OffsetLimitInterceptor.PARAMETER_INDEX];
        final RowBounds rowBounds = (RowBounds) queryArgs[OffsetLimitInterceptor.ROWBOUNDS_INDEX];
        final BoundSql boundSql = ms.getBoundSql(parameter);
        final StringBuffer bufferSql = new StringBuffer(boundSql.getSql().trim());
        if (bufferSql.lastIndexOf(";") == bufferSql.length() - 1) {
            bufferSql.deleteCharAt(bufferSql.length() - 1);
        }

        // 查询总记录数并放入PageContext
        if (rowBounds instanceof Page) {
            Page page = (Page) rowBounds;
            if (page.isCountTotalRows()) {
                try {
                    Integer count = SQLHelper.getCount(bufferSql.toString(), ms, parameter, boundSql, dialect);
                    page.setTotalRows(count);
                } catch (Exception e) {
                    OffsetLimitInterceptor.logger.error(
                            String.format("Query total count error, count sql: [%s]",
                                    dialect.getSelectCountString(bufferSql.toString())), e);
                }
            }
        }

        final int offset = rowBounds.getOffset();
        final int limit = rowBounds.getLimit();

        String sql = bufferSql.toString();
        if (dialect.supportsLimit() && (offset != RowBounds.NO_ROW_OFFSET || limit != RowBounds.NO_ROW_LIMIT)) {

            if (dialect.supportsLimit()) {
                sql = dialect.getLimitString(sql, offset, limit);
            } else {
                sql = dialect.getLimitString(sql, 0, limit);
            }
            queryArgs[OffsetLimitInterceptor.ROWBOUNDS_INDEX] = new RowBounds(RowBounds.NO_ROW_OFFSET,
                    RowBounds.NO_ROW_LIMIT);
        }

        queryArgs[OffsetLimitInterceptor.MAPPED_STATEMENT_INDEX] = copyFromNewSql(ms, boundSql, sql);

        return invocation.proceed();

    }

    /*
     * (non-Javadoc)
     * @see org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    private void setDialect(Dialect dialect) {
        OffsetLimitInterceptor.logger.debug("dialectClass: {} ", dialect.getClass().getName());
        this.dialect = dialect;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.ibatis.plugin.Interceptor#setProperties(java.util.Properties)
     */
    @Override
    public void setProperties(Properties properties) {
        String dialectClass = properties.getProperty("dialect");
        try {
            setDialect((Dialect) Class.forName(dialectClass).newInstance());
        } catch (Exception e) {
            throw new RuntimeException("cannot create dialect instance by dialectClass:" + dialectClass, e);
        }
    }

}

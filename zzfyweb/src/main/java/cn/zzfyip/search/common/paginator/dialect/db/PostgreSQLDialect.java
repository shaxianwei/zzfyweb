package cn.zzfyip.search.common.paginator.dialect.db;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zzfyip.search.common.paginator.dialect.Dialect;
import cn.zzfyip.search.utils.PatternUtils;

/**
 * 
 * 实现描述：Postgre Sql的方言实现
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-9-6 下午1:21:06
 */
public class PostgreSQLDialect implements Dialect {

    private final static Logger logger = LoggerFactory.getLogger(PostgreSQLDialect.class);

    private static final String QUERY_SQL_CONTENT_EXP = "(.*)(\\s+from\\s+)(.*)";
    private static final String QUERY_SQL_CONTENT_GROUP_EXP = "(.*)(\\s+from\\s+)(.*)(\\s+group\\s+by\\s+)(.*)";
    private static final String QUERY_SQL_CONTENT_ORDER_EXP = "(.*)(\\s+from\\s+)(.*)(\\s+order\\s+by\\s+)(.*)";

    @Override
    public String getLimitString(String sql, int offset, int limit) {
        return getLimitString(sql, offset, Integer.toString(offset), Integer.toString(limit));
    }

    /**
     * 将sql变成分页sql语句,提供将offset及limit使用占位符号(placeholder)替换.
     * 
     * <pre>
     * 如mysql
     * dialect.getLimitString("select * from user", 12, ":offset",0,":limit") 将返回
     * select * from user limit :offset,:limit
     * </pre>
     * 
     * @param sql 实际SQL语句
     * @param offset 分页开始纪录条数
     * @param offsetPlaceholder 分页开始纪录条数－占位符号
     * @param limitPlaceholder 分页纪录条数占位符号
     * @return 包含占位符的分页sql
     */
    public String getLimitString(String sql, int offset, String offsetPlaceholder, String limitPlaceholder) {
        StringBuilder pageSql = new StringBuilder().append(sql);
        pageSql = offset <= 0 ? pageSql.append(" limit ").append(limitPlaceholder) : pageSql.append(" limit ")
                .append(limitPlaceholder).append(" offset ").append(offsetPlaceholder);
        return pageSql.toString();
    }

    @Override
    public String getSelectCountString(String sql) {

        String querySql = null;

        if (PatternUtils.isMatch(sql, PostgreSQLDialect.QUERY_SQL_CONTENT_GROUP_EXP)) {
            querySql = PatternUtils.getMatchString(PostgreSQLDialect.QUERY_SQL_CONTENT_GROUP_EXP, sql, 3);
        } else if (PatternUtils.isMatch(sql, PostgreSQLDialect.QUERY_SQL_CONTENT_ORDER_EXP)) {
            querySql = PatternUtils.getMatchString(PostgreSQLDialect.QUERY_SQL_CONTENT_ORDER_EXP, sql, 3);
        } else {
            // SQL中不包含group by和order by的时候，直接取from后面的SQL语句.
            querySql = PatternUtils.getMatchString(PostgreSQLDialect.QUERY_SQL_CONTENT_EXP, sql, 3);
        }

        if (StringUtils.isBlank(querySql)) {
            PostgreSQLDialect.logger.warn("Get select count sql failed, the origin sql is {}", sql);
            return "select count(1) from (" + sql + ") tmp_count";
        }
        return "select count(1) from " + querySql.trim();
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

}

/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.common.exception.resolver;

import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import cn.zzfyip.search.common.exception.BusinessException;
import cn.zzfyip.search.common.support.StandardJsonObject;
import cn.zzfyip.search.utils.JsonUtils;

import com.google.common.collect.Lists;

/**
 * 
 * <pre>
 * 实现描述：
 * 1.如果是数据接口（Data，API）
 * 1.1.出现业务异常（BusinessException），便将业务错误封装为message做为一个ErrorJsonObject返回
 * 1.2.如果是非业务异常（除了BusinessException以外的Exception），则返回一个默认的"api error"的ErrorJsonObject返回
 * 2.如果是页面跳转（Page）
 * 2.1.出现BusinessException便跳转到带有业务错误信息的error页面
 * 2.2.如果是非业务异常（除了BusinessException以外的Exception），则返回一个默error页面
 * </pre>
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-10-11 下午8:50:13
 */
public class ControllerExceptionResolver extends SimpleMappingExceptionResolver {

	static class PGStatActivities {

		private String appName;
		private String connEstab;
		private String dbName;
		private String ipAddr;
		private String query;
		private String queryStart;
		private String state;
		private String stateChange;
		private String usrName;
		private boolean waiting;
		private String xactStart;

		public String getAppName() {
			return appName;
		}

		public String getConnEstab() {
			return connEstab;
		}

		public String getDbName() {
			return dbName;
		}

		public String getIpAddr() {
			return ipAddr;
		}

		public String getQuery() {
			return query;
		}

		public String getQueryStart() {
			return queryStart;
		}

		public String getState() {
			return state;
		}

		public String getStateChange() {
			return stateChange;
		}

		public String getUsrName() {
			return usrName;
		}

		public String getXactStart() {
			return xactStart;
		}

		public boolean isWaiting() {
			return waiting;
		}

		public void setAppName(String appName) {
			this.appName = appName;
		}

		public void setConnEstab(String connEstab) {
			this.connEstab = connEstab;
		}

		public void setDbName(String dbName) {
			this.dbName = dbName;
		}

		public void setIpAddr(String ipAddr) {
			this.ipAddr = ipAddr;
		}

		public void setQuery(String query) {
			this.query = query;
		}

		public void setQueryStart(String queryStart) {
			this.queryStart = queryStart;
		}

		public void setState(String state) {
			this.state = state;
		}

		public void setStateChange(String stateChange) {
			this.stateChange = stateChange;
		}

		public void setUsrName(String usrName) {
			this.usrName = usrName;
		}

		public void setWaiting(boolean waiting) {
			this.waiting = waiting;
		}

		public void setXactStart(String xactStart) {
			this.xactStart = xactStart;
		}

	}

	private static final String API_CONTROLLER = "ApiController";
	private static final String DATA_CONTROLLER = "DataController";
	private final static Logger logger = LoggerFactory
			.getLogger(ControllerExceptionResolver.class);
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	private String jdbcPasswd;
	private String jdbcUrl;
	private String jdbcUser;

	@PreDestroy
	public void destroy() {
		executor.shutdown();
	}

	@Override
	public ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {

		ControllerExceptionResolver.logger.error(String.format(
				"ERROR ## [%s] happend error,the trace is ",
				request.getServletPath()), ex);

		// 查看是不是JDBC连接异常
		handleJdbcConnectionException(ex);

		String fullClazzName = StringUtils.substringAfterLast(handler
				.getClass().getName(), ".");
		String clazzName = StringUtils.substringBefore(fullClazzName, "$");

		if (clazzName.endsWith(ControllerExceptionResolver.DATA_CONTROLLER)
				|| clazzName
						.endsWith(ControllerExceptionResolver.API_CONTROLLER)) /**
		 * 
		 * 如果是数据接口（Data，API）
		 **/
		{
			if (ex instanceof BusinessException) /**
			 * 
			 * 出现业务异常（BusinessException），便将业务错误封装为message做为一个ErrorJsonObject返回
			 **/
			{
				BusinessException be = (BusinessException) ex;
				resolveDataException(request, response, handler,
						StandardJsonObject.newErrorJsonObject(
								be.getErrorCode(), be.getLocalizedMessage()));
			} else/**
			 * 
			 * 如果是非业务异常（除了BusinessException以外的Exception），则返回一个默认的"api error"
			 * 的ErrorJsonObject返回
			 **/
			{
				resolveDataException(request, response, handler,
						StandardJsonObject.newErrorJsonObject("api error"));
			}

			return null;
		} else /** 如果是页面跳转（Page） **/
		{
			if (ex instanceof BusinessException)/**
			 * 
			 * 出现BusinessException便跳转到带有业务错误信息的error页面
			 **/
			{
				ModelAndView mv = new ModelAndView();
				mv.addObject("errMsg",
						((BusinessException) ex).getLocalizedMessage());
				mv.setViewName("anonymous/error");
				return mv;
			} else /** 如果是非业务异常（除了BusinessException以外的Exception），则返回一个默error页面 **/
			{
				return super.doResolveException(request, response, handler, ex);
			}
		}

	}

	private void handleJdbcConnectionException(final Exception ex) {
        if (!(ex instanceof CannotGetJdbcConnectionException) && !(ex instanceof CannotCreateTransactionException)) {
            return;
        }
        // 当无法获取JDBC连接时，记录PG连接信息
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                try {
                    conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPasswd);
                    // log
                    String sql = "SELECT datname, usename, application_name, client_addr, waiting, state, "
                            + "query, query_start, xact_start, backend_start, state_change "
                            + "FROM pg_stat_activity ORDER BY query_start DESC"; // log里记录完整的连接信息
                    List<PGStatActivities> pgStatActivities = query(conn, sql);
                    ControllerExceptionResolver.logger.error("cannot get jdbc connection, pg stat activities: {}",
                            JsonUtils.marshalToString(pgStatActivities));
                    // mail
                    sql = "SELECT datname, usename, application_name, client_addr, waiting, state, "
                            + "query, query_start, xact_start, backend_start, state_change "
                            + "FROM pg_stat_activity WHERE state<>'idle' ORDER BY query_start DESC"; // 邮件只发活跃连接，减少无用信息
                    pgStatActivities = query(conn, sql);
                } catch (SQLException e) {
                    ControllerExceptionResolver.logger.error(
                            "cannot get jdbc connection, retrieve pg stat activities failure", e);
                } finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException _) {
                            // ignore
                        }
                    }
                }
            }
        });
    }

	private List<PGStatActivities> query(Connection conn, String sql)
			throws SQLException {
		ResultSet rs = conn.createStatement().executeQuery(sql);
		List<PGStatActivities> pgStatActivities = Lists.newArrayList();
		while (rs.next()) {
			PGStatActivities activity = new PGStatActivities();
			activity.setDbName(rs.getString("datname"));
			activity.setUsrName(rs.getString("usename"));
			activity.setAppName(rs.getString("application_name"));
			activity.setIpAddr(rs.getString("client_addr"));
			activity.setWaiting(rs.getBoolean("waiting"));
			activity.setState(rs.getString("state"));
			String query = rs.getString("query");
			if (query != null) {
				query = query.replaceAll("\n", " ");
			}
			activity.setQuery(query);
			Timestamp queryStart = rs.getTimestamp("query_start");
			if (queryStart != null) {
				activity.setQueryStart(new DateTime(queryStart)
						.toString("yyyy-MM-dd HH:mm:ss.SSS"));
			}
			Timestamp xactStart = rs.getTimestamp("xact_start");
			if (xactStart != null) {
				activity.setXactStart(new DateTime(xactStart)
						.toString("yyyy-MM-dd HH:mm:ss.SSS"));
			}
			Timestamp connEstab = rs.getTimestamp("backend_start");
			if (connEstab != null) {
				activity.setConnEstab(new DateTime(connEstab)
						.toString("yyyy-MM-dd HH:mm:ss.SSS"));
			}
			Timestamp stateChange = rs.getTimestamp("state_change");
			if (stateChange != null) {
				activity.setStateChange(new DateTime(stateChange)
						.toString("yyyy-MM-dd HH:mm:ss.SSS"));
			}
			pgStatActivities.add(activity);
		}
		return pgStatActivities;
	}

	/**
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 */
	private void resolveDataException(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			StandardJsonObject errorJsonObject) {
		try {
			response.setContentType("application/json;charset=UTF-8");
			Writer writer = response.getWriter();
			writer.write(JsonUtils.marshalToString(errorJsonObject));
			writer.close();
		} catch (IOException e) {
			ControllerExceptionResolver.logger.error(
					"ERROR ## write message happened error, the trace ", e);
		}
	}

	public void setJdbcPasswd(String jdbcPasswd) {
		this.jdbcPasswd = jdbcPasswd;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public void setJdbcUser(String jdbcUser) {
		this.jdbcUser = jdbcUser;
	}

}

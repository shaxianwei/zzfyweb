/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 实现描述：全局常量管理
 * 
 * @author shawn
 * @version v1.0.0
 * @see
 * @since 2013-8-13 下午12:00:46
 */
@Component
@Lazy(false)
public class GlobalConstant {

    @Value("${dbJdbcurl}")
    private String dbJdbcurl;

    @Value("${dbUsername}")
    private String dbUsername;
    
    @Value("${dbPassword}")
    private String dbPassword;

    @Value("${patentNo.fromDate}")
    private String patentNoFromDate;
    
    @Value("${patentNo.numPerPage}")
    private Integer patentNoNumPerPage;
    
    @Value("${patentInfo.searchThreadNum}")
    private Integer patentInfoSearchThreadNum;
    
    @Value("${patentFawen.searchThreadNum}")
    private Integer patentFawenSearchTreadNum;
    
    @Value("${patentFawen.info.filterWords}")
    private String patentFawenInfoFilterWords;
    
    @Value("${patentInfo.threadDelayMilliSeconds}")
    private Integer patentInfoThreadDelayMilliSeconds;
    
    @Value("${patentFawen.threadDelayMilliSeconds}")
    private Integer patentFawenThreadDelayMilliSeconds;
    

	public String getDbJdbcurl() {
		return dbJdbcurl;
	}

	public void setDbJdbcurl(String dbJdbcurl) {
		this.dbJdbcurl = dbJdbcurl;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getPatentNoFromDate() {
		return patentNoFromDate;
	}

	public void setPatentNoFromDate(String patentNoFromDate) {
		this.patentNoFromDate = patentNoFromDate;
	}

	public Integer getPatentNoNumPerPage() {
		return patentNoNumPerPage;
	}

	public void setPatentNoNumPerPage(Integer patentNoNumPerPage) {
		this.patentNoNumPerPage = patentNoNumPerPage;
	}

	public Integer getPatentInfoSearchThreadNum() {
		return patentInfoSearchThreadNum;
	}

	public void setPatentInfoSearchThreadNum(Integer patentInfoSearchThreadNum) {
		this.patentInfoSearchThreadNum = patentInfoSearchThreadNum;
	}

	public Integer getPatentFawenSearchTreadNum() {
		return patentFawenSearchTreadNum;
	}

	public void setPatentFawenSearchTreadNum(Integer patentFawenSearchTreadNum) {
		this.patentFawenSearchTreadNum = patentFawenSearchTreadNum;
	}

	public String getPatentFawenInfoFilterWords() {
		return patentFawenInfoFilterWords;
	}

	public void setPatentFawenInfoFilterWords(String patentFawenInfoFilterWords) {
		this.patentFawenInfoFilterWords = patentFawenInfoFilterWords;
	}

	public Integer getPatentInfoThreadDelayMilliSeconds() {
		return patentInfoThreadDelayMilliSeconds;
	}


	public Integer getPatentFawenThreadDelayMilliSeconds() {
		return patentFawenThreadDelayMilliSeconds;
	}

    
}

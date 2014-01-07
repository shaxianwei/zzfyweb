/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.common.paginator;

import org.apache.ibatis.session.RowBounds;

/**
 * 实现描述：替代RowBounds分页，使用页码和每页条数作为构造参数
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-9-11 下午7:28:03
 */
public class Page extends RowBounds {

    /**
     * 默认页码
     */
    public static final int DEFAULT_PAGE_NO = 1;

    /**
     * 默认每页显示个数
     */
    public static final int DEFAULT_PER_PAGE_SIZE = 20;

    /**
     * 页码
     */
    public static final String PARAMETER_PAGE_NO = "pageNo";

    /**
     * 每页显示个数
     */
    public static final String PARAMETER_PER_PAGE_SIZE = "perPageSize";

    /**
     * 总数返回默认的key值
     */
    public static final String TOTAL_ROWS_KEY = "totalRows";

    public static int getStartRow(int pageNo, int pageSize) {
        pageNo = pageNo > 0 ? pageNo : 1;
        pageSize = pageSize > 0 ? pageSize : 20;
        return (pageNo - 1) * pageSize;
    }

    /**
     * 是否需要包含总条数
     */
    private boolean countTotalRows;

    /**
     * 数据总条数
     */
    private int totalRows;

    public Page(int pageNo, int pageSize) {
        super(Page.getStartRow(pageNo, pageSize), pageSize);
    }

    /**
     * @return the totalRows
     */
    public int getTotalRows() {
        return totalRows;
    }

    /**
     * @return the countTotalRows
     */
    public boolean isCountTotalRows() {
        return countTotalRows;
    }

    /**
     * @param countTotalRows the countTotalRows to set
     */
    public void setCountTotalRows(boolean countTotalRows) {
        this.countTotalRows = countTotalRows;
    }

    /**
     * @param totalRows the totalRows to set
     */
    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

}

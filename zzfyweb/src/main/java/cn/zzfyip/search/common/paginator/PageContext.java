/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.common.paginator;

/**
 * 实现描述：分页上下文
 * 
 * @author reeboo
 * @version v1.0.0
 * @since 2013年9月23日 下午3:04:25
 */
public class PageContext {

    private static final ThreadLocal<Page> context = new ThreadLocal<Page>();

    /**
     * 清除线程分页数据
     */
    public static void clear() {
        PageContext.context.remove();
    }

    /**
     * 
     * 获取线程分页信息（需要查询totalRows）
     * 
     * @return
     */
    public static Page getCountTotalRowsPage() {
        Page page = PageContext.context.get();
        page.setCountTotalRows(true);
        return page;
    }

    /**
     * 
     * 获取线程分页信息
     * 
     * @return
     * @author reeboo
     */
    public static Page getPage() {
        return PageContext.context.get();
    }

    /**
     * 
     * 设置线程分页信息
     * 
     * @param page
     * @author reeboo
     */
    public static void setPage(Page page) {
        PageContext.context.set(page);
    }
}

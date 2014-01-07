/*
 * Copyright 2013 Belleing.com All right reserved. This software is the confidential and proprietary information of
 * Belleing.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Belleing.com.
 */
package cn.zzfyip.search.event.async.callback.event;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 实现描述：关闭超时支付订单
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-12-18 下午6:00:40
 */
public class CloseOvertimeUnpaidOrderEvent implements Delayed {

    private String displayId;
    private Date payDeadlineDate;

    private CloseOvertimeUnpaidOrderEvent(String displayId, Date payDeadlineDate) {
        this.displayId = displayId;
        this.payDeadlineDate = payDeadlineDate;
    }

    public String getDisplayId() {
        return displayId;
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    public Date getPayDeadlineDate() {
        return payDeadlineDate;
    }

    public void setPayDeadlineDate(Date payDeadlineDate) {
        this.payDeadlineDate = payDeadlineDate;
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return payDeadlineDate.getTime() - System.currentTimeMillis();
    }

}

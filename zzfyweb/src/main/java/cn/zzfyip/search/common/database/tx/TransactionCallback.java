/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.common.database.tx;

import cn.zzfyip.search.common.exception.BusinessException;

/**
 * 
 * @author chaoyi.he
 * @version v1.0.0
 * @see
 * @since 2013年10月24日 下午1:55:45
 */
public interface TransactionCallback {

    void doInTransaction() throws BusinessException;

}

/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.common.database.typehandler;

import org.apache.ibatis.type.TypeHandler;

/**
 * 实现描述：TypeHandler
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-9-17 下午6:23:03
 */
public interface ITypeHandler<F, T> extends TypeHandler<T> {

    T transform(F value);

}

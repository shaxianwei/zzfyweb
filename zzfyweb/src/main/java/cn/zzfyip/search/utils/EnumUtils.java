/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.utils;

import cn.zzfyip.search.common.support.IEnum;

/**
 * 实现描述：枚举工具
 * 
 * @author qing.liu
 * @version v1.0.0
 * @see
 * @since 2013-9-15 下午10:22:18
 */
public class EnumUtils {

    @SuppressWarnings("rawtypes")
    public static <E extends Enum<?>> E getEnum(E[] enums, Object enumVal) {
        for (E e : enums) {
            IEnum en = (IEnum) e;
            if (en.getValue().equals(enumVal)) {
                return e;
            }
        }
        return null;
    }

}

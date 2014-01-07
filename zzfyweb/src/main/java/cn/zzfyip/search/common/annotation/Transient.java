/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实现描述：标识不需要Json序列化的属性
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-8-2 下午5:29:16
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Transient {

    boolean value() default true;
}

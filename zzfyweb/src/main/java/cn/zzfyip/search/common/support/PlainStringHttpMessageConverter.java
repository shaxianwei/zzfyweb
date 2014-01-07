/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.common.support;

import java.io.IOException;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;

/**
 * 实现描述：指定Content-Type的StringHttpMessageConverter
 * 
 * @author chaoyi.he
 * @version v1.0.0
 * @see
 * @since 2013-9-27 下午8:02:37
 */
public class PlainStringHttpMessageConverter extends StringHttpMessageConverter {

    public PlainStringHttpMessageConverter() {
        super(StringHttpMessageConverter.DEFAULT_CHARSET);
        setWriteAcceptCharset(false);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.http.converter.StringHttpMessageConverter#writeInternal(java.lang.String,
     * org.springframework.http.HttpOutputMessage)
     */
    @Override
    protected void writeInternal(String s, HttpOutputMessage outputMessage) throws IOException {
        outputMessage.getHeaders().setContentType(MediaType.TEXT_PLAIN);
        super.writeInternal(s, outputMessage);
    }

}

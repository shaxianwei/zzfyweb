/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实现描述：http访问客户端工具类
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-8-2 下午5:21:09
 */
public class HttpClientUtils {
    /**
     * 
     * 实现描述：返回请求的内容，支持默认的字符集，如果httpEntity没有指定的话
     * 
     * @author simon
     * @version v1.0.0
     * @see
     * @since 2013-8-2 下午5:21:59
     */
    private static class CharsetableResponseHandler implements ResponseHandler<String> {

        private String defaultEncoding;

        public CharsetableResponseHandler(String defaultEncoding) {
            this.defaultEncoding = defaultEncoding;
        }

        @Override
        public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
            StatusLine statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            if (statusLine.getStatusCode() >= 300) {
                EntityUtils.consume(entity);
                throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
            }
            return entity == null ? null : EntityUtils.toString(entity, defaultEncoding);
        }
    }

    private static HttpClient client;

    private static final int CONNECTION_TIMEOUT = 5000;

    private static PoolingClientConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private static final int READ_TIMEOUT = 120000;

    static {
        HttpClientUtils.connectionManager = new PoolingClientConnectionManager();
        HttpClientUtils.connectionManager.setDefaultMaxPerRoute(100);
        HttpClientUtils.connectionManager.setMaxTotal(200);
        HttpParams defaultParams = new SyncBasicHttpParams();

        defaultParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, HttpClientUtils.READ_TIMEOUT);
        defaultParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, HttpClientUtils.CONNECTION_TIMEOUT);

        HttpProtocolParams.setContentCharset(defaultParams, Consts.UTF_8.name());
        HttpProtocolParams.setUseExpectContinue(defaultParams, false);
        DefaultHttpClient.setDefaultHttpParams(defaultParams);
        HttpClientUtils.client = new DefaultHttpClient(HttpClientUtils.connectionManager, defaultParams);
    }

    private static String getDefaultEncoding(String defaultEncoding) {
        String encoding = StringUtils.isEmpty(defaultEncoding) ? Consts.UTF_8.name() : defaultEncoding;
        return encoding;
    }

    /**
     * 请求特定的url提交表单，使用post方法，返回响应的内容
     * 
     * @param url
     * @param formData 表单的键值对
     * @return
     */
    public static String post(String url, Map<String, String> formData) {
        return HttpClientUtils.post(url, formData, null);
    }

    /**
     * 请求特定的url提交表单，使用post方法，返回响应的内容
     * 
     * @param url
     * @param formData 表单的键值对
     * @param defaultEncoding 处理 form encode 的编码，以及作为 contentType 的默认编码
     * @return
     */
    public static String post(String url, Map<String, String> formData, String defaultEncoding) {
        defaultEncoding = HttpClientUtils.getDefaultEncoding(defaultEncoding);

        HttpPost post = new HttpPost(url);
        String content = null;
        List<NameValuePair> nameValues = new ArrayList<NameValuePair>();
        if (formData != null && !formData.isEmpty()) {
            for (Entry<String, String> entry : formData.entrySet()) {
                nameValues.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        try {
        	//调整为 stringEntity 否则 专利局不认 Content-Type: application/x-www-form-urlencoded 带编码的
        	StringEntity stringEntity = new StringEntity(URLEncodedUtils.format(nameValues,
        			defaultEncoding != null ? defaultEncoding : HTTP.DEF_CONTENT_CHARSET.name()),
                    ContentType.create(URLEncodedUtils.CONTENT_TYPE));
//            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValues, defaultEncoding);
            post.setEntity(stringEntity);
            content = HttpClientUtils.client.execute(post, new CharsetableResponseHandler(defaultEncoding));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("unsupported Encoding " + defaultEncoding, e);
        } catch (Exception e) {
            HttpClientUtils.logger.error(String.format("post [%s] happens error ", url), e);
        }
        return content;
    }

    /**
     * 请求特定的url提交Json字符串，使用post方法，返回响应的内容
     * 
     * @param url
     * @param jsonData
     * @return
     */
    public static String post(String url, String jsonData) {
        return HttpClientUtils.post(url, jsonData, null);
    }

    /**
     * 请求特定的url提交Json字符串，使用post方法，返回响应的内容
     * 
     * @param url
     * @param jsonData
     * @return
     */
    public static String post(String url, String jsonData, String defaultEncoding) {
        defaultEncoding = HttpClientUtils.getDefaultEncoding(defaultEncoding);
        String content = null;
        try {
            HttpPost post = new HttpPost(url);
            StringEntity entity = new StringEntity(jsonData, defaultEncoding);
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(entity);
            HttpResponse response = HttpClientUtils.client.execute(post);
            content = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            HttpClientUtils.logger.error(String.format("post [%s] happens error ", url), e);
        }
        return content;
    }

    /**
     * 请求特定的url，返回响应的内容
     * 
     * @param url
     * @return
     */
    public static String request(String url) {
        return HttpClientUtils.request(url, null);
    }

    /**
     * 请求特定的url，返回响应的内容
     * 
     * @param url
     * @param defaultEncoding 如果返回的 contentType 中没有指定编码，则使用默认编码
     * @return
     */
    public static String request(String url, String defaultEncoding) {
        defaultEncoding = HttpClientUtils.getDefaultEncoding(defaultEncoding);

        HttpGet get = new HttpGet(url);
        String content = null;
        try {
            content = HttpClientUtils.client.execute(get, new CharsetableResponseHandler(defaultEncoding));
        } catch (Exception e) {
            HttpClientUtils.logger.error(String.format("request [%s] happens error ", url), e);
        }
        return content;
    }

    /**
     * 对url解码
     * 
     * @param str 需要解码的字符串
     * @param encode 编码，如 GBK, UTF-8 等
     * @return 如果解码出错，会传回 null；如果 str 为空，则也返回 null
     */
    public static String urlDecode(String str, String encode) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        String result = null;
        try {
            result = URLDecoder.decode(str, encode);
        } catch (UnsupportedEncodingException e) {
            HttpClientUtils.logger.error("urldecode error for {} with encode {}", str, encode);
        }
        return result;
    }

    /**
     * 对url编码，
     * 
     * @param str 需要编码的字符串
     * @param encode 编码，如 GBK, UTF-8 等
     * @return 如果编码出错，会传回 null；如果 str 为空，则也返回 null
     */
    public static String urlEncode(String str, String encode) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        String result = null;
        try {
            result = URLEncoder.encode(str, encode);
        } catch (UnsupportedEncodingException e) {
            HttpClientUtils.logger.error("urlencode error for {} with encode {}", str, encode);
        }
        return result;
    }

    private HttpClientUtils() {
    }
}

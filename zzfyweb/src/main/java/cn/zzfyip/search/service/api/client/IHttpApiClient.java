package cn.zzfyip.search.service.api.client;

import java.util.Map;

/**
 * <pre>
 * 实现描述：封装HttpClientUtils，用于外部接口的交互访问。
 * 需要对此实现做AOP切片，记录交互原始请求和相应结果。
 * </pre>
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-9-20 上午1:29:05
 */
public interface IHttpApiClient {

    /**
     * 通过Get方式提交，执行原始请求并输出结果的方法
     * 
     * @param url 请求地址
     * @return
     */
    public String doGet(String url);

    /**
     * 通过Post方式提交，执行原始请求并输出结果的方法
     * 
     * @param url 请求地址
     * @param formData 参数
     * @return
     */
    public String doPost(String url, Map<String, String> formData);

    /**
     * 通过Post方式提交Json字符串，执行原始请求并输出结果的方法
     * 
     * @param url 请求地址
     * @param jsonData Json字符串参数
     * @return
     */
    public String doPostJson(String url, String jsonData);

}

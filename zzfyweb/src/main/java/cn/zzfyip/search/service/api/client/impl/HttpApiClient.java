package cn.zzfyip.search.service.api.client.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import cn.zzfyip.search.service.api.client.IHttpApiClient;
import cn.zzfyip.search.utils.HttpClientUtils;

@Component
public class HttpApiClient implements IHttpApiClient {

    @Override
    public String doGet(String url) {
        return HttpClientUtils.request(url);
    }

    @Override
    public String doPost(String url, Map<String, String> formData) {
        return HttpClientUtils.post(url, formData);
    }

    @Override
    public String doPostJson(String url, String jsonData) {
        return HttpClientUtils.post(url, jsonData);
    }
}

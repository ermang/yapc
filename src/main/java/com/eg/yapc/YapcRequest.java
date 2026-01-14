package com.eg.yapc;

import java.util.Map;

public class YapcRequest {
    public final String httpMethod;
    public final String name;
    public final String url;
    public final Map<String, String> requestHeaderMap;
    public final String requestBody;

    public YapcRequest(String httpMethod, String name, String url, Map<String, String> requestHeaderMap, String requestBody) {
        this.httpMethod = httpMethod;
        this.name = name;
        this.url = url;
        this.requestHeaderMap = requestHeaderMap;
        this.requestBody =requestBody;
    }

}

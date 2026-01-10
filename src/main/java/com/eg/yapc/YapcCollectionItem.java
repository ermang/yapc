package com.eg.yapc;

import java.util.List;

public class YapcCollectionItem {
    public final String httpMethod;
    public final String name;
    public final String url;
    public final List<String> requestHeaderList;
    public final String requestBody;

    public YapcCollectionItem(String httpMethod, String name, String url, List<String> requestHeaderList, String requestBody) {
        this.httpMethod = httpMethod;
        this.name = name;
        this.url = url;
        this.requestHeaderList = requestHeaderList;
        this.requestBody =requestBody;
    }

}

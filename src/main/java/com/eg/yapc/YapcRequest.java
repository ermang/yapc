package com.eg.yapc;

import java.util.Map;

public record YapcRequest (String httpMethod, String name, String url, Map<String, String> requestHeaderMap, String requestBody) {}

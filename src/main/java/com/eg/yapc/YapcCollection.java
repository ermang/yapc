package com.eg.yapc;

import java.util.List;

public class YapcCollection {
    private final String name;
    private final List<YapcRequest> yapcRequestList;

    public YapcCollection(String name, List<YapcRequest> yapcRequestList) {
        this.name = name;
        this.yapcRequestList = yapcRequestList;
    }

    public String getName() {
        return name;
    }

    public List<YapcRequest> getYapcRequestList() {
        return yapcRequestList;
    }

    public void deleteItemWithNameFromCollection(String name) {

        for (int i = 0; i < yapcRequestList.size(); i++)
            if (yapcRequestList.get(i).name.equals(name)) {
                yapcRequestList.remove(i);
                break;
            }

    }
}

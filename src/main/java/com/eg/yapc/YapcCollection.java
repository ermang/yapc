package com.eg.yapc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class YapcCollection {
    private final String name;
    private final List<YapcRequest> yapcRequestList;


    @JsonCreator
    public YapcCollection(@JsonProperty("name") String name,
                          @JsonProperty("yapcRequestList") List<YapcRequest> yapcRequestList) {
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
            if (yapcRequestList.get(i).name().equals(name)) {
                yapcRequestList.remove(i);
                break;
            }

    }
}

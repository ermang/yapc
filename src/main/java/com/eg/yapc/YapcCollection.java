package com.eg.yapc;

import java.util.List;

public class YapcCollection {
    private final String name;
    private final List<YapcRequest> collectionItemList;

    public YapcCollection(String name, List<YapcRequest> yapcRequestList) {
        this.name = name;
        this.collectionItemList = yapcRequestList;
    }

    public String getName() {
        return name;
    }

    public List<YapcRequest> getCollectionItemList() {
        return collectionItemList;
    }

    public void deleteItemWithNameFromCollection(String name) {

        for (int i = 0; i < collectionItemList.size(); i++)
            if (collectionItemList.get(i).name.equals(name)) {
                collectionItemList.remove(i);
                break;
            }

    }
}

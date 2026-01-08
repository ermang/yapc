package com.eg.yapc;

import java.util.List;

public class YapcCollection {
    private final String name;
    private final List<YapcCollectionItem> collectionItemList;

    public YapcCollection(String name, List<YapcCollectionItem> collectionItemList) {
        this.name = name;
        this.collectionItemList = collectionItemList;
    }

    public String getName() {
        return name;
    }

    public List<YapcCollectionItem> getCollectionItemList() {
        return collectionItemList;
    }
}

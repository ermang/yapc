package com.eg.yapc;

import java.util.ArrayList;
import java.util.List;

public class YapcSystem {

    private static YapcSystem INSTANCE;

    private final List<YapcCollection> yapcCollectionList;

    private YapcSystem() {
        this.yapcCollectionList = new ArrayList<>();
        //YapcCollectionItem yapcCollectionItem = new YapcCollectionItem("req1");
        List<YapcCollectionItem> yapcCollectionItemList = new ArrayList<>();
        //yapcCollectionItemList.add(yapcCollectionItem);
        yapcCollectionList.add(new YapcCollection("col1", yapcCollectionItemList));
    }

    public static YapcSystem getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new YapcSystem();
        }
        return INSTANCE;
    }

    public List<YapcCollection> getYapcCollectionList() {
        return yapcCollectionList;
    }

    public List<String> getYapcCollectionNamesList() {
        return yapcCollectionList.stream().map(YapcCollection::getName).toList();
    }

    public void addRequestToCollection(YapcCollectionItem yapcCollectionItem, String selectedCollectionName) {
        for (YapcCollection yapcCollection : yapcCollectionList) {
            if (selectedCollectionName.equals(yapcCollection.getName())) {
                yapcCollection.getCollectionItemList().add(yapcCollectionItem);
            }
        }
    }
}

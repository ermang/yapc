package com.eg.yapc;

import java.util.ArrayList;
import java.util.List;

public class YapcSystem {

    private static YapcSystem INSTANCE;

    private final List<YapcCollection> yapcCollectionList;

    private YapcSystem() {
        this.yapcCollectionList = new ArrayList<>();

        List<YapcRequest> yapcRequestList = new ArrayList<>();

        yapcCollectionList.add(new YapcCollection("Dummy Collection", yapcRequestList));
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

    public List<String> getYapcCollectionNames() {
        return yapcCollectionList.stream().map(YapcCollection::getName).toList();
    }

    public void addRequestToCollection(YapcRequest yapcRequest, String collectionName) {
        for (YapcCollection yapcCollection : yapcCollectionList) {
            if (collectionName.equals(yapcCollection.getName())) {
                yapcCollection.getCollectionItemList().add(yapcRequest);
            }
        }
    }

    public void removeRequestFromCollection(String requestName, String collectionName) {
        for ( YapcCollection yapcCollection : yapcCollectionList) {
            if (collectionName.equals(yapcCollection.getName())) {
                yapcCollection.deleteItemWithNameFromCollection(requestName);
                break;
            }
        }
    }

    public YapcRequest getItemFromCollection(String collectionName, String requestName) {
        for (YapcCollection yapcCollection : yapcCollectionList)
            if (collectionName.equals(yapcCollection.getName()))
                for (YapcRequest yapcRequest : yapcCollection.getCollectionItemList())
                    if (requestName.equals(yapcRequest.name))
                        return yapcRequest;

        return null;
    }
}

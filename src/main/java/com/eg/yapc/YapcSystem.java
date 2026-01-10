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

    public List<String> getYapcCollectionNamesList() {
        return yapcCollectionList.stream().map(YapcCollection::getName).toList();
    }

    public void addRequestToCollection(YapcRequest yapcRequest, String selectedCollectionName) {
        for (YapcCollection yapcCollection : yapcCollectionList) {
            if (selectedCollectionName.equals(yapcCollection.getName())) {
                yapcCollection.getCollectionItemList().add(yapcRequest);
            }
        }
    }

    public void removeRequestWithNameFromCollection(String existingRequestName, String existingCollectionName) {
        for ( YapcCollection yapcCollection : yapcCollectionList) {
            if (existingRequestName.equals(yapcCollection.getName())) {
                yapcCollection.deleteItemWithNameFromCollection(existingRequestName);
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

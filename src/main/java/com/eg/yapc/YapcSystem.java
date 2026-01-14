package com.eg.yapc;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class YapcSystem {

    private static YapcSystem INSTANCE;

    private List<YapcCollection> yapcCollectionList;

    private YapcSystem() {

        tryLoadCollectionsFromFile();


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
                yapcCollection.getYapcRequestList().add(yapcRequest);
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
                for (YapcRequest yapcRequest : yapcCollection.getYapcRequestList())
                    if (requestName.equals(yapcRequest.name()))
                        return yapcRequest;

        return null;
    }

    public void addCollection(String collectionName) {
        yapcCollectionList.add(new YapcCollection(collectionName, new ArrayList<>()));
    }

    private void tryLoadCollectionsFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("yapc_collection_list.json");

        if (file.exists()) {
            List<YapcCollection> yapcCollections = mapper.readValue(file, new TypeReference<List<YapcCollection>>() {
            });

            this.yapcCollectionList = yapcCollections;
        } else {
            this.yapcCollectionList = new ArrayList<>();
            yapcCollectionList.add(new YapcCollection("Dummy Collection", new ArrayList<>()));
        }


    }

    public void exportCollection(String collectionName) {
        for (YapcCollection yapcCollection : yapcCollectionList)
            if (collectionName.equals(yapcCollection.getName())) {
                String name = yapcCollection.getName();
                List<YapcRequest> yapcRequestList = yapcCollection.getYapcRequestList();

            }
    }

    public void exportCollectionsToFile() {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode node = mapper.valueToTree(yapcCollectionList);

        File file = new File("yapc_collection_list.json");
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, node);
    }



}

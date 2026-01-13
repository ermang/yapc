package com.eg.yapc.controller;

import com.eg.yapc.YapcCollection;
import com.eg.yapc.YapcRequest;
import com.eg.yapc.YapcSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainSceneController {

    @FXML private TabPane mainTabPane;
    @FXML private TreeView<String> collectionsTreeView;

    private YapcSystem yapcSystem;

    @FXML
    public void initialize() throws IOException {
        yapcSystem = YapcSystem.getInstance();

        reloadCollectionsFromYapcSystem();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainTabScene.fxml"));

        Parent tabContent = loader.load();
        MainTabSceneController mainTabSceneController = loader.getController();
        mainTabSceneController.setMainSceneController(this);



        Tab tab = new Tab("New Request");
        tab.setContent(tabContent);
        tab.setClosable(true);

        mainTabSceneController.setTab(tab);

        mainTabPane.getTabs().add(tab);
        mainTabPane.getSelectionModel().select(tab);

        Tab plusTab = new Tab("+");
        plusTab.setClosable(false);

        plusTab.setOnSelectionChanged(event -> {
            if (plusTab.isSelected()) {
                try {
                    createNewTab(); // load your FXML
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // re-select the last real tab
                Tab lastTab;
                lastTab = mainTabPane.getTabs().get(mainTabPane.getTabs().size() -2);
                mainTabPane.getSelectionModel().select(lastTab);
            }
        });

        mainTabPane.getTabs().add(plusTab);
    }

    private void createNewTab() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainTabScene.fxml"));

        Parent tabContent = loader.load();
        MainTabSceneController mainTabSceneController = loader.getController();
        mainTabSceneController.setMainSceneController(this);

        Tab tab = new Tab("New Request");
        tab.setContent(tabContent);
        tab.setClosable(true);
        mainTabSceneController.setTab(tab);

        mainTabPane.getTabs().add(mainTabPane.getTabs().size() - 1, tab);


    }

    @FXML
    private void onNewCollectionClicked(ActionEvent event) {
        System.out.println("onNewCollectionClicked");

    }

    public void reloadCollectionsFromYapcSystem() {

        TreeItem<String> root = collectionsTreeView.getRoot();
        root.getChildren().clear();

        for (YapcCollection yapcCollection: yapcSystem.getYapcCollectionList()) {
            TreeItem<String> treeRoot = new TreeItem<>(yapcCollection.getName());
            treeRoot.setExpanded(true);

            for (YapcRequest yapcRequest : yapcCollection.getCollectionItemList()) {
                TreeItem<String> child = new TreeItem<>(yapcRequest.name);
                treeRoot.getChildren().add(child);
            }

            root.getChildren().add(treeRoot);
        }

        collectionsTreeView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TreeItem<String> selectedItem =
                        collectionsTreeView.getSelectionModel().getSelectedItem();

                if (selectedItem != null && !selectedItem.getParent().getValue().equals("ROOT")) {
                    try {
                        onTreeItemDoubleClicked(selectedItem);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void onTreeItemDoubleClicked(TreeItem<String> selectedItem) throws IOException {

        if (mainTabPane.getTabs().stream().map(Tab::getText).toList().contains(selectedItem.getValue()))
            return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainTabScene.fxml"));

        Parent tabContent = loader.load();
        MainTabSceneController mainTabSceneController = loader.getController();
        mainTabSceneController.setMainSceneController(this);
        YapcRequest yapcRequest = yapcSystem.getItemFromCollection(selectedItem.getParent().getValue(), selectedItem.getValue());
        mainTabSceneController.updateUiWithExistingYapcRequest(yapcRequest);
        mainTabSceneController.initData(selectedItem.getValue(), selectedItem.getParent().getValue());

        Tab tab = new Tab(selectedItem.getValue());

        tab.setContent(tabContent);
        tab.setClosable(true);

        mainTabPane.getTabs().add(mainTabPane.getTabs().size() -1, tab);
        mainTabPane.getSelectionModel().select(tab);

    }

}



package com.eg.yapc.controller;

import com.eg.yapc.YapcCollection;
import com.eg.yapc.YapcCollectionItem;
import com.eg.yapc.YapcSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainSceneController {

    @FXML private TabPane mainTabPane;
    @FXML private VBox collectionsVBox;


    @FXML
    public void initialize() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainTabScene.fxml"));

        Parent tabContent = loader.load();
        MainTabSceneController mainTabSceneController = loader.getController();
        mainTabSceneController.setMainSceneController(this);

        Tab tab = new Tab("New Request");
        tab.setContent(tabContent);
        tab.setClosable(true);

        mainTabPane.getTabs().add(tab);
        mainTabPane.getSelectionModel().select(tab);

    }

    @FXML
    private void onNewCollectionClicked(ActionEvent event) {
        System.out.println("onNewCollectionClicked");

    }

    public void reloadCollectionsFromYapcSystem() {
        collectionsVBox.getChildren().clear();

        YapcSystem yapcSystem = YapcSystem.getInstance();

        for (YapcCollection yapcCollection: yapcSystem.getYapcCollectionList()) {
            TreeItem<String> treeRoot = new TreeItem<>(yapcCollection.getName());
            treeRoot.setExpanded(true);


            for (YapcCollectionItem yapcCollectionItem : yapcCollection.getCollectionItemList()) {
                TreeItem<String> child = new TreeItem<>(yapcCollectionItem.name);
                treeRoot.getChildren().add(child);
            }

            TreeView<String> treeView = new TreeView<>(treeRoot);
            collectionsVBox.getChildren().add(treeView);
        }
    }
}



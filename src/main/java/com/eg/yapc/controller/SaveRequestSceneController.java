package com.eg.yapc.controller;

import com.eg.yapc.YapcRequest;
import com.eg.yapc.YapcSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class SaveRequestSceneController {

    private YapcSystem yapcSystem;
    private String selectedCollectionName;

    @FXML private Label responseStatusLabel;
    @FXML private TextField requestNameTextField;
    @FXML private ListView<String> collectionNamesListView;
    private String httpMethod;
    private String url;
    private Map<String, String> requestHeaderMap;
    private String requestBody;

    @FXML
    public void initialize() {
        System.out.println("FXML Loaded");
        yapcSystem = YapcSystem.getInstance();
        collectionNamesListView.getItems().addAll(yapcSystem.getYapcCollectionNames());

        collectionNamesListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    System.out.println("Selected: " + newVal);
                    selectedCollectionName = newVal;
                });

    }


    @FXML
    private void onSaveClicked(ActionEvent actionEvent) {
        System.out.println("saveClicked Loaded");
        System.out.println("RequestnameTextField=" + requestNameTextField.getText());
        System.out.println("selectedCollectionName=" + selectedCollectionName);

        if (requestNameTextField.getText().isBlank()) {

            return;
        }

        if (selectedCollectionName.isBlank()) {

            return;
        }

        YapcRequest yapcRequest = new YapcRequest(httpMethod, requestNameTextField.getText().trim(), url, requestHeaderMap, requestBody);

        yapcSystem.addRequestToCollection(yapcRequest, selectedCollectionName);

        Stage stage = (Stage) requestNameTextField.getScene().getWindow();
        stage.close();
    }


    public void initData(String httpMethod, String url, Map<String, String> requestHeadersMap, String requestBody) {
        this.httpMethod= httpMethod;
        this.url = url;
        this.requestHeaderMap = requestHeadersMap;
        this.requestBody = requestBody;
    }

    public String getRequestName() {
        return requestNameTextField.getText().trim();
    }
}



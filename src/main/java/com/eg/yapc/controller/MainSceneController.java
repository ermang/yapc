package com.eg.yapc.controller;

import com.eg.yapc.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class MainSceneController {

    @FXML private Label responseStatusLabel;
    @FXML private TextField urlTextField;
    @FXML private ComboBox<String> httpMethodComboBox;
    @FXML private TextArea requestBodyTextArea;

//    @FXML private TreeView collectionsTreeView;
    @FXML private VBox collectionsVBox;

    @FXML
    private TableView<RequestHeaderItem> requestHeaderTableView;
    @FXML
    private TableColumn<RequestHeaderItem, String> requestHeaderNameColumn;

    @FXML
    private TableColumn<RequestHeaderItem, String> requestHeaderValueColumn;

    @FXML
    private TableColumn<RequestHeaderItem, Void> addRemoveButtonColumn;

    @FXML
    private TextArea responseHeadersTextArea;

    @FXML
    private TextArea responseBodyTextArea;


    // Optional: initialize method called after FXML loaded
    @FXML
    public void initialize() {
        System.out.println("FXML Loaded");
        //collectionsVBox.setVgrow(treeView, Priority.NEVER);
        httpMethodComboBox.getSelectionModel().selectFirst();


        //request header block begin
        requestHeaderTableView.setEditable(true);

        requestHeaderNameColumn.setCellValueFactory(cellData ->
                cellData.getValue().headerNameProperty()
        );

        requestHeaderNameColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                FXCollections.observableArrayList(YapcConstant.REQUEST_HEADER_NAME_LIST)
        ));

        requestHeaderValueColumn.setCellValueFactory(cellData ->
                cellData.getValue().headerValueProperty()
        );

        requestHeaderValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        requestHeaderValueColumn.setOnEditCommit(event ->
                event.getRowValue().setHeaderValue(event.getNewValue())
        );



        requestHeaderTableView.setRowFactory(tv -> {
            TableRow<RequestHeaderItem> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    TablePosition<?, ?> pos = requestHeaderTableView.getFocusModel().getFocusedCell();
                    if (pos != null && pos.getTableColumn() == requestHeaderNameColumn) {
                        int rowIndex = row.getIndex();
                        requestHeaderTableView.edit(rowIndex, requestHeaderNameColumn);
                    }
                }
            });

            return row;
        });

        requestHeaderTableView.getSelectionModel().setCellSelectionEnabled(true);


        // Button column
        addRemoveButtonColumn.setCellFactory(col -> new TableCell<>() {
            //private final Button button = new Button();

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                Button button = new Button();

                int rowIndex = getIndex();
                ObservableList<RequestHeaderItem> items = getTableView().getItems();

                if (rowIndex == items.size() - 1) {
                    // Last row: "+" to add
                    button.setText("+");
                    button.setOnAction(e -> {
                        items.add(new RequestHeaderItem("", "")); // new empty row
                        getTableView().refresh();
                    });
                } else {
                    // Previous rows: "-" to remove
                    button.setText("-");
                    button.setOnAction(e -> {
                        items.remove(rowIndex);
                        getTableView().refresh();
                    });
                }

                setGraphic(button);
            }
        });

        requestHeaderTableView.setItems(FXCollections.observableArrayList(
                new RequestHeaderItem("Content-Type", "application/json"),
                new RequestHeaderItem("User-Agent", "Java/17 HttpClient")
        ));


        //request header block end

        requestBodyTextArea.setText("""
                {
                  "key": "value"
                }
                """ );

        //set request header table size dynamically
        requestHeaderTableView.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Platform.runLater(() -> {
                    // this runs AFTER the TableView is in the scene and layout is done
                    Node header = requestHeaderTableView.lookup("TableHeaderRow");
                    double headerHeight = header.prefHeight(-1);
                    double rowHeight = requestHeaderTableView.getFixedCellSize() > 0
                            ? requestHeaderTableView.getFixedCellSize()
                            : 24;
                    int visibleRows = requestHeaderTableView.getItems().size() + 3; //show ~3 empty rows

                    requestHeaderTableView.setPrefHeight(headerHeight + rowHeight * visibleRows);
                    requestHeaderTableView.setMinHeight(Region.USE_PREF_SIZE);
                    requestHeaderTableView.setMaxHeight(Region.USE_PREF_SIZE);
                });
            }
        });


        urlTextField.setText("https://httpbin.org/get"); //TODO: remove me
    }

    @FXML
    private void onSendClick(ActionEvent actionEvent) throws IOException, InterruptedException {
        System.out.println("onSendClick");

        String httpMethod = httpMethodComboBox.getSelectionModel().getSelectedItem();

        HttpClient client = HttpClient.newHttpClient();

        List<String> requestHeadersList = new ArrayList<>();
        for (RequestHeaderItem requestHeaderItem : requestHeaderTableView.getItems()) {
            requestHeadersList.add(requestHeaderItem.getHeaderName());
            requestHeadersList.add(requestHeaderItem.getHeaderValue());
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlTextField.getText().trim()))
                .headers(requestHeadersList.toArray(new String[0]))
                .method(httpMethod,
                        httpMethod.equals("GET") || requestBodyTextArea.getText().trim().isBlank() ? HttpRequest.BodyPublishers.noBody() :
                                HttpRequest.BodyPublishers.ofString(requestBodyTextArea.getText().trim()))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        responseStatusLabel.setText(String.valueOf(response.statusCode()));
        System.out.println("Body:\n" + response.body());
        Map<String, List<String>> responseHeadersMap = response.headers().map();

        responseHeadersTextArea.setText("");

        for (Map.Entry<String, List<String>> entry : new TreeMap<> (responseHeadersMap).entrySet()) {
            responseHeadersTextArea.appendText(entry.getKey() + ":");
            responseHeadersTextArea.appendText(String.join(" ", entry.getValue()));
            responseHeadersTextArea.appendText(System.lineSeparator());
        }

        responseBodyTextArea.setText(String.valueOf(response.body()));
        System.out.println("Response Header:\n" + response.headers());
    }

    @FXML
    private void onSaveClick(ActionEvent actionEvent) {
        System.out.println("Save clicked");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SaveRequestScene.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setTitle("Save request to collection");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); // optional
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

        reloadCollectionsFromYapcSystem();
    }

    @FXML
    private void onNewCollectionClicked(ActionEvent event) {
        System.out.println("onNewCollectionClicked");

    }

    private void reloadCollectionsFromYapcSystem() {
        collectionsVBox.getChildren().clear();

        YapcSystem yapcSystem = YapcSystem.getInstance();

        for (YapcCollection yapcCollection: yapcSystem.getYapcCollectionList()) {
            TreeItem<String> treeRoot = new TreeItem<>(yapcCollection.getName());
            treeRoot.setExpanded(true);


            for (YapcCollectionItem yapcCollectionItem : yapcCollection.getCollectionItemList()) {
                TreeItem<String> child = new TreeItem<>(yapcCollectionItem.getName());
                treeRoot.getChildren().add(child);
            }

            TreeView<String> treeView = new TreeView<>(treeRoot);
            collectionsVBox.getChildren().add(treeView);
        }
    }
}



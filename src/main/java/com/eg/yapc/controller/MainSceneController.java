package com.eg.yapc.controller;

import com.eg.yapc.RequestHeaderItem;
import com.eg.yapc.YapcConstant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MainSceneController {

    @FXML private Label responseStatusLabel;
    @FXML private TextField urlTextField;
    @FXML private ComboBox<String> httpMethodComboBox;
    @FXML private TextArea requestBodyTextArea;

    @FXML
    private TableView<RequestHeaderItem> requestHeaderTableView;
    @FXML
    private TableColumn<RequestHeaderItem, String> requestHeaderNameColumn;

    @FXML
    private TableColumn<RequestHeaderItem, String> requestHeaderValueColumn;

    @FXML
    private TableColumn<RequestHeaderItem, Void> addRemoveButtonColumn;

    @FXML
    private TextArea responseBodyTextArea;


    // Optional: initialize method called after FXML loaded
    @FXML
    public void initialize() {
        System.out.println("FXML Loaded");
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
                new RequestHeaderItem("Content-Type:", "application/json"),
                new RequestHeaderItem("User-Agent:", "Java/17 HttpClient")
        ));


        //request header block end

        requestBodyTextArea.setText("""
                {
                  "key": "value"
                }
                """ );

    }

    @FXML
    private void onSendClick(ActionEvent actionEvent) throws IOException, InterruptedException {
        System.out.println("onSendClick");

        String httpMethod = httpMethodComboBox.getSelectionModel().getSelectedItem();

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlTextField.getText().trim()))
                //.headers()
                .method(httpMethod,
                        httpMethod.equals("GET") || requestBodyTextArea.getText().trim().isBlank() ? HttpRequest.BodyPublishers.noBody() :
                                HttpRequest.BodyPublishers.ofString(requestBodyTextArea.getText().trim()))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        responseStatusLabel.setText(String.valueOf(response.statusCode()));
        System.out.println("Body:\n" + response.body());
        responseBodyTextArea.setText(String.valueOf(response.body()));
    }
}



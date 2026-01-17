package com.eg.yapc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainScene.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, Color.TRANSPARENT);
        stage.setTitle("YAPC");

        stage.getIcons().addAll(
                new Image(getClass().getResourceAsStream("/icons/app-icon-16.png")),
                new Image(getClass().getResourceAsStream("/icons/app-icon-32.png")),
                new Image(getClass().getResourceAsStream("/icons/app-icon-64.png")),
                new Image(getClass().getResourceAsStream("/icons/app-icon-128.png"))
        );


        stage.setScene(scene);

        stage.setOnCloseRequest(event ->{
            System.out.println("writing to file");
            YapcSystem.getInstance().exportCollectionsToFile();
        } );
        stage.show();
    }
}

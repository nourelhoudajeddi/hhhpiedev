package tn.greenly.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;


import java.io.IOException;

import javafx.scene.Parent; // <-- Cet import manquait !

public class mainfx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterModule.fxml"));


        try {
            Parent parent=loader.load();
            Scene scene=new Scene(parent);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
}
    }
}
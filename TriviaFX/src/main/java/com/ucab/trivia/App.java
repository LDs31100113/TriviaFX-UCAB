package com.ucab.trivia;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        GestorVistas.setStage(primaryStage);
        primaryStage.setResizable(false);
        primaryStage.setTitle("TRIVIA-UCAB");
        GestorVistas.mostrarMenuPrincipal();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
package com.ucab.trivia;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // 1. Se crea la única instancia de GestorVistas, pasándole la ventana principal (Stage).
        GestorVistas gestorVistas = new GestorVistas(primaryStage);

        // 2. Se configura el título y se le pide al gestor que muestre la primera vista.
        primaryStage.setResizable(false);
        primaryStage.setTitle("TRIVIA-UCAB");
        gestorVistas.mostrarMenuPrincipal(); // Se usa la instancia, no un método estático
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
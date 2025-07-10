package com.ucab.trivia;

import com.ucab.trivia.GestorVistas;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Clase principal y punto de entrada para la aplicación de escritorio TRIVIA-UCAB.
 * Esta clase es responsable de iniciar el entorno de JavaFX, configurar la ventana
 * principal (Stage) y delegar el control de las vistas al {@link GestorVistas}.
 */
public class App extends Application {

    /**
     * El método de entrada principal para todas las aplicaciones JavaFX.
     * Se llama después de que el sistema está listo para que la aplicación comience a funcionar.
     *
     * @param primaryStage El escenario o ventana principal de la aplicación, proporcionado por el runtime de JavaFX.
     * @throws IOException Si ocurre un error al cargar el archivo FXML para la vista inicial.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        // 1. Se crea la única instancia de GestorVistas, pasándole la ventana principal (Stage).
        GestorVistas gestorVistas = new GestorVistas(primaryStage);

        // 2. Se configura el título y se le pide al gestor que muestre la primera vista.
        primaryStage.setResizable(false);
        primaryStage.setTitle("TRIVIA-UCAB");
        gestorVistas.mostrarMenuPrincipal();
        primaryStage.show();
    }

    /**
     * El punto de entrada principal del programa.
     * Llama al método launch() de la clase Application, que se encarga de iniciar la aplicación JavaFX.
     *
     * @param args Argumentos de la línea de comandos (no se utilizan en esta aplicación).
     */
    public static void main(String[] args) {
        launch();
    }
}
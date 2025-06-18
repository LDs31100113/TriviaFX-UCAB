package com.ucab.trivia;

import com.ucab.trivia.controlador.VentanaJuegoController;
import com.ucab.trivia.modelo.PerfilJugador;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Clase de utilidad para gestionar la navegación entre las diferentes vistas (escenas) de la aplicación.
 * Versión final con corrección para evitar que la UI se congele al cargar vistas pesadas.
 */
public class GestorVistas {
    private static Stage stage;

    public static void setStage(Stage stage) {
        GestorVistas.stage = stage;
    }

    private static void cambiarEscena(Parent root) {
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    private static FXMLLoader getLoader(String fxml) {
        return new FXMLLoader(App.class.getResource("vista/" + fxml + ".fxml"));
    }

    public static void mostrarMenuPrincipal() {
        try {
            cambiarEscena(getLoader("MenuPrincipal").load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mostrarSeleccionJugador() {
        try {
            cambiarEscena(getLoader("SeleccionJugador").load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra la ventana del juego. Primero muestra la ventana vacía y luego,
     * usando Platform.runLater, ejecuta la inicialización pesada para no congelar la UI.
     */
    public static void mostrarVentanaJuego(List<PerfilJugador> jugadoresSeleccionados, boolean esPartidaCargada) {
        try {
            FXMLLoader loader = getLoader("VentanaJuego");
            Parent root = loader.load();
            VentanaJuegoController controller = loader.getController();

            // --- INICIO DE LA CORRECCIÓN ---
            // 1. Mostrar la nueva ventana INMEDIATAMENTE.
            cambiarEscena(root);

            // 2. Usar Platform.runLater para diferir el trabajo pesado de inicialización.
            // Esto permite que la UI se muestre primero y luego se cargue el contenido.
            Platform.runLater(() -> controller.iniciarJuego(jugadoresSeleccionados, esPartidaCargada));
            // --- FIN DE LA CORRECCIÓN ---

        } catch (IOException e) {
            System.err.println("Error al cargar la vista de juego: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void mostrarEstadisticas() {
        try {
            cambiarEscena(getLoader("Estadisticas").load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
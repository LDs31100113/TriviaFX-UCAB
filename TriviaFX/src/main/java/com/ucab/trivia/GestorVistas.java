package com.ucab.trivia;

import com.ucab.trivia.controlador.VentanaJuegoController;
import com.ucab.trivia.modelo.PerfilJugador;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Clase de utilidad para gestionar la navegación entre las diferentes vistas (escenas) de la aplicación.
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
     * **MÉTODO CORREGIDO**
     * Ahora acepta dos parámetros: la lista de jugadores (para una partida nueva)
     * y un booleano para indicar si se debe cargar una partida guardada.
     * @param jugadoresSeleccionados La lista de perfiles de jugadores para una nueva partida. Puede ser null si se carga una partida.
     * @param cargarPartida true si se debe cargar la última partida guardada, false si es una nueva partida.
     */
    public static void mostrarVentanaJuego(List<PerfilJugador> jugadoresSeleccionados, boolean cargarPartida) {
        try {
            FXMLLoader loader = getLoader("VentanaJuego");
            Parent root = loader.load();

            // Obtener el controlador de la ventana de juego
            VentanaJuegoController controller = loader.getController();
            // Llamar a su método de inicialización pasándole los datos necesarios
            controller.iniciarJuego(jugadoresSeleccionados, cargarPartida);

            cambiarEscena(root);
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
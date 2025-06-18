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
 * Versión actualizada para ajustar el tamaño de la ventana en cada cambio de escena.
 */
public class GestorVistas {
    private static Stage stage;

    public static void setStage(Stage stage) {
        GestorVistas.stage = stage;
    }

    /**
     * Carga el archivo FXML especificado y lo establece como la raíz de la escena actual.
     * **Ahora también ajusta el tamaño de la ventana y la centra.**
     * @param root El nodo raíz de la nueva vista cargada desde el FXML.
     */
    private static void cambiarEscena(Parent root) {
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }

        // --- INICIO DE LA CORRECCIÓN ---
        // Estas dos líneas solucionan el problema del contenido "cortado".
        stage.sizeToScene(); // Ajusta el tamaño de la ventana al tamaño preferido de la nueva vista.
        stage.centerOnScreen(); // Opcional: Centra la ventana redimensionada en la pantalla.
        // --- FIN DE LA CORRECCIÓN ---
    }

    private static FXMLLoader getLoader(String fxml) {
        // La ruta ahora es relativa a la clase App, que está en com.ucab.trivia
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
     * Muestra la ventana del juego, pasando los jugadores seleccionados al controlador.
     * @param jugadoresSeleccionados La lista de perfiles de jugadores para una nueva partida. Puede ser null si se carga una partida.
     * @param esPartidaCargada true si se debe cargar la última partida guardada, false si es una nueva partida.
     */
    public static void mostrarVentanaJuego(List<PerfilJugador> jugadoresSeleccionados, boolean esPartidaCargada) {
        try {
            FXMLLoader loader = getLoader("VentanaJuego");
            Parent root = loader.load();

            VentanaJuegoController controller = loader.getController();
            controller.iniciarJuego(jugadoresSeleccionados, esPartidaCargada);

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
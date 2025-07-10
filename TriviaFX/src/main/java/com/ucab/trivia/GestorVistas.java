package com.ucab.trivia;

import com.ucab.trivia.controlador.*;
import com.ucab.trivia.modelo.Juego;
import com.ucab.trivia.modelo.PerfilJugador;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Gestiona la navegación y transición entre las diferentes vistas (escenas) de la aplicación.
 * Es responsable de cargar los archivos FXML, configurar sus controladores
 * e inyectar las dependencias necesarias, como una referencia a sí mismo.
 */
public class GestorVistas {

    /** La ventana principal (Stage) de la aplicación donde se muestran todas las escenas. */
    private final Stage stage;

    /**
     * Construye un nuevo gestor de vistas.
     * @param stage La ventana principal (Stage) que controlará este gestor.
     */
    public GestorVistas(Stage stage) {
        this.stage = stage;
    }

    /**
     * Crea y devuelve un FXMLLoader para un archivo FXML específico.
     * Estandariza la ruta base a la carpeta 'vista'.
     *
     * @param fxml El nombre del archivo FXML (sin la extensión .fxml).
     * @return Una instancia de FXMLLoader configurada con la ruta al archivo.
     */
    private FXMLLoader getLoader(String fxml) {
        // App.class.getResource(...) busca el recurso relativo a la ubicación de la clase App.
        return new FXMLLoader(App.class.getResource("vista/" + fxml + ".fxml"));
    }

    /**
     * Cambia la escena actual en el Stage principal.
     * Si no hay escena, crea una nueva; si ya existe, reemplaza su contenido.
     *
     * @param root El nodo raíz (Parent) de la nueva vista a mostrar.
     */
    private void cambiarEscena(Parent root) {
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        // Ajusta el tamaño de la ventana al contenido y la centra en la pantalla.
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    /**
     * Carga y muestra la vista del menú principal.
     */
    public void mostrarMenuPrincipal() {
        try {
            FXMLLoader loader = getLoader("MenuPrincipal");
            Parent root = loader.load();
            MenuPrincipalController controller = loader.getController();
            // Inyecta este gestor en el controlador para que pueda solicitar cambios de vista.
            controller.setGestorVistas(this);
            cambiarEscena(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga y muestra la vista de selección de jugador.
     */
    public void mostrarSeleccionJugador() {
        try {
            FXMLLoader loader = getLoader("SeleccionJugador");
            Parent root = loader.load();
            SeleccionJugadorController controller = loader.getController();
            controller.setGestorVistas(this);
            cambiarEscena(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga y muestra la vista principal del juego.
     *
     * @param jugadoresSeleccionados La lista de perfiles de los jugadores que participarán.
     * @param esPartidaCargada       Indica si se debe iniciar una nueva partida o cargar una guardada.
     */
    public void mostrarVentanaJuego(List<PerfilJugador> jugadoresSeleccionados, boolean esPartidaCargada) {
        try {
            FXMLLoader loader = getLoader("VentanaJuego");
            Parent root = loader.load();
            VentanaJuegoController controller = loader.getController();
            controller.setGestorVistas(this); // Inyectar dependencia

            cambiarEscena(root);

            // Se usa Platform.runLater para asegurarse de que la UI esté completamente cargada
            // antes de ejecutar la lógica de inicio del juego, que puede ser pesada.
            Platform.runLater(() -> controller.iniciarJuego(jugadoresSeleccionados, esPartidaCargada));
        } catch (IOException e) {
            System.err.println("Error al cargar la vista de juego: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carga y muestra la vista de estadísticas.
     */
    public void mostrarEstadisticas() {
        try {
            FXMLLoader loader = getLoader("Estadisticas");
            Parent root = loader.load();
            EstadisticasController controller = loader.getController();
            controller.setGestorVistas(this);
            cambiarEscena(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
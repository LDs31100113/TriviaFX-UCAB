package com.ucab.trivia;

import com.ucab.trivia.controlador.VentanaJuegoController;
import com.ucab.trivia.modelo.PerfilJugador;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GestorVistas {
    private static Stage stage;

    public static void setStage(Stage stage) {
        GestorVistas.stage = stage;
    }

    private static void cargarVista(String fxml) {
        try {
            Parent root = FXMLLoader.load(App.class.getResource("vista/" + fxml + ".fxml"));
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Error al cargar la vista: " + fxml);
            e.printStackTrace();
        }
    }

    public static void mostrarMenuPrincipal() {
        cargarVista("MenuPrincipal");
    }

    public static void mostrarSeleccionJugador() {
        cargarVista("SeleccionJugador");
    }

    public static void mostrarVentanaJuego(List<PerfilJugador> jugadoresSeleccionados) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("vista/VentanaJuego.fxml"));
            Parent root = loader.load();

            VentanaJuegoController controller = loader.getController();
            controller.iniciarJuego(jugadoresSeleccionados);

            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Error al cargar la vista de juego");
            e.printStackTrace();
        }
    }

    public static void mostrarEstadisticas() {
        cargarVista("Estadisticas");
    }
}
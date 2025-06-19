package com.ucab.trivia;

import com.ucab.trivia.controlador.*;
import com.ucab.trivia.modelo.PerfilJugador;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GestorVistas {
    private final Stage stage;

    public GestorVistas(Stage stage) {
        this.stage = stage;
    }

    private FXMLLoader getLoader(String fxml) {
        return new FXMLLoader(App.class.getResource("vista/" + fxml + ".fxml"));
    }

    private void cambiarEscena(Parent root) {
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    public void mostrarMenuPrincipal() {
        try {
            FXMLLoader loader = getLoader("MenuPrincipal");
            Parent root = loader.load();
            MenuPrincipalController controller = loader.getController();
            controller.setGestorVistas(this);
            cambiarEscena(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public void mostrarVentanaJuego(List<PerfilJugador> jugadoresSeleccionados, boolean esPartidaCargada) {
        try {
            FXMLLoader loader = getLoader("VentanaJuego");
            Parent root = loader.load();
            VentanaJuegoController controller = loader.getController();
            controller.setGestorVistas(this); // Inyectar dependencia

            cambiarEscena(root);

            Platform.runLater(() -> controller.iniciarJuego(jugadoresSeleccionados, esPartidaCargada));
        } catch (IOException e) {
            System.err.println("Error al cargar la vista de juego: " + e.getMessage());
            e.printStackTrace();
        }
    }

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
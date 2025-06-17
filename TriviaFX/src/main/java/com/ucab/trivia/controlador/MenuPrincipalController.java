package com.ucab.trivia.controlador;

import com.ucab.trivia.GestorVistas;
import com.ucab.trivia.modelo.ServicioDatos;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class MenuPrincipalController {
    @FXML
    private Button btnPartidaGuardada;
    private final ServicioDatos servicioDatos = new ServicioDatos();

    @FXML
    public void initialize() {
        // Deshabilitar el botón de cargar si no existe una partida guardada
        btnPartidaGuardada.setDisable(!servicioDatos.existePartidaGuardada());
    }

    @FXML
    private void onNuevaPartida() {
        GestorVistas.mostrarSeleccionJugador();
    }

    @FXML
    private void onPartidaGuardada() {
        // La lógica de carga se pasa a la ventana de juego
        GestorVistas.mostrarVentanaJuego(null, true);
    }

    @FXML
    private void onEstadisticas() {
        GestorVistas.mostrarEstadisticas();
    }

    @FXML
    private void onSalir() {
        Platform.exit();
    }
}
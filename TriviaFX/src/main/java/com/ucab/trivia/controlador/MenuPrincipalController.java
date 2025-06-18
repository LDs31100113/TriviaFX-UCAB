package com.ucab.trivia.controlador;

import com.ucab.trivia.GestorVistas;
import com.ucab.trivia.modelo.ServicioDatos;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controlador para la vista del menú principal (MenuPrincipal.fxml).
 * Maneja la navegación a las otras ventanas de la aplicación.
 */
public class MenuPrincipalController {
    @FXML
    private Button btnPartidaGuardada;

    private ServicioDatos servicioDatos;

    @FXML
    public void initialize() {
        this.servicioDatos = new ServicioDatos();
        // Deshabilitar el botón de cargar si no existe un archivo de partida guardada
        btnPartidaGuardada.setDisable(!servicioDatos.existePartidaGuardada());
    }

    @FXML
    private void onNuevaPartida() {
        GestorVistas.mostrarSeleccionJugador();
    }

    @FXML
    private void onPartidaGuardada() {
        // **LLAMADA CORREGIDA**
        // Llama a mostrarVentanaJuego pasando null para la lista de jugadores
        // y 'true' para indicar que se debe cargar una partida.
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
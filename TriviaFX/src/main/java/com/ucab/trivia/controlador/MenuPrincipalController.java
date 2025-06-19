package com.ucab.trivia.controlador;

import com.ucab.trivia.GestorVistas;
import com.ucab.trivia.modelo.ServicioDatos;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuPrincipalController {
    @FXML private Button btnPartidaGuardada;

    private ServicioDatos servicioDatos;
    private GestorVistas gestorVistas; // Atributo para guardar la instancia

    // Método para que GestorVistas pueda inyectar la dependencia
    public void setGestorVistas(GestorVistas gestorVistas) {
        this.gestorVistas = gestorVistas;
    }

    @FXML
    public void initialize() {
        this.servicioDatos = new ServicioDatos();
        btnPartidaGuardada.setDisable(!servicioDatos.existePartidaGuardada());
    }

    @FXML
    private void onNuevaPartida() {
        gestorVistas.mostrarSeleccionJugador(); // Se usa la instancia
    }

    @FXML
    private void onPartidaGuardada() {
        gestorVistas.mostrarVentanaJuego(null, true); // Se usa la instancia
    }

    @FXML
    private void onEstadisticas() {
        gestorVistas.mostrarEstadisticas(); // Se usa la instancia
    }

    @FXML
    private void onSalir() {
        Platform.exit();
    }
}
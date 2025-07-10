package com.ucab.trivia.controlador;

import com.ucab.trivia.GestorVistas;
import com.ucab.trivia.modelo.ServicioDatos;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controlador para la vista del menú principal (MenuPrincipal.fxml).
 * Maneja la navegación inicial de la aplicación, permitiendo al usuario
 * iniciar una partida nueva, cargar una existente, ver estadísticas o salir.
 */
public class MenuPrincipalController {

    @FXML
    private Button btnPartidaGuardada;

    private ServicioDatos servicioDatos;
    private GestorVistas gestorVistas;

    /**
     * Inyecta la dependencia del gestor de vistas. Este método es llamado
     * por el GestorVistas al cargar esta escena para permitir la navegación.
     * @param gestorVistas La instancia única del gestor de vistas de la aplicación.
     */
    public void setGestorVistas(GestorVistas gestorVistas) {
        this.gestorVistas = gestorVistas;
    }

    /**
     * Se llama automáticamente después de que se carga el archivo FXML.
     * Inicializa el servicio de datos y deshabilita el botón "Partida Guardada"
     * si no se encuentra un archivo de guardado existente.
     */
    @FXML
    public void initialize() {
        this.servicioDatos = new ServicioDatos();
        btnPartidaGuardada.setDisable(!servicioDatos.existePartidaGuardada());
    }

    /**
     * Maneja el evento de clic en el botón "Partida Nueva".
     * Llama al GestorVistas para navegar a la pantalla de selección de jugadores.
     */
    @FXML
    private void onNuevaPartida() {
        gestorVistas.mostrarSeleccionJugador();
    }

    /**
     * Maneja el evento de clic en el botón "Partida Guardada".
     * Llama al GestorVistas para cargar la partida guardada y navegar a la pantalla de juego.
     */
    @FXML
    private void onPartidaGuardada() {
        gestorVistas.mostrarVentanaJuego(null, true);
    }

    /**
     * Maneja el evento de clic en el botón "Estadísticas".
     * Llama al GestorVistas para navegar a la pantalla de estadísticas.
     */
    @FXML
    private void onEstadisticas() {
        gestorVistas.mostrarEstadisticas();
    }

    /**
     * Maneja el evento de clic en el botón "Salir".
     * Cierra la aplicación de forma segura.
     */
    @FXML
    private void onSalir() {
        Platform.exit();
    }
}
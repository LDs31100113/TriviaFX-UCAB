package com.ucab.trivia.controlador;

import com.ucab.trivia.GestorVistas;
import com.ucab.trivia.modelo.PerfilJugador;
import com.ucab.trivia.modelo.ServicioDatos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para la vista de selección de jugadores (SeleccionJugador.fxml).
 * Permite al usuario elegir los participantes para una nueva partida a partir de
 * una lista de perfiles predefinidos en un archivo JSON.
 */
public class SeleccionJugadorController {

    @FXML private ListView<PerfilJugador> listaDisponibles;
    @FXML private ListView<PerfilJugador> listaSeleccionados;

    private final ServicioDatos servicioDatos = new ServicioDatos();
    private final ObservableList<PerfilJugador> jugadoresDisponibles = FXCollections.observableArrayList();
    private final ObservableList<PerfilJugador> jugadoresSeleccionados = FXCollections.observableArrayList();
    private GestorVistas gestorVistas;

    /**
     * Inyecta la dependencia del gestor de vistas para permitir la navegación.
     * @param gestorVistas La instancia única del gestor de vistas.
     */
    public void setGestorVistas(GestorVistas gestorVistas) {
        this.gestorVistas = gestorVistas;
    }

    /**
     * Se ejecuta al cargar la vista. Carga los perfiles de jugadores desde
     * el archivo JSON y los muestra en la lista de jugadores disponibles.
     */
    @FXML
    public void initialize() {
        List<PerfilJugador> perfiles = servicioDatos.cargarPerfiles();
        jugadoresDisponibles.setAll(perfiles);
        listaDisponibles.setItems(jugadoresDisponibles);
        listaSeleccionados.setItems(jugadoresSeleccionados);
    }

    /**
     * Maneja el evento de clic en el botón ">".
     * Mueve un jugador de la lista de disponibles a la de seleccionados.
     */
    @FXML
    private void onAgregarJugador() {
        PerfilJugador seleccionado = listaDisponibles.getSelectionModel().getSelectedItem();
        if (seleccionado != null && jugadoresSeleccionados.size() < 6) {
            jugadoresDisponibles.remove(seleccionado);
            jugadoresSeleccionados.add(seleccionado);
        } else if (jugadoresSeleccionados.size() >= 6) {
            mostrarAlerta("Máximo de Jugadores", "No se pueden seleccionar más de 6 jugadores.");
        }
    }

    /**
     * Maneja el evento de clic en el botón "<".
     * Devuelve un jugador de la lista de seleccionados a la de disponibles.
     */
    @FXML
    private void onQuitarJugador() {
        PerfilJugador seleccionado = listaSeleccionados.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            jugadoresSeleccionados.remove(seleccionado);
            jugadoresDisponibles.add(seleccionado);
        }
    }

    /**
     * Maneja el evento de clic en el botón "Jugar".
     * Valida la selección y navega a la ventana del juego.
     */
    @FXML
    private void onJugar() {
        if (jugadoresSeleccionados.isEmpty() || jugadoresSeleccionados.size() > 6) {
            mostrarAlerta("Error de Selección", "Debe seleccionar entre 1 y 6 jugadores para comenzar.");
            return;
        }
        gestorVistas.mostrarVentanaJuego(new ArrayList<>(jugadoresSeleccionados), false);
    }

    /**
     * Maneja el evento de clic en el botón "Regresar".
     * Vuelve al menú principal.
     */
    @FXML
    private void onRegresar() {
        gestorVistas.mostrarMenuPrincipal();
    }

    /**
     * Maneja el evento de clic en el botón "Salir".
     * Cierra la aplicación.
     */
    @FXML
    private void onSalir() {
        Platform.exit();
    }

    /**
     * Muestra una ventana de alerta con un mensaje.
     * @param titulo El título de la ventana de alerta.
     * @param mensaje El mensaje a mostrar dentro de la alerta.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
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

import java.util.ArrayList; // <<--- IMPORTACIÓN AÑADIDA PARA CORREGIR EL ERROR
import java.util.List;

public class SeleccionJugadorController {

    @FXML
    private ListView<PerfilJugador> listaDisponibles;
    @FXML
    private ListView<PerfilJugador> listaSeleccionados;

    private final ServicioDatos servicioDatos = new ServicioDatos();
    private final ObservableList<PerfilJugador> jugadoresDisponibles = FXCollections.observableArrayList();
    private final ObservableList<PerfilJugador> jugadoresSeleccionados = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        List<PerfilJugador> perfiles = servicioDatos.cargarPerfiles();
        jugadoresDisponibles.setAll(perfiles);
        listaDisponibles.setItems(jugadoresDisponibles);
        listaSeleccionados.setItems(jugadoresSeleccionados);
    }

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

    @FXML
    private void onQuitarJugador() {
        PerfilJugador seleccionado = listaSeleccionados.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            jugadoresSeleccionados.remove(seleccionado);
            jugadoresDisponibles.add(seleccionado);
        }
    }

    @FXML
    private void onJugar() {
        if (jugadoresSeleccionados.isEmpty() || jugadoresSeleccionados.size() > 6) {
            mostrarAlerta("Error de Selección", "Debe seleccionar entre 1 y 6 jugadores para comenzar.");
            return;
        }
        GestorVistas.mostrarVentanaJuego(new ArrayList<>(jugadoresSeleccionados), false);
    }

    @FXML
    private void onRegresar() {
        GestorVistas.mostrarMenuPrincipal();
    }

    @FXML
    private void onSalir() {
        Platform.exit();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
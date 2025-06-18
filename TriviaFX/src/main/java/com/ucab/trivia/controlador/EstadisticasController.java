package com.ucab.trivia.controlador;

import com.ucab.trivia.GestorVistas;
import com.ucab.trivia.modelo.EstadisticaGlobal;
import com.ucab.trivia.modelo.ServicioDatos;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstadisticasController {

    @FXML private TableView<EstadisticaGlobal> tablaEstadisticas;
    @FXML private TableColumn<EstadisticaGlobal, String> colAlias;
    @FXML private TableColumn<EstadisticaGlobal, Integer> colGanadas;
    @FXML private TableColumn<EstadisticaGlobal, Integer> colPerdidas;
    @FXML private TableColumn<EstadisticaGlobal, Integer> colJugadas;
    @FXML private TableColumn<EstadisticaGlobal, String> colCorrectas;
    @FXML private TableColumn<EstadisticaGlobal, String> colTiempo;

    private final ServicioDatos servicioDatos = new ServicioDatos();

    @FXML
    public void initialize() {
        colAlias.setCellValueFactory(new PropertyValueFactory<>("alias"));
        colGanadas.setCellValueFactory(new PropertyValueFactory<>("partidasGanadas"));
        colPerdidas.setCellValueFactory(new PropertyValueFactory<>("partidasPerdidas"));
        colJugadas.setCellValueFactory(new PropertyValueFactory<>("partidasJugadas"));

        colCorrectas.setCellValueFactory(cellData -> {
            if (cellData.getValue().getCorrectasPorCategoria() == null) return new SimpleStringProperty("");
            Map<String, Integer> mapa = cellData.getValue().getCorrectasPorCategoria().entrySet().stream()
                    .collect(Collectors.toMap(e -> e.getKey().getNombreMostrado().substring(0,1), Map.Entry::getValue));
            return new SimpleStringProperty(mapa.toString());
        });

        colTiempo.setCellValueFactory(cellData -> {
            double segundos = cellData.getValue().getTiempoTotalRespuestasCorrectasMs() / 1000.0;
            return new SimpleStringProperty(String.format("%.2f", segundos));
        });

        cargarYMostrarEstadisticas();
    }

    private void cargarYMostrarEstadisticas() {
        List<EstadisticaGlobal> estadisticas = servicioDatos.cargarEstadisticas();
        estadisticas.sort(Comparator.comparingInt(EstadisticaGlobal::getPartidasGanadas).reversed());

        ObservableList<EstadisticaGlobal> datosObservables = FXCollections.observableArrayList(estadisticas);
        tablaEstadisticas.setItems(datosObservables);
    }

    @FXML
    private void onRegresar() {
        GestorVistas.mostrarMenuPrincipal();
    }
}
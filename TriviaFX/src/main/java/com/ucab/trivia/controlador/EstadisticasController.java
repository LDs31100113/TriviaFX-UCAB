package com.ucab.trivia.controlador;

import com.ucab.trivia.GestorVistas;
import com.ucab.trivia.modelo.CategoriaTrivia;
import com.ucab.trivia.modelo.EstadisticaGlobal;
import com.ucab.trivia.modelo.ServicioDatos;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Comparator;
import java.util.List;

/**
 * Controlador para la vista de estadísticas (Estadisticas.fxml).
 * Se encarga de cargar las estadísticas globales de los jugadores y mostrarlas
 * en una tabla ordenada, con columnas detalladas por categoría.
 * @author [Tu Nombre/Equipo]
 * @version 1.1
 */
public class EstadisticasController {

    @FXML private TableView<EstadisticaGlobal> tablaEstadisticas;
    @FXML private TableColumn<EstadisticaGlobal, String> colAlias;
    @FXML private TableColumn<EstadisticaGlobal, Integer> colGanadas;
    @FXML private TableColumn<EstadisticaGlobal, Integer> colPerdidas;
    @FXML private TableColumn<EstadisticaGlobal, Integer> colJugadas;
    @FXML private TableColumn<EstadisticaGlobal, String> colTiempo;

    // --- INICIO DE LA MODIFICACIÓN: Se reemplaza la columna única por 6 columnas específicas ---
    @FXML private TableColumn<EstadisticaGlobal, Integer> colGeografia;
    @FXML private TableColumn<EstadisticaGlobal, Integer> colHistoria;
    @FXML private TableColumn<EstadisticaGlobal, Integer> colDeportes;
    @FXML private TableColumn<EstadisticaGlobal, Integer> colCiencia;
    @FXML private TableColumn<EstadisticaGlobal, Integer> colArte;
    @FXML private TableColumn<EstadisticaGlobal, Integer> colEntret;
    // --- FIN DE LA MODIFICACIÓN ---

    private final ServicioDatos servicioDatos = new ServicioDatos();
    private GestorVistas gestorVistas;

    public void setGestorVistas(GestorVistas gestorVistas) {
        this.gestorVistas = gestorVistas;
    }

    @FXML
    public void initialize() {
        // Configuración de columnas con datos directos
        colAlias.setCellValueFactory(new PropertyValueFactory<>("alias"));
        colGanadas.setCellValueFactory(new PropertyValueFactory<>("partidasGanadas"));
        colPerdidas.setCellValueFactory(new PropertyValueFactory<>("partidasPerdidas"));
        colJugadas.setCellValueFactory(new PropertyValueFactory<>("partidasJugadas"));

        // Configuración para la columna de tiempo formateado
        colTiempo.setCellValueFactory(cellData -> {
            double segundos = cellData.getValue().getTiempoTotalRespuestasCorrectasMs() / 1000.0;
            return new SimpleStringProperty(String.format("%.2f", segundos));
        });

        // --- INICIO DE LA MODIFICACIÓN: Configuración para cada columna de categoría ---
        configurarColumnaCategoria(colGeografia, CategoriaTrivia.GEOGRAFIA);
        configurarColumnaCategoria(colHistoria, CategoriaTrivia.HISTORIA);
        configurarColumnaCategoria(colDeportes, CategoriaTrivia.DEPORTES);
        configurarColumnaCategoria(colCiencia, CategoriaTrivia.CIENCIA);
        configurarColumnaCategoria(colArte, CategoriaTrivia.ARTE_LITERATURA);
        configurarColumnaCategoria(colEntret, CategoriaTrivia.ENTRETENIMIENTO);
        // --- FIN DE LA MODIFICACIÓN ---

        cargarYMostrarEstadisticas();
    }

    /**
     * Método de ayuda para configurar una columna de categoría.
     * Le dice a la columna cómo obtener el número de respuestas correctas para una categoría específica
     * del mapa de estadísticas de cada jugador.
     * @param columna La columna de la tabla a configurar.
     * @param categoria La categoría que esta columna representa.
     */
    private void configurarColumnaCategoria(TableColumn<EstadisticaGlobal, Integer> columna, CategoriaTrivia categoria) {
        columna.setCellValueFactory(cellData -> {
            Integer count = cellData.getValue().getCorrectasPorCategoria().getOrDefault(categoria, 0);
            return new SimpleObjectProperty<>(count);
        });
    }

    private void cargarYMostrarEstadisticas() {
        List<EstadisticaGlobal> estadisticas = servicioDatos.cargarEstadisticas();
        // Ordenar por partidas ganadas para crear el ranking
        estadisticas.sort(Comparator.comparingInt(EstadisticaGlobal::getPartidasGanadas).reversed());

        ObservableList<EstadisticaGlobal> datosObservables = FXCollections.observableArrayList(estadisticas);
        tablaEstadisticas.setItems(datosObservables);
    }

    @FXML
    private void onRegresar() {
        gestorVistas.mostrarMenuPrincipal();
    }
}
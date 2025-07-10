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

/**
 * Controlador para la vista de estadísticas (Estadisticas.fxml).
 * Se encarga de cargar las estadísticas globales de los jugadores desde un archivo JSON
 * y mostrarlas en una tabla ordenada por partidas ganadas.
 */
public class EstadisticasController {

    @FXML private TableView<EstadisticaGlobal> tablaEstadisticas;
    @FXML private TableColumn<EstadisticaGlobal, String> colAlias;
    @FXML private TableColumn<EstadisticaGlobal, Integer> colGanadas;
    @FXML private TableColumn<EstadisticaGlobal, Integer> colPerdidas;
    @FXML private TableColumn<EstadisticaGlobal, Integer> colJugadas;
    @FXML private TableColumn<EstadisticaGlobal, String> colCorrectas;
    @FXML private TableColumn<EstadisticaGlobal, String> colTiempo;

    private final ServicioDatos servicioDatos = new ServicioDatos();
    private GestorVistas gestorVistas;

    /**
     * Inyecta la dependencia del gestor de vistas para permitir la navegación.
     * Este método es llamado por GestorVistas al cargar esta escena.
     * @param gestorVistas La instancia única del gestor de vistas.
     */
    public void setGestorVistas(GestorVistas gestorVistas) {
        this.gestorVistas = gestorVistas;
    }

    /**
     * Método de inicialización que se ejecuta automáticamente después de que se carga el FXML.
     * Configura las celdas de la tabla para que muestren los datos de los objetos EstadisticaGlobal
     * y llama al método para cargar y mostrar los datos.
     */
    @FXML
    public void initialize() {
        // Asigna las propiedades de la clase EstadisticaGlobal a cada columna de la tabla.
        colAlias.setCellValueFactory(new PropertyValueFactory<>("alias"));
        colGanadas.setCellValueFactory(new PropertyValueFactory<>("partidasGanadas"));
        colPerdidas.setCellValueFactory(new PropertyValueFactory<>("partidasPerdidas"));
        colJugadas.setCellValueFactory(new PropertyValueFactory<>("partidasJugadas"));

        // Configura la celda de "Preguntas Correctas" para mostrar un formato personalizado.
        colCorrectas.setCellValueFactory(cellData -> {
            if (cellData.getValue().getCorrectasPorCategoria() == null) return new SimpleStringProperty("");
            Map<String, Integer> mapa = cellData.getValue().getCorrectasPorCategoria().entrySet().stream()
                    .collect(Collectors.toMap(e -> e.getKey().getNombreMostrado().substring(0,1), Map.Entry::getValue));
            return new SimpleStringProperty(mapa.toString());
        });

        // Configura la celda de "Tiempo Total" para mostrar los segundos con dos decimales.
        colTiempo.setCellValueFactory(cellData -> {
            double segundos = cellData.getValue().getTiempoTotalRespuestasCorrectasMs() / 1000.0;
            return new SimpleStringProperty(String.format("%.2f", segundos));
        });

        cargarYMostrarEstadisticas();
    }

    /**
     * Carga los datos de estadísticas desde el archivo JSON, los ordena por partidas ganadas
     * en orden descendente y los establece como el contenido de la tabla.
     */
    private void cargarYMostrarEstadisticas() {
        List<EstadisticaGlobal> estadisticas = servicioDatos.cargarEstadisticas();
        estadisticas.sort(Comparator.comparingInt(EstadisticaGlobal::getPartidasGanadas).reversed());

        ObservableList<EstadisticaGlobal> datosObservables = FXCollections.observableArrayList(estadisticas);
        tablaEstadisticas.setItems(datosObservables);
    }

    /**
     * Maneja el evento de clic del botón "Regresar".
     * Llama al GestorVistas para volver al menú principal.
     */
    @FXML
    private void onRegresar() {
        gestorVistas.mostrarMenuPrincipal();
    }
}
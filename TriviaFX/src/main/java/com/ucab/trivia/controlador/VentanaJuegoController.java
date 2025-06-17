package com.ucab.trivia.controlador;

import com.ucab.trivia.GestorVistas;
import com.ucab.trivia.modelo.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class VentanaJuegoController {

    @FXML private AnchorPane panelTablero;
    @FXML private Label labelTurno;
    @FXML private VBox panelInfoJugadores;
    @FXML private Button btnLanzarDado;
    @FXML private Label labelResultadoDado;
    @FXML private Label labelInfoJuego;
    @FXML private Button btnRendirse;

    private Juego juego;
    private ServicioDatos servicioDatos;
    private Map<Posicion, Point2D> mapaPosicionACoordenadas;
    private Map<Jugador, Group> mapaFichasGraficas;

    @FXML
    public void initialize() {
        // Se inicializa todo a través de iniciarJuego
    }

    public void iniciarJuego(List<PerfilJugador> perfiles, boolean esPartidaCargada) {
        this.servicioDatos = new ServicioDatos();
        if (esPartidaCargada) {
            EstadoJuegoGuardado estado = servicioDatos.cargarPartidaGuardada();
            if (estado != null) {
                this.juego = new Juego(estado);
            } else {
                mostrarAlerta("Error", "No se pudo cargar la partida guardada. Volviendo al menú principal.");
                GestorVistas.mostrarMenuPrincipal();
                return;
            }
        } else {
            this.juego = new Juego(perfiles);
        }

        dibujarTablero();
        dibujarFichasJugadores();
        actualizarVistaCompleta();
    }

    private void dibujarTablero() {
        panelTablero.getChildren().clear();
        mapaPosicionACoordenadas = new HashMap<>();

        double centroX = panelTablero.getPrefWidth() / 2;
        double centroY = panelTablero.getPrefHeight() / 2;
        double radioCirculo = 250;
        double tamanoCasilla = 35;

        // Dibujar Círculo Exterior
        for (int i = 0; i < TableroGrafico.NUMERO_CASILLAS_CIRCULO; i++) {
            double angulo = (2 * Math.PI * i / TableroGrafico.NUMERO_CASILLAS_CIRCULO) - (Math.PI / 2); // Ajuste para empezar arriba
            double x = centroX + radioCirculo * Math.cos(angulo);
            double y = centroY + radioCirculo * Math.sin(angulo);
            Posicion pos = Posicion.enCirculo(i);
            crearRectanguloCasilla(x, y, tamanoCasilla, juego.getTablero().getCasillaEnPosicion(pos));
            mapaPosicionACoordenadas.put(pos, new Point2D(x, y));
        }

        // Dibujar Rayos
        for (int i = 0; i < TableroGrafico.NUMERO_RAYOS; i++) {
            Posicion posEntradaRayo = Posicion.enCirculo(i * 7);
            Point2D coordInicial = mapaPosicionACoordenadas.get(posEntradaRayo);

            for (int j = 0; j < TableroGrafico.CASILLAS_POR_RAYO_INTERNO; j++) {
                double fraccion = (double)(j + 1) / (TableroGrafico.CASILLAS_POR_RAYO_INTERNO + 1);
                double x = (1 - fraccion) * coordInicial.getX() + fraccion * centroX;
                double y = (1 - fraccion) * coordInicial.getY() + fraccion * centroY;

                Posicion pos = Posicion.enRayo(i, j);
                crearRectanguloCasilla(x, y, tamanoCasilla, juego.getTablero().getCasillaEnPosicion(pos));
                mapaPosicionACoordenadas.put(pos, new Point2D(x, y));
            }
        }

        crearRectanguloCasilla(centroX, centroY, tamanoCasilla * 1.5, new Casilla(null, false, true));
        mapaPosicionACoordenadas.put(Posicion.enCentro(), new Point2D(centroX, centroY));
    }

    private void crearRectanguloCasilla(double x, double y, double tamano, Casilla casilla) {
        Rectangle rect = new Rectangle(x - tamano / 2, y - tamano / 2, tamano, tamano);
        rect.setStroke(Color.BLACK);
        if (casilla != null) {
            rect.setFill(getColorParaCategoria(casilla.getCategoria()));
            if (casilla.isEsEspecialReRoll()) {
                rect.setStrokeWidth(3.0);
                rect.setStroke(Color.GOLD);
            }
        }
        panelTablero.getChildren().add(rect);
    }

    private Color getColorParaCategoria(CategoriaTrivia cat) {
        if (cat == null) return Color.WHITE;
        return Color.web(cat.getColorWeb());
    }

    private void dibujarFichasJugadores() {
        mapaFichasGraficas = new HashMap<>();
        Color[] coloresBorde = {Color.WHITE, Color.BLACK, Color.DARKRED, Color.DARKBLUE, Color.DARKGREEN, Color.GOLD};
        for (int i = 0; i < juego.getJugadores().size(); i++) {
            Jugador jugador = juego.getJugadores().get(i);
            Group fichaGrafica = crearFichaGrafica(jugador, coloresBorde[i % coloresBorde.length]);
            mapaFichasGraficas.put(jugador, fichaGrafica);
            panelTablero.getChildren().add(fichaGrafica);
        }
    }

    private Group crearFichaGrafica(Jugador jugador, Color colorBorde) {
        Group quesitoGroup = new Group();
        double radioFicha = 12.0;

        Map<CategoriaTrivia, Boolean> categoriasObtenidas = jugador.getFicha().getCategoriasObtenidas();
        CategoriaTrivia[] categorias = CategoriaTrivia.values();

        for (int i = 0; i < categorias.length; i++) {
            Arc arc = new Arc(0, 0, radioFicha, radioFicha, i * 60, 60);
            arc.setType(ArcType.ROUND);
            if (categoriasObtenidas.get(categorias[i])) {
                arc.setFill(Color.web(categorias[i].getColorWeb()));
            } else {
                arc.setFill(Color.LIGHTGRAY);
            }
            arc.setStroke(colorBorde);
            arc.setStrokeWidth(1.5);
            quesitoGroup.getChildren().add(arc);
        }

        Text inicial = new Text(jugador.getAlias().substring(0,1).toUpperCase());
        inicial.setX(-inicial.getLayoutBounds().getWidth() / 2);
        inicial.setY(inicial.getLayoutBounds().getHeight() / 4);
        inicial.setFill(Color.WHITE);
        quesitoGroup.getChildren().add(inicial);

        return quesitoGroup;
    }

    private void actualizarVistaCompleta() {
        Jugador jugadorActual = juego.getJugadorActual();
        if (jugadorActual == null) return;

        labelTurno.setText("Turno de: " + jugadorActual.getAlias());

        // Actualizar panel de información de todos los jugadores
        panelInfoJugadores.getChildren().clear();
        for (Jugador j : juego.getJugadores()) {
            String estado = j.isEstaRendido() ? " (Rendido)" : "";
            String textoFicha = j.getAlias() + estado + ": " + j.getFicha().getCategoriasObtenidasCount() + "/6";
            Label infoJugador = new Label(textoFicha);
            panelInfoJugadores.getChildren().add(infoJugador);
        }

        // Actualizar tiempo del jugador actual
        double segundos = jugadorActual.getTiempoTotalEnPartidaMs() / 1000.0;
        Label infoTiempo = new Label("Tiempo de Rtas. Correctas: " + String.format("%.2f", segundos) + "s");
        panelInfoJugadores.getChildren().add(infoTiempo);

        // Actualizar posiciones de las fichas gráficas
        for (Jugador j : juego.getJugadores()) {
            Node fichaGrafica = mapaFichasGraficas.get(j);
            Point2D coord = mapaPosicionACoordenadas.get(j.getPosicionActual());
            if(fichaGrafica != null && coord != null) {
                fichaGrafica.setTranslateX(coord.getX());
                fichaGrafica.setTranslateY(coord.getY());
            }
        }

        btnLanzarDado.setDisable(false);
        btnRendirse.setDisable(false);
    }

    @FXML
    private void onLanzarDado() {
        // Lógica de juego...
    }

    @FXML
    private void onRendirse() {
        // Lógica de rendición...
    }

    @FXML
    private void onFinalizarPartida() {
        // Finaliza el juego y actualiza estadísticas globales
        finalizarYGuardarEstadisticas(null); // null indica que no hay un ganador por juego normal
        GestorVistas.mostrarMenuPrincipal();
    }

    private void finalizarYGuardarEstadisticas(Jugador ganador) {
        List<EstadisticaGlobal> estadisticasGlobales = servicioDatos.cargarEstadisticas();

        for(Jugador jugadorDePartida : juego.getJugadores()) {
            EstadisticaGlobal estGlobal = estadisticasGlobales.stream()
                    .filter(e -> e.getAlias().equals(jugadorDePartida.getAlias()))
                    .findFirst()
                    .orElse(null);

            if (estGlobal == null) {
                estGlobal = new EstadisticaGlobal(jugadorDePartida.getAlias());
                estadisticasGlobales.add(estGlobal);
            }

            estGlobal.registrarPartidaJugada(jugadorDePartida.equals(ganador));
            estGlobal.agregarEstadisticasDePartida(jugadorDePartida.getCorrectasEnPartida(), jugadorDePartida.getTiempoTotalEnPartidaMs());
        }

        servicioDatos.guardarEstadisticas(estadisticasGlobales);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

package com.ucab.trivia.controlador;

import com.ucab.trivia.GestorVistas;
import com.ucab.trivia.modelo.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
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
    private boolean turnoEnProceso = false;
    private GestorVistas gestorVistas;

    public void setGestorVistas(GestorVistas gestorVistas) {
        this.gestorVistas = gestorVistas;
    }

    public void iniciarJuego(List<PerfilJugador> perfiles, boolean esPartidaCargada) {
        this.servicioDatos = new ServicioDatos();
        if (esPartidaCargada) {
            EstadoJuegoGuardado estado = servicioDatos.cargarPartidaGuardada();
            if (estado != null) {
                this.juego = new Juego(estado);
            } else {
                mostrarAlerta("Error", "No se pudo cargar la partida guardada.");
                gestorVistas.mostrarMenuPrincipal();
                return;
            }
        } else {
            if (perfiles == null || perfiles.isEmpty()) {
                mostrarAlerta("Error", "No se seleccionaron jugadores.");
                gestorVistas.mostrarMenuPrincipal();
                return;
            }
            this.juego = new Juego(perfiles);
        }
        dibujarTablero();
        dibujarFichasJugadores();
        actualizarVistaCompleta();
        labelInfoJuego.setText("¡Partida iniciada! Es el turno de " + juego.getJugadorActual().getAlias() + ".");
    }

    private void dibujarTablero() {
        panelTablero.getChildren().clear();
        mapaPosicionACoordenadas = new HashMap<>();
        double centroX = panelTablero.getPrefWidth() / 2;
        double centroY = panelTablero.getPrefHeight() / 2;
        double radioCirculo = 280;
        double tamanoCasilla = 38;

        for (int i = 0; i < TableroGrafico.NUMERO_CASILLAS_CIRCULO; i++) {
            double angulo = (2 * Math.PI * i / TableroGrafico.NUMERO_CASILLAS_CIRCULO) - (Math.PI / 2);
            double x = centroX + radioCirculo * Math.cos(angulo);
            double y = centroY + radioCirculo * Math.sin(angulo);
            Posicion pos = Posicion.enCirculo(i);
            crearRectanguloCasilla(x, y, tamanoCasilla, juego.getTablero().getCasillaEnPosicion(pos));
            mapaPosicionACoordenadas.put(pos, new Point2D(x, y));
        }

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
        } else {
            rect.setFill(Color.WHITE);
        }
        panelTablero.getChildren().add(rect);
    }

    private Color getColorParaCategoria(CategoriaTrivia cat) {
        if (cat == null) return Color.WHITE;
        return Color.web(cat.getColorWeb());
    }

    private void dibujarFichasJugadores() {
        mapaFichasGraficas = new HashMap<>();
        Color[] coloresBorde = {Color.GHOSTWHITE, Color.BLACK, Color.DARKRED, Color.DARKBLUE, Color.DARKGREEN, Color.GOLD};
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
            Arc arc = new Arc(0, 0, radioFicha, radioFicha, (i * 60) + 30, 60);
            arc.setType(ArcType.ROUND);
            if (categoriasObtenidas != null && categoriasObtenidas.getOrDefault(categorias[i], false)) {
                arc.setFill(Color.web(categorias[i].getColorWeb()));
            } else {
                arc.setFill(Color.LIGHTGRAY);
            }
            arc.setStroke(colorBorde);
            arc.setStrokeWidth(2.0);
            quesitoGroup.getChildren().add(arc);
        }
        Text inicial = new Text(jugador.getAlias().substring(0, Math.min(jugador.getAlias().length(), 2)).toUpperCase());
        inicial.setFont(Font.font("System", 10));
        inicial.setX(-inicial.getLayoutBounds().getWidth() / 2);
        inicial.setY(inicial.getLayoutBounds().getHeight() / 4);
        quesitoGroup.getChildren().add(inicial);
        return quesitoGroup;
    }

    private void actualizarVistaCompleta() {
        Jugador jugadorActual = juego.getJugadorActual();
        if (jugadorActual == null) return;
        labelTurno.setText("Turno de: " + jugadorActual.getAlias());
        panelInfoJugadores.getChildren().clear();
        for (Jugador j : juego.getJugadores()) {
            String estado = j.isEstaRendido() ? " (Rendido)" : "";
            String textoFicha = j.getAlias() + estado + ": " + j.getFicha().getCategoriasObtenidasCount() + "/6";
            Label infoJugador = new Label(textoFicha);
            if (j.equals(jugadorActual)) infoJugador.setStyle("-fx-font-weight: bold;");
            panelInfoJugadores.getChildren().add(infoJugador);
        }
        double segundos = jugadorActual.getTiempoTotalEnPartidaMs() / 1000.0;
        Label infoTiempo = new Label("Tu tiempo: " + String.format("%.2f", segundos) + "s");
        panelInfoJugadores.getChildren().add(infoTiempo);
        actualizarPosicionesFichas();
        btnLanzarDado.setDisable(turnoEnProceso);
        btnRendirse.setDisable(turnoEnProceso);
    }

    private void actualizarPosicionesFichas() {
        for (Jugador j : juego.getJugadores()) {
            Node fichaGrafica = mapaFichasGraficas.get(j);
            Posicion posLogica = j.getPosicionActual();
            Point2D coordGrafica = mapaPosicionACoordenadas.get(posLogica);
            if(fichaGrafica != null && coordGrafica != null) {
                List<Jugador> jugadoresEnCasilla = juego.getJugadores().stream()
                        .filter(p -> p.getPosicionActual() != null && p.getPosicionActual().equals(posLogica)).toList();
                int indiceJugadorEnCasilla = jugadoresEnCasilla.indexOf(j);
                double offset = (jugadoresEnCasilla.size() > 1) ? (-5 + indiceJugadorEnCasilla * 5) : 0;
                fichaGrafica.setTranslateX(coordGrafica.getX() + offset);
                fichaGrafica.setTranslateY(coordGrafica.getY() + offset);
            }
        }
    }

    @FXML
    private void onLanzarDado() {
        turnoEnProceso = true;
        actualizarVistaCompleta();
        int resultado = juego.lanzarDado();
        labelResultadoDado.setText("Resultado del Dado: " + resultado);
        Jugador jugadorActual = juego.getJugadorActual();
        Posicion posActual = jugadorActual.getPosicionActual();
        boolean eligeEntrarRayo = false;
        Casilla casillaActual = juego.getTablero().getCasillaEnPosicion(posActual);
        if(casillaActual != null && casillaActual.isEsEntradaRayo()){
            eligeEntrarRayo = mostrarDialogoEleccionRayo();
        }
        juego.moverJugador(jugadorActual, resultado, eligeEntrarRayo);
        labelInfoJuego.setText(jugadorActual.getAlias() + " se mueve a " + jugadorActual.getPosicionActual());
        procesarLlegadaACasilla();
    }

    private void procesarLlegadaACasilla() {
        Platform.runLater(() -> {
            actualizarVistaCompleta();
            Jugador jugadorActual = juego.getJugadorActual();
            Posicion posLlegada = jugadorActual.getPosicionActual();

            if (posLlegada.getTipo() == Posicion.TipoLugar.CENTRO) {
                if (jugadorActual.getFicha().estaCompleta()) {
                    manejarIntentoDeVictoria();
                } else {
                    labelInfoJuego.setText("Llegaste al centro. Debes salir. Lanza de nuevo.");
                    turnoEnProceso = false;
                    actualizarVistaCompleta();
                }
                return;
            }

            Casilla casillaLlegada = juego.getTablero().getCasillaEnPosicion(posLlegada);
            if (casillaLlegada.isEsEspecialReRoll()) {
                labelInfoJuego.setText("¡Casilla Especial! Vuelves a lanzar.");
                turnoEnProceso = false;
                actualizarVistaCompleta();
                return;
            }

            CategoriaTrivia categoriaCasilla = casillaLlegada.getCategoria();
            PreguntaOriginal pregunta = juego.getPreguntaParaCategoria(categoriaCasilla);
            if (pregunta == null) {
                labelInfoJuego.setText("No hay pregunta para " + categoriaCasilla + ". Turno del siguiente.");
                juego.pasarTurno();
                turnoEnProceso = false;
                actualizarVistaCompleta();
                return;
            }

            long tiempoInicio = System.currentTimeMillis();
            boolean acerto = mostrarDialogoPregunta(pregunta);
            long tiempoFin = System.currentTimeMillis();

            if (acerto) {
                jugadorActual.registrarRespuestaCorrecta(categoriaCasilla, tiempoFin - tiempoInicio);

                String infoAdicional = "";
                // ** INICIO DE LA CORRECCIÓN **
                // La lógica anterior para ganar todos los quesitos era para tablero hexagonal,
                // ahora la adaptamos a la regla de los rayos del tablero circular.
                if (casillaLlegada.isEsEntradaRayo()) {
                    labelInfoJuego.setText("¡BONUS! Acertaste en una casilla de Rayo y ganas todos los quesitos.");
                    jugadorActual.getFicha().rellenarTodasLasCategorias();
                } else if (!jugadorActual.getFicha().haObtenidoCategoria(casillaLlegada.getCategoria())) {
                    jugadorActual.getFicha().marcarCategoriaObtenida(casillaLlegada.getCategoria());
                    labelInfoJuego.setText("¡Correcto! Quesito de " + categoriaCasilla + " obtenido.");
                } else {
                    labelInfoJuego.setText("¡Correcto!");
                }

                // Lógica corregida para actualizar la ficha gráfica sin errores de tipo
                Group fichaGrafica = mapaFichasGraficas.get(jugadorActual);
                if (fichaGrafica != null) {
                    Map<CategoriaTrivia, Boolean> categoriasObtenidas = jugadorActual.getFicha().getCategoriasObtenidas();
                    CategoriaTrivia[] categorias = CategoriaTrivia.values();
                    for(int i=0; i < categorias.length; i++) {
                        if (categoriasObtenidas.get(categorias[i])) {
                            Arc arc = (Arc) fichaGrafica.getChildren().get(i); // Asume que los Arcs son los primeros 6 hijos
                            arc.setFill(Color.web(categorias[i].getColorWeb()));
                        }
                    }
                }
                // ** FIN DE LA CORRECCIÓN **

                if (jugadorActual.getFicha().estaCompleta()) {
                    labelInfoJuego.setText(labelInfoJuego.getText() + "\n¡FICHA COMPLETA! Ahora al centro para ganar.");
                }
                labelInfoJuego.setText(labelInfoJuego.getText() + "\nVuelves a lanzar.");
                turnoEnProceso = false;
            } else {
                labelInfoJuego.setText("Incorrecto. Turno del siguiente jugador.");
                juego.pasarTurno();
                turnoEnProceso = false;
            }
            actualizarVistaCompleta();
        });
    }

    private void manejarIntentoDeVictoria() {
        Jugador jugadorActual = juego.getJugadorActual();
        labelInfoJuego.setText("¡FICHA COMPLETA! Elige categoría para ganar.");

        List<CategoriaTrivia> opciones = new ArrayList<>(List.of(CategoriaTrivia.values()));
        ChoiceDialog<CategoriaTrivia> dialog = new ChoiceDialog<>(opciones.get(0), opciones);
        dialog.setTitle("Pregunta Final");
        dialog.setHeaderText("Elige la categoría para tu pregunta final");
        Optional<CategoriaTrivia> result = dialog.showAndWait();

        if(result.isPresent()){
            CategoriaTrivia categoriaElegida = result.get();
            // **INICIO DE LA CORRECCIÓN**
            // Se usa el método del objeto 'juego' existente en lugar de crear un nuevo servicio.
            PreguntaOriginal preguntaFinal = juego.getPreguntaParaCategoria(categoriaElegida);
            // **FIN DE LA CORRECCIÓN**

            if (preguntaFinal == null) {
                mostrarAlerta("¡Ganador!", "No hay preguntas para " + categoriaElegida + ". ¡Ganas por defecto!");
                finalizarYGuardarEstadisticas(jugadorActual);
                return;
            }

            long tiempoInicio = System.currentTimeMillis();
            boolean acerto = mostrarDialogoPregunta(preguntaFinal);
            long tiempoFin = System.currentTimeMillis();
            if(acerto) {
                jugadorActual.registrarRespuestaCorrecta(categoriaElegida, tiempoFin - tiempoInicio);
                mostrarAlerta("¡FELICIDADES!", "¡" + jugadorActual.getAlias() + " HAS GANADO TRIVIA-UCAB!");
                finalizarYGuardarEstadisticas(jugadorActual);
            } else {
                mostrarAlerta("Casi...", "Respuesta incorrecta. Permaneces en el centro.");
                juego.pasarTurno();
                turnoEnProceso = false;
                actualizarVistaCompleta();
            }
        } else {
            labelInfoJuego.setText("Elección cancelada. Sigues en el centro.");
            turnoEnProceso = false;
            actualizarVistaCompleta();
        }
    }

    @FXML private void onRendirse() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro?", ButtonType.YES, ButtonType.NO);
        confirmacion.setTitle("Confirmar Rendición");
        confirmacion.setHeaderText(null);
        Optional<ButtonType> result = confirmacion.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.YES) {
            Jugador jugadorQueSeRinde = juego.getJugadorActual();
            labelInfoJuego.setText(jugadorQueSeRinde.getAlias() + " se ha rendido.");
            juego.rendirse();

            Jugador ganadorPorRendicion = juego.determinarGanadorPorRendicion();
            if(ganadorPorRendicion != null) {
                mostrarAlerta("Fin del Juego", "El ganador es: " + ganadorPorRendicion.getAlias() + "!");
                finalizarYGuardarEstadisticas(ganadorPorRendicion);
            } else {
                juego.pasarTurno();
                actualizarVistaCompleta();
            }
        }
    }

    @FXML private void onFinalizarPartida() {
        finalizarYGuardarEstadisticas(null);
        gestorVistas.mostrarMenuPrincipal();
    }

    private void finalizarYGuardarEstadisticas(Jugador ganador) {
        List<EstadisticaGlobal> estadisticasGlobales = servicioDatos.cargarEstadisticas();
        for(Jugador jugadorDePartida : juego.getJugadores()) {
            EstadisticaGlobal estGlobal = estadisticasGlobales.stream()
                    .filter(e -> e.getAlias().equals(jugadorDePartida.getAlias()))
                    .findFirst().orElse(null);

            if (estGlobal == null) {
                estGlobal = new EstadisticaGlobal(jugadorDePartida.getAlias());
                estadisticasGlobales.add(estGlobal);
            }

            estGlobal.registrarPartidaJugada(jugadorDePartida.equals(ganador));
            estGlobal.agregarEstadisticasDePartida(jugadorDePartida.getCorrectasEnPartida(), jugadorDePartida.getTiempoTotalEnPartidaMs());
        }
        servicioDatos.guardarEstadisticas(estadisticasGlobales);

        btnLanzarDado.setDisable(true);
        btnRendirse.setDisable(true);
    }

    private boolean mostrarDialogoPregunta(PreguntaOriginal pregunta) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Pregunta de Trivia");
        dialog.setHeaderText(pregunta.getPregunta());
        dialog.setContentText("Tu respuesta:");
        Optional<String> result = dialog.showAndWait();
        return result.map(s -> s.trim().equalsIgnoreCase(pregunta.getRespuesta().trim())).orElse(false);
    }

    private boolean mostrarDialogoEleccionRayo() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Deseas moverte hacia el centro por el rayo?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Decisión de Movimiento");
        alert.setHeaderText("Estás en una entrada de rayo.");
        return alert.showAndWait().filter(b -> b == ButtonType.YES).isPresent();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
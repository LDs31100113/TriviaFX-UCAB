package com.ucab.trivia.modelo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Representa la lógica central y el estado de una partida de trivia.
 * Gestiona los jugadores, el tablero, el flujo del juego, las preguntas y el guardado de la partida.
 */
public class Juego {

    /** Lista de todos los jugadores que participan en la partida. */
    private List<Jugador> jugadores;

    /** Índice del jugador cuyo turno está en curso. */
    private int indiceJugadorActual;

    /** El tablero gráfico del juego. */
    private final TableroGrafico tablero;

    /** El dado utilizado para determinar el número de pasos a mover. */
    private final Dado dado;

    /** Servicio para obtener las preguntas del juego. */
    private final ServicioPreguntasJuego servicioPreguntas;

    /** Servicio para manejar la persistencia de los datos del juego. */
    private final ServicioDatos servicioDatos;

    /**
     * Construye una nueva partida de trivia a partir de una lista de perfiles de jugadores.
     * Inicializa el tablero, el dado y los servicios necesarios.
     *
     * @param perfiles La lista de perfiles de los jugadores que participarán.
     */
    public Juego(List<PerfilJugador> perfiles) {
        this.tablero = new TableroGrafico();
        this.dado = new Dado();
        this.servicioPreguntas = new ServicioPreguntasJuego();
        this.servicioDatos = new ServicioDatos();
        this.jugadores = perfiles.stream().map(Jugador::new).collect(Collectors.toList());
        determinarPrimerJugador();
    }

    /**
     * Construye una partida de trivia a partir de un estado de juego previamente guardado.
     *
     * @param estado El estado del juego guardado desde el cual se reanudará la partida.
     */
    public Juego(EstadoJuegoGuardado estado) {
        this.tablero = new TableroGrafico();
        this.dado = new Dado();
        this.servicioPreguntas = new ServicioPreguntasJuego();
        this.servicioDatos = new ServicioDatos();
        this.jugadores = estado.getJugadores();
        this.indiceJugadorActual = estado.getIndiceJugadorActual();
    }

    /**
     * Guarda el estado actual de la partida utilizando el {@link ServicioDatos}.
     * Solo se guarda si hay jugadores en la partida.
     */
    public void guardarEstadoActualDelJuego() {
        if (jugadores != null && !jugadores.isEmpty()) {
            EstadoJuegoGuardado estadoActual = new EstadoJuegoGuardado(new ArrayList<>(jugadores), this.indiceJugadorActual);
            servicioDatos.guardarPartida(estadoActual);
        }
    }

    /**
     * Selecciona aleatoriamente al jugador que comenzará la partida.
     * Si solo hay un jugador o ninguno, se asigna el índice 0 por defecto.
     */
    private void determinarPrimerJugador() {
        if (jugadores == null || jugadores.size() <= 1) {
            indiceJugadorActual = 0;
            return;
        }
        indiceJugadorActual = (int) (Math.random() * jugadores.size());
    }

    /**
     * Lanza el dado y devuelve el resultado.
     *
     * @return Un número entero que representa el resultado de lanzar el dado.
     */
    public int lanzarDado() {
        return dado.lanzar();
    }

    /**
     * Mueve un jugador en el tablero según el número de pasos obtenidos.
     *
     * @param jugador El jugador a mover.
     * @param pasos El número de casillas que el jugador se moverá.
     * @param eligeEntrarRayo Indica si el jugador elige entrar a un rayo para ganar una categoría.
     */
    public void moverJugador(Jugador jugador, int pasos, boolean eligeEntrarRayo) {
        if(jugador.getPosicionActual().getTipo() == Posicion.TipoLugar.CENTRO) {
            int rayoElegido = (int) (Math.random() * 6);
            int indiceSalida = (rayoElegido * 7) + (pasos - 1) % 7;
            jugador.setPosicionActual(Posicion.enCirculo(indiceSalida % TableroGrafico.NUMERO_CASILLAS_CIRCULO));
        } else {
            jugador.setPosicionActual(tablero.calcularNuevaPosicion(jugador.getPosicionActual(), pasos, eligeEntrarRayo));
        }
    }

    /**
     * Marca al jugador actual como rendido en la partida.
     */
    public void rendirse() {
        getJugadorActual().setEstaRendido(true);
    }

    /**
     * Determina si hay un ganador por rendición de los demás jugadores.
     *
     * @return El jugador ganador si solo queda uno activo. Si todos se rinden,
     * devuelve el que tenga más categorías y menos tiempo. Devuelve {@code null} si aún hay
     * múltiples jugadores activos.
     */
    public Jugador determinarGanadorPorRendicion() {
        List<Jugador> jugadoresActivos = getJugadoresActivos();
        if (jugadoresActivos.size() == 1) {
            return jugadoresActivos.get(0);
        }
        if (jugadoresActivos.isEmpty()) {
            return jugadores.stream()
                    .max(Comparator.comparingInt((Jugador j) -> j.getFicha().getCategoriasObtenidasCount())
                            .thenComparingLong(j -> -j.getTiempoTotalEnPartidaMs()))
                    .orElse(null);
        }
        return null;
    }

    /**
     * Pasa el turno al siguiente jugador activo en la lista.
     * Si no quedan jugadores activos, el método no hace nada.
     */
    public void pasarTurno() {
        if(getJugadoresActivos().isEmpty()) return;
        do {
            indiceJugadorActual = (indiceJugadorActual + 1) % jugadores.size();
        } while (getJugadorActual().isEstaRendido());
    }

    /**
     * Obtiene el jugador cuyo turno está en curso.
     *
     * @return El {@link Jugador} actual, o {@code null} si no hay jugadores.
     */
    public Jugador getJugadorActual() {
        if (jugadores == null || jugadores.isEmpty()) return null;
        return jugadores.get(indiceJugadorActual);
    }

    /**
     * Devuelve la lista de todos los jugadores.
     *
     * @return Una lista de {@link Jugador}.
     */
    public List<Jugador> getJugadores() { return jugadores; }

    /**
     * Devuelve una lista de los jugadores que no se han rendido.
     *
     * @return Una lista de {@link Jugador} activos.
     */
    public List<Jugador> getJugadoresActivos() {
        return jugadores.stream().filter(j -> !j.isEstaRendido()).collect(Collectors.toList());
    }

    /**
     * Devuelve la instancia del tablero del juego.
     *
     * @return El {@link TableroGrafico} de la partida.
     */
    public TableroGrafico getTablero() { return tablero; }

    /**
     * Obtiene una pregunta aleatoria correspondiente a la categoría de una posición en el tablero.
     *
     * @param pos La {@link Posicion} para la cual se necesita una pregunta.
     * @return Una {@link PreguntaOriginal} o {@code null} si la posición o su categoría no son válidas.
     */
    public PreguntaOriginal getPreguntaParaPosicion(Posicion pos) {
        if(pos == null || pos.getTipo() == Posicion.TipoLugar.CENTRO) return null;
        Casilla c = tablero.getCasillaEnPosicion(pos);
        if (c == null || c.getCategoria() == null) return null;
        return servicioPreguntas.seleccionarPreguntaAleatoria(c.getCategoria());
    }

    /**
     * Obtiene una pregunta aleatoria para una categoría específica.
     *
     * @param categoria La {@link CategoriaTrivia} de la cual se desea obtener una pregunta.
     * @return Una {@link PreguntaOriginal} o {@code null} si la categoría es nula.
     */
    public PreguntaOriginal getPreguntaParaCategoria(CategoriaTrivia categoria) {
        if (categoria == null) return null;
        return servicioPreguntas.seleccionarPreguntaAleatoria(categoria);
    }
}
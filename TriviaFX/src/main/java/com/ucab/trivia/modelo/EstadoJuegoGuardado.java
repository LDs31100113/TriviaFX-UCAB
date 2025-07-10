package com.ucab.trivia.modelo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Representa el estado de una partida guardada.
 * Esta clase es un objeto de transferencia de datos (DTO) simple que se utiliza
 * para serializar y deserializar el progreso del juego a un archivo JSON.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EstadoJuegoGuardado {
    private List<Jugador> jugadores;
    private int indiceJugadorActual;

    /**
     * Constructor por defecto, requerido por la librería Jackson para la deserialización.
     */
    public EstadoJuegoGuardado() {}

    /**
     * Constructor para crear un nuevo objeto de estado de juego para ser guardado.
     * @param jugadores La lista de jugadores con su estado actual (posición, ficha, etc.).
     * @param indiceJugadorActual El índice en la lista del jugador cuyo turno es el actual.
     */
    public EstadoJuegoGuardado(List<Jugador> jugadores, int indiceJugadorActual) {
        this.jugadores = jugadores;
        this.indiceJugadorActual = indiceJugadorActual;
    }

    /**
     * Obtiene la lista de jugadores de la partida guardada.
     * @return una lista de objetos Jugador.
     */
    public List<Jugador> getJugadores() {
        return jugadores;
    }

    /**
     * Establece la lista de jugadores.
     * @param jugadores la nueva lista de jugadores.
     */
    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    /**
     * Obtiene el índice del jugador que tiene el turno.
     * @return un entero que representa el índice en la lista de jugadores.
     */
    public int getIndiceJugadorActual() {
        return indiceJugadorActual;
    }

    /**
     * Establece el índice del jugador en turno.
     * @param indiceJugadorActual el nuevo índice del jugador actual.
     */
    public void setIndiceJugadorActual(int indiceJugadorActual) {
        this.indiceJugadorActual = indiceJugadorActual;
    }
}
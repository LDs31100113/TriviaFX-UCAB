package com.ucab.trivia.modelo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EstadoJuegoGuardado {
    private List<Jugador> jugadores;
    private int indiceJugadorActual;

    public EstadoJuegoGuardado() {}

    /**
     * Constructor para guardar el estado esencial de la partida.
     * @param jugadores La lista de jugadores con su estado actual.
     * @param indiceJugadorActual El índice del jugador en turno.
     */
    public EstadoJuegoGuardado(List<Jugador> jugadores, int indiceJugadorActual) {
        this.jugadores = jugadores;
        this.indiceJugadorActual = indiceJugadorActual;
    }

    public List<Jugador> getJugadores() { return jugadores; }
    public void setJugadores(List<Jugador> jugadores) { this.jugadores = jugadores; }

    public int getIndiceJugadorActual() { return indiceJugadorActual; }
    public void setIndiceJugadorActual(int indiceJugadorActual) { this.indiceJugadorActual = indiceJugadorActual; }
}
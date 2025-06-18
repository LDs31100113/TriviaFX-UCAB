package com.ucab.trivia.modelo;

import java.util.List;

public class EstadoJuegoGuardado {
    private List<Jugador> jugadores;
    private int indiceJugadorActual;

    public EstadoJuegoGuardado() {}

    public EstadoJuegoGuardado(List<Jugador> jugadores, int indiceJugadorActual) {
        this.jugadores = jugadores;
        this.indiceJugadorActual = indiceJugadorActual;
    }

    public List<Jugador> getJugadores() { return jugadores; }
    public void setJugadores(List<Jugador> j) { this.jugadores = j; }
    public int getIndiceJugadorActual() { return indiceJugadorActual; }
    public void setIndiceJugadorActual(int i) { this.indiceJugadorActual = i; }
}
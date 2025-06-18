package com.ucab.trivia.modelo;

import java.util.EnumMap;
import java.util.Map;

/**
 * Representa a un jugador dentro de una partida. Contiene su perfil,
 * su estado en el juego (ficha, posición) y sus estadísticas para la partida actual.
 */
public class Jugador {
    private String alias;
    private String email;
    private Ficha ficha;
    private Posicion posicionActual;
    private boolean estaRendido = false;
    private Map<CategoriaTrivia, Integer> correctasEnPartida;
    private long tiempoTotalEnPartidaMs = 0;

    public Jugador() {}

    public Jugador(PerfilJugador perfil) {
        this.alias = perfil.getAlias();
        this.email = perfil.getEmail();
        this.ficha = new Ficha();
        this.posicionActual = Posicion.enCentro();
        this.correctasEnPartida = new EnumMap<>(CategoriaTrivia.class);
    }

    /**
     * Registra una respuesta correcta para el jugador, actualizando el contador
     * por categoría y el tiempo total de respuesta.
     * @param categoria La categoría de la pregunta correcta.
     * @param tiempoMs El tiempo que tardó en responder.
     */
    public void registrarRespuestaCorrecta(CategoriaTrivia categoria, long tiempoMs) {
        correctasEnPartida.merge(categoria, 1, Integer::sum);
        this.tiempoTotalEnPartidaMs += tiempoMs;
    }

    // Getters y Setters para el estado y la persistencia.
    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Ficha getFicha() { return ficha; }
    public void setFicha(Ficha ficha) { this.ficha = ficha; }
    public Posicion getPosicionActual() { return posicionActual; }
    public void setPosicionActual(Posicion posicionActual) { this.posicionActual = posicionActual; }
    public boolean isEstaRendido() { return estaRendido; }
    public void setEstaRendido(boolean estaRendido) { this.estaRendido = estaRendido; }
    public Map<CategoriaTrivia, Integer> getCorrectasEnPartida() { return correctasEnPartida; }
    public void setCorrectasEnPartida(Map<CategoriaTrivia, Integer> c) { this.correctasEnPartida = c; }
    public long getTiempoTotalEnPartidaMs() { return tiempoTotalEnPartidaMs; }
    public void setTiempoTotalEnPartidaMs(long t) { this.tiempoTotalEnPartidaMs = t; }
}
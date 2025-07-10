package com.ucab.trivia.modelo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.EnumMap;
import java.util.Map;

/**
 * Representa a un jugador dentro de una partida de trivia.
 * Almacena la información del perfil del jugador, su estado en el juego (posición, ficha)
 * y sus estadísticas durante la partida.
 * La anotación {@code @JsonIgnoreProperties(ignoreUnknown = true)} permite que Jackson
 * ignore campos desconocidos al deserializar desde JSON, lo que es útil para la retrocompatibilidad.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Jugador {

    /** El alias o apodo del jugador. */
    private String alias;

    /** El correo electrónico del jugador. */
    private String email;

    /** La ficha del jugador, que rastrea las categorías obtenidas. */
    private Ficha ficha;

    /** La posición actual del jugador en el tablero. */
    private Posicion posicionActual;

    /** Indica si el jugador se ha rendido en la partida actual. */
    private boolean estaRendido = false;

    /** Mapa para contabilizar las respuestas correctas por categoría en la partida actual. */
    private Map<CategoriaTrivia, Integer> correctasEnPartida;

    /** Tiempo total acumulado en milisegundos que el jugador ha tardado en responder. */
    private long tiempoTotalEnPartidaMs = 0;

    /**
     * Constructor por defecto.
     * Necesario para la deserialización de frameworks como Jackson.
     */
    public Jugador() {}

    /**
     * Construye un nuevo jugador a partir de un perfil de jugador existente.
     * Inicializa al jugador en la posición central del tablero y prepara sus estadísticas para una nueva partida.
     *
     * @param perfil El {@link PerfilJugador} con los datos base (alias, email).
     */
    public Jugador(PerfilJugador perfil) {
        this.alias = perfil.getAlias();
        this.email = perfil.getEmail();
        this.ficha = new Ficha();
        this.posicionActual = Posicion.enCentro();
        this.correctasEnPartida = new EnumMap<>(CategoriaTrivia.class);
    }

    /**
     * Registra una respuesta correcta para una categoría específica y actualiza el tiempo total.
     *
     * @param categoria La {@link CategoriaTrivia} de la pregunta respondida correctamente.
     * @param tiempoMs El tiempo en milisegundos que tardó en responder.
     */
    public void registrarRespuestaCorrecta(CategoriaTrivia categoria, long tiempoMs) {
        correctasEnPartida.merge(categoria, 1, Integer::sum);
        this.tiempoTotalEnPartidaMs += tiempoMs;
    }

    // --- Getters y Setters ---

    /**
     * Obtiene el alias del jugador.
     * @return El alias del jugador.
     */
    public String getAlias() { return alias; }

    /**
     * Establece el alias del jugador.
     * @param alias El nuevo alias a establecer.
     */
    public void setAlias(String alias) { this.alias = alias; }

    /**
     * Obtiene el email del jugador.
     * @return El email del jugador.
     */
    public String getEmail() { return email; }

    /**
     * Establece el email del jugador.
     * @param email El nuevo email a establecer.
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Obtiene la ficha del jugador.
     * @return La instancia de {@link Ficha} del jugador.
     */
    public Ficha getFicha() { return ficha; }

    /**
     * Establece la ficha del jugador.
     * @param ficha La nueva ficha a establecer.
     */
    public void setFicha(Ficha ficha) { this.ficha = ficha; }

    /**
     * Obtiene la posición actual del jugador.
     * @return La instancia de {@link Posicion} del jugador.
     */
    public Posicion getPosicionActual() { return posicionActual; }

    /**
     * Establece la posición actual del jugador.
     * @param posicionActual La nueva posición a establecer.
     */
    public void setPosicionActual(Posicion posicionActual) { this.posicionActual = posicionActual; }

    /**
     * Comprueba si el jugador se ha rendido.
     * @return {@code true} si el jugador está rendido, {@code false} en caso contrario.
     */
    public boolean isEstaRendido() { return estaRendido; }

    /**
     * Establece el estado de rendición del jugador.
     * @param estaRendido El nuevo estado de rendición.
     */
    public void setEstaRendido(boolean estaRendido) { this.estaRendido = estaRendido; }

    /**
     * Obtiene el mapa de respuestas correctas por categoría.
     * @return Un mapa que asocia cada {@link CategoriaTrivia} con el número de respuestas correctas.
     */
    public Map<CategoriaTrivia, Integer> getCorrectasEnPartida() { return correctasEnPartida; }

    /**
     * Establece el mapa de respuestas correctas.
     * @param c El nuevo mapa de respuestas correctas.
     */
    public void setCorrectasEnPartida(Map<CategoriaTrivia, Integer> c) { this.correctasEnPartida = c; }

    /**
     * Obtiene el tiempo total de respuesta del jugador en milisegundos.
     * @return El tiempo total en milisegundos.
     */
    public long getTiempoTotalEnPartidaMs() { return tiempoTotalEnPartidaMs; }

    /**
     * Establece el tiempo total de respuesta del jugador en milisegundos.
     * @param t El nuevo tiempo total en milisegundos.
     */
    public void setTiempoTotalEnPartidaMs(long t) { this.tiempoTotalEnPartidaMs = t; }
}
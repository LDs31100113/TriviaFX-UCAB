package com.ucab.trivia.modelo;

import java.util.EnumMap;
import java.util.Map;

/**
 * Almacena las estadísticas persistentes de un jugador a lo largo de múltiples partidas.
 * Esta clase es serializable a JSON para guardar el historial a largo plazo.
 */
public class EstadisticaGlobal {
    private String alias;
    private int partidasJugadas = 0;
    private int partidasGanadas = 0;
    private int partidasPerdidas = 0;
    private Map<CategoriaTrivia, Integer> correctasPorCategoria;
    private long tiempoTotalRespuestasCorrectasMs = 0;

    /**
     * Constructor por defecto, necesario para la deserialización con Jackson.
     * Inicializa el mapa de categorías.
     */
    public EstadisticaGlobal() {
        this.correctasPorCategoria = new EnumMap<>(CategoriaTrivia.class);
    }

    /**
     * Constructor para crear una nueva entrada de estadísticas para un jugador.
     * @param alias El alias único del jugador.
     */
    public EstadisticaGlobal(String alias) {
        this();
        this.alias = alias;
    }

    /**
     * Registra el resultado de una partida finalizada, incrementando los contadores
     * de partidas jugadas y ganadas/perdidas.
     * @param gano true si el jugador ganó la partida, false en caso contrario.
     */
    public void registrarPartidaJugada(boolean gano) {
        this.partidasJugadas++;
        if (gano) {
            this.partidasGanadas++;
        } else {
            this.partidasPerdidas++;
        }
    }

    /**
     * Acumula las estadísticas de una partida finalizada (respuestas correctas y tiempo)
     * a las estadísticas globales del jugador.
     * @param correctas Un mapa con el conteo de respuestas correctas por categoría en la última partida.
     * @param tiempoMs El tiempo total acumulado en respuestas correctas durante la última partida.
     */
    public void agregarEstadisticasDePartida(Map<CategoriaTrivia, Integer> correctas, long tiempoMs) {
        if(correctas != null) {
            correctas.forEach((cat, count) -> correctasPorCategoria.merge(cat, count, Integer::sum));
        }
        this.tiempoTotalRespuestasCorrectasMs += tiempoMs;
    }

    // --- Getters y Setters para Jackson y la visualización en la tabla ---

    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }

    public int getPartidasJugadas() { return partidasJugadas; }
    public void setPartidasJugadas(int n) { this.partidasJugadas = n; }

    public int getPartidasGanadas() { return partidasGanadas; }
    public void setPartidasGanadas(int n) { this.partidasGanadas = n; }

    public int getPartidasPerdidas() { return partidasPerdidas; }
    public void setPartidasPerdidas(int n) { this.partidasPerdidas = n; }

    public Map<CategoriaTrivia, Integer> getCorrectasPorCategoria() { return correctasPorCategoria; }
    public void setCorrectasPorCategoria(Map<CategoriaTrivia, Integer> map) { this.correctasPorCategoria = map; }

    public long getTiempoTotalRespuestasCorrectasMs() { return tiempoTotalRespuestasCorrectasMs; }
    public void setTiempoTotalRespuestasCorrectasMs(long t) { this.tiempoTotalRespuestasCorrectasMs = t; }
}
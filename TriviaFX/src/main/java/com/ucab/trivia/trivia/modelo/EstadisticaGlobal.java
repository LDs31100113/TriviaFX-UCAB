package com.ucab.trivia.modelo;

import java.util.EnumMap;
import java.util.Map;

public class EstadisticaGlobal {
    private String alias;
    private int partidasJugadas = 0;
    private int partidasGanadas = 0;
    private int partidasPerdidas = 0;
    private Map<CategoriaTrivia, Integer> correctasPorCategoria;
    private long tiempoTotalRespuestasCorrectasMs = 0;

    public EstadisticaGlobal() {
        this.correctasPorCategoria = new EnumMap<>(CategoriaTrivia.class);
    }

    public EstadisticaGlobal(String alias) {
        this();
        this.alias = alias;
    }

    public void registrarPartidaJugada(boolean gano) {
        this.partidasJugadas++;
        if (gano) this.partidasGanadas++;
        else this.partidasPerdidas++;
    }

    public void agregarEstadisticasDePartida(Map<CategoriaTrivia, Integer> correctas, long tiempoMs) {
        if(correctas != null) {
            correctas.forEach((cat, count) -> correctasPorCategoria.merge(cat, count, Integer::sum));
        }
        this.tiempoTotalRespuestasCorrectasMs += tiempoMs;
    }

    // Getters y Setters para Jackson
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
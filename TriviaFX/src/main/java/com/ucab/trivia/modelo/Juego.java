package com.ucab.trivia.modelo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Juego {
    private List<Jugador> jugadores;
    private int indiceJugadorActual;
    private final TableroGrafico tablero;
    private final Dado dado;
    private final ServicioPreguntasJuego servicioPreguntas;
    private final ServicioDatos servicioDatos;

    public Juego(List<PerfilJugador> perfiles) {
        this.tablero = new TableroGrafico();
        this.dado = new Dado();
        this.servicioPreguntas = new ServicioPreguntasJuego();
        this.servicioDatos = new ServicioDatos();
        this.jugadores = perfiles.stream().map(Jugador::new).collect(Collectors.toList());
        this.indiceJugadorActual = 0;
    }

    public Juego(EstadoJuegoGuardado estado) {
        this.tablero = new TableroGrafico();
        this.dado = new Dado();
        this.servicioPreguntas = new ServicioPreguntasJuego();
        this.servicioDatos = new ServicioDatos();
        this.jugadores = estado.getJugadores();
        this.indiceJugadorActual = estado.getIndiceJugadorActual();
    }

    public void guardarEstadoActualDelJuego() {
        if (jugadores != null && !jugadores.isEmpty()) {
            EstadoJuegoGuardado estadoActual = new EstadoJuegoGuardado(new ArrayList<>(jugadores), this.indiceJugadorActual);
            servicioDatos.guardarPartida(estadoActual);
        }
    }

    public void setIndiceJugadorActual(int indice) {
        if (indice >= 0 && indice < jugadores.size()) {
            this.indiceJugadorActual = indice;
        }
    }

    public int lanzarDado() {
        return dado.lanzar();
    }

    public void moverJugador(Jugador jugador, int pasos, int direccion, boolean eligeEntrarRayo) {
        if(jugador.getPosicionActual().getTipo() == Posicion.TipoLugar.CENTRO) {
            // **CORRECCIÓN:** La lógica de salida del centro es moverse HACIA AFUERA por un rayo.
            // Un lanzamiento de 1 te deja en la casilla del rayo más cercana al centro (índice 4).
            // Un lanzamiento de 5 te deja en la casilla del rayo más lejana al centro (índice 0).
            int rayoElegido = direccion; // El controlador pasa el índice del rayo (0-5)
            if (pasos <= TableroGrafico.CASILLAS_POR_RAYO_INTERNO) {
                // Se resta de la última casilla para que el movimiento sea "hacia afuera".
                int indiceEnRayo = TableroGrafico.CASILLAS_POR_RAYO_INTERNO - pasos;
                jugador.setPosicionActual(Posicion.enRayo(rayoElegido, indiceEnRayo));
            } else {
                // Si saca más pasos que el largo del rayo, llega a la casilla del círculo.
                int indiceCasillaCircular = rayoElegido * (TableroGrafico.NUMERO_CASILLAS_CIRCULO / TableroGrafico.NUMERO_RAYOS);
                jugador.setPosicionActual(Posicion.enCirculo(indiceCasillaCircular));
            }
        } else {
            jugador.setPosicionActual(tablero.calcularNuevaPosicion(jugador.getPosicionActual(), pasos, direccion, eligeEntrarRayo));
        }
    }

    // ... (El resto de los métodos como rendirse, pasarTurno, getters, etc. se mantienen igual) ...
    public void rendirse() { getJugadorActual().setEstaRendido(true); }
    public Jugador determinarGanadorPorRendicion() { List<Jugador> activos = getJugadoresActivos(); if (activos.size() == 1) return activos.get(0); if (activos.isEmpty()) return jugadores.stream().max(Comparator.comparingInt((Jugador j) -> j.getFicha().getCategoriasObtenidasCount()).thenComparingLong(j -> -j.getTiempoTotalEnPartidaMs())).orElse(null); return null; }
    public void pasarTurno() { if(getJugadoresActivos().isEmpty()) return; do { indiceJugadorActual = (indiceJugadorActual + 1) % jugadores.size(); } while (getJugadorActual().isEstaRendido()); }
    public Jugador getJugadorActual() { if (jugadores == null || jugadores.isEmpty()) return null; return jugadores.get(indiceJugadorActual); }
    public List<Jugador> getJugadores() { return jugadores; }
    public List<Jugador> getJugadoresActivos() { return jugadores.stream().filter(j -> !j.isEstaRendido()).collect(Collectors.toList()); }
    public TableroGrafico getTablero() { return tablero; }
    public PreguntaOriginal getPreguntaParaPosicion(Posicion pos) { if(pos == null || pos.getTipo() == Posicion.TipoLugar.CENTRO) return null; Casilla c = tablero.getCasillaEnPosicion(pos); if (c == null || c.getCategoria() == null) return null; return servicioPreguntas.seleccionarPreguntaAleatoria(c.getCategoria()); }
}
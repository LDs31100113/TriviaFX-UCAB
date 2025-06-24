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
        determinarPrimerJugador();
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
            EstadoJuegoGuardado estadoActual = new EstadoJuegoGuardado(new ArrayList<>(jugadores), indiceJugadorActual);
            servicioDatos.guardarPartida(estadoActual);
        }
    }

    private void determinarPrimerJugador() {
        if (jugadores == null || jugadores.size() <= 1) {
            indiceJugadorActual = 0;
            return;
        }
        indiceJugadorActual = (int) (Math.random() * jugadores.size());
    }

    /**
     * **MÉTODO CORREGIDO**
     * Ahora devuelve el valor exacto del dado, sin ninguna bonificación.
     * @return El resultado del dado (un número del 1 al 6).
     */
    public int lanzarDado() {
        return dado.lanzar();
    }

    public void moverJugador(Jugador jugador, int pasos, boolean eligeEntrarRayo) {
        if(jugador.getPosicionActual().getTipo() == Posicion.TipoLugar.CENTRO) {
            int rayoElegido = (int) (Math.random() * 6);
            int indiceSalida = (rayoElegido * 7) + (pasos - 1) % 7;
            jugador.setPosicionActual(Posicion.enCirculo(indiceSalida % TableroGrafico.NUMERO_CASILLAS_CIRCULO));
        } else {
            jugador.setPosicionActual(tablero.calcularNuevaPosicion(jugador.getPosicionActual(), pasos, eligeEntrarRayo));
        }
    }

    public void rendirse() {
        getJugadorActual().setEstaRendido(true);
    }

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

    public void pasarTurno() {
        if(getJugadoresActivos().isEmpty()) return;
        do {
            indiceJugadorActual = (indiceJugadorActual + 1) % jugadores.size();
        } while (getJugadorActual().isEstaRendido());
    }

    public Jugador getJugadorActual() {
        if (jugadores == null || jugadores.isEmpty()) return null;
        return jugadores.get(indiceJugadorActual);
    }
    public List<Jugador> getJugadores() { return jugadores; }
    public List<Jugador> getJugadoresActivos() {
        return jugadores.stream().filter(j -> !j.isEstaRendido()).collect(Collectors.toList());
    }
    public TableroGrafico getTablero() { return tablero; }

    public PreguntaOriginal getPreguntaParaPosicion(Posicion pos) {
        if(pos == null || pos.getTipo() == Posicion.TipoLugar.CENTRO) return null;
        Casilla c = tablero.getCasillaEnPosicion(pos);
        if (c == null || c.getCategoria() == null) return null;
        return servicioPreguntas.seleccionarPreguntaAleatoria(c.getCategoria());
    }
}
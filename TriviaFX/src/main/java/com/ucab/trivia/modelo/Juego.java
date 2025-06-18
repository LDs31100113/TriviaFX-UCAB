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

    public Juego(List<PerfilJugador> perfiles) {
        this.tablero = new TableroGrafico();
        this.dado = new Dado();
        this.servicioPreguntas = new ServicioPreguntasJuego();
        this.jugadores = perfiles.stream().map(Jugador::new).collect(Collectors.toList());
        determinarPrimerJugador();
    }

    public Juego(EstadoJuegoGuardado estado) {
        this.tablero = new TableroGrafico();
        this.dado = new Dado();
        this.servicioPreguntas = new ServicioPreguntasJuego();
        this.jugadores = estado.getJugadores();
        this.indiceJugadorActual = estado.getIndiceJugadorActual();
    }

    private void determinarPrimerJugador() {
        if (jugadores == null || jugadores.size() <= 1) {
            indiceJugadorActual = 0;
            return;
        }
        // En una GUI, esto debería ser un proceso visual, pero para la lógica, un inicio aleatorio es suficiente.
        indiceJugadorActual = (int) (Math.random() * jugadores.size());
    }

    public int lanzarDado() {
        int lanzamiento = dado.lanzar();
        // Regla de número impar + 2
        if(lanzamiento % 2 != 0) {
            return lanzamiento + 2;
        }
        return lanzamiento;
    }

    public void moverJugador(Jugador jugador, int pasos, boolean eligeEntrarRayo) {
        if(jugador.getPosicionActual().getTipo() == Posicion.TipoLugar.CENTRO) {
            // La salida del centro es un caso especial manejado por el controlador
            // donde se elige una dirección (un rayo) y se mueve 1 paso.
            // Para simplificar, asumimos que se mueve a una casilla de rayo inicial.
            int rayoElegido = (int) (Math.random() * TableroGrafico.NUMERO_RAYOS);
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
        // Si solo queda un jugador activo, ese gana.
        if (jugadoresActivos.size() == 1) {
            return jugadoresActivos.get(0);
        }
        // Si ya no quedan jugadores activos (todos se rindieron).
        if (jugadoresActivos.isEmpty()) {
            // Gana el que tenga más categorías. En caso de empate, el que tenga menor tiempo.
            return jugadores.stream()
                    .max(Comparator.comparingInt((Jugador j) -> j.getFicha().getCategoriasObtenidasCount())
                            .thenComparingLong(j -> -j.getTiempoTotalEnPartidaMs())) // Negativo para que menor tiempo sea mayor valor
                    .orElse(null);
        }
        // Si quedan más de un jugador activo, el juego continúa.
        return null;
    }

    public void pasarTurno() {
        if(getJugadoresActivos().isEmpty()) return; // Si no hay jugadores activos, no hacer nada.
        // Avanzar al siguiente jugador en la lista, saltando a los que se han rendido.
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
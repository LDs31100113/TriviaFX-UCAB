package com.ucab.trivia.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Modela la estructura y la lógica del tablero de juego.
 * Esta clase no es un componente de UI, sino que representa la disposición de las casillas,
 * incluyendo el círculo exterior y los rayos internos que conducen al centro.
 * También gestiona la lógica de movimiento de los jugadores sobre este tablero.
 */
public class TableroGrafico {

    /** Número total de casillas en el círculo exterior del tablero. */
    public static final int NUMERO_CASILLAS_CIRCULO = 42;
    /** Número total de rayos que conectan el círculo con el centro. */
    public static final int NUMERO_RAYOS = 6;
    /** Número de casillas que componen cada uno de los rayos internos. */
    public static final int CASILLAS_POR_RAYO_INTERNO = 5;

    /** Lista que contiene las casillas del círculo exterior. */
    private final List<Casilla> casillasCirculo;
    /** Lista de listas, donde cada lista interna representa las casillas de un rayo. */
    private final List<List<Casilla>> casillasRayos;
    /** Mapa para asociar una casilla de entrada en el círculo con su rayo correspondiente. */
    private final Map<Integer, Integer> mapaCirculoARayo;

    /**
     * Construye un nuevo tablero, inicializando sus componentes y
     * poblando las casillas del círculo y de los rayos.
     */
    public TableroGrafico() {
        this.casillasCirculo = new ArrayList<>(NUMERO_CASILLAS_CIRCULO);
        this.casillasRayos = new ArrayList<>(NUMERO_RAYOS);
        for (int i = 0; i < NUMERO_RAYOS; i++) {
            this.casillasRayos.add(new ArrayList<>(CASILLAS_POR_RAYO_INTERNO));
        }
        this.mapaCirculoARayo = new HashMap<>();
        inicializarTablero();
    }

    /**
     * Inicializa la configuración de las casillas del tablero.
     * Genera las 42 casillas del círculo, asignando categorías de forma cíclica.
     * Designa las casillas de entrada a los rayos y las casillas especiales de "lanzar de nuevo".
     * Al crear una casilla de entrada, también genera el rayo correspondiente.
     */
    private void inicializarTablero() {
        CategoriaTrivia[] categorias = CategoriaTrivia.values();
        int casillasPorSegmento = NUMERO_CASILLAS_CIRCULO / NUMERO_RAYOS; // Debería ser 7

        for (int i = 0; i < NUMERO_CASILLAS_CIRCULO; i++) {
            CategoriaTrivia catActual = categorias[i % categorias.length];
            // La primera casilla de cada segmento (índice 0, 7, 14, ...) es una entrada de rayo.
            boolean esEntradaRayo = (i % casillasPorSegmento == 0);
            // Las dos casillas siguientes a la de entrada son especiales de "Re-Roll".
            boolean esEspecialReRoll = !esEntradaRayo && (i % casillasPorSegmento == 1 || i % casillasPorSegmento == 2);

            casillasCirculo.add(new Casilla(catActual, esEspecialReRoll, esEntradaRayo));

            if (esEntradaRayo) {
                int indiceRayo = i / casillasPorSegmento;
                mapaCirculoARayo.put(i, indiceRayo);
                // Inicializa el rayo asociado a esta entrada.
                for (int j = 0; j < CASILLAS_POR_RAYO_INTERNO; j++) {
                    casillasRayos.get(indiceRayo).add(new Casilla(catActual, false, false));
                }
            }
        }
    }

    /**
     * Obtiene el objeto Casilla correspondiente a una Posicion dada.
     *
     * @param pos La {@link Posicion} de la cual se desea obtener la casilla.
     * @return El objeto {@link Casilla} en esa posición, o {@code null} si la posición es nula o del tipo CENTRO.
     */
    public Casilla getCasillaEnPosicion(Posicion pos) {
        if (pos == null) return null;
        switch (pos.getTipo()) {
            case CIRCULO:
                return casillasCirculo.get(pos.getIndiceCirculo());
            case RAYO:
                return casillasRayos.get(pos.getIndiceRayo()).get(pos.getIndiceEnRayo());
            default: // CENTRO u otros tipos no tienen una casilla como tal.
                return null;
        }
    }

    /**
     * Calcula la nueva posición de un jugador basado en su posición actual y el resultado del dado.
     *
     * @param actual La {@link Posicion} actual del jugador.
     * @param pasos El número de pasos a mover (resultado del dado).
     * @param eligeEntrarRayo {@code true} si el jugador decide entrar a un rayo al caer en una casilla de entrada.
     * @return La nueva {@link Posicion} después de realizar el movimiento.
     */
    public Posicion calcularNuevaPosicion(Posicion actual, int pasos, boolean eligeEntrarRayo) {
        // La lógica para salir del centro se maneja en la clase Juego, esta función no contempla ese caso.
        if (actual.getTipo() == Posicion.TipoLugar.CENTRO) {
            return actual;
        }

        if (actual.getTipo() == Posicion.TipoLugar.CIRCULO) {
            Casilla casillaActual = getCasillaEnPosicion(actual);
            // Si el jugador está en una entrada de rayo y elige entrar.
            if (casillaActual.isEsEntradaRayo() && eligeEntrarRayo) {
                int indiceRayo = mapaCirculoARayo.get(actual.getIndiceCirculo());
                // Si con los pasos no llega al final del rayo, se mueve dentro de él.
                if (pasos - 1 < CASILLAS_POR_RAYO_INTERNO) {
                    return Posicion.enRayo(indiceRayo, pasos - 1);
                } else { // Si los pasos son suficientes o exceden el largo del rayo, llega al centro.
                    return Posicion.enCentro();
                }
            } else { // Movimiento normal en el círculo.
                int nuevoIndice = (actual.getIndiceCirculo() + pasos) % NUMERO_CASILLAS_CIRCULO;
                return Posicion.enCirculo(nuevoIndice);
            }
        } else if (actual.getTipo() == Posicion.TipoLugar.RAYO) {
            int nuevoIndiceEnRayo = actual.getIndiceEnRayo() + pasos;
            // Si el movimiento lo lleva al final o más allá del rayo, llega al centro.
            if (nuevoIndiceEnRayo >= CASILLAS_POR_RAYO_INTERNO) {
                return Posicion.enCentro();
            } else { // Movimiento normal dentro del rayo.
                return Posicion.enRayo(actual.getIndiceRayo(), nuevoIndiceEnRayo);
            }
        }
        // Devuelve la posición actual si el tipo es desconocido o no se pudo calcular.
        return actual;
    }
}
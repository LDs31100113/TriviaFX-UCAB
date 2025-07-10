package com.ucab.trivia.modelo;

/**
 * Representa una casilla individual en el tablero del juego.
 * Cada casilla está definida por una categoría de trivia y puede tener
 * propiedades especiales, como permitir volver a lanzar el dado o ser la
 * entrada a un "rayo" para obtener una ficha de categoría.
 */
public class Casilla {

    /** La categoría de trivia asociada a esta casilla. */
    private final CategoriaTrivia categoria;

    /** Indica si esta casilla permite al jugador volver a lanzar el dado (Re-Roll). */
    private final boolean esEspecialReRoll;

    /** Indica si esta casilla es una entrada a un rayo de categoría. */
    private final boolean esEntradaRayo;

    /**
     * Construye una nueva casilla con sus propiedades específicas.
     *
     * @param categoria La {@link CategoriaTrivia} de la casilla.
     * @param esEspecialReRoll {@code true} si es una casilla de "lanzar de nuevo", {@code false} en caso contrario.
     * @param esEntradaRayo {@code true} si es una casilla de entrada a un rayo, {@code false} en caso contrario.
     */
    public Casilla(CategoriaTrivia categoria, boolean esEspecialReRoll, boolean esEntradaRayo) {
        this.categoria = categoria;
        this.esEspecialReRoll = esEspecialReRoll;
        this.esEntradaRayo = esEntradaRayo;
    }

    /**
     * Obtiene la categoría de la casilla.
     *
     * @return La {@link CategoriaTrivia} asociada a esta casilla.
     */
    public CategoriaTrivia getCategoria() {
        return categoria;
    }

    /**
     * Comprueba si la casilla es de tipo especial "lanzar de nuevo".
     *
     * @return {@code true} si el jugador puede volver a lanzar el dado, {@code false} en caso contrario.
     */
    public boolean isEsEspecialReRoll() {
        return esEspecialReRoll;
    }

    /**
     * Comprueba si la casilla es una entrada a un rayo de categoría.
     *
     * @return {@code true} si desde esta casilla se puede acceder a un rayo, {@code false} en caso contrario.
     */
    public boolean isEsEntradaRayo() {
        return esEntradaRayo;
    }
}
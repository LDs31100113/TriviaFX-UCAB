package com.ucab.trivia.modelo;

/**
 * Representa una casilla individual en el tablero.
 * Contiene una categoría y puede tener propiedades especiales.
 */
public class Casilla {
    private final CategoriaTrivia categoria;
    private final boolean esEspecialReRoll;
    private final boolean esEntradaRayo;

    public Casilla(CategoriaTrivia categoria, boolean esEspecialReRoll, boolean esEntradaRayo) {
        this.categoria = categoria;
        this.esEspecialReRoll = esEspecialReRoll;
        this.esEntradaRayo = esEntradaRayo;
    }
    public CategoriaTrivia getCategoria() { return categoria; }
    public boolean isEsEspecialReRoll() { return esEspecialReRoll; }
    public boolean isEsEntradaRayo() { return esEntradaRayo; }
}
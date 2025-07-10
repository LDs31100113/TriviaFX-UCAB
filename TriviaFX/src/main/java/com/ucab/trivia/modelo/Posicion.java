package com.ucab.trivia.modelo;

import java.util.Objects;

/**
 * Representa una ubicación específica en el tablero del juego.
 * Una posición puede ser en el centro, en el círculo exterior o en uno de los rayos
 * que conectan el círculo con el centro. Esta clase es inmutable cuando se crea
 * a través de sus métodos de fábrica estáticos, pero también puede ser usada como
 * un bean (POJO) gracias a su constructor por defecto y sus setters.
 */
public class Posicion {

    /**
     * Enumera los posibles tipos de lugares en el tablero.
     */
    public enum TipoLugar {
        /** El área central del tablero, el objetivo final. */
        CENTRO,
        /** El anillo exterior principal del tablero donde los jugadores se mueven. */
        CIRCULO,
        /** Los caminos que conectan el círculo exterior con el centro. */
        RAYO
    }

    private TipoLugar tipo;
    private int indiceCirculo;
    private int indiceRayo;
    private int indiceEnRayo;

    /**
     * Constructor privado para ser utilizado por los métodos de fábrica estáticos.
     * @param tipo El tipo de lugar para esta posición.
     */
    private Posicion(TipoLugar tipo) { this.tipo = tipo; }

    /**
     * Crea un objeto Posicion que representa el centro del tablero.
     * @return Una nueva instancia de {@code Posicion} en el centro.
     */
    public static Posicion enCentro() { return new Posicion(TipoLugar.CENTRO); }

    /**
     * Crea un objeto Posicion en una casilla del círculo exterior.
     * @param indice El índice de la casilla en el círculo (0-41).
     * @return Una nueva instancia de {@code Posicion} en el círculo.
     * @throws IllegalArgumentException si el índice está fuera del rango válido.
     */
    public static Posicion enCirculo(int indice) {
        if (indice < 0 || indice >= 42) throw new IllegalArgumentException("Índice de círculo inválido: " + indice);
        Posicion p = new Posicion(TipoLugar.CIRCULO);
        p.indiceCirculo = indice;
        return p;
    }

    /**
     * Crea un objeto Posicion en una casilla de un rayo.
     * @param indiceRayo El índice del rayo (0-5).
     * @param indiceEnRayo El índice de la casilla dentro del rayo (0-4).
     * @return Una nueva instancia de {@code Posicion} en un rayo.
     * @throws IllegalArgumentException si alguno de los índices está fuera del rango válido.
     */
    public static Posicion enRayo(int indiceRayo, int indiceEnRayo) {
        if (indiceRayo < 0 || indiceRayo >= 6) throw new IllegalArgumentException("Índice de rayo inválido: " + indiceRayo);
        if (indiceEnRayo < 0 || indiceEnRayo >= 5) throw new IllegalArgumentException("Índice en rayo inválido: " + indiceEnRayo);
        Posicion p = new Posicion(TipoLugar.RAYO);
        p.indiceRayo = indiceRayo;
        p.indiceEnRayo = indiceEnRayo;
        return p;
    }

    /**
     * Constructor por defecto. Requerido para frameworks de (de)serialización como Jackson.
     */
    public Posicion() {}

    // --- Getters y Setters ---

    /**
     * Obtiene el tipo de lugar de la posición.
     * @return El {@link TipoLugar} (CENTRO, CIRCULO, RAYO).
     */
    public TipoLugar getTipo() { return tipo; }

    /**
     * Obtiene el índice de la casilla en el círculo. Solo relevante si getTipo() es CIRCULO.
     * @return El índice en el círculo.
     */
    public int getIndiceCirculo() { return indiceCirculo; }

    /**
     * Obtiene el índice del rayo. Solo relevante si getTipo() es RAYO.
     * @return El índice del rayo.
     */
    public int getIndiceRayo() { return indiceRayo; }

    /**
     * Obtiene el índice de la casilla dentro de un rayo. Solo relevante si getTipo() es RAYO.
     * @return El índice de la casilla en el rayo.
     */
    public int getIndiceEnRayo() { return indiceEnRayo; }

    /**
     * Establece el tipo de lugar.
     * @param t El nuevo {@link TipoLugar}.
     */
    public void setTipo(TipoLugar t) { this.tipo = t; }

    /**
     * Establece el índice del círculo.
     * @param i El nuevo índice del círculo.
     */
    public void setIndiceCirculo(int i) { this.indiceCirculo = i; }

    /**
     * Establece el índice del rayo.
     * @param i El nuevo índice del rayo.
     */
    public void setIndiceRayo(int i) { this.indiceRayo = i; }

    /**
     * Establece el índice de la casilla en el rayo.
     * @param i El nuevo índice en el rayo.
     */
    public void setIndiceEnRayo(int i) { this.indiceEnRayo = i; }


    /**
     * Compara esta Posicion con otro objeto para determinar si son iguales.
     * Dos posiciones son iguales si son del mismo tipo y sus índices correspondientes coinciden.
     * @param o El objeto a comparar.
     * @return {@code true} si las posiciones son iguales, {@code false} en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Posicion p = (Posicion) o;
        if (tipo != p.tipo) return false;
        switch (tipo) {
            case CENTRO: return true;
            case CIRCULO: return indiceCirculo == p.indiceCirculo;
            case RAYO: return indiceRayo == p.indiceRayo && indiceEnRayo == p.indiceEnRayo;
            default: return false;
        }
    }

    /**
     * Genera un código hash para la Posicion.
     * El hash se calcula en función del tipo y los índices relevantes, manteniendo la consistencia
     * con el método {@link #equals(Object)}.
     * @return El código hash de la posición.
     */
    @Override
    public int hashCode() {
        switch (tipo) {
            case CENTRO: return Objects.hash(tipo);
            case CIRCULO: return Objects.hash(tipo, indiceCirculo);
            case RAYO: return Objects.hash(tipo, indiceRayo, indiceEnRayo);
            default: return super.hashCode();
        }
    }

    /**
     * Devuelve una representación en String de la Posicion, útil para depuración o para la UI.
     * Los índices se muestran en formato legible para humanos (base 1 en lugar de base 0).
     * @return Un String legible que representa la posición.
     */
    @Override
    public String toString() {
        switch (tipo) {
            case CENTRO: return "Centro";
            case CIRCULO: return "Círculo, Casilla " + (indiceCirculo + 1);
            case RAYO: return "Rayo " + (indiceRayo + 1) + ", Casilla " + (indiceEnRayo + 1);
            default: return "Desconocida";
        }
    }
}
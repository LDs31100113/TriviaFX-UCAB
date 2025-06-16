package com.ucab.trivia.modelo;

import java.util.Objects;

public class Posicion {
    public enum TipoLugar { CENTRO, CIRCULO, RAYO }
    private TipoLugar tipo;
    private int indiceCirculo;
    private int indiceRayo;
    private int indiceEnRayo;

    private Posicion(TipoLugar tipo) { this.tipo = tipo; }
    public static Posicion enCentro() { return new Posicion(TipoLugar.CENTRO); }
    public static Posicion enCirculo(int indice) {
        if (indice < 0 || indice >= 42) throw new IllegalArgumentException("Índice de círculo inválido");
        Posicion p = new Posicion(TipoLugar.CIRCULO);
        p.indiceCirculo = indice;
        return p;
    }
    public static Posicion enRayo(int indiceRayo, int indiceEnRayo) {
        if (indiceRayo < 0 || indiceRayo >= 6) throw new IllegalArgumentException("Índice de rayo inválido");
        if (indiceEnRayo < 0 || indiceEnRayo >= 5) throw new IllegalArgumentException("Índice en rayo inválido");
        Posicion p = new Posicion(TipoLugar.RAYO);
        p.indiceRayo = indiceRayo;
        p.indiceEnRayo = indiceEnRayo;
        return p;
    }
    public Posicion() {}
    public TipoLugar getTipo() { return tipo; }
    public int getIndiceCirculo() { return indiceCirculo; }
    public int getIndiceRayo() { return indiceRayo; }
    public int getIndiceEnRayo() { return indiceEnRayo; }
    public void setTipo(TipoLugar t) { this.tipo = t; }
    public void setIndiceCirculo(int i) { this.indiceCirculo = i; }
    public void setIndiceRayo(int i) { this.indiceRayo = i; }
    public void setIndiceEnRayo(int i) { this.indiceEnRayo = i; }

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

    @Override
    public int hashCode() {
        switch (tipo) {
            case CENTRO: return Objects.hash(tipo);
            case CIRCULO: return Objects.hash(tipo, indiceCirculo);
            case RAYO: return Objects.hash(tipo, indiceRayo, indiceEnRayo);
            default: return super.hashCode();
        }
    }
}
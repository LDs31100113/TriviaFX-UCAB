package com.ucab.trivia.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableroGrafico {
    public static final int NUMERO_CASILLAS_CIRCULO = 42;
    public static final int NUMERO_RAYOS = 6;
    public static final int CASILLAS_POR_RAYO_INTERNO = 5;
    private final List<Casilla> casillasCirculo;
    private final List<List<Casilla>> casillasRayos;
    private final Map<Integer, Integer> mapaCirculoARayo;

    public TableroGrafico() {
        this.casillasCirculo = new ArrayList<>(NUMERO_CASILLAS_CIRCULO);
        this.casillasRayos = new ArrayList<>(NUMERO_RAYOS);
        for (int i = 0; i < NUMERO_RAYOS; i++) this.casillasRayos.add(new ArrayList<>(CASILLAS_POR_RAYO_INTERNO));
        this.mapaCirculoARayo = new HashMap<>();
        inicializarTablero();
    }

    private void inicializarTablero() {
        CategoriaTrivia[] categorias = CategoriaTrivia.values();
        int casillasPorSegmento = NUMERO_CASILLAS_CIRCULO / NUMERO_RAYOS;
        for (int i = 0; i < NUMERO_CASILLAS_CIRCULO; i++) {
            CategoriaTrivia catActual = categorias[i % categorias.length];
            boolean esEntradaRayo = (i % casillasPorSegmento == 0);
            boolean esEspecial = !esEntradaRayo && (i % casillasPorSegmento == 1 || i % casillasPorSegmento == 2);
            casillasCirculo.add(new Casilla(catActual, esEspecial, esEntradaRayo));
            if (esEntradaRayo) {
                int indiceRayo = i / casillasPorSegmento;
                mapaCirculoARayo.put(i, indiceRayo);
                for (int j = 0; j < CASILLAS_POR_RAYO_INTERNO; j++) {
                    casillasRayos.get(indiceRayo).add(new Casilla(catActual, false, false));
                }
            }
        }
    }

    public Casilla getCasillaEnPosicion(Posicion pos) {
        if (pos == null) return null;
        switch (pos.getTipo()) {
            case CIRCULO: return casillasCirculo.get(pos.getIndiceCirculo());
            case RAYO: return casillasRayos.get(pos.getIndiceRayo()).get(pos.getIndiceEnRayo());
            default: return null;
        }
    }

    public Posicion calcularNuevaPosicion(Posicion actual, int pasos, boolean eligeEntrarRayo) {
        if(actual.getTipo() == Posicion.TipoLugar.CENTRO){
            // La salida del centro la maneja el controlador, que elige un rayo
            // y mueve un paso inicial, esta lógica es para movimientos subsecuentes.
            // Por seguridad, si se llama desde el centro, no se mueve.
            return actual;
        }

        if (actual.getTipo() == Posicion.TipoLugar.CIRCULO) {
            if (getCasillaEnPosicion(actual).isEsEntradaRayo() && eligeEntrarRayo) {
                int indiceRayo = mapaCirculoARayo.get(actual.getIndiceCirculo());
                if (pasos - 1 < CASILLAS_POR_RAYO_INTERNO) return Posicion.enRayo(indiceRayo, pasos - 1);
                else return Posicion.enCentro();
            } else {
                int nuevoIndice = (actual.getIndiceCirculo() + pasos) % NUMERO_CASILLAS_CIRCULO;
                return Posicion.enCirculo(nuevoIndice);
            }
        } else if (actual.getTipo() == Posicion.TipoLugar.RAYO) {
            int nuevoIndice = actual.getIndiceEnRayo() + pasos;
            if (nuevoIndice >= CASILLAS_POR_RAYO_INTERNO) return Posicion.enCentro();
            else return Posicion.enRayo(actual.getIndiceRayo(), nuevoIndice);
        }
        return actual;
    }
}
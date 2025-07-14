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
    private final Map<Integer, Integer> mapaRayoACirculo;

    public TableroGrafico() {
        this.casillasCirculo = new ArrayList<>(NUMERO_CASILLAS_CIRCULO);
        this.casillasRayos = new ArrayList<>(NUMERO_RAYOS);
        for (int i = 0; i < NUMERO_RAYOS; i++) this.casillasRayos.add(new ArrayList<>(CASILLAS_POR_RAYO_INTERNO));
        this.mapaCirculoARayo = new HashMap<>();
        this.mapaRayoACirculo = new HashMap<>();
        inicializarTablero();
    }

    private void inicializarTablero() {
        CategoriaTrivia[] categorias = CategoriaTrivia.values();
        int casillasPorSegmento = NUMERO_CASILLAS_CIRCULO / NUMERO_RAYOS;
        for (int i = 0; i < NUMERO_CASILLAS_CIRCULO; i++) {
            CategoriaTrivia catActual = categorias[i % categorias.length];
            boolean esEntradaRayo = (i % casillasPorSegmento == 0);
            boolean esEspecialReRoll = !esEntradaRayo && (i % casillasPorSegmento == 1 || i % casillasPorSegmento == 2);
            casillasCirculo.add(new Casilla(catActual, esEspecialReRoll, esEntradaRayo));
            if (esEntradaRayo) {
                int indiceRayo = i / casillasPorSegmento;
                mapaCirculoARayo.put(i, indiceRayo);
                mapaRayoACirculo.put(indiceRayo, i); // Añadir el mapeo inverso
                for (int j = 0; j < CASILLAS_POR_RAYO_INTERNO; j++) {
                    CategoriaTrivia catRayo = categorias[(j + indiceRayo) % categorias.length];
                    casillasRayos.get(indiceRayo).add(new Casilla(catRayo, false, false));
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

    public Posicion calcularNuevaPosicion(Posicion actual, int pasos, int direccion, boolean eligeEntrarRayo) {
        if (actual.getTipo() == Posicion.TipoLugar.CIRCULO) {
            Casilla casillaActual = getCasillaEnPosicion(actual);
            if (casillaActual.isEsEntradaRayo() && eligeEntrarRayo) {
                int indiceRayo = mapaCirculoARayo.get(actual.getIndiceCirculo());
                if (pasos - 1 < CASILLAS_POR_RAYO_INTERNO) return Posicion.enRayo(indiceRayo, pasos - 1);
                else return Posicion.enCentro();
            } else {
                int nuevoIndice = (actual.getIndiceCirculo() + (pasos * direccion) + NUMERO_CASILLAS_CIRCULO) % NUMERO_CASILLAS_CIRCULO;
                return Posicion.enCirculo(nuevoIndice);
            }
        } else if (actual.getTipo() == Posicion.TipoLugar.RAYO) {
            // **INICIO DE MODIFICACIÓN: Movimiento bidireccional en el rayo**
            int nuevoIndiceEnRayo = actual.getIndiceEnRayo() + (pasos * direccion);
            if (direccion == 1 && nuevoIndiceEnRayo >= CASILLAS_POR_RAYO_INTERNO) {
                return Posicion.enCentro(); // Se pasó del final, llega al centro
            } else if (direccion == -1 && nuevoIndiceEnRayo < 0) {
                // Se pasó del inicio, sale al círculo y se mueve los pasos restantes
                int indiceCasillaCircular = this.mapaRayoACirculo.get(actual.getIndiceRayo());
                int pasosRestantes = Math.abs(nuevoIndiceEnRayo) - 1;
                // El movimiento en el círculo se decidirá en el siguiente turno. Por ahora, solo se mueve al círculo.
                int nuevoIndiceCircular = (indiceCasillaCircular + pasosRestantes + NUMERO_CASILLAS_CIRCULO) % NUMERO_CASILLAS_CIRCULO;
                return Posicion.enCirculo(nuevoIndiceCircular);
            } else {
                return Posicion.enRayo(actual.getIndiceRayo(), nuevoIndiceEnRayo);
            }
            // **FIN DE MODIFICACIÓN**
        }
        return actual;
    }
}
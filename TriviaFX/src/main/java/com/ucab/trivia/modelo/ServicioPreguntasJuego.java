package com.ucab.trivia.modelo;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class ServicioPreguntasJuego {
    private final Map<String, List<PreguntaOriginal>> preguntasPorCategoria;
    private final Random random;

    public ServicioPreguntasJuego() {
        ServicioDatos servicioDatos = new ServicioDatos();
        this.preguntasPorCategoria = servicioDatos.cargarPreguntasOriginales();
        this.random = new Random();
    }

    public PreguntaOriginal seleccionarPreguntaAleatoria(CategoriaTrivia categoria) {
        if (categoria == null) return null;
        List<PreguntaOriginal> lista = preguntasPorCategoria.get(categoria.getNombreMostrado());
        if (lista == null || lista.isEmpty()) {
            System.err.println("ADVERTENCIA: No hay preguntas para la categoría: " + categoria.getNombreMostrado());
            return null;
        }
        return lista.get(random.nextInt(lista.size()));
    }
}
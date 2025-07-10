package com.ucab.trivia.modelo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Proporciona acceso a las preguntas del juego.
 * Esta clase se encarga de cargar el banco de preguntas desde el {@link ServicioDatos}
 * al momento de su creación y de servir preguntas aleatorias de una categoría específica
 * cuando se le solicitan.
 */
public class ServicioPreguntasJuego {

    /**
     * Un mapa que almacena todas las preguntas del juego, organizadas por el nombre de su categoría.
     * La clave es un {@code String} con el nombre de la categoría y el valor es una lista de {@link PreguntaOriginal}.
     */
    private final Map<String, List<PreguntaOriginal>> preguntasPorCategoria;

    /** Instancia de {@link Random} para seleccionar preguntas de forma aleatoria. */
    private final Random random;

    /**
     * Constructor del servicio de preguntas.
     * Carga todas las preguntas desde la capa de datos a través de {@link ServicioDatos}
     * y las almacena en memoria para un acceso rápido durante la partida.
     */
    public ServicioPreguntasJuego() {
        ServicioDatos servicioDatos = new ServicioDatos();
        this.preguntasPorCategoria = servicioDatos.cargarPreguntasOriginales();
        this.random = new Random();
    }

    /**
     * Selecciona y devuelve una pregunta al azar de la categoría especificada.
     *
     * @param categoria La {@link CategoriaTrivia} de la cual se desea una pregunta.
     * @return Una {@link PreguntaOriginal} seleccionada aleatoriamente, o {@code null} si la categoría
     * es nula o si no se encuentran preguntas para esa categoría en el banco de datos.
     */
    public PreguntaOriginal seleccionarPreguntaAleatoria(CategoriaTrivia categoria) {
        if (categoria == null) {
            return null;
        }

        // Obtiene la lista de preguntas usando el nombre legible de la categoría
        List<PreguntaOriginal> lista = preguntasPorCategoria.get(categoria.getNombreMostrado());

        if (lista == null || lista.isEmpty()) {
            System.err.println("ADVERTENCIA: No hay preguntas para la categoría: " + categoria.getNombreMostrado());
            return null;
        }

        // Devuelve un elemento aleatorio de la lista
        return lista.get(random.nextInt(lista.size()));
    }
}
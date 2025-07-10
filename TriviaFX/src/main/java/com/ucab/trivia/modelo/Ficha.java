package com.ucab.trivia.modelo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.EnumMap;
import java.util.Map;

/**
 * Representa la ficha de un jugador, que almacena los "quesitos" o categorías que ha ganado.
 * El objetivo del juego es rellenar todos los espacios de la ficha.
 * Esta clase es serializable a JSON.

 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ficha {
    private Map<CategoriaTrivia, Boolean> categoriasObtenidas;

    /**
     * Constructor que inicializa una nueva ficha.
     * Todas las categorías se marcan inicialmente como no obtenidas (false).
     */
    public Ficha() {
        this.categoriasObtenidas = new EnumMap<>(CategoriaTrivia.class);
        for (CategoriaTrivia cat : CategoriaTrivia.values()) {
            this.categoriasObtenidas.put(cat, false);
        }
    }

    /**
     * Verifica si una categoría específica ya ha sido obtenida.
     * @param categoria La categoría a verificar.
     * @return true si la categoría ha sido obtenida, false en caso contrario.
     */
    public boolean haObtenidoCategoria(CategoriaTrivia categoria) {
        return categoria != null && this.categoriasObtenidas.getOrDefault(categoria, false);
    }

    /**
     * Marca una categoría como obtenida (true).
     * @param categoria La categoría a marcar como ganada.
     */
    public void marcarCategoriaObtenida(CategoriaTrivia categoria) {
        if (categoria != null) this.categoriasObtenidas.put(categoria, true);
    }

    /**
     * Verifica si todos los "quesitos" de la ficha han sido obtenidos.
     * @return true si la ficha está completa, false en caso contrario.
     */
    public boolean estaCompleta() {
        if (categoriasObtenidas == null || categoriasObtenidas.isEmpty()) return false;
        if (categoriasObtenidas.size() < CategoriaTrivia.values().length) return false;
        return this.categoriasObtenidas.values().stream().allMatch(obtenida -> obtenida);
    }

    /**
     * Cuenta cuántas categorías han sido obtenidas hasta el momento.
     * @return El número de categorías ganadas.
     */
    public int getCategoriasObtenidasCount() {
        if (categoriasObtenidas == null) return 0;
        return (int) categoriasObtenidas.values().stream().filter(b -> b).count();
    }

    /**
     * Obtiene el mapa que almacena el estado de las categorías.
     * Requerido para la serialización JSON con Jackson.
     * @return Un mapa de CategoriaTrivia a Boolean.
     */
    public Map<CategoriaTrivia, Boolean> getCategoriasObtenidas() { return categoriasObtenidas; }

    /**
     * Establece el mapa que almacena el estado de las categorías.
     * Requerido para la deserialización JSON con Jackson.
     * @param c El nuevo mapa de categorías obtenidas.
     */
    public void setCategoriasObtenidas(Map<CategoriaTrivia, Boolean> c) { this.categoriasObtenidas = c; }
}
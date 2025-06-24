package com.ucab.trivia.modelo;

import java.util.EnumMap;
import java.util.Map;

/**
 * Representa la ficha hexagonal de un jugador, que tiene espacios
 * para cada categoría del juego.
 */
public class Ficha {
    private Map<CategoriaTrivia, Boolean> categoriasObtenidas;

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
     * @param categoria La categoría a marcar.
     */
    public void marcarCategoriaObtenida(CategoriaTrivia categoria) {
        if (categoria != null) this.categoriasObtenidas.put(categoria, true);
    }

    /**
     * Verifica si todos los espacios de la ficha han sido obtenidos.
     * @return true si la ficha está completa, false en caso contrario.
     */
    public boolean estaCompleta() {
        if (categoriasObtenidas.isEmpty()) return false;
        return this.categoriasObtenidas.values().stream().allMatch(obtenida -> obtenida);
    }

    /**
     * Cuenta cuántas categorías han sido obtenidas.
     * @return El número de categorías obtenidas.
     */
    public int getCategoriasObtenidasCount() {
        return (int) categoriasObtenidas.values().stream().filter(b -> b).count();
    }

    // Getters y Setters para que Jackson pueda serializar/deserializar el estado.
    public Map<CategoriaTrivia, Boolean> getCategoriasObtenidas() { return categoriasObtenidas; }
    public void setCategoriasObtenidas(Map<CategoriaTrivia, Boolean> c) { this.categoriasObtenidas = c; }
}
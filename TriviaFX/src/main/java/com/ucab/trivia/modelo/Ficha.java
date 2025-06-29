package com.ucab.trivia.modelo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.EnumMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ficha {
    private Map<CategoriaTrivia, Boolean> categoriasObtenidas;

    public Ficha() {
        this.categoriasObtenidas = new EnumMap<>(CategoriaTrivia.class);
        for (CategoriaTrivia cat : CategoriaTrivia.values()) {
            this.categoriasObtenidas.put(cat, false);
        }
    }

    public boolean haObtenidoCategoria(CategoriaTrivia categoria) {
        return categoria != null && this.categoriasObtenidas.getOrDefault(categoria, false);
    }

    public void marcarCategoriaObtenida(CategoriaTrivia categoria) {
        if (categoria != null) this.categoriasObtenidas.put(categoria, true);
    }

    public boolean estaCompleta() {
        if (categoriasObtenidas == null || categoriasObtenidas.isEmpty()) return false;
        // El PDF original no especifica 6 categorías, pero el juego clásico sí. Asumimos 6.
        // Si el enum CategoriaTrivia tiene más o menos, esta lógica se adapta.
        if (categoriasObtenidas.size() < CategoriaTrivia.values().length) return false;
        return this.categoriasObtenidas.values().stream().allMatch(obtenida -> obtenida);
    }

    public int getCategoriasObtenidasCount() {
        if (categoriasObtenidas == null) return 0;
        return (int) categoriasObtenidas.values().stream().filter(b -> b).count();
    }

    // Getters y Setters para que Jackson pueda serializar/deserializar el estado.
    public Map<CategoriaTrivia, Boolean> getCategoriasObtenidas() { return categoriasObtenidas; }
    public void setCategoriasObtenidas(Map<CategoriaTrivia, Boolean> c) { this.categoriasObtenidas = c; }
}
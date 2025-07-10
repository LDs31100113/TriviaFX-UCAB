package com.ucab.trivia.modelo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

/**
 * Enumeración que representa las categorías de preguntas en el juego TRIVIA-UCAB.
 * Cada categoría tiene un nombre para mostrar y un color asociado en formato web hexadecimal,
 * utilizado para la representación gráfica en la interfaz.
 *
 * @author [Tu Nombre/Equipo]
 * @version 1.0
 */
public enum CategoriaTrivia {
    GEOGRAFIA("Geografía", "#0D6ABF"),
    HISTORIA("Historia", "#FFC300"),
    DEPORTES("Deportes", "#F57C00"),
    CIENCIA("Ciencia", "#009E73"),
    ARTE_LITERATURA("Arte y Literatura", "#CC79A7"),
    ENTRETENIMIENTO("Entretenimiento", "#9C27B0");

    private final String nombreMostrado;
    private final String colorWeb;

    /**
     * Constructor para cada constante del enum.
     * @param nombreMostrado El nombre legible de la categoría.
     * @param colorWeb El código de color hexadecimal para la UI.
     */
    CategoriaTrivia(String nombreMostrado, String colorWeb) {
        this.nombreMostrado = nombreMostrado;
        this.colorWeb = colorWeb;
    }

    /**
     * Obtiene el nombre legible de la categoría para mostrar en la interfaz.
     * La anotación @JsonValue indica a Jackson que use este método para serializar el enum a JSON.
     * @return El nombre de la categoría.
     */
    @JsonValue
    public String getNombreMostrado() {
        return nombreMostrado;
    }

    /**
     * Obtiene el código de color hexadecimal para la categoría.
     * @return Un String con el color en formato web (ej. "#FFC300").
     */
    public String getColorWeb() {
        return colorWeb;
    }

    /**
     * Permite crear una instancia del enum a partir de un String, ignorando mayúsculas/minúsculas.
     * La anotación @JsonCreator indica a Jackson que use este método para deserializar desde JSON.
     * @param nombre El nombre de la categoría a buscar.
     * @return La constante del enum correspondiente, o null si no se encuentra.
     */
    @JsonCreator
    public static CategoriaTrivia fromString(String nombre) {
        return Arrays.stream(values())
                .filter(cat -> cat.nombreMostrado.equalsIgnoreCase(nombre) || cat.name().equalsIgnoreCase(nombre))
                .findFirst().orElse(null);
    }

    /**
     * Devuelve la representación en String del objeto, que es su nombre legible.
     * @return El nombre mostrado de la categoría.
     */
    @Override
    public String toString() {
        return nombreMostrado;
    }
}
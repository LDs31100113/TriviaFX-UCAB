package com.ucab.trivia.modelo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum CategoriaTrivia {
    GEOGRAFIA("Geografía", "#0D6ABF"), // Azul
    HISTORIA("Historia", "#FFC300"), // Amarillo
    DEPORTES("Deportes", "#F57C00"), // Naranja
    CIENCIA("Ciencia", "#009E73"), // Verde
    ARTE_LITERATURA("Arte y Literatura", "#CC79A7"), // Fucsia
    ENTRETENIMIENTO("Entretenimiento", "#9C27B0"); // Púrpura

    private final String nombreMostrado;
    private final String colorWeb;

    CategoriaTrivia(String nombreMostrado, String colorWeb) {
        this.nombreMostrado = nombreMostrado;
        this.colorWeb = colorWeb;
    }

    @JsonValue public String getNombreMostrado() { return nombreMostrado; }
    public String getColorWeb() { return colorWeb; }

    @JsonCreator
    public static CategoriaTrivia fromString(String nombre) {
        return Arrays.stream(values())
                .filter(cat -> cat.nombreMostrado.equalsIgnoreCase(nombre) || cat.name().equalsIgnoreCase(nombre))
                .findFirst().orElse(null);
    }

    @Override
    public String toString() { return nombreMostrado; }
}
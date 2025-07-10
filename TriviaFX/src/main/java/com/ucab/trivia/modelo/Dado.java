package com.ucab.trivia.modelo;

import java.security.SecureRandom;

/**
 * Representa un dado de 6 caras utilizado en el juego.
 * Proporciona un método para obtener un resultado aleatorio en cada lanzamiento.
 */
public class Dado {

    private final SecureRandom random;

    /**
     * Constructor que inicializa el generador de números aleatorios de forma segura.
     * SecureRandom es una implementación más fuerte que Random, recomendada para este tipo de usos.
     */
    public Dado() {
        this.random = new SecureRandom();
    }

    /**
     * Simula el lanzamiento del dado, generando un resultado.
     * @return un número entero aleatorio entre 1 y 6 (ambos inclusive).
     */
    public int lanzar() {
        return random.nextInt(6) + 1;
    }
}
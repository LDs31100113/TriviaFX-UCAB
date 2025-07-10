package com.ucab.trivia.modelo;

import java.util.Objects;

/**
 * Representa el perfil de un usuario o jugador en el sistema.
 * Contiene información básica y persistente como el email y el alias,
 * que sirve como identificador único y nombre visible del jugador.
 */
public class PerfilJugador {

    /** El correo electrónico del jugador, utilizado como identificador único. */
    private String email;

    /** El alias o apodo visible del jugador. */
    private String alias;

    /**
     * Constructor por defecto.
     * Requerido para la correcta operación de librerías de (de)serialización como Jackson.
     */
    public PerfilJugador() {}

    /**
     * Obtiene el email del jugador.
     * @return El email del jugador.
     */
    public String getEmail() { return email; }

    /**
     * Establece el email del jugador.
     * @param email El nuevo email a establecer.
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Obtiene el alias del jugador.
     * @return El alias del jugador.
     */
    public String getAlias() { return alias; }

    /**
     * Establece el alias del jugador.
     * @param alias El nuevo alias a establecer.
     */
    public void setAlias(String alias) { this.alias = alias; }

    /**
     * Devuelve la representación en cadena del perfil, que es el alias del jugador.
     * Útil para mostrar el perfil en componentes de UI.
     * @return El alias del jugador.
     */
    @Override
    public String toString() { return alias; }

    /**
     * Compara este perfil con otro objeto para verificar si son iguales.
     * Dos perfiles se consideran iguales si sus direcciones de email son idénticas.
     *
     * @param o El objeto a comparar.
     * @return {@code true} si los objetos son el mismo o si tienen el mismo email,
     * {@code false} en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerfilJugador that = (PerfilJugador) o;
        return Objects.equals(email, that.email);
    }

    /**
     * Genera un código hash para el perfil.
     * El hash se basa únicamente en el email del jugador, para ser consistente
     * con el método {@link #equals(Object)}.
     *
     * @return El código hash basado en el email.
     */
    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
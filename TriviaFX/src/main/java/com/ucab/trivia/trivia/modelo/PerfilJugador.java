package com.ucab.trivia.modelo;

import java.util.Objects;

public class PerfilJugador {
    private String email;
    private String alias;

    public PerfilJugador() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }

    @Override
    public String toString() { return alias; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerfilJugador that = (PerfilJugador) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
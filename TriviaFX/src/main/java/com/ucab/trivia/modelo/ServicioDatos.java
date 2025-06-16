package com.ucab.trivia.modelo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServicioDatos {
    private static final String JUGADORES_FILE = "jugadores.json";
    private static final String ESTADISTICAS_FILE = "estadisticas_globales.json";
    private static final String PARTIDA_GUARDADA_FILE = "partida_guardada.json";
    private static final String PREGUNTAS_INICIALES_FILE = "preguntasJuegoTrivia.json";

    private final ObjectMapper objectMapper;

    public ServicioDatos() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.findAndRegisterModules();
    }

    public List<PerfilJugador> cargarPerfiles() {
        return leerArchivo(JUGADORES_FILE, new TypeReference<>() {});
    }

    public List<EstadisticaGlobal> cargarEstadisticas() {
        return leerArchivo(ESTADISTICAS_FILE, new TypeReference<>() {});
    }

    public void guardarEstadisticas(List<EstadisticaGlobal> estadisticas) {
        escribirArchivo(ESTADISTICAS_FILE, estadisticas);
    }

    public Map<String, List<PreguntaOriginal>> cargarPreguntasOriginales() {
        return leerArchivo(PREGUNTAS_INICIALES_FILE, new TypeReference<>() {});
    }

    public EstadoJuegoGuardado cargarPartidaGuardada() {
        return leerArchivo(PARTIDA_GUARDADA_FILE, new TypeReference<>() {});
    }

    public void guardarPartida(EstadoJuegoGuardado estado) {
        escribirArchivo(PARTIDA_GUARDADA_FILE, estado);
    }

    private <T> T leerArchivo(String nombreArchivo, TypeReference<T> typeRef) {
        try {
            File file = new File(nombreArchivo);
            if (file.exists() && file.length() > 0) {
                return objectMapper.readValue(file, typeRef);
            }
        } catch (IOException e) {
            System.err.println("Error leyendo el archivo " + nombreArchivo);
            e.printStackTrace();
        }
        // Devolver un objeto vacío apropiado si el archivo no existe o hay error
        if (typeRef.getType().getTypeName().contains("List")) {
            return (T) new ArrayList<>();
        }
        if (typeRef.getType().getTypeName().contains("Map")) {
            return (T) new java.util.HashMap<>();
        }
        return null;
    }

    private <T> void escribirArchivo(String nombreArchivo, T datos) {
        try {
            objectMapper.writeValue(new File(nombreArchivo), datos);
        } catch (IOException e) {
            System.err.println("Error escribiendo en el archivo " + nombreArchivo);
            e.printStackTrace();
        }
    }
}
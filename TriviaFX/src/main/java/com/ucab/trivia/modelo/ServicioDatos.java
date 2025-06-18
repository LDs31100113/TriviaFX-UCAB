package com.ucab.trivia.modelo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
        return leerArchivo(JUGADORES_FILE, new TypeReference<>() {}, new ArrayList<>());
    }

    public List<EstadisticaGlobal> cargarEstadisticas() {
        return leerArchivo(ESTADISTICAS_FILE, new TypeReference<>() {}, new ArrayList<>());
    }

    public void guardarEstadisticas(List<EstadisticaGlobal> estadisticas) {
        escribirArchivo(ESTADISTICAS_FILE, estadisticas);
    }

    public Map<String, List<PreguntaOriginal>> cargarPreguntasOriginales() {
        return leerArchivo(PREGUNTAS_INICIALES_FILE, new TypeReference<>() {}, new HashMap<>());
    }

    public EstadoJuegoGuardado cargarPartidaGuardada() {
        return leerArchivo(PARTIDA_GUARDADA_FILE, new TypeReference<>() {}, null);
    }

    public void guardarPartida(EstadoJuegoGuardado estado) {
        escribirArchivo(PARTIDA_GUARDADA_FILE, estado);
    }

    /**
     * **MÉTODO AÑADIDO PARA CORREGIR EL ERROR**
     * Verifica si existe un archivo de partida guardada válido.
     * @return true si el archivo existe y no está vacío, false en caso contrario.
     */
    public boolean existePartidaGuardada() {
        File archivo = new File(PARTIDA_GUARDADA_FILE);
        return archivo.exists() && archivo.length() > 0;
    }

    private <T> T leerArchivo(String nombreArchivo, TypeReference<T> typeRef, T valorPorDefecto) {
        try {
            File file = new File(nombreArchivo);
            if (file.exists() && file.length() > 0) {
                return objectMapper.readValue(file, typeRef);
            }
        } catch (IOException e) {
            System.err.println("Error leyendo el archivo " + nombreArchivo);
            e.printStackTrace();
        }
        return valorPorDefecto;
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
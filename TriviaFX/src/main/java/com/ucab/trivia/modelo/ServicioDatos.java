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

/**
 * Gestiona la persistencia de los datos del juego.
 * Esta clase se encarga de leer y escribir la información del juego, como perfiles,
 * estadísticas, preguntas y partidas guardadas, en archivos JSON.
 * Utiliza la librería Jackson para la serialización y deserialización de objetos.
 */
public class ServicioDatos {
    /** Nombre del archivo que almacena los perfiles de los jugadores. */
    private static final String JUGADORES_FILE = "jugadores.json";
    /** Nombre del archivo que almacena las estadísticas globales de los jugadores. */
    private static final String ESTADISTICAS_FILE = "estadisticas_globales.json";
    /** Nombre del archivo que almacena el estado de una partida guardada. */
    private static final String PARTIDA_GUARDADA_FILE = "partida_guardada.json";
    /** Nombre del archivo que contiene el banco inicial de preguntas. */
    private static final String PREGUNTAS_INICIALES_FILE = "preguntasJuegoTrivia.json";

    /** Objeto de Jackson para mapear entre objetos Java y JSON. */
    private final ObjectMapper objectMapper;

    /**
     * Constructor del servicio de datos.
     * Inicializa y configura el {@link ObjectMapper} para que el JSON de salida
     * esté formateado con indentación y para que registre módulos automáticamente.
     */
    public ServicioDatos() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.findAndRegisterModules(); // Para soportar tipos de Java 8 como LocalDate, etc.
    }

    /**
     * Carga la lista de perfiles de jugador desde el archivo correspondiente.
     * @return Una lista de {@link PerfilJugador}. Si el archivo no existe o está vacío, devuelve una lista vacía.
     */
    public List<PerfilJugador> cargarPerfiles() {
        return leerArchivo(JUGADORES_FILE, new TypeReference<>() {}, new ArrayList<>());
    }

    /**
     * Carga la lista de estadísticas globales desde el archivo correspondiente.
     * @return Una lista de {@link EstadisticaGlobal}. Si el archivo no existe o está vacío, devuelve una lista vacía.
     */
    public List<EstadisticaGlobal> cargarEstadisticas() {
        return leerArchivo(ESTADISTICAS_FILE, new TypeReference<>() {}, new ArrayList<>());
    }

    /**
     * Guarda la lista de estadísticas globales en su archivo correspondiente.
     * @param estadisticas La lista de {@link EstadisticaGlobal} a guardar.
     */
    public void guardarEstadisticas(List<EstadisticaGlobal> estadisticas) {
        escribirArchivo(ESTADISTICAS_FILE, estadisticas);
    }

    /**
     * Carga el banco de preguntas originales desde el archivo JSON.
     * @return Un mapa donde la clave es el nombre de la categoría y el valor es una lista de {@link PreguntaOriginal}.
     */
    public Map<String, List<PreguntaOriginal>> cargarPreguntasOriginales() {
        return leerArchivo(PREGUNTAS_INICIALES_FILE, new TypeReference<>() {}, new HashMap<>());
    }

    /**
     * Carga el estado de una partida guardada desde el archivo.
     * @return Un objeto {@link EstadoJuegoGuardado} o {@code null} si no hay partida guardada.
     */
    public EstadoJuegoGuardado cargarPartidaGuardada() {
        return leerArchivo(PARTIDA_GUARDADA_FILE, new TypeReference<>() {}, null);
    }

    /**
     * Guarda el estado actual del juego en un archivo.
     * @param estado El objeto {@link EstadoJuegoGuardado} que representa la partida actual.
     */
    public void guardarPartida(EstadoJuegoGuardado estado) {
        escribirArchivo(PARTIDA_GUARDADA_FILE, estado);
    }

    /**
     * Verifica si existe un archivo de partida guardada válido.
     * @return {@code true} si el archivo existe y no está vacío, {@code false} en caso contrario.
     */
    public boolean existePartidaGuardada() {
        File archivo = new File(PARTIDA_GUARDADA_FILE);
        return archivo.exists() && archivo.length() > 0;
    }

    /**
     * Método genérico para leer y deserializar un archivo JSON a un objeto Java.
     *
     * @param nombreArchivo El nombre del archivo a leer.
     * @param typeRef El tipo de referencia del objeto a deserializar, para manejar tipos genéricos.
     * @param valorPorDefecto El valor a devolver si el archivo no existe o hay un error.
     * @param <T> El tipo genérico del objeto a devolver.
     * @return El objeto deserializado o el valor por defecto.
     */
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

    /**
     * Método genérico para serializar un objeto Java y escribirlo en un archivo JSON.
     *
     * @param nombreArchivo El nombre del archivo donde se guardarán los datos.
     * @param datos El objeto a serializar y guardar.
     * @param <T> El tipo genérico del objeto a escribir.
     */
    private <T> void escribirArchivo(String nombreArchivo, T datos) {
        try {
            objectMapper.writeValue(new File(nombreArchivo), datos);
        } catch (IOException e) {
            System.err.println("Error escribiendo en el archivo " + nombreArchivo);
            e.printStackTrace();
        }
    }
}
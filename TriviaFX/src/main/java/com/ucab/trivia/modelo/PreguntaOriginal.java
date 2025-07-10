package com.ucab.trivia.modelo;

/**
 * Representa una pregunta básica de trivia con su respuesta correcta.
 * Esta clase sirve como una estructura de datos simple (POJO) para almacenar
 * el enunciado de una pregunta y su única respuesta válida.
 */
public class PreguntaOriginal {

    /** El texto completo del enunciado de la pregunta. */
    private String pregunta;

    /** El texto de la respuesta correcta para la pregunta. */
    private String respuesta;

    /**
     * Constructor por defecto.
     * Es necesario para que los frameworks de (de)serialización, como Jackson,
     * puedan instanciar el objeto.
     */
    public PreguntaOriginal() {}

    /**
     * Obtiene el texto de la pregunta.
     * @return El enunciado de la pregunta.
     */
    public String getPregunta() { return pregunta; }

    /**
     * Establece el texto de la pregunta.
     * @param pregunta El nuevo enunciado para la pregunta.
     */
    public void setPregunta(String pregunta) { this.pregunta = pregunta; }

    /**
     * Obtiene el texto de la respuesta correcta.
     * @return La respuesta correcta.
     */
    public String getRespuesta() { return respuesta; }

    /**
     * Establece el texto de la respuesta correcta.
     * @param respuesta La nueva respuesta correcta.
     */
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }
}
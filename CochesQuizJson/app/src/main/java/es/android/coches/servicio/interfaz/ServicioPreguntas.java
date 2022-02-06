package es.android.coches.servicio.interfaz;

import android.content.Context;

import java.util.List;

import es.android.coches.entidad.Pregunta;

public interface ServicioPreguntas {
    List<String> generarRespuestasPosibles(String respuestaCorrecta, int numRespuestas);

    List<Pregunta> generarPreguntas(String recurso) throws Exception;
}

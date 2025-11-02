/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadematriculaycalificaciones;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ronni
 */
public class FalsoVerdadero {
    public String descripcion;
    public int puntos;
    public List<String> opciones;
    public String respuesta;
    
    public FalsoVerdadero(String descripcion, int puntos, List<String> opciones,
                 String respuesta){
        this.descripcion = descripcion;
        this.puntos = puntos;
        this.opciones = opciones != null ? new ArrayList<>(opciones) : new ArrayList<>();
        this.respuesta = respuesta;  
    }
    //Setters y getters.
    // Descripción
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Puntos
    public int getPuntos() {
        return puntos;
    }
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    // Opciones
    public List<String> getOpciones() {
        return opciones;
    }
    public void setOpciones(List<String> opciones) {
        this.opciones = opciones != null ? new ArrayList<>(opciones) : new ArrayList<>();
    }

    // Respuesta
    public String getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
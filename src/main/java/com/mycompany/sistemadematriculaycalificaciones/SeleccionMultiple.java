/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadematriculaycalificaciones;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
/**
 *
 * @author ronni
 */
public class SeleccionMultiple implements Serializable {
    public String descripcion;
    public int puntos;
    public List<String> opciones;
    public List<String> respuestas;
    
    
    
    private static final long serialVersionUID = 1L;
    
    
    public SeleccionMultiple(){};
    
    public SeleccionMultiple(String descripcion, int puntos, List<String> opciones,
                 List<String> respuesta){
        this.descripcion = descripcion;
        this.puntos = puntos;
        this.opciones = opciones != null ? new ArrayList<>(opciones) : new ArrayList<>();
        this.respuestas = respuestas != null ? new ArrayList<>(respuestas) : new ArrayList<>();
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
    public List<String> getRespuesta() {
        return respuestas;
    }
    public void setRespuesta(List<String> respuesta) {
        this.respuestas = respuestas != null ? new ArrayList<>(respuestas) : new ArrayList<>();
    }
    
}

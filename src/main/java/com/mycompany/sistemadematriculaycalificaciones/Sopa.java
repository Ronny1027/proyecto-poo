/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadematriculaycalificaciones;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
/**
 *
 * @author ronni
 */
public class Sopa implements Serializable  {
    public String descripcion;
    public int puntos;
    
    private static final long serialVersionUID = 1L;
    
    public Sopa(String descripcion, int puntos){
        this.descripcion = descripcion;
        this.puntos = puntos;
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
    
}
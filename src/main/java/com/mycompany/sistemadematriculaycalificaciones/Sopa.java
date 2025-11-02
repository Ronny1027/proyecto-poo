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
public class Sopa {
    public String descripcion;
    public int puntos;
    
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

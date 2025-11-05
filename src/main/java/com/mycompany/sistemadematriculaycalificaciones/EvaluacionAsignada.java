/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadematriculaycalificaciones;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase que representa una evaluación asignada a un grupo específico
 * @author ronni
 */
public class EvaluacionAsignada implements Serializable {
    private Evaluaciones evaluacion;
    private Grupos grupo;
    private Date fechaAsignacion;
    private Date fechaInicio; // Fecha y hora en que inicia la evaluación
    private Date fechaFin; // Fecha y hora en que termina la evaluación
    private boolean activa;

    private static final long serialVersionUID = 1L;

    // Constructor vacío
    public EvaluacionAsignada() {
        this.fechaAsignacion = new Date();
        this.activa = true;
    }

    // Constructor con parámetros
    public EvaluacionAsignada(Evaluaciones evaluacion, Grupos grupo, Date fechaInicio, Date fechaFin) {
        this();
        this.evaluacion = evaluacion;
        this.grupo = grupo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Getters y Setters
    public Evaluaciones getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Evaluaciones evaluacion) {
        this.evaluacion = evaluacion;
    }

    public Grupos getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupos grupo) {
        this.grupo = grupo;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    @Override
    public String toString() {
        return "Evaluación: " + evaluacion.getNombre() +
               " - Grupo: " + grupo.getIdentificacionGrupo() +
               " - Inicia: " + (fechaInicio != null ? fechaInicio.toString() : "Sin fecha") +
               " - Termina: " + (fechaFin != null ? fechaFin.toString() : "Sin fecha");
    }

    // Método para verificar si la evaluación está disponible para realizar hoy
    public boolean estaDisponibleHoy() {
        Date ahora = new Date();
        if (fechaInicio == null || fechaFin == null) {
            return false;
        }
        return ahora.after(fechaInicio) && ahora.before(fechaFin);
    }
}

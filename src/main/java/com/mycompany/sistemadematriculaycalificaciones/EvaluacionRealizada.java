/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadematriculaycalificaciones;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Clase que representa una evaluación realizada por un estudiante
 * @author ronni
 */
public class EvaluacionRealizada implements Serializable {
    private Estudiantes estudiante;
    private EvaluacionAsignada evaluacionAsignada;
    private Date fechaHoraInicio;
    private Date fechaHoraFin;
    private Map<Integer, Object> respuestas; // Número de pregunta -> respuesta del estudiante
    private List<Object> ordenPreguntas; // Orden exacto en que se presentaron las preguntas
    private double calificacionObtenida;
    private boolean finalizada;

    private static final long serialVersionUID = 1L;

    // Constructor vacío
    public EvaluacionRealizada() {
        this.respuestas = new HashMap<>();
        this.ordenPreguntas = new ArrayList<>();
        this.finalizada = false;
    }

    // Constructor con parámetros
    public EvaluacionRealizada(Estudiantes estudiante, EvaluacionAsignada evaluacionAsignada) {
        this();
        this.estudiante = estudiante;
        this.evaluacionAsignada = evaluacionAsignada;
        this.fechaHoraInicio = new Date();
    }

    // Getters y Setters
    public Estudiantes getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiantes estudiante) {
        this.estudiante = estudiante;
    }

    public EvaluacionAsignada getEvaluacionAsignada() {
        return evaluacionAsignada;
    }

    public void setEvaluacionAsignada(EvaluacionAsignada evaluacionAsignada) {
        this.evaluacionAsignada = evaluacionAsignada;
    }

    public Date getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(Date fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public Date getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(Date fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public Map<Integer, Object> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(Map<Integer, Object> respuestas) {
        this.respuestas = respuestas;
    }

    public List<Object> getOrdenPreguntas() {
        return ordenPreguntas;
    }

    public void setOrdenPreguntas(List<Object> ordenPreguntas) {
        this.ordenPreguntas = ordenPreguntas;
    }

    public double getCalificacionObtenida() {
        return calificacionObtenida;
    }

    public void setCalificacionObtenida(double calificacionObtenida) {
        this.calificacionObtenida = calificacionObtenida;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }

    // Método para guardar una respuesta
    public void guardarRespuesta(int numeroPregunta, Object respuesta) {
        this.respuestas.put(numeroPregunta, respuesta);
    }

    // Método para finalizar la evaluación
    public void finalizar() {
        this.fechaHoraFin = new Date();
        this.finalizada = true;
    }

    @Override
    public String toString() {
        return "Evaluación realizada por: " + estudiante.getNombre() + " " + estudiante.getApellido1() +
               "\nEvaluación: " + evaluacionAsignada.getEvaluacion().getNombre() +
               "\nInicio: " + fechaHoraInicio +
               "\nFin: " + (fechaHoraFin != null ? fechaHoraFin.toString() : "No finalizada") +
               "\nCalificación: " + calificacionObtenida;
    }
}

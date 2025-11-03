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
public class Evaluaciones implements Serializable{
    public int identificacion;
    public String nombre;
    public String instrucGenerales;
    public  List<String> objEva;
    public int duracion;
    public String pregunRandom;
    public String respueRandom;
    public List<SeleccionUnica> seleccionesUnicas;
    public List<SeleccionMultiple> seleccionesMultiples;
    public List<FalsoVerdadero> falsoVerdaderos;
    public List<Pareo> pareos;
    public List<Sopa> sopas;
            
    
    private static final long serialVersionUID = 1L;

    
    //Constructor vacio que se necesita
    public Evaluaciones() {
    this.objEva = new ArrayList<>();
    this.seleccionesUnicas = new ArrayList<>();
    this.seleccionesMultiples = new ArrayList<>();
    this.falsoVerdaderos = new ArrayList<>();
    this.pareos = new ArrayList<>();
    this.sopas = new ArrayList<>();
    }
    public Evaluaciones(int identificacion, String nombre, String instrucGenerales,
                 List<String> objEva,int duracion, String pregunRandom, String respuesRandom, 
                 List<SeleccionUnica> seleccionUni, List<SeleccionMultiple> seleccionMulti,
                 List<FalsoVerdadero> falsoVerdadero, List<Pareo> pareo, List<Sopa> sopa){
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.instrucGenerales = instrucGenerales;
        this.objEva = objEva != null ? new ArrayList<>(objEva) : new ArrayList<>();
        this.duracion = duracion;
        this.pregunRandom = pregunRandom;
        this.respueRandom = respuesRandom;
        this.seleccionesUnicas = seleccionUni != null ? new ArrayList<>(seleccionUni) : new ArrayList<>();
        this.seleccionesMultiples = seleccionMulti != null ? new ArrayList<>(seleccionMulti) : new ArrayList<>();
        this.falsoVerdaderos = falsoVerdadero != null ? new ArrayList<>(falsoVerdadero) : new ArrayList<>();
        this.pareos = pareo != null ? new ArrayList<>(pareo) : new ArrayList<>();
        this.sopas = sopa != null ? new ArrayList<>(sopa) : new ArrayList<>();
    }
    //Getters y setters de los atributos
    // Identificación
    public int getIdentificacion() {
        return identificacion;
    }
    public void setIdentificacion(int identificacion) {
        this.identificacion = identificacion;
    }

    // Nombre
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Instrucciones generales
    public String getInstrucGenerales() {
        return instrucGenerales;
    }
    public void setInstrucGenerales(String instrucGenerales) {
        this.instrucGenerales = instrucGenerales;
    }

    // Objetivos de evaluación 
    public List<String> getObjEva() {
        return objEva;
    }

    // Objetivo de evaluación 
    public void setObjEva(List<String> objEva) {
        this.objEva = objEva != null ? new ArrayList<>(objEva) : new ArrayList<>();
    }

    // Método para agregar un objetivos
    public void agregarObjetivo(String objetivo) {
        if (objetivo != null && !objetivo.trim().isEmpty()) {
            this.objEva.add(objetivo.trim());
        }
    }
    //Duración
    public int getDuracion() {
        return duracion;
    }
    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
    // Preguntas en orden aleatorio
    public String getPregunRandom() {
        return pregunRandom;
    }
    public void setPregunRandom(String pregunRandom) {
        this.pregunRandom = pregunRandom;
    }

    // Respuestas random
    public String getRespueRandom() {
        return respueRandom;
    }
    public void setRespueRandom(String respueRandom) {
        this.respueRandom = respueRandom;
    }

    public void agregarSeleccionUnica(SeleccionUnica pregunta) {
        if (pregunta != null) {
            this.seleccionesUnicas.add(pregunta);
        }
    }
    
    public void agregarSeleccionMultiple(SeleccionMultiple pregunta) {
        if (pregunta != null) {
            this.seleccionesMultiples.add(pregunta);
        }
    }
    
    public void agregarFalsoVerdadero(FalsoVerdadero pregunta) {
        if (pregunta != null) {
            this.falsoVerdaderos.add(pregunta);
        }
    }
    
    public void agregarPareo(Pareo pregunta) {
        if (pregunta != null) {
            this.pareos.add(pregunta);
        }
    }
    
    public void agregarSopa(Sopa pregunta) {
        if (pregunta != null) {
            this.sopas.add(pregunta);
        }
    }
    public List<SeleccionUnica> getSeleccionesUnicas() {
    return seleccionesUnicas;
}

    public List<SeleccionMultiple> getSeleccionesMultiples() {
        return seleccionesMultiples;
    }

    public List<FalsoVerdadero> getFalsoVerdaderos() {
        return falsoVerdaderos;
    }

    public List<Pareo> getPareos() {
        return pareos;
    }

    public List<Sopa> getSopas() {
        return sopas;
    }
    // Método para obtener todas las preguntas (para mostrar en lista)
    public List<Object> getTodasLasPreguntas() {
        List<Object> todas = new ArrayList<>();
        todas.addAll(seleccionesUnicas);
        todas.addAll(seleccionesMultiples);
        todas.addAll(falsoVerdaderos);
        todas.addAll(pareos);
        todas.addAll(sopas);
        return todas;
    }

    //Validaciones de los atributos
    // Validación de identificación 
    public String validarIdentificacion(List<Evaluaciones> todasEvaluaciones) {
        if (identificacion <= 0) {
            return "La identificación debe ser mayor a 0";
        }
        return null;
    }

    // Validación del nombre
    public String validarNombre() {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Por favor digite el nombre de la evaluacion";
        }
        if (nombre.length() < 5 || nombre.length() > 20) {
            return "El nombre debe tener entre 5 y 20 caracteres";
        }
        return null;
    }

    // Validación de instrucciones generales
    public String validarInstruccionesGenerales() {
        if (instrucGenerales == null || instrucGenerales.trim().isEmpty()) {
            return "Por favor digite las instrucciones generales";
        }
        if (instrucGenerales.length() < 5 || instrucGenerales.length() > 400) {
            return "Las instrucciones generales deben tener entre 5 y 400 caracteres";
        }
        return null;
    }

    // Validación de objetivos de evaluación
    public String validarObjetivosEvaluacion() {
        if (objEva == null || objEva.isEmpty()) {
            return "Por favor digite al menos un objetivo de evaluación";
        }
        for (String objetivo : objEva) {
            if (objetivo == null || objetivo.trim().isEmpty()) {
                return "Los objetivos no pueden estar vacíos";
            }
            if (objetivo.length() < 10 || objetivo.length() > 40) {
                return "Cada objetivo debe tener entre 10 y 40 caracteres";
            }
        }
        return null;
    }

    // Validación de duración
    public String validarDuracion() {
        if (duracion < 1) {
            return "La duración debe ser de al menos 1 minuto";
        }
        return null;
    }

    // Validación completa
    public List<String> validarEvaluacionCompleta(List<Evaluaciones> todasEvaluaciones) {
        List<String> errores = new ArrayList<>();

        agregarError(errores, validarIdentificacion(todasEvaluaciones));
        agregarError(errores, validarNombre());
        agregarError(errores, validarInstruccionesGenerales());
        agregarError(errores, validarObjetivosEvaluacion());
        agregarError(errores, validarDuracion());

        return errores;
    }

    private void agregarError(List<String> errores, String error) {
        if (error != null) {
            errores.add(error);
        }
    }
            
}
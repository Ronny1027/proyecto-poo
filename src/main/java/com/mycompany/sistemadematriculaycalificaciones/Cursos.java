/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadematriculaycalificaciones;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *
 * @author ronni
 */
public class Cursos {
    private String identificacion;
    private String nombre;
    private String descripcion;
    private int horasDia;
    private String modalidad;
    private int minEstu;
    private int maxEstu;
    private String tipo;
    private int calificacionMinima;
    
    public Cursos(){};
    
    public Cursos(String identificacion, String nombre, String descripcion, 
                 int horasDia, String modalidad, int minEstu, 
                 int maxEstu, String tipo, int calificacionMinima){
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.horasDia = horasDia;
        this.modalidad = modalidad;
        this.minEstu = minEstu;
        this.maxEstu = maxEstu;
        this.tipo = tipo;
        this.calificacionMinima = calificacionMinima;
    }
    // Getters y Setters
    //Identificación
    public String getIdentificacion() {
        return identificacion; }
    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion; }
    //Nombre
    public String getNombre() { 
        return nombre; 
    }
    public void setNombre(String nombre) {
        this.nombre = nombre; 
    }
    //Descripcion
    public String getDescripcion() {
        return descripcion; 
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion; 
    }
    //Horas por dia
    public int getHorasPorDia() {
        return horasDia; 
    }
    public void setHorasPorDia(int horasPorDia) { 
        this.horasDia = horasDia; }
    //Modalidad
    public String getModalidad() {
        return modalidad; 
    }
    public void setModalidad(String modalidad) { 
        this.modalidad = modalidad; 
    }
    //Minimo de estudiantes
    public int getMinEstudiantes() {
        return minEstu; 
    }
    public void setMinEstudiantes(int minEstudiantes) {
        this.minEstu = minEstu; 
    }
    //Maximo de estudiantes
    public int getMaxEstudiantes() {
        return maxEstu; 
    }
    public void setMaxEstudiantes(int maxEstudiantes) {
        this.maxEstu = maxEstu; 
    }
    //Tipo de Curso
    public String getTipo() {
        return tipo; 
    }
    public void setTipo(String tipo) {
        this.tipo = tipo; 
    }
    //Calificación Minima
    public int getCalificacionMinima() {
        return calificacionMinima; 
    }
    public void setCalificacionMinima(int calificacionMinima) {
        this.calificacionMinima = calificacionMinima; 
    }
    //Validación de los datos.
    //Validación de la identificación.
    public String validarIdentificacion() {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            return "Por favor digite la identificación del curso";
        }
        if (identificacion.length() != 6) {
            return "La identificación debe tener exactamente 6 caracteres";
        }
        return null;
    }
    //Validación del nombre.
    public String validarNombre() {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Por favor digite el nombre del curso";
        }
        if (nombre.length() < 5 || nombre.length() > 40) {
            return "El nombre debe tener entre 5 y 40 caracteres";
        }
        return null;
    }
    //Validación de la descripción
    public String validarDescripcion() {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return "Por favor digite la descripción";
        }
        if (descripcion.length() < 5 || descripcion.length() > 400) {
            return "La descripción debe tener entre 5 y 400 caracteres";
        }
        return null;
    }
    //Validación de las horas por dia
    public String validarHorasPorDia() {
        if (horasDia < 1 || horasDia > 8) {
            return "Las horas por día deben estar entre 1 y 8";
        }
        return null;
    }
    //Validación de la modalidad
    public String validarModalidad() {
        if (modalidad == null || modalidad.trim().isEmpty()) {
            return "La modalidad es requerida";
        }
        return null;
    }
    //Validación del minimo de Estudiantes
    public String validarMinEstudiantes() {
        if (minEstu < 1 || minEstu > 5) {
            return "La cantidad mínima de estudiantes debe estar entre 1 y 5";
        }
        return null;
    }
    //Validación del maximo de estudiantes
    public String validarMaxEstudiantes() {
        if (maxEstu < minEstu) {
            return "La cantidad máxima de estudiantes no puede ser "
                    + "menor que la mínima de estudiantes";
        }
        if (maxEstu > 20) {
            return "La cantidad máxima no puede exceder 20 estudiantes";
        }
        return null;
    }
    //Validación del tipo de curso(si selecciono algo)
    public String validarTipoCurso() {
        if (tipo == null || tipo.trim().isEmpty()) {
            return "El tipo de curso es requerido";
        }
        
        return null;
    }
    //Validación de la calificación minima.
    public String validarCalificacionMinima() {
        if (calificacionMinima < 0 || calificacionMinima > 100) {
            return "La calificación mínima debe estar entre 0 y 100";
        }
        return null;
    }

    // Validación completa del curso
    public List<String> validarCursoCompleto() {
        List<String> errores = new ArrayList<>();
        
        agregarError(errores, validarIdentificacion());
        agregarError(errores, validarNombre());
        agregarError(errores, validarDescripcion());
        agregarError(errores, validarHorasPorDia());
        agregarError(errores, validarModalidad());
        agregarError(errores, validarMinEstudiantes());
        agregarError(errores, validarMaxEstudiantes());
        agregarError(errores, validarTipoCurso());
        agregarError(errores, validarCalificacionMinima());
        
        return errores;
    }

    private void agregarError(List<String> errores, String error) {
        if (error != null) {
            errores.add(error);
        }
    }

    

    }
    
    


    
    
  
   
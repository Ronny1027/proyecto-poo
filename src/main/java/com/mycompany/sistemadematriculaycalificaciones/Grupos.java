/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadematriculaycalificaciones;

/**
 *
 * @author ronni
 */
import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
public class Grupos implements Serializable {
    private int identificacionGrupo;
    private Date fechaInicio;
    private Date fechaFin;
    private Cursos curso;
    private Profesores profesor;
    private List<Estudiantes> estudiantes;
    
   public Grupos (int identificacionGrupo, Date fechaInicio, 
           Date fechaFin, Cursos curso){
       this.identificacionGrupo = identificacionGrupo;
       this.fechaInicio = fechaInicio;
       this.fechaFin = fechaFin;
       this.curso = curso;
       this.estudiantes = new ArrayList<>();
   }
   private static final long serialVersionUID = 1L;
   //getter y setter necesario
   public int getIdentificacionGrupo() {
       return identificacionGrupo;
   }
    public void setIdentificacionGrupo(int identificacionGrupo) {
        this.identificacionGrupo = identificacionGrupo;
    }
    public Cursos getCurso() {
        return curso;
    }
    public List<Estudiantes> getEstudiantes() {
        return estudiantes;
    }
   //Metodos de validacion.
   // Validar la identificacion.
    public String validarIdentificacionGrupo() {
        if (identificacionGrupo < 1) {
            return "Por favor digite una identificación mayor o igual a 1";
        }
        return null;
    }
    //Validar las fechas.
    public String validarFechas() {
        if (fechaInicio == null) {
            return "La fecha de inicio es requerida";
        }
        if (fechaFin == null) {
            return "La fecha de finalización es requerida";
        }
        if (fechaFin.before(fechaInicio)) {
            return "La fecha de finalización no puede ser anterior a la fecha de inicio";
        }
        return null;
    }
    //Validación de todo el grupo.
    public List<String> validarGrupoCompleto() {
        List<String> errores = new ArrayList<>();
        agregarError(errores, validarIdentificacionGrupo());
        agregarError(errores, validarFechas());
        return errores;
    }
    //Se agrega el error a la lista.
    private void agregarError(List<String> errores, String error) {
        if (error != null) {
            errores.add(error);
        }
    }
    //Metodos para poder modificar la lista de estudiantes.
    public void agregarEstudiante(Estudiantes estudiante) {
        if (estudiante != null && !estudiantes.contains(estudiante)) {
            this.estudiantes.add(estudiante);
        }
    }
    
    public void eliminarEstudiante(Estudiantes estudiante) {
        this.estudiantes.remove(estudiante);
    }
    
    public String asignarProfesor(Profesores profesor) {
        // Validar que el profesor no sea nulo
        if (profesor == null) {
            return "El profesor no puede ser nulo";
        }
        
        // Validar que el grupo no tenga ya un profesor asignado
        if (this.profesor != null) {
            return "Este grupo ya tiene un profesor asignado: " + this.profesor.getNombre();
        }
 
        // Asignar el profesor al grupo
        this.profesor = profesor;
        
        // También agregar este grupo a la lista del profesor
        profesor.agregarGrupo(this);
        
        return null; // Éxito - no hay error
    }
    
    
    
}

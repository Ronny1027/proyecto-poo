/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadematriculaycalificaciones;
import java.util.Date;//para poder incluir la fecha
import java.util.List;
import java.util.ArrayList;//modulos de manejo de fechas
import java.util.regex.Pattern;
/**
 *
 * @author ronni
 */
public class Estudiantes {
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String identificacion;
    private String telefono;
    private String correo;
    private String direccion;
    private String organizacion;
    private List<String> temasInteres; 
    private String contraseña;
    private Date fechaRegistro;
    
    //Metodo constructor
    public Estudiantes() {
    }
    public Estudiantes(String nombre, String apellido1, String apellido2, 
                      String identificacion, String telefono, String correo, 
                      String direccion, String organizacion, String contraseña) {
        this();
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.identificacion = identificacion;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.organizacion = organizacion;
        this.contraseña = contraseña;
        this.temasInteres = new ArrayList<>();
        this.fechaRegistro = new Date(); 
    }
    //Setters y getters
    //Nombre
    public String getNombre() { 
        return nombre; 
    }
    public void setNombre(String nombre) {
        this.nombre = nombre; 
    }
    //Apellido1
    public String getApellido1() {
        return apellido1; 
    }
    public void setApellido1(String apellido1) { 
        this.apellido1 = apellido1; 
    }
    //Apellido2
    public String getApellido2() { 
        return apellido2; 
    }
    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2; 
    }
    //Identificación
    public String getIdentificacion() 
    { return identificacion; 
    }
    public void setIdentificacion(String identificacion) { 
        this.identificacion = identificacion; 
    }
    //Telefono
    public String getTelefono() {
        return telefono; 
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono; 
    }
    //Correo
    public String getCorreo() {
        return correo; 
    }
    public void setCorreo(String correo) 
    { this.correo = correo; 
    }
    //Dirección
    public String getDireccion() 
    { return direccion; 
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion; 
    }
    //Organización
    public String getOrganizacion() {
        return organizacion; 
    }
    public void setOrganizacion(String organizacion) {
        this.organizacion = organizacion; 
    }
    //Temas de interes
    public List<String> getTemasInteres() 
    { return temasInteres; 
    }
    public void setTemasInteres(List<String> temasInteres) {
        this.temasInteres = temasInteres; 
    }
    //Contraseña
    public String getContrasena() {
        return contraseña; 
    }
    public void setContrasena(String contraseña) {
        this.contraseña = contraseña; 
    }
    //Fecha de registro
    public Date getFechaRegistro() 
    { return fechaRegistro; 
    }
    public void agregarTemaInteres(String tema) {
        if (tema != null && tema.length() >= 5 && tema.length() <= 30) {
            this.temasInteres.add(tema);
        }
    }
    
    public void eliminarTemaInteres(String tema) {
        this.temasInteres.remove(tema);
    }
    
    public void limpiarTemasInteres() {
        this.temasInteres.clear();
    }
    //Validación de los datos
    //Validación del nombre:
    public String validarNombre() {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Por favor digite el nombre";
        }
        if (nombre.length() < 2 || nombre.length() > 20) {
            return "El nombre debe tener entre 2 y 20 caracteres";
        }
        return null; // null significa que no hay error
    }
    //Validación del primer apellido
    public String validarApellido1() {
        if (apellido1 == null || apellido1.trim().isEmpty()) {
            return "El primer apellido es requerido";
        }
        if (apellido1.length() < 2 || apellido1.length() > 20) {
            return "El primer apellido debe tener entre 2 y 20 caracteres";
        }
        return null;
    }
    //Validación del segundo apellido
    public String validarApellido2() {
        if (apellido2 == null || apellido2.trim().isEmpty()) {
            return "El segundo apellido es requerido";
        }
        if (apellido2.length() < 2 || apellido2.length() > 20) {
            return "El segundo apellido debe tener entre 2 y 20 caracteres";
        }
        return null;
    }
    //Validación de la identificación
    public String validarIdentificacion() {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            return "La identificación es requerida";
        }
        if (identificacion.length() < 9) {
            return "La identificación debe tener al menos 9 caracteres";
        }
        return null;
    }
    //Validación del telefono.
    public String validarTelefono() {
        if (telefono == null || telefono.trim().isEmpty()) {
            return "El teléfono es requerido";
        }
        if (telefono.length() < 8) {
            return "El teléfono debe tener al menos 8 caracteres";
        }
        if (!telefono.matches("\\d+")) {
            return "El teléfono debe contener solo números";
        }
        return null;
    }
    //Validación del correo electronico
    public String validarCorreo() {
        //Primero se valida que el correo no este en blanco
        if (correo == null || correo.trim().isEmpty()) {
            return "El correo electrónico es requerido";
        }
        if (correo.contains(" ")) {
            return "El correo no puede contener espacios en blanco";
        }
        //Luego se divide en 2 partes y se analiza cada parte
        String[] partes = correo.split("@");
             if (partes.length != 2) {
                 return "Formato de correo inválido. Debe ser: usuario@dominio";
             }
             if (partes[0].length() < 3 || partes[1].length() < 3) {
                 return "Ambas partes del correo deben tener al menos 3 caracteres";
             }
             if (!partes[1].contains(".")) {
                 return "El dominio del correo debe contener un punto (ej: gmail.com)";
             }
             return null;
         }
    //Validacion de la dirección
    public String validarDireccion() {
        if (direccion == null || direccion.trim().isEmpty()) {
            return "La dirección es requerida";
        }
        if (direccion.length() < 5 || direccion.length() > 60) {
            return "La dirección debe tener entre 5 y 60 caracteres";
        }
        return null;
    }
    //Validación de la organización
    public String validarOrganizacion() {
        if (organizacion != null && organizacion.length() > 40) {
            return "La organización no puede tener más de 40 caracteres";
        }
        return null;
    }
    //Validación de los temas de interes
    public String validarTemaInteres(String tema) {
        if (tema == null || tema.trim().isEmpty()) {
            return "El tema de interés no puede estar vacío";
        }
        if (tema.length() < 5 || tema.length() > 30) {
            return "Cada tema de interés debe tener entre 5 y 30 caracteres";
        }
        return null;
    }
    //Validación de contraseña
    public String validarContrasena() {
        if (contraseña == null || contraseña.trim().isEmpty()) {
            return "La contraseña es requerida";
        }
        if (contraseña.length() < 6) {
            return "La contraseña debe tener al menos 6 caracteres";
        }
        return null;
    }
    // Método para validar todo el estudiante de una vez
    public List<String> validarEstudianteCompleto() {
        List<String> errores = new ArrayList<>();
        
        agregarError(errores, validarNombre());
        agregarError(errores, validarApellido1());
        agregarError(errores, validarApellido2());
        agregarError(errores, validarIdentificacion());
        agregarError(errores, validarTelefono());
        agregarError(errores, validarCorreo());
        agregarError(errores, validarDireccion());
        agregarError(errores, validarOrganizacion());
        agregarError(errores, validarContrasena());
        
        return errores;
    }
      private void agregarError(List<String> errores, String error) {
        if (error != null) {
            errores.add(error);
        }
    }

}
         
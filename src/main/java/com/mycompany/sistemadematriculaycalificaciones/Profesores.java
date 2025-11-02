/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadematriculaycalificaciones;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ronni
 */
public class Profesores implements Serializable{
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String identificacion;
    private String telefono;
    private String correo;
    private String direccion;
    private List<String> titulosobtenidos;
    private List<String> certificaciones; 
    private String contraseña;
    private Date fechaRegistro;
    private List<Grupos> gruposImpartiendo;
    private String hashContrasena;
    
    private static final long serialVersionUID = 1L;
    //Constructor
    public Profesores() {
    }
    public Profesores(String nombre, String apellido1, String apellido2,
                  String identificacion, String telefono, String correo,
                  String direccion, List<String> titulosobtenidos, List<String> certificaciones, String contraseña) {
        this();
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.identificacion = identificacion;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.titulosobtenidos = titulosobtenidos != null ? titulosobtenidos : new ArrayList<>();
        this.certificaciones = certificaciones != null ? certificaciones : new ArrayList<>();
        this.contraseña = contraseña;
        this.hashContrasena = crearHashSeguro(contraseña); // Hashear la contraseña al crear el objeto
        this.gruposImpartiendo = new ArrayList<>();
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
    
    
    //Títulos obtenidos
    public List<String> getTitulosobtenidos() {
        return titulosobtenidos; 
    }
    public void setTitulosobtenidos(List<String> titulosobtenidos) {
        this.titulosobtenidos = titulosobtenidos; 
    }
    //Certificaciones de estudios
    public List<String> getCertificaciones() 
    { return certificaciones; 
    }
    public void setCertificaciones(List<String> certificaciones) {
        this.certificaciones = certificaciones; 
    }
    
    //Implementación de Hash para guardar la contraseña de forma segura.
    private String crearHashSeguro(String contraseña) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(contraseña.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    } catch (Exception e) {
        throw new RuntimeException("Error al crear hash", e);
    }
}
    
    // Setter con Hash
    public void setContrasena(String contraseña) {
        this.hashContrasena = crearHashSeguro(contraseña);
    }

    // Getter con Hash
    public String getContrasena() {
        return "********"; //Solo devuelve asteriscos
    }

    // Para validar cuando el usuario ingrese contraseña
    public boolean compararContrasena(String contraseñaIngresada) {
        return crearHashSeguro(contraseñaIngresada).equals(this.hashContrasena);
    }
    //Fecha de registro
    public Date getFechaRegistro() 
    { return fechaRegistro; 
    }
    //Metodos para manipular las certificaciones
    public void agregarCertificaciones(String certificacion) {
        if (certificacion != null && certificacion.length() >= 5 && certificacion.length() <= 30) {
            this.certificaciones.add(certificacion);
        }
    }
    
    public void eliminarCertificaciones(String certificacion) {
        this.certificaciones.remove(certificacion);
    }
    
    public void limpiarCertificaciones() {
        this.certificaciones.clear();
    }
    //Metodos para manipular los titulos obtenidos
    public void agregarTitulos(String titulo) {
        if (titulo != null && titulo.length() >= 5 && titulo.length() <= 30) {
            this.titulosobtenidos.add(titulo);
        }
    }
    
    public void eliminarTitulos(String titulo) {
        this.titulosobtenidos.remove(titulo);
    }
    
    public void limpiarTitulos() {
        this.titulosobtenidos.clear();
    }
    // Getter para la lista de grupos
    public List<Grupos> getGruposImpartiendo() {
        return gruposImpartiendo;
    }

    // Método para agregar grupo al profesor
    public void agregarGrupo(Grupos grupo) {
        if (grupo != null && !gruposImpartiendo.contains(grupo)) {
            this.gruposImpartiendo.add(grupo);
        }
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
            return "Por favor digite el primer apellido";
        }
        if (apellido1.length() < 2 || apellido1.length() > 20) {
            return "El primer apellido debe tener entre 2 y 20 caracteres";
        }
        return null;
    }
    //Validación del segundo apellido
    public String validarApellido2() {
        if (apellido2 == null || apellido2.trim().isEmpty()) {
            return "Por favor digite el segundo apellido";
        }
        if (apellido2.length() < 2 || apellido2.length() > 20) {
            return "El segundo apellido debe tener entre 2 y 20 caracteres";
        }
        return null;
    }
    //Validación de la identificación
    public String validarIdentificacion() {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            return "Por favor digite la identificación";
        }
        if (identificacion.length() < 9) {
            return "La identificación debe tener al menos 9 caracteres";
        }
        return null;
    }
    //Validación del telefono.
    public String validarTelefono() {
        if (telefono == null || telefono.trim().isEmpty()) {
            return "Por favor digite el número de telefono";
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
            return "Por favor digite el correo electrónico";
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
            return "Por favor digite la dirección";
        }
        if (direccion.length() < 5 || direccion.length() > 60) {
            return "La dirección debe tener entre 5 y 60 caracteres";
        }
        return null;
    }
    
    //Validación de los temas de interes
    public String validarCertificaciones(String certificacion) {
        if (certificacion == null || certificacion.trim().isEmpty()) {
            return "Por favor digite al menos UNA certificación";
        }
        if (certificacion.length() < 5 || certificacion.length() > 40) {
            return "Cada certificación debe tener entre 5 y 30 caracteres";
        }
        return null;
    }
    //Validación de los titulos
    public String validarTitulosObtenidos(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return "Por favor digite al menos UN titulo obtenido ";
        }
        if (titulo.length() < 5 || titulo.length() > 40) {
            return "Cada titulo debe tener entre 5 y 40 caracteres";
        }
        return null;
    }
    
    //Validación de contraseña
    public String validarContrasena() {
    if (contraseña == null || contraseña.trim().isEmpty()) {
        return "Por favor digite la contraseña";
    }
    if (contraseña.length() < 8) {
        return "La contraseña debe tener al menos 8 caracteres";
    }
    
    // Verificar que contenga al menos un número
    if (!contraseña.matches(".*\\d.*")) {
        return "La contraseña debe contener al menos un número";
    }
    
    // Verificar que contenga al menos un carácter especial
    if (!contraseña.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
        return "La contraseña debe contener al menos un carácter especial";
    }
    
    return null;
}
    // Validar todos los títulos
public String validarTodosLosTitulos() {
    if (titulosobtenidos == null || titulosobtenidos.isEmpty()) {
        return "Debe agregar al menos un título obtenido";
    }
    
    for (String titulo : titulosobtenidos) {
        String error = validarTitulosObtenidos(titulo);
        if (error != null) {
            return error;
        }
    }
    return null;
}

// Validar todas las certificaciones
public String validarTodasLasCertificaciones() {
    if (certificaciones == null || certificaciones.isEmpty()) {
        return "Debe agregar al menos una certificación";
    }
    
    for (String certificacion : certificaciones) {
        String error = validarCertificaciones(certificacion);
        if (error != null) {
            return error;
        }
    }
    return null;
}
    // Método para validar todo el profesor de una vez
    public List<String> validarProfesorCompleto() {
        List<String> errores = new ArrayList<>();
        
        agregarError(errores, validarNombre());
        agregarError(errores, validarApellido1());
        agregarError(errores, validarApellido2());
        agregarError(errores, validarIdentificacion());
        agregarError(errores, validarTelefono());
        agregarError(errores, validarCorreo());
        agregarError(errores, validarDireccion());
        agregarError(errores, validarContrasena());
        agregarError(errores, validarTodosLosTitulos());
        agregarError(errores, validarTodasLasCertificaciones());
        return errores;
    }
    //Agregar los errores a una lista
      private void agregarError(List<String> errores, String error) {
        if (error != null) {
            errores.add(error);
        }
    }
    
    public boolean ingresoSistema(String identificacion, String contrasena) {
        if (identificacion.equals(this.identificacion)) {
            if (compararContrasena(contrasena)) {
                return true;
            }
        }
        return false;
    }
    
}


package com.mycompany.sistemadematriculaycalificaciones;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Clase para gestionar el guardado de datos en archivos
 * @author fabiansanchezd
 */
public class GestorArchivos {

    // Nombres de archivos
    private static final String ARCHIVO_ESTUDIANTES = "estudiantes.dat";
    private static final String ARCHIVO_PROFESORES = "profesores.dat";
    private static final String ARCHIVO_CURSOS = "cursos.dat";
    private static final String ARCHIVO_EVALUACIONES = "evaluaciones.dat";
    private static final String ARCHIVO_EVALUACIONES_ASIGNADAS = "evaluacionesAsignadas.dat";
    private static final String ARCHIVO_EVALUACIONES_REALIZADAS = "evaluacionesRealizadas.dat";

    // ==================== ESTUDIANTES ====================

    public static void guardarEstudiantes(List<Estudiantes> estudiantes) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_ESTUDIANTES))) {
            oos.writeObject(estudiantes);
        } catch (IOException e) {
            System.err.println("Error al guardar estudiantes: " + e.getMessage());
        }
    }

    public static List<Estudiantes> cargarEstudiantes() {
        File archivo = new File(ARCHIVO_ESTUDIANTES);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_ESTUDIANTES))) {
            return (List<Estudiantes>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar estudiantes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ==================== PROFESORES ====================

    public static void guardarProfesores(List<Profesores> profesores) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_PROFESORES))) {
            oos.writeObject(profesores);
        } catch (IOException e) {
            System.err.println("Error al guardar profesores: " + e.getMessage());
        }
    }

    public static List<Profesores> cargarProfesores() {
        File archivo = new File(ARCHIVO_PROFESORES);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_PROFESORES))) {
            return (List<Profesores>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    // ==================== CURSOS ====================

    public static void guardarCursos(List<Cursos> cursos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_CURSOS))) {
            oos.writeObject(cursos);
        } catch (IOException e) {
            System.err.println("Error al guardar cursos: " + e.getMessage());
        }
    }

    public static List<Cursos> cargarCursos() {
        File archivo = new File(ARCHIVO_CURSOS);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_CURSOS))) {
            return (List<Cursos>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    // ==================== EVALUACIONES ====================

    public static void guardarEvaluaciones(List<Evaluaciones> evaluaciones) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_EVALUACIONES))) {
            oos.writeObject(evaluaciones);
        } catch (IOException e) {
            System.err.println("Error al guardar evaluaciones: " + e.getMessage());
        }
    }

    public static List<Evaluaciones> cargarEvaluaciones() {
        File archivo = new File(ARCHIVO_EVALUACIONES);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_EVALUACIONES))) {
            return (List<Evaluaciones>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    // ==================== EVALUACIONES ASIGNADAS ====================

    public static void guardarEvaluacionesAsignadas(List<EvaluacionAsignada> evaluacionesAsignadas) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_EVALUACIONES_ASIGNADAS))) {
            oos.writeObject(evaluacionesAsignadas);
        } catch (IOException e) {
            System.err.println("Error al guardar evaluaciones asignadas: " + e.getMessage());
        }
    }

    public static List<EvaluacionAsignada> cargarEvaluacionesAsignadas() {
        File archivo = new File(ARCHIVO_EVALUACIONES_ASIGNADAS);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_EVALUACIONES_ASIGNADAS))) {
            return (List<EvaluacionAsignada>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    // ==================== EVALUACIONES REALIZADAS ====================

    public static void guardarEvaluacionesRealizadas(List<EvaluacionRealizada> evaluacionesRealizadas) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_EVALUACIONES_REALIZADAS))) {
            oos.writeObject(evaluacionesRealizadas);
        } catch (IOException e) {
            System.err.println("Error al guardar evaluaciones realizadas: " + e.getMessage());
        }
    }

    public static List<EvaluacionRealizada> cargarEvaluacionesRealizadas() {
        File archivo = new File(ARCHIVO_EVALUACIONES_REALIZADAS);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_EVALUACIONES_REALIZADAS))) {
            return (List<EvaluacionRealizada>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}

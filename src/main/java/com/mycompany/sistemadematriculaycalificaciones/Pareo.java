/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadematriculaycalificaciones;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.io.Serializable;

/**
 * Clase que representa una pregunta de tipo Pareo (matching)
 * @author ronni
 */
public class Pareo implements Serializable {
    private String descripcion;
    private int puntos;
    private List<String> columna1; // Lista de enunciados
    private List<String> columna2; // Lista de respuestas y distractores
    private Map<Integer, Integer> relacionesCorrectas; // Mapa: índice columna1 -> índice columna2
    private boolean ordenAleatorio; // Si las opciones se muestran en orden aleatorio

    private static final long serialVersionUID = 1L;

    // Constructor vacío
    public Pareo() {
        this.columna1 = new ArrayList<>();
        this.columna2 = new ArrayList<>();
        this.relacionesCorrectas = new HashMap<>();
        this.ordenAleatorio = false;
    }

    // Constructor con parámetros
    public Pareo(String descripcion, int puntos) {
        this();
        this.descripcion = descripcion;
        this.puntos = puntos;
    }

    // Constructor completo
    public Pareo(String descripcion, int puntos, List<String> columna1,
                 List<String> columna2, Map<Integer, Integer> relacionesCorrectas,
                 boolean ordenAleatorio) {
        this.descripcion = descripcion;
        this.puntos = puntos;
        this.columna1 = columna1 != null ? columna1 : new ArrayList<>();
        this.columna2 = columna2 != null ? columna2 : new ArrayList<>();
        this.relacionesCorrectas = relacionesCorrectas != null ? relacionesCorrectas : new HashMap<>();
        this.ordenAleatorio = ordenAleatorio;
    }

    // Getters y Setters
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public List<String> getColumna1() {
        return columna1;
    }

    public void setColumna1(List<String> columna1) {
        this.columna1 = columna1;
    }

    public List<String> getColumna2() {
        return columna2;
    }

    public void setColumna2(List<String> columna2) {
        this.columna2 = columna2;
    }

    public Map<Integer, Integer> getRelacionesCorrectas() {
        return relacionesCorrectas;
    }

    public void setRelacionesCorrectas(Map<Integer, Integer> relacionesCorrectas) {
        this.relacionesCorrectas = relacionesCorrectas;
    }

    public boolean isOrdenAleatorio() {
        return ordenAleatorio;
    }

    public void setOrdenAleatorio(boolean ordenAleatorio) {
        this.ordenAleatorio = ordenAleatorio;
    }

    // Métodos para agregar elementos a las columnas
    public void agregarEnColumna1(String enunciado) {
        if (enunciado != null && !enunciado.trim().isEmpty()) {
            this.columna1.add(enunciado.trim());
        }
    }

    public void agregarEnColumna2(String respuesta) {
        if (respuesta != null && !respuesta.trim().isEmpty()) {
            this.columna2.add(respuesta.trim());
        }
    }

    // Método para establecer una relación correcta
    public void agregarRelacion(int indiceColumna1, int indiceColumna2) {
        if (indiceColumna1 >= 0 && indiceColumna1 < columna1.size() &&
            indiceColumna2 >= 0 && indiceColumna2 < columna2.size()) {
            this.relacionesCorrectas.put(indiceColumna1, indiceColumna2);
        }
    }

    // Método para obtener columna 2 mezclada si ordenAleatorio es true
    public List<String> getColumna2Mezclada() {
        List<String> columna2Mezclada = new ArrayList<>(columna2);
        if (ordenAleatorio) {
            Collections.shuffle(columna2Mezclada);
        }
        return columna2Mezclada;
    }

    // Método para obtener columna 1 mezclada si ordenAleatorio es true
    public List<String> getColumna1Mezclada() {
        List<String> columna1Mezclada = new ArrayList<>(columna1);
        if (ordenAleatorio) {
            Collections.shuffle(columna1Mezclada);
        }
        return columna1Mezclada;
    }

    // Método para calificar las respuestas del estudiante
    // Parámetro: Map con índice de columna1 -> índice de columna2 que el estudiante seleccionó
    public double calificar(Map<Integer, Integer> respuestasEstudiante) {
        if (respuestasEstudiante == null || respuestasEstudiante.isEmpty()) {
            return 0.0;
        }

        int correctas = 0;
        int total = relacionesCorrectas.size();

        if (total == 0) {
            return 0.0;
        }

        // Contar respuestas correctas
        for (Map.Entry<Integer, Integer> entrada : respuestasEstudiante.entrySet()) {
            Integer indiceCol1 = entrada.getKey();
            Integer indiceCol2 = entrada.getValue();

            if (relacionesCorrectas.containsKey(indiceCol1)) {
                if (relacionesCorrectas.get(indiceCol1).equals(indiceCol2)) {
                    correctas++;
                }
            }
        }

        // Calcular calificación proporcional
        double porcentaje = (double) correctas / total;
        return porcentaje * puntos;
    }

    // Método de validación
    public String validar() {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return "La descripción no puede estar vacía";
        }

        if (puntos < 1) {
            return "Los puntos deben ser al menos 1";
        }

        if (columna1 == null || columna1.isEmpty()) {
            return "La columna 1 debe tener al menos un enunciado";
        }

        if (columna2 == null || columna2.isEmpty()) {
            return "La columna 2 debe tener al menos una respuesta";
        }

        if (columna2.size() < columna1.size()) {
            return "La columna 2 debe tener al menos tantos elementos como la columna 1";
        }

        if (relacionesCorrectas == null || relacionesCorrectas.isEmpty()) {
            return "Debe definir al menos una relación correcta";
        }

        if (relacionesCorrectas.size() != columna1.size()) {
            return "Debe haber una relación definida para cada elemento de la columna 1";
        }

        // Validar que todas las relaciones sean válidas
        for (Map.Entry<Integer, Integer> entrada : relacionesCorrectas.entrySet()) {
            Integer indiceCol1 = entrada.getKey();
            Integer indiceCol2 = entrada.getValue();

            if (indiceCol1 < 0 || indiceCol1 >= columna1.size()) {
                return "Índice inválido en columna 1: " + indiceCol1;
            }

            if (indiceCol2 < 0 || indiceCol2 >= columna2.size()) {
                return "Índice inválido en columna 2: " + indiceCol2;
            }
        }

        return null; // null significa que no hay errores
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pregunta de Pareo:\n");
        sb.append("Descripción: ").append(descripcion).append("\n");
        sb.append("Puntos: ").append(puntos).append("\n");
        sb.append("Orden aleatorio: ").append(ordenAleatorio ? "Sí" : "No").append("\n");
        sb.append("\nColumna 1 (Enunciados):\n");
        for (int i = 0; i < columna1.size(); i++) {
            sb.append(i).append(". ").append(columna1.get(i)).append("\n");
        }
        sb.append("\nColumna 2 (Respuestas):\n");
        for (int i = 0; i < columna2.size(); i++) {
            sb.append(i).append(". ").append(columna2.get(i)).append("\n");
        }
        sb.append("\nRelaciones correctas:\n");
        for (Map.Entry<Integer, Integer> entrada : relacionesCorrectas.entrySet()) {
            sb.append("Columna 1[").append(entrada.getKey()).append("] -> Columna 2[")
              .append(entrada.getValue()).append("]\n");
        }
        return sb.toString();
    }
}

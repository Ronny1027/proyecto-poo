/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadematriculaycalificaciones;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.io.Serializable;

/**
 * Clase que representa una pregunta de tipo Sopa de Letras (word search)
 * @author ronni
 */
public class Sopa implements Serializable {
    private String descripcion;
    private int puntos;
    private List<String> palabras; // Palabras a buscar
    private int tamano; // Tamaño de la cuadrícula (cuadrada)
    private char[][] grid; // La cuadrícula de letras
    private boolean ordenAleatorio; // Si las palabras se colocan aleatoriamente
    private Map<String, PosicionPalabra> posicionesPalabras; // Posiciones de cada palabra

    private static final long serialVersionUID = 1L;

    // Clase interna para almacenar la posición de una palabra
    private static class PosicionPalabra implements Serializable {
        int filaInicio;
        int colInicio;
        int direccion; // 0-7 para las 8 direcciones

        public PosicionPalabra(int fila, int col, int dir) {
            this.filaInicio = fila;
            this.colInicio = col;
            this.direccion = dir;
        }
    }

    // Direcciones: [filaIncremento, columnaIncremento]
    // 0: Horizontal derecha (0,1)
    // 1: Horizontal izquierda (0,-1)
    // 2: Vertical abajo (1,0)
    // 3: Vertical arriba (-1,0)
    // 4: Diagonal abajo-derecha (1,1)
    // 5: Diagonal arriba-izquierda (-1,-1)
    // 6: Diagonal abajo-izquierda (1,-1)
    // 7: Diagonal arriba-derecha (-1,1)
    private static final int[][] DIRECCIONES = {
        {0, 1},   // Horizontal derecha
        {0, -1},  // Horizontal izquierda
        {1, 0},   // Vertical abajo
        {-1, 0},  // Vertical arriba
        {1, 1},   // Diagonal abajo-derecha
        {-1, -1}, // Diagonal arriba-izquierda
        {1, -1},  // Diagonal abajo-izquierda
        {-1, 1}   // Diagonal arriba-derecha
    };

    // Constructor vacío
    public Sopa() {
        this.palabras = new ArrayList<>();
        this.posicionesPalabras = new HashMap<>();
        this.ordenAleatorio = false;
        this.tamano = 0;
    }

    // Constructor con parámetros
    public Sopa(String descripcion, int puntos) {
        this();
        this.descripcion = descripcion;
        this.puntos = puntos;
    }

    // Constructor completo
    public Sopa(String descripcion, int puntos, List<String> palabras, boolean ordenAleatorio) {
        this.descripcion = descripcion;
        this.puntos = puntos;
        this.palabras = palabras != null ? palabras : new ArrayList<>();
        this.ordenAleatorio = ordenAleatorio;
        this.posicionesPalabras = new HashMap<>();
        calcularTamano();
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

    public List<String> getPalabras() {
        return palabras;
    }

    public void setPalabras(List<String> palabras) {
        this.palabras = palabras;
        calcularTamano();
    }

    public int getTamano() {
        return tamano;
    }

    public char[][] getGrid() {
        return grid;
    }

    public boolean isOrdenAleatorio() {
        return ordenAleatorio;
    }

    public void setOrdenAleatorio(boolean ordenAleatorio) {
        this.ordenAleatorio = ordenAleatorio;
    }

    // Método para agregar una palabra
    public void agregarPalabra(String palabra) {
        if (palabra != null && !palabra.trim().isEmpty()) {
            this.palabras.add(palabra.trim().toUpperCase());
            calcularTamano();
        }
    }

    // Calcular el tamaño de la cuadrícula basado en la palabra más larga
    private void calcularTamano() {
        if (palabras.isEmpty()) {
            this.tamano = 10; // Tamaño mínimo
            return;
        }

        int maxLongitud = 0;
        for (String palabra : palabras) {
            if (palabra.length() > maxLongitud) {
                maxLongitud = palabra.length();
            }
        }

        // El tamaño debe ser al menos la longitud de la palabra más larga + 2
        // para tener espacio para colocarla en diferentes posiciones
        this.tamano = Math.max(maxLongitud + 2, 10);
    }

    // Generar la sopa de letras
    public boolean generarSopa() {
        if (palabras.isEmpty()) {
            return false;
        }

        calcularTamano();
        grid = new char[tamano][tamano];
        posicionesPalabras.clear();

        // Inicializar el grid con espacios
        for (int i = 0; i < tamano; i++) {
            for (int j = 0; j < tamano; j++) {
                grid[i][j] = ' ';
            }
        }

        Random random = new Random();

        // Intentar colocar cada palabra
        for (String palabra : palabras) {
            boolean colocada = false;
            int intentos = 0;
            int maxIntentos = 100;

            while (!colocada && intentos < maxIntentos) {
                int direccion;

                if (ordenAleatorio) {
                    // Seleccionar dirección aleatoria
                    direccion = random.nextInt(8);
                } else {
                    // Usar direcciones de manera secuencial/predecible
                    direccion = intentos % 8;
                }

                int fila = random.nextInt(tamano);
                int col = random.nextInt(tamano);

                if (puedeColocarPalabra(palabra, fila, col, direccion)) {
                    colocarPalabra(palabra, fila, col, direccion);
                    posicionesPalabras.put(palabra, new PosicionPalabra(fila, col, direccion));
                    colocada = true;
                }

                intentos++;
            }

            if (!colocada) {
                // Si no se pudo colocar una palabra, aumentar el tamaño y reintentar
                tamano += 2;
                return generarSopa();
            }
        }

        // Rellenar espacios vacíos con letras aleatorias
        for (int i = 0; i < tamano; i++) {
            for (int j = 0; j < tamano; j++) {
                if (grid[i][j] == ' ') {
                    grid[i][j] = (char) ('A' + random.nextInt(26));
                }
            }
        }

        return true;
    }

    // Verificar si se puede colocar una palabra en una posición y dirección
    private boolean puedeColocarPalabra(String palabra, int fila, int col, int direccion) {
        int[] dir = DIRECCIONES[direccion];
        int filaIncr = dir[0];
        int colIncr = dir[1];

        for (int i = 0; i < palabra.length(); i++) {
            int nuevaFila = fila + (i * filaIncr);
            int nuevaCol = col + (i * colIncr);

            // Verificar límites
            if (nuevaFila < 0 || nuevaFila >= tamano || nuevaCol < 0 || nuevaCol >= tamano) {
                return false;
            }

            // Verificar si la celda está vacía o contiene la misma letra
            char letraActual = grid[nuevaFila][nuevaCol];
            if (letraActual != ' ' && letraActual != palabra.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    // Colocar una palabra en el grid
    private void colocarPalabra(String palabra, int fila, int col, int direccion) {
        int[] dir = DIRECCIONES[direccion];
        int filaIncr = dir[0];
        int colIncr = dir[1];

        for (int i = 0; i < palabra.length(); i++) {
            int nuevaFila = fila + (i * filaIncr);
            int nuevaCol = col + (i * colIncr);
            grid[nuevaFila][nuevaCol] = palabra.charAt(i);
        }
    }

    // Método de validación
    public String validar() {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return "La descripción no puede estar vacía";
        }

        if (puntos < 1) {
            return "Los puntos deben ser al menos 1";
        }

        if (palabras == null || palabras.isEmpty()) {
            return "Debe haber al menos una palabra";
        }

        if (palabras.size() < 10) {
            return "Debe haber al menos 10 palabras en la sopa de letras";
        }

        // Validar que cada palabra tenga al menos 3 caracteres
        for (String palabra : palabras) {
            if (palabra == null || palabra.trim().isEmpty()) {
                return "No puede haber palabras vacías";
            }
            if (palabra.length() < 3) {
                return "Cada palabra debe tener al menos 3 caracteres";
            }
            if (!palabra.matches("[A-ZÁÉÍÓÚÑa-záéíóúñ]+")) {
                return "Las palabras solo pueden contener letras: " + palabra;
            }
        }

        return null; // null significa que no hay errores
    }

    // Método para calificar las respuestas del estudiante
    // Parámetro: Lista de palabras que el estudiante encontró
    public double calificar(List<String> palabrasEncontradas) {
        if (palabrasEncontradas == null || palabrasEncontradas.isEmpty()) {
            return 0.0;
        }

        if (palabras.isEmpty()) {
            return 0.0;
        }

        int correctas = 0;
        int total = palabras.size();

        // Normalizar las palabras encontradas a mayúsculas
        List<String> palabrasNormalizadas = new ArrayList<>();
        for (String palabra : palabrasEncontradas) {
            if (palabra != null) {
                palabrasNormalizadas.add(palabra.trim().toUpperCase());
            }
        }

        // Contar cuántas palabras correctas encontró
        for (String palabra : palabras) {
            if (palabrasNormalizadas.contains(palabra.toUpperCase())) {
                correctas++;
            }
        }

        // Calcular calificación proporcional
        double porcentaje = (double) correctas / total;
        return porcentaje * puntos;
    }

    // Método para verificar si una palabra está en la sopa
    public boolean palabraEstaEnSopa(String palabra) {
        if (palabra == null) {
            return false;
        }
        String palabraNormalizada = palabra.trim().toUpperCase();
        return palabras.contains(palabraNormalizada);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sopa de Letras:\n");
        sb.append("Descripción: ").append(descripcion).append("\n");
        sb.append("Puntos: ").append(puntos).append("\n");
        sb.append("Tamaño: ").append(tamano).append("x").append(tamano).append("\n");
        sb.append("Orden aleatorio: ").append(ordenAleatorio ? "Sí" : "No").append("\n");
        sb.append("\nPalabras a buscar (").append(palabras.size()).append("):\n");

        for (int i = 0; i < palabras.size(); i++) {
            sb.append((i + 1)).append(". ").append(palabras.get(i)).append("\n");
        }

        if (grid != null) {
            sb.append("\nCuadrícula:\n");
            sb.append("  ");
            for (int j = 0; j < tamano; j++) {
                sb.append(j % 10).append(" ");
            }
            sb.append("\n");

            for (int i = 0; i < tamano; i++) {
                sb.append(i % 10).append(" ");
                for (int j = 0; j < tamano; j++) {
                    sb.append(grid[i][j]).append(" ");
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    // Método para obtener las direcciones como texto
    public static String obtenerNombreDireccion(int direccion) {
        switch (direccion) {
            case 0: return "Horizontal derecha";
            case 1: return "Horizontal izquierda";
            case 2: return "Vertical abajo";
            case 3: return "Vertical arriba";
            case 4: return "Diagonal abajo-derecha";
            case 5: return "Diagonal arriba-izquierda";
            case 6: return "Diagonal abajo-izquierda";
            case 7: return "Diagonal arriba-derecha";
            default: return "Desconocida";
        }
    }
}

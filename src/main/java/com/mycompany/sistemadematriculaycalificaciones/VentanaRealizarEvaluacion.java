/*
 * Ventana para realizar evaluaciones con temporizador en tiempo real
 */
package com.mycompany.sistemadematriculaycalificaciones;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * Ventana dedicada a la realización de evaluaciones por estudiantes
 * Incluye temporizador, navegación entre preguntas, y guardado automático
 */
public class VentanaRealizarEvaluacion extends JFrame {
    private Estudiantes estudiante;
    private EvaluacionAsignada evaluacionAsignada;
    private EvaluacionRealizada evaluacionRealizada;
    private Sistemadematriculaycalificaciones sistemaPrincipal;

    // Timer y tiempo
    private javax.swing.Timer timer;
    private int tiempoRestanteSegundos;
    private JLabel labelTiempo;

    // Preguntas y navegación
    private List<Object> todasLasPreguntas;
    private int preguntaActual;
    private Map<Integer, Object> respuestasEstudiante;

    // Componentes UI
    private JPanel panelPregunta;
    private JButton btnAnterior, btnSiguiente, btnFinalizar;
    private JLabel labelNumeroPregunta;

    // Constructor
    public VentanaRealizarEvaluacion(Estudiantes estudiante, EvaluacionAsignada asignacion,
                                     Sistemadematriculaycalificaciones sistema) {
        this.estudiante = estudiante;
        this.evaluacionAsignada = asignacion;
        this.sistemaPrincipal = sistema;
        this.preguntaActual = 0;
        this.respuestasEstudiante = new HashMap<>();

        // Crear evaluación realizada
        this.evaluacionRealizada = new EvaluacionRealizada(estudiante, asignacion);

        // Configurar ventana
        setTitle("Realizando Evaluación: " + asignacion.getEvaluacion().getNombre());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        // Evitar que se cierre sin finalizar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int opcion = JOptionPane.showConfirmDialog(
                    VentanaRealizarEvaluacion.this,
                    "¿Está seguro que desea salir?\nSu progreso se perderá.",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION
                );
                if (opcion == JOptionPane.YES_OPTION) {
                    if (timer != null) {
                        timer.stop();
                    }
                    dispose();
                }
            }
        });

        // Generar lista de preguntas
        generarListaPreguntas();

        // Calcular tiempo en segundos
        this.tiempoRestanteSegundos = asignacion.getEvaluacion().getDuracion() * 60;

        // Crear interfaz
        crearInterfaz();

        // Iniciar temporizador
        iniciarTemporizador();

        // Mostrar primera pregunta
        mostrarPregunta();
    }

    private void generarListaPreguntas() {
        todasLasPreguntas = new ArrayList<>();
        Evaluaciones eval = evaluacionAsignada.getEvaluacion();

        // Agregar todas las preguntas
        todasLasPreguntas.addAll(eval.getSeleccionesUnicas());
        todasLasPreguntas.addAll(eval.getSeleccionesMultiples());
        todasLasPreguntas.addAll(eval.getFalsoVerdaderos());
        todasLasPreguntas.addAll(eval.getPareos());
        todasLasPreguntas.addAll(eval.getSopas());

        // Mezclar si está configurado
        if ("On".equals(eval.getPregunRandom())) {
            Collections.shuffle(todasLasPreguntas);
        }

        // Guardar orden de preguntas
        evaluacionRealizada.setOrdenPreguntas(new ArrayList<>(todasLasPreguntas));
    }

    private void crearInterfaz() {
        setLayout(new BorderLayout(10, 10));

        // Panel superior: información y temporizador
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel labelTitulo = new JLabel("Evaluación: " + evaluacionAsignada.getEvaluacion().getNombre());
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelSuperior.add(labelTitulo, BorderLayout.WEST);

        labelTiempo = new JLabel("Tiempo: 00:00:00");
        labelTiempo.setFont(new Font("Arial", Font.BOLD, 24));
        labelTiempo.setForeground(Color.RED);
        panelSuperior.add(labelTiempo, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // Panel central: pregunta actual
        panelPregunta = new JPanel();
        panelPregunta.setLayout(new BoxLayout(panelPregunta, BoxLayout.Y_AXIS));
        panelPregunta.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPregunta = new JScrollPane(panelPregunta);
        add(scrollPregunta, BorderLayout.CENTER);

        // Panel inferior: navegación
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        labelNumeroPregunta = new JLabel("Pregunta 1 de " + todasLasPreguntas.size());
        labelNumeroPregunta.setFont(new Font("Arial", Font.BOLD, 14));
        panelInferior.add(labelNumeroPregunta, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnAnterior = new JButton("← Anterior");
        btnAnterior.setEnabled(false);
        btnAnterior.addActionListener(e -> navegarAnterior());

        btnSiguiente = new JButton("Siguiente →");
        btnSiguiente.addActionListener(e -> navegarSiguiente());

        btnFinalizar = new JButton("Revisar y Finalizar");
        btnFinalizar.setBackground(new Color(0, 150, 0));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.addActionListener(e -> mostrarRevision());

        panelBotones.add(btnAnterior);
        panelBotones.add(btnSiguiente);
        panelBotones.add(btnFinalizar);

        panelInferior.add(panelBotones, BorderLayout.EAST);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void iniciarTemporizador() {
        timer = new javax.swing.Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tiempoRestanteSegundos--;
                actualizarLabelTiempo();

                if (tiempoRestanteSegundos <= 0) {
                    timer.stop();
                    finalizarAutomaticamente();
                }
            }
        });
        timer.start();
        actualizarLabelTiempo();
    }

    private void actualizarLabelTiempo() {
        int horas = tiempoRestanteSegundos / 3600;
        int minutos = (tiempoRestanteSegundos % 3600) / 60;
        int segundos = tiempoRestanteSegundos % 60;

        labelTiempo.setText(String.format("Tiempo: %02d:%02d:%02d", horas, minutos, segundos));

        // Cambiar color si queda poco tiempo
        if (tiempoRestanteSegundos <= 300) { // 5 minutos
            labelTiempo.setForeground(Color.RED);
        } else if (tiempoRestanteSegundos <= 600) { // 10 minutos
            labelTiempo.setForeground(new Color(255, 140, 0)); // Naranja
        }
    }

    private void mostrarPregunta() {
        // Guardar respuesta de pregunta anterior si existe
        guardarRespuestaActual();

        // Limpiar panel
        panelPregunta.removeAll();

        // Actualizar número de pregunta
        labelNumeroPregunta.setText("Pregunta " + (preguntaActual + 1) + " de " + todasLasPreguntas.size());

        // Actualizar botones
        btnAnterior.setEnabled(preguntaActual > 0);
        btnSiguiente.setEnabled(preguntaActual < todasLasPreguntas.size() - 1);

        // Obtener pregunta actual
        Object pregunta = todasLasPreguntas.get(preguntaActual);

        // Mostrar según tipo
        if (pregunta instanceof SeleccionUnica) {
            mostrarSeleccionUnica((SeleccionUnica) pregunta);
        } else if (pregunta instanceof SeleccionMultiple) {
            mostrarSeleccionMultiple((SeleccionMultiple) pregunta);
        } else if (pregunta instanceof FalsoVerdadero) {
            mostrarFalsoVerdadero((FalsoVerdadero) pregunta);
        } else if (pregunta instanceof Pareo) {
            mostrarPareo((Pareo) pregunta);
        } else if (pregunta instanceof Sopa) {
            mostrarSopa((Sopa) pregunta);
        }

        panelPregunta.revalidate();
        panelPregunta.repaint();
    }

    private void mostrarSeleccionUnica(SeleccionUnica pregunta) {
        JLabel labelDesc = new JLabel("<html><b>Pregunta " + (preguntaActual + 1) + ":</b> " + pregunta.getDescripcion() + "</html>");
        labelDesc.setFont(new Font("Arial", Font.PLAIN, 16));
        panelPregunta.add(labelDesc);
        panelPregunta.add(Box.createVerticalStrut(20));

        ButtonGroup grupo = new ButtonGroup();
        List<String> opciones = pregunta.getOpciones();

        // Mezclar opciones si está configurado
        if ("On".equals(evaluacionAsignada.getEvaluacion().getRespueRandom())) {
            opciones = new ArrayList<>(opciones);
            Collections.shuffle(opciones);
        }

        for (String opcion : opciones) {
            JRadioButton radio = new JRadioButton(opcion);
            radio.setFont(new Font("Arial", Font.PLAIN, 14));
            radio.setActionCommand(opcion);
            grupo.add(radio);
            panelPregunta.add(radio);
            panelPregunta.add(Box.createVerticalStrut(10));

            // Restaurar respuesta si existe
            Object respuestaGuardada = respuestasEstudiante.get(preguntaActual);
            if (respuestaGuardada != null && respuestaGuardada.equals(opcion)) {
                radio.setSelected(true);
            }
        }

        // Guardar referencia al grupo para obtener respuesta después
        panelPregunta.putClientProperty("buttonGroup", grupo);
    }

    private void mostrarSeleccionMultiple(SeleccionMultiple pregunta) {
        JLabel labelDesc = new JLabel("<html><b>Pregunta " + (preguntaActual + 1) + ":</b> " + pregunta.getDescripcion() + "</html>");
        labelDesc.setFont(new Font("Arial", Font.PLAIN, 16));
        panelPregunta.add(labelDesc);
        panelPregunta.add(Box.createVerticalStrut(10));

        JLabel labelInst = new JLabel("(Seleccione todas las que apliquen)");
        labelInst.setFont(new Font("Arial", Font.ITALIC, 12));
        panelPregunta.add(labelInst);
        panelPregunta.add(Box.createVerticalStrut(20));

        List<JCheckBox> checkboxes = new ArrayList<>();
        List<String> opciones = pregunta.getOpciones();

        // Mezclar opciones si está configurado
        if ("On".equals(evaluacionAsignada.getEvaluacion().getRespueRandom())) {
            opciones = new ArrayList<>(opciones);
            Collections.shuffle(opciones);
        }

        for (String opcion : opciones) {
            JCheckBox checkbox = new JCheckBox(opcion);
            checkbox.setFont(new Font("Arial", Font.PLAIN, 14));
            checkboxes.add(checkbox);
            panelPregunta.add(checkbox);
            panelPregunta.add(Box.createVerticalStrut(10));

            // Restaurar respuesta si existe
            Object respuestaGuardada = respuestasEstudiante.get(preguntaActual);
            if (respuestaGuardada instanceof List) {
                List<String> respuestas = (List<String>) respuestaGuardada;
                if (respuestas.contains(opcion)) {
                    checkbox.setSelected(true);
                }
            }
        }

        panelPregunta.putClientProperty("checkboxes", checkboxes);
    }

    private void mostrarFalsoVerdadero(FalsoVerdadero pregunta) {
        JLabel labelDesc = new JLabel("<html><b>Pregunta " + (preguntaActual + 1) + ":</b> " + pregunta.getDescripcion() + "</html>");
        labelDesc.setFont(new Font("Arial", Font.PLAIN, 16));
        panelPregunta.add(labelDesc);
        panelPregunta.add(Box.createVerticalStrut(20));

        ButtonGroup grupo = new ButtonGroup();

        JRadioButton radioVerdadero = new JRadioButton("Verdadero");
        radioVerdadero.setFont(new Font("Arial", Font.PLAIN, 14));
        radioVerdadero.setActionCommand("Verdadero");
        grupo.add(radioVerdadero);
        panelPregunta.add(radioVerdadero);
        panelPregunta.add(Box.createVerticalStrut(10));

        JRadioButton radioFalso = new JRadioButton("Falso");
        radioFalso.setFont(new Font("Arial", Font.PLAIN, 14));
        radioFalso.setActionCommand("Falso");
        grupo.add(radioFalso);
        panelPregunta.add(radioFalso);

        // Restaurar respuesta si existe
        Object respuestaGuardada = respuestasEstudiante.get(preguntaActual);
        if (respuestaGuardada != null) {
            if (respuestaGuardada.equals("Verdadero")) {
                radioVerdadero.setSelected(true);
            } else if (respuestaGuardada.equals("Falso")) {
                radioFalso.setSelected(true);
            }
        }

        panelPregunta.putClientProperty("buttonGroup", grupo);
    }

    private void mostrarPareo(Pareo pregunta) {
        JLabel labelDesc = new JLabel("<html><b>Pregunta " + (preguntaActual + 1) + ":</b> " + pregunta.getDescripcion() + "</html>");
        labelDesc.setFont(new Font("Arial", Font.PLAIN, 16));
        panelPregunta.add(labelDesc);
        panelPregunta.add(Box.createVerticalStrut(10));

        JLabel labelInst = new JLabel("Relacione los elementos de la columna 1 con la columna 2:");
        labelInst.setFont(new Font("Arial", Font.ITALIC, 12));
        panelPregunta.add(labelInst);
        panelPregunta.add(Box.createVerticalStrut(20));

        List<String> col1 = pregunta.getColumna1();
        List<String> col2 = pregunta.isOrdenAleatorio() ? pregunta.getColumna2Mezclada() : pregunta.getColumna2();

        Map<Integer, JComboBox<String>> combos = new HashMap<>();

        for (int i = 0; i < col1.size(); i++) {
            JPanel panelFila = new JPanel(new FlowLayout(FlowLayout.LEFT));

            JLabel labelCol1 = new JLabel((i + 1) + ". " + col1.get(i) + " → ");
            labelCol1.setFont(new Font("Arial", Font.PLAIN, 14));
            panelFila.add(labelCol1);

            JComboBox<String> comboCol2 = new JComboBox<>();
            comboCol2.addItem("Seleccione...");
            for (int j = 0; j < col2.size(); j++) {
                comboCol2.addItem(j + ": " + col2.get(j));
            }
            comboCol2.setPreferredSize(new Dimension(300, 25));
            panelFila.add(comboCol2);

            combos.put(i, comboCol2);
            panelPregunta.add(panelFila);
            panelPregunta.add(Box.createVerticalStrut(10));

            // Restaurar respuesta si existe
            Object respuestaGuardada = respuestasEstudiante.get(preguntaActual);
            if (respuestaGuardada instanceof Map) {
                Map<Integer, Integer> respuestas = (Map<Integer, Integer>) respuestaGuardada;
                if (respuestas.containsKey(i)) {
                    comboCol2.setSelectedIndex(respuestas.get(i) + 1);
                }
            }
        }

        panelPregunta.putClientProperty("combos", combos);
        panelPregunta.putClientProperty("col2Size", col2.size());
    }

    private void mostrarSopa(Sopa pregunta) {
        JLabel labelDesc = new JLabel("<html><b>Pregunta " + (preguntaActual + 1) + ":</b> " + pregunta.getDescripcion() + "</html>");
        labelDesc.setFont(new Font("Arial", Font.PLAIN, 16));
        panelPregunta.add(labelDesc);
        panelPregunta.add(Box.createVerticalStrut(10));

        JLabel labelInst = new JLabel("Encuentre las siguientes palabras en la sopa de letras:");
        labelInst.setFont(new Font("Arial", Font.ITALIC, 12));
        panelPregunta.add(labelInst);
        panelPregunta.add(Box.createVerticalStrut(20));

        // Mostrar palabras a buscar
        JLabel labelPalabras = new JLabel("Palabras a buscar: " + String.join(", ", pregunta.getPalabras()));
        labelPalabras.setFont(new Font("Arial", Font.PLAIN, 12));
        panelPregunta.add(labelPalabras);
        panelPregunta.add(Box.createVerticalStrut(20));

        // Mostrar la cuadrícula
        char[][] grid = pregunta.getGrid();
        if (grid != null) {
            JPanel panelGrid = new JPanel(new GridLayout(pregunta.getTamano(), pregunta.getTamano(), 2, 2));
            panelGrid.setMaximumSize(new Dimension(500, 500));

            for (int i = 0; i < pregunta.getTamano(); i++) {
                for (int j = 0; j < pregunta.getTamano(); j++) {
                    JLabel labelCelda = new JLabel(String.valueOf(grid[i][j]), JLabel.CENTER);
                    labelCelda.setFont(new Font("Monospaced", Font.BOLD, 12));
                    labelCelda.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    labelCelda.setOpaque(true);
                    labelCelda.setBackground(Color.WHITE);
                    panelGrid.add(labelCelda);
                }
            }

            panelPregunta.add(panelGrid);
            panelPregunta.add(Box.createVerticalStrut(20));
        }

        // Campo para marcar palabras encontradas
        JLabel labelRespuesta = new JLabel("Palabras encontradas (separadas por coma):");
        labelRespuesta.setFont(new Font("Arial", Font.PLAIN, 14));
        panelPregunta.add(labelRespuesta);
        panelPregunta.add(Box.createVerticalStrut(10));

        JTextArea textArea = new JTextArea(3, 40);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollText = new JScrollPane(textArea);
        scrollText.setMaximumSize(new Dimension(600, 80));
        panelPregunta.add(scrollText);

        // Restaurar respuesta si existe
        Object respuestaGuardada = respuestasEstudiante.get(preguntaActual);
        if (respuestaGuardada instanceof List) {
            List<String> palabras = (List<String>) respuestaGuardada;
            textArea.setText(String.join(", ", palabras));
        }

        panelPregunta.putClientProperty("textArea", textArea);
    }

    private void guardarRespuestaActual() {
        if (preguntaActual < 0 || preguntaActual >= todasLasPreguntas.size()) {
            return;
        }

        Object pregunta = todasLasPreguntas.get(preguntaActual);

        if (pregunta instanceof SeleccionUnica || pregunta instanceof FalsoVerdadero) {
            ButtonGroup grupo = (ButtonGroup) panelPregunta.getClientProperty("buttonGroup");
            if (grupo != null && grupo.getSelection() != null) {
                respuestasEstudiante.put(preguntaActual, grupo.getSelection().getActionCommand());
            }
        } else if (pregunta instanceof SeleccionMultiple) {
            List<JCheckBox> checkboxes = (List<JCheckBox>) panelPregunta.getClientProperty("checkboxes");
            if (checkboxes != null) {
                List<String> seleccionadas = new ArrayList<>();
                for (JCheckBox cb : checkboxes) {
                    if (cb.isSelected()) {
                        seleccionadas.add(cb.getText());
                    }
                }
                if (!seleccionadas.isEmpty()) {
                    respuestasEstudiante.put(preguntaActual, seleccionadas);
                }
            }
        } else if (pregunta instanceof Pareo) {
            Map<Integer, JComboBox<String>> combos = (Map<Integer, JComboBox<String>>) panelPregunta.getClientProperty("combos");
            if (combos != null) {
                Map<Integer, Integer> relaciones = new HashMap<>();
                for (Map.Entry<Integer, JComboBox<String>> entry : combos.entrySet()) {
                    int selectedIndex = entry.getValue().getSelectedIndex();
                    if (selectedIndex > 0) {
                        relaciones.put(entry.getKey(), selectedIndex - 1);
                    }
                }
                if (!relaciones.isEmpty()) {
                    respuestasEstudiante.put(preguntaActual, relaciones);
                }
            }
        } else if (pregunta instanceof Sopa) {
            JTextArea textArea = (JTextArea) panelPregunta.getClientProperty("textArea");
            if (textArea != null && !textArea.getText().trim().isEmpty()) {
                String[] palabras = textArea.getText().split(",");
                List<String> listaPalabras = new ArrayList<>();
                for (String palabra : palabras) {
                    String p = palabra.trim();
                    if (!p.isEmpty()) {
                        listaPalabras.add(p);
                    }
                }
                if (!listaPalabras.isEmpty()) {
                    respuestasEstudiante.put(preguntaActual, listaPalabras);
                }
            }
        }
    }

    private void navegarAnterior() {
        if (preguntaActual > 0) {
            preguntaActual--;
            mostrarPregunta();
        }
    }

    private void navegarSiguiente() {
        if (preguntaActual < todasLasPreguntas.size() - 1) {
            preguntaActual++;
            mostrarPregunta();
        }
    }

    private void mostrarRevision() {
        guardarRespuestaActual();

        StringBuilder revision = new StringBuilder();
        revision.append("Resumen de su evaluación:\n\n");
        revision.append("Total de preguntas: ").append(todasLasPreguntas.size()).append("\n");
        revision.append("Preguntas respondidas: ").append(respuestasEstudiante.size()).append("\n");
        revision.append("Preguntas sin responder: ").append(todasLasPreguntas.size() - respuestasEstudiante.size()).append("\n\n");
        revision.append("¿Desea finalizar la evaluación?\n");
        revision.append("Una vez finalizada no podrá modificar sus respuestas.");

        int opcion = JOptionPane.showConfirmDialog(
            this,
            revision.toString(),
            "Revisar y Finalizar",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            finalizarEvaluacion();
        }
    }

    private void finalizarAutomaticamente() {
        JOptionPane.showMessageDialog(
            this,
            "El tiempo ha terminado.\nLa evaluación se finalizará automáticamente.",
            "Tiempo Agotado",
            JOptionPane.WARNING_MESSAGE
        );
        finalizarEvaluacion();
    }

    private void finalizarEvaluacion() {
        // Detener timer
        if (timer != null) {
            timer.stop();
        }

        // Guardar última respuesta
        guardarRespuestaActual();

        // Guardar respuestas en evaluación realizada
        evaluacionRealizada.setRespuestas(respuestasEstudiante);
        evaluacionRealizada.finalizar();

        // Calcular calificación
        double calificacionTotal = calcularCalificacion();
        evaluacionRealizada.setCalificacionObtenida(calificacionTotal);

        // Guardar en el sistema
        sistemaPrincipal.agregarEvaluacionRealizada(evaluacionRealizada);

        // Mostrar resultado
        JOptionPane.showMessageDialog(
            this,
            "Evaluación finalizada exitosamente.\n\n" +
            "Calificación obtenida: " + String.format("%.2f", calificacionTotal) + " puntos",
            "Evaluación Finalizada",
            JOptionPane.INFORMATION_MESSAGE
        );

        // Cerrar ventana
        dispose();
    }

    private double calcularCalificacion() {
        double calificacion = 0.0;

        for (int i = 0; i < todasLasPreguntas.size(); i++) {
            Object pregunta = todasLasPreguntas.get(i);
            Object respuesta = respuestasEstudiante.get(i);

            if (respuesta == null) {
                continue; // No respondió, 0 puntos
            }

            if (pregunta instanceof SeleccionUnica) {
                SeleccionUnica su = (SeleccionUnica) pregunta;
                // Comparar la respuesta del estudiante con la correcta
                if (su.getRespuesta() != null && su.getRespuesta().equals(respuesta)) {
                    calificacion += su.getPuntos();
                }
            } else if (pregunta instanceof SeleccionMultiple) {
                SeleccionMultiple sm = (SeleccionMultiple) pregunta;
                // Comparar las respuestas del estudiante con las correctas
                List<String> respuestasCorrectas = sm.getRespuesta();
                List<String> respuestasEstudiante = (List<String>) respuesta;
                if (respuestasCorrectas != null && respuestasEstudiante != null) {
                    // Verificar si ambas listas contienen los mismos elementos
                    if (respuestasCorrectas.size() == respuestasEstudiante.size() &&
                        respuestasCorrectas.containsAll(respuestasEstudiante) &&
                        respuestasEstudiante.containsAll(respuestasCorrectas)) {
                        calificacion += sm.getPuntos();
                    }
                }
            } else if (pregunta instanceof FalsoVerdadero) {
                FalsoVerdadero fv = (FalsoVerdadero) pregunta;
                // Comparar la respuesta del estudiante con la correcta
                if (fv.getRespuesta() != null && fv.getRespuesta().equals(respuesta)) {
                    calificacion += fv.getPuntos();
                }
            } else if (pregunta instanceof Pareo) {
                Pareo p = (Pareo) pregunta;
                calificacion += p.calificar((Map<Integer, Integer>) respuesta);
            } else if (pregunta instanceof Sopa) {
                Sopa s = (Sopa) pregunta;
                calificacion += s.calificar((List<String>) respuesta);
            }
        }

        return calificacion;
    }
}

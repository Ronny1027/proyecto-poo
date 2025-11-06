package com.mycompany.sistemadematriculaycalificaciones;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.Date;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

/**
 * Clase que maneja todas las ventanas y funcionalidades del administrador
 */
public class VentanaAdministrador {

    private Sistemadematriculaycalificaciones sistema;

    public VentanaAdministrador(Sistemadematriculaycalificaciones sistema) {
        this.sistema = sistema;
    }

    // Método para abrir la ventana principal del administrador
    public void abrirVentanaAdministradores() {
        // Cerrar ventana actual del sistema
        sistema.dispose();

        // Cargar datos
        sistema.getEstudiantes().clear();
        sistema.getEstudiantes().addAll(GestorArchivos.cargarEstudiantes());
        sistema.getProfesores().clear();
        sistema.getProfesores().addAll(GestorArchivos.cargarProfesores());
        sistema.getCursos().clear();
        sistema.getCursos().addAll(GestorArchivos.cargarCursos());

        // Crear nueva ventana
        JFrame ventanaAdministradores = new JFrame("Ventana de Administradores");
        ventanaAdministradores.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaAdministradores.setSize(330, 350);
        ventanaAdministradores.setLocationRelativeTo(null);

        // Agregar label
        JLabel label = new JLabel("Menú de administradores", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        ventanaAdministradores.add(label, BorderLayout.NORTH);

        //Botones
        JButton btnVolver1 = new JButton("Regresar");
        btnVolver1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventanaAdministradores.dispose();
                new Sistemadematriculaycalificaciones().setVisible(true);
            }
        });

        JButton btnEstudiantes = new JButton("Estudiantes");
        btnEstudiantes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventanaAdministradores.dispose();
                administradorEstudiantes();
            }
        });

        JButton btnProfesores = new JButton("Profesores");
        btnProfesores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventanaAdministradores.dispose();
                administradorProfesores();
            }
        });

        JButton btnCursos = new JButton("Cursos");
        btnCursos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventanaAdministradores.dispose();
                administradorCursos();
            }
        });

        JButton btnAsoCur = new JButton("Asociar grupos a cursos");
        btnAsoCur.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventanaAdministradores.dispose();
                ventanaAsociarGruposCursos();
            }
        });

        JButton btnAsoPro = new JButton("Asociar grupos a profesores");
        btnAsoPro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventanaAdministradores.dispose();
                ventanaAsociarProfesoresGrupos();
            }
        });

        JButton btnReportes = new JButton("Reportes");
        btnReportes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventanaAdministradores.dispose();
                mostrarVentanaReportesAdministrador();
            }
        });

        // Panel para los botones
        JPanel panelBoton = new JPanel();
        panelBoton.setLayout(new BoxLayout(panelBoton, BoxLayout.Y_AXIS));

        // CENTRAR LOS BOTONES
        btnEstudiantes.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnProfesores.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCursos.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAsoCur.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAsoPro.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnReportes.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver1.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agregar los botones con espacios
        panelBoton.add(Box.createVerticalStrut(20));
        panelBoton.add(btnEstudiantes);
        panelBoton.add(Box.createVerticalStrut(12));
        panelBoton.add(btnProfesores);
        panelBoton.add(Box.createVerticalStrut(12));
        panelBoton.add(btnCursos);
        panelBoton.add(Box.createVerticalStrut(12));
        panelBoton.add(btnAsoCur);
        panelBoton.add(Box.createVerticalStrut(12));
        panelBoton.add(btnAsoPro);
        panelBoton.add(Box.createVerticalStrut(12));
        panelBoton.add(btnReportes);
        panelBoton.add(Box.createVerticalStrut(20));
        panelBoton.add(btnVolver1);
        panelBoton.add(Box.createVerticalGlue());

        ventanaAdministradores.add(panelBoton, BorderLayout.CENTER);
        ventanaAdministradores.setVisible(true);
    }

    // NOTA: Los demás métodos (administradorEstudiantes, administradorProfesores, administradorCursos,
    // ventanaAsociarGruposCursos, ventanaAsociarProfesoresGrupos, registrarEstudiante, registrarProfesor,
    // registrarCurso, mostrarVentanaReportesAdministrador, generarReporteListaEstudiantes,
    // generarReporteEstadisticas, obtenerGruposVigentes, generarIdentificacionUnica)
    // serán movidos aquí desde el archivo principal

    // Placeholder para métodos que serán movidos
    private void administradorEstudiantes() {
        // TODO: Mover desde Sistemadematriculaycalificaciones
    }

    private void administradorProfesores() {
        // TODO: Mover desde Sistemadematriculaycalificaciones
    }

    private void administradorCursos() {
        // TODO: Mover desde Sistemadematriculaycalificaciones
    }

    private void ventanaAsociarGruposCursos() {
        // TODO: Mover desde Sistemadematriculaycalificaciones
    }

    private void ventanaAsociarProfesoresGrupos() {
        // TODO: Mover desde Sistemadematriculaycalificaciones
    }

    private void mostrarVentanaReportesAdministrador() {
        // TODO: Mover desde Sistemadematriculaycalificaciones
    }

    private void generarReporteListaEstudiantes(Date fechaVigencia, int alcance, Cursos cursoEspecifico, Grupos grupoEspecifico) {
        // TODO: Mover desde Sistemadematriculaycalificaciones
    }

    private void generarReporteEstadisticas(Date fechaVigencia, int alcance, Cursos cursoEspecifico, Grupos grupoEspecifico) {
        // TODO: Mover desde Sistemadematriculaycalificaciones
    }

    private List<Grupos> obtenerGruposVigentes(Date fechaVigencia, int alcance, Cursos cursoEspecifico, Grupos grupoEspecifico) {
        // TODO: Mover desde Sistemadematriculaycalificaciones
        return new ArrayList<>();
    }

    private String generarIdentificacionUnica() {
        // TODO: Mover desde Sistemadematriculaycalificaciones
        return "";
    }
}

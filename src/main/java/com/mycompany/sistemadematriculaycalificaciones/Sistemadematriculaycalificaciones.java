/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistemadematriculaycalificaciones;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Sistemadematriculaycalificaciones extends JFrame {
    
    public Sistemadematriculaycalificaciones() {
        // Configurar la ventana principal
        setTitle("Sistema de Matrícula y Calificaciones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        // Crear componentes
        crearInterfaz();
    }
    
    private void crearInterfaz() {
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        
        // Label superior
        JLabel labelTitulo = new JLabel("Seleccione la ventana que desea abrir", JLabel.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelPrincipal.add(labelTitulo, BorderLayout.NORTH);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        // Crear los 3 botones
        JButton btnAdministradores = new JButton("Administradores");
        JButton btnProfesores = new JButton("Profesores");
        JButton btnEstudiantes = new JButton("Estudiantes");
        
        
        
        // Agregar botones al panel
        panelBotones.add(btnAdministradores);
        panelBotones.add(btnProfesores);
        panelBotones.add(btnEstudiantes);
        
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        
        // Agregar action listeners a los botones
        btnAdministradores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaAdministradores();
            }
        });
        
        btnProfesores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaProfesores();
            }
        });
        
        btnEstudiantes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaEstudiantes();
            }
        });
        
        add(panelPrincipal);
    }
    private void abrirVentanaAdministradores() {
        // Cerrar ventana actual
        this.dispose();
        
        // Crear nueva ventana
        JFrame ventanaAdministradores = new JFrame("Ventana de Administradores");
        ventanaAdministradores.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaAdministradores.setSize(400, 300);
        ventanaAdministradores.setLocationRelativeTo(null);
        
        // Agregar label
        JLabel label = new JLabel("Menú de administradores", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        ventanaAdministradores.add(label, BorderLayout.NORTH);
        
        //Botones
        JButton btnVolver1 = new JButton("Regresar");
        btnVolver1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cerrar ventana actual y abrir la principal
                ventanaAdministradores.dispose();
                new Sistemadematriculaycalificaciones().setVisible(true);
            }
        });
        // Panel para el botón
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnVolver1);
        ventanaAdministradores.add(panelBoton, BorderLayout.CENTER);

        ventanaAdministradores.setVisible(true);
        // Crear los botones
       
    }
    private void abrirVentanaProfesores() {
        // Cerrar ventana actual
        this.dispose();
        
        // Crear nueva ventana
        JFrame ventanaProfesores = new JFrame("Ventana de profesores");
        ventanaProfesores.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaProfesores.setSize(400, 300);
        ventanaProfesores.setLocationRelativeTo(null);
        
        // Agregar label
        JLabel label = new JLabel("Menú de profesores", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        ventanaProfesores.add(label, BorderLayout.NORTH);
        
        JButton btnVolver1 = new JButton("Regresar");
        btnVolver1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cerrar ventana actual y abrir la principal
                ventanaProfesores.dispose();
                new Sistemadematriculaycalificaciones().setVisible(true);
            }
        });

        // Panel para el botón
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnVolver1);
        ventanaProfesores.add(panelBoton, BorderLayout.CENTER);

        ventanaProfesores.setVisible(true);
        }
    private void abrirVentanaEstudiantes() {
        // Cerrar ventana actual
        this.dispose();
        
        // Crear nueva ventana
        JFrame ventanaEstudiantes = new JFrame("Ventana de Estudiantes");
        ventanaEstudiantes.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaEstudiantes.setSize(400, 300);
        ventanaEstudiantes.setLocationRelativeTo(null);
        
        // Agregar label
        JLabel label = new JLabel("Menú de Estudiantes", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        ventanaEstudiantes.add(label, BorderLayout.NORTH);
        
        JButton btnVolver1 = new JButton("Regresar");
        btnVolver1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cerrar ventana actual y abrir la principal
                ventanaEstudiantes.dispose();
                new Sistemadematriculaycalificaciones().setVisible(true);
            }
        });
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnVolver1);
        ventanaEstudiantes.add(panelBoton, BorderLayout.CENTER);
        
        ventanaEstudiantes.setVisible(true);
    }
    
   
    
    
    
    public static void main(String[] args) {
        // Ejecutar en el Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Sistemadematriculaycalificaciones().setVisible(true);
            }
        });
    }
}
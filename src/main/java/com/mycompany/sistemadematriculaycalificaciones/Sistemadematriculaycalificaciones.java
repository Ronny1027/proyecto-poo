/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistemadematriculaycalificaciones;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;


public class Sistemadematriculaycalificaciones extends JFrame {
    private String validarTemaInteres(String tema) {
    if (tema == null || tema.trim().isEmpty()) {
        return "Por favor digite algun tema de interes";
    }
    if (tema.length() < 5 || tema.length() > 30) {
        return "Cada tema de interés debe tener entre 5 y 30 caracteres";
    }
    return null;
}
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
            public void actionPerformed(ActionEvent e) {
                abrirVentanaAdministradores();
            }
        });
        
        btnProfesores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirVentanaProfesores();
            }
        });
        
        btnEstudiantes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirVentanaEstudiantes();
            }
        });
        
        add(panelPrincipal);
    }
    private void limpiarCampos(JTextField... campos) {
    for (JTextField campo : campos) {
        campo.setText("");
    }
}
    private java.util.List<Estudiantes> estudiantes = new java.util.ArrayList<>();
    private static final String ARCHIVO_ESTUDIANTES = "estudiantes.dat";
    //Función para guardar la info en un archivo.
    private void guardarEstudiantesEnArchivo() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_ESTUDIANTES))) {
        oos.writeObject(estudiantes);
    } catch (IOException e) {
    }
    }
    //Función para cargar la información del archivo de estudiantes.
    private void cargarEstudiantesDesdeArchivo() {
    File archivo = new File(ARCHIVO_ESTUDIANTES);
    if (!archivo.exists()) {
        estudiantes = new ArrayList<>();
        return;
    }
    
    try {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_ESTUDIANTES));
        estudiantes = (List<Estudiantes>) ois.readObject();
        ois.close();
    } catch (IOException | ClassNotFoundException e) {
        estudiantes = new ArrayList<>();
    }
    }
    
    
    private void registrarEstudiante(JTextField txtNom, JTextField txtApel1, JTextField txtApel2,
                                JTextField txtIdent, JTextField txtTelefono, JTextField txtCorreo,JTextField txtDirec,
                                JTextField txtOrganizacion, JTextField txtTemasInteres, JPasswordField txtContraseña) {
       
        // Verificar si ya existe un estudiante con esa identificación
        for (Estudiantes est : estudiantes) {
            if (est.getIdentificacion().equals(txtIdent.getText().trim())) {
                JOptionPane.showMessageDialog(null, "Ya existe un estudiante con esta identificación");
                return;
            }
        }
        // 
        List<String> temasInteres = new ArrayList<>();
        String[] temasArray = txtTemasInteres.getText().split(",");
        for (String tema : temasArray) {
            String temaLimpio = tema.trim();
            if (!temaLimpio.isEmpty()) {
                temasInteres.add(temaLimpio);
            }
        }
        String contraseña = new String(txtContraseña.getPassword());
        Estudiantes nuevoEstudiante = new Estudiantes(
            txtNom.getText().trim(),
            txtApel1.getText().trim(),
            txtApel2.getText().trim(),
            txtIdent.getText().trim(),
            txtTelefono.getText().trim(),
            txtCorreo.getText().trim(),
            txtDirec.getText().trim(),
            txtOrganizacion.getText().trim(),
            temasInteres,
            contraseña
        );
        List<String> errores = nuevoEstudiante.validarEstudianteCompleto();
        if (!errores.isEmpty()) {
            String mensajeError = String.join("\n", errores);
            JOptionPane.showMessageDialog(null, mensajeError);
            return;
        }
        estudiantes.add(nuevoEstudiante);
        guardarEstudiantesEnArchivo();//Se guarda la lista en el archivo
        limpiarCampos(txtNom, txtApel1, txtApel2, txtIdent, txtTelefono, 
              txtCorreo, txtDirec, txtOrganizacion, txtTemasInteres, txtContraseña);   
}
    private void consultarEstudiante(JTextField txtIdent, JTextField txtNom, JTextField txtApel1, 
                                JTextField txtApel2, JTextField txtTelefono, JTextField txtCorreo,
                                JTextField txtDirec, JTextField txtOrganizacion, JTextField txtTemasInteres, JPasswordField txtContraseña) {
    String identificacion = txtIdent.getText().trim();
    
    // Solo validar que la identificación no esté vacía
    if (identificacion.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Digite una identificación valida");
        return;
    }
    
    // Buscar el estudiante por identificación
    for (Estudiantes est : estudiantes) {
        if (est.getIdentificacion().equals(identificacion)) {
            // Llenar los campos con los datos del estudiante
            txtNom.setText(est.getNombre());
            txtApel1.setText(est.getApellido1());
            txtApel2.setText(est.getApellido2());
            txtTelefono.setText(est.getTelefono());
            txtCorreo.setText(est.getCorreo());
            txtDirec.setText(est.getDireccion());
            txtOrganizacion.setText(est.getOrganizacion());
            txtContraseña.setText("********");
            // Convertir lista de temas a texto separado por comas
            String temasTexto = String.join(", ", est.getTemasInteres());
            txtTemasInteres.setText(temasTexto);
            
            
            
            
            return;
        }
    }
    //Si no se encuentra la identificación
    JOptionPane.showMessageDialog(null, "Estudiante no encontrado");
    }   
    private void modificarEstudiante(JTextField txtNom, JTextField txtApel1, JTextField txtApel2,
                                JTextField txtIdent, JTextField txtTelefono, JTextField txtCorreo,
                                JTextField txtDirec, JTextField txtOrganizacion, JTextField txtTemasInteres, 
                                JPasswordField txtContraseña) {
    
    String identificacion = txtIdent.getText().trim();
    
    if (identificacion.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Digite una identificación valida");
        return;
    }
    
    // Buscar el estudiante a modificar
    for (Estudiantes est : estudiantes) {
        if (est.getIdentificacion().equals(identificacion)) {
            
            // Crear lista de temas actualizada
            List<String> temasInteres = new ArrayList<>();
            String[] temasArray = txtTemasInteres.getText().split(",");
            for (String tema : temasArray) {
                String temaLimpio = tema.trim();
                if (!temaLimpio.isEmpty()) {
                    temasInteres.add(temaLimpio);
                }
            }
            
            // Actualizar los datos del estudiante
            est.setNombre(txtNom.getText().trim());
            est.setApellido1(txtApel1.getText().trim());
            est.setApellido2(txtApel2.getText().trim());
            est.setTelefono(txtTelefono.getText().trim());
            est.setCorreo(txtCorreo.getText().trim());
            est.setDireccion(txtDirec.getText().trim());
            est.setOrganizacion(txtOrganizacion.getText().trim());
            est.setTemasInteres(temasInteres);
            
            // Solo actualizar contraseña si no está vacía
            String nuevaContraseña = new String(txtContraseña.getPassword()).trim();
            if (!nuevaContraseña.isEmpty()) {
                est.setContrasena(nuevaContraseña);
            }
            
            // Validar los nuevos datos
            List<String> errores = est.validarEstudianteCompleto();
            if (!errores.isEmpty()) {
                String mensajeError = String.join("\n", errores);
                JOptionPane.showMessageDialog(null, mensajeError);
                return;
            }
            
            guardarEstudiantesEnArchivo();
            return;
        }
    }
    
    JOptionPane.showMessageDialog(null, "Estudiante no encontrado");
    }
    private void eliminarEstudiante(JTextField txtNom, JTextField txtApel1, JTextField txtApel2,
                                JTextField txtIdent, JTextField txtTelefono, JTextField txtCorreo,
                                JTextField txtDirec, JTextField txtOrganizacion, JTextField txtTemasInteres, 
                                JPasswordField txtContraseña) {
    String identificacion = txtIdent.getText().trim();
    
    if (identificacion.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Digite una identificación valida");
        return;
    }
    
    for (Estudiantes est : estudiantes) {
        if (est.getIdentificacion().equals(identificacion)) {
            estudiantes.remove(est);
            guardarEstudiantesEnArchivo();
           limpiarCampos(txtNom, txtApel1, txtApel2, txtIdent, txtTelefono, 
              txtCorreo, txtDirec, txtOrganizacion, txtTemasInteres, txtContraseña); 
            return;
        }
    }
    
    JOptionPane.showMessageDialog(null, "Estudiante no encontrado");
    }
    private void administradorEstudiantes(){
        this.dispose();
        cargarEstudiantesDesdeArchivo();
        // Crear nueva ventana
        JFrame ventanaAdminEstu = new JFrame("Administración de estudiantes");
        ventanaAdminEstu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaAdminEstu.setSize(500, 650);
        ventanaAdminEstu.setLocationRelativeTo(null);
        
        // Agregar label de titulo
        JLabel label = new JLabel("Administración de estudiantes", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        ventanaAdminEstu.add(label, BorderLayout.NORTH);
        //Panel para poder mostrar el CRUD.
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        // Labels y text box del CRUD
        //Nombre
        JLabel label1 = new JLabel("Nombre");
        label1.setFont(new Font("Arial", Font.BOLD, 14));
        label1.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda

        JTextField txtNom = new JTextField(20);
        txtNom.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtNom.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Apellido 1
        JLabel label2 = new JLabel("Primer Apellido");
        label2.setFont(new Font("Arial", Font.BOLD, 14));
        label2.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda

        JTextField txtApel1 = new JTextField(20);
        txtApel1.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtApel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Apellido 2
        JLabel label3 = new JLabel("Segundo Apellido");
        label3.setFont(new Font("Arial", Font.BOLD, 14));
        label3.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda

        JTextField txtApel2 = new JTextField(20);
        txtApel2.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtApel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Identificación
        JLabel label4 = new JLabel("Identificación");
        label4.setFont(new Font("Arial", Font.BOLD, 14));
        label4.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda

        JTextField txtIdent = new JTextField(20);
        txtIdent.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtIdent.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Teléfono
        JLabel label5 = new JLabel("Número de teléfono");
        label5.setFont(new Font("Arial", Font.BOLD, 14));
        label5.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtTelefono = new JTextField(20);
        txtTelefono.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtTelefono.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Correo electrónico
        JLabel label6 = new JLabel("Correo electrónico");
        label6.setFont(new Font("Arial", Font.BOLD, 14));
        label6.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtCorreo = new JTextField(20);
        txtCorreo.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtCorreo.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Dirección fisica
        JLabel label10 = new JLabel("Dirección fisica");
        label10.setFont(new Font("Arial", Font.BOLD, 14));
        label10.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtDirec = new JTextField(20);
        txtDirec.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtDirec.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Organización donde labora
        JLabel label7 = new JLabel("Organización donde labora");
        label7.setFont(new Font("Arial", Font.BOLD, 14));
        label7.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtOrganizacion = new JTextField(20);
        txtOrganizacion.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtOrganizacion.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Organización donde labora
        JLabel label8 = new JLabel("Temas de interés");
        label8.setFont(new Font("Arial", Font.BOLD, 14));
        label8.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtTemasInteres = new JTextField(20);
        txtTemasInteres.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtTemasInteres.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Contraseña
        JLabel label9 = new JLabel("Contraseña");
        label9.setFont(new Font("Arial", Font.BOLD, 14));
        label9.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JPasswordField txtContraseña = new JPasswordField(20);
        txtContraseña.setMaximumSize(new Dimension(200, 25));
        txtContraseña.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        // Agregar al panel central
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label1);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtNom);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label2);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtApel1);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label3);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtApel2);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label4);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtIdent);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label5);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtTelefono);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label6);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtCorreo);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label10);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtDirec);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label7);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtOrganizacion);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label8);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtTemasInteres);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label9);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtContraseña);
        

        // Panel principal para los botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));

        
        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            registrarEstudiante(txtNom, txtApel1, txtApel2, txtIdent, txtTelefono, 
                              txtCorreo,txtDirec, txtOrganizacion, txtTemasInteres, txtContraseña);
        }});
        JButton btnModificar = new JButton("Modificar");
        btnModificar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            modificarEstudiante(txtNom, txtApel1, txtApel2, txtIdent, txtTelefono, 
                              txtCorreo, txtDirec, txtOrganizacion, txtTemasInteres, txtContraseña);
        }});
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            eliminarEstudiante(txtNom, txtApel1, txtApel2, txtIdent, txtTelefono, 
                              txtCorreo, txtDirec, txtOrganizacion, txtTemasInteres, txtContraseña);
        }});
        JButton btnConsultar = new JButton("Consultar");
        btnConsultar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            consultarEstudiante(txtIdent, txtNom, txtApel1, txtApel2, txtTelefono, 
                              txtCorreo,txtDirec, txtOrganizacion, txtTemasInteres, txtContraseña);
        }});
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            ventanaAdminEstu.dispose();
            abrirVentanaAdministradores(); 
        }});
        
        // Primera fila de botones 
        JPanel panelFila1 = new JPanel();
        panelFila1.setLayout(new FlowLayout());
        panelFila1.add(btnRegistrar);
        panelFila1.add(Box.createHorizontalStrut(20)); // Espacio entre botones
        panelFila1.add(btnModificar);

        // Segunda fila de botones 
        JPanel panelFila2 = new JPanel();
        panelFila2.setLayout(new FlowLayout());
        panelFila2.add(btnEliminar);
        panelFila2.add(Box.createHorizontalStrut(20)); // Espacio entre botones
        panelFila2.add(btnConsultar);
        // Tercera fila (solo Volver centrado)
        JPanel panelFila3 = new JPanel();
        panelFila3.setLayout(new FlowLayout());
        panelFila3.add(btnVolver);

        // Se agregan los paneles
        panelBotones.add(panelFila1);
        panelBotones.add(Box.createVerticalStrut(10)); // Espacio entre filas
        panelBotones.add(panelFila2);
        panelBotones.add(Box.createVerticalStrut(10)); // Espacio entre filas
        panelBotones.add(panelFila3);

        // Agregar el panel de botones al panel central
        panelCentral.add(Box.createVerticalStrut(20)); // Espacio antes de los botones
        panelCentral.add(panelBotones);
        // Agregar el panel central a la ventana
        ventanaAdminEstu.add(panelCentral, BorderLayout.CENTER);
        ventanaAdminEstu.setVisible(true);
    }
    private void administradorProfesores(){
        this.dispose();
        // Crear nueva ventana
        JFrame ventanaAdminProf = new JFrame("Administración de profesores");
        ventanaAdminProf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaAdminProf.setSize(500, 650);
        ventanaAdminProf.setLocationRelativeTo(null);
        
        // Agregar label de titulo
        JLabel label = new JLabel("Administración de profesores", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        ventanaAdminProf.add(label, BorderLayout.NORTH);
        //Panel para poder mostrar el CRUD.
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        // Labels y text box del CRUD
        //Nombre
        JLabel label1 = new JLabel("Nombre");
        label1.setFont(new Font("Arial", Font.BOLD, 14));
        label1.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda

        JTextField txtNom = new JTextField(20);
        txtNom.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtNom.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Apellido 1
        JLabel label2 = new JLabel("Primer Apellido");
        label2.setFont(new Font("Arial", Font.BOLD, 14));
        label2.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda

        JTextField txtApel1 = new JTextField(20);
        txtApel1.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtApel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Apellido 2
        JLabel label3 = new JLabel("Segundo Apellido");
        label3.setFont(new Font("Arial", Font.BOLD, 14));
        label3.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda

        JTextField txtApel2 = new JTextField(20);
        txtApel2.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtApel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Identificación
        JLabel label4 = new JLabel("Identificación");
        label4.setFont(new Font("Arial", Font.BOLD, 14));
        label4.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda

        JTextField txtIdent = new JTextField(20);
        txtIdent.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtIdent.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Teléfono
        JLabel label5 = new JLabel("Número de teléfono");
        label5.setFont(new Font("Arial", Font.BOLD, 14));
        label5.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtTelefono = new JTextField(20);
        txtTelefono.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtTelefono.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Correo electrónico
        JLabel label6 = new JLabel("Correo electrónico");
        label6.setFont(new Font("Arial", Font.BOLD, 14));
        label6.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtCorreo = new JTextField(20);
        txtCorreo.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtCorreo.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Titulos obtenidos
        JLabel label7 = new JLabel("Títulos obtenidos");
        label7.setFont(new Font("Arial", Font.BOLD, 14));
        label7.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtTitulos = new JTextField(20);
        txtTitulos.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtTitulos.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Certificaciones de estudios
        JLabel label8 = new JLabel("Certificaciones de estudios");
        label8.setFont(new Font("Arial", Font.BOLD, 14));
        label8.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtCertificaciones = new JTextField(20);
        txtCertificaciones.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtCertificaciones.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Contraseña
        JLabel label9 = new JLabel("Contraseña");
        label9.setFont(new Font("Arial", Font.BOLD, 14));
        label9.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtContraseña = new JTextField(20);
        txtContraseña.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtContraseña.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Agregar al panel central
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label1);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtNom);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label2);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtApel1);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label3);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtApel2);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label4);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtIdent);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label5);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtTelefono);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label6);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtCorreo);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label7);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtTitulos);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label8);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtCertificaciones);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label9);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtContraseña);
        

        // Panel principal para los botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));

        
        JButton btnAgregar = new JButton("Agregar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnConsultar = new JButton("Consultar");
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            ventanaAdminProf.dispose();
            abrirVentanaAdministradores(); 
        }
        });
        
        // Primera fila de botones 
        JPanel panelFila1 = new JPanel();
        panelFila1.setLayout(new FlowLayout());
        panelFila1.add(btnAgregar);
        panelFila1.add(Box.createHorizontalStrut(20)); // Espacio entre botones
        panelFila1.add(btnModificar);

        // Segunda fila de botones 
        JPanel panelFila2 = new JPanel();
        panelFila2.setLayout(new FlowLayout());
        panelFila2.add(btnEliminar);
        panelFila2.add(Box.createHorizontalStrut(20)); // Espacio entre botones
        panelFila2.add(btnConsultar);
        // Tercera fila (solo Volver centrado)
        JPanel panelFila3 = new JPanel();
        panelFila3.setLayout(new FlowLayout());
        panelFila3.add(btnVolver);

        // Se agregan los paneles
        panelBotones.add(panelFila1);
        panelBotones.add(Box.createVerticalStrut(10)); // Espacio entre filas
        panelBotones.add(panelFila2);
        panelBotones.add(Box.createVerticalStrut(10)); // Espacio entre filas
        panelBotones.add(panelFila3);

        // Agregar el panel de botones al panel central
        panelCentral.add(Box.createVerticalStrut(20)); // Espacio antes de los botones
        panelCentral.add(panelBotones);
        // Agregar el panel central a la ventana
        ventanaAdminProf.add(panelCentral, BorderLayout.CENTER);
        ventanaAdminProf.setVisible(true);
    }
    private void administradorCursos(){
        this.dispose();
        // Crear nueva ventana
        JFrame ventanaAdminCurs = new JFrame("Administración de cursos");
        ventanaAdminCurs.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaAdminCurs.setSize(500, 650);
        ventanaAdminCurs.setLocationRelativeTo(null);
        
        // Agregar label de titulo
        JLabel label = new JLabel("Administración de cursos", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        ventanaAdminCurs.add(label, BorderLayout.NORTH);
        //Panel para poder mostrar el CRUD.
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        // Labels y text box del CRUD
        //Identificación
        JLabel label1 = new JLabel("Identificación del curso");
        label1.setFont(new Font("Arial", Font.BOLD, 14));
        label1.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtIdentCur = new JTextField(20);
        txtIdentCur.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtIdentCur.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Nombre del curso
        JLabel label2 = new JLabel("Nombre del curso");
        label2.setFont(new Font("Arial", Font.BOLD, 14));
        label2.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda

        JTextField txtNomCurso = new JTextField(20);
        txtNomCurso.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtNomCurso.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Descripción del Curso
        JLabel label3 = new JLabel("Descripción del Curso");
        label3.setFont(new Font("Arial", Font.BOLD, 14));
        label3.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda

        JTextField txtDescCur = new JTextField(20);
        txtDescCur.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtDescCur.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Cantidad de horas por día
        JLabel label4 = new JLabel("Cantidad de horas por día");
        label4.setFont(new Font("Arial", Font.BOLD, 14));
        label4.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda

        JTextField txtCantHoras = new JTextField(20);
        txtCantHoras.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtCantHoras.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Modalidad
        JLabel label5 = new JLabel("Modalidad");
        label5.setFont(new Font("Arial", Font.BOLD, 14));
        label5.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        String[] modalidades = {"Presencial", "Virtual", "Virtual asincrónico",
        "Virtual híbrido", "Semipresencial"};
        JComboBox<String> comboModalidad = new JComboBox<>(modalidades);
        comboModalidad.setMaximumSize(new Dimension(200, 25));
        comboModalidad.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Cantidad mínima de estudiantes para abrir un grupo
        JLabel label6 = new JLabel("Cantidad mínima de estudiantes para abrir un grupo");
        label6.setFont(new Font("Arial", Font.BOLD, 14));
        label6.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtEstuMini = new JTextField(20);
        txtEstuMini.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtEstuMini.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Cantidad máxima de estudiantes
        JLabel label7 = new JLabel("Cantidad máxima de estudiantes");
        label7.setFont(new Font("Arial", Font.BOLD, 14));
        label7.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtEstuMax = new JTextField(20);
        txtEstuMax.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtEstuMax.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Tipo de curso
        JLabel label8 = new JLabel("Tipo de curso");
        label8.setFont(new Font("Arial", Font.BOLD, 14));
        label8.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        String[] tiposCurso = {"Teórico", "Práctico","Taller","Seminario",};
        //Teórico/Práctico/Taller/Seminario
        JComboBox<String> comboTipoCurso = new JComboBox<>(tiposCurso);
        comboTipoCurso.setMaximumSize(new Dimension(200, 25));
        comboTipoCurso.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Cantidad máxima de estudiantes
        JLabel label9 = new JLabel("Calificación mínima para aprobar el curso");
        label9.setFont(new Font("Arial", Font.BOLD, 14));
        label9.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtCaliMini = new JTextField(20);
        txtCaliMini.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtCaliMini.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Agregar al panel central
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label1);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtIdentCur);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label2);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtNomCurso);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label3);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtDescCur);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label4);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtCantHoras);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label5);
        panelCentral.add(comboModalidad);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label6);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtEstuMini);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label7);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtEstuMax);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label8);
        panelCentral.add(comboTipoCurso);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label9);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtCaliMini);
        
        // Panel principal para los botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));

        
        JButton btnAgregar = new JButton("Agregar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnConsultar = new JButton("Consultar");
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            ventanaAdminCurs.dispose();
            abrirVentanaAdministradores(); 
        }
        });
        
        // Primera fila de botones 
        JPanel panelFila1 = new JPanel();
        panelFila1.setLayout(new FlowLayout());
        panelFila1.add(btnAgregar);
        panelFila1.add(Box.createHorizontalStrut(20)); // Espacio entre botones
        panelFila1.add(btnModificar);

        // Segunda fila de botones 
        JPanel panelFila2 = new JPanel();
        panelFila2.setLayout(new FlowLayout());
        panelFila2.add(btnEliminar);
        panelFila2.add(Box.createHorizontalStrut(20)); // Espacio entre botones
        panelFila2.add(btnConsultar);
        // Tercera fila (solo Volver centrado)
        JPanel panelFila3 = new JPanel();
        panelFila3.setLayout(new FlowLayout());
        panelFila3.add(btnVolver);

        // Se agregan los paneles
        panelBotones.add(panelFila1);
        panelBotones.add(Box.createVerticalStrut(10)); // Espacio entre filas
        panelBotones.add(panelFila2);
        panelBotones.add(Box.createVerticalStrut(10)); // Espacio entre filas
        panelBotones.add(panelFila3);

        // Agregar el panel de botones al panel central
        panelCentral.add(Box.createVerticalStrut(20)); // Espacio antes de los botones
        panelCentral.add(panelBotones);
        // Agregar el panel central a la ventana
        ventanaAdminCurs.add(panelCentral, BorderLayout.CENTER);
        ventanaAdminCurs.setVisible(true);
    }
    private void abrirVentanaAdministradores() {
        // Cerrar ventana actual
        this.dispose();
        
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
                // Cerrar ventana actual y abrir la principal
                ventanaAdministradores.dispose();
                new Sistemadematriculaycalificaciones().setVisible(true);
            }
        });
        JButton btnEstudiantes = new JButton("Estudiantes");
        btnEstudiantes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Cerrar ventana actual y abrir la principal
                ventanaAdministradores.dispose();
                administradorEstudiantes();
            }
        });
        JButton btnProfesores = new JButton("Profesores");
        btnProfesores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Cerrar ventana actual y abrir la principal
                ventanaAdministradores.dispose();
                administradorProfesores();
            }
        });
        JButton btnCursos = new JButton("Cursos");
        btnCursos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Cerrar ventana actual y abrir la principal
                ventanaAdministradores.dispose();
                administradorCursos();
            }
        });
        JButton btnAsoCur = new JButton("Asociar grupos a cursos");
        JButton btnAsoPro = new JButton("Asociar grupos a profesores");
        // Panel para el botón
        JPanel panelBoton = new JPanel();
        panelBoton.setLayout(new BoxLayout(panelBoton, BoxLayout.Y_AXIS));

        // CENTRAR LOS BOTONES
        btnEstudiantes.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnProfesores.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCursos.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAsoCur.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAsoPro.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver1.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agregar los botones con espacios
        panelBoton.add(Box.createVerticalStrut(30)); // Empuja los botones al centro
        panelBoton.add(btnEstudiantes);
        panelBoton.add(Box.createVerticalStrut(15));
        panelBoton.add(btnProfesores);
        panelBoton.add(Box.createVerticalStrut(15));
        panelBoton.add(btnCursos);
        panelBoton.add(Box.createVerticalStrut(15));
        panelBoton.add(btnAsoCur);
        panelBoton.add(Box.createVerticalStrut(15));
        panelBoton.add(btnAsoPro);
        panelBoton.add(Box.createVerticalStrut(25));
        panelBoton.add(btnVolver1);
        panelBoton.add(Box.createVerticalGlue()); // Empuja los botones al centro

        ventanaAdministradores.add(panelBoton, BorderLayout.CENTER);
        ventanaAdministradores.setVisible(true);
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
            public void run() {
                new Sistemadematriculaycalificaciones().setVisible(true);
            }
        });
    }
}
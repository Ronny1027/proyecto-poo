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
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Random;

public class Sistemadematriculaycalificaciones extends JFrame {
    
    public Sistemadematriculaycalificaciones() {
        // Configurar la ventana principal
        setTitle("Sistema de Matrícula y Calificaciones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        
        //Se inician los archivos
        cargarEstudiantesDesdeArchivo();
        cargarProfesoresDesdeArchivo();
        cargarCursosDesdeArchivo();
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
                mostrarLoginProfesores();
            }
        });

        btnEstudiantes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarLoginEstudiantes();
            }
        });
        
        add(panelPrincipal);
    }
    private void limpiarCampos(JTextField... campos) {
    for (JTextField campo : campos) {
        campo.setText("");
    }
    }
    private void limpiarCamposCursos(JTextField txtIdentCur, JTextField txtNomCurso, JTextField txtDescCur,
                                JTextField txtCantHoras, JTextField txtEstuMini, JTextField txtEstuMax,
                                JTextField txtCaliMini, JComboBox<String> comboModalidad, JComboBox<String> comboTipoCurso) {
    txtIdentCur.setText("");
    txtNomCurso.setText("");
    txtDescCur.setText("");
    txtCantHoras.setText("");
    txtEstuMini.setText("");
    txtEstuMax.setText("");
    txtCaliMini.setText("");
    comboModalidad.setSelectedIndex(0);
    comboTipoCurso.setSelectedIndex(0);
    }
    //Función para correos.
    private void enviarCorreo(String destinatario, String nombre, String identificacion, String operacion, String tipoUsuario) throws MessagingException {
    // Configuración (la misma que ya tienes)
    String host = "smtp.gmail.com";
    String usuario = "ronniadmision@gmail.com";
    String contraseña = "fmfe pkuy xzwj jkls";
    
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.port", "587");
    
    Session session = Session.getInstance(props, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(usuario, contraseña);
        }
    });
    
    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress(usuario));
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
    message.setSubject("Notificación del Sistema - Operación: " + operacion);
    
    String mensaje = "Estimado " + nombre + ",\n\n" +
                    "Se ha realizado la siguiente operación en su cuenta de " + tipoUsuario + ": " + operacion + "\n\n" +
                    "Fecha: " + new Date() + "\n" +
                    "Identificación: " + identificacion + "\n\n" +
                    "Saludos,\nSistema de Matrícula";
    
    message.setText(mensaje);
    Transport.send(message);
    
    }
    
    //Manejo de archivos
    //Estudiantes
    private java.util.List<Estudiantes> estudiantes = new java.util.ArrayList<>();
    private Estudiantes estudianteAutenticado; // Usuario estudiante autenticado
    private String codigoVerificacionEstudiante; // Código de verificación para recuperación
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
        JOptionPane.showMessageDialog(null, "Error al cargar estudiantes: " + e.getMessage());
        estudiantes = new ArrayList<>();
    }
    }
    //Profesores
    private java.util.List<Profesores> profesores = new java.util.ArrayList<>();
    private Profesores profesorAutenticado; // Usuario profesor autenticado
    private String codigoVerificacionProfesor; // Código de verificación para recuperación
    private static final String ARCHIVO_PROFESORES = "profesores.dat";
    //Función para guardar la info en un archivo.
    private void guardarProfesoresEnArchivo() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_PROFESORES))) {
        oos.writeObject(profesores);
    } catch (IOException e) {
    }
    }
    //Función para cargar la información del archivo de profesores.
    private void cargarProfesoresDesdeArchivo() {
    File archivo = new File(ARCHIVO_PROFESORES);
    if (!archivo.exists()) {
        profesores = new ArrayList<>();
        return;
    }
    
    try {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_PROFESORES));
        profesores = (List<Profesores>) ois.readObject();
        ois.close();
    } catch (IOException | ClassNotFoundException e) {
        profesores = new ArrayList<>();
    }
    }
    //Cursos
    private java.util.List<Cursos> cursos = new java.util.ArrayList<>();
    private static final String ARCHIVO_CURSOS = "cursos.dat";
    //Función para guardar la info en un archivo.
    private void guardarCursosEnArchivo() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_CURSOS))) {
        oos.writeObject(cursos);
    } catch (IOException e) {
    }
    }
    //Función para cargar la información del archivo de cursos.
    private void cargarCursosDesdeArchivo() {
    File archivo = new File(ARCHIVO_CURSOS);
    if (!archivo.exists()) {
        cursos = new ArrayList<>();
        return;
    }
    
    try {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_CURSOS));
        cursos = (List<Cursos>) ois.readObject();
        ois.close();
    } catch (IOException | ClassNotFoundException e) {
        cursos = new ArrayList<>();
    }
    }
    //Evaluaciones
    private java.util.List<Evaluaciones> evaluaciones = new java.util.ArrayList<>();
    private static final String ARCHIVO_EVALUACIONES = "evaluaciones.dat";
    
    private void guardarEvaluacionesEnArchivo() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_EVALUACIONES))) {
        oos.writeObject(evaluaciones);
    } catch (IOException e) {
    }
    }
    //Función para cargar la información del archivo de cursos.
    private void cargarEvaluacionesDesdeArchivo() {
    File archivo = new File(ARCHIVO_EVALUACIONES);
    if (!archivo.exists()) {
        evaluaciones = new ArrayList<>();
        return;
    }
    
    try {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_EVALUACIONES));
        evaluaciones = (List<Evaluaciones>) ois.readObject();
        ois.close();
    } catch (IOException | ClassNotFoundException e) {
        evaluaciones = new ArrayList<>();
    }
    }

    private void registrarEstudiante(JTextField txtNom, JTextField txtApel1, JTextField txtApel2,
                                JTextField txtIdent, JTextField txtTelefono, JTextField txtCorreo,JTextField txtDirec,
                                JTextField txtOrganizacion, JTextField txtTemasInteres, JPasswordField txtContraseña) {
       
        // Se verifica si ya existe un estudiante con esa identificación
        for (Estudiantes est : estudiantes) {
            if (est.getIdentificacion().equals(txtIdent.getText().trim())) {
                JOptionPane.showMessageDialog(null, "Ya hay un estudiante registrado con esta identificación","Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        //Se parte el string para generar la lista
        List<String> temasInteres = new ArrayList<>();
        String[] temasArray = txtTemasInteres.getText().split(",");
        for (String tema : temasArray) {
            String temaLimpio = tema.trim();
            if (!temaLimpio.isEmpty()) {
                temasInteres.add(temaLimpio);
            }
        }
        
        String contraseña = new String(txtContraseña.getPassword());
        //Se crea un nuevo Estudiante
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
        //Se buscan los errores
        List<String> errores = nuevoEstudiante.validarEstudianteCompleto();
        if (!errores.isEmpty()) {
            String mensajeError = String.join("\n", errores);
            JOptionPane.showMessageDialog(null, mensajeError);
            return;
        }
        estudiantes.add(nuevoEstudiante);
        guardarEstudiantesEnArchivo();//Si no hay errores y el programa sigue entonces se guarda.
        limpiarCampos(txtNom, txtApel1, txtApel2, txtIdent, txtTelefono, 
              txtCorreo, txtDirec, txtOrganizacion, txtTemasInteres, txtContraseña);  
        try {
            enviarCorreo(
                nuevoEstudiante.getCorreo(),
                nuevoEstudiante.getNombre() + " " + nuevoEstudiante.getApellido1(),
                nuevoEstudiante.getIdentificacion(),
                "REGISTRO EXITOSO",
                "ESTUDIANTE"
            );
        } catch (MessagingException e) {
            System.out.println("No se pudo enviar correo: " + e.getMessage());
        }
    }       
    private void consultarEstudiante(JTextField txtIdent, JTextField txtNom, JTextField txtApel1, 
                                JTextField txtApel2, JTextField txtTelefono, JTextField txtCorreo,
                                JTextField txtDirec, JTextField txtOrganizacion, JTextField txtTemasInteres, JPasswordField txtContraseña) {
    String identificacion = txtIdent.getText().trim();
    
    // Solo se debe validar la identificación
    if (identificacion.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Por favor digite una identificación","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Se busca el estudiante en la lista
    for (Estudiantes est : estudiantes) {
        if (est.getIdentificacion().equals(identificacion)) {
            // La info del estudiante se llena
            txtNom.setText(est.getNombre());
            txtApel1.setText(est.getApellido1());
            txtApel2.setText(est.getApellido2());
            txtTelefono.setText(est.getTelefono());
            txtCorreo.setText(est.getCorreo());
            txtDirec.setText(est.getDireccion());
            txtOrganizacion.setText(est.getOrganizacion());
            txtContraseña.setText(""); 
            // Los temas se agregan con las comas.
            String temasTexto = String.join(", ", est.getTemasInteres());
            txtTemasInteres.setText(temasTexto);
            return;
        }
    }
    //Si no se encuentra la identificación
    JOptionPane.showMessageDialog(null, "No hay ningun estudiante registrado con esta identificación","Error", JOptionPane.ERROR_MESSAGE);
    }   
    private void modificarEstudiante(JTextField txtNom, JTextField txtApel1, JTextField txtApel2,
                                JTextField txtIdent, JTextField txtTelefono, JTextField txtCorreo,
                                JTextField txtDirec, JTextField txtOrganizacion, JTextField txtTemasInteres, 
                                JPasswordField txtContraseña) {
    
    String identificacion = txtIdent.getText().trim();
    
    if (identificacion.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Por favor digite una identificación","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Se busca el estudiante que se va a modificar
    for (Estudiantes est : estudiantes) {
        if (est.getIdentificacion().equals(identificacion)) {
            
            // Se crea una nueva lista de temas por si acaso.
            List<String> temasInteres = new ArrayList<>();
            String[] temasArray = txtTemasInteres.getText().split(",");
            for (String tema : temasArray) {
                String temaLimpio = tema.trim();
                if (!temaLimpio.isEmpty()) {
                    temasInteres.add(temaLimpio);
                }
            }
            
            // Los datos del estudiante se actualizan
            est.setNombre(txtNom.getText().trim());
            est.setApellido1(txtApel1.getText().trim());
            est.setApellido2(txtApel2.getText().trim());
            est.setTelefono(txtTelefono.getText().trim());
            est.setCorreo(txtCorreo.getText().trim());
            est.setDireccion(txtDirec.getText().trim());
            est.setOrganizacion(txtOrganizacion.getText().trim());
            est.setTemasInteres(temasInteres);
            
           
            String nuevaContraseña = new String(txtContraseña.getPassword()).trim();
            if (!nuevaContraseña.isEmpty()) {
                // Solo validar si se ingresó nueva contraseña
                String errorContraseña = est.validarContrasena();
                if (errorContraseña != null) {
                    JOptionPane.showMessageDialog(null, errorContraseña);
                    return;
                }
                est.setContrasena(nuevaContraseña);
            }
            
            // Se hace la validación de los errores
            List<String> errores = est.validarEstudianteCompleto();
            if (!errores.isEmpty()) {
                String mensajeError = String.join("\n", errores);
                JOptionPane.showMessageDialog(null, mensajeError);
                return;
            }
            
            guardarEstudiantesEnArchivo();
            try {
            enviarCorreo(
            est.getCorreo(),
            est.getNombre() + " " + est.getApellido1(),
            est.getIdentificacion(),
            "ACTUALIZACIÓN DE DATOS", 
            "ESTUDIANTE"
            );
            } catch (MessagingException e) {
                System.out.println("No se pudo enviar correo: " + e.getMessage());
            }
            return;
        }
    }
    
    JOptionPane.showMessageDialog(null, "No hay ningun estudiante registrado con esta identificación","Error", JOptionPane.ERROR_MESSAGE);
    }
    private void eliminarEstudiante(JTextField txtNom, JTextField txtApel1, JTextField txtApel2,
                                JTextField txtIdent, JTextField txtTelefono, JTextField txtCorreo,
                                JTextField txtDirec, JTextField txtOrganizacion, JTextField txtTemasInteres, 
                                JPasswordField txtContraseña) {
    String identificacion = txtIdent.getText().trim();
    
    if (identificacion.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Por favor digite una identificación","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    for (Estudiantes est : estudiantes) {
        if (est.getIdentificacion().equals(identificacion)) {
            try {
            enviarCorreo(
            est.getCorreo(),
            est.getNombre() + " " + est.getApellido1(),
            est.getIdentificacion(),
            "ELIMINACIÓN DE CUENTA",
            "ESTUDIANTE"
            );
            } catch (MessagingException e) {
                System.out.println("No se pudo enviar correo: " + e.getMessage());
            }
            estudiantes.remove(est);
            guardarEstudiantesEnArchivo();
           limpiarCampos(txtNom, txtApel1, txtApel2, txtIdent, txtTelefono, 
              txtCorreo, txtDirec, txtOrganizacion, txtTemasInteres, txtContraseña); 
            return;
        }
    }
    
    JOptionPane.showMessageDialog(null, "No hay ningun estudiante registrado con esta identificación","Error", JOptionPane.ERROR_MESSAGE);
    }
    private void administradorEstudiantes(){
        this.dispose();
        
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
    private void registrarProfesor(JTextField txtNom, JTextField txtApel1, JTextField txtApel2,
                              JTextField txtIdent, JTextField txtTelefono, JTextField txtCorreo, 
                              JTextField txtDirec, JTextField txtTitulos, JTextField txtCertificaciones, 
                              JPasswordField txtContraseña) {
   
    // Se verifica que no hay otro profesor con esa identificacion
    for (Profesores prof : profesores) {
        if (prof.getIdentificacion().equals(txtIdent.getText().trim())) {
            JOptionPane.showMessageDialog(null, "Ya hay un profesor registrado con esta identificación","Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    
    // Crear lista de titulos
    List<String> titulos = new ArrayList<>();
    String[] titulosArray = txtTitulos.getText().split(",");
    for (String titulo : titulosArray) {
        String tituloLimpio = titulo.trim();
        if (!tituloLimpio.isEmpty()) {
            titulos.add(tituloLimpio);
        }
    }
    
    // Crear lista de certificaciones
    List<String> certificaciones = new ArrayList<>();
    String[] certArray = txtCertificaciones.getText().split(",");
    for (String cert : certArray) {
        String certLimpio = cert.trim();
        if (!certLimpio.isEmpty()) {
            certificaciones.add(certLimpio);
        }
    }
    
    String contraseña = new String(txtContraseña.getPassword());
    Profesores nuevoProfesor = new Profesores(
        txtNom.getText().trim(),
        txtApel1.getText().trim(),
        txtApel2.getText().trim(),
        txtIdent.getText().trim(),
        txtTelefono.getText().trim(),
        txtCorreo.getText().trim(),
        txtDirec.getText().trim(),
        titulos,
        certificaciones,
        contraseña
    );
    
    List<String> errores = nuevoProfesor.validarProfesorCompleto();
    if (!errores.isEmpty()) {
        String mensajeError = String.join("\n", errores);
        JOptionPane.showMessageDialog(null, mensajeError);
        return;
    }
    
    profesores.add(nuevoProfesor);
    guardarProfesoresEnArchivo();
    try {
        enviarCorreo(
        nuevoProfesor.getCorreo(),
        nuevoProfesor.getNombre() + " " + nuevoProfesor.getApellido1(),
        nuevoProfesor.getIdentificacion(),
        "REGISTRO EXITOSO",
        "PROFESOR"
        );
    } catch (MessagingException e) {
        System.out.println("No se pudo enviar correo: " + e.getMessage());
    }
    limpiarCampos(txtNom, txtApel1, txtApel2, txtIdent, txtTelefono, 
                  txtCorreo, txtDirec, txtTitulos, txtCertificaciones, txtContraseña);   
    }
    private void consultarProfesor( JTextField txtNom, JTextField txtApel1, 
                              JTextField txtApel2,JTextField txtIdent, JTextField txtTelefono, JTextField txtCorreo,
                              JTextField txtDirec, JTextField txtTitulos, JTextField txtCertificaciones, 
                              JPasswordField txtContraseña) {
    String identificacion = txtIdent.getText().trim();
    
    if (identificacion.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Por favor digite una identificación","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    for (Profesores prof : profesores) {
        if (prof.getIdentificacion().equals(identificacion)) {
            txtNom.setText(prof.getNombre());
            txtApel1.setText(prof.getApellido1());
            txtApel2.setText(prof.getApellido2());
            txtTelefono.setText(prof.getTelefono());
            txtCorreo.setText(prof.getCorreo());
            txtDirec.setText(prof.getDireccion());
            txtContraseña.setText("");
            
 
            String titulosTexto = String.join(", ", prof.getTitulosobtenidos());
            txtTitulos.setText(titulosTexto);
            
            String certsTexto = String.join(", ", prof.getCertificaciones());
            txtCertificaciones.setText(certsTexto);
            
            return;
        }
    }
    
    JOptionPane.showMessageDialog(null, "No hay ningun profesor registrado con esa identificacion","Error", JOptionPane.ERROR_MESSAGE);
    }
    private void modificarProfesor(JTextField txtNom, JTextField txtApel1, JTextField txtApel2,
                              JTextField txtIdent, JTextField txtTelefono, JTextField txtCorreo,
                              JTextField txtDirec, JTextField txtTitulos, JTextField txtCertificaciones, 
                              JPasswordField txtContraseña) {
    
    String identificacion = txtIdent.getText().trim();
    
    if (identificacion.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Por favor digite una identificación","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    for (Profesores prof : profesores) {
        if (prof.getIdentificacion().equals(identificacion)) {
            
            // Procesar títulos actualizados
            List<String> titulos = new ArrayList<>();
            String[] titulosArray = txtTitulos.getText().split(",");
            for (String titulo : titulosArray) {
                String tituloLimpio = titulo.trim();
                if (!tituloLimpio.isEmpty()) {
                    titulos.add(tituloLimpio);
                }
            }
            
            // Procesar certificaciones actualizadas
            List<String> certificaciones = new ArrayList<>();
            String[] certArray = txtCertificaciones.getText().split(",");
            for (String cert : certArray) {
                String certLimpio = cert.trim();
                if (!certLimpio.isEmpty()) {
                    certificaciones.add(certLimpio);
                }
            }
            
            // Actualizar datos
            prof.setNombre(txtNom.getText().trim());
            prof.setApellido1(txtApel1.getText().trim());
            prof.setApellido2(txtApel2.getText().trim());
            prof.setTelefono(txtTelefono.getText().trim());
            prof.setCorreo(txtCorreo.getText().trim());
            prof.setDireccion(txtDirec.getText().trim());
            prof.setTitulosobtenidos(titulos);
            prof.setCertificaciones(certificaciones);
            
    
             String nuevaContraseña = new String(txtContraseña.getPassword()).trim();
            if (!nuevaContraseña.isEmpty()) {
                // Solo validar la contraseña si el usuario ingresó una nueva
                String errorContraseña = prof.validarContrasena();
                if (errorContraseña != null) {
                    JOptionPane.showMessageDialog(null, errorContraseña);
                    return;
                }
                prof.setContrasena(nuevaContraseña);
            }
            
            // Validar
            List<String> errores = prof.validarProfesorCompleto();
            if (!errores.isEmpty()) {
                String mensajeError = String.join("\n", errores);
                JOptionPane.showMessageDialog(null, mensajeError);
                return;
            }
            
            guardarProfesoresEnArchivo();
            try {
                enviarCorreo(
                prof.getCorreo(),
                prof.getNombre() + " " + prof.getApellido1(),
                prof.getIdentificacion(),
                "ACTUALIZACIÓN DE DATOS",
                "PROFESOR"
                );
            } catch (MessagingException e) {
                System.out.println("No se pudo enviar correo: " + e.getMessage());
            }
                return;
            }
    }
    
    JOptionPane.showMessageDialog(null, "No hay ningun profesor registrado con esa identificacion","Error", JOptionPane.ERROR_MESSAGE);
    }
    private void eliminarProfesor(JTextField txtNom, JTextField txtApel1, JTextField txtApel2,
                             JTextField txtIdent, JTextField txtTelefono, JTextField txtCorreo,
                             JTextField txtDirec, JTextField txtTitulos, JTextField txtCertificaciones, 
                             JPasswordField txtContraseña) {
    String identificacion = txtIdent.getText().trim();
    
    if (identificacion.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Por favor digite una identificación","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    for (Profesores prof : profesores) {
        if (prof.getIdentificacion().equals(identificacion)) {
            try {
                enviarCorreo(
                prof.getCorreo(),
                prof.getNombre() + " " + prof.getApellido1(),
                prof.getIdentificacion(),
                "ELIMINACIÓN DE CUENTA",
                "PROFESOR"
            );
            } catch (MessagingException e) {
                System.out.println("No se pudo enviar correo: " + e.getMessage());
            }
            profesores.remove(prof);
            guardarProfesoresEnArchivo();
            limpiarCampos(txtNom, txtApel1, txtApel2, txtIdent, txtTelefono, 
                         txtCorreo, txtDirec, txtTitulos, txtCertificaciones, txtContraseña);
            return;
        }
    }
    
    JOptionPane.showMessageDialog(null, "No hay ningun profesor registrado con esa identificacion","Error", JOptionPane.ERROR_MESSAGE);
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
        
        JPasswordField txtContraseña = new JPasswordField(20);
        txtContraseña.setMaximumSize(new Dimension(200, 25));
        txtContraseña.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Direc
        JLabel label10 = new JLabel("Dirección");
        label10.setFont(new Font("Arial", Font.BOLD, 14));
        label10.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtDirec = new JTextField(20);
        txtDirec.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtDirec.setAlignmentX(Component.CENTER_ALIGNMENT);
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

        
        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            registrarProfesor(txtNom, txtApel1, txtApel2, txtIdent, txtTelefono, 
                              txtCorreo,txtDirec, txtTitulos, txtCertificaciones, txtContraseña);
        }});
        JButton btnModificar = new JButton("Modificar");
        btnModificar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            modificarProfesor(txtNom, txtApel1, txtApel2, txtIdent, txtTelefono, 
                              txtCorreo,txtDirec, txtTitulos, txtCertificaciones, txtContraseña);
        }});
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            eliminarProfesor(txtNom, txtApel1, txtApel2, txtIdent, txtTelefono, 
                              txtCorreo,txtDirec, txtTitulos, txtCertificaciones, txtContraseña);
        }});
        JButton btnConsultar = new JButton("Consultar");
        btnConsultar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            consultarProfesor(txtNom, txtApel1, txtApel2, txtIdent, txtTelefono, 
                              txtCorreo,txtDirec, txtTitulos, txtCertificaciones, txtContraseña);
        }});
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
        ventanaAdminProf.add(panelCentral, BorderLayout.CENTER);
        ventanaAdminProf.setVisible(true);
    }
    //Para validar que se estan recibiendo numeros
    private boolean esNumero(String texto) {
    if (texto == null || texto.trim().isEmpty()) {
        return false;
    }
    // Verificar que todos los caracteres sean dígitos
    for (char c : texto.toCharArray()) {
        if (!Character.isDigit(c)) {
            return false;
        }
    }
    return true;
    }
    private void registrarCurso(JTextField txtIdentCur, JTextField txtNomCurso, JTextField txtDescCur,
                           JTextField txtCantHoras, JComboBox<String> comboModalidad,
                           JTextField txtEstuMini, JTextField txtEstuMax, JComboBox<String> comboTipoCurso,
                           JTextField txtCaliMini) {
   
    for (Cursos curso : cursos) {
        if (curso.getIdentificacion().equals(txtIdentCur.getText().trim())) {
            JOptionPane.showMessageDialog(null, "Ya hay un curso registrado con esta identificación","Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    if (txtIdentCur.getText().trim().isEmpty() ||
        txtNomCurso.getText().trim().isEmpty() ||
        txtDescCur.getText().trim().isEmpty() ||
        txtCantHoras.getText().trim().isEmpty() ||
        txtEstuMini.getText().trim().isEmpty() ||
        txtEstuMax.getText().trim().isEmpty() ||
        txtCaliMini.getText().trim().isEmpty()) {
        
        JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    
    
    // Se validan los campos que deben ser número antes de todo
    if (!esNumero(txtCantHoras.getText().trim())) {
        JOptionPane.showMessageDialog(null, "Las horas por día deben ser un número válido","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (!esNumero(txtEstuMini.getText().trim())) {
        JOptionPane.showMessageDialog(null, "El mínimo de estudiantes debe ser un número válido","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (!esNumero(txtEstuMax.getText().trim())) {
        JOptionPane.showMessageDialog(null, "El máximo de estudiantes debe ser un número válido","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (!esNumero(txtCaliMini.getText().trim())) {
        JOptionPane.showMessageDialog(null, "La calificación mínima debe ser un número válido","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Obtener valores de los ComboBox
    String modalidad = (String) comboModalidad.getSelectedItem();
    String tipoCurso = (String) comboTipoCurso.getSelectedItem();
    
    // 
    int horasDia = Integer.parseInt(txtCantHoras.getText().trim());
    int minEstu = Integer.parseInt(txtEstuMini.getText().trim());
    int maxEstu = Integer.parseInt(txtEstuMax.getText().trim());
    int calificacionMinima = Integer.parseInt(txtCaliMini.getText().trim());
    
    // Crear nuevo curso
    Cursos nuevoCurso = new Cursos(
        txtIdentCur.getText().trim(),
        txtNomCurso.getText().trim(),
        txtDescCur.getText().trim(),
        horasDia,
        modalidad,
        minEstu,
        maxEstu,
        tipoCurso,
        calificacionMinima
    );
    
    // Validar el curso
    List<String> errores = nuevoCurso.validarCursoCompleto();
    if (!errores.isEmpty()) {
        String mensajeError = String.join("\n", errores);
        JOptionPane.showMessageDialog(null, mensajeError);
        return;
    }
    
    cursos.add(nuevoCurso);
    guardarCursosEnArchivo();
    limpiarCamposCursos(txtIdentCur, txtNomCurso, txtDescCur, txtCantHoras, txtEstuMini, 
                       txtEstuMax, txtCaliMini, comboModalidad, comboTipoCurso);
    
    }
    private void seleccionarEnComboBox(JComboBox<String> comboBox, String valor) {
    if (valor == null) return;
    
    // Intentar selección exacta primero
    comboBox.setSelectedItem(valor);
    
    // Si no funcionó, buscar insensible a mayúsculas
    if (comboBox.getSelectedItem() == null || !comboBox.getSelectedItem().equals(valor)) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).equalsIgnoreCase(valor)) {
                comboBox.setSelectedIndex(i);
                break;
            }
        }
    }
    }
    private void consultarCurso(JTextField txtIdentCur, JTextField txtNomCurso, JTextField txtDescCur,
                           JTextField txtCantHoras, JComboBox<String> comboModalidad,
                           JTextField txtEstuMini, JTextField txtEstuMax, JComboBox<String> comboTipoCurso,
                           JTextField txtCaliMini) {
    String identificacion = txtIdentCur.getText().trim();
    
    if (identificacion.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Por favor digite una identificación de curso","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    for (Cursos curso : cursos) {
        if (curso.getIdentificacion().equals(identificacion)) {
            txtNomCurso.setText(curso.getNombre());
            txtDescCur.setText(curso.getDescripcion());
            txtCantHoras.setText(String.valueOf(curso.getHorasPorDia())); 
            txtEstuMini.setText(String.valueOf(curso.getMinEstudiantes())); 
            txtEstuMax.setText(String.valueOf(curso.getMaxEstudiantes())); 
            txtCaliMini.setText(String.valueOf(curso.getCalificacionMinima()));
            
            // Uso de otro metodo para colocar las opciones en el combo
            seleccionarEnComboBox(comboModalidad, curso.getModalidad());
            seleccionarEnComboBox(comboTipoCurso, curso.getTipo());
            
            return;
        }
    }
    
    JOptionPane.showMessageDialog(null, "No hay un curso registrado con esa identificacion","Error", JOptionPane.ERROR_MESSAGE);
    }
    private void modificarCurso(JTextField txtIdentCur, JTextField txtNomCurso, JTextField txtDescCur,
                           JTextField txtCantHoras, JComboBox<String> comboModalidad,
                           JTextField txtEstuMini, JTextField txtEstuMax, JComboBox<String> comboTipoCurso,
                           JTextField txtCaliMini) {
    
    String identificacion = txtIdentCur.getText().trim();
    
    if (identificacion.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Digite una identificación válida","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    

    if (txtNomCurso.getText().trim().isEmpty() ||
        txtDescCur.getText().trim().isEmpty() ||
        txtCantHoras.getText().trim().isEmpty() ||
        txtEstuMini.getText().trim().isEmpty() ||
        txtEstuMax.getText().trim().isEmpty() ||
        txtCaliMini.getText().trim().isEmpty()) {
        
        JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // VALIDAR QUE LOS CAMPOS NUMÉRICOS CONTENGAN SOLO NÚMEROS
    if (!esNumero(txtCantHoras.getText().trim())) {
        JOptionPane.showMessageDialog(null, "Las horas por día deben ser un número válido","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (!esNumero(txtEstuMini.getText().trim())) {
        JOptionPane.showMessageDialog(null, "El mínimo de estudiantes debe ser un número válido","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (!esNumero(txtEstuMax.getText().trim())) {
        JOptionPane.showMessageDialog(null, "El máximo de estudiantes debe ser un número válido","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (!esNumero(txtCaliMini.getText().trim())) {
        JOptionPane.showMessageDialog(null, "La calificación mínima debe ser un número válido","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    for (Cursos curso : cursos) {
        if (curso.getIdentificacion().equals(identificacion)) {
            
 
            String modalidad = (String) comboModalidad.getSelectedItem();
            String tipoCurso = (String) comboTipoCurso.getSelectedItem();
            

            int horasDia = Integer.parseInt(txtCantHoras.getText().trim());
            int minEstu = Integer.parseInt(txtEstuMini.getText().trim());
            int maxEstu = Integer.parseInt(txtEstuMax.getText().trim());
            int calificacionMinima = Integer.parseInt(txtCaliMini.getText().trim());
            

            curso.setNombre(txtNomCurso.getText().trim());
            curso.setDescripcion(txtDescCur.getText().trim());
            curso.setHorasPorDia(horasDia);
            curso.setModalidad(modalidad);
            curso.setMinEstudiantes(minEstu);
            curso.setMaxEstudiantes(maxEstu);
            curso.setTipo(tipoCurso);
            curso.setCalificacionMinima(calificacionMinima);
            

            List<String> errores = curso.validarCursoCompleto();
            if (!errores.isEmpty()) {
                String mensajeError = String.join("\n", errores);
                JOptionPane.showMessageDialog(null, mensajeError);
                return;
            }
            
            guardarCursosEnArchivo();
            JOptionPane.showMessageDialog(null, "Curso modificado exitosamente");
            return;
        }
    }
    
    JOptionPane.showMessageDialog(null, "No hay un curso registrado con esta identificación");
}
    private void eliminarCurso(JTextField txtIdentCur, JTextField txtNomCurso, JTextField txtDescCur,
                          JTextField txtCantHoras, JComboBox<String> comboModalidad,
                          JTextField txtEstuMini, JTextField txtEstuMax, JComboBox<String> comboTipoCurso,
                          JTextField txtCaliMini) {
    String identificacion = txtIdentCur.getText().trim();
    
    if (identificacion.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Por favor digite una identificación de curso","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    for (Cursos curso : cursos) {
        if (curso.getIdentificacion().equals(identificacion)) {
            cursos.remove(curso);
            guardarCursosEnArchivo();
            limpiarCamposCursos(txtIdentCur, txtNomCurso, txtDescCur, txtCantHoras, txtEstuMini, 
                               txtEstuMax, txtCaliMini, comboModalidad, comboTipoCurso);
            return;
        }
    }
    
    JOptionPane.showMessageDialog(null, "No hay un curso registrado con esa identificación");
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

        
        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            registrarCurso(txtIdentCur, txtNomCurso, txtDescCur, txtCantHoras, comboModalidad, 
                              txtEstuMini,txtEstuMax, comboTipoCurso, txtCaliMini);
        }});
        JButton btnModificar = new JButton("Modificar");
        btnModificar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            modificarCurso(txtIdentCur, txtNomCurso, txtDescCur, txtCantHoras, comboModalidad, 
                              txtEstuMini,txtEstuMax, comboTipoCurso, txtCaliMini);
        }});
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            eliminarCurso(txtIdentCur, txtNomCurso, txtDescCur, txtCantHoras, comboModalidad, 
                              txtEstuMini,txtEstuMax, comboTipoCurso, txtCaliMini);
        }});
        JButton btnConsultar = new JButton("Consultar");
        btnConsultar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            consultarCurso(txtIdentCur, txtNomCurso, txtDescCur, txtCantHoras, comboModalidad, 
                              txtEstuMini,txtEstuMax, comboTipoCurso, txtCaliMini);
        }});
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
        ventanaAdminCurs.add(panelCentral, BorderLayout.CENTER);
        ventanaAdminCurs.setVisible(true);
    }
    private void asignarGrupoACurso(JTextField txtIdentGru, JTextField txtFechaIni, 
                               JTextField txtFechaFin, JTextField txtIdentCur, 
                               JFrame ventana) {
    
    // Obtener valores de los campos
    String identGrupoStr = txtIdentGru.getText().trim();
    String fechaIniStr = txtFechaIni.getText().trim();
    String fechaFinStr = txtFechaFin.getText().trim();
    String identCurso = txtIdentCur.getText().trim();
    
    // Validar campos vacíos
    if (identGrupoStr.isEmpty() || fechaIniStr.isEmpty() || fechaFinStr.isEmpty() || identCurso.isEmpty()) {
        JOptionPane.showMessageDialog(ventana, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Validar que la identificación del grupo sea un número
    if (!esNumero(identGrupoStr)) {
    JOptionPane.showMessageDialog(ventana, "La identificación del grupo debe ser un número", "Error", JOptionPane.ERROR_MESSAGE);
    return;
    }
    int identGrupo = Integer.parseInt(identGrupoStr);//Se transforma a int
    
    // Se validan las fechas usando try, si a la hora de convertir ocurre un error se vuelven a pedir las fechas.
    Date fechaInicio;
    Date fechaFin;
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        fechaInicio = sdf.parse(fechaIniStr);
        fechaFin = sdf.parse(fechaFinStr);
    } catch (ParseException e) {
        JOptionPane.showMessageDialog(ventana, "Formato de fecha inválido. Use dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Se busca el curso y se le asigna a una variable.
    Cursos cursoEncontrado = null;
    for (Cursos curso : cursos) {
        if (curso.getIdentificacion().equals(identCurso)) {
            cursoEncontrado = curso;
            break;
        }
    }
    //Se valida que el curso este.
    if (cursoEncontrado == null) {
        JOptionPane.showMessageDialog(ventana, "No se encontró un curso con la identificación: " + identCurso, "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Se crea el grupo y se valida que no haya otro grupo con esa identificación en este curso.
    for (Grupos grupoExistente : cursoEncontrado.getGrupos()) {
        if (grupoExistente.getIdentificacionGrupo() == identGrupo) {
            JOptionPane.showMessageDialog(ventana, 
                "Ya existe un grupo con identificación " + identGrupo + " en este curso", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    //Se crea el nuevo objeto
    Grupos nuevoGrupo = new Grupos(identGrupo, fechaInicio, fechaFin, cursoEncontrado);
    //Se valida todo el grupo, validaciones extra como de las fechas y la identificación.
    List<String> errores = nuevoGrupo.validarGrupoCompleto();
    //Si hay errores entonces se muestra en un cuadro.
    if (!errores.isEmpty()) {
        String mensajeError = "Errores en el grupo:\n• " + String.join("\n• ", errores);
        JOptionPane.showMessageDialog(ventana, mensajeError, "Error de validación", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Asignar grupo al curso
        cursoEncontrado.agregarGrupo(nuevoGrupo);
        guardarCursosEnArchivo();
        
        
        // Limpiar campos
        txtIdentGru.setText("");
        txtFechaIni.setText("");
        txtFechaFin.setText("");
        txtIdentCur.setText("");
        
    
}
    private void ventanaAsociarGruposCursos() {
        // Cerrar ventana actual
        this.dispose();
        
        
        // Crear nueva ventana
        JFrame ventanaAsoGruCur = new JFrame("Asociar grupos a los cursos");
        ventanaAsoGruCur.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaAsoGruCur.setSize(330, 350);
        ventanaAsoGruCur.setLocationRelativeTo(null);
        
        
        // Agregar label de titulo
        JLabel label = new JLabel("Asociar grupos a los cursos", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        ventanaAsoGruCur.add(label, BorderLayout.NORTH);
        // Labels y text box necesarios.
        //Panel para poder mostrar la info.
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        // Labels y text box de los elementos.
        //Identificación
        JLabel label1 = new JLabel("Identificación del grupo");
        label1.setFont(new Font("Arial", Font.BOLD, 14));
        label1.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtIdentGru = new JTextField(20);
        txtIdentGru.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtIdentGru.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        //Fecha de inicio
        JLabel label2 = new JLabel("Fecha de inicio del grupo (dd/MM/yyyy)");
        label2.setFont(new Font("Arial", Font.BOLD, 14));
        label2.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtFechaIni = new JTextField(20);
        txtFechaIni.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtFechaIni.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Fecha de finalización
        JLabel label3 = new JLabel("Fecha de finalización del grupo (dd/MM/yyyy)");
        label3.setFont(new Font("Arial", Font.BOLD, 14));
        label3.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtFechaFin = new JTextField(20);
        txtFechaFin.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtFechaFin.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Codigo del curso
        JLabel label4 = new JLabel("Identificación del curso al que desea asociar el grupo");
        label4.setFont(new Font("Arial", Font.BOLD, 14));
        label4.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtIdentCur = new JTextField(20);
        txtIdentCur.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtIdentCur.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label1);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtIdentGru);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label2);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtFechaIni);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label3);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtFechaFin);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label4);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtIdentCur);
        
        JButton btnAsignar = new JButton("Asignar grupo");
        btnAsignar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAsignar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            asignarGrupoACurso(txtIdentGru, txtFechaIni, txtFechaFin, txtIdentCur, ventanaAsoGruCur);
            }
        });

        // Botón Volver
        JButton btnVolver = new JButton("Volver");
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventanaAsoGruCur.dispose(); 
                abrirVentanaAdministradores(); 
            }
        });
        panelCentral.add(Box.createVerticalStrut(10));
        panelCentral.add(btnAsignar);
        panelCentral.add(Box.createVerticalStrut(10));
        panelCentral.add(btnVolver);
        
        ventanaAsoGruCur.add(panelCentral, BorderLayout.CENTER);
        ventanaAsoGruCur.setVisible(true);
    }
    private void asignarProfesorAGrupo(JTextField txtIdentCur, JTextField txtIdentGru, 
                                  JTextField txtIdentPro, JFrame ventana) {
    
    // Obtener valores
    String identCurso = txtIdentCur.getText().trim();
    String identGrupoStr = txtIdentGru.getText().trim();
    String identProfesor = txtIdentPro.getText().trim();
    
    // Validar campos vacíos
    if (identCurso.isEmpty() || identGrupoStr.isEmpty() || identProfesor.isEmpty()) {
        JOptionPane.showMessageDialog(ventana, "Por favor rellene todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Validar que la identificación del grupo sea numérica
    if (!esNumero(identGrupoStr)) {
        JOptionPane.showMessageDialog(ventana, "La identificación del grupo debe ser un número", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    int identGrupo = Integer.parseInt(identGrupoStr);
    
    // Buscar el curso
    Cursos cursoEncontrado = null;
    for (Cursos curso : cursos) {
        if (curso.getIdentificacion().equals(identCurso)) {
            cursoEncontrado = curso;//Si se encuentra el curso se le asocia a una variable.
            break;
        }
    }
    //En caso de que el curso no se haya encontrado se devueve error
    if (cursoEncontrado == null) {
        JOptionPane.showMessageDialog(ventana, "No se encontró el curso con la identificación: " + identCurso, "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Buscar el grupo dentro del curso
    Grupos grupoEncontrado = null;
    for (Grupos grupo : cursoEncontrado.getGrupos()) {
        if (grupo.getIdentificacionGrupo() == identGrupo) {
            grupoEncontrado = grupo;//Igual que con el curso, se asigna una variable.
            break;
        }
    }
    //Se devuelve error.
    if (grupoEncontrado == null) {
        JOptionPane.showMessageDialog(ventana, 
            "No se encontró el grupo con ID: " + identGrupo + " en el curso: " + cursoEncontrado.getNombre(), 
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    //Mismo proceso con los profesores
    Profesores profesorEncontrado = null;
    for (Profesores profesor : profesores) {
        if (profesor.getIdentificacion().equals(identProfesor)) {
            profesorEncontrado = profesor;
            break;
        }
    }
    
    if (profesorEncontrado == null) {
        JOptionPane.showMessageDialog(ventana, "No se encontró el profesor con ID: " + identProfesor, "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    //Finalmente si todo sale bien entonces se guarda el profesor, y se guarda e archivo.
    String resultado = grupoEncontrado.asignarProfesor(profesorEncontrado);

    if (resultado == null) {
        // Éxito
        guardarCursosEnArchivo(); // Guardar cambios en cursos
        guardarProfesoresEnArchivo(); // Guardar cambios en profesores (para actualizar gruposImpartiendo)
        JOptionPane.showMessageDialog(ventana, "Profesor asignado exitosamente al grupo", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        // Limpiar campos
        txtIdentCur.setText("");
        txtIdentGru.setText("");
        txtIdentPro.setText("");
    } else {
        // Mostrar el error devuelto por asignarProfesor
        JOptionPane.showMessageDialog(ventana, resultado, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    private void ventanaAsociarProfesoresGrupos() {
        // Cerrar ventana actual
        this.dispose();
        
        
        // Crear nueva ventana
        JFrame ventanaAsoProGru = new JFrame("Asociar grupos a los profesores");
        ventanaAsoProGru.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaAsoProGru.setSize(330, 350);
        ventanaAsoProGru.setLocationRelativeTo(null);
        
        // Agregar label
        JLabel label = new JLabel("Asociar grupos a los profesores", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        ventanaAsoProGru.add(label, BorderLayout.NORTH);
        
         // Labels y text box necesarios.
        //Panel para poder mostrar el CRUD.
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        // Labels y text box de los elementos.
        //Codigo del curso
        JLabel label1 = new JLabel("Identificación del curso que desea asignar");
        label1.setFont(new Font("Arial", Font.BOLD, 14));
        label1.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtIdentCur = new JTextField(20);
        txtIdentCur.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtIdentCur.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Identificación del grupo
        JLabel label2 = new JLabel("Identificación del grupo que desea asignar");
        label2.setFont(new Font("Arial", Font.BOLD, 14));
        label2.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtIdentGru = new JTextField(20);
        txtIdentGru.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtIdentGru.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        //Identificación del profesor
        JLabel label3 = new JLabel("Identificación del profesor");
        label3.setFont(new Font("Arial", Font.BOLD, 14));
        label3.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtIdentPro = new JTextField(20);
        txtIdentPro.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtIdentPro.setAlignmentX(Component.CENTER_ALIGNMENT);
     
        
        
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label1);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtIdentCur);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label2);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtIdentGru);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label3);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtIdentPro);

        
        JButton btnAsignar = new JButton("Asignar profesor");
        btnAsignar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAsignar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                asignarProfesorAGrupo(txtIdentCur, txtIdentGru, txtIdentPro, ventanaAsoProGru);
            }
        });
        // Botón Volver
        JButton btnVolver = new JButton("Regresar");
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventanaAsoProGru.dispose(); 
                abrirVentanaAdministradores(); 
            }
        });

 
        panelCentral.add(Box.createVerticalStrut(10));
        panelCentral.add(btnAsignar);
        panelCentral.add(Box.createVerticalStrut(10));
        panelCentral.add(btnVolver);
        
        ventanaAsoProGru.add(panelCentral, BorderLayout.CENTER);
        ventanaAsoProGru.setVisible(true);
    }
       
    
    
    private void abrirVentanaAdministradores() {
        // Cerrar ventana actual
        this.dispose();
        cargarEstudiantesDesdeArchivo();
        cargarProfesoresDesdeArchivo();
        cargarCursosDesdeArchivo();
        
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
        btnAsoCur.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Cerrar ventana actual y abrir la principal
                ventanaAdministradores.dispose();
                ventanaAsociarGruposCursos();
            }
        });
        JButton btnAsoPro = new JButton("Asociar grupos a profesores");
        btnAsoPro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Cerrar ventana actual y abrir la principal
                ventanaAdministradores.dispose();
                ventanaAsociarProfesoresGrupos();
            }
        });
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
    //Funciones para generar la identificación aleatoria.
    private String generarIdentificacionUnica() {
    Random random = new Random();
    String nuevaIdentificacion;
    
    // Generar IDs hasta encontrar uno único
    do {
        // Generar número entre 100000 y 999999 (6 dígitos)
        int num = random.nextInt(900000) + 100000;
        nuevaIdentificacion = String.valueOf(num);
    } while (existeIdentificacionEvaluacion(nuevaIdentificacion));
    
    return nuevaIdentificacion;
    }
    
    private boolean existeIdentificacionEvaluacion(String identificacion) {
    for (Evaluaciones eval : evaluaciones) {
        if (eval.getIdentificacion() == Integer.parseInt(identificacion)) {
            return true;
        }
    }
    return false;
    }
    //Metodo para agregar la pregunta a la lista y mostrarlo.
    private void actualizarListaPreguntas(Evaluaciones evaluacionActual, JList<String> listaPreguntas) {
    if (evaluacionActual == null) return;
    
    List<String> nombresPreguntas = new ArrayList<>();
    List<Object> todasPreguntas = evaluacionActual.getTodasLasPreguntas();
    
    for (Object pregunta : todasPreguntas) {
        String descripcion = "";
        String tipo = pregunta.getClass().getSimpleName();
        
        if (pregunta instanceof SeleccionUnica) {
            descripcion = ((SeleccionUnica) pregunta).getDescripcion();
        } else if (pregunta instanceof SeleccionMultiple) {
            descripcion = ((SeleccionMultiple) pregunta).getDescripcion();
        } else if (pregunta instanceof FalsoVerdadero) {
            descripcion = ((FalsoVerdadero) pregunta).getDescripcion();
        } else if (pregunta instanceof Pareo) {
            descripcion = ((Pareo) pregunta).getDescripcion();
        } else if (pregunta instanceof Sopa) {
            descripcion = ((Sopa) pregunta).getDescripcion();
        }
        
        // Limitar longitud
        if (descripcion.length() > 30) {
            descripcion = descripcion.substring(0, 30) + "...";
        }
        
        nombresPreguntas.add(tipo + " - " + descripcion);
    }
    listaPreguntas.setListData(nombresPreguntas.toArray(new String[0]));
    }
    
    private SeleccionUnica ventanaSeleccionUnica(JFrame parent, Evaluaciones evalActual, JList<String> listaPreg) {
    JDialog dialog = new JDialog(parent, "Agregar Selección Única", true);
    dialog.setSize(500, 600);
    dialog.setLocationRelativeTo(parent);
    dialog.setLayout(new BorderLayout());
    
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
    // 1. DESCRIPCIÓN
    JLabel lblDesc = new JLabel("Descripción de la pregunta:");
    lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
    JTextArea txtDesc = new JTextArea(3, 30);
    txtDesc.setLineWrap(true);
    txtDesc.setWrapStyleWord(true);
    JScrollPane scrollDesc = new JScrollPane(txtDesc);
    scrollDesc.setMaximumSize(new Dimension(400, 80));
    
    // 2. PUNTOS
    JLabel lblPuntos = new JLabel("Puntos que vale la pregunta:");
    lblPuntos.setAlignmentX(Component.LEFT_ALIGNMENT);
    JTextField txtPuntos = new JTextField();
    txtPuntos.setMaximumSize(new Dimension(100, 25));
    
    // 3. OPCIONES
    JLabel lblOpciones = new JLabel("Opciones (una por línea, mínimo 2):");
    lblOpciones.setAlignmentX(Component.LEFT_ALIGNMENT);
    JTextArea txtOpciones = new JTextArea(5, 30);
    txtOpciones.setLineWrap(true);
    JScrollPane scrollOpciones = new JScrollPane(txtOpciones);
    scrollOpciones.setMaximumSize(new Dimension(400, 120));
    
    // 4. RESPUESTA CORRECTA
    JLabel lblRespuesta = new JLabel("Respuesta correcta (escriba solamente una opción):");
    lblRespuesta.setAlignmentX(Component.LEFT_ALIGNMENT);
    JTextField txtRespuesta = new JTextField();
    txtRespuesta.setMaximumSize(new Dimension(400, 25));
    
    // BOTONES
    JPanel panelBotones = new JPanel();
    JButton btnGuardar = new JButton("Guardar");
    JButton btnCancelar = new JButton("Cancelar");
    
    // Resultado
    final SeleccionUnica[] preguntaCreada = {null};
    
    // ACTION LISTENERS
    btnGuardar.addActionListener(e -> {
        // Validaciones
        if (txtDesc.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Por favor digite una descripción");
            return;
        }
        if (!esNumero(txtPuntos.getText().trim()) || Integer.parseInt(txtPuntos.getText().trim()) < 1) {
            JOptionPane.showMessageDialog(dialog, "Los puntos deben ser un número mayor a 0");
            return;
        }
        
        String[] opcionesArray = txtOpciones.getText().split("\n");
        if (opcionesArray.length < 2) {
            JOptionPane.showMessageDialog(dialog, "Debe haber al menos 2 opciones");
            return;
        }
        
        List<String> opciones = new ArrayList<>();
        for (String opcion : opcionesArray) {
            if (!opcion.trim().isEmpty()) {
                opciones.add(opcion.trim());
            }
        }
        
        if (!opciones.contains(txtRespuesta.getText().trim())) {
            JOptionPane.showMessageDialog(dialog, "La respuesta debe coincidir con una de las opciones");
            return;
        }
        
        // Crear pregunta
        preguntaCreada[0] = new SeleccionUnica(
            txtDesc.getText().trim(),
            Integer.parseInt(txtPuntos.getText().trim()),
            opciones,
            txtRespuesta.getText().trim()
        );
        if (preguntaCreada[0] != null) {
        evalActual.agregarSeleccionUnica(preguntaCreada[0]);
        actualizarListaPreguntas(evalActual, listaPreg);
    }
        dialog.dispose();
    });
    
    btnCancelar.addActionListener(e -> {
        dialog.dispose();
    });
    
    // AGREGAR COMPONENTES
    panel.add(lblDesc);
    panel.add(Box.createVerticalStrut(5));
    panel.add(scrollDesc);
    panel.add(Box.createVerticalStrut(15));
    
    panel.add(lblPuntos);
    panel.add(Box.createVerticalStrut(5));
    panel.add(txtPuntos);
    panel.add(Box.createVerticalStrut(15));
    
    panel.add(lblOpciones);
    panel.add(Box.createVerticalStrut(5));
    panel.add(scrollOpciones);
    panel.add(Box.createVerticalStrut(15));
    
    panel.add(lblRespuesta);
    panel.add(Box.createVerticalStrut(5));
    panel.add(txtRespuesta);
    panel.add(Box.createVerticalStrut(20));
    
    panelBotones.add(btnGuardar);
    panelBotones.add(btnCancelar);
    
    dialog.add(panel, BorderLayout.CENTER);
    dialog.add(panelBotones, BorderLayout.SOUTH);
    
    dialog.setVisible(true);
    return preguntaCreada[0];
    }
    private SeleccionMultiple ventanaSeleccionMultiple(JFrame parent, Evaluaciones evalActual, JList<String> listaPreg) {
    JDialog dialog = new JDialog(parent, "Agregar Selección Multiple", true);
    dialog.setSize(500, 600);
    dialog.setLocationRelativeTo(parent);
    dialog.setLayout(new BorderLayout());
    
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
    // 1. DESCRIPCIÓN
    JLabel lblDesc = new JLabel("Descripción de la pregunta:");
    lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
    JTextArea txtDesc = new JTextArea(3, 30);
    txtDesc.setLineWrap(true);
    txtDesc.setWrapStyleWord(true);
    JScrollPane scrollDesc = new JScrollPane(txtDesc);
    scrollDesc.setMaximumSize(new Dimension(400, 80));
    
    // 2. PUNTOS
    JLabel lblPuntos = new JLabel("Puntos que vale la pregunta:");
    lblPuntos.setAlignmentX(Component.LEFT_ALIGNMENT);
    JTextField txtPuntos = new JTextField();
    txtPuntos.setMaximumSize(new Dimension(100, 25));
    
    // 3. OPCIONES
    JLabel lblOpciones = new JLabel("Opciones (una por línea, mínimo 3):");
    lblOpciones.setAlignmentX(Component.LEFT_ALIGNMENT);
    JTextArea txtOpciones = new JTextArea(5, 30);
    txtOpciones.setLineWrap(true);
    JScrollPane scrollOpciones = new JScrollPane(txtOpciones);
    scrollOpciones.setMaximumSize(new Dimension(400, 120));
    
    JLabel lblRespuesta = new JLabel("Respuestas correctas (una por línea, mínimo 1):");
    lblRespuesta.setAlignmentX(Component.LEFT_ALIGNMENT);
    JTextArea txtRespuestas = new JTextArea(5, 30); // ← MISMO TAMAÑO
    txtRespuestas.setLineWrap(true);
    JScrollPane scrollRespuestas = new JScrollPane(txtRespuestas); // ← JScrollPane también
    scrollRespuestas.setMaximumSize(new Dimension(400, 120)); // ← MISMA ALTURA
    
    // BOTONES
    JPanel panelBotones = new JPanel();
    JButton btnGuardar = new JButton("Guardar");
    JButton btnCancelar = new JButton("Cancelar");
    
    // Resultado
    final SeleccionMultiple[] preguntaCreada = {null};
    
    // ACTION LISTENERS
    btnGuardar.addActionListener(e -> {
        // Validaciones
        if (txtDesc.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Por favor digite una descripción");
            return;
        }
        if (!esNumero(txtPuntos.getText().trim()) || Integer.parseInt(txtPuntos.getText().trim()) < 1) {
            JOptionPane.showMessageDialog(dialog, "Los puntos deben ser un número mayor a 0");
            return;
        }
        
        String[] opcionesArray = txtOpciones.getText().split("\n");
        if (opcionesArray.length < 3) {
            JOptionPane.showMessageDialog(dialog, "Debe haber al menos 3 opciones");
            return;
        }
        
        List<String> opciones = new ArrayList<>();
        for (String opcion : opcionesArray) {
            if (!opcion.trim().isEmpty()) {
                opciones.add(opcion.trim());
            }
        }
        String[] respuestasArray = txtRespuestas.getText().split("\n");
        if (respuestasArray.length < 1) {
            JOptionPane.showMessageDialog(dialog, "Debe haber al menos 1 respuesta correcta");
            return;
        }
        //Validar las respuestas correctas
        List<String> respuestasCorrectas = new ArrayList<>();
        for (String respuesta : respuestasArray) {
            if (!respuesta.trim().isEmpty()) {
                // Validar que cada respuesta coincida con una opción
                if (!opciones.contains(respuesta.trim())) {
                    JOptionPane.showMessageDialog(dialog, 
                        "La respuesta '" + respuesta.trim() + "' no coincide con ninguna opción");
                    return;
                }
                respuestasCorrectas.add(respuesta.trim());
            }
        }
        if (respuestasCorrectas.size() >= opciones.size()) {
            JOptionPane.showMessageDialog(dialog, 
                "No pueden todas las opciones ser correctas");
            return;
        }
        
        // Crear pregunta
        preguntaCreada[0] = new SeleccionMultiple(
            txtDesc.getText().trim(),
            Integer.parseInt(txtPuntos.getText().trim()),
            opciones,
            respuestasCorrectas  // ← Lista de respuestas correctas
        );
        
        if (preguntaCreada[0] != null) {
            evalActual.agregarSeleccionMultiple(preguntaCreada[0]); // ← CAMBIADO A MÚLTIPLE
            actualizarListaPreguntas(evalActual, listaPreg);
        }
        dialog.dispose();
    
    });
    
    btnCancelar.addActionListener(e -> {
        dialog.dispose();
    });
    
    // AGREGAR COMPONENTES
    panel.add(lblDesc);
    panel.add(Box.createVerticalStrut(5));
    panel.add(scrollDesc);
    panel.add(Box.createVerticalStrut(15));
    
    panel.add(lblPuntos);
    panel.add(Box.createVerticalStrut(5));
    panel.add(txtPuntos);
    panel.add(Box.createVerticalStrut(15));
    
    panel.add(lblOpciones);
    panel.add(Box.createVerticalStrut(5));
    panel.add(scrollOpciones);
    panel.add(Box.createVerticalStrut(15));
    
    panel.add(lblRespuesta);
    panel.add(Box.createVerticalStrut(5));
    panel.add(scrollRespuestas);
    panel.add(Box.createVerticalStrut(20));
    
    panelBotones.add(btnGuardar);
    panelBotones.add(btnCancelar);
    
    dialog.add(panel, BorderLayout.CENTER);
    dialog.add(panelBotones, BorderLayout.SOUTH);
    
    dialog.setVisible(true);
    return preguntaCreada[0];
    }
    private FalsoVerdadero ventanaFalsoVerdadero(JFrame parent, Evaluaciones evalActual, JList<String> listaPreg) {
    JDialog dialog = new JDialog(parent, "Agregar pregunta Verdadero/Falso", true);
    dialog.setSize(450, 450); // Ventana más pequeña
    dialog.setLocationRelativeTo(parent);
    dialog.setLayout(new BorderLayout());
    
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
    // 1. DESCRIPCIÓN
    JLabel lblDesc = new JLabel("Descripción de la pregunta:");
    lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
    JTextArea txtDesc = new JTextArea(3, 25);
    txtDesc.setLineWrap(true);
    txtDesc.setWrapStyleWord(true);
    JScrollPane scrollDesc = new JScrollPane(txtDesc);
    scrollDesc.setMaximumSize(new Dimension(350, 80));
    
    // 2. PUNTOS
    JLabel lblPuntos = new JLabel("Puntos que vale la pregunta:");
    lblPuntos.setAlignmentX(Component.LEFT_ALIGNMENT);
    JTextField txtPuntos = new JTextField();
    txtPuntos.setMaximumSize(new Dimension(100, 25));
    
    // 3. OPCIONES FIJA (Verdadero/Falso)
    JLabel lblOpciones = new JLabel("Opciones (automáticas):");
    lblOpciones.setAlignmentX(Component.LEFT_ALIGNMENT);
    JTextArea txtOpciones = new JTextArea("Verdadero\nFalso", 2, 25);
    txtOpciones.setLineWrap(true);
    txtOpciones.setEditable(false); // No editable - opciones fijas
    txtOpciones.setBackground(new Color(240, 240, 240)); // Fondo gris para indicar no editable
    JScrollPane scrollOpciones = new JScrollPane(txtOpciones);
    scrollOpciones.setMaximumSize(new Dimension(350, 60));
    
    // 4. RESPUESTA CORRECTA
    JLabel lblRespuesta = new JLabel("Respuesta correcta (escriba: Verdadero o Falso):");
    lblRespuesta.setAlignmentX(Component.LEFT_ALIGNMENT);
    JTextField txtRespuesta = new JTextField(); // TextField normal para una respuesta
    txtRespuesta.setMaximumSize(new Dimension(200, 25));
    
    // BOTONES
    JPanel panelBotones = new JPanel();
    JButton btnGuardar = new JButton("Guardar");
    JButton btnCancelar = new JButton("Cancelar");
    
    // Resultado - CAMBIADO A FalsoVerdadero
    final FalsoVerdadero[] preguntaCreada = {null};
    
    // ACTION LISTENERS
    btnGuardar.addActionListener(e -> {
        // Validaciones
        if (txtDesc.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Por favor digite una descripción");
            return;
        }
        if (!esNumero(txtPuntos.getText().trim()) || Integer.parseInt(txtPuntos.getText().trim()) < 1) {
            JOptionPane.showMessageDialog(dialog, "Los puntos deben ser un número mayor a 0");
            return;
        }
        
        // Validar respuesta (solo "Verdadero" o "Falso")
        String respuesta = txtRespuesta.getText().trim();
        if (!respuesta.equalsIgnoreCase("Verdadero") && !respuesta.equalsIgnoreCase("Falso")) {
            JOptionPane.showMessageDialog(dialog, 
                "La respuesta debe ser 'Verdadero' o 'Falso'");
            return;
        }
        
        // Crear lista fija de opciones
        List<String> opciones = new ArrayList<>();
        opciones.add("Verdadero");
        opciones.add("Falso");
        
        // Crear pregunta de Falso/Verdadero
        preguntaCreada[0] = new FalsoVerdadero(
            txtDesc.getText().trim(),
            Integer.parseInt(txtPuntos.getText().trim()),
            opciones,
            respuesta  // ← Solo una respuesta correcta
        );
        
        if (preguntaCreada[0] != null) {
            evalActual.agregarFalsoVerdadero(preguntaCreada[0]); // ← CAMBIADO A FalsoVerdadero
            actualizarListaPreguntas(evalActual, listaPreg);
        }
        dialog.dispose();
    });
    
    btnCancelar.addActionListener(e -> {
        dialog.dispose();
    });
    
    // AGREGAR COMPONENTES
    panel.add(lblDesc);
    panel.add(Box.createVerticalStrut(5));
    panel.add(scrollDesc);
    panel.add(Box.createVerticalStrut(15));
    
    panel.add(lblPuntos);
    panel.add(Box.createVerticalStrut(5));
    panel.add(txtPuntos);
    panel.add(Box.createVerticalStrut(15));
    
    panel.add(lblOpciones);
    panel.add(Box.createVerticalStrut(5));
    panel.add(scrollOpciones);
    panel.add(Box.createVerticalStrut(15));
    
    panel.add(lblRespuesta);
    panel.add(Box.createVerticalStrut(5));
    panel.add(txtRespuesta);
    panel.add(Box.createVerticalStrut(20));
    
    panelBotones.add(btnGuardar);
    panelBotones.add(btnCancelar);
    
    dialog.add(panel, BorderLayout.CENTER);
    dialog.add(panelBotones, BorderLayout.SOUTH);
    
    dialog.setVisible(true);
    return preguntaCreada[0];
    }

    // Método para ventana de Pareo
    private Pareo ventanaPareo(JFrame parent, Evaluaciones evalActual, JList<String> listaPreg) {
    JDialog dialog = new JDialog(parent, "Agregar Pregunta de Pareo", true);
    dialog.setSize(600, 700);
    dialog.setLocationRelativeTo(parent);
    dialog.setLayout(new BorderLayout());

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // 1. DESCRIPCIÓN
    JLabel lblDesc = new JLabel("Descripción de la pregunta:");
    lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
    JTextArea txtDesc = new JTextArea(2, 30);
    txtDesc.setLineWrap(true);
    txtDesc.setWrapStyleWord(true);
    JScrollPane scrollDesc = new JScrollPane(txtDesc);
    scrollDesc.setMaximumSize(new Dimension(500, 60));

    // 2. PUNTOS
    JLabel lblPuntos = new JLabel("Puntos que vale la pregunta:");
    lblPuntos.setAlignmentX(Component.LEFT_ALIGNMENT);
    JTextField txtPuntos = new JTextField();
    txtPuntos.setMaximumSize(new Dimension(100, 25));

    // 3. COLUMNA 1 (Enunciados)
    JLabel lblCol1 = new JLabel("Columna 1 - Enunciados:");
    lblCol1.setAlignmentX(Component.LEFT_ALIGNMENT);
    DefaultListModel<String> modelCol1 = new DefaultListModel<>();
    JList<String> listaCol1 = new JList<>(modelCol1);
    JScrollPane scrollCol1 = new JScrollPane(listaCol1);
    scrollCol1.setMaximumSize(new Dimension(500, 100));

    JPanel panelAgregarCol1 = new JPanel();
    panelAgregarCol1.setLayout(new FlowLayout(FlowLayout.LEFT));
    JTextField txtItemCol1 = new JTextField(25);
    JButton btnAgregarCol1 = new JButton("Agregar");
    btnAgregarCol1.addActionListener(e -> {
        String item = txtItemCol1.getText().trim();
        if (!item.isEmpty()) {
            modelCol1.addElement(item);
            txtItemCol1.setText("");
        }
    });
    panelAgregarCol1.add(txtItemCol1);
    panelAgregarCol1.add(btnAgregarCol1);

    // 4. COLUMNA 2 (Respuestas/Distractores)
    JLabel lblCol2 = new JLabel("Columna 2 - Respuestas/Distractores:");
    lblCol2.setAlignmentX(Component.LEFT_ALIGNMENT);
    DefaultListModel<String> modelCol2 = new DefaultListModel<>();
    JList<String> listaCol2 = new JList<>(modelCol2);
    JScrollPane scrollCol2 = new JScrollPane(listaCol2);
    scrollCol2.setMaximumSize(new Dimension(500, 100));

    JPanel panelAgregarCol2 = new JPanel();
    panelAgregarCol2.setLayout(new FlowLayout(FlowLayout.LEFT));
    JTextField txtItemCol2 = new JTextField(25);
    JButton btnAgregarCol2 = new JButton("Agregar");
    btnAgregarCol2.addActionListener(e -> {
        String item = txtItemCol2.getText().trim();
        if (!item.isEmpty()) {
            modelCol2.addElement(item);
            txtItemCol2.setText("");
        }
    });
    panelAgregarCol2.add(txtItemCol2);
    panelAgregarCol2.add(btnAgregarCol2);

    // 5. RELACIONES
    JLabel lblRelaciones = new JLabel("Relaciones correctas (índice Col1 -> índice Col2):");
    lblRelaciones.setAlignmentX(Component.LEFT_ALIGNMENT);
    DefaultListModel<String> modelRelaciones = new DefaultListModel<>();
    JList<String> listaRelaciones = new JList<>(modelRelaciones);
    JScrollPane scrollRelaciones = new JScrollPane(listaRelaciones);
    scrollRelaciones.setMaximumSize(new Dimension(500, 80));

    JPanel panelAgregarRelacion = new JPanel();
    panelAgregarRelacion.setLayout(new FlowLayout(FlowLayout.LEFT));
    JLabel lblIndice1 = new JLabel("Col1:");
    JTextField txtIndice1 = new JTextField(3);
    JLabel lblIndice2 = new JLabel("-> Col2:");
    JTextField txtIndice2 = new JTextField(3);
    JButton btnAgregarRelacion = new JButton("Agregar Relación");
    btnAgregarRelacion.addActionListener(e -> {
        String idx1 = txtIndice1.getText().trim();
        String idx2 = txtIndice2.getText().trim();
        if (!idx1.isEmpty() && !idx2.isEmpty()) {
            modelRelaciones.addElement(idx1 + " -> " + idx2);
            txtIndice1.setText("");
            txtIndice2.setText("");
        }
    });
    panelAgregarRelacion.add(lblIndice1);
    panelAgregarRelacion.add(txtIndice1);
    panelAgregarRelacion.add(lblIndice2);
    panelAgregarRelacion.add(txtIndice2);
    panelAgregarRelacion.add(btnAgregarRelacion);

    // 6. ORDEN ALEATORIO
    JCheckBox chkOrdenAleatorio = new JCheckBox("Orden aleatorio");
    chkOrdenAleatorio.setAlignmentX(Component.LEFT_ALIGNMENT);

    // BOTONES
    JPanel panelBotones = new JPanel();
    JButton btnGuardar = new JButton("Guardar");
    JButton btnCancelar = new JButton("Cancelar");

    final Pareo[] preguntaCreada = {null};

    // ACTION LISTENERS
    btnGuardar.addActionListener(e -> {
        // Validaciones
        if (txtDesc.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Por favor digite una descripción");
            return;
        }
        if (!esNumero(txtPuntos.getText().trim()) || Integer.parseInt(txtPuntos.getText().trim()) < 1) {
            JOptionPane.showMessageDialog(dialog, "Los puntos deben ser un número mayor a 0");
            return;
        }

        // Convertir las listas a List<String>
        List<String> col1 = new ArrayList<>();
        for (int i = 0; i < modelCol1.size(); i++) {
            col1.add(modelCol1.getElementAt(i));
        }

        List<String> col2 = new ArrayList<>();
        for (int i = 0; i < modelCol2.size(); i++) {
            col2.add(modelCol2.getElementAt(i));
        }

        // Convertir relaciones
        java.util.Map<Integer, Integer> relaciones = new java.util.HashMap<>();
        for (int i = 0; i < modelRelaciones.size(); i++) {
            String rel = modelRelaciones.getElementAt(i);
            String[] partes = rel.split(" -> ");
            if (partes.length == 2) {
                try {
                    int idx1 = Integer.parseInt(partes[0].trim());
                    int idx2 = Integer.parseInt(partes[1].trim());
                    relaciones.put(idx1, idx2);
                } catch (NumberFormatException ex) {
                    // Ignorar relaciones inválidas
                }
            }
        }

        // Crear pregunta
        Pareo pareo = new Pareo(
            txtDesc.getText().trim(),
            Integer.parseInt(txtPuntos.getText().trim()),
            col1,
            col2,
            relaciones,
            chkOrdenAleatorio.isSelected()
        );

        // Validar
        String error = pareo.validar();
        if (error != null) {
            JOptionPane.showMessageDialog(dialog, error);
            return;
        }

        preguntaCreada[0] = pareo;
        if (preguntaCreada[0] != null) {
            evalActual.agregarPareo(preguntaCreada[0]);
            actualizarListaPreguntas(evalActual, listaPreg);
        }
        dialog.dispose();
    });

    btnCancelar.addActionListener(e -> {
        dialog.dispose();
    });

    // AGREGAR COMPONENTES
    panel.add(lblDesc);
    panel.add(Box.createVerticalStrut(5));
    panel.add(scrollDesc);
    panel.add(Box.createVerticalStrut(10));

    panel.add(lblPuntos);
    panel.add(Box.createVerticalStrut(5));
    panel.add(txtPuntos);
    panel.add(Box.createVerticalStrut(10));

    panel.add(lblCol1);
    panel.add(Box.createVerticalStrut(5));
    panel.add(scrollCol1);
    panel.add(Box.createVerticalStrut(5));
    panel.add(panelAgregarCol1);
    panel.add(Box.createVerticalStrut(10));

    panel.add(lblCol2);
    panel.add(Box.createVerticalStrut(5));
    panel.add(scrollCol2);
    panel.add(Box.createVerticalStrut(5));
    panel.add(panelAgregarCol2);
    panel.add(Box.createVerticalStrut(10));

    panel.add(lblRelaciones);
    panel.add(Box.createVerticalStrut(5));
    panel.add(scrollRelaciones);
    panel.add(Box.createVerticalStrut(5));
    panel.add(panelAgregarRelacion);
    panel.add(Box.createVerticalStrut(10));

    panel.add(chkOrdenAleatorio);
    panel.add(Box.createVerticalStrut(15));

    panelBotones.add(btnGuardar);
    panelBotones.add(btnCancelar);

    JScrollPane scrollPanel = new JScrollPane(panel);
    dialog.add(scrollPanel, BorderLayout.CENTER);
    dialog.add(panelBotones, BorderLayout.SOUTH);

    dialog.setVisible(true);
    return preguntaCreada[0];
    }

    // Método para ventana de Sopa de Letras
    private Sopa ventanaSopa(JFrame parent, Evaluaciones evalActual, JList<String> listaPreg) {
    JDialog dialog = new JDialog(parent, "Agregar Sopa de Letras", true);
    dialog.setSize(550, 600);
    dialog.setLocationRelativeTo(parent);
    dialog.setLayout(new BorderLayout());

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // 1. DESCRIPCIÓN
    JLabel lblDesc = new JLabel("Descripción de la pregunta:");
    lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
    JTextArea txtDesc = new JTextArea(2, 30);
    txtDesc.setLineWrap(true);
    txtDesc.setWrapStyleWord(true);
    JScrollPane scrollDesc = new JScrollPane(txtDesc);
    scrollDesc.setMaximumSize(new Dimension(450, 60));

    // 2. PUNTOS
    JLabel lblPuntos = new JLabel("Puntos que vale la pregunta:");
    lblPuntos.setAlignmentX(Component.LEFT_ALIGNMENT);
    JTextField txtPuntos = new JTextField();
    txtPuntos.setMaximumSize(new Dimension(100, 25));

    // 3. PALABRAS A BUSCAR (mínimo 10)
    JLabel lblPalabras = new JLabel("Palabras a buscar (mínimo 10):");
    lblPalabras.setAlignmentX(Component.LEFT_ALIGNMENT);
    DefaultListModel<String> modelPalabras = new DefaultListModel<>();
    JList<String> listaPalabras = new JList<>(modelPalabras);
    JScrollPane scrollPalabras = new JScrollPane(listaPalabras);
    scrollPalabras.setMaximumSize(new Dimension(450, 200));

    JPanel panelAgregarPalabra = new JPanel();
    panelAgregarPalabra.setLayout(new FlowLayout(FlowLayout.LEFT));
    JTextField txtPalabra = new JTextField(20);
    JButton btnAgregarPalabra = new JButton("Agregar Palabra");
    JButton btnEliminarPalabra = new JButton("Eliminar");

    btnAgregarPalabra.addActionListener(e -> {
        String palabra = txtPalabra.getText().trim();
        if (!palabra.isEmpty()) {
            modelPalabras.addElement(palabra.toUpperCase());
            txtPalabra.setText("");
        }
    });

    btnEliminarPalabra.addActionListener(e -> {
        int selectedIndex = listaPalabras.getSelectedIndex();
        if (selectedIndex != -1) {
            modelPalabras.remove(selectedIndex);
        }
    });

    panelAgregarPalabra.add(txtPalabra);
    panelAgregarPalabra.add(btnAgregarPalabra);
    panelAgregarPalabra.add(btnEliminarPalabra);

    // 4. ORDEN ALEATORIO
    JCheckBox chkOrdenAleatorio = new JCheckBox("Generar con posiciones aleatorias");
    chkOrdenAleatorio.setAlignmentX(Component.LEFT_ALIGNMENT);

    // Contador de palabras
    JLabel lblContador = new JLabel("Palabras agregadas: 0");
    lblContador.setAlignmentX(Component.LEFT_ALIGNMENT);

    // Actualizar contador cuando se agregue o elimine
    btnAgregarPalabra.addActionListener(e -> {
        lblContador.setText("Palabras agregadas: " + modelPalabras.size());
    });
    btnEliminarPalabra.addActionListener(e -> {
        lblContador.setText("Palabras agregadas: " + modelPalabras.size());
    });

    // BOTONES
    JPanel panelBotones = new JPanel();
    JButton btnGuardar = new JButton("Guardar");
    JButton btnCancelar = new JButton("Cancelar");

    final Sopa[] preguntaCreada = {null};

    // ACTION LISTENERS
    btnGuardar.addActionListener(e -> {
        // Validaciones
        if (txtDesc.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Por favor digite una descripción");
            return;
        }
        if (!esNumero(txtPuntos.getText().trim()) || Integer.parseInt(txtPuntos.getText().trim()) < 1) {
            JOptionPane.showMessageDialog(dialog, "Los puntos deben ser un número mayor a 0");
            return;
        }

        // Convertir las palabras a List<String>
        List<String> palabras = new ArrayList<>();
        for (int i = 0; i < modelPalabras.size(); i++) {
            palabras.add(modelPalabras.getElementAt(i));
        }

        if (palabras.size() < 10) {
            JOptionPane.showMessageDialog(dialog, "Debe agregar al menos 10 palabras");
            return;
        }

        // Crear pregunta
        Sopa sopa = new Sopa(
            txtDesc.getText().trim(),
            Integer.parseInt(txtPuntos.getText().trim()),
            palabras,
            chkOrdenAleatorio.isSelected()
        );

        // Validar
        String error = sopa.validar();
        if (error != null) {
            JOptionPane.showMessageDialog(dialog, error);
            return;
        }

        // Generar la sopa de letras
        boolean generada = sopa.generarSopa();
        if (!generada) {
            JOptionPane.showMessageDialog(dialog, "Error al generar la sopa de letras");
            return;
        }

        preguntaCreada[0] = sopa;
        if (preguntaCreada[0] != null) {
            evalActual.agregarSopa(preguntaCreada[0]);
            actualizarListaPreguntas(evalActual, listaPreg);
            JOptionPane.showMessageDialog(dialog, "Sopa de letras creada exitosamente!");
        }
        dialog.dispose();
    });

    btnCancelar.addActionListener(e -> {
        dialog.dispose();
    });

    // AGREGAR COMPONENTES
    panel.add(lblDesc);
    panel.add(Box.createVerticalStrut(5));
    panel.add(scrollDesc);
    panel.add(Box.createVerticalStrut(10));

    panel.add(lblPuntos);
    panel.add(Box.createVerticalStrut(5));
    panel.add(txtPuntos);
    panel.add(Box.createVerticalStrut(15));

    panel.add(lblPalabras);
    panel.add(Box.createVerticalStrut(5));
    panel.add(scrollPalabras);
    panel.add(Box.createVerticalStrut(5));
    panel.add(panelAgregarPalabra);
    panel.add(Box.createVerticalStrut(10));

    panel.add(lblContador);
    panel.add(Box.createVerticalStrut(10));

    panel.add(chkOrdenAleatorio);
    panel.add(Box.createVerticalStrut(15));

    panelBotones.add(btnGuardar);
    panelBotones.add(btnCancelar);

    JScrollPane scrollPanel = new JScrollPane(panel);
    dialog.add(scrollPanel, BorderLayout.CENTER);
    dialog.add(panelBotones, BorderLayout.SOUTH);

    dialog.setVisible(true);
    return preguntaCreada[0];
    }

    private void limpiarCamposEvaluacion(JTextField txtIdentEva, JTextField txtNomEva,
                                   JTextField txtInstru, JTextField txtObjeti,
                                   JTextField txtDuracion, JComboBox<String> comboPreguntAlea,
                                   JComboBox<String> comboRespuestAlea, JList<String> listaPreg,
                                   Evaluaciones evaluacionActual) {
    txtIdentEva.setText("");
    txtNomEva.setText("");
    txtInstru.setText("");
    txtObjeti.setText("");
    txtDuracion.setText("");
    comboPreguntAlea.setSelectedIndex(0); 
    comboRespuestAlea.setSelectedIndex(0); 
    listaPreg.setListData(new String[0]);
    evaluacionActual = new Evaluaciones(); 
}

    private void registrarEvaluacion(JTextField txtIdentEva,JTextField txtNomEva,
            JTextField txtInstru, JTextField txtObjeti, JTextField txtDuracion, 
            JComboBox<String> comboPreguntAle, JComboBox<String> comboRespuestAle,
            JList<String> listaPreg, Evaluaciones evaluacionActual) {
    //Se valida que la identificacion no sea repetida(por si acaso)
    for (Evaluaciones evaluacion : evaluaciones) {
        if (evaluacion.getIdentificacion() == Integer.parseInt(txtIdentEva.getText().trim())) {
            JOptionPane.showMessageDialog(null, "Ya hay una evaluación registrada con esta identificación","Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    //Se valida la lista
    if (listaPreg.getModel().getSize() == 0) {
    JOptionPane.showMessageDialog(null, "Debe agregar al menos una pregunta a la evaluación", "Error", JOptionPane.ERROR_MESSAGE);
    return;
    }
    //Se validad que todos los campos esten con algo
    if (txtIdentEva.getText().trim().isEmpty() ||
        txtNomEva.getText().trim().isEmpty() ||
        txtInstru.getText().trim().isEmpty() ||
        txtObjeti.getText().trim().isEmpty() ||
        txtDuracion.getText().trim().isEmpty()) {
        
        JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    // Se validan los campos que deben ser número antes de todo
    if (!esNumero(txtDuracion.getText().trim())) {
        JOptionPane.showMessageDialog(null, "La duración de la evaluacion debe ser un número valido","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (!esNumero(txtIdentEva.getText().trim())) {
        JOptionPane.showMessageDialog(null, "La identificación debe ser un número válido","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    
    // Obtener valores de los ComboBox
    String preguntAle = (String) comboPreguntAle.getSelectedItem();
    String respuestAle = (String) comboRespuestAle.getSelectedItem();
    
    //Se revisan estos valores
    if (!"On".equals(preguntAle) && !"Off".equals(preguntAle)) {
        JOptionPane.showMessageDialog(null, "Selección de preguntas aleatorias inválida","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (!"On".equals(respuestAle) && !"Off".equals(respuestAle)) {
        JOptionPane.showMessageDialog(null, "Selección de respuestas aleatorias inválida","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    //Se pasan los valores que son String y deben ser Int
    int identEva = Integer.parseInt(txtIdentEva.getText().trim());
    int duracion = Integer.parseInt(txtDuracion.getText().trim());
    
    //Crear lista de objetivos
    List<String> objetivos = new ArrayList<>();
    String textoObjetivos = txtObjeti.getText().trim();

    if (!textoObjetivos.isEmpty()) {
        String[] objetivosArray = textoObjetivos.split(","); //Para separar por comas
        for (String objetivo : objetivosArray) {
            String objetivoTrim = objetivo.trim();
            if (!objetivoTrim.isEmpty()) {
                objetivos.add(objetivoTrim);
            }
        }
    }

    // Validar que haya al menos un objetivo
    if (objetivos.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Debe ingresar al menos un objetivo", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Crear nueva evaluacion
    Evaluaciones nuevaEvaluacion = new Evaluaciones(
        identEva,
        txtNomEva.getText().trim(),
        txtInstru.getText().trim(),
        objetivos,
        duracion,
        preguntAle,
        respuestAle,
        evaluacionActual.getSeleccionesUnicas(), // ← Pasar las listas de preguntas
        evaluacionActual.getSeleccionesMultiples(),
        evaluacionActual.getFalsoVerdaderos(),
        evaluacionActual.getPareos(),
        evaluacionActual.getSopas()

    );
    
    // Validar el curso
    List<String> errores = nuevaEvaluacion.validarEvaluacionCompleta(evaluaciones);
    if (!errores.isEmpty()) {
        String mensajeError = "Errores de validación:\n• " + String.join("\n• ", errores);
        JOptionPane.showMessageDialog(null, mensajeError, "Errores de validación", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    evaluaciones.add(nuevaEvaluacion);
    
    
    guardarEvaluacionesEnArchivo();
    
    
    

    // Limpiar campos (necesitas crear esta función)
    limpiarCamposEvaluacion(txtIdentEva, txtNomEva, txtInstru, txtObjeti, txtDuracion,comboPreguntAle,
            comboRespuestAle,listaPreg, evaluacionActual);
    }
    private void consultarEvaluacion(JTextField txtIdentEva, JTextField txtNomEva,
                                JTextField txtInstru, JTextField txtObjeti, 
                                JTextField txtDuracion, JComboBox<String> comboPreguntAlea,
                                JComboBox<String> comboRespuestAlea, JScrollPane scrollPreguntas) {
    cargarEvaluacionesDesdeArchivo();
    
    
    // Validar que la identificación no esté vacía
    if (txtIdentEva.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Por favor ingrese una identificación para consultar", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    // Validar que la identificación no esté vacía
    if (txtIdentEva.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Por favor ingrese una identificación para consultar", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Validar que sea número
    if (!esNumero(txtIdentEva.getText().trim())) {
        JOptionPane.showMessageDialog(null, "La identificación debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    int identBuscada = Integer.parseInt(txtIdentEva.getText().trim());
    
    // Buscar la evaluación
    Evaluaciones evaluacionEncontrada = null;
    for (Evaluaciones eval : evaluaciones) {
        if (eval.getIdentificacion() == identBuscada) {
            evaluacionEncontrada = eval;
            break;
        }
    }
    
    if (evaluacionEncontrada == null) {
        JOptionPane.showMessageDialog(null, "No se encontró una evaluación con la identificación: " + identBuscada, "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Llenar los campos con la información encontrada
    txtNomEva.setText(evaluacionEncontrada.getNombre());
    txtInstru.setText(evaluacionEncontrada.getInstrucGenerales());
    
    // Convertir lista de objetivos a texto separado por comas
    String objetivosTexto = String.join(", ", evaluacionEncontrada.getObjEva());
    txtObjeti.setText(objetivosTexto);
    
    txtDuracion.setText(String.valueOf(evaluacionEncontrada.getDuracion()));
    comboPreguntAlea.setSelectedItem(evaluacionEncontrada.getPregunRandom());
    comboRespuestAlea.setSelectedItem(evaluacionEncontrada.getRespueRandom());
    
    // Actualizar lista de preguntas
    actualizarListaPreguntasConsulta(evaluacionEncontrada, scrollPreguntas);
    
    
}

private void actualizarListaPreguntasConsulta(Evaluaciones evaluacion, JScrollPane scrollPreguntas) {
    // Obtener el JList del JScrollPane
    JList<String> listaPreguntas = (JList<String>) scrollPreguntas.getViewport().getView();
    
    List<String> nombresPreguntas = new ArrayList<>();
    List<Object> todasPreguntas = evaluacion.getTodasLasPreguntas();
    
    for (Object pregunta : todasPreguntas) {
        String descripcion = "";
        String tipo = pregunta.getClass().getSimpleName();
        
        if (pregunta instanceof SeleccionUnica) {
            descripcion = ((SeleccionUnica) pregunta).getDescripcion();
        } else if (pregunta instanceof SeleccionMultiple) {
            descripcion = ((SeleccionMultiple) pregunta).getDescripcion();
        } else if (pregunta instanceof FalsoVerdadero) {
            descripcion = ((FalsoVerdadero) pregunta).getDescripcion();
        }
        
        if (descripcion.length() > 30) {
            descripcion = descripcion.substring(0, 30) + "...";
        }
        
        nombresPreguntas.add(tipo + " - " + descripcion);
    }
    listaPreguntas.setListData(nombresPreguntas.toArray(new String[0]));
}   
    private void eliminarEvaluacion(JTextField txtIdentEva, JTextField txtNomEva,
                               JTextField txtInstru, JTextField txtObjeti, 
                               JTextField txtDuracion, JComboBox<String> comboPreguntAlea,
                               JComboBox<String> comboRespuestAlea, JList<String> listaPreg,
                               Evaluaciones evaluacionActual) {
    
    // Cargar evaluaciones actuales
    cargarEvaluacionesDesdeArchivo();
    
    // Se valida que la identificación no este vacia.
    if (txtIdentEva.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Por favor ingrese una identificación para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Se valida que la identificación sea un número
    if (!esNumero(txtIdentEva.getText().trim())) {
        JOptionPane.showMessageDialog(null, "La identificación debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    int identBuscada = Integer.parseInt(txtIdentEva.getText().trim());
    
    // Se valida que la identificación si este
    Evaluaciones evaluacionEncontrada = null;
    for (Evaluaciones eval : evaluaciones) {
        if (eval.getIdentificacion() == identBuscada) {
            evaluacionEncontrada = eval;
            break;
        }
    }
    //Si la identificación no esta se devuelve error.
    if (evaluacionEncontrada == null) {
        JOptionPane.showMessageDialog(null, "No se encontró una evaluación con la identificación: " + identBuscada, "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Eliminar la evaluación
    evaluaciones.remove(evaluacionEncontrada);
    guardarEvaluacionesEnArchivo();
    
    limpiarCamposEvaluacion(txtIdentEva, txtNomEva, txtInstru, txtObjeti, txtDuracion, 
                           comboPreguntAlea, comboRespuestAlea, listaPreg, evaluacionActual);
    
   
    }
    private void modificarEvaluacion(JTextField txtIdentEva, JTextField txtNomEva,
                                JTextField txtInstru, JTextField txtObjeti, 
                                JTextField txtDuracion, JComboBox<String> comboPreguntAlea,
                                JComboBox<String> comboRespuestAlea, JList<String> listaPreg,
                                Evaluaciones evaluacionActual) {
    
    // Cargar evaluaciones actuales
    cargarEvaluacionesDesdeArchivo();
    
    // Validar que la identificación no esté vacía
    if (txtIdentEva.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Por favor ingrese una identificación para modificar", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Validar que sea número
    if (!esNumero(txtIdentEva.getText().trim())) {
        JOptionPane.showMessageDialog(null, "La identificación debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    int identBuscada = Integer.parseInt(txtIdentEva.getText().trim());
    
    // Buscar la evaluación
    Evaluaciones evaluacionEncontrada = null;
    int indiceEncontrado = -1;
    for (int i = 0; i < evaluaciones.size(); i++) {
        if (evaluaciones.get(i).getIdentificacion() == identBuscada) {
            evaluacionEncontrada = evaluaciones.get(i);
            indiceEncontrado = i;
            break;
        }
    }
    
    if (evaluacionEncontrada == null) {
        JOptionPane.showMessageDialog(null, "No se encontró una evaluación con la identificación: " + identBuscada, "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Validar campos vacíos
    if (txtNomEva.getText().trim().isEmpty() ||
        txtInstru.getText().trim().isEmpty() ||
        txtObjeti.getText().trim().isEmpty() ||
        txtDuracion.getText().trim().isEmpty()) {
        
        JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Validar números
    if (!esNumero(txtDuracion.getText().trim())) {
        JOptionPane.showMessageDialog(null, "La duración debe ser un número válido","Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Obtener valores
    String preguntAle = (String) comboPreguntAlea.getSelectedItem();
    String respuestAle = (String) comboRespuestAlea.getSelectedItem();
    int duracion = Integer.parseInt(txtDuracion.getText().trim());
    
    // Crear lista de objetivos
    List<String> objetivos = new ArrayList<>();
    String[] objetivosArray = txtObjeti.getText().split(",");
    for (String objetivo : objetivosArray) {
        if (!objetivo.trim().isEmpty()) {
            objetivos.add(objetivo.trim());
        }
    }
    
    // Crear evaluación actualizada
    Evaluaciones evaluacionActualizada = new Evaluaciones(
        identBuscada, // Mantener misma ID
        txtNomEva.getText().trim(),
        txtInstru.getText().trim(),
        objetivos,
        duracion,
        preguntAle,
        respuestAle,
        evaluacionActual.getSeleccionesUnicas(),
        evaluacionActual.getSeleccionesMultiples(),
        evaluacionActual.getFalsoVerdaderos(),
        evaluacionActual.getPareos(),
        evaluacionActual.getSopas()
    );
    
    // Validar la evaluación actualizada
    List<String> errores = evaluacionActualizada.validarEvaluacionCompleta(evaluaciones);
    if (!errores.isEmpty()) {
        String mensajeError = "Errores de validación:\n• " + String.join("\n• ", errores);
        JOptionPane.showMessageDialog(null, mensajeError, "Errores de validación", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Reemplazar la evaluación antigua con la actualizada
    evaluaciones.set(indiceEncontrado, evaluacionActualizada);
    guardarEvaluacionesEnArchivo();
    limpiarCamposEvaluacion(txtIdentEva, txtNomEva, txtInstru, txtObjeti, txtDuracion, 
                           comboPreguntAlea, comboRespuestAlea, listaPreg, evaluacionActual);
    
    JOptionPane.showMessageDialog(null, 
        "Evaluación modificada exitosamente:\n" +
        "ID: " + identBuscada + "\n" +
        "Nombre: " + txtNomEva.getText().trim(),
        "Éxito", 
        JOptionPane.INFORMATION_MESSAGE);
    }
    private void abriVentanaEvaluaciones(){
        this.dispose();
        Evaluaciones evaluacionActual = new Evaluaciones();
        JList<String> listaPreguntas = new JList<>();
        
        // Crear nueva ventana
        JFrame ventanaEvaluaciones = new JFrame("Evaluaciones");
        ventanaEvaluaciones.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaEvaluaciones.setSize(400, 300);
        ventanaEvaluaciones.setLocationRelativeTo(null);
        // Agregar label de titulo
        JLabel label = new JLabel("Administración de evaluaciones", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        ventanaEvaluaciones.add(label, BorderLayout.NORTH);
        
        //Panel para poder mostrar el CRUD.
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        
        //Labels y text box requeridos
        //Identificación de la evaluación.
        JLabel label1 = new JLabel("Identificación de la evaluación.");
        label1.setFont(new Font("Arial", Font.BOLD, 14));
        label1.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtIdentEva = new JTextField(20);
        txtIdentEva.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtIdentEva.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Nombre de la evaluación
        JLabel label2 = new JLabel("Nombre de la evaluación");
        label2.setFont(new Font("Arial", Font.BOLD, 14));
        label2.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JTextField txtNomEva = new JTextField(20);
        txtNomEva.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtNomEva.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        //Instrucciones generales
        JLabel label3 = new JLabel("Instrucciones generales");
        label3.setFont(new Font("Arial", Font.BOLD, 14));
        label3.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
       
        JTextField txtInstru = new JTextField(20);
        txtInstru.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtInstru.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        //Objetivos de la evaluación.
        JLabel label4 = new JLabel("Objetivos de la evaluación");
        label4.setFont(new Font("Arial", Font.BOLD, 14));
        label4.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
       
        JTextField txtObjeti = new JTextField(20);
        txtObjeti.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtObjeti.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        //Duración de la evaluación.
        JLabel label5 = new JLabel("Duración de la evaluación (minutos)");
        label5.setFont(new Font("Arial", Font.BOLD, 14));
        label5.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
       
        JTextField txtDuracion = new JTextField(20);
        txtDuracion.setMaximumSize(new Dimension(200, 25)); // Tamaño máximo
        txtDuracion.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        String[] modos = {"On", "Off"};
        //Preguntas en orden aleatorio
        JLabel label6 = new JLabel("Preguntas aleatorias");
        label6.setFont(new Font("Arial", Font.BOLD, 14));
        label6.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        
        JComboBox<String> comboPreguntAlea = new JComboBox<>(modos);
        comboPreguntAlea.setMaximumSize(new Dimension(200, 25));
        comboPreguntAlea.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Respuestas en orden aleatorio
        JLabel label7 = new JLabel("Respuestas aleatorias");
        label7.setFont(new Font("Arial", Font.BOLD, 14));
        label7.setAlignmentX(Component.CENTER_ALIGNMENT); // Para alinear a la izquierda
        
        JComboBox<String> comboRespuestAlea = new JComboBox<>(modos);
        comboRespuestAlea.setMaximumSize(new Dimension(200, 25));
        comboRespuestAlea.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label1);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtIdentEva);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label2);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtNomEva);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label3);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtInstru);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label4);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtObjeti);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label5);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(txtDuracion);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label6);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(comboPreguntAlea);
        panelCentral.add(Box.createVerticalStrut(10)); // Espacio
        panelCentral.add(label7);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(comboRespuestAlea);
        
        ventanaEvaluaciones.add(panelCentral, BorderLayout.CENTER);
        //Para generar una identificación con el sistema.
        String idUnica = generarIdentificacionUnica();
        txtIdentEva.setText(idUnica);
        
        // Declarar scrollPreguntas antes de los ActionListeners
        final JScrollPane scrollPreguntas = new JScrollPane(listaPreguntas);
        scrollPreguntas.setPreferredSize(new Dimension(300, 150));
        
        //Espacio de los botones
        // Botones para tipos de preguntas (en columna)
        JButton btnAgregarSeleccionUnica = new JButton("Selección Única");
        btnAgregarSeleccionUnica.addActionListener(e -> {
        ventanaSeleccionUnica(ventanaEvaluaciones, evaluacionActual, listaPreguntas);
        });
        JButton btnAgregarSeleccionMultiple = new JButton("Selección Múltiple");
        btnAgregarSeleccionMultiple.addActionListener(e -> {
        ventanaSeleccionMultiple(ventanaEvaluaciones, evaluacionActual, listaPreguntas);
        });
        JButton btnAgregarFalsoVerdadero = new JButton("Falso/Verdadero");
        btnAgregarFalsoVerdadero.addActionListener(e -> {
        ventanaFalsoVerdadero(ventanaEvaluaciones, evaluacionActual, listaPreguntas);
        });
        JButton btnAgregarPareo = new JButton("Pareo");
        btnAgregarPareo.addActionListener(e -> {
        ventanaPareo(ventanaEvaluaciones, evaluacionActual, listaPreguntas);
        });
        JButton btnAgregarSopa = new JButton("Sopa de Letras");
        btnAgregarSopa.addActionListener(e -> {
        ventanaSopa(ventanaEvaluaciones, evaluacionActual, listaPreguntas);
        });
        // Botones de operaciones CRUD (en parejas)
        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            registrarEvaluacion(txtIdentEva, txtNomEva, txtInstru, txtObjeti, txtDuracion, 
                              comboPreguntAlea,comboRespuestAlea, listaPreguntas, evaluacionActual);
        }});
        JButton btnModificar = new JButton("Modificar");
        btnModificar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            modificarEvaluacion(txtIdentEva, txtNomEva, txtInstru, txtObjeti, txtDuracion, 
                              comboPreguntAlea,comboRespuestAlea, listaPreguntas, evaluacionActual);
        }});
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            eliminarEvaluacion(txtIdentEva, txtNomEva, txtInstru, txtObjeti, txtDuracion, 
                              comboPreguntAlea,comboRespuestAlea, listaPreguntas, evaluacionActual);
        }});
        JButton btnConsultar = new JButton("Consultar");
        btnConsultar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            consultarEvaluacion(txtIdentEva, txtNomEva, txtInstru, txtObjeti, txtDuracion, 
                              comboPreguntAlea,comboRespuestAlea, scrollPreguntas);
        }});
        // Botón regresar 
        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventanaEvaluaciones.dispose();
                abrirVentanaProfesores(profesorAutenticado);
            }
        });

        // Configurar tamaños y alineación
        btnAgregarSeleccionUnica.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAgregarSeleccionMultiple.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAgregarFalsoVerdadero.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAgregarPareo.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAgregarSopa.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnModificar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEliminar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConsultar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegresar.setAlignmentX(Component.CENTER_ALIGNMENT);

        
        

        // Agregar al panel central en el orden que pides
        panelCentral.add(Box.createVerticalStrut(10));

        // Primeros 4 botones en columna (tipos de pregunta)
        panelCentral.add(btnAgregarSeleccionUnica);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(btnAgregarSeleccionMultiple);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(btnAgregarFalsoVerdadero);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(btnAgregarPareo);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(btnAgregarSopa);

        panelCentral.add(Box.createVerticalStrut(15));

        // Lista de preguntas
        panelCentral.add(scrollPreguntas);

        panelCentral.add(Box.createVerticalStrut(15));

        // Botones CRUD en parejas
        JPanel panelCRUD1 = new JPanel();
        panelCRUD1.setLayout(new FlowLayout());
        panelCRUD1.add(btnRegistrar);
        panelCRUD1.add(btnModificar);

        JPanel panelCRUD2 = new JPanel();
        panelCRUD2.setLayout(new FlowLayout());
        panelCRUD2.add(btnEliminar);
        panelCRUD2.add(btnConsultar);

        panelCentral.add(panelCRUD1);
        panelCentral.add(Box.createVerticalStrut(5));
        panelCentral.add(panelCRUD2);

        panelCentral.add(Box.createVerticalStrut(15));

        // Botón regresar solo
        panelCentral.add(btnRegresar);
        ventanaEvaluaciones.setVisible(true);
    }

    private void mostrarInfoEstudiante(Estudiantes estudiante) {
        // Crear ventana para mostrar información
        JFrame ventanaInfo = new JFrame("Información del Estudiante");
        ventanaInfo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaInfo.setSize(600, 700);
        ventanaInfo.setLocationRelativeTo(null);

        // Panel principal con scroll
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Título
        JLabel labelTitulo = new JLabel("Información General del Estudiante");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(labelTitulo);
        panelPrincipal.add(Box.createVerticalStrut(25));

        // Formato de información
        String[] labels = {
            "Nombre:",
            "Primer Apellido:",
            "Segundo Apellido:",
            "Identificación:",
            "Teléfono:",
            "Correo:",
            "Dirección:",
            "Organización:",
            "Temas de Interés:",
            "Fecha de Registro:"
        };

        String[] valores = {
            estudiante.getNombre(),
            estudiante.getApellido1(),
            estudiante.getApellido2(),
            estudiante.getIdentificacion(),
            estudiante.getTelefono(),
            estudiante.getCorreo(),
            estudiante.getDireccion(),
            estudiante.getOrganizacion(),
            estudiante.getTemasInteresComoTexto(),
            new SimpleDateFormat("dd/MM/yyyy").format(estudiante.getFechaRegistro())
        };

        for (int i = 0; i < labels.length; i++) {
            JLabel labelCampo = new JLabel(labels[i]);
            labelCampo.setFont(new Font("Arial", Font.BOLD, 14));
            labelCampo.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelPrincipal.add(labelCampo);

            JLabel labelValor = new JLabel(valores[i]);
            labelValor.setFont(new Font("Arial", Font.PLAIN, 14));
            labelValor.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelPrincipal.add(labelValor);
            panelPrincipal.add(Box.createVerticalStrut(15));
        }

        // Botón cerrar
        panelPrincipal.add(Box.createVerticalStrut(10));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventanaInfo.dispose();
            }
        });
        panelPrincipal.add(btnCerrar);

        // Agregar scroll pane
        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        ventanaInfo.add(scrollPane);
        ventanaInfo.setVisible(true);
    }

    private void mostrarInfoProfesor(Profesores profesor) {
        // Crear ventana para mostrar información
        JFrame ventanaInfo = new JFrame("Información del Profesor");
        ventanaInfo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaInfo.setSize(600, 750);
        ventanaInfo.setLocationRelativeTo(null);

        // Panel principal con scroll
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Título
        JLabel labelTitulo = new JLabel("Información General del Profesor");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(labelTitulo);
        panelPrincipal.add(Box.createVerticalStrut(25));

        // Información básica
        String[] labels = {
            "Nombre:",
            "Primer Apellido:",
            "Segundo Apellido:",
            "Identificación:",
            "Teléfono:",
            "Correo:",
            "Dirección:",
            "Fecha de Registro:"
        };

        String[] valores = {
            profesor.getNombre(),
            profesor.getApellido1(),
            profesor.getApellido2(),
            profesor.getIdentificacion(),
            profesor.getTelefono(),
            profesor.getCorreo(),
            profesor.getDireccion(),
            new SimpleDateFormat("dd/MM/yyyy").format(profesor.getFechaRegistro())
        };

        for (int i = 0; i < labels.length; i++) {
            JLabel labelCampo = new JLabel(labels[i]);
            labelCampo.setFont(new Font("Arial", Font.BOLD, 14));
            labelCampo.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelPrincipal.add(labelCampo);

            JLabel labelValor = new JLabel(valores[i]);
            labelValor.setFont(new Font("Arial", Font.PLAIN, 14));
            labelValor.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelPrincipal.add(labelValor);
            panelPrincipal.add(Box.createVerticalStrut(15));
        }

        // Títulos obtenidos
        panelPrincipal.add(Box.createVerticalStrut(10));
        JLabel labelTitulos = new JLabel("Títulos Obtenidos:");
        labelTitulos.setFont(new Font("Arial", Font.BOLD, 14));
        labelTitulos.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(labelTitulos);
        panelPrincipal.add(Box.createVerticalStrut(10));

        if (profesor.getTitulosobtenidos() != null && !profesor.getTitulosobtenidos().isEmpty()) {
            for (String titulo : profesor.getTitulosobtenidos()) {
                JLabel labelTituloItem = new JLabel("• " + titulo);
                labelTituloItem.setFont(new Font("Arial", Font.PLAIN, 14));
                labelTituloItem.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelPrincipal.add(labelTituloItem);
                panelPrincipal.add(Box.createVerticalStrut(5));
            }
        } else {
            JLabel labelSinTitulos = new JLabel("No hay títulos registrados");
            labelSinTitulos.setFont(new Font("Arial", Font.ITALIC, 14));
            labelSinTitulos.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelPrincipal.add(labelSinTitulos);
        }
        panelPrincipal.add(Box.createVerticalStrut(15));

        // Certificaciones
        JLabel labelCertificaciones = new JLabel("Certificaciones:");
        labelCertificaciones.setFont(new Font("Arial", Font.BOLD, 14));
        labelCertificaciones.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(labelCertificaciones);
        panelPrincipal.add(Box.createVerticalStrut(10));

        if (profesor.getCertificaciones() != null && !profesor.getCertificaciones().isEmpty()) {
            for (String certificacion : profesor.getCertificaciones()) {
                JLabel labelCert = new JLabel("• " + certificacion);
                labelCert.setFont(new Font("Arial", Font.PLAIN, 14));
                labelCert.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelPrincipal.add(labelCert);
                panelPrincipal.add(Box.createVerticalStrut(5));
            }
        } else {
            JLabel labelSinCert = new JLabel("No hay certificaciones registradas");
            labelSinCert.setFont(new Font("Arial", Font.ITALIC, 14));
            labelSinCert.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelPrincipal.add(labelSinCert);
        }

        // Botón cerrar
        panelPrincipal.add(Box.createVerticalStrut(20));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventanaInfo.dispose();
            }
        });
        panelPrincipal.add(btnCerrar);

        // Agregar scroll pane
        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        ventanaInfo.add(scrollPane);
        ventanaInfo.setVisible(true);
    }

    private String generarCodigoVerificacion() {
        Random random = new Random();
        int codigo = 100000 + random.nextInt(900000); // Genera número entre 100000 y 999999
        return String.valueOf(codigo);
    }

    private void recuperarContrasenaEstudiante() {
        // Solicitar correo
        String correo = JOptionPane.showInputDialog(null,
            "Ingrese su correo electrónico:",
            "Recuperar Contraseña",
            JOptionPane.QUESTION_MESSAGE);

        if (correo == null || correo.trim().isEmpty()) {
            return;
        }

        // Buscar estudiante por correo
        Estudiantes estudianteEncontrado = null;
        for (Estudiantes est : estudiantes) {
            if (est.getCorreo().equalsIgnoreCase(correo.trim())) {
                estudianteEncontrado = est;
                break;
            }
        }

        if (estudianteEncontrado == null) {
            JOptionPane.showMessageDialog(null,
                "No se encontró ningún estudiante con ese correo",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Generar código
        codigoVerificacionEstudiante = generarCodigoVerificacion();

        // Enviar código por correo
        try {
            enviarCorreoConCodigo(estudianteEncontrado.getCorreo(),
                estudianteEncontrado.getNombre(),
                codigoVerificacionEstudiante);

            JOptionPane.showMessageDialog(null,
                "Se ha enviado un código de verificación a su correo",
                "Código Enviado",
                JOptionPane.INFORMATION_MESSAGE);

            // Solicitar código
            String codigoIngresado = JOptionPane.showInputDialog(null,
                "Ingrese el código de verificación que recibió por correo:",
                "Verificación",
                JOptionPane.QUESTION_MESSAGE);

            if (codigoIngresado == null || !codigoIngresado.equals(codigoVerificacionEstudiante)) {
                JOptionPane.showMessageDialog(null,
                    "Código incorrecto",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Mostrar ventana para cambiar contraseña
            mostrarVentanaCambiarContrasena(estudianteEncontrado, true);

        } catch (MessagingException e) {
            JOptionPane.showMessageDialog(null,
                "Error al enviar el correo: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void recuperarContrasenaProfesor() {
        // Solicitar correo
        String correo = JOptionPane.showInputDialog(null,
            "Ingrese su correo electrónico:",
            "Recuperar Contraseña",
            JOptionPane.QUESTION_MESSAGE);

        if (correo == null || correo.trim().isEmpty()) {
            return;
        }

        // Buscar profesor por correo
        Profesores profesorEncontrado = null;
        for (Profesores prof : profesores) {
            if (prof.getCorreo().equalsIgnoreCase(correo.trim())) {
                profesorEncontrado = prof;
                break;
            }
        }

        if (profesorEncontrado == null) {
            JOptionPane.showMessageDialog(null,
                "No se encontró ningún profesor con ese correo",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Generar código
        codigoVerificacionProfesor = generarCodigoVerificacion();

        // Enviar código por correo
        try {
            enviarCorreoConCodigo(profesorEncontrado.getCorreo(),
                profesorEncontrado.getNombre(),
                codigoVerificacionProfesor);

            JOptionPane.showMessageDialog(null,
                "Se ha enviado un código de verificación a su correo",
                "Código Enviado",
                JOptionPane.INFORMATION_MESSAGE);

            // Solicitar código
            String codigoIngresado = JOptionPane.showInputDialog(null,
                "Ingrese el código de verificación que recibió por correo:",
                "Verificación",
                JOptionPane.QUESTION_MESSAGE);

            if (codigoIngresado == null || !codigoIngresado.equals(codigoVerificacionProfesor)) {
                JOptionPane.showMessageDialog(null,
                    "Código incorrecto",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Mostrar ventana para cambiar contraseña
            mostrarVentanaCambiarContrasena(profesorEncontrado, false);

        } catch (MessagingException e) {
            JOptionPane.showMessageDialog(null,
                "Error al enviar el correo: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void enviarCorreoConCodigo(String destinatario, String nombre, String codigo) throws MessagingException {
        String host = "smtp.gmail.com";
        String usuario = "ronniadmision@gmail.com";
        String contraseña = "fmfe pkuy xzwj jkls";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, contraseña);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(usuario));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        message.setSubject("Código de Verificación - Recuperación de Contraseña");

        String mensaje = "Estimado " + nombre + ",\n\n" +
                        "Su código de verificación para recuperar su contraseña es: " + codigo + "\n\n" +
                        "Este código es válido por esta sesión únicamente.\n\n" +
                        "Si usted no solicitó este código, ignore este correo.\n\n" +
                        "Saludos,\nSistema de Matrícula";

        message.setText(mensaje);
        Transport.send(message);
    }

    private void mostrarVentanaCambiarContrasena(Object usuario, boolean esEstudiante) {
        JFrame ventanaCambio = new JFrame("Cambiar Contraseña");
        ventanaCambio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaCambio.setSize(400, 300);
        ventanaCambio.setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Título
        JLabel labelTitulo = new JLabel("Cambiar Contraseña");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(labelTitulo);
        panelPrincipal.add(Box.createVerticalStrut(20));

        // Nueva contraseña
        JLabel labelNueva = new JLabel("Nueva Contraseña:");
        labelNueva.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(labelNueva);

        JPasswordField txtNuevaContrasena = new JPasswordField(20);
        txtNuevaContrasena.setMaximumSize(new Dimension(250, 25));
        txtNuevaContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(txtNuevaContrasena);
        panelPrincipal.add(Box.createVerticalStrut(15));

        // Confirmar contraseña
        JLabel labelConfirmar = new JLabel("Confirmar Contraseña:");
        labelConfirmar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(labelConfirmar);

        JPasswordField txtConfirmarContrasena = new JPasswordField(20);
        txtConfirmarContrasena.setMaximumSize(new Dimension(250, 25));
        txtConfirmarContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(txtConfirmarContrasena);
        panelPrincipal.add(Box.createVerticalStrut(20));

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nuevaContrasena = new String(txtNuevaContrasena.getPassword());
                String confirmarContrasena = new String(txtConfirmarContrasena.getPassword());

                if (nuevaContrasena.isEmpty() || confirmarContrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(ventanaCambio,
                        "Por favor complete todos los campos",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!nuevaContrasena.equals(confirmarContrasena)) {
                    JOptionPane.showMessageDialog(ventanaCambio,
                        "Las contraseñas no coinciden",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validar contraseña
                String errorValidacion = null;
                if (nuevaContrasena.length() < 8) {
                    errorValidacion = "La contraseña debe tener al menos 8 caracteres";
                } else if (!nuevaContrasena.matches(".*\\d.*")) {
                    errorValidacion = "La contraseña debe contener al menos un número";
                } else if (!nuevaContrasena.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
                    errorValidacion = "La contraseña debe contener al menos un carácter especial";
                }

                if (errorValidacion != null) {
                    JOptionPane.showMessageDialog(ventanaCambio,
                        errorValidacion,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Cambiar contraseña
                if (esEstudiante) {
                    Estudiantes est = (Estudiantes) usuario;
                    est.setContrasena(nuevaContrasena);
                    guardarEstudiantesEnArchivo();
                } else {
                    Profesores prof = (Profesores) usuario;
                    prof.setContrasena(nuevaContrasena);
                    guardarProfesoresEnArchivo();
                }

                JOptionPane.showMessageDialog(ventanaCambio,
                    "Contraseña cambiada exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                ventanaCambio.dispose();
            }
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventanaCambio.dispose();
            }
        });

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        panelPrincipal.add(panelBotones);

        ventanaCambio.add(panelPrincipal);
        ventanaCambio.setVisible(true);
    }

    private void mostrarVentanaMatricular(Estudiantes estudiante) {
        // Crear ventana para matricular curso
        JFrame ventanaMatricula = new JFrame("Matricular Curso");
        ventanaMatricula.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaMatricula.setSize(500, 400);
        ventanaMatricula.setLocationRelativeTo(null);

        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Título
        JLabel labelTitulo = new JLabel("Matricular Curso");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(labelTitulo);
        panelPrincipal.add(Box.createVerticalStrut(20));

        // Verificar si hay cursos disponibles
        if (cursos == null || cursos.isEmpty()) {
            JLabel labelNoCursos = new JLabel("No hay cursos disponibles");
            labelNoCursos.setFont(new Font("Arial", Font.PLAIN, 14));
            labelNoCursos.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelPrincipal.add(labelNoCursos);
            panelPrincipal.add(Box.createVerticalStrut(20));

            JButton btnCerrar = new JButton("Cerrar");
            btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnCerrar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ventanaMatricula.dispose();
                }
            });
            panelPrincipal.add(btnCerrar);

            ventanaMatricula.add(panelPrincipal);
            ventanaMatricula.setVisible(true);
            return;
        }

        // Label y ComboBox para seleccionar curso
        JLabel labelCurso = new JLabel("Seleccione un curso:");
        labelCurso.setFont(new Font("Arial", Font.BOLD, 14));
        labelCurso.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(labelCurso);
        panelPrincipal.add(Box.createVerticalStrut(10));

        JComboBox<String> comboCursos = new JComboBox<>();
        comboCursos.setMaximumSize(new Dimension(400, 25));
        comboCursos.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Llenar combo con cursos
        for (Cursos curso : cursos) {
            comboCursos.addItem(curso.getIdentificacion() + " - " + curso.getNombre());
        }
        panelPrincipal.add(comboCursos);
        panelPrincipal.add(Box.createVerticalStrut(20));

        // Label y ComboBox para seleccionar grupo
        JLabel labelGrupo = new JLabel("Seleccione un grupo:");
        labelGrupo.setFont(new Font("Arial", Font.BOLD, 14));
        labelGrupo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(labelGrupo);
        panelPrincipal.add(Box.createVerticalStrut(10));

        JComboBox<String> comboGrupos = new JComboBox<>();
        comboGrupos.setMaximumSize(new Dimension(400, 25));
        comboGrupos.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(comboGrupos);
        panelPrincipal.add(Box.createVerticalStrut(20));

        // Actualizar grupos cuando se seleccione un curso
        comboCursos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = comboCursos.getSelectedIndex();
                if (selectedIndex >= 0) {
                    Cursos cursoSeleccionado = cursos.get(selectedIndex);
                    comboGrupos.removeAllItems();

                    if (cursoSeleccionado.getGrupos() != null && !cursoSeleccionado.getGrupos().isEmpty()) {
                        for (Grupos grupo : cursoSeleccionado.getGrupos()) {
                            comboGrupos.addItem("Grupo " + grupo.getIdentificacionGrupo());
                        }
                    } else {
                        comboGrupos.addItem("No hay grupos disponibles");
                    }
                }
            }
        });

        // Inicializar grupos del primer curso
        if (!cursos.isEmpty()) {
            Cursos primerCurso = cursos.get(0);
            if (primerCurso.getGrupos() != null && !primerCurso.getGrupos().isEmpty()) {
                for (Grupos grupo : primerCurso.getGrupos()) {
                    comboGrupos.addItem("Grupo " + grupo.getIdentificacionGrupo());
                }
            } else {
                comboGrupos.addItem("No hay grupos disponibles");
            }
        }

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton btnMatricular = new JButton("Matricular");
        btnMatricular.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int cursoIndex = comboCursos.getSelectedIndex();
                int grupoIndex = comboGrupos.getSelectedIndex();

                if (cursoIndex < 0 || grupoIndex < 0) {
                    JOptionPane.showMessageDialog(ventanaMatricula,
                        "Por favor seleccione un curso y un grupo",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Cursos cursoSeleccionado = cursos.get(cursoIndex);

                if (cursoSeleccionado.getGrupos() == null || cursoSeleccionado.getGrupos().isEmpty()) {
                    JOptionPane.showMessageDialog(ventanaMatricula,
                        "El curso seleccionado no tiene grupos disponibles",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Grupos grupoSeleccionado = cursoSeleccionado.getGrupos().get(grupoIndex);

                // Llamar al método matricularCurso del estudiante
                String resultado = estudiante.matricularCurso(cursoSeleccionado, grupoSeleccionado);

                if (resultado.equals("Matriculado satisfactoriamente.")) {
                    // Guardar cambios
                    guardarEstudiantesEnArchivo();
                    guardarCursosEnArchivo();

                    JOptionPane.showMessageDialog(ventanaMatricula,
                        resultado,
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    ventanaMatricula.dispose();
                } else {
                    JOptionPane.showMessageDialog(ventanaMatricula,
                        resultado,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventanaMatricula.dispose();
            }
        });

        panelBotones.add(btnMatricular);
        panelBotones.add(btnCancelar);
        panelPrincipal.add(panelBotones);

        ventanaMatricula.add(panelPrincipal);
        ventanaMatricula.setVisible(true);
    }

    private void mostrarLoginEstudiantes() {
        // Cerrar ventana actual
        this.dispose();

        // Crear ventana de login
        JFrame ventanaLogin = new JFrame("Login - Estudiantes");
        ventanaLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaLogin.setSize(400, 300);
        ventanaLogin.setLocationRelativeTo(null);

        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titulo
        JLabel labelTitulo = new JLabel("Inicio de Sesión - Estudiantes");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(labelTitulo);
        panelPrincipal.add(Box.createVerticalStrut(20));

        // Campo de identificacion
        JLabel labelIdent = new JLabel("Identificación:");
        labelIdent.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(labelIdent);

        JTextField txtIdentificacion = new JTextField(20);
        txtIdentificacion.setMaximumSize(new Dimension(250, 25));
        txtIdentificacion.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(txtIdentificacion);
        panelPrincipal.add(Box.createVerticalStrut(10));

        // Campo de contraseña
        JLabel labelPass = new JLabel("Contraseña:");
        labelPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(labelPass);

        JPasswordField txtContrasena = new JPasswordField(20);
        txtContrasena.setMaximumSize(new Dimension(250, 25));
        txtContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(txtContrasena);
        panelPrincipal.add(Box.createVerticalStrut(20));

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton btnIngresar = new JButton("Ingresar");
        JButton btnVolver = new JButton("Volver");

        // Action listener para el boton Ingresar
        btnIngresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String identificacion = txtIdentificacion.getText().trim();
                String contrasena = new String(txtContrasena.getPassword());

                if (identificacion.isEmpty() || contrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(ventanaLogin,
                        "Por favor complete todos los campos",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Buscar estudiante y validar credenciales
                estudianteAutenticado = null;
                for (Estudiantes est : estudiantes) {
                    if (est.ingresoSistema(identificacion, contrasena)) {
                        estudianteAutenticado = est;
                        break;
                    }
                }

                if (estudianteAutenticado != null) {
                    ventanaLogin.dispose();
                    abrirVentanaEstudiantes(estudianteAutenticado);
                } else {
                    JOptionPane.showMessageDialog(ventanaLogin,
                        "Identificación o contraseña incorrecta",
                        "Error de autenticación",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action listener para el boton Volver
        btnVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventanaLogin.dispose();
                new Sistemadematriculaycalificaciones().setVisible(true);
            }
        });

        panelBotones.add(btnIngresar);
        panelBotones.add(btnVolver);
        panelPrincipal.add(panelBotones);
        panelPrincipal.add(Box.createVerticalStrut(10));

        // Botón cambiar contraseña
        JButton btnCambiarContrasena = new JButton("Cambiar Contraseña");
        btnCambiarContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCambiarContrasena.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                recuperarContrasenaEstudiante();
            }
        });
        panelPrincipal.add(btnCambiarContrasena);

        ventanaLogin.add(panelPrincipal);
        ventanaLogin.setVisible(true);
    }

    private void mostrarLoginProfesores() {
        // Cerrar ventana actual
        this.dispose();

        // Crear ventana de login
        JFrame ventanaLogin = new JFrame("Login - Profesores");
        ventanaLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaLogin.setSize(400, 300);
        ventanaLogin.setLocationRelativeTo(null);

        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titulo
        JLabel labelTitulo = new JLabel("Inicio de Sesión - Profesores");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(labelTitulo);
        panelPrincipal.add(Box.createVerticalStrut(20));

        // Campo de identificacion
        JLabel labelIdent = new JLabel("Identificación:");
        labelIdent.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(labelIdent);

        JTextField txtIdentificacion = new JTextField(20);
        txtIdentificacion.setMaximumSize(new Dimension(250, 25));
        txtIdentificacion.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(txtIdentificacion);
        panelPrincipal.add(Box.createVerticalStrut(10));

        // Campo de contraseña
        JLabel labelPass = new JLabel("Contraseña:");
        labelPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(labelPass);

        JPasswordField txtContrasena = new JPasswordField(20);
        txtContrasena.setMaximumSize(new Dimension(250, 25));
        txtContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(txtContrasena);
        panelPrincipal.add(Box.createVerticalStrut(20));

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton btnIngresar = new JButton("Ingresar");
        JButton btnVolver = new JButton("Volver");

        // Action listener para el boton Ingresar
        btnIngresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String identificacion = txtIdentificacion.getText().trim();
                String contrasena = new String(txtContrasena.getPassword());

                if (identificacion.isEmpty() || contrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(ventanaLogin,
                        "Por favor complete todos los campos",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Buscar profesor y validar credenciales
                profesorAutenticado = null;
                for (Profesores prof : profesores) {
                    if (prof.ingresoSistema(identificacion, contrasena)) {
                        profesorAutenticado = prof;
                        break;
                    }
                }

                if (profesorAutenticado != null) {
                    ventanaLogin.dispose();
                    abrirVentanaProfesores(profesorAutenticado);
                } else {
                    JOptionPane.showMessageDialog(ventanaLogin,
                        "Identificación o contraseña incorrecta",
                        "Error de autenticación",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action listener para el boton Volver
        btnVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventanaLogin.dispose();
                new Sistemadematriculaycalificaciones().setVisible(true);
            }
        });

        panelBotones.add(btnIngresar);
        panelBotones.add(btnVolver);
        panelPrincipal.add(panelBotones);
        panelPrincipal.add(Box.createVerticalStrut(10));

        // Botón cambiar contraseña
        JButton btnCambiarContrasena = new JButton("Cambiar Contraseña");
        btnCambiarContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCambiarContrasena.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                recuperarContrasenaProfesor();
            }
        });
        panelPrincipal.add(btnCambiarContrasena);

        ventanaLogin.add(panelPrincipal);
        ventanaLogin.setVisible(true);
    }

    private void mostrarCursosProfesor(Profesores profesor) {
        JFrame ventanaCursos = new JFrame("Mis Cursos Impartidos");
        ventanaCursos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaCursos.setSize(700, 500);
        ventanaCursos.setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Título
        JLabel labelTitulo = new JLabel("Mis Cursos Impartidos");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(labelTitulo);
        panelPrincipal.add(Box.createVerticalStrut(20));

        // Obtener cursos usando el método de la clase Profesores
        List<Grupos> cursosImpartidos = profesor.getCursosImpartidos();

        // Verificar si tiene grupos asignados
        if (cursosImpartidos == null || cursosImpartidos.isEmpty()) {
            JLabel labelNoCursos = new JLabel("No tienes cursos asignados");
            labelNoCursos.setFont(new Font("Arial", Font.PLAIN, 14));
            labelNoCursos.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelPrincipal.add(labelNoCursos);
        } else {
            // Mostrar cada curso que imparte
            for (Grupos grupo : cursosImpartidos) {
                Cursos curso = grupo.getCurso();

                // Panel para cada curso
                JPanel panelCurso = new JPanel();
                panelCurso.setLayout(new BoxLayout(panelCurso, BoxLayout.Y_AXIS));
                panelCurso.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 1),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
                panelCurso.setMaximumSize(new Dimension(600, 220));
                panelCurso.setAlignmentX(Component.CENTER_ALIGNMENT);

                // Nombre del curso
                JLabel labelNombreCurso = new JLabel("Curso: " + curso.getNombre());
                labelNombreCurso.setFont(new Font("Arial", Font.BOLD, 16));
                labelNombreCurso.setAlignmentX(Component.LEFT_ALIGNMENT);
                panelCurso.add(labelNombreCurso);
                panelCurso.add(Box.createVerticalStrut(5));

                // Información del curso
                JLabel labelId = new JLabel("ID: " + curso.getIdentificacion());
                labelId.setFont(new Font("Arial", Font.PLAIN, 14));
                labelId.setAlignmentX(Component.LEFT_ALIGNMENT);
                panelCurso.add(labelId);

                JLabel labelGrupo = new JLabel("Grupo: " + grupo.getIdentificacionGrupo());
                labelGrupo.setFont(new Font("Arial", Font.PLAIN, 14));
                labelGrupo.setAlignmentX(Component.LEFT_ALIGNMENT);
                panelCurso.add(labelGrupo);

                JLabel labelModalidad = new JLabel("Modalidad: " + curso.getModalidad());
                labelModalidad.setFont(new Font("Arial", Font.PLAIN, 14));
                labelModalidad.setAlignmentX(Component.LEFT_ALIGNMENT);
                panelCurso.add(labelModalidad);

                JLabel labelHoras = new JLabel("Horas por día: " + curso.getHorasPorDia());
                labelHoras.setFont(new Font("Arial", Font.PLAIN, 14));
                labelHoras.setAlignmentX(Component.LEFT_ALIGNMENT);
                panelCurso.add(labelHoras);

                JLabel labelTipo = new JLabel("Tipo: " + curso.getTipo());
                labelTipo.setFont(new Font("Arial", Font.PLAIN, 14));
                labelTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
                panelCurso.add(labelTipo);

                // Cantidad de estudiantes en el grupo
                int cantidadEstudiantes = grupo.getEstudiantes() != null ? grupo.getEstudiantes().size() : 0;
                JLabel labelEstudiantes = new JLabel("Estudiantes matriculados: " + cantidadEstudiantes + "/" + curso.getMaxEstudiantes());
                labelEstudiantes.setFont(new Font("Arial", Font.PLAIN, 14));
                labelEstudiantes.setAlignmentX(Component.LEFT_ALIGNMENT);
                panelCurso.add(labelEstudiantes);

                panelPrincipal.add(panelCurso);
                panelPrincipal.add(Box.createVerticalStrut(15));
            }
        }

        // Botón cerrar
        panelPrincipal.add(Box.createVerticalStrut(10));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventanaCursos.dispose();
            }
        });
        panelPrincipal.add(btnCerrar);

        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        ventanaCursos.add(scrollPane);
        ventanaCursos.setVisible(true);
    }

    private void abrirVentanaProfesores(Profesores profesor) {
        // Cerrar ventana actual
        this.dispose();
        cargarEvaluacionesDesdeArchivo();
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
            public void actionPerformed(ActionEvent e) {
                // Cerrar ventana actual y abrir la principal
                ventanaProfesores.dispose();
                new Sistemadematriculaycalificaciones().setVisible(true);
            }
        });
        JButton btnInfoGeneral = new JButton("Info General");
        btnInfoGeneral.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarInfoProfesor(profesor);
            }
        });
        JButton btnEvaluaciones = new JButton("Evaluaciones");
        btnEvaluaciones.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventanaProfesores.dispose();
                abriVentanaEvaluaciones();
            }
        });

        JButton btnVerMisCursos = new JButton("Ver Mis Cursos");
        btnVerMisCursos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarCursosProfesor(profesor);
            }
        });

        // Panel para el botón
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnVolver1);
        panelBoton.add(btnInfoGeneral);
        panelBoton.add(btnEvaluaciones);
        panelBoton.add(btnVerMisCursos);
        ventanaProfesores.add(panelBoton, BorderLayout.CENTER);

        ventanaProfesores.setVisible(true);
        }
    private void mostrarCursosMatriculados(Estudiantes estudiante) {
        JFrame ventanaCursos = new JFrame("Mis Cursos Matriculados");
        ventanaCursos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaCursos.setSize(700, 500);
        ventanaCursos.setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Título
        JLabel labelTitulo = new JLabel("Mis Cursos Matriculados");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(labelTitulo);
        panelPrincipal.add(Box.createVerticalStrut(20));

        // Obtener cursos usando el método de la clase Estudiantes
        List<Grupos> cursosMatriculados = estudiante.getCursosMatriculados();

        // Verificar si tiene cursos matriculados
        if (cursosMatriculados == null || cursosMatriculados.isEmpty()) {
            JLabel labelNoCursos = new JLabel("No tienes cursos matriculados");
            labelNoCursos.setFont(new Font("Arial", Font.PLAIN, 14));
            labelNoCursos.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelPrincipal.add(labelNoCursos);
        } else {
            // Mostrar cada curso matriculado
            for (Grupos grupo : cursosMatriculados) {
                Cursos curso = grupo.getCurso();

                // Panel para cada curso
                JPanel panelCurso = new JPanel();
                panelCurso.setLayout(new BoxLayout(panelCurso, BoxLayout.Y_AXIS));
                panelCurso.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 1),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
                panelCurso.setMaximumSize(new Dimension(600, 200));
                panelCurso.setAlignmentX(Component.CENTER_ALIGNMENT);

                // Nombre del curso
                JLabel labelNombreCurso = new JLabel("Curso: " + curso.getNombre());
                labelNombreCurso.setFont(new Font("Arial", Font.BOLD, 16));
                labelNombreCurso.setAlignmentX(Component.LEFT_ALIGNMENT);
                panelCurso.add(labelNombreCurso);
                panelCurso.add(Box.createVerticalStrut(5));

                // Información del curso
                JLabel labelId = new JLabel("ID: " + curso.getIdentificacion());
                labelId.setFont(new Font("Arial", Font.PLAIN, 14));
                labelId.setAlignmentX(Component.LEFT_ALIGNMENT);
                panelCurso.add(labelId);

                JLabel labelGrupo = new JLabel("Grupo: " + grupo.getIdentificacionGrupo());
                labelGrupo.setFont(new Font("Arial", Font.PLAIN, 14));
                labelGrupo.setAlignmentX(Component.LEFT_ALIGNMENT);
                panelCurso.add(labelGrupo);

                JLabel labelModalidad = new JLabel("Modalidad: " + curso.getModalidad());
                labelModalidad.setFont(new Font("Arial", Font.PLAIN, 14));
                labelModalidad.setAlignmentX(Component.LEFT_ALIGNMENT);
                panelCurso.add(labelModalidad);

                JLabel labelHoras = new JLabel("Horas por día: " + curso.getHorasPorDia());
                labelHoras.setFont(new Font("Arial", Font.PLAIN, 14));
                labelHoras.setAlignmentX(Component.LEFT_ALIGNMENT);
                panelCurso.add(labelHoras);

                JLabel labelTipo = new JLabel("Tipo: " + curso.getTipo());
                labelTipo.setFont(new Font("Arial", Font.PLAIN, 14));
                labelTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
                panelCurso.add(labelTipo);

                panelPrincipal.add(panelCurso);
                panelPrincipal.add(Box.createVerticalStrut(15));
            }
        }

        // Botón cerrar
        panelPrincipal.add(Box.createVerticalStrut(10));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventanaCursos.dispose();
            }
        });
        panelPrincipal.add(btnCerrar);

        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        ventanaCursos.add(scrollPane);
        ventanaCursos.setVisible(true);
    }

    private void abrirVentanaEstudiantes(Estudiantes estudiante) {
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
            public void actionPerformed(ActionEvent e) {
                // Cerrar ventana actual y abrir la principal
                ventanaEstudiantes.dispose();
                new Sistemadematriculaycalificaciones().setVisible(true);
            }
        });

        JButton btnInfoGeneral = new JButton("Info General");
        btnInfoGeneral.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarInfoEstudiante(estudiante);
            }
        });

        JButton btnMatricularCurso = new JButton("Matricular Curso");
        btnMatricularCurso.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarVentanaMatricular(estudiante);
            }
        });

        JButton btnVerMisCursos = new JButton("Ver Mis Cursos");
        btnVerMisCursos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarCursosMatriculados(estudiante);
            }
        });

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnVolver1);
        panelBoton.add(btnInfoGeneral);
        panelBoton.add(btnMatricularCurso);
        panelBoton.add(btnVerMisCursos);
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
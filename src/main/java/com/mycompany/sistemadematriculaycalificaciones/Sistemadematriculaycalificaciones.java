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
            @Override
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
        guardarCursosEnArchivo(); // Guardar cambios
        // Limpiar campos
        txtIdentCur.setText("");
        txtIdentGru.setText("");
        txtIdentPro.setText("");
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
        JButton btnVolver = new JButton("Volver");
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

        // Panel para los botones
        JPanel panelBoton = new JPanel(new GridLayout(2, 1, 10, 10));
        panelBoton.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton btnIngresarSistema = new JButton("Ingresar al Sistema");
        btnIngresarSistema.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirLoginProfesores(ventanaProfesores);
            }
        });

        JButton btnVolver1 = new JButton("Regresar");
        btnVolver1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cerrar ventana actual y abrir la principal
                ventanaProfesores.dispose();
                new Sistemadematriculaycalificaciones().setVisible(true);
            }
        });

        panelBoton.add(btnIngresarSistema);
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

        // Panel para los botones
        JPanel panelBoton = new JPanel(new GridLayout(2, 1, 10, 10));
        panelBoton.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton btnIngresarSistema = new JButton("Ingresar al Sistema");
        btnIngresarSistema.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirLoginEstudiantes(ventanaEstudiantes);
            }
        });

        JButton btnVolver1 = new JButton("Regresar");
        btnVolver1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Cerrar ventana actual y abrir la principal
                ventanaEstudiantes.dispose();
                new Sistemadematriculaycalificaciones().setVisible(true);
            }
        });

        panelBoton.add(btnIngresarSistema);
        panelBoton.add(btnVolver1);
        ventanaEstudiantes.add(panelBoton, BorderLayout.CENTER);

        ventanaEstudiantes.setVisible(true);
    }

    private void abrirLoginProfesores(JFrame ventanaAnterior) {
        // Crear diálogo de login
        JDialog dialogLogin = new JDialog(ventanaAnterior, "Login de Profesores", true);
        dialogLogin.setSize(400, 250);
        dialogLogin.setLocationRelativeTo(ventanaAnterior);
        dialogLogin.setLayout(new BorderLayout());

        // Panel principal
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel lblTitulo = new JLabel("Ingreso de Profesores", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelPrincipal.add(lblTitulo, gbc);

        // Campo de identificación
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panelPrincipal.add(new JLabel("Identificación:"), gbc);

        gbc.gridx = 1;
        JTextField txtIdentificacion = new JTextField(20);
        panelPrincipal.add(txtIdentificacion, gbc);

        // Campo de contraseña
        gbc.gridy = 2;
        gbc.gridx = 0;
        panelPrincipal.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1;
        JPasswordField txtContrasena = new JPasswordField(20);
        panelPrincipal.add(txtContrasena, gbc);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnIngresar = new JButton("Ingresar");
        JButton btnCancelar = new JButton("Cancelar");

        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String identificacion = txtIdentificacion.getText().trim();
                String contrasena = new String(txtContrasena.getPassword());

                if (identificacion.isEmpty() || contrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(dialogLogin,
                        "Por favor ingrese identificación y contraseña",
                        "Campos vacíos",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Buscar el profesor en la lista
                Profesores profesorEncontrado = null;
                for (Profesores profesor : profesores) {
                    if (profesor.ingresoSistema(identificacion, contrasena)) {
                        profesorEncontrado = profesor;
                        break;
                    }
                }

                if (profesorEncontrado != null) {
                    JOptionPane.showMessageDialog(dialogLogin,
                        "Bienvenido/a " + profesorEncontrado.getNombre() + " " +
                        profesorEncontrado.getApellido1(),
                        "Ingreso exitoso",
                        JOptionPane.INFORMATION_MESSAGE);
                    dialogLogin.dispose();
                    // Aquí puedes agregar código para abrir la ventana del profesor autenticado
                } else {
                    JOptionPane.showMessageDialog(dialogLogin,
                        "Identificación o contraseña incorrecta",
                        "Error de autenticación",
                        JOptionPane.ERROR_MESSAGE);
                    txtContrasena.setText("");
                }
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogLogin.dispose();
            }
        });

        panelBotones.add(btnIngresar);
        panelBotones.add(btnCancelar);

        dialogLogin.add(panelPrincipal, BorderLayout.CENTER);
        dialogLogin.add(panelBotones, BorderLayout.SOUTH);

        dialogLogin.setVisible(true);
    }

    private void abrirLoginEstudiantes(JFrame ventanaAnterior) {
        // Crear diálogo de login
        JDialog dialogLogin = new JDialog(ventanaAnterior, "Login de Estudiantes", true);
        dialogLogin.setSize(400, 250);
        dialogLogin.setLocationRelativeTo(ventanaAnterior);
        dialogLogin.setLayout(new BorderLayout());

        // Panel principal
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel lblTitulo = new JLabel("Ingreso de Estudiantes", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelPrincipal.add(lblTitulo, gbc);

        // Campo de identificación
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panelPrincipal.add(new JLabel("Identificación:"), gbc);

        gbc.gridx = 1;
        JTextField txtIdentificacion = new JTextField(20);
        panelPrincipal.add(txtIdentificacion, gbc);

        // Campo de contraseña
        gbc.gridy = 2;
        gbc.gridx = 0;
        panelPrincipal.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1;
        JPasswordField txtContrasena = new JPasswordField(20);
        panelPrincipal.add(txtContrasena, gbc);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnIngresar = new JButton("Ingresar");
        JButton btnCancelar = new JButton("Cancelar");

        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String identificacion = txtIdentificacion.getText().trim();
                String contrasena = new String(txtContrasena.getPassword());

                if (identificacion.isEmpty() || contrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(dialogLogin,
                        "Por favor ingrese identificación y contraseña",
                        "Campos vacíos",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Buscar el estudiante en la lista
                Estudiantes estudianteEncontrado = null;
                for (Estudiantes estudiante : estudiantes) {
                    if (estudiante.ingresoSistema(identificacion, contrasena)) {
                        estudianteEncontrado = estudiante;
                        break;
                    }
                }

                if (estudianteEncontrado != null) {
                    JOptionPane.showMessageDialog(dialogLogin,
                        "Bienvenido/a " + estudianteEncontrado.getNombre() + " " +
                        estudianteEncontrado.getApellido1(),
                        "Ingreso exitoso",
                        JOptionPane.INFORMATION_MESSAGE);
                    dialogLogin.dispose();
                    // Aquí puedes agregar código para abrir la ventana del estudiante autenticado
                } else {
                    JOptionPane.showMessageDialog(dialogLogin,
                        "Identificación o contraseña incorrecta",
                        "Error de autenticación",
                        JOptionPane.ERROR_MESSAGE);
                    txtContrasena.setText("");
                }
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogLogin.dispose();
            }
        });

        panelBotones.add(btnIngresar);
        panelBotones.add(btnCancelar);

        dialogLogin.add(panelPrincipal, BorderLayout.CENTER);
        dialogLogin.add(panelBotones, BorderLayout.SOUTH);

        dialogLogin.setVisible(true);
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
package Biblioteca_Final_POO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.List;

public class BibliotecaGUI extends JFrame {
    private JTextField loginEmailField, loginPasswordField;
    private JTextField userIdField, userNameField, userEmailField, userPasswordField, userRoleField;
    private JTextField bookIdField, bookTitleField, bookAuthorField, bookPublisherField, bookStateField;
    private JTextField solicitanteCodigoField, solicitanteNombreField;
    private JTextField loanBookIdField, loanUserIdField, loanDateField, returnDateField;
    private JTextField reservationBookIdField, reservationUserIdField, reservationDateField, reservationIdField;
    private JTextArea resultArea;
    private Usuario usuarioActual;

    public BibliotecaGUI() {
        setTitle("Gestión de Biblioteca");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        showLoginPanel();
    }

    private void showLoginPanel() {
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));

        loginPanel.add(new JLabel("Email:"));
        loginEmailField = new JTextField();
        loginPanel.add(loginEmailField);

        loginPanel.add(new JLabel("Contraseña:"));
        loginPasswordField = new JPasswordField();
        loginPanel.add(loginPasswordField);

        JButton loginButton = new JButton("Iniciar sesión");
        loginButton.addActionListener(new LoginAction());
        loginPanel.add(loginButton);

        add(loginPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void showMainPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();

        if (usuarioActual.getRol().equals("administrador")) {
            tabbedPane.addTab("Usuarios", createUserPanel());
        }

        tabbedPane.addTab("Libros", createBookPanel());
        tabbedPane.addTab("Préstamos", createLoanPanel());
        tabbedPane.addTab("Reservas", createReservationPanel());

        resultArea = new JTextArea(10, 50);  // Limita la altura del área de texto
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Cerrar sesión");
        logoutButton.addActionListener(new LogoutAction());
        add(logoutButton, BorderLayout.NORTH);

        add(tabbedPane, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new GridLayout(9, 2));

        // User input fields
        panel.add(new JLabel("ID del Usuario:"));
        userIdField = new JTextField();
        panel.add(userIdField);

        panel.add(new JLabel("Nombre:"));
        userNameField = new JTextField();
        panel.add(userNameField);

        panel.add(new JLabel("Email:"));
        userEmailField = new JTextField();
        panel.add(userEmailField);

        panel.add(new JLabel("Contraseña:"));
        userPasswordField = new JTextField();
        panel.add(userPasswordField);

        panel.add(new JLabel("Rol:"));
        userRoleField = new JTextField();
        panel.add(userRoleField);

        // Buttons for user operations
        JButton consultarButton = new JButton("Consultar");
        consultarButton.addActionListener(new ConsultarUsuarioAction());
        panel.add(consultarButton);

        JButton actualizarButton = new JButton("Actualizar");
        actualizarButton.addActionListener(new ActualizarUsuarioAction());
        panel.add(actualizarButton);

        JButton eliminarButton = new JButton("Eliminar");
        eliminarButton.addActionListener(new EliminarUsuarioAction());
        panel.add(eliminarButton);

        JButton agregarButton = new JButton("Agregar");
        agregarButton.addActionListener(new AgregarUsuarioAction());
        panel.add(agregarButton);

        JButton listarButton = new JButton("Listar Usuarios");
        listarButton.addActionListener(new ListarUsuariosAction());
        panel.add(listarButton);

        return panel;
    }

    private JPanel createBookPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(6, 2));

        // Book input fields
        inputPanel.add(new JLabel("ID del Libro:"));
        bookIdField = new JTextField();
        inputPanel.add(bookIdField);

        inputPanel.add(new JLabel("Título:"));
        bookTitleField = new JTextField();
        inputPanel.add(bookTitleField);

        inputPanel.add(new JLabel("Autor:"));
        bookAuthorField = new JTextField();
        inputPanel.add(bookAuthorField);

        inputPanel.add(new JLabel("Editorial:"));
        bookPublisherField = new JTextField();
        inputPanel.add(bookPublisherField);

        inputPanel.add(new JLabel("Estado:"));
        bookStateField = new JTextField();
        inputPanel.add(bookStateField);

        panel.add(new JScrollPane(inputPanel), BorderLayout.CENTER);

        // Buttons for book operations
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        
        if (usuarioActual.getRol().equals("administrador")) {
            JButton agregarButton = new JButton("Agregar");
            agregarButton.addActionListener(new AgregarLibroAction());
            buttonPanel.add(agregarButton);

            JButton eliminarButton = new JButton("Eliminar");
            eliminarButton.addActionListener(new EliminarLibroAction());
            buttonPanel.add(eliminarButton);
        }

        JButton listarDisponiblesButton = new JButton("Listar Disponibles");
        listarDisponiblesButton.addActionListener(new ListarLibrosDisponiblesAction());
        buttonPanel.add(listarDisponiblesButton);

        JButton listarPrestadosButton = new JButton("Listar Libros Prestados");
        listarPrestadosButton.addActionListener(new ListarLibrosPrestadosAction());
        buttonPanel.add(listarPrestadosButton);

        JButton listarReservadosButton = new JButton("Listar Libros Reservados");
        listarReservadosButton.addActionListener(new ListarLibrosReservadosAction());
        buttonPanel.add(listarReservadosButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createLoanPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(10, 2));

        // Loan input fields
        inputPanel.add(new JLabel("ID del Libro:"));
        loanBookIdField = new JTextField();
        inputPanel.add(loanBookIdField);

        inputPanel.add(new JLabel("ID del Usuario:"));
        loanUserIdField = new JTextField();
        inputPanel.add(loanUserIdField);

        inputPanel.add(new JLabel("Código del Solicitante:"));
        solicitanteCodigoField = new JTextField();
        inputPanel.add(solicitanteCodigoField);

        inputPanel.add(new JLabel("Nombre del Solicitante:"));
        solicitanteNombreField = new JTextField();
        inputPanel.add(solicitanteNombreField);

        inputPanel.add(new JLabel("Fecha del Préstamo (YYYY-MM-DD):"));
        loanDateField = new JTextField();
        inputPanel.add(loanDateField);

        inputPanel.add(new JLabel("Fecha de Devolución (YYYY-MM-DD):"));
        returnDateField = new JTextField();
        inputPanel.add(returnDateField);

        panel.add(new JScrollPane(inputPanel), BorderLayout.CENTER);

        // Buttons for loan operations
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        JButton prestarButton = new JButton("Prestar Libro");
        prestarButton.addActionListener(new PrestarLibroAction());
        buttonPanel.add(prestarButton);

        JButton devolverButton = new JButton("Devolver Libro");
        devolverButton.addActionListener(new DevolverLibroAction());
        buttonPanel.add(devolverButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createReservationPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(10, 2));

        // Reservation input fields
        inputPanel.add(new JLabel("ID del Libro:"));
        reservationBookIdField = new JTextField();
        inputPanel.add(reservationBookIdField);

        inputPanel.add(new JLabel("ID del Usuario:"));
        reservationUserIdField = new JTextField();
        inputPanel.add(reservationUserIdField);

        inputPanel.add(new JLabel("Código del Solicitante:"));
        solicitanteCodigoField = new JTextField();
        inputPanel.add(solicitanteCodigoField);

        inputPanel.add(new JLabel("Nombre del Solicitante:"));
        solicitanteNombreField = new JTextField();
        inputPanel.add(solicitanteNombreField);

        inputPanel.add(new JLabel("Fecha de la Reserva (YYYY-MM-DD):"));
        reservationDateField = new JTextField();
        inputPanel.add(reservationDateField);

        panel.add(new JScrollPane(inputPanel), BorderLayout.CENTER);

        // Buttons for reservation operations
        JPanel buttonPanel = new JPanel(new GridLayout(1, 1));
        JButton reservarButton = new JButton("Reservar Libro");
        reservarButton.addActionListener(new ReservarLibroAction());
        buttonPanel.add(reservarButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Login action
    private class LoginAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String email = loginEmailField.getText();
            String contrasena = new String(((JPasswordField) loginPasswordField).getPassword());
            usuarioActual = autenticarUsuario(email, contrasena);

            if (usuarioActual == null) {
                JOptionPane.showMessageDialog(BibliotecaGUI.this, "Email o contraseña incorrectos.");
            } else {
                getContentPane().removeAll();
                showMainPanel();
                revalidate();
                repaint();
            }
        }

        private Usuario autenticarUsuario(String email, String contrasena) {
            try (Connection conn = ConexionBD.conectar();
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM usuarios WHERE email = ? AND contrasena = ?")) {
                stmt.setString(1, email);
                stmt.setString(2, contrasena);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    String rol = rs.getString("rol");
                    switch (rol) {
                        case "administrador":
                            return new Administrador(id, nombre, email, contrasena);
                        case "bibliotecario":
                            return new Bibliotecario(id, nombre, email, contrasena);
                        case "monitor":
                            return new MonitorBiblioteca(id, nombre, email, contrasena);
                        default:
                            return null;
                    }
                } else {
                    return null;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }

    // Logout action
    private class LogoutAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            usuarioActual = null;
            getContentPane().removeAll();
            showLoginPanel();
            revalidate();
            repaint();
        }
    }

    // ActionListener classes for Users
    private class ConsultarUsuarioAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int id = Integer.parseInt(userIdField.getText());
            String result = consultarUsuario(id);
            resultArea.setText(result);
        }

        private String consultarUsuario(int id) {
            try (Connection conn = ConexionBD.conectar();
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM usuarios WHERE id = ?")) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String email = rs.getString("email");
                    String contrasena = rs.getString("contrasena");
                    String rol = rs.getString("rol");
                    return "ID: " + id + "\nNombre: " + nombre + "\nEmail: " + email +
                            "\nContraseña: " + contrasena + "\nRol: " + rol;
                } else {
                    return "Usuario no encontrado.";
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return "Error al consultar el usuario.";
            }
        }
    }

    private class ActualizarUsuarioAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int id = Integer.parseInt(userIdField.getText());
            String nombre = userNameField.getText();
            String email = userEmailField.getText();
            String contrasena = userPasswordField.getText();
            String rol = userRoleField.getText();
            boolean success = actualizarUsuario(id, nombre, email, contrasena, rol);
            if (success) {
                resultArea.setText("Usuario actualizado correctamente.");
            } else {
                resultArea.setText("Error al actualizar el usuario.");
            }
        }

        private boolean actualizarUsuario(int id, String nombre, String email, String contrasena, String rol) {
            try (Connection conn = ConexionBD.conectar();
                 PreparedStatement stmt = conn.prepareStatement("UPDATE usuarios SET nombre = ?, email = ?, contrasena = ?, rol = ? WHERE id = ?")) {
                stmt.setString(1, nombre);
                stmt.setString(2, email);
                stmt.setString(3, contrasena);
                stmt.setString(4, rol);
                stmt.setInt(5, id);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        }
    }

    private class EliminarUsuarioAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int id = Integer.parseInt(userIdField.getText());
            boolean success = eliminarUsuario(id);
            if (success) {
                resultArea.setText("Usuario eliminado correctamente.");
            } else {
                resultArea.setText("Error al eliminar el usuario.");
            }
        }

        private boolean eliminarUsuario(int id) {
            try (Connection conn = ConexionBD.conectar();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM usuarios WHERE id = ?")) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        }
    }

    private class AgregarUsuarioAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String nombre = userNameField.getText();
            String email = userEmailField.getText();
            String contrasena = userPasswordField.getText();
            String rol = userRoleField.getText();
            ConexionBD.registrarUsuario(nombre, email, contrasena, rol);
            resultArea.setText("Usuario agregado correctamente.");
        }
    }

    private class ListarUsuariosAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            List<Usuario> usuarios = ConexionBD.listarUsuarios();
            StringBuilder result = new StringBuilder("Usuarios:\n");
            for (Usuario usuario : usuarios) {
                result.append("ID: ").append(usuario.getId())
                      .append(", Nombre: ").append(usuario.getNombre())
                      .append(", Email: ").append(usuario.getEmail())
                      .append(", Rol: ").append(usuario.getRol()).append("\n");
            }
            resultArea.setText(result.toString());
        }
    }

    // ActionListener classes for Books
    private class ConsultarLibroAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int id = Integer.parseInt(bookIdField.getText());
            String result = consultarLibro(id);
            resultArea.setText(result);
        }

        private String consultarLibro(int id) {
            try (Connection conn = ConexionBD.conectar();
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM libros WHERE id = ?")) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String titulo = rs.getString("titulo");
                    String autor = rs.getString("autor");
                    String editorial = rs.getString("editorial");
                    String estado = rs.getString("estado");
                    return "ID: " + id + "\nTítulo: " + titulo + "\nAutor: " + autor +
                            "\nEditorial: " + editorial + "\nEstado: " + estado;
                } else {
                    return "Libro no encontrado.";
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return "Error al consultar el libro.";
            }
        }
    }

    private class ActualizarLibroAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int id = Integer.parseInt(bookIdField.getText());
            String titulo = bookTitleField.getText();
            String autor = bookAuthorField.getText();
            String editorial = bookPublisherField.getText();
            String estado = bookStateField.getText();
            boolean success = actualizarLibro(id, titulo, autor, editorial, estado);
            if (success) {
                resultArea.setText("Libro actualizado correctamente.");
            } else {
                resultArea.setText("Error al actualizar el libro.");
            }
        }

        private boolean actualizarLibro(int id, String titulo, String autor, String editorial, String estado) {
            try (Connection conn = ConexionBD.conectar();
                 PreparedStatement stmt = conn.prepareStatement("UPDATE libros SET titulo = ?, autor = ?, editorial = ?, estado = ? WHERE id = ?")) {
                stmt.setString(1, titulo);
                stmt.setString(2, autor);
                stmt.setString(3, editorial);
                stmt.setString(4, estado);
                stmt.setInt(5, id);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        }
    }

    private class EliminarLibroAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int id = Integer.parseInt(bookIdField.getText());
            boolean success = eliminarLibro(id);
            if (success) {
                resultArea.setText("Libro eliminado correctamente.");
            } else {
                resultArea.setText("Error al eliminar el libro.");
            }
        }

        private boolean eliminarLibro(int id) {
            try (Connection conn = ConexionBD.conectar();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM libros WHERE id = ?")) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        }
    }

    private class AgregarLibroAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String titulo = bookTitleField.getText();
            String autor = bookAuthorField.getText();
            String editorial = bookPublisherField.getText();
            ConexionBD.insertarLibro(titulo, autor, editorial);
            resultArea.setText("Libro agregado correctamente.");
        }
    }

    private class ListarLibrosDisponiblesAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            List<Libro> libros = ConexionBD.obtenerLibrosPorEstado("disponible");
            StringBuilder result = new StringBuilder("Libros Disponibles:\n");
            for (Libro libro : libros) {
                result.append("ID: ").append(libro.getId())
                      .append(", Título: ").append(libro.getTitulo())
                      .append(", Autor: ").append(libro.getAutor())
                      .append(", Editorial: ").append(libro.getEditorial())
                      .append(", Estado: ").append(libro.getEstado()).append("\n");
            }
            resultArea.setText(result.toString());
        }
    }

    private class ListarLibrosPrestadosAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            List<LibroPrestado> libros = ConexionBD.obtenerLibrosPrestados();
            StringBuilder result = new StringBuilder("Libros Prestados:\n");
            for (LibroPrestado libro : libros) {
                result.append("ID: ").append(libro.getId())
                      .append(", Título: ").append(libro.getTitulo())
                      .append(", Autor: ").append(libro.getAutor())
                      .append(", Editorial: ").append(libro.getEditorial())
                      .append(", Estado: ").append(libro.getEstado())
                      .append(", Solicitante Código: ").append(libro.getSolicitanteCodigo())
                      .append(", Solicitante Nombre: ").append(libro.getSolicitanteNombre()).append("\n");
            }
            resultArea.setText(result.toString());
        }
    }

    private class ListarLibrosReservadosAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            List<LibroReservado> libros = ConexionBD.obtenerLibrosReservados();
            StringBuilder result = new StringBuilder("Libros Reservados:\n");
            for (LibroReservado libro : libros) {
                result.append("ID: ").append(libro.getId())
                      .append(", Título: ").append(libro.getTitulo())
                      .append(", Autor: ").append(libro.getAutor())
                      .append(", Editorial: ").append(libro.getEditorial())
                      .append(", Estado: ").append(libro.getEstado())
                      .append(", Solicitante Código: ").append(libro.getSolicitanteCodigo())
                      .append(", Solicitante Nombre: ").append(libro.getSolicitanteNombre()).append("\n");
            }
            resultArea.setText(result.toString());
        }
    }

    // ActionListener classes for Loans
    private class ConsultarPrestamoAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int id = Integer.parseInt(loanBookIdField.getText());
                String result = consultarPrestamo(id);
                resultArea.setText(result);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(BibliotecaGUI.this, "ID del libro no válido.");
            }
        }

        private String consultarPrestamo(int id) {
            try (Connection conn = ConexionBD.conectar();
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM prestamos WHERE id = ?")) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int libroId = rs.getInt("libro_id");
                    int usuarioId = rs.getInt("usuario_id");
                    Date fechaPrestamo = rs.getDate("fecha_prestamo");
                    Date fechaDevolucion = rs.getDate("fecha_devolucion");
                    return "ID: " + id + "\nID del Libro: " + libroId + "\nID del Usuario: " + usuarioId +
                            "\nFecha del Préstamo: " + fechaPrestamo + "\nFecha de Devolución: " + fechaDevolucion;
                } else {
                    return "Préstamo no encontrado.";
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return "Error al consultar el préstamo.";
            }
        }
    }

    private class ActualizarPrestamoAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int id = Integer.parseInt(loanBookIdField.getText());
                int libroId = Integer.parseInt(loanBookIdField.getText());
                int usuarioId = Integer.parseInt(loanUserIdField.getText());
                Date fechaPrestamo = Date.valueOf(loanDateField.getText());
                Date fechaDevolucion = Date.valueOf(returnDateField.getText());
                boolean success = actualizarPrestamo(id, libroId, usuarioId, fechaPrestamo, fechaDevolucion);
                if (success) {
                    resultArea.setText("Préstamo actualizado correctamente.");
                } else {
                    resultArea.setText("Error al actualizar el préstamo.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(BibliotecaGUI.this, "Datos de préstamo no válidos.");
            }
        }

        private boolean actualizarPrestamo(int id, int libroId, int usuarioId, Date fechaPrestamo, Date fechaDevolucion) {
            try (Connection conn = ConexionBD.conectar();
                 PreparedStatement stmt = conn.prepareStatement("UPDATE prestamos SET libro_id = ?, usuario_id = ?, fecha_prestamo = ?, fecha_devolucion = ? WHERE id = ?")) {
                stmt.setInt(1, libroId);
                stmt.setInt(2, usuarioId);
                stmt.setDate(3, fechaPrestamo);
                stmt.setDate(4, fechaDevolucion);
                stmt.setInt(5, id);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        }
    }

    private class EliminarPrestamoAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int id = Integer.parseInt(loanBookIdField.getText());
                boolean success = eliminarPrestamo(id);
                if (success) {
                    resultArea.setText("Préstamo eliminado correctamente.");
                } else {
                    resultArea.setText("Error al eliminar el préstamo.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(BibliotecaGUI.this, "ID de préstamo no válido.");
            }
        }

        private boolean eliminarPrestamo(int id) {
            try (Connection conn = ConexionBD.conectar();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM prestamos WHERE id = ?")) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        }
    }

    private class AgregarPrestamoAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int libroId = Integer.parseInt(loanBookIdField.getText());
                int usuarioId = Integer.parseInt(loanUserIdField.getText());
                String solicitanteCodigo = solicitanteCodigoField.getText();
                String solicitanteNombre = solicitanteNombreField.getText();
                ConexionBD.prestarLibro(libroId, usuarioId, solicitanteCodigo, solicitanteNombre);
                resultArea.setText("Préstamo agregado correctamente.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(BibliotecaGUI.this, "Datos de préstamo no válidos.");
            }
        }
    }

    // ActionListener classes for Reservations
    private class ConsultarReservaAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int id = Integer.parseInt(reservationIdField.getText());
                String result = consultarReserva(id);
                resultArea.setText(result);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(BibliotecaGUI.this, "ID de reserva no válido.");
            }
        }

        private String consultarReserva(int id) {
            try (Connection conn = ConexionBD.conectar();
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM reservas WHERE id = ?")) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int libroId = rs.getInt("libro_id");
                    int usuarioId = rs.getInt("usuario_id");
                    Date fechaReserva = rs.getDate("fecha_reserva");
                    return "ID: " + id + "\nID del Libro: " + libroId + "\nID del Usuario: " + usuarioId +
                            "\nFecha de la Reserva: " + fechaReserva;
                } else {
                    return "Reserva no encontrada.";
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return "Error al consultar la reserva.";
            }
        }
    }

    private class ActualizarReservaAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int id = Integer.parseInt(reservationIdField.getText());
                int libroId = Integer.parseInt(reservationBookIdField.getText());
                int usuarioId = Integer.parseInt(reservationUserIdField.getText());
                Date fechaReserva = Date.valueOf(reservationDateField.getText());
                boolean success = actualizarReserva(id, libroId, usuarioId, fechaReserva);
                if (success) {
                    resultArea.setText("Reserva actualizada correctamente.");
                } else {
                    resultArea.setText("Error al actualizar la reserva.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(BibliotecaGUI.this, "Datos de reserva no válidos.");
            }
        }

        private boolean actualizarReserva(int id, int libroId, int usuarioId, Date fechaReserva) {
            try (Connection conn = ConexionBD.conectar();
                 PreparedStatement stmt = conn.prepareStatement("UPDATE reservas SET libro_id = ?, usuario_id = ?, fecha_reserva = ? WHERE id = ?")) {
                stmt.setInt(1, libroId);
                stmt.setInt(2, usuarioId);
                stmt.setDate(3, fechaReserva);
                stmt.setInt(4, id);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        }
    }

    private class EliminarReservaAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int id = Integer.parseInt(reservationIdField.getText());
                boolean success = eliminarReserva(id);
                if (success) {
                    resultArea.setText("Reserva eliminada correctamente.");
                } else {
                    resultArea.setText("Error al eliminar la reserva.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(BibliotecaGUI.this, "ID de reserva no válido.");
            }
        }

        private boolean eliminarReserva(int id) {
            try (Connection conn = ConexionBD.conectar();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM reservas WHERE id = ?")) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        }
    }

    private class AgregarReservaAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int libroId = Integer.parseInt(reservationBookIdField.getText());
                int usuarioId = Integer.parseInt(reservationUserIdField.getText());
                String solicitanteCodigo = solicitanteCodigoField.getText();
                String solicitanteNombre = solicitanteNombreField.getText();
                ConexionBD.reservarLibro(libroId, usuarioId, solicitanteCodigo, solicitanteNombre);
                resultArea.setText("Reserva agregada correctamente.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(BibliotecaGUI.this, "Datos de reserva no válidos.");
            }
        }
    }

    private class PrestarLibroAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int libroId = Integer.parseInt(loanBookIdField.getText());
                int usuarioId = Integer.parseInt(loanUserIdField.getText());
                String solicitanteCodigo = solicitanteCodigoField.getText();
                String solicitanteNombre = solicitanteNombreField.getText();
                ConexionBD.prestarLibro(libroId, usuarioId, solicitanteCodigo, solicitanteNombre);
                resultArea.setText("Intento de préstamo realizado.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(BibliotecaGUI.this, "Datos de préstamo no válidos.");
            }
        }
    }

    private class DevolverLibroAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int libroId = Integer.parseInt(loanBookIdField.getText());
                String solicitanteCodigo = solicitanteCodigoField.getText();
                String solicitanteNombre = solicitanteNombreField.getText();
                ConexionBD.devolverLibro(libroId, solicitanteCodigo, solicitanteNombre);
                resultArea.setText("Intento de devolución realizado.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(BibliotecaGUI.this, "Datos de devolución no válidos.");
            }
        }
    }

    private class ReservarLibroAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int libroId = Integer.parseInt(reservationBookIdField.getText());
                int usuarioId = Integer.parseInt(reservationUserIdField.getText());
                String solicitanteCodigo = solicitanteCodigoField.getText();
                String solicitanteNombre = solicitanteNombreField.getText();
                ConexionBD.reservarLibro(libroId, usuarioId, solicitanteCodigo, solicitanteNombre);
                resultArea.setText("Reserva agregada correctamente.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(BibliotecaGUI.this, "Datos de reserva no válidos.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BibliotecaGUI::new);
    }
}

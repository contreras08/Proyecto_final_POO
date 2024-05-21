package Biblioteca_Final_POO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USER = "root";
    private static final String PASSWORD = "Daniel3214059327.";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void registrarUsuario(String nombre, String email, String contrasena, String rol) {
        String sql = "INSERT INTO usuarios (nombre, email, contrasena, rol) VALUES (?, ?, ?, ?)";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, email);
            stmt.setString(3, contrasena);
            stmt.setString(4, rol);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Connection conn = conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String contrasena = rs.getString("contrasena");
                String rol = rs.getString("rol");
                usuarios.add(new Usuario(id, nombre, email, contrasena, rol));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public static void insertarLibro(String titulo, String autor, String editorial) {
        String sql = "INSERT INTO libros (titulo, autor, editorial) VALUES (?, ?, ?)";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, titulo);
            stmt.setString(2, autor);
            stmt.setString(3, editorial);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Libro> obtenerLibrosPorEstado(String estado) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros WHERE estado = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estado);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                libros.add(new Libro(id, titulo, autor, editorial, estado));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libros;
    }

    public static List<Libro> listarTodosLosLibros() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros";
        try (Connection conn = conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                String estado = rs.getString("estado");
                libros.add(new Libro(id, titulo, autor, editorial, estado));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libros;
    }


    public static List<LibroPrestado> obtenerLibrosPrestados() {
        List<LibroPrestado> libros = new ArrayList<>();
        String sql = "SELECT l.id, l.titulo, l.autor, l.editorial, l.estado, p.solicitante_codigo, p.solicitante_nombre " +
                     "FROM libros l JOIN prestamos p ON l.id = p.libro_id WHERE p.fecha_devolucion IS NULL";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                String estado = rs.getString("estado");
                String solicitanteCodigo = rs.getString("solicitante_codigo");
                String solicitanteNombre = rs.getString("solicitante_nombre");
                libros.add(new LibroPrestado(id, titulo, autor, editorial, estado, solicitanteCodigo, solicitanteNombre));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libros;
    }

    public static List<LibroReservado> obtenerLibrosReservados() {
        List<LibroReservado> libros = new ArrayList<>();
        String sql = "SELECT l.id, l.titulo, l.autor, l.editorial, l.estado, r.solicitante_codigo, r.solicitante_nombre " +
                     "FROM libros l JOIN reservas r ON l.id = r.libro_id WHERE l.estado = 'reservado'";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                String estado = rs.getString("estado");
                String solicitanteCodigo = rs.getString("solicitante_codigo");
                String solicitanteNombre = rs.getString("solicitante_nombre");
                libros.add(new LibroReservado(id, titulo, autor, editorial, estado, solicitanteCodigo, solicitanteNombre));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libros;
    }

    public static void prestarLibro(int libroId, int usuarioId, String solicitanteCodigo, String solicitanteNombre) {
        if (!puedePrestar(libroId, solicitanteCodigo, solicitanteNombre)) {
            System.out.println("El libro está prestado o reservado por otra persona y no se puede prestar en este momento.");
            JOptionPane.showMessageDialog(null, "El libro está prestado o reservado por otra persona y no se puede prestar en este momento.");
            return;
        }

        String sql = "INSERT INTO prestamos (libro_id, usuario_id, solicitante_codigo, solicitante_nombre, fecha_prestamo) VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, libroId);
            stmt.setInt(2, usuarioId);
            stmt.setString(3, solicitanteCodigo);
            stmt.setString(4, solicitanteNombre);
            stmt.executeUpdate();
            actualizarEstadoLibro(libroId, "prestado");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean puedePrestar(int libroId, String solicitanteCodigo, String solicitanteNombre) {
        String sql = "SELECT l.estado, r.solicitante_codigo, r.solicitante_nombre " +
                     "FROM libros l LEFT JOIN reservas r ON l.id = r.libro_id WHERE l.id = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, libroId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String estado = rs.getString("estado");
                String codigoReserva = rs.getString("solicitante_codigo");
                String nombreReserva = rs.getString("solicitante_nombre");
                if ("disponible".equalsIgnoreCase(estado)) {
                    return true;
                }
                if ("reservado".equalsIgnoreCase(estado)) {
                    if (solicitanteCodigo.equals(codigoReserva) && solicitanteNombre.equals(nombreReserva)) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void devolverLibro(int libroId, String solicitanteCodigo, String solicitanteNombre) {
        String sqlVerificar = "SELECT * FROM prestamos WHERE libro_id = ? AND solicitante_codigo = ? AND solicitante_nombre = ? AND fecha_devolucion IS NULL";
        String sqlDevolver = "UPDATE prestamos SET fecha_devolucion = NOW() WHERE libro_id = ? AND solicitante_codigo = ? AND solicitante_nombre = ? AND fecha_devolucion IS NULL";
        try (Connection conn = conectar(); PreparedStatement stmtVerificar = conn.prepareStatement(sqlVerificar); PreparedStatement stmtDevolver = conn.prepareStatement(sqlDevolver)) {
            stmtVerificar.setInt(1, libroId);
            stmtVerificar.setString(2, solicitanteCodigo);
            stmtVerificar.setString(3, solicitanteNombre);
            ResultSet rs = stmtVerificar.executeQuery();
            if (rs.next()) {
                stmtDevolver.setInt(1, libroId);
                stmtDevolver.setString(2, solicitanteCodigo);
                stmtDevolver.setString(3, solicitanteNombre);
                stmtDevolver.executeUpdate();
                actualizarEstadoLibro(libroId, "disponible");
                verificarRetrasoYAplicarSancion(libroId, rs.getInt("usuario_id"));
            } else {
                System.out.println("El libro no puede ser devuelto por este solicitante.");
                JOptionPane.showMessageDialog(null, "El libro no puede ser devuelto por este solicitante.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void reservarLibro(int libroId, int usuarioId, String solicitanteCodigo, String solicitanteNombre) {
        if (esLibroPrestadoOReservado(libroId)) {
            System.out.println("El libro está prestado o reservado y no se puede reservar en este momento.");
            JOptionPane.showMessageDialog(null, "El libro está prestado o reservado y no se puede reservar en este momento.");
            return;
        }

        String sql = "INSERT INTO reservas (libro_id, usuario_id, solicitante_codigo, solicitante_nombre, fecha_reserva) VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, libroId);
            stmt.setInt(2, usuarioId);
            stmt.setString(3, solicitanteCodigo);
            stmt.setString(4, solicitanteNombre);
            stmt.executeUpdate();
            actualizarEstadoLibro(libroId, "reservado");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void cancelarReservacion(int libroId, String solicitanteCodigo, String solicitanteNombre) {
        String sql = "DELETE FROM reservas WHERE libro_id = ? AND solicitante_codigo = ? AND solicitante_nombre = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, libroId);
            stmt.setString(2, solicitanteCodigo);
            stmt.setString(3, solicitanteNombre);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                actualizarEstadoLibro(libroId, "disponible");
            } else {
                System.out.println("No se encontró la reservación para cancelar.");
                JOptionPane.showMessageDialog(null, "No se encontró la reservación para cancelar.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void actualizarEstadoLibro(int libroId, String estado) {
        String sql = "UPDATE libros SET estado = ? WHERE id = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estado);
            stmt.setInt(2, libroId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void verificarRetrasoYAplicarSancion(int libroId, int usuarioId) {
        String sql = "SELECT fecha_prestamo, fecha_devolucion FROM prestamos WHERE libro_id = ? AND usuario_id = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, libroId);
            stmt.setInt(2, usuarioId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Date fechaPrestamo = rs.getDate("fecha_prestamo");
                Date fechaDevolucion = rs.getDate("fecha_devolucion");
                long diffInMillis = fechaDevolucion.getTime() - fechaPrestamo.getTime();
                long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);
                if (diffInDays > 5) {
                    sancionarUsuario(usuarioId, "Retraso en la devolución del libro");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void sancionarUsuario(int usuarioId, String motivo) {
        String sql = "UPDATE usuarios SET sancionado = true, motivo_sancion = ? WHERE id = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, motivo);
            stmt.setInt(2, usuarioId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean esLibroPrestadoOReservado(int libroId) {
        String sql = "SELECT estado FROM libros WHERE id = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, libroId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String estado = rs.getString("estado");
                return "prestado".equalsIgnoreCase(estado) || "reservado".equalsIgnoreCase(estado);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

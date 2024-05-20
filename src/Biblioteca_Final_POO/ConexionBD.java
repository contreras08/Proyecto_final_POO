package Biblioteca_Final_POO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static void prestarLibro(int libroId, int usuarioId, String solicitanteCodigo, String solicitanteNombre) {
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

    public static void devolverLibro(int libroId, int usuarioId) {
        String sql = "UPDATE prestamos SET fecha_devolucion = NOW() WHERE libro_id = ? AND usuario_id = ? AND fecha_devolucion IS NULL";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, libroId);
            stmt.setInt(2, usuarioId);
            stmt.executeUpdate();
            actualizarEstadoLibro(libroId, "disponible");
            verificarRetrasoYAplicarSancion(libroId, usuarioId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void reservarLibro(int libroId, int usuarioId, String solicitanteCodigo, String solicitanteNombre) {
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
}





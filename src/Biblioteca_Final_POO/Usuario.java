package Biblioteca_Final_POO;

public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String contrasena;
    private String rol;
    private boolean sancionado;
    private String motivoSancion; // Añadir motivo de sanción

    // Constructor
    public Usuario(int id, String nombre, String email, String contrasena, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.rol = rol;
        this.sancionado = false; // Valor predeterminado
        this.motivoSancion = "";
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getRol() {
        return rol;
    }

    public boolean isSancionado() {
        return sancionado;
    }

    public void setSancionado(boolean sancionado, String motivo) {
        this.sancionado = sancionado;
        this.motivoSancion = motivo;
    }

    public String getMotivoSancion() {
        return motivoSancion;
    }
}


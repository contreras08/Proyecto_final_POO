package Biblioteca_Final_POO;


public class Libro {
    private int id;
    private String titulo;
    private String autor;
    private String editorial;
    private String estado; // disponible, prestado, reservado

    // Constructor
    public Libro(int id, String titulo, String autor, String editorial, String estado) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.estado = estado;
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getEditorial() { return editorial; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}


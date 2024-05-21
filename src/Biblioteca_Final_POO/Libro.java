package Biblioteca_Final_POO;

public class Libro {
    private int id;
    private String titulo;
    private String autor;
    private String editorial;
    private String estado;
    private String solicitanteCodigo;
    private String solicitanteNombre;

    // Constructor con todos los campos
    public Libro(int id, String titulo, String autor, String editorial, String estado, String solicitanteCodigo, String solicitanteNombre) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.estado = estado;
        this.solicitanteCodigo = solicitanteCodigo;
        this.solicitanteNombre = solicitanteNombre;
    }

    // Constructor sin solicitante
    public Libro(int id, String titulo, String autor, String editorial, String estado) {
        this(id, titulo, autor, editorial, estado, null, null);
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getEditorial() { return editorial; }
    public String getEstado() { return estado; }
    public String getSolicitanteCodigo() { return solicitanteCodigo; }
    public String getSolicitanteNombre() { return solicitanteNombre; }
    public void setEstado(String estado) { this.estado = estado; }
}


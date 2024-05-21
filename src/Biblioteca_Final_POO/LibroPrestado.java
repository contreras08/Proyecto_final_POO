package Biblioteca_Final_POO;

public class LibroPrestado extends Libro {
    private String solicitanteCodigo;
    private String solicitanteNombre;

    public LibroPrestado(int id, String titulo, String autor, String editorial, String estado, String solicitanteCodigo, String solicitanteNombre) {
        super(id, titulo, autor, editorial, estado);
        this.solicitanteCodigo = solicitanteCodigo;
        this.solicitanteNombre = solicitanteNombre;
    }

    public String getSolicitanteCodigo() {
        return solicitanteCodigo;
    }

    public String getSolicitanteNombre() {
        return solicitanteNombre;
    }
}

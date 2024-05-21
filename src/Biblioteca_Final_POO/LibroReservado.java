package Biblioteca_Final_POO;


public class LibroReservado extends Libro {
    private String solicitanteCodigo;
    private String solicitanteNombre;

    public LibroReservado(int id, String titulo, String autor, String editorial, String estado, String solicitanteCodigo, String solicitanteNombre) {
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


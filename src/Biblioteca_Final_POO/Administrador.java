package Biblioteca_Final_POO;

class Administrador extends Usuario {
    public Administrador(int id, String nombre, String email, String contrasena) {
        super(id, nombre, email, contrasena, "administrador");
    }
}

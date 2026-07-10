package main;
import dao.UsuarioDAO;

public class Main {
    public static void main(String[] args) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        usuarioDAO.inserir(null);
    }   
}

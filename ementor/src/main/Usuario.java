package main;
public class Usuario extends Pessoa {
    private int id; // Adicione o campo id
    private String nomeUsuario;
    private String senha;
    private NivelAcesso nivelAcesso; // 1 - Administrador, 2 - Professor, 3 - Aluno

    public Usuario() {
        // Construtor padrão
    }

    public void setDadosUsuario(String nomeUsuario, String senha, NivelAcesso nivelAcesso) {
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.nivelAcesso = nivelAcesso;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public NivelAcesso getNivelAcesso() {   
        return nivelAcesso;
    }

    public int getId() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }
}

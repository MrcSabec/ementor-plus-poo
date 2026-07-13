package main;
import java.util.Date;
public class Pessoa {
    protected String nome;
    protected Date nascimento;
    protected String cpf;
    protected String telefone;
    protected String rua;
    protected String bairro;
    protected String cidade;
    protected String estado;

    public Pessoa() {
    }

    public Pessoa(String nome, Date nascimento, String cpf, String telefone, String rua, String bairro, String cidade, String estado) {
        this.nome = nome;
        this.nascimento = nascimento;
        this.cpf = cpf;
        this.telefone = telefone;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }

    public String getBairro() {
        return bairro;
    }
    public String getCidade() {
        return cidade;
    }
    public String getNome() {
        return nome;
    }
    public Date getNascimento() {
        return nascimento;
    }
    public String getCpf() {
        return cpf;
    }
    public String getTelefone() {
        return telefone;
    }
    public String getRua() {
        return rua;
    }
    public String getEstado() {
        return estado;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setNascimento(Date nascimento){
        this.nascimento = nascimento;
    }

    public void setCpf(String cpf){
        this.cpf = cpf;
    }

    public void setTelefone(String telefone){
        this.telefone = telefone;
    }
    
    public void setRua(String rua){
        this.rua = rua;
    }

    public void setBairro(String bairro){
        this.bairro = bairro;
    }

    public void setEstado(String estado){
        this.estado = estado;
    }

    public void setCidade(String cidade){
        this.cidade = cidade;
    }

}

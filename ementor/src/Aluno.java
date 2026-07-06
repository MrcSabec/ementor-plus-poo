import javax.swing.*;
import java.util.Date;

public class Aluno extends Pessoa{
    private String matricula;
    private int periodo;
    private String turma;
    private double[] notas = new double[10];
    public Aluno(){};
    public Aluno(String nome, Date dataNascimento,String cpf,String telefone,String rua,String bairro,String cidade,String estado, String matricula, int periodo, String turma){
        super(nome, dataNascimento, cpf, telefone, rua, bairro, cidade, estado);
        this.matricula = matricula;
        this.periodo = periodo;
        this.turma = turma;
    }
    public void setDados(String nome, Date dataNascimento, String cpf, String telefone, String rua, String bairro, String cidade, String estado, String matricula, int periodo, String turma, double[] notas){
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.telefone = telefone;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;

        this.matricula = matricula;
        this.periodo = periodo;
        this.turma = turma;
        this.notas = notas;
    }
    public String getMatricula(){
        return this.matricula;
    }
    public int getPeriodo(){
        return this.periodo;
    }
    public void printAluno(){
        String texto = "Dados Aluno:\n" + "Nome: " + this.nome + "\n" +  "CPF: " + this.cpf + "\n" +  "Telefone: " + this.telefone  + "\n" + "Cidade/Estado: " + this.cidade + "/" + this.estado + "\n" + "------------------------\n" +  "Matricula: " + this.matricula + "\n" + "Periodo: " + this.periodo + "\n" + "Turma: " + this.turma;
        JOptionPane.showMessageDialog(null, texto);
    }
}

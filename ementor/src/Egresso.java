import javax.swing.*;
import java.util.Date;

public class Egresso extends Aluno{
    private String profissaoAtual;
    private double faixaSalarial;
    private String cursoAnterior;
    private String cursoAtual;
    public Egresso(){}
    public Egresso(String nome, Date dataNascimento, String cpf, String telefone, String rua, String bairro, String cidade, String estado,
                   String matricula, int periodo, String turma,
                   String profissaoAtual, double faixaSalarial, String cursoAnterior, String cursoAtual){
        super(nome, dataNascimento, cpf, telefone, rua, bairro, cidade, estado, matricula, periodo, turma);
        this.profissaoAtual = profissaoAtual;
        this.faixaSalarial = faixaSalarial;
        this.cursoAnterior = cursoAnterior;
        this.cursoAtual = cursoAtual;
    }
    public void setDados(String nome, Date dataNascimento, String cpf, String telefone, String rua, String bairro, String cidade, String estado,
                         String matricula, int periodo, String turma, double[] notas,
                         String profissaoAtual, double faixaSalarial, String cursoAnterior, String cursoAtual){
        //Classe Pessoa + Aluno
        super.setDados( nome,  dataNascimento,  cpf,  telefone,  rua,  bairro,  cidade,  estado, matricula,  periodo,  turma, notas);
        //Classe Egresso
        this.profissaoAtual = profissaoAtual;
        this.faixaSalarial = faixaSalarial;
        this.cursoAnterior = cursoAnterior;
        this.cursoAtual = cursoAtual;
    }
    @Override
    public void imprimirDados(){
        String texto = "Dados Aluno:\n" + "Nome: " + this.nome + "\n" +  "CPF: " + this.cpf + "\n" +  "Telefone: " + this.telefone  + "\n" + "Cidade/Estado: " + this.cidade + "/" + this.estado + "\n"
                + "------------------------\n" +
                "Matricula: " + getMatricula() + "\n" + "Periodo: " + getPeriodo() + "\n"
                + "------------------------\n" +
                "Profissao Atual: " + this.profissaoAtual + "\n" + "Faixa Salarial: " + this.faixaSalarial +"\n" +  "Curso Anterior: " + this.cursoAnterior + "\n" + "Profissao Atual: " + this.profissaoAtual + "\n" + "Curso Atual: " + this.cursoAtual;
        JOptionPane.showMessageDialog(null, texto);
    }
}

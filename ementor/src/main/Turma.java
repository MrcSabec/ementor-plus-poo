package main;
import java.util.ArrayList;

public class Turma {
    private String codigo;
    private String nome;
    private ArrayList <Aluno> alunos;
    public Turma(){}
    public Turma(String codigo, String nome){
        this.codigo = codigo;
        this.nome = nome;
        this.alunos = new ArrayList<>();
    }
    public void adicionarAluno(Aluno aluno) {
        alunos.add(aluno);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setAlunos(ArrayList <Aluno> alunos) {
        this.alunos = alunos;
    }

    public String toString(){
        return "Codigo:" + this.codigo + "Nome:" + this.nome;
    }
}

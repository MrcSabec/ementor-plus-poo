import java.util.ArrayList;

public class Turma {
    private String turma;
    private String nome;
    private ArrayList <Aluno> alunos;
    public Turma(){}
    public Turma(String turma, String nome){
        this.turma = turma;
        this.nome = nome;
        this.alunos = new ArrayList<>();
    }
    public void adicionarAluno(Aluno aluno) {
        alunos.add(aluno);
    }
}

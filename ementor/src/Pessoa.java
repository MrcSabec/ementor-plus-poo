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

    public Pessoa(){
        // Construtor padrão
    }

    // Construtor da classe Pessoa
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
}

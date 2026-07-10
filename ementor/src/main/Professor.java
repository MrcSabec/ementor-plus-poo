package main;
import java.util.Date;
public class Professor extends Pessoa {
    private Date dataAdmissao;
    private boolean cargoChefia;
    private boolean cargoCoordenacao;
    private double salarioBruto;
    
    public Professor() {
        // Construtor padrão
    }

    //Construtor para iniciar todos os atributos da classe Professor, incluindo os atributos herdados da classe Pessoa
    public Professor(String nome, Date nascimento, String cpf, String telefone, String rua, String bairro, String cidade, String estado,
                     Date dataAdmissao, boolean cargoChefia, boolean cargoCoordenacao, double salarioBruto) {
        super(nome, nascimento, cpf, telefone, rua, bairro, cidade, estado);
        this.dataAdmissao = dataAdmissao;
        this.cargoChefia = cargoChefia;
        this.cargoCoordenacao = cargoCoordenacao;
        this.salarioBruto = salarioBruto;
    }

    public void setDadosProfessor(Date dataAdmissao, boolean cargoChefia, boolean cargoCoordenacao, double salarioBruto) {
        this.dataAdmissao = dataAdmissao;
        this.cargoChefia = cargoChefia;
        this.cargoCoordenacao = cargoCoordenacao;
        this.salarioBruto = salarioBruto;
    }

    public Date getDataAdmissao() {
        return dataAdmissao;
    }

    public double getSalarioBruto() {
        return salarioBruto;
    }

    public void imprimirDadosProfessor() {
        System.out.println("Nome: " + nome);
        System.out.println("Data de Nascimento: " + nascimento);
        System.out.println("CPF: " + cpf);
        System.out.println("Telefone: " + telefone);
        System.out.println("Endereço: " + rua + ", " + bairro + ", " + cidade + ", " + estado);
        System.out.println("Data de Admissão: " + dataAdmissao);
        System.out.println("Cargo de Chefia: " + (cargoChefia ? "Sim" : "Não"));
        System.out.println("Cargo de Coordenação: " + (cargoCoordenacao ? "Sim" : "Não"));
        System.out.println("Salário Bruto: R$" + salarioBruto);
    }

    public void calcularSalarioBruto(){
        double salarioFinal = salarioBruto;

        // Aplicando aumento de 10% para cargo de chefia
        if (cargoChefia) {
            salarioFinal += salarioBruto * 0.10;
        }

        // Aplicando aumento de 5% para cargo de coordenação
        if (cargoCoordenacao) {
            salarioFinal += salarioBruto * 0.05;
        }

        System.out.println("Salário Bruto: R$" + salarioFinal);
    }

    public void calcularSalarioLiquido() {
        double salarioLiquido = salarioBruto;

        //desconto de 22,5% para IRRF
        if(salarioBruto >= 5000) {
            salarioLiquido -= salarioBruto * 0.225;
        }

        // Aplicando desconto de 14% para INSS
        salarioLiquido -= salarioBruto * 0.14;

        System.out.println("Salário Líquido: R$" + salarioLiquido);
    }
}

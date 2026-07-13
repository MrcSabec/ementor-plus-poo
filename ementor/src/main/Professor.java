package main;
import java.util.Date;
public class Professor extends Pessoa {
    private Date dataAdmissao;
    private boolean cargoChefia;
    private boolean cargoCoordenacao;
    private double salarioBruto;
    
    public Professor() {
    }

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

    public boolean getCargoChefia(){
        return cargoChefia;
    }

    public boolean getCargoCoordenacao(){
        return cargoCoordenacao;
    }

    public void setSalarioBruto(double salarioBruto) {
        this.salarioBruto = salarioBruto;
    }
    public void setCargoChefia(boolean cargoChefia) {
        this.cargoChefia = cargoChefia;
    }
    public void setCargoCoordenacao(boolean cargoCoordenacao) {
        this.cargoCoordenacao = cargoCoordenacao;
    }
    public void setDataAdmissao(Date dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
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

        // Adicional de chefia (10%)
        if (cargoChefia) {
            salarioFinal += salarioBruto * 0.10;
        }

        // Adicional de coordenação (5%)
        if (cargoCoordenacao) {
            salarioFinal += salarioBruto * 0.05;
        }

        System.out.println("Salário Bruto: R$" + salarioFinal);
    }

    public void calcularSalarioLiquido() {
        double salarioLiquido = salarioBruto;

        // Desconto de IRRF (22,5%) para faixa salarial aplicável
        if(salarioBruto >= 5000) {
            salarioLiquido -= salarioBruto * 0.225;
        }

        // Desconto de INSS (14%)
        salarioLiquido -= salarioBruto * 0.14;

        System.out.println("Salário Líquido: R$" + salarioLiquido);
    }

    public double getSalarioLiquido() {
        double salarioFinalBruto = salarioBruto;
        double salarioLiquido = salarioFinalBruto;

        // Desconto de IRRF (22,5%) para salários iguais ou superiores a R$5.000,00
        if(salarioFinalBruto >= 5000) {
            salarioLiquido -= salarioFinalBruto * 0.225;
        }

        // Desconto de INSS (14%)
        salarioLiquido -= salarioFinalBruto * 0.14;

        return salarioLiquido;
    }
}

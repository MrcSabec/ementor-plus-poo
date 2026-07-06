import java.util.Date;
public class Professor extends Pessoa {
    private Date dataAdmissao;
    private boolean cargoChefia;
    private boolean cargoCoordenacao;
    private double salarioBruto;
    
    public Professor() {
        // Construtor padrão
    }

    public void setDadosProfessor(Date dataAdmissao, boolean cargoChefia, boolean cargoCoordenacao, double salarioBruto) {
        this.dataAdmissao = dataAdmissao;
        this.cargoChefia = cargoChefia;
        this.cargoCoordenacao = cargoCoordenacao;
        this.salarioBruto = salarioBruto;
    }
}

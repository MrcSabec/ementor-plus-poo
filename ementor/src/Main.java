import dao.PessoaDAO;

public class Main {
    public static void main(String[] args) {
        // Testando cadastro de uma pessoa no banco de dados
        PessoaDAO pessoaDAO = new PessoaDAO();
        pessoaDAO.salvarPessoa("João Silva", new java.util.Date(), "1234567890", "123456789", "Rua A", "Bairro B", "Cidade C", "AA");    
    }
}

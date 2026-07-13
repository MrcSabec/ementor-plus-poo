import java.util.Date;
import main.Egresso;
import main.Turma;
import dao.EgressoDAO;

public class TesteEgressoDAO {
    public static void main(String[] args) {
        try {
            System.out.println("Iniciando teste...");
            Turma t = new Turma();
            t.setCodigo("T01"); // Assume that Turma T01 exists
            t.setNome("Turma Teste");

            // Cria um novo egresso completamente do zero
            Egresso e = new Egresso(
                "Teste " + System.currentTimeMillis(), // Nome
                new Date(), // Nasc
                "11111111111", // CPF
                "999999999", // Tel
                "Rua X", "Bairro Y", "Cidade Z", "SP",
                "MAT12345", // Matricula
                1, t, 
                "Programador", 5000.0, "Nenhum", "Ciencia"
            );

            EgressoDAO dao = new EgressoDAO();
            dao.inserir(e);
            System.out.println("Teste finalizado com sucesso.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

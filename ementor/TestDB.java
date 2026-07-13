import java.util.Date;
import main.Professor;
import dao.ProfessorDAO;

public class TestDB {
    public static void main(String[] args) {
        try {
            Professor p = new Professor();
            p.setNome("Test");
            p.setCpf("12345678901");
            p.setTelefone("11999999999");
            p.setRua("Rua Test");
            p.setBairro("Bairro Test");
            p.setCidade("Cidade Test");
            p.setEstado("ST");
            p.setNascimento(new Date());
            p.setDataAdmissao(new Date());
            p.setCargoChefia(false);
            p.setCargoCoordenacao(true);
            p.setSalarioBruto(1000.0);
            
            ProfessorDAO dao = new ProfessorDAO();
            dao.inserir(p);
            System.out.println("SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        }
    }
}

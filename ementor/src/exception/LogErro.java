package exception;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class LogErro {

    private static final String ARQUIVO = "erros.dat";

    public static void registrar(CodigoErro erro, Exception e) {

        try (FileWriter writer = new FileWriter(ARQUIVO, true);
        BufferedWriter bw = new BufferedWriter(writer);
        PrintWriter out = new PrintWriter(bw)) {
            out.println("================================");
            out.println(LocalDateTime.now());
            out.println("Código: " + erro.getCodigo());
            out.println("Descrição: " + erro.getDescricao());
            out.println("Mensagem: " + e.getMessage());

            out.println();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
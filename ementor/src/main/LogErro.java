import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class LogErro {

    public static void registrar(String codigo, String descricao) {

        try (FileWriter writer = new FileWriter("erros.dat", true)) {

            writer.write("Data: " + LocalDateTime.now() + "\n");
            writer.write("Código: " + codigo + "\n");
            writer.write("Descrição: " + descricao + "\n");
            writer.write("---------------------------------\n");

        } catch (IOException e) {
            System.out.println("Não foi possível gravar o log de erros.");
        }
    }
}
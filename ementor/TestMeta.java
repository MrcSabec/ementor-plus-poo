import database.Conexao;
import java.sql.*;

public class TestMeta {
    public static void main(String[] args) {
        try (Connection c = Conexao.getConnection();
             ResultSet rs = c.getMetaData().getColumns(null, null, "egresso", null)) {
            while (rs.next()) {
                System.out.println(rs.getString("COLUMN_NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

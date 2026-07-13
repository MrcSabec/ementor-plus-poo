package database;

import java.sql.Connection;
import java.sql.DriverManager;
import com.mysql.cj.jdbc.Driver;

import exception.*;

public class Conexao {
    private static final String URL = "jdbc:mysql://localhost:3306/ementor_plus";
    private static final String USUARIO = "ementor";
    private static final String SENHA = "123456";

    public static Connection getConnection() {
        try {
            DriverManager.registerDriver(new Driver());
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (Exception e) {
            throw new DAOException(CodigoErro.ERRO_CONEXAO, e);  
        }
    }
}

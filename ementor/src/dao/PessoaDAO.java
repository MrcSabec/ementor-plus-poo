package dao;

import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PessoaDAO {    
    public void salvarPessoa(String nome, Date nascimento, String cpf, String telefone, String rua, String bairro, String cidade, String estado) {
        String sql = "INSERT INTO pessoa (nome, nascimento, cpf, telefone, rua, bairro, cidade, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = database.Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nome);
            statement.setDate(2, new java.sql.Date(nascimento.getTime()));
            statement.setString(3, cpf);
            statement.setString(4, telefone);
            statement.setString(5, rua);
            statement.setString(6, bairro);
            statement.setString(7, cidade);
            statement.setString(8, estado);
            statement.executeUpdate();
            System.out.println("Pessoa salva com sucesso no banco de dados!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


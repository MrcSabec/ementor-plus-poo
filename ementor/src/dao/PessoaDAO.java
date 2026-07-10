package dao;

import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;

import main.Pessoa;

public class PessoaDAO {    
    public void inserir(Pessoa pessoa) {
        String sql = "INSERT INTO pessoa (nome, nascimento, cpf, telefone, rua, bairro, cidade, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = database.Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, pessoa.getNome());
            statement.setDate(2, new java.sql.Date(pessoa.getNascimento().getTime()));
            statement.setString(3, pessoa.getCpf());
            statement.setString(4, pessoa.getTelefone());
            statement.setString(5, pessoa.getRua());
            statement.setString(6, pessoa.getBairro());
            statement.setString(7, pessoa.getCidade());
            statement.setString(8, pessoa.getEstado());
            statement.executeUpdate();
            System.out.println("Pessoa salva com sucesso no banco de dados!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void atualizar(Pessoa pessoa) {
        String sql = "UPDATE pessoa SET telefone = ?, rua = ?, bairro = ?, cidade = ?, estado = ? WHERE cpf = ?";
        try (Connection connection = database.Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, pessoa.getTelefone());
            statement.setString(2, pessoa.getRua());
            statement.setString(3, pessoa.getBairro());
            statement.setString(4, pessoa.getCidade());
            statement.setString(5, pessoa.getEstado());
            statement.setString(6, pessoa.getCpf());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Pessoa atualizada com sucesso no banco de dados!");
            } else {
                System.out.println("Pessoa não encontrada no banco de dados.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remover(String cpf) {
        String sql = "DELETE FROM pessoa WHERE cpf = ?";
        try (Connection connection = database.Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cpf);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Pessoa removida com sucesso do banco de dados!");
            } else {
                System.out.println("Pessoa não encontrada no banco de dados.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Pessoa buscar(String cpf){
        String sql = "SELECT * FROM pessoa WHERE cpf = ?";
        try (Connection connection = database.Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cpf);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String nome = resultSet.getString("nome");
                Date nascimento = resultSet.getDate("nascimento");
                String telefone = resultSet.getString("telefone");
                String rua = resultSet.getString("rua");
                String bairro = resultSet.getString("bairro");
                String cidade = resultSet.getString("cidade");
                String estado = resultSet.getString("estado");
                return new Pessoa(nome, nascimento, cpf, telefone, rua, bairro, cidade, estado);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null se a pessoa não for encontrada
    }

    public List<Pessoa> listar() {
        List<Pessoa> pessoas = new ArrayList<>();
        String sql = "SELECT * FROM pessoa";
        try (Connection connection = database.Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String nome = resultSet.getString("nome");
                Date nascimento = resultSet.getDate("nascimento");
                String cpf = resultSet.getString("cpf");
                String telefone = resultSet.getString("telefone");
                String rua = resultSet.getString("rua");
                String bairro = resultSet.getString("bairro");
                String cidade = resultSet.getString("cidade");
                String estado = resultSet.getString("estado");
                Pessoa pessoa = new Pessoa(nome, nascimento, cpf, telefone, rua, bairro, cidade, estado);
                pessoas.add(pessoa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null se não houver pessoas cadastradas
    }
}


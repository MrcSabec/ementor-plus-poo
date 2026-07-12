//NÃO ESTÁ UTILIZAVEL!!!!!!!!!

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;  
import java.sql.SQLException;

import main.Pessoa;
import main.Aluno;  

public class AlunoDAO {
    public void inserir(Aluno aluno) { //como aluno é herdado de pessoa não deveria inserir na tabela pessoa?
        String sql = "INSERT INTO pessoa (nome, nascimento, cpf, telefone, rua, bairro, cidade, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = database.Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, aluno.getNome());
            statement.setDate(2, new java.sql.Date(aluno.getNascimento().getTime()));
            statement.setString(3, aluno.getCpf());
            statement.setString(4, aluno.getTelefone());
            statement.setString(5, aluno.getRua());
            statement.setString(6, aluno.getBairro());
            statement.setString(7, aluno.getCidade());
            statement.setString(8, aluno.getEstado());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sqlAluno = "INSERT INTO aluno (matricula, periodo, turma, nota1, nota2, nota3, nota4, nota5, nota6, nota7, nota8, nota9, nota10) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = database.Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlAluno)) {
            statement.setString(1, aluno.getMatricula());
            statement.setInt(2, aluno.getPeriodo());
            statement.setString(3, aluno.getTurma());
            for (int i = 0; i < 10; i++) {
                statement.setDouble(i + 4, aluno.getNotas()[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void alterar(Aluno aluno){
        String sql = "UPDATE aluno SET telefone = ?, rua = ?, bairro = ?, cidade = ?, estado = ? WHERE cpf = ?";
        try (Connection connection = database.Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, aluno.getTelefone());
            statement.setString(2, aluno.getRua());
            statement.setString(3, aluno.getBairro());
            statement.setString(4, aluno.getCidade());
            statement.setString(5, aluno.getEstado());
            statement.setString(6, aluno.getCpf());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Aluno atualizado com sucesso no banco de dados!");
            } else {
                System.out.println("Aluno não encontrado no banco de dados.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void remover(String cpf) {
        String sql = "DELETE FROM aluno WHERE cpf = ?";
        try (Connection connection = database.Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cpf);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Aluno removido com sucesso do banco de dados!");
            } else {
                System.out.println("Aluno não encontrado no banco de dados.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**public Aluno buscar(String matricula){
        String sql = "SELECT * FROM aluno WHERE matricula = ?";
        try (Connection connection = database.Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, matricula);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Aluno aluno = new Aluno();
                aluno.setMatricula(resultSet.getString("matricula"));
                aluno.setPeriodo(resultSet.getInt("periodo"));
                aluno.setTurma(resultSet.getString("turma"));
                aluno.setNotas(getNotasFromResultSet(resultSet));
                return aluno;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    
    }*/
}

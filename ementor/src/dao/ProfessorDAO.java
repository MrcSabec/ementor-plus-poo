package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.Conexao;

import main.Professor;

public class ProfessorDAO {


    //Função responsavel por inserir um professor no banco de dados
    public void inserir(Professor professor) {
        Connection connection = null;
        try {
            connection = Conexao.getConnection();
            // Inicia a transação
            connection.setAutoCommit(false);
            PessoaDAO pessoaDAO = new PessoaDAO();
            pessoaDAO.inserir(connection, professor);

            String sql = """
            INSERT INTO professor
            (cpf_pessoa, data_admissao, cargo_chefia,
            cargo_coordenacao, salario_bruto)
            VALUES (?, ?, ?, ?, ?)
            """;

            try (PreparedStatement statementprofessor =
                        connection.prepareStatement(sql)) {

                statementprofessor.setString(1, professor.getCpf());
                statementprofessor.setDate(2, new java.sql.Date(professor.getDataAdmissao().getTime()));
                statementprofessor.setBoolean(3, professor.getCargoChefia());
                statementprofessor.setBoolean(4, professor.getCargoCoordenacao());
                statementprofessor.setDouble(5, professor.getSalarioBruto());
                
                statementprofessor.executeUpdate();
            }
            connection.commit();

            System.out.println("professor cadastrado com sucesso!");

        } catch (SQLException e) {

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            throw new RuntimeException("Erro ao cadastrar professor.", e);

        } finally {

            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //Funcao responsavel por alterar os dados de um professor do banco de dados, atraves da matricula informada no professor
    public void alterar(Connection connection, Professor professor){
        try{
            connection = Conexao.getConnection();
            connection.setAutoCommit(false);
            PessoaDAO pessoaDAO = new PessoaDAO();
            pessoaDAO.atualizar(connection, professor);
            
            String sql = """
                UPDATE professor 
                SET data_admissao = ?, cargo_chefia = ?,cargo_coordenacao = ?, salario_bruto = ? WHERE cpf_pessoa = ?
                """;
            
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDate(1, new java.sql.Date(professor.getDataAdmissao().getTime()));
                statement.setBoolean(2, professor.getCargoChefia());
                statement.setBoolean(3, professor.getCargoCoordenacao());
                statement.setDouble(4, professor.getSalarioBruto());
                statement.setString(5,professor.getCpf());
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("professor atualizado com sucesso no banco de dados!");
                } else {
                    System.out.println("professor não encontrado no banco de dados.");
                }
            } 
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new RuntimeException("Erro ao atualizar professor.", e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    
    private Professor criarProfessor(ResultSet rs) throws SQLException {

        Professor professor = new Professor();

        professor.setCpf(rs.getString("cpf"));
        professor.setNome(rs.getString("nome"));
        professor.setNascimento(rs.getDate("nascimento"));
        professor.setTelefone(rs.getString("telefone"));
        professor.setRua(rs.getString("rua"));
        professor.setBairro(rs.getString("bairro"));
        professor.setCidade(rs.getString("cidade"));
        professor.setEstado(rs.getString("estado"));

        professor.setSalarioBruto(rs.getDouble("salario_bruto"));
        professor.setCargoChefia(rs.getBoolean("cargo_chefia"));
        professor.setCargoCoordenacao(rs.getBoolean("cargo_coordenacao"));
        professor.setDataAdmissao(rs.getDate("data_admissao"));

        return professor;
    }

    public Professor buscar(String cpf) {
        String sql = """
            SELECT a.*,
            p.*
            FROM professor a
            JOIN pessoa p 
            ON a.cpf_pessoa = p.cpf 
            WHERE a.cpf_pessoa = ?
        """;

        try (Connection connection = Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cpf);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return criarProfessor(rs);
                }

                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar professor.", e);
        }
    }

    public List<Professor> listarprofessors(){
        List<Professor> professors = new ArrayList<>();

        String sql = """
                SELECT a.*,
                p.*
                FROM professor a 
                JOIN pessoa p ON a.cpf_pessoa = p.cpf 
                ORDER BY p.nome
                """;

        try (Connection connection = Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                professors.add(criarProfessor(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar professors.", e);
        }

        return professors;
    }
    
    public void remover(String cpf) {
        Connection connection = null;
        try {
            connection = Conexao.getConnection();
            connection.setAutoCommit(false);

            // Remove da tabela professor
            String sql = "DELETE FROM professor WHERE matricula = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, cpf);

                statement.executeUpdate();
            }

            // Remove da tabela pessoa
            PessoaDAO pessoaDAO = new PessoaDAO();
            pessoaDAO.remover(connection, cpf);

            connection.commit();

        } catch (SQLException e) {

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            throw new RuntimeException("Erro ao remover professor.", e);

        } finally {

            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


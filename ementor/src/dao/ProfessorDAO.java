package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.Conexao;
import exception.CodigoErro;
import exception.DAOException;
import main.Professor;

public class ProfessorDAO {

    public boolean existe(Connection connection, String cpf_pessoa) throws SQLException {
        String sql = "SELECT cpf_pessoa FROM professor WHERE cpf_pessoa = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cpf_pessoa);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        }
    }


    public void inserir(Professor professor) {
        Connection connection = null;
        try {
            connection = Conexao.getConnection();
            connection.setAutoCommit(false);
            PessoaDAO pessoaDAO = new PessoaDAO();
            if (pessoaDAO.buscar(connection, professor.getCpf()) != null) {
                pessoaDAO.atualizar(connection, professor);
            } else {
                pessoaDAO.inserir(connection, professor);
            }

            if (existe(connection, professor.getCpf())) {
                String sqlUpdate = """
                    UPDATE professor 
                    SET data_admissao = ?, cargo_chefia = ?, cargo_coordenacao = ?, salario_bruto = ? 
                    WHERE cpf_pessoa = ?
                    """;
                try (PreparedStatement statement = connection.prepareStatement(sqlUpdate)) {
                    statement.setDate(1, new java.sql.Date(professor.getDataAdmissao().getTime()));
                    statement.setInt(2, professor.getCargoChefia() ? 1 : 0);
                    statement.setInt(3, professor.getCargoCoordenacao() ? 1 : 0);
                    statement.setDouble(4, professor.getSalarioBruto());
                    statement.setString(5, professor.getCpf());
                    statement.executeUpdate();
                }
                System.out.println("Professor atualizado com sucesso no banco de dados!");
            } else {
                String sql = """
                INSERT INTO professor
                (cpf_pessoa, data_admissao, cargo_chefia,
                cargo_coordenacao, salario_bruto)
                VALUES (?, ?, ?, ?, ?)
                """;

                try (PreparedStatement statementprofessor = connection.prepareStatement(sql)) {
                    statementprofessor.setString(1, professor.getCpf());
                    statementprofessor.setDate(2, new java.sql.Date(professor.getDataAdmissao().getTime()));
                    statementprofessor.setInt(3, professor.getCargoChefia() ? 1 : 0);
                    statementprofessor.setInt(4, professor.getCargoCoordenacao() ? 1 : 0);
                    statementprofessor.setDouble(5, professor.getSalarioBruto());
                    
                    statementprofessor.executeUpdate();
                }
                System.out.println("Professor cadastrado com sucesso!");
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

            throw new DAOException(CodigoErro.ERRO_INSERIR, e);

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
    public void alterar(Professor professor){
        Connection connection = null;
        try{
            connection = Conexao.getConnection();
            connection.setAutoCommit(false);
            
            PessoaDAO pessoaDAO = new PessoaDAO();
            if (pessoaDAO.buscar(connection, professor.getCpf()) != null) {
                pessoaDAO.atualizar(connection, professor);
            } else {
                pessoaDAO.inserir(connection, professor);
            }
            
            if (existe(connection, professor.getCpf())) {
                String sql = """
                    UPDATE professor 
                    SET data_admissao = ?, cargo_chefia = ?,cargo_coordenacao = ?, salario_bruto = ? WHERE cpf_pessoa = ?
                    """;
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setDate(1, new java.sql.Date(professor.getDataAdmissao().getTime()));
                    statement.setInt(2, professor.getCargoChefia() ? 1 : 0);
                    statement.setInt(3, professor.getCargoCoordenacao() ? 1 : 0);
                    statement.setDouble(4, professor.getSalarioBruto());
                    statement.setString(5,professor.getCpf());
                    statement.executeUpdate();
                }
                System.out.println("Professor atualizado com sucesso no banco de dados!");
            } else {
                System.out.println("Professor não encontrado no banco de dados.");
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
            throw new DAOException(CodigoErro.ERRO_ALTERAR, e);
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
        professor.setCargoChefia(rs.getInt("cargo_chefia") == 1);
        professor.setCargoCoordenacao(rs.getInt("cargo_coordenacao") == 1);
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
            throw new DAOException(CodigoErro.ERRO_BUSCAR, e);
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
            throw new DAOException(CodigoErro.ERRO_LISTAR, e);
        }

        return professors;
    }
    
    public void remover(String cpf) {
        Connection connection = null;
        try {
            connection = Conexao.getConnection();
            connection.setAutoCommit(false);

            String sql = "DELETE FROM professor WHERE cpf_pessoa = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, cpf);

                statement.executeUpdate();
            }

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

            throw new DAOException(CodigoErro.ERRO_REMOVER, e);

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


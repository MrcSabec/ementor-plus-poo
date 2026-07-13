package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.Conexao;
import exception.CodigoErro;
import exception.DAOException;
import main.Turma;

public class TurmaDAO {
    public void inserir(Turma turma){

        Connection connection = null;

        try{
            connection = Conexao.getConnection();
            connection.setAutoCommit(false);

            String sql = "INSERT INTO turma (codigo, nome) VALUES (?, ?)";
            
            try (PreparedStatement statementTurma = connection.prepareStatement(sql)) {
                statementTurma.setString(1,turma.getCodigo());
                statementTurma.setString(2, turma.getNome());
            
                statementTurma.executeUpdate();
            }
            connection.commit();

        }catch (SQLException e) {

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
    
    public void alterar(Turma turma){
        Connection connection = null;
        try{
            connection = Conexao.getConnection();
            connection.setAutoCommit(false);
            
            String sql = "UPDATE turma SET nome = ? WHERE codigo = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1,turma.getNome());
                statement.setString(2,turma.getCodigo());
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Turma atualizado com sucesso no banco de dados!");
                } else {
                    System.out.println("Turma não encontrado no banco de dados.");
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

    public Turma buscaSimples(String codigo){

        String sql = """
                SELECT * FROM turma WHERE codigo = ?
                """;

        try (Connection connection = Conexao.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codigo);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Turma turmaBusca = new Turma();

                    turmaBusca.setCodigo(rs.getString("codigo"));
                    turmaBusca.setNome(rs.getString("nome"));
                    return turmaBusca;
                }

                return null;
            }
        } catch (SQLException e) {
            throw new DAOException(CodigoErro.ERRO_BUSCAR, e);
        }

    }


    public Turma buscaCompleta(String codigo){

        String sql = """
                SELECT * FROM turma WHERE codigo = ?
                """;

        try (Connection connection = Conexao.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codigo);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Turma turmaBusca = new Turma();

                    turmaBusca.setCodigo(rs.getString("codigo"));
                    turmaBusca.setNome(rs.getString("nome"));
                    AlunoDAO alunoDAO = new AlunoDAO();
                    turmaBusca.setAlunos(alunoDAO.listarPorTurma(codigo));
                    return turmaBusca;
                }

                return null;
            }
        } catch (SQLException e) {
            throw new DAOException(CodigoErro.ERRO_BUSCAR, e);
        }

    }
        
    public void remover (String codigo){
        Connection connection = null;
        try {
            connection = Conexao.getConnection();
            connection.setAutoCommit(false);

            String sql = "DELETE FROM turma WHERE codigo = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, codigo);

                statement.executeUpdate();
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

    public List<Turma> listarTurmas(){
        List<Turma> turmas = new ArrayList<>();

        String sql = """
                SELECT * FROM turma;
                """;

        try (Connection connection = Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            AlunoDAO alunoDAO = new AlunoDAO();
            while (rs.next()) {
                Turma turma = new Turma();
            
                turma.setCodigo(rs.getString("codigo"));
                turma.setNome(rs.getString("nome"));
                
                turma.setAlunos(alunoDAO.listarPorTurma(turma.getCodigo()));

                turmas.add(turma);
            }
        } catch (SQLException e) {
            throw new DAOException(CodigoErro.ERRO_REMOVER, e);
        }

        return turmas;
    }
}
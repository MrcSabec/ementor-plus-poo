package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import database.Conexao;
import exception.*;
import main.Aluno;
import main.Turma;  

public class AlunoDAO {

    public boolean existe(Connection connection, String matricula) throws SQLException {
        String sql = "SELECT matricula FROM aluno WHERE matricula = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, matricula);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        }
    }


    public void inserir(Aluno aluno) {
        Connection connection = null;
        try {
            connection = Conexao.getConnection();
            connection.setAutoCommit(false);
            
            PessoaDAO pessoaDAO = new PessoaDAO();
            pessoaDAO.inserir(connection, aluno);
            
            inserir(connection, aluno);
            
            connection.commit();
            System.out.println("Aluno cadastrado com sucesso!");
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

    public void inserir(Connection connection, Aluno aluno) throws SQLException {
        String sql = """
        INSERT INTO aluno
        (matricula, cpf_pessoa, periodo, codigo_turma,
        nota1, nota2, nota3, nota4, nota5,
        nota6, nota7, nota8, nota9, nota10)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement statementAluno = connection.prepareStatement(sql)) {
            statementAluno.setString(1, aluno.getMatricula());
            statementAluno.setString(2, aluno.getCpf());
            statementAluno.setInt(3, aluno.getPeriodo());
            if(aluno.getTurma() == null){
                throw new IllegalArgumentException("O aluno deve possuir uma turma.");
            }
            statementAluno.setString(4, aluno.getTurma().getCodigo());

            for (int i = 0; i < 10; i++) {
                statementAluno.setDouble(i + 5, aluno.getNotas()[i]);
            }

            statementAluno.executeUpdate();
        }
    }

    public void alterar(Connection connection, Aluno aluno){
        try{
            connection = Conexao.getConnection();
            connection.setAutoCommit(false);
            PessoaDAO pessoaDAO = new PessoaDAO();
            pessoaDAO.atualizar(connection, aluno);
            
            String sql = "UPDATE aluno SET codigo_turma = ? , nota1 = ? , nota2 = ? , nota3 = ? , nota4 = ? , nota5 = ? , nota6 = ? , nota7 = ? , nota8 = ? , nota9 = ? , nota10 = ? , periodo = ? WHERE matricula = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, aluno.getTurma().getCodigo());
                
                for (int i = 0; i < 10; i++) {
                    statement.setDouble(i + 2, aluno.getNotas()[i]);
                }

                statement.setInt(12, aluno.getPeriodo());
                statement.setString(13, aluno.getMatricula());
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Aluno atualizado com sucesso no banco de dados!");
                } else {
                    System.out.println("Aluno não encontrado no banco de dados.");
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

    public void atualizarNotas(String matricula, double[] notas) {
        String sql = "UPDATE aluno SET nota1 = ?, nota2 = ?, nota3 = ?, nota4 = ?, nota5 = ?, nota6 = ?, nota7 = ?, nota8 = ?, nota9 = ?, nota10 = ? WHERE matricula = ?";
        try (Connection connection = Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
             
            for (int i = 0; i < 10; i++) {
                if (notas != null && i < notas.length) {
                    statement.setDouble(i + 1, notas[i]);
                } else {
                    statement.setDouble(i + 1, 0.0);
                }
            }
            statement.setString(11, matricula);
            
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Notas do aluno atualizadas com sucesso!");
            } else {
                System.out.println("Aluno não encontrado para atualizar as notas.");
            }
        } catch (SQLException e) {
            throw new DAOException(CodigoErro.ERRO_ATUALIZAR_NOTAS, e);
        }
    }

    private Turma criarTurma(ResultSet rs) throws SQLException{
        Turma turma = new Turma();

        turma.setCodigo(rs.getString("codigo"));
        turma.setNome(rs.getString("nome"));

        return turma;
    }
    
    private Aluno criarAluno(ResultSet rs) throws SQLException {

        Aluno aluno = new Aluno();

        aluno.setCpf(rs.getString("cpf"));
        aluno.setNome(rs.getString("nome"));
        aluno.setNascimento(rs.getDate("nascimento"));
        aluno.setTelefone(rs.getString("telefone"));
        aluno.setRua(rs.getString("rua"));
        aluno.setBairro(rs.getString("bairro"));
        aluno.setCidade(rs.getString("cidade"));
        aluno.setEstado(rs.getString("estado"));

        aluno.setMatricula(rs.getString("matricula"));
        aluno.setPeriodo(rs.getInt("periodo"));

        aluno.setTurma(criarTurma(rs));

        double[] notas = new double[10];

        for (int i = 0; i < 10; i++) {
            notas[i] = rs.getDouble("nota" + (i + 1));
        }

        aluno.setNotas(notas);

        return aluno;
    }

    public Aluno buscar(String matricula) {
        String sql = """
            SELECT a.*,
            p.*,
            t.codigo,
            t.nome
            FROM aluno a 
            JOIN pessoa p 
            ON a.cpf_pessoa = p.cpf 
            JOIN turma t
            ON a.codigo_turma = t.codigo
            WHERE a.matricula = ?
        """;

        try (Connection connection = Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, matricula);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return criarAluno(rs);
                }

                return null;
            }
        } catch (SQLException e) {
            throw new DAOException(CodigoErro.ERRO_BUSCAR, e);
        }
    }

    public List<Aluno> listarAlunos(){
        List<Aluno> alunos = new ArrayList<>();

        String sql = """
                SELECT a.*,
                p.*,
                t.codigo,
                t.nome
                FROM aluno a 
                JOIN pessoa p ON a.cpf_pessoa = p.cpf 
                JOIN turma t ON a.codigo_turma = t.codigo
                LEFT JOIN egresso e ON e.matricula = a.matricula WHERE e.matricula IS NULL 
                ORDER BY p.nome
                """;

        try (Connection connection = Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                alunos.add(criarAluno(rs));
            }
        } catch (SQLException e) {
            throw new DAOException(CodigoErro.ERRO_LISTAR, e);
        }

        return alunos;
    }

    public ArrayList <Aluno> listarPorTurma(String codigo_turma){
        ArrayList <Aluno> alunosTurma = new ArrayList<>();

        String sql = """
                SELECT 
                a.*, 
                p.*, 
                t.codigo, 
                t.nome 
                FROM aluno a 
                JOIN pessoa p ON a.cpf_pessoa = p.cpf 
                JOIN turma t ON t.codigo = a.codigo_turma 
                LEFT JOIN egresso e ON e.matricula = a.matricula WHERE e.matricula IS NULL 
                AND a.codigo_turma = ? 
                ORDER BY p.nome
                """;
        try (Connection connection = Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, codigo_turma);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    alunosTurma.add(criarAluno(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException(CodigoErro.ERRO_LISTAR_FILTRADO, e);
        }

        return alunosTurma;
    }
    
    public void desvincularTurma(String matricula) {
        Connection connection = null;
        try {
            connection = Conexao.getConnection();
            connection.setAutoCommit(false);

            String sql = "UPDATE aluno SET codigo_turma = '000' WHERE matricula = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, matricula);
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Aluno desvinculado com sucesso no banco de dados!");
                } else {
                    System.out.println("Aluno não encontrado para desvinculação.");
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
            throw new DAOException(CodigoErro.ERRO_DESVINCULAR_TURMA, e);
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
    
    private String buscarCpfPorMatricula(Connection connection, String matricula)
        throws SQLException {

        String sql = "SELECT cpf_pessoa FROM aluno WHERE matricula = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, matricula);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return rs.getString("cpf_pessoa");
                }

                return null;
            }
        }
    }
    
    public void remover(String matricula) {
        Connection connection = null;
        try {
            connection = Conexao.getConnection();
            connection.setAutoCommit(false);
            
            remover(connection, matricula);
            
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

    public void remover(Connection connection, String matricula) throws SQLException {
        String cpf = buscarCpfPorMatricula(connection, matricula);

        if (cpf == null) {
            throw new RuntimeException("Aluno não encontrado.");
        }

        String sql = "DELETE FROM aluno WHERE matricula = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, matricula);
            statement.executeUpdate();
        }

        PessoaDAO pessoaDAO = new PessoaDAO();
        pessoaDAO.remover(connection, cpf);
    }
}

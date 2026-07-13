package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.Conexao;

import main.Egresso;
import main.Aluno;
import main.Turma;



public class EgressoDAO {


    public boolean existe(Connection connection, String cpf_pessoa) throws SQLException {
        String sql = "SELECT cpf_pessoa FROM egresso WHERE cpf_pessoa = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cpf_pessoa);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            StringBuilder sb = new StringBuilder();
            try (ResultSet rs = connection.getMetaData().getColumns(null, null, "egresso", null)) {
                sb.append("Colunas reais de egresso: ");
                while(rs.next()) {
                    sb.append(rs.getString("COLUMN_NAME")).append(", ");
                }
            } catch (Exception ex) {}
            throw new SQLException(e.getMessage() + "\n" + sb.toString(), e);
        }
    }

    public void inserir(Egresso egresso) {
        Connection connection = null;
        try {
            connection = Conexao.getConnection();
            connection.setAutoCommit(false);
            
            PessoaDAO pessoaDAO = new PessoaDAO();
            if (pessoaDAO.buscar(connection, egresso.getCpf()) != null) {
                pessoaDAO.atualizar(connection, egresso);
            } else {
                pessoaDAO.inserir(connection, egresso);
            }

            AlunoDAO alunoDAO = new AlunoDAO();
            if (alunoDAO.existe(connection, egresso.getMatricula())) {
                String sqlAluno = "UPDATE aluno SET periodo = ?, codigo_turma = ? WHERE matricula = ?";
                try (PreparedStatement statementAluno = connection.prepareStatement(sqlAluno)) {
                    statementAluno.setInt(1, egresso.getPeriodo());
                    statementAluno.setString(2, egresso.getTurma().getCodigo());
                    statementAluno.setString(3, egresso.getMatricula());
                    statementAluno.executeUpdate();
                }
            } else {
                alunoDAO.inserir(connection, egresso);
            }

            if (existe(connection, egresso.getCpf())) {
                String sqlEgresso = "UPDATE egresso SET profissao_atual = ?, faixa_salarial = ?, curso_anterior = ?, curso_atual = ? WHERE cpf_pessoa = ?";
                try (PreparedStatement statement = connection.prepareStatement(sqlEgresso)) {
                    statement.setString(1, egresso.getProfissaoAtual());
                    statement.setDouble(2, egresso.getFaixaSalarial());
                    statement.setString(3, egresso.getCursoAnterior());
                    statement.setString(4, egresso.getCursoAtual());
                    statement.setString(5, egresso.getCpf());
                    statement.executeUpdate();
                }
            } else {
                String sql = """
                INSERT INTO egresso
                (profissao_atual, faixa_salarial, curso_anterior, curso_atual,
                cpf_pessoa)
                VALUES (?, ?, ?, ?, ?)
                """;
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, egresso.getProfissaoAtual());
                    statement.setDouble(2, egresso.getFaixaSalarial());
                    statement.setString(3, egresso.getCursoAnterior());
                    statement.setString(4, egresso.getCursoAtual());
                    statement.setString(5, egresso.getCpf());
                    statement.executeUpdate();
                }
            }
            connection.commit();
            System.out.println("Egresso cadastrado com sucesso!");

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new RuntimeException("Erro ao cadastrar Egresso", e);
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
    public void alterar(Egresso egresso){
        Connection connection = null;
        try{
            connection = Conexao.getConnection();
            connection.setAutoCommit(false);

            PessoaDAO pessoa = new PessoaDAO();
            pessoa.atualizar(connection, egresso);

            AlunoDAO aluno = new AlunoDAO();
            aluno.alterar(connection, egresso);
            
            String sql = "UPDATE egresso SET profissao_atual = ?, faixa_salarial = ?, curso_anterior = ?, curso_atual = ? WHERE matricula = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, egresso.getProfissaoAtual());
                statement.setDouble(2, egresso.getFaixaSalarial());
                statement.setString(3, egresso.getCursoAnterior());
                statement.setString(4,egresso.getCursoAtual());
                statement.setString(5,egresso.getMatricula());
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
            throw new RuntimeException("Erro ao atualizar aluno.", e);
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

    private Turma criarTurma(ResultSet rs) throws SQLException{
        Turma turma = new Turma();

        turma.setCodigo(rs.getString("codigo"));
        turma.setNome(rs.getString("nome"));

        return turma;
    }
    
    private Egresso criarEgresso(ResultSet rs) throws SQLException {

        Egresso egresso = new Egresso();

        egresso.setCpf(rs.getString("cpf"));
        egresso.setNome(rs.getString("nome"));
        egresso.setNascimento(rs.getDate("nascimento"));
        egresso.setTelefone(rs.getString("telefone"));
        egresso.setRua(rs.getString("rua"));
        egresso.setBairro(rs.getString("bairro"));
        egresso.setCidade(rs.getString("cidade"));
        egresso.setEstado(rs.getString("estado"));

        egresso.setMatricula(rs.getString("matricula"));
        egresso.setPeriodo(rs.getInt("periodo"));

        egresso.setTurma(criarTurma(rs));

        double[] notas = new double[10];

        for (int i = 0; i < 10; i++) {
            notas[i] = rs.getDouble("nota" + (i + 1));
        }

        egresso.setNotas(notas);

        egresso.setProfissaoAtual(rs.getString("profissao_atual"));
        egresso.setFaixaSalarial(rs.getDouble("faixa_salarial"));
        egresso.setCursoAnterior(rs.getString("curso_anterior"));
        egresso.setCursoAtual(rs.getString("curso_atual"));

        return egresso;
    }

    public Egresso buscar(String matricula) {
        String sql = """
            SELECT 
            e.*,
            a.*,
            p.*,
            t.codigo,
            t.nome
            FROM egresso e
            JOIN aluno a
            ON e.cpf_pessoa = a.cpf_pessoa 
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
                    return criarEgresso(rs);
                }

                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar aluno.", e);
        }
    }

    public List<Egresso> listarEgressos(){
        List<Egresso> alunos = new ArrayList<>();

        String sql = """
                SELECT e.*,
                a.*,
                p.*,
                t.codigo,
                t.nome
                FROM egresso e 
                JOIN aluno a ON a.cpf_pessoa = e.cpf_pessoa 
                JOIN pessoa p ON a.cpf_pessoa = p.cpf 
                JOIN turma t ON a.codigo_turma = t.codigo
                ORDER BY p.nome
                """;

        try (Connection connection = Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                alunos.add(criarEgresso(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar egressos.", e);
        }

        return alunos;
    }

    public ArrayList <Aluno> listarPorTurma(String codigo_turma){
        ArrayList <Aluno> alunosTurma = new ArrayList<>();

        String sql = """
                SELECT * 
                FROM egresso e
                JOIN aluno a ON a.cpf_pessoa = e.cpf_pessoa
                JOIN pessoa p ON a.cpf_pessoa = p.cpf 
                JOIN turma t ON t.codigo = a.codigo_turma 
                WHERE a.codigo_turma = ?
                ORDER BY p.nome 
                """;
        try (Connection connection = Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                statement.setString(1,codigo_turma);
                alunosTurma.add(criarEgresso(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar egressos de uma turma", e);
        }

        return alunosTurma;
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
            }
        }
        return null;
    }
    
    public void remover(String matricula) {
        Connection connection = null;
        try {
            connection = Conexao.getConnection();
            connection.setAutoCommit(false);
            String cpf = buscarCpfPorMatricula(connection, matricula);

            if (cpf == null) {
                throw new RuntimeException("egresso não encontrado.");
            }

            String sqlEgresso = "DELETE FROM egresso WHERE cpf_pessoa = ?";
            try (PreparedStatement stmtEgresso = connection.prepareStatement(sqlEgresso)) {
                stmtEgresso.setString(1, cpf);
                stmtEgresso.executeUpdate();
            }

            AlunoDAO alunoDAO = new AlunoDAO();
            alunoDAO.remover(connection, matricula);

            connection.commit();

        } catch (SQLException e) {

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            throw new RuntimeException("Erro ao remover egresso: " + e.getMessage(), e);

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

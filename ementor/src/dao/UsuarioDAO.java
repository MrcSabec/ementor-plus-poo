package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.util.List;
import java.util.ArrayList;

import main.Usuario;
import main.NivelAcesso;

public class UsuarioDAO {
    public void inserir(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome_usuario, senha, nivel_acesso) VALUES (?, ?, ?)";
        try (Connection connection = database.Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, usuario.getNomeUsuario());
            statement.setString(2, usuario.getSenha());
            statement.setInt(3, usuario.getNivelAcesso().ordinal() + 1);
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                usuario.setID(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void alterar(Usuario usuario) {
        String sql = "UPDATE usuario SET nome_usuario = ?, senha = ?, nivel_acesso = ? WHERE id = ?";
        try (Connection connection = database.Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getNomeUsuario());
            statement.setString(2, usuario.getSenha());
            statement.setInt(3, usuario.getNivelAcesso().ordinal() + 1);
            statement.setInt(4, usuario.getId());
            int updates = statement.executeUpdate();
            if(updates > 0) {
                System.out.println("Usuário atualizado com sucesso!");
            } else {
                System.out.println("Nenhum usuário encontrado com o ID fornecido.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int obterID(Usuario usuario) {
        String sql = "SELECT id FROM usuario WHERE nome_usuario = ? AND senha = ? AND nivel_acesso = ?";
        try (Connection connection = database.Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getNomeUsuario());
            statement.setString(2, usuario.getSenha());
            statement.setInt(3, usuario.getNivelAcesso().ordinal() + 1);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Retorna -1 se o usuário não for encontrado
    }

    public void excluir(int id) {
        String sql = "DELETE FROM usuario WHERE id = ?";
        try (Connection connection = database.Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int deletes = statement.executeUpdate();
            if(deletes > 0) {
                System.out.println("Usuário excluído com sucesso!");
            } else {
                System.out.println("Nenhum usuário encontrado com o ID fornecido.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Usuario autenticar(String usuario, String senha) {
        String sql = "SELECT * FROM usuario WHERE nome_usuario = ? AND senha = ?";
        try (Connection connection = database.Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario);
            statement.setString(2, senha);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Usuario usuarioAutenticado = new Usuario();
                usuarioAutenticado.setDadosUsuario(
                        resultSet.getString("nome_usuario"),
                        resultSet.getString("senha"),
                        NivelAcesso.values()[resultSet.getInt("nivel_acesso") - 1]
                );
                return usuarioAutenticado;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Usuario> listar(){
        String sql = "SELECT * FROM usuario";
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection connection = database.Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Usuario usuario = new Usuario();
                usuario.setDadosUsuario(
                        resultSet.getString("nome_usuario"),
                        resultSet.getString("senha"),
                        NivelAcesso.values()[resultSet.getInt("nivel_acesso") - 1]
                );
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }
}
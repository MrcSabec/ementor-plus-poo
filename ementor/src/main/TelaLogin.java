package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import dao.*;

public class TelaLogin extends JFrame {

    public TelaLogin() {
        setTitle("eMentor-Plus - Login");
        setSize(400, 250); // Aumentei um pouco a janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Font fontePadrao = new Font("Segoe UI", Font.PLAIN, 16);

        JPanel painelPrincipal = new JPanel(new GridLayout(3, 2, 10, 15));

        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblUsuario = new JLabel("Usuário:");
        lblUsuario.setFont(fontePadrao);
        JTextField txtUsuario = new JTextField();
        txtUsuario.setFont(fontePadrao);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(fontePadrao);
        JPasswordField txtSenha = new JPasswordField();
        txtSenha.setFont(fontePadrao);

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrar.setBackground(new Color(70, 130, 180));
        btnEntrar.setForeground(Color.WHITE);

        painelPrincipal.add(lblUsuario);
        painelPrincipal.add(txtUsuario);
        painelPrincipal.add(lblSenha);
        painelPrincipal.add(txtSenha);
        painelPrincipal.add(new JLabel(""));
        painelPrincipal.add(btnEntrar);

        add(painelPrincipal);

        btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuarioDigitado = txtUsuario.getText();
                String senhaDigitada = new String(txtSenha.getPassword());

                UsuarioDAO dao = new UsuarioDAO();

                Usuario usuarioLogado = dao.autenticar(usuarioDigitado, senhaDigitada);

                if(usuarioLogado != null) {
                    JOptionPane.showMessageDialog(null, "Acesso Permitido! Bem-vindo.");
                    dispose();
                    // MenuPrincipal menu = new MenuPrincipal();
                    // menu.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Usuário ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
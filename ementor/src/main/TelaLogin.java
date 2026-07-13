package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import dao.*;

public class TelaLogin extends JFrame {

    public TelaLogin() {
        setTitle("eMentor-Plus - Login");
        setSize(600, 400); // Define o tamanho da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        ImageIcon iconeLogoOriginal = new ImageIcon(getClass().getResource("/imagens/logo.png"));
        Image imagemLogoRedimensionada = iconeLogoOriginal.getImage().getScaledInstance(388, 213, Image.SCALE_SMOOTH);
        ImageIcon iconeLogo = new ImageIcon(imagemLogoRedimensionada);
        JLabel lblLogo = new JLabel(iconeLogo);

        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);

        lblLogo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        add(lblLogo, BorderLayout.NORTH);


        Font fontePadrao = new Font("Segoe UI", Font.PLAIN, 16);

        JPanel painelPrincipal = new JPanel(new GridLayout(3, 2, 10, 15));

        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JLabel lblUsuario = new JLabel("Usuário:");
        lblUsuario.setFont(fontePadrao);
        JTextField txtUsuario = new JTextField();
        txtUsuario.setPreferredSize(new Dimension(200, 30));
        txtUsuario.setFont(fontePadrao);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(fontePadrao);
        JPasswordField txtSenha = new JPasswordField();
        txtSenha.setPreferredSize(new Dimension(200, 30));
        txtSenha.setFont(fontePadrao);

        ImageIcon iconeLoginOriginal = new ImageIcon(getClass().getResource("/imagens/icone_login.png"));
        Image imagemSetaRedimensionada = iconeLoginOriginal.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        JButton btnEntrar = new JButton("Entrar", new ImageIcon(imagemSetaRedimensionada));

        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrar.setBackground(new Color(70, 130, 180));
        btnEntrar.setForeground(Color.WHITE);


        painelPrincipal.add(lblUsuario);
        painelPrincipal.add(txtUsuario);
        painelPrincipal.add(lblSenha);
        painelPrincipal.add(txtSenha);
        painelPrincipal.add(new JLabel("")); // Espaçador para alinhamento
        painelPrincipal.add(btnEntrar);

        JPanel painelCentralizador = new JPanel(new GridBagLayout());
        painelCentralizador.add(painelPrincipal);

        add(painelCentralizador, BorderLayout.CENTER);


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
                    MenuPrincipal menu = new MenuPrincipal();
                    menu.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Usuário ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
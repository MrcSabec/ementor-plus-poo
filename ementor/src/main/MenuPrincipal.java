package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("eMentor-Plus - Menu Principal");
        setSize(800, 600); // Configura o tamanho da tela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Encerra o sistema ao fechar
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ==========================================
        // TOPO: TÍTULO DO MENU
        // ==========================================
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBorder(BorderFactory.createEmptyBorder(0, -20, 0, 0));

        // Carregando e redimensionando a logo principal
        try {
            ImageIcon iconeLogo = new ImageIcon(getClass().getResource("/imagens/logo.png"));

            // Redimensionamento da logo
            Image imgLogo = iconeLogo.getImage().getScaledInstance(388, 213, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(imgLogo));
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);

            // Adicionando a logo ao painel
            painelTopo.add(lblLogo, BorderLayout.CENTER);

        } catch (Exception ex) {
            System.out.println("Erro ao carregar a logo do topo.");
        }

        // Finalmente, adiciona o painel completo no NORTE da janela principal
        add(painelTopo, BorderLayout.NORTH);

        // ==========================================
        // CENTRO: GRADE DE BOTÕES (3x3)
        // ==========================================
        // Define o layout do painel de botões
        JPanel painelBotoes = new JPanel(new GridLayout(3, 3, 15, 15));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        // Criação dos botões de ação
        JButton btnCadAluno = criarBotaoMenu("Cadastrar Aluno", "/imagens/cad_aluno.png");
        JButton btnListarAluno = criarBotaoMenuLST("Listar Aluno", "/imagens/list_aluno.png");
        JButton btnAlterarAluno = criarBotaoMenuALT("Alterar Aluno", "/imagens/alt_aluno.png");


        JButton btnCadEgresso = criarBotaoMenu("Cadastrar Egresso", "/imagens/cad_egressor.png");
        JButton btnListarEgresso = criarBotaoMenuLST("Listar Egresso", "/imagens/list_egressor.png");
        JButton btnAlterarEgresso = criarBotaoMenuALT("Alterar Egresso", "/imagens/alt_egressor.png");

        JButton btnCadProfessor = criarBotaoMenu("Cadastrar Professor", "/imagens/cad_professor.png");
        JButton btnListarProfessor = criarBotaoMenuLST("Listar Professor", "/imagens/list_professor.png");
        JButton btnAlterarProfessor = criarBotaoMenuALT("Alterar Professor", "/imagens/alt_professor.png");
        
        painelBotoes.add(btnCadAluno);
        painelBotoes.add(btnListarAluno);
        painelBotoes.add(btnAlterarAluno);

        painelBotoes.add(btnCadEgresso);
        painelBotoes.add(btnListarEgresso);
        painelBotoes.add(btnAlterarEgresso);

        painelBotoes.add(btnCadProfessor);
        painelBotoes.add(btnListarProfessor);
        painelBotoes.add(btnAlterarProfessor);

        add(painelBotoes, BorderLayout.CENTER);

        // ==========================================
        // AÇÕES DOS BOTÕES (Exemplo do Cadastrar Aluno)
        // ==========================================
        btnCadAluno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aqui você vai instanciar a próxima tela
                JOptionPane.showMessageDialog(null, "Abrindo tela de Cadastrar Aluno...");
                TelaCadastroAluno telaCad = new TelaCadastroAluno();
                telaCad.setVisible(true);
            }
        });

        btnListarAluno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aqui você vai instanciar a próxima tela
                JOptionPane.showMessageDialog(null, "Abrindo tela de Lista dos Alunos...");
                TelaListaAluno telaList = new TelaListaAluno();
                telaList.setVisible(true);
            }
        });
        btnAlterarAluno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aqui você vai instanciar a próxima tela
                JOptionPane.showMessageDialog(null, "Abrindo tela de Edição de Alunos...");
                TelaAlterarAluno telaAltera = new TelaAlterarAluno();
                telaAltera.setVisible(true);
            }
        });
        btnCadEgresso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aqui você vai instanciar a próxima tela
                JOptionPane.showMessageDialog(null, "Abrindo tela de Cadastrar Egresso...");
                TelaCadastroEgresso telaCad = new TelaCadastroEgresso();
                telaCad.setVisible(true);
            }
        });
        
        btnListarEgresso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Abrindo tela de Lista dos Egressos...");
                TelaListaEgresso telaList = new TelaListaEgresso();
                telaList.setVisible(true);
            }
        });
        
        btnAlterarEgresso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Abrindo tela de Edição de Egressos...");
                TelaAlterarEgresso telaAltera = new TelaAlterarEgresso();
                telaAltera.setVisible(true);
            }
        });
        
        // Repita o addActionListener para os outros botões...
    }


    // ==========================================
        // Método auxiliar para criar botões de menu
    // ==========================================
    private JButton criarBotaoMenu(String texto, String caminhoIcone) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botao.setBackground(new Color(240, 248, 255));
        botao.setFocusPainted(false);

        // Configuração de texto e ícone
        botao.setHorizontalTextPosition(SwingConstants.CENTER);
        botao.setVerticalTextPosition(SwingConstants.BOTTOM);

        try {
            ImageIcon icone = new ImageIcon(getClass().getResource(caminhoIcone));
            Image img = icone.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            botao.setIcon(new ImageIcon(img));

        } catch (Exception ex) {
            System.out.println("Ícone não encontrado: " + caminhoIcone);
        }

        return botao;
    }

    private JButton criarBotaoMenuLST(String texto, String caminhoIcone) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botao.setBackground(new Color(70, 130, 180));
        botao.setForeground(Color.WHITE);

        botao.setHorizontalTextPosition(SwingConstants.CENTER);
        botao.setVerticalTextPosition(SwingConstants.BOTTOM);

        try {
            ImageIcon icone = new ImageIcon(getClass().getResource(caminhoIcone));
            Image img = icone.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            botao.setIcon(new ImageIcon(img));

        } catch (Exception ex) {
            System.out.println("Ícone não encontrado: " + caminhoIcone);
        }

        return botao;
    }

private JButton criarBotaoMenuALT(String texto, String caminhoIcone) {
    JButton botao = new JButton(texto);
    botao.setFont(new Font("Segoe UI", Font.BOLD, 16));
    botao.setBackground(new Color(0, 200, 100));
    botao.setForeground(Color.WHITE);

    botao.setHorizontalTextPosition(SwingConstants.CENTER);
    botao.setVerticalTextPosition(SwingConstants.BOTTOM);

    try {
        ImageIcon icone = new ImageIcon(getClass().getResource(caminhoIcone));
        Image img = icone.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
        botao.setIcon(new ImageIcon(img));

    } catch (Exception ex) {
        System.out.println("Ícone não encontrado: " + caminhoIcone);
    }

    return botao;
}
}
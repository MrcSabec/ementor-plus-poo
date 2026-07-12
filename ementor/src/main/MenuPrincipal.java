package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("eMentor-Plus - Menu Principal");
        setSize(800, 600); // Uma tela bem maior para caber o painel de controle
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Encerra o sistema ao fechar
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ==========================================
        // TOPO: TÍTULO DO MENU
        // ==========================================
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBorder(BorderFactory.createEmptyBorder(0, -20, 0, 0));

        // 1. Carregando e redimensionando a Logo (Boa prática para evitar bugs futuros)
        try {
            // Usa o getResource para garantir que a imagem seja achada em qualquer PC
            ImageIcon iconeLogo = new ImageIcon(getClass().getResource("/imagens/logo.png"));

            // Redimensiona a logo para um tamanho fixo e agradável (ex: 150x150)
            Image imgLogo = iconeLogo.getImage().getScaledInstance(388, 213, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(imgLogo));
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);

            // Coloca a logo no centro deste mini-painel
            painelTopo.add(lblLogo, BorderLayout.CENTER);

        } catch (Exception ex) {
            System.out.println("Erro ao carregar a logo do topo.");
        }

        // Finalmente, adiciona o painel completo no NORTE da janela principal
        add(painelTopo, BorderLayout.NORTH);

        // ==========================================
        // CENTRO: GRADE DE BOTÕES (3x3)
        // ==========================================
        // GridLayout de 3 linhas, 3 colunas, com 15px de espaçamento horizontal e vertical
        JPanel painelBotoes = new JPanel(new GridLayout(3, 3, 15, 15));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        // Criação dos 9 botões exigidos (Usando um método auxiliar para não repetir código)
        // OBS: Você precisará baixar 9 ícones PNG (tamanho recomendado 48x48 ou 64x64) e colocar na pasta 'imagens'
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

        // Repita o addActionListener para os outros botões...
    }

    // ==========================================
    // MÉTODO AUXILIAR PARA CRIAR OS BOTÕES GRANDES
    // ==========================================
    private JButton criarBotaoMenu(String texto, String caminhoIcone) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botao.setBackground(new Color(240, 248, 255)); // Um fundo azul bem clarinho
        botao.setFocusPainted(false);

        // Posicionando o texto embaixo do ícone para o botão ficar mais "quadrado" e grande
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
        botao.setBackground(new Color(70, 130, 180)); // Um fundo azul bem clarinho
        botao.setForeground(Color.WHITE);

        // Posicionando o texto embaixo do ícone para o botão ficar mais "quadrado" e grande
        botao.setHorizontalTextPosition(SwingConstants.CENTER);
        botao.setVerticalTextPosition(SwingConstants.BOTTOM);

        try {
            // Tenta carregar a imagem. Você pode aplicar aquele redimensionamento
            // que discutimos antes se a imagem original for gigante!
            ImageIcon icone = new ImageIcon(getClass().getResource(caminhoIcone));

            // Truque de redimensionamento caso a imagem venha enorme:
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
    botao.setBackground(new Color(0, 200, 100)); // Um fundo azul bem clarinho
    botao.setForeground(Color.WHITE);

    // Posicionando o texto embaixo do ícone para o botão ficar mais "quadrado" e grande
    botao.setHorizontalTextPosition(SwingConstants.CENTER);
    botao.setVerticalTextPosition(SwingConstants.BOTTOM);

    try {
        // Tenta carregar a imagem. Você pode aplicar aquele redimensionamento
        // que discutimos antes se a imagem original for gigante!
        ImageIcon icone = new ImageIcon(getClass().getResource(caminhoIcone));

        // Truque de redimensionamento caso a imagem venha enorme:
        Image img = icone.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
        botao.setIcon(new ImageIcon(img));

    } catch (Exception ex) {
        System.out.println("Ícone não encontrado: " + caminhoIcone);
    }

    return botao;
}
}
package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaNotasAluno extends JFrame {

    private String matricula; // Armazena a matrícula selecionada
    private JTextField[] camposNotas; // Array de campos de notas
    private boolean isEgresso = false;

    public TelaNotasAluno(String matriculaAluno, boolean isEgresso) {
        this(matriculaAluno);
        this.isEgresso = isEgresso;
    }

    // Construtor principal
    public TelaNotasAluno(String matriculaAluno) {
        this.matricula = matriculaAluno;

        setTitle("eMentor-Plus - Atribuir Notas");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ==========================================
        // Componentes do painel superior
        // ==========================================
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        try {
            ImageIcon iconeLogo = new ImageIcon(getClass().getResource("/imagens/logo.png"));
            Image imgLogo = iconeLogo.getImage().getScaledInstance(182, 100, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(imgLogo));
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
            painelTopo.add(lblLogo, BorderLayout.CENTER);
        } catch (Exception ex) {
            System.out.println("Aviso: Logo não encontrada.");
        }

        JLabel lblTitulo = new JLabel("Lançamento de Notas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel lblMatricula = new JLabel("Aluno Matrícula: " + this.matricula, SwingConstants.CENTER);
        lblMatricula.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblMatricula.setForeground(Color.GRAY);

        JPanel painelTextosTopo = new JPanel(new GridLayout(2, 1));
        painelTextosTopo.add(lblTitulo);
        painelTextosTopo.add(lblMatricula);
        painelTopo.add(painelTextosTopo, BorderLayout.SOUTH);

        add(painelTopo, BorderLayout.NORTH);

        // ==========================================
        // Componentes do painel central
        // ==========================================
        JPanel painelCentralizador = new JPanel(new GridBagLayout());

        // Configura o layout do formulário
        JPanel painelFormulario = new JPanel(new GridLayout(5, 4, 15, 15));
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        camposNotas = new JTextField[10]; // Inicializa o array de campos
        Font fontePadrao = new Font("Segoe UI", Font.PLAIN, 14);

        for (int i = 0; i < 10; i++) {
            JLabel lblNota = new JLabel("Nota " + (i + 1) + ":");
            lblNota.setHorizontalAlignment(SwingConstants.RIGHT);
            lblNota.setFont(fontePadrao);

            camposNotas[i] = new JTextField();
            camposNotas[i].setFont(fontePadrao);

            painelFormulario.add(lblNota);
            painelFormulario.add(camposNotas[i]);
        }

        painelCentralizador.add(painelFormulario);
        add(painelCentralizador, BorderLayout.CENTER);

        // ==========================================
        // Componentes do painel inferior
        // ==========================================
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton btnVoltar = new JButton("Voltar");
        JButton btnSalvar = new JButton("Salvar Notas");
        btnVoltar.setBackground(new Color(240, 248, 255));
        btnSalvar.setBackground(new Color(70, 130, 180)); // Define a cor de fundo
        btnSalvar.setForeground(Color.WHITE);

        // Configuração dos ícones dos botões
        try {
            ImageIcon iconVoltar = new ImageIcon(getClass().getResource("/imagens/icone_voltar_black.png"));
            Image iconVoltarRedimensionada = iconVoltar.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            if(iconVoltar != null) btnVoltar.setIcon(new ImageIcon(iconVoltarRedimensionada));

            ImageIcon iconSalvar = new ImageIcon(getClass().getResource("/imagens/icone_salvar.png"));
            Image iconSalvarRedimensionada = iconSalvar.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            if(iconSalvar != null) btnSalvar.setIcon(new ImageIcon(iconSalvarRedimensionada));
        } catch (Exception ex) {
            System.out.println("Erro ao carregar ícones dos botões.");
        }

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnSalvar);
        add(painelBotoes, BorderLayout.SOUTH);

        // ==========================================
        // AÇÕES DOS BOTÕES
        // ==========================================
        btnVoltar.addActionListener(e -> {
            dispose();
            if (isEgresso) {
                new TelaListaEgresso().setVisible(true);
            } else {
                new TelaListaAluno().setVisible(true);
            }
        });

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Desabilita o botão para evitar múltiplas submissões
                btnSalvar.setEnabled(false);

                // Configuração da barra de progresso
                JDialog dialogProgresso = new JDialog();
                dialogProgresso.setTitle("Salvando Notas...");
                dialogProgresso.setSize(300, 100);
                dialogProgresso.setLocationRelativeTo(null);
                dialogProgresso.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

                JProgressBar barraProgresso = new JProgressBar(0, 100);
                barraProgresso.setStringPainted(true);
                dialogProgresso.add(barraProgresso, BorderLayout.CENTER);
                dialogProgresso.setVisible(true);

                // Inicia thread para processamento assíncrono
                Thread threadSalvamento = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            barraProgresso.setValue(10);
                            barraProgresso.setString("Lendo valores...");
                            Thread.sleep(500); // Simulação de delay

                            // Realiza a leitura dos valores na interface
                            double[] valoresLidos = new double[10];
                            int quantidadeNotas = 0;

                            for (int i = 0; i < 10; i++) {
                                String texto = camposNotas[i].getText();
                                if (!texto.trim().isEmpty()) {
                                    // Realiza a conversão de formato das notas
                                    valoresLidos[i] = Double.parseDouble(texto.replace(",", "."));
                                    quantidadeNotas++;
                                }
                            }

                            barraProgresso.setValue(50);
                            barraProgresso.setString("Atualizando Banco de Dados...");
                            Thread.sleep(800);

                            // Persistência das notas (DAO)
                            dao.AlunoDAO dao = new dao.AlunoDAO();
                            dao.atualizarNotas(matricula, valoresLidos);

                            barraProgresso.setValue(100);
                            barraProgresso.setString("Concluído!");
                            Thread.sleep(400);

                            dialogProgresso.dispose();
                            JOptionPane.showMessageDialog(null, quantidadeNotas + " notas cadastradas com sucesso para a Matrícula: " + matricula);

                            dispose();
                            if (isEgresso) {
                                new TelaListaEgresso().setVisible(true);
                            } else {
                                new TelaListaAluno().setVisible(true);
                            }

                        } catch (NumberFormatException ex) {
                            dialogProgresso.dispose();
                            btnSalvar.setEnabled(true);
                            JOptionPane.showMessageDialog(null, "Por favor, digite apenas números válidos nas notas (ex: 8.5).", "Erro de Preenchimento", JOptionPane.ERROR_MESSAGE);
                        } catch (Exception ex) {
                            dialogProgresso.dispose();
                            btnSalvar.setEnabled(true);
                            JOptionPane.showMessageDialog(null, "Erro inesperado: " + ex.getMessage());
                            // Registrar o log de erros posteriormente
                        }
                    }
                });

                threadSalvamento.start();
            }
        });
    }
}

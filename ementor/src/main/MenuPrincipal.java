package main;

import exception.LogErro;
import exception.CodigoErro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("eMentor-Plus - Menu Principal");
        setSize(800, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Componentes do painel superior
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBorder(BorderFactory.createEmptyBorder(0, -20, 0, 0));

        // Carregamento e redimensionamento da logomarca
        try {
            ImageIcon iconeLogo = new ImageIcon(getClass().getResource("/imagens/logo.png"));

            Image imgLogo = iconeLogo.getImage().getScaledInstance(388, 213, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(imgLogo));
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);

            painelTopo.add(lblLogo, BorderLayout.CENTER);

        } catch (Exception ex) {
            LogErro.registrar(CodigoErro.ERRO_SISTEMA, ex);
            System.out.println("Erro ao carregar a logo do topo.");
        }

        add(painelTopo, BorderLayout.NORTH);

        // Componentes do painel central (Grade de operações)
        JPanel painelBotoes = new JPanel(new GridLayout(4, 3, 20, 20));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        JButton btnCadAluno = criarBotaoMenu("Cadastrar Aluno", "/imagens/cad_aluno.png");
        JButton btnListarAluno = criarBotaoMenuLST("Listar Aluno", "/imagens/list_aluno.png");
        JButton btnAlterarAluno = criarBotaoMenuALT("Alterar Aluno", "/imagens/alt_aluno.png");


        JButton btnCadEgresso = criarBotaoMenu("Cadastrar Egresso", "/imagens/cad_egressor.png");
        JButton btnListarEgresso = criarBotaoMenuLST("Listar Egresso", "/imagens/list_egressor.png");
        JButton btnAlterarEgresso = criarBotaoMenuALT("Alterar Egresso", "/imagens/alt_egressor.png");

        JButton btnCadProfessor = criarBotaoMenu("Cadastrar Professor", "/imagens/cad_professor.png");
        JButton btnListarProfessor = criarBotaoMenuLST("Listar Professor", "/imagens/list_professor.png");
        JButton btnAlterarProfessor = criarBotaoMenuALT("Alterar Professor", "/imagens/alt_professor.png");

        JButton btnCadTurma = criarBotaoMenu("Cadastrar Turma", "/imagens/cad_turma.png");
        JButton btnListarTurma = criarBotaoMenuLST("Listar Turma", "/imagens/list_turma.png");
        JButton btnAlterarTurma = criarBotaoMenuALT("Alterar Turma", "/imagens/alt_turma.png");
        
        painelBotoes.add(btnCadAluno);
        painelBotoes.add(btnListarAluno);
        painelBotoes.add(btnAlterarAluno);

        painelBotoes.add(btnCadEgresso);
        painelBotoes.add(btnListarEgresso);
        painelBotoes.add(btnAlterarEgresso);

        painelBotoes.add(btnCadProfessor);
        painelBotoes.add(btnListarProfessor);
        painelBotoes.add(btnAlterarProfessor);

        painelBotoes.add(btnCadTurma);
        painelBotoes.add(btnListarTurma);
        painelBotoes.add(btnAlterarTurma);

        add(painelBotoes, BorderLayout.CENTER);

        // Componentes do painel inferior
        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelRodape.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 30));
        JButton btnGerarRelatorio = new JButton("Gerar Relatório (PDF)");
        btnGerarRelatorio.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGerarRelatorio.setBackground(new Color(220, 53, 69));
        btnGerarRelatorio.setForeground(Color.WHITE);
        
        try {
            ImageIcon iconePdf = new ImageIcon(getClass().getResource("/imagens/icone_pdf.png"));
            Image imgPdf = iconePdf.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            btnGerarRelatorio.setIcon(new ImageIcon(imgPdf));
        } catch (Exception ex) {
            LogErro.registrar(CodigoErro.ERRO_SISTEMA, ex);
            System.out.println("Ícone PDF não encontrado.");
        }
        painelRodape.add(btnGerarRelatorio);
        add(painelRodape, BorderLayout.SOUTH);

        // Configuração dos eventos de ação dos botões
        btnCadAluno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Abrindo tela de Cadastrar Aluno...");
                dispose();
                TelaCadastroAluno telaCad = new TelaCadastroAluno();
                telaCad.setVisible(true);
            }
        });

        btnListarAluno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Abrindo tela de Lista dos Alunos...");
                dispose();
                TelaListaAluno telaList = new TelaListaAluno();
                telaList.setVisible(true);
            }
        });
        btnAlterarAluno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Abrindo tela de Edição de Alunos...");
                dispose();
                TelaAlterarAluno telaAltera = new TelaAlterarAluno();
                telaAltera.setVisible(true);
            }
        });
        btnCadEgresso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Abrindo tela de Cadastrar Egresso...");
                dispose();
                TelaCadastroEgresso telaCad = new TelaCadastroEgresso();
                telaCad.setVisible(true);
            }
        });
        
        btnListarEgresso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Abrindo tela de Lista dos Egressos...");
                dispose();
                TelaListaEgresso telaList = new TelaListaEgresso();
                telaList.setVisible(true);
            }
        });
        
        btnAlterarEgresso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Abrindo tela de Edição de Egressos...");
                dispose();
                TelaAlterarEgresso telaAltera = new TelaAlterarEgresso();
                telaAltera.setVisible(true);
            }
        });
        
        btnCadProfessor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Abrindo tela de Cadastrar Professor...");
                dispose();
                TelaCadastroProfessor telaCad = new TelaCadastroProfessor();
                telaCad.setVisible(true);
            }
        });
        
        btnListarProfessor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Abrindo tela de Lista de Professores...");
                dispose();
                TelaListaProfessor telaList = new TelaListaProfessor();
                telaList.setVisible(true);
            }
        });

        btnAlterarProfessor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Abrindo tela de Edição de Professores...");
                dispose();
                TelaAlterarProfessor telaAltera = new TelaAlterarProfessor();
                telaAltera.setVisible(true);
            }
        });

        btnCadTurma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Abrindo tela de Cadastrar Turma...");
                dispose();
                TelaCadastroTurma telaCad = new TelaCadastroTurma();
                telaCad.setVisible(true);
            }
        });

        btnListarTurma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Abrindo tela de Lista das Turmas...");
                dispose();
                TelaListaTurma telaList = new TelaListaTurma();
                telaList.setVisible(true);
            }
        });

        btnAlterarTurma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Abrindo tela de Edição de Turmas...");
                dispose();
                TelaAlterarTurma telaAltera = new TelaAlterarTurma();
                telaAltera.setVisible(true);
            }
        });

        // Evento de geração do relatório PDF
        btnGerarRelatorio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Salvar Relatório PDF");
                fileChooser.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivo PDF", "pdf");
                fileChooser.addChoosableFileFilter(filter);
                fileChooser.setSelectedFile(new File("relatorio_ementor.pdf"));

                int userSelection = fileChooser.showSaveDialog(MenuPrincipal.this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    String caminhoAbsoluto = fileToSave.getAbsolutePath();
                    if (!caminhoAbsoluto.toLowerCase().endsWith(".pdf")) {
                        caminhoAbsoluto += ".pdf";
                    }
                    
                    final String caminhoFinal = caminhoAbsoluto;

                    btnGerarRelatorio.setEnabled(false);

                    JDialog dialogProgresso = new JDialog();
                    dialogProgresso.setTitle("Gerando Relatório PDF...");
                    dialogProgresso.setSize(300, 100);
                    dialogProgresso.setLocationRelativeTo(null);
                    dialogProgresso.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

                    JProgressBar barraProgresso = new JProgressBar(0, 100);
                    barraProgresso.setStringPainted(true);
                    dialogProgresso.add(barraProgresso, BorderLayout.CENTER);
                    dialogProgresso.setVisible(true);

                    Thread threadSalvamento = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                barraProgresso.setValue(20);
                                barraProgresso.setString("Buscando dados no banco...");
                                Thread.sleep(800);

                                barraProgresso.setValue(50);
                                barraProgresso.setString("Calculando médias e formatando...");
                                
                                GeradorRelatorioPDF gerador = new GeradorRelatorioPDF();
                                gerador.gerarRelatorio(caminhoFinal);

                                barraProgresso.setValue(100);
                                barraProgresso.setString("Finalizado!");
                                Thread.sleep(500);

                                dialogProgresso.dispose();
                                JOptionPane.showMessageDialog(null, "Relatório PDF gerado com sucesso em:\n" + caminhoFinal);

                            } catch (Exception ex) {
            LogErro.registrar(CodigoErro.ERRO_SISTEMA, ex);
                                dialogProgresso.dispose();
                                JOptionPane.showMessageDialog(null, "Erro ao gerar PDF: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                            } finally {
                                btnGerarRelatorio.setEnabled(true);
                            }
                        }
                    });
                    threadSalvamento.start();
                }
            }
        });
    }


    // Métodos auxiliares para criação de botões
    private JButton criarBotaoMenu(String texto, String caminhoIcone) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botao.setBackground(new Color(240, 248, 255));
        botao.setFocusPainted(false);

        botao.setHorizontalTextPosition(SwingConstants.CENTER);
        botao.setVerticalTextPosition(SwingConstants.BOTTOM);

        try {
            ImageIcon icone = new ImageIcon(getClass().getResource(caminhoIcone));
            Image img = icone.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            botao.setIcon(new ImageIcon(img));

        } catch (Exception ex) {
            LogErro.registrar(CodigoErro.ERRO_SISTEMA, ex);
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
            LogErro.registrar(CodigoErro.ERRO_SISTEMA, ex);
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
            LogErro.registrar(CodigoErro.ERRO_SISTEMA, ex);
        System.out.println("Ícone não encontrado: " + caminhoIcone);
    }

    return botao;
}
}
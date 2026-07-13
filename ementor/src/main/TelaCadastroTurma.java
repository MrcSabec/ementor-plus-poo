package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import dao.TurmaDAO;

public class TelaCadastroTurma extends JFrame {

    public TelaCadastroTurma() {
        setTitle("eMentor-Plus - Cadastrar Turma");
        setSize(500, 400); // Configuração de dimensão
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        try {
            ImageIcon iconeLogo = new ImageIcon(getClass().getResource("/imagens/logo.png"));
            Image imgLogo = iconeLogo.getImage().getScaledInstance(182, 100, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(imgLogo));
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
            painelTopo.add(lblLogo, BorderLayout.CENTER);
        } catch (Exception ex) {
            System.out.println("Erro ao carregar a logo.");
        }
        add(painelTopo, BorderLayout.NORTH);

        JPanel painelCentralizador = new JPanel(new GridBagLayout());

        // Configuração do LayoutManager
        JPanel painelFormulario = new JPanel(new GridLayout(2, 2, 10, 10));
        Font fontePadrao = new Font("Segoe UI", Font.PLAIN, 18);

        // Instanciação dos campos de texto
        JTextField txtCodigo = criarCampo(painelFormulario, "Código da Turma:", fontePadrao);
        JTextField txtNome = criarCampo(painelFormulario, "Nome da Turma:", fontePadrao);

        painelCentralizador.add(painelFormulario);
        add(painelCentralizador, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // Botão Voltar
        JButton btnVoltar = new JButton("Voltar");
        try {
            ImageIcon iconeVoltarOriginal = new ImageIcon(getClass().getResource("/imagens/icone_voltar_black.png"));
            Image imagemVoltarRedimensionada = iconeVoltarOriginal.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            btnVoltar.setIcon(new ImageIcon(imagemVoltarRedimensionada));
        } catch (Exception ex) {
            System.out.println("Ícone voltar não encontrado.");
        }
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVoltar.setBackground(new Color(240, 248, 255));
        btnVoltar.setForeground(Color.BLACK);

        // Botão Salvar
        JButton btnSalvar = new JButton("Salvar");
        try {
            ImageIcon iconeSalvarOriginal = new ImageIcon(getClass().getResource("/imagens/icone_salvar.png"));
            Image imagemSalvarRedimensionada = iconeSalvarOriginal.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            btnSalvar.setIcon(new ImageIcon(imagemSalvarRedimensionada));
        } catch (Exception ex) {
            System.out.println("Ícone salvar não encontrado.");
        }
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.setBackground(new Color(0, 200, 100));
        btnSalvar.setForeground(Color.WHITE);

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnSalvar);
        add(painelBotoes, BorderLayout.SOUTH);

        // ==========================================
        // Listeners de eventos
        // ==========================================
        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fecha a tela atual
                new MenuPrincipal().setVisible(true); // Volta pro menu
            }
        });

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Coleta os dados do formulário
                String codigoStr = txtCodigo.getText();
                String nomeStr = txtNome.getText();
                
                if(codigoStr.isEmpty() || nomeStr.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Desabilita o botão para evitar duplicação
                btnSalvar.setEnabled(false);

                // Cria o diálogo de progresso
                JDialog dialogProgresso = new JDialog();
                dialogProgresso.setTitle("Salvando...");
                dialogProgresso.setSize(300, 100);
                dialogProgresso.setLocationRelativeTo(null);
                dialogProgresso.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Impede fechamento manual

                JProgressBar barraProgresso = new JProgressBar(0, 100);
                barraProgresso.setStringPainted(true); // Exibe a porcentagem
                dialogProgresso.add(barraProgresso, BorderLayout.CENTER);
                dialogProgresso.setVisible(true);

                // Inicia thread de salvamento
                Thread threadSalvamento = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Atualiza progresso: preparação
                            barraProgresso.setValue(30);
                            barraProgresso.setString("Preparando dados...");
                            Thread.sleep(500); // Simulação de tempo de processamento

                            // Instancia e salva a Turma
                            Turma novaTurma = new Turma(codigoStr, nomeStr);
                            TurmaDAO turmaDao = new TurmaDAO();
                            turmaDao.inserir(novaTurma);

                            barraProgresso.setValue(100);
                            barraProgresso.setString("Finalizado!");
                            Thread.sleep(500);

                            // Conclui e fecha a tela
                            dialogProgresso.dispose();
                            JOptionPane.showMessageDialog(null, "Turma cadastrada com sucesso!");

                            dispose(); // Fecha a janela
                            new MenuPrincipal().setVisible(true);
                        } catch (Exception ex) {
                            dialogProgresso.dispose();
                            btnSalvar.setEnabled(true);
                            JOptionPane.showMessageDialog(null, "Erro ao salvar turma: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                            registrarErroLog(ex.getMessage());
                        }
                    }
                });
                threadSalvamento.start();
            }
        });
    }

    private JTextField criarCampo(JPanel painel, String labelTexto, Font fonte) {
        JLabel label = new JLabel(labelTexto);
        label.setFont(fonte);
        JTextField campo = new JTextField();
        campo.setFont(fonte);
        campo.setPreferredSize(new Dimension(250, 25));
        painel.add(label);
        painel.add(campo);
        return campo;
    }

    private void registrarErroLog(String mensagemErro) {
        try (FileWriter writer = new FileWriter("erros.dat", true)) {
            SimpleDateFormat dataHoraAtual = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String dataFormatada = dataHoraAtual.format(new Date());
            writer.write("[" + dataFormatada + "] Erro: " + mensagemErro + "\n");
        } catch (IOException e) {
            System.out.println("Erro ao tentar gravar no arquivo de log: " + e.getMessage());
        }
    }
}

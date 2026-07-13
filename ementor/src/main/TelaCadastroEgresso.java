package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import dao.EgressoDAO;
import dao.TurmaDAO;

public class TelaCadastroEgresso extends JFrame {

    private JTextField txtNome, txtCpf, txtDataNasc, txtTelefone, txtRua, txtBairro, txtCidade, txtEstado;
    private JTextField txtMatricula, txtPeriodo;
    private JComboBox<main.Turma> cbTurma;
    private JTextField txtProfissao, txtSalario, txtCursoAnterior, txtCursoAtual;

    public TelaCadastroEgresso() {
        setTitle("eMentor-Plus - Cadastrar Egresso");
        setSize(700, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());



        // Componentes do painel superior
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
        
        JLabel lblTitulo = new JLabel("Cadastro de Egresso", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        painelTopo.add(lblTitulo, BorderLayout.SOUTH);
        add(painelTopo, BorderLayout.NORTH);

        // Componentes do painel central
        JPanel painelCentralizador = new JPanel(new GridBagLayout());

        JPanel painelFormulario = new JPanel(new GridLayout(15, 2, 10, 8));
        Font fontePadrao = new Font("Segoe UI", Font.PLAIN, 14);

        // 1. Dados Pessoais
        txtNome = criarCampo(painelFormulario, "Nome Completo:", fontePadrao);
        txtCpf = criarCampoComChave(painelFormulario, "CPF:", fontePadrao);
        txtDataNasc = criarCampo(painelFormulario, "Data de Nascimento:", fontePadrao);
        txtTelefone = criarCampo(painelFormulario, "Telefone:", fontePadrao);
        txtRua = criarCampo(painelFormulario, "Rua:", fontePadrao);
        txtBairro = criarCampo(painelFormulario, "Bairro:", fontePadrao);
        txtCidade = criarCampo(painelFormulario, "Cidade:", fontePadrao);
        txtEstado = criarCampo(painelFormulario, "Estado:", fontePadrao);

        // 2. Dados Acadêmicos (Aluno)
        txtMatricula = criarCampoComChave(painelFormulario, "Matrícula Anterior:", fontePadrao);
        txtPeriodo = criarCampo(painelFormulario, "Último Período:", fontePadrao);

        JLabel lblTurma = new JLabel("Turma:");
        lblTurma.setFont(fontePadrao);
        lblTurma.setHorizontalAlignment(SwingConstants.RIGHT);
        cbTurma = new JComboBox<>();
        cbTurma.setFont(fontePadrao);
        cbTurma.setPreferredSize(new Dimension(200, 25));
        
        try {
            TurmaDAO turmaDAO = new TurmaDAO();
            for (main.Turma t : turmaDAO.listarTurmas()) {
                cbTurma.addItem(t);
            }
        } catch (Exception ex) {
            System.out.println("Erro ao carregar turmas.");
        }

        cbTurma.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof main.Turma) {
                    setText(((main.Turma) value).getNome() + " (" + ((main.Turma) value).getCodigo() + ")");
                }
                return this;
            }
        });

        painelFormulario.add(lblTurma);
        painelFormulario.add(cbTurma);

        // 3. Dados Específicos do Egresso
        txtProfissao = criarCampo(painelFormulario, "Profissão Atual:", fontePadrao);
        txtSalario = criarCampo(painelFormulario, "Faixa Salarial (R$):", fontePadrao);
        txtCursoAnterior = criarCampo(painelFormulario, "Curso Anterior:", fontePadrao);
        txtCursoAtual = criarCampo(painelFormulario, "Curso Atual:", fontePadrao);

        painelCentralizador.add(painelFormulario);

        JScrollPane scrollFormulario = new JScrollPane(painelCentralizador);
        scrollFormulario.setBorder(null);
        add(scrollFormulario, BorderLayout.CENTER);

        // Componentes do painel inferior
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVoltar.setBackground(new Color(240, 248, 255));
        
        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLimpar.setBackground(new Color(70, 130, 180));
        btnLimpar.setForeground(Color.WHITE);

        JButton btnSalvar = new JButton("Salvar Egresso");
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.setBackground(new Color(0, 200, 100));
        btnSalvar.setForeground(Color.WHITE);

        try {
            java.net.URL urlVoltar = getClass().getResource("/imagens/icone_voltar_black.png");
            if(urlVoltar != null) {
                Image img = new ImageIcon(urlVoltar).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                btnVoltar.setIcon(new ImageIcon(img));
            }
            // Add icon for Limpar if we have one, otherwise just text
            java.net.URL urlSalvar = getClass().getResource("/imagens/icone_salvar.png");
            if(urlSalvar != null) {
                Image img = new ImageIcon(urlSalvar).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                btnSalvar.setIcon(new ImageIcon(img));
            }
        } catch (Exception ex) {
            System.out.println("Aviso: Ícones não encontrados.");
        }

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnSalvar);
        add(painelBotoes, BorderLayout.SOUTH);

        // EVENTOS DOS BOTÕES
        btnVoltar.addActionListener(e -> {
            dispose();
            new MenuPrincipal().setVisible(true);
        });
        
        btnLimpar.addActionListener(e -> {
            txtNome.setText("");
            txtCpf.setText("");
            txtDataNasc.setText("");
            txtTelefone.setText("");
            txtRua.setText("");
            txtBairro.setText("");
            txtCidade.setText("");
            txtEstado.setText("");
            txtMatricula.setText("");
            txtPeriodo.setText("");
            txtProfissao.setText("");
            txtSalario.setText("");
            txtCursoAnterior.setText("");
            txtCursoAtual.setText("");
            if (cbTurma.getItemCount() > 0) cbTurma.setSelectedIndex(0);
        });

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nomeStr = txtNome.getText();
                    String cpfStr = txtCpf.getText();
                    String telefoneStr = txtTelefone.getText();
                    String ruaStr = txtRua.getText();
                    String bairroStr = txtBairro.getText();
                    String cidadeStr = txtCidade.getText();
                    String estadoStr = txtEstado.getText();
                    String matriculaStr = txtMatricula.getText();
                    int periodoInt = Integer.parseInt(txtPeriodo.getText());
                    main.Turma turmaSel = (main.Turma) cbTurma.getSelectedItem();
                    
                    String profissaoStr = txtProfissao.getText();
                    double salarioDb = Double.parseDouble(txtSalario.getText().replace(",", "."));
                    String cursoAntStr = txtCursoAnterior.getText();
                    String cursoAtuStr = txtCursoAtual.getText();

                    SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
                    Date dataNscDt = formatoData.parse(txtDataNasc.getText());

                    btnSalvar.setEnabled(false);

                    JDialog dialogProgresso = new JDialog();
                    dialogProgresso.setTitle("Salvando Egresso...");
                    dialogProgresso.setSize(300, 100);
                    dialogProgresso.setLocationRelativeTo(null);
                    dialogProgresso.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

                    JProgressBar barraProgresso = new JProgressBar(0, 100);
                    barraProgresso.setStringPainted(true);
                    dialogProgresso.add(barraProgresso, BorderLayout.CENTER);
                    dialogProgresso.setVisible(true);

                    Thread threadSalvamento = new Thread(() -> {
                        try {
                            barraProgresso.setValue(30);
                            barraProgresso.setString("Processando dados de egresso...");
                            Thread.sleep(600);
                            
                            Egresso novoEgresso = new Egresso(nomeStr, dataNscDt, cpfStr, telefoneStr, ruaStr, bairroStr, cidadeStr, estadoStr, matriculaStr, periodoInt, turmaSel, profissaoStr, salarioDb, cursoAntStr, cursoAtuStr);

                            barraProgresso.setValue(70);
                            barraProgresso.setString("Gravando no Banco de Dados...");
                            
                            EgressoDAO egressoDAO = new EgressoDAO();
                            egressoDAO.inserir(novoEgresso);
                            
                            Thread.sleep(800);

                            barraProgresso.setValue(100);
                            barraProgresso.setString("Finalizado!");
                            Thread.sleep(400);

                            dialogProgresso.dispose();
                            JOptionPane.showMessageDialog(null, "Egresso cadastrado com sucesso!");

                            dispose();
                            new MenuPrincipal().setVisible(true);

                        } catch (Exception ex) {
                            dialogProgresso.dispose();
                            btnSalvar.setEnabled(true);
                            String causa = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
                            JOptionPane.showMessageDialog(null, "Erro ao salvar: " + causa);
                        }
                    });
                    threadSalvamento.start();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, digite valores numéricos válidos para Período e Faixa Salarial.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                } catch (java.text.ParseException ex) {
                    JOptionPane.showMessageDialog(null, "Data de Nascimento inválida! Use o formato dd/MM/yyyy.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private JTextField criarCampo(JPanel painel, String textoLabel, Font fonte) {
        JLabel label = new JLabel(textoLabel);
        label.setFont(fonte);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JTextField textField = new JTextField();
        textField.setFont(fonte);
        
        painel.add(label);
        painel.add(textField);
        
        return textField;
    }

    private JTextField criarCampoComChave(JPanel painel, String textoLabel, Font fonte) {
        JPanel painelLabel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        painelLabel.setOpaque(false);
        try {
            ImageIcon iconeChave = new ImageIcon(getClass().getResource("/imagens/icone_chave_black.png"));
            Image imgChave = iconeChave.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            JLabel lblIcone = new JLabel(new ImageIcon(imgChave));
            painelLabel.add(lblIcone);
        } catch (Exception e) {}
        
        JLabel label = new JLabel(textoLabel);
        label.setFont(fonte);
        painelLabel.add(label);
        
        JTextField textField = new JTextField();
        textField.setFont(fonte);
        
        painel.add(painelLabel);
        painel.add(textField);
        
        return textField;
    }
}

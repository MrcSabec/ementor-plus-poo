package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TelaCadastroProfessor extends JFrame {

    public TelaCadastroProfessor() {
        setTitle("eMentor-Plus - Cadastrar Professor");
        setSize(700, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ==========================================
        // TOPO: LOGO E TÍTULO
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
        JLabel lblTitulo = new JLabel("Cadastro de Professor", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        painelTopo.add(lblTitulo, BorderLayout.SOUTH);
        add(painelTopo, BorderLayout.NORTH);

        // ==========================================
        // CENTRO: FORMULÁRIO (Herança e Cálculos)
        // ==========================================
        JPanel painelCentralizador = new JPanel(new GridBagLayout());
        JPanel painelFormulario = new JPanel(new GridLayout(15, 2, 10, 8));
        Font fontePadrao = new Font("Segoe UI", Font.PLAIN, 14);

        JTextField txtNome = criarCampoTexto(painelFormulario, "Nome Completo:", fontePadrao);
        JTextField txtCpf = criarCampoTexto(painelFormulario, "CPF:", fontePadrao);
        JTextField txtDataNasc = criarCampoTexto(painelFormulario, "Data de Nascimento:", fontePadrao);
        JTextField txtTelefone = criarCampoTexto(painelFormulario, "Telefone:", fontePadrao);
        JTextField txtRua = criarCampoTexto(painelFormulario, "Rua:", fontePadrao);
        JTextField txtBairro = criarCampoTexto(painelFormulario, "Bairro:", fontePadrao);
        JTextField txtCidade = criarCampoTexto(painelFormulario, "Cidade:", fontePadrao);
        JTextField txtEstado = criarCampoTexto(painelFormulario, "Estado:", fontePadrao);

        // 2. Atributos Exclusivos de Professor
        JTextField txtDataAdmissao = criarCampoTexto(painelFormulario, "Data de Admissão:", fontePadrao);

        // Caixas de Seleção (Combo Box) para os cargos exigidos (sim/não)
        JComboBox<String> cbChefia = criarComboBox(painelFormulario, "Cargo de Chefia?", new String[]{"Não", "Sim"}, fontePadrao);
        JComboBox<String> cbCoordenacao = criarComboBox(painelFormulario, "Cargo de Coordenação?", new String[]{"Não", "Sim"}, fontePadrao);

        // Campos para o cálculo do Salário Bruto
        JTextField txtSalarioBase = criarCampoTexto(painelFormulario, "Salário Base (R$):", fontePadrao);
        JTextField txtAdicionalChefia = criarCampoTexto(painelFormulario, "Adicional Chefia (R$):", fontePadrao);
        JTextField txtAdicionalCoordenacao = criarCampoTexto(painelFormulario, "Adicional Coordenação (R$):", fontePadrao);

        // Configuração inicial e lógica de interatividade
        txtAdicionalChefia.setEnabled(false);
        txtAdicionalChefia.setText("0");
        txtAdicionalCoordenacao.setEnabled(false);
        txtAdicionalCoordenacao.setText("0");

        cbChefia.addActionListener(e -> {
            boolean isSim = cbChefia.getSelectedItem().toString().equals("Sim");
            txtAdicionalChefia.setEnabled(isSim);
            if (!isSim) txtAdicionalChefia.setText("0");
        });

        cbCoordenacao.addActionListener(e -> {
            boolean isSim = cbCoordenacao.getSelectedItem().toString().equals("Sim");
            txtAdicionalCoordenacao.setEnabled(isSim);
            if (!isSim) txtAdicionalCoordenacao.setText("0");
        });

        painelCentralizador.add(painelFormulario);

        JScrollPane scrollFormulario = new JScrollPane(painelCentralizador);
        scrollFormulario.setBorder(null);
        add(scrollFormulario, BorderLayout.CENTER);

        // ==========================================
        // SUL: BOTÕES DE AÇÃO COM ÍCONES
        // ==========================================
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVoltar.setBackground(new Color(240, 248, 255));

        JButton btnSalvar = new JButton("Salvar Professor");
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.setBackground(new Color(0, 200, 100));
        btnSalvar.setForeground(Color.WHITE);

        try {
            ImageIcon iconVoltar = new ImageIcon(getClass().getResource("/imagens/icone_voltar_black.png"));
            Image imgVoltar = iconVoltar.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            if(iconVoltar != null) btnVoltar.setIcon(new ImageIcon(imgVoltar));

            ImageIcon iconSalvar = new ImageIcon(getClass().getResource("/imagens/icone_salvar.png"));
            Image imgSalvar = iconSalvar.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            if(iconSalvar != null) btnSalvar.setIcon(new ImageIcon(imgSalvar));
        } catch (Exception ex) {
            System.out.println("Aviso: Ícones não encontrados.");
        }

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnSalvar);
        add(painelBotoes, BorderLayout.SOUTH);

        // ==========================================
        // AÇÕES DOS BOTÕES (Com Threads e Cálculos)
        // ==========================================
        btnVoltar.addActionListener(e -> {
            dispose();
            new MenuPrincipal().setVisible(true);
        });

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String isChefia = cbChefia.getSelectedItem().toString();
                    String isCoordenacao = cbCoordenacao.getSelectedItem().toString();

                    // Lógica de Negócio do Salário exigida no documento
                    double salBase = Double.parseDouble(txtSalarioBase.getText().replace(",", "."));
                    double adicChefia = isChefia.equals("Sim") ? Double.parseDouble(txtAdicionalChefia.getText().replace(",", ".")) : 0.0;
                    double adicCoord = isCoordenacao.equals("Sim") ? Double.parseDouble(txtAdicionalCoordenacao.getText().replace(",", ".")) : 0.0;

                    // Calculando o Salário Bruto
                    double salarioBruto = salBase + adicChefia + adicCoord;

                    // Calculando o Salário Líquido (14% INSS e 22,5% IRPF se >= 5000)
                    double descontoINSS = salarioBruto * 0.14;
                    double descontoIRPF = (salarioBruto >= 5000.00) ? (salarioBruto * 0.225) : 0.0;
                    double salarioLiquido = salarioBruto - descontoINSS - descontoIRPF;

                    SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
                    Date dataAdmissao = formatoData.parse(txtDataAdmissao.getText());

                    btnSalvar.setEnabled(false);

                    // Requisito: Thread e Barra de Progresso
                    JDialog dialogProgresso = new JDialog();
                    dialogProgresso.setTitle("Salvando Professor...");
                    dialogProgresso.setSize(350, 100);
                    dialogProgresso.setLocationRelativeTo(null);
                    dialogProgresso.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

                    JProgressBar barraProgresso = new JProgressBar(0, 100);
                    barraProgresso.setStringPainted(true);
                    dialogProgresso.add(barraProgresso, BorderLayout.CENTER);
                    dialogProgresso.setVisible(true);

                    Thread threadSalvamento = new Thread(() -> {
                        try {
                            barraProgresso.setValue(20);
                            barraProgresso.setString("Calculando folha de pagamento...");
                            Thread.sleep(600);

                            barraProgresso.setValue(60);
                            barraProgresso.setString("Gravando Professor no Banco...");
                            
                            Professor novoProfessor = new Professor();
                            novoProfessor.setNome(txtNome.getText());
                            novoProfessor.setCpf(txtCpf.getText());
                            novoProfessor.setTelefone(txtTelefone.getText());
                            novoProfessor.setRua(txtRua.getText());
                            novoProfessor.setBairro(txtBairro.getText());
                            novoProfessor.setCidade(txtCidade.getText());
                            novoProfessor.setEstado(txtEstado.getText());
                            
                            SimpleDateFormat fmtPessoa = new SimpleDateFormat("dd/MM/yyyy");
                            novoProfessor.setNascimento(fmtPessoa.parse(txtDataNasc.getText()));
                            
                            novoProfessor.setDataAdmissao(dataAdmissao);
                            novoProfessor.setCargoChefia(isChefia.equals("Sim"));
                            novoProfessor.setCargoCoordenacao(isCoordenacao.equals("Sim"));
                            novoProfessor.setSalarioBruto(salarioBruto);
                            
                            dao.ProfessorDAO dao = new dao.ProfessorDAO();
                            dao.inserir(novoProfessor);
                            
                            Thread.sleep(800);

                            barraProgresso.setValue(100);
                            barraProgresso.setString("Finalizado!");
                            Thread.sleep(400);

                            dialogProgresso.dispose();

                            String resumo = String.format("Professor salvo com sucesso!\nSalário Bruto: R$ %.2f\nSalário Líquido: R$ %.2f", salarioBruto, salarioLiquido);
                            JOptionPane.showMessageDialog(null, resumo);

                            dispose();
                            new MenuPrincipal().setVisible(true);

                        } catch (Exception ex) {
                            dialogProgresso.dispose();
                            btnSalvar.setEnabled(true);
                            JOptionPane.showMessageDialog(null, "Erro ao salvar: " + (ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage()));
                            // Gravar no erros.dat
                        }
                    });
                    threadSalvamento.start();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, digite valores numéricos válidos nos campos de salário (ex: 2500.50).", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                } catch (java.text.ParseException ex) {
                    JOptionPane.showMessageDialog(null, "Data de Admissão inválida! Use o formato dd/MM/yyyy.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Métodos Auxiliares
    private JTextField criarCampoTexto(JPanel painel, String textoLabel, Font fonte) {
        JLabel label = new JLabel(textoLabel);
        label.setFont(fonte);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        JTextField txtField = new JTextField();
        txtField.setFont(fonte);
        txtField.setPreferredSize(new Dimension(200, 25));
        painel.add(label);
        painel.add(txtField);
        return txtField;
    }

    private JComboBox<String> criarComboBox(JPanel painel, String textoLabel, String[] opcoes, Font fonte) {
        JLabel label = new JLabel(textoLabel);
        label.setFont(fonte);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        JComboBox<String> comboBox = new JComboBox<>(opcoes);
        comboBox.setFont(fonte);
        comboBox.setPreferredSize(new Dimension(200, 25));
        painel.add(label);
        painel.add(comboBox);
        return comboBox;
    }
}
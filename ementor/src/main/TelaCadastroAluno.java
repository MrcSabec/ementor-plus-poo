package  main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import dao.*;
import java.text.SimpleDateFormat;

public class TelaCadastroAluno extends JFrame {

    public TelaCadastroAluno() {
        setTitle("eMentor-Plus - Cadastrar Aluno");
        setSize(600, 750); // Configuração de dimensão
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
        JPanel painelFormulario = new JPanel(new GridLayout(11, 2, 10, 10));
        Font fontePadrao = new Font("Segoe UI", Font.PLAIN, 18);

        // Instanciação dos campos de texto
        JTextField txtNome = criarCampo(painelFormulario, "Nome Completo:", fontePadrao);
        JTextField txtCpf = criarCampo(painelFormulario, "CPF:", fontePadrao);
        JTextField txtDataNasc = criarCampo(painelFormulario, "Data de Nascimento:", fontePadrao);
        JTextField txtTelefone = criarCampo(painelFormulario, "Telefone:", fontePadrao);
        JTextField txtRua = criarCampo(painelFormulario, "Rua:", fontePadrao);
        JTextField txtBairro = criarCampo(painelFormulario, "Bairro:", fontePadrao);
        JTextField txtCidade = criarCampo(painelFormulario, "Cidade:", fontePadrao);
        JTextField txtEstado = criarCampo(painelFormulario, "Estado:", fontePadrao);
        JTextField txtMatricula = criarCampo(painelFormulario, "Matrícula:", fontePadrao);
        JTextField txtPeriodo = criarCampo(painelFormulario, "Período:", fontePadrao);
        // Campo de seleção de Turma
        JLabel lblTurma = new JLabel("Turma:");
        lblTurma.setFont(fontePadrao);
        JComboBox<Turma> cbTurma = new JComboBox<>();
        cbTurma.setFont(fontePadrao);
        cbTurma.setPreferredSize(new Dimension(250, 25));
        
        // Carrega as turmas do banco de dados
        TurmaDAO turmaDAO = new TurmaDAO();
        java.util.List<Turma> listaTurmas = turmaDAO.listarTurmas();
        for (Turma t : listaTurmas) {
            cbTurma.addItem(t);
        }
        
        // Configura o renderer para exibir o nome da turma
        cbTurma.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Turma) {
                    setText(((Turma) value).getNome() + " (" + ((Turma) value).getCodigo() + ")");
                }
                return this;
            }
        });
        
        painelFormulario.add(lblTurma);
        painelFormulario.add(cbTurma);
        // Adiciona os painéis à janela
        painelCentralizador.add(painelFormulario);
        add(painelCentralizador, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // Botão Voltar
        JButton btnVoltar = new JButton("Voltar");
        try {
            ImageIcon iconeVoltarOriginal = new ImageIcon(getClass().getResource("/imagens/icone_voltar.png"));
            Image imagemVoltarRedimensionada = iconeVoltarOriginal.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            btnVoltar.setIcon(new ImageIcon(imagemVoltarRedimensionada));
        } catch (Exception ex) {
            System.out.println("Ícone voltar não encontrado.");
        }
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVoltar.setBackground(new Color(70, 130, 180));
        btnVoltar.setForeground(Color.WHITE);

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
                MenuPrincipal menu = new MenuPrincipal(); // Volta pro menu
                menu.setVisible(true);
            }
        });

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
                    Date dataNscDt = formatoData.parse(txtDataNasc.getText());
                    // Coleta os dados do formulário
                    String nomeStr = txtNome.getText();
                    String cpfStr = txtCpf.getText();
                    String telefoneStr = txtTelefone.getText();
                    String ruaStr = txtRua.getText();
                    String bairroStr = txtBairro.getText();
                    String cidadeStr = txtCidade.getText();
                    String estadoStr = txtEstado.getText();
                    String matriculaStr = txtMatricula.getText();
                    int periodoStr = Integer.parseInt(txtPeriodo.getText());
                    Turma turmaSelecionada = (Turma) cbTurma.getSelectedItem();
                    
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

                    // =========================================================
                    // Inicia thread de salvamento
                    // =========================================================
                    Thread threadSalvamento = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Atualiza progresso: preparação
                                barraProgresso.setValue(20);
                                barraProgresso.setString("Preparando dados...");
                                Thread.sleep(500); // Simulação de tempo de processamento

                                // Instancia e salva o Aluno
                                Aluno novoAluno = new Aluno(nomeStr, dataNscDt, cpfStr, telefoneStr, ruaStr, bairroStr, cidadeStr, estadoStr, matriculaStr, periodoStr, turmaSelecionada);
                                AlunoDAO alunoDao = new AlunoDAO();
                                alunoDao.inserir(novoAluno);

                                barraProgresso.setValue(60);
                                barraProgresso.setString("Aluno salvo! Criando acesso...");
                                Thread.sleep(500);

                                // Cria o usuário de acesso para o Aluno
                                Usuario novoUsuario = new Usuario();
                                novoUsuario.setDadosUsuario(matriculaStr, "mudar@123", NivelAcesso.NIVEL_3);
                                UsuarioDAO usuarioDao = new UsuarioDAO();
                                usuarioDao.inserir(novoUsuario);

                                barraProgresso.setValue(100);
                                barraProgresso.setString("Finalizado!");
                                Thread.sleep(500);

                                // Conclui e fecha a tela
                                dialogProgresso.dispose();
                                JOptionPane.showMessageDialog(null, "Aluno e Usuário cadastrados com sucesso!\nLogin: " + matriculaStr + "\nSenha Padrão: mudar@123");

                                dispose(); // Fecha a janela
                                MenuPrincipal menu = new MenuPrincipal();
                                menu.setVisible(true);
                            } catch (Exception ex) {
                                dialogProgresso.dispose();
                                btnSalvar.setEnabled(true);
                                JOptionPane.showMessageDialog(null, "Erro ao salvar no banco: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });

                    // Inicia a execução da thread
                    threadSalvamento.start();
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, digite apenas números no campo Período.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                    btnSalvar.setEnabled(true);
                } catch (java.text.ParseException ex) {
                    JOptionPane.showMessageDialog(null, "Data de Nascimento inválida! Use o formato dd/MM/yyyy.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                    btnSalvar.setEnabled(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro inesperado: " + ex.getMessage());
                    btnSalvar.setEnabled(true);
                }
            }
        });
    }

    // ==========================================
    // Método auxiliar para criar campos do formulário
    // ==========================================
    private JTextField criarCampo(JPanel painel, String textoLabel, Font fonte) {
        JLabel label = new JLabel(textoLabel);
        label.setFont(fonte);
        JTextField txtField = new JTextField();
        txtField.setFont(fonte);
        txtField.setPreferredSize(new Dimension(250, 25)); // Define o tamanho preferencial

        painel.add(label);
        painel.add(txtField);
        return txtField;
    }
}

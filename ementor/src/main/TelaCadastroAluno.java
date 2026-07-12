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
        setSize(600, 750); // Janela um pouco mais alta para caber todos os campos
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ==========================================
        // TOPO: LOGO E TÍTULO (Te perseguindo!)
        // ==========================================
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        try {
            ImageIcon iconeLogo = new ImageIcon(getClass().getResource("/imagens/logo.png"));
            Image imgLogo = iconeLogo.getImage().getScaledInstance(388, 213, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(imgLogo));
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
            painelTopo.add(lblLogo, BorderLayout.CENTER);
        } catch (Exception ex) {
            System.out.println("Erro ao carregar a logo.");
        }
        add(painelTopo, BorderLayout.NORTH);

        // ==========================================
        // CENTRO: FORMULÁRIO COMPACTO
        // ==========================================
        // Usamos GridBagLayout no painel centralizador para o formulário não esticar
        JPanel painelCentralizador = new JPanel(new GridBagLayout());

        // GridLayout para alinhar os rótulos e caixas de texto (11 linhas, 2 colunas)
        JPanel painelFormulario = new JPanel(new GridLayout(11, 2, 10, 10));
        Font fontePadrao = new Font("Segoe UI", Font.PLAIN, 18);

        // Criando os campos
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
        JTextField txtTurma = criarCampo(painelFormulario, "Turma:", fontePadrao);

        // Adiciona o formulário no centralizador, e o centralizador na janela
        painelCentralizador.add(painelFormulario);
        add(painelCentralizador, BorderLayout.CENTER);

        // ==========================================
        // SUL: BOTÕES DE AÇÃO (Com ícones exigidos)
        // ==========================================
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // Botão Voltar (Exigência do projeto)
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
        // AÇÕES DOS BOTÕES
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
                    // 1. Coletar os dados digitados ANTES de iniciar a Thread
                    String nomeStr = txtNome.getText();
                    String cpfStr = txtCpf.getText();
                    String telefoneStr = txtTelefone.getText();
                    String ruaStr = txtRua.getText();
                    String bairroStr = txtBairro.getText();
                    String cidadeStr = txtCidade.getText();
                    String estadoStr = txtEstado.getText();
                    String matriculaStr = txtMatricula.getText();
                    int periodoStr = Integer.parseInt(txtPeriodo.getText());
                    String turmaStr = txtTurma.getText();

                    // 2. Desabilitar o botão para o usuário não clicar duas vezes
                    btnSalvar.setEnabled(false);

                    // 3. Criar a Janela com a Barra de Progresso (Exigência do Projeto)
                    JDialog dialogProgresso = new JDialog();
                    dialogProgresso.setTitle("Salvando...");
                    dialogProgresso.setSize(300, 100);
                    dialogProgresso.setLocationRelativeTo(null);
                    dialogProgresso.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Impede de fechar no X

                    JProgressBar barraProgresso = new JProgressBar(0, 100);
                    barraProgresso.setStringPainted(true); // Mostra a porcentagem %
                    dialogProgresso.add(barraProgresso, BorderLayout.CENTER);
                    dialogProgresso.setVisible(true);

                    // =========================================================
                    // 4. A THREAD (Processo em paralelo para salvar no Banco)
                    // =========================================================
                    Thread threadSalvamento = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Passo A: Simula o início do salvamento
                                barraProgresso.setValue(20);
                                barraProgresso.setString("Preparando dados...");
                                Thread.sleep(500); // Pausa apenas para o efeito visual da barra

                                // Passo B: Instanciar o Aluno e salvar no banco
                                Aluno novoAluno = new Aluno(nomeStr, dataNscDt, cpfStr, telefoneStr, ruaStr, bairroStr, cidadeStr, estadoStr, matriculaStr, periodoStr, turmaStr);
                                AlunoDAO alunoDao = new AlunoDAO();
                                alunoDao.inserir(novoAluno);

                                barraProgresso.setValue(60);
                                barraProgresso.setString("Aluno salvo! Criando acesso...");
                                Thread.sleep(500);

                                // Passo C: Tornar o Aluno um "Usuário" do sistema!
                                Usuario novoUsuario = new Usuario();
                                novoUsuario.setDadosUsuario(matriculaStr, "mudar@123", NivelAcesso.NIVEL_3);
                                UsuarioDAO usuarioDao = new UsuarioDAO();
                                usuarioDao.inserir(novoUsuario);

                                barraProgresso.setValue(100);
                                barraProgresso.setString("Finalizado!");
                                Thread.sleep(500);

                                // Passo D: Sucesso e fechar a tela
                                dialogProgresso.dispose();
                                JOptionPane.showMessageDialog(null, "Aluno e Usuário cadastrados com sucesso!\nLogin: " + matriculaStr + "\nSenha Padrão: mudar@123");

                                dispose(); // Fecha a tela de cadastro
                                MenuPrincipal menu = new MenuPrincipal();
                                menu.setVisible(true);
                            } catch (Exception ex) {
                                dialogProgresso.dispose();
                                btnSalvar.setEnabled(true);
                                JOptionPane.showMessageDialog(null, "Erro ao salvar no banco: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });

                    // 5. Dar o "Start" na Thread
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
    // MÉTODO AUXILIAR PARA CRIAR CAMPOS RAPIDAMENTE
    // ==========================================
    private JTextField criarCampo(JPanel painel, String textoLabel, Font fonte) {
        JLabel label = new JLabel(textoLabel);
        label.setFont(fonte);
        JTextField txtField = new JTextField();
        txtField.setFont(fonte);
        txtField.setPreferredSize(new Dimension(250, 25)); // Define um tamanho fixo bonito

        painel.add(label);
        painel.add(txtField);
        return txtField;
    }
}

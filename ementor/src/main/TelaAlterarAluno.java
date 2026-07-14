package main;

import exception.LogErro;
import exception.CodigoErro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import dao.AlunoDAO;
import dao.TurmaDAO;

public class TelaAlterarAluno extends JFrame {

    private JTextField txtBuscaMatricula, txtNome, txtCpf, txtDataNasc, txtTelefone, txtCidade, txtEstado, txtMatricula, txtPeriodo;
    private JTextField[] txtNotas = new JTextField[10];
    private JComboBox<main.Turma> cbTurma;
    private Aluno alunoCarregado;
    private java.util.List<Aluno> listaAlunos;
    private int indiceAtual = -1;

    // Construtor padrão
    public TelaAlterarAluno() {
        this(""); // Chama o construtor abaixo passando vazio
    }

    // Construtor com parâmetro
    public TelaAlterarAluno(String matriculaInicial) {
        setTitle("eMentor-Plus - Alterar Dados do Aluno");
        setSize(700, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        try {
            listaAlunos = new AlunoDAO().listarAlunos();
        } catch (Exception ex) {
            LogErro.registrar(CodigoErro.ERRO_SISTEMA, ex);
            System.out.println("Erro ao carregar lista de alunos: " + ex.getMessage());
        }

        // Componentes do painel superior
        JPanel painelTopo = new JPanel(new BorderLayout());

        // Logo
        try {
            ImageIcon iconeLogo = new ImageIcon(getClass().getResource("/imagens/logo.png"));
            Image imgLogo = iconeLogo.getImage().getScaledInstance(182, 100, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(imgLogo));
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
            painelTopo.add(lblLogo, BorderLayout.NORTH);
        } catch (Exception ex) {
            LogErro.registrar(CodigoErro.ERRO_SISTEMA, ex);
            System.out.println("Aviso: Logo não encontrada.");
        }

        // Barra de Busca
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBusca.setBorder(BorderFactory.createTitledBorder("Buscar Registro"));

        JLabel lblBusca = new JLabel("Digite a Matrícula:");
        lblBusca.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtBuscaMatricula = new JTextField(15);
        txtBuscaMatricula.setText(matriculaInicial); // Preenche se vier da tabela!
        txtBuscaMatricula.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        if (!matriculaInicial.isEmpty() && listaAlunos != null) {
            for (int i = 0; i < listaAlunos.size(); i++) {
                if (listaAlunos.get(i).getMatricula().equals(matriculaInicial)) {
                    indiceAtual = i;
                    // We can't call preencherCampos here yet because fields are not instantiated.
                    // We will call it at the end of the constructor.
                    break;
                }
            }
        }

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(70, 130, 180));
        btnBuscar.setForeground(Color.WHITE);

        painelBusca.add(lblBusca);
        painelBusca.add(txtBuscaMatricula);
        painelBusca.add(btnBuscar);

        painelTopo.add(painelBusca, BorderLayout.CENTER);
        add(painelTopo, BorderLayout.NORTH);

        // Componentes do painel central
        JPanel painelCentralizador = new JPanel(new GridBagLayout());
        JPanel painelFormulario = new JPanel(new GridLayout(10, 2, 10, 10));
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        Font fontePadrao = new Font("Segoe UI", Font.PLAIN, 14);

        txtNome = criarCampo(painelFormulario, "Nome Completo:", fontePadrao);
        txtCpf = criarCampoComChave(painelFormulario, "CPF:", fontePadrao);
        txtCpf.setEditable(false); // CPF é chave primária e não deve ser alterado
        txtDataNasc = criarCampo(painelFormulario, "Data de Nascimento:", fontePadrao);
        txtTelefone = criarCampo(painelFormulario, "Telefone:", fontePadrao);
        txtCidade = criarCampo(painelFormulario, "Cidade:", fontePadrao);
        txtEstado = criarCampo(painelFormulario, "Estado:", fontePadrao);
        txtMatricula = criarCampoComChave(painelFormulario, "Matrícula:", fontePadrao);
        txtMatricula.setEditable(false); // Define chave primária como não editável
        txtPeriodo = criarCampo(painelFormulario, "Período:", fontePadrao);

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
            LogErro.registrar(CodigoErro.ERRO_SISTEMA, ex);
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

        // Painel Global do Centro que vai segurar o Formulario + Notas
        JPanel painelCentroGlobal = new JPanel(new BorderLayout());
        painelCentroGlobal.add(painelFormulario, BorderLayout.CENTER);

        // Painel Inferior: Exibição de Notas e Botão "Atribuir Notas"
        JPanel painelNotasContainer = new JPanel(new BorderLayout());
        painelNotasContainer.setBorder(BorderFactory.createTitledBorder("Notas Lançadas"));
        
        JPanel painelNotas = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        for (int i = 0; i < 10; i++) {
            txtNotas[i] = new JTextField(3);
            txtNotas[i].setEditable(false);
            txtNotas[i].setHorizontalAlignment(JTextField.CENTER);
            txtNotas[i].setBackground(new Color(245, 245, 245));
            painelNotas.add(new JLabel("N" + (i + 1) + ":"));
            painelNotas.add(txtNotas[i]);
        }
        
        JPanel painelAcaoNotas = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAtribuirNotas = new JButton("Gerenciar Notas");
        btnAtribuirNotas.setBackground(new Color(255, 165, 0)); // Laranja
        btnAtribuirNotas.setForeground(Color.WHITE);
        try {
            ImageIcon iconNotas = new ImageIcon(getClass().getResource("/imagens/icone_notas.png"));
            Image imgNotas = iconNotas.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            btnAtribuirNotas.setIcon(new ImageIcon(imgNotas));
        } catch (Exception ex) {
            LogErro.registrar(CodigoErro.ERRO_SISTEMA, ex);
            System.out.println("Ícone de notas não encontrado.");
        }
        painelAcaoNotas.add(btnAtribuirNotas);

        painelNotasContainer.add(painelNotas, BorderLayout.CENTER);
        painelNotasContainer.add(painelAcaoNotas, BorderLayout.SOUTH);

        painelCentroGlobal.add(painelNotasContainer, BorderLayout.SOUTH);

        painelCentralizador.add(painelCentroGlobal);
        add(painelCentralizador, BorderLayout.CENTER);

        // Componentes do painel inferior
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));

        JButton btnAnterior = new JButton("<< Anterior");
        btnAnterior.setBackground(new Color(240, 248, 255));
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(new Color(70, 130, 180));
        JButton btnProximo = new JButton("Próximo >>");
        btnProximo.setBackground(new Color(240, 248, 255));

        JButton btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.setBackground(new Color(34, 139, 34)); // Define cor de fundo verde
        btnSalvar.setForeground(Color.WHITE);

        // Configuração dos ícones
        try {
            ImageIcon iconVoltar = new ImageIcon(getClass().getResource("/imagens/icone_voltar.png"));
            Image imgVoltar = iconVoltar.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            if(iconVoltar != null) btnVoltar.setIcon(new ImageIcon(imgVoltar));

            ImageIcon iconSalvar = new ImageIcon(getClass().getResource("/imagens/icone_salvar.png"));
            Image imgSalvar = iconSalvar.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            if(iconSalvar != null) btnSalvar.setIcon(new ImageIcon(imgSalvar));
        } catch (Exception ex) {
            LogErro.registrar(CodigoErro.ERRO_SISTEMA, ex);
            System.out.println("Aviso: Ícones não encontrados.");
        }

        painelBotoes.add(btnAnterior);
        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnProximo);
        add(painelBotoes, BorderLayout.SOUTH);

        // ==========================================
        // EVENTOS DOS BOTÕES
        // ==========================================

        btnAtribuirNotas.addActionListener(e -> {
            String matricula = txtMatricula.getText();
            if (matricula == null || matricula.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Busque um aluno primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Abre a tela de notas passando a matrícula
            TelaNotasAluno telaNotas = new TelaNotasAluno(matricula);
            telaNotas.setVisible(true);
            dispose();
        });

        // 1. Ação Buscar
        btnBuscar.addActionListener(e -> {
            String matBusca = txtBuscaMatricula.getText();
            if(matBusca.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Digite uma matrícula para buscar!");
                return;
            }
            if (listaAlunos != null) {
                for (int i = 0; i < listaAlunos.size(); i++) {
                    if (listaAlunos.get(i).getMatricula().equals(matBusca)) {
                        indiceAtual = i;
                        preencherCampos(listaAlunos.get(i));
                        JOptionPane.showMessageDialog(null, "Dados recuperados com sucesso!");
                        return;
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Aluno não encontrado no banco de dados.");
        });

        // Navegação
        btnAnterior.addActionListener(e -> {
            if (listaAlunos == null || listaAlunos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhum dado carregado. Realize uma busca primeiro!");
                return;
            }
            if (indiceAtual > 0) {
                indiceAtual--;
                preencherCampos(listaAlunos.get(indiceAtual));
            } else {
                JOptionPane.showMessageDialog(null, "Atenção: Este já é o primeiro registro da lista!", "Navegação", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnProximo.addActionListener(e -> {
            if (listaAlunos == null || listaAlunos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhum dado carregado. Realize uma busca primeiro!");
                return;
            }
            if (indiceAtual < listaAlunos.size() - 1) {
                indiceAtual++;
                preencherCampos(listaAlunos.get(indiceAtual));
            } else {
                JOptionPane.showMessageDialog(null, "Atenção: Este já é o último registro da lista!", "Navegação", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Ação Voltar
        btnVoltar.addActionListener(e -> {
            dispose();
            new MenuPrincipal().setVisible(true);
        });

        // Ação Salvar
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(txtMatricula.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Busque um aluno primeiro antes de alterar!");
                    return;
                }

                btnSalvar.setEnabled(false);
                JDialog dialogProgresso = new JDialog();
                dialogProgresso.setTitle("Atualizando Dados...");
                dialogProgresso.setSize(300, 100);
                dialogProgresso.setLocationRelativeTo(null);
                dialogProgresso.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

                JProgressBar barraProgresso = new JProgressBar(0, 100);
                barraProgresso.setStringPainted(true);
                dialogProgresso.add(barraProgresso, BorderLayout.CENTER);
                dialogProgresso.setVisible(true);

                Thread threadUpdate = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            barraProgresso.setValue(20);
                            barraProgresso.setString("Validando alterações...");
                            Thread.sleep(600);
                                // Prepara as atualizações
                                alunoCarregado.setNome(txtNome.getText());
                                alunoCarregado.setCpf(txtCpf.getText());
                                alunoCarregado.setTelefone(txtTelefone.getText());
                                alunoCarregado.setCidade(txtCidade.getText());
                                alunoCarregado.setEstado(txtEstado.getText());
                                alunoCarregado.setPeriodo(Integer.parseInt(txtPeriodo.getText()));
                                alunoCarregado.setTurma((main.Turma) cbTurma.getSelectedItem());
                                
                                try {
                                    alunoCarregado.setNascimento(new SimpleDateFormat("dd/MM/yyyy").parse(txtDataNasc.getText()));
                                } catch (Exception parseEx) {
            LogErro.registrar(CodigoErro.ERRO_SISTEMA, parseEx);
                                    // Continua com a data anterior
                                }

                                barraProgresso.setValue(60);
                                barraProgresso.setString("Atualizando registro no banco...");
                                
                                AlunoDAO daoUpdate = new AlunoDAO();
                                daoUpdate.alterar(null, alunoCarregado);

                                Thread.sleep(500);

                                barraProgresso.setValue(100);
                                barraProgresso.setString("Finalizado!");
                                Thread.sleep(400);

                                dialogProgresso.dispose();
                                JOptionPane.showMessageDialog(null, "Dados do aluno alterados com sucesso!");

                                dispose();
                                new MenuPrincipal().setVisible(true);

                        } catch (Exception ex) {
            LogErro.registrar(CodigoErro.ERRO_SISTEMA, ex);
                            dialogProgresso.dispose();
                            btnSalvar.setEnabled(true);
                            JOptionPane.showMessageDialog(null, "Erro na alteração: " + ex.getMessage());
                        }
                    }
                });
                threadUpdate.start();
            }
        });

        // Preenche campos se veio de outra tela
        if (indiceAtual != -1 && listaAlunos != null) {
            preencherCampos(listaAlunos.get(indiceAtual));
        }
    }

    private void preencherCampos(Aluno aluno) {
        alunoCarregado = aluno;
        txtBuscaMatricula.setText(aluno.getMatricula());
        txtNome.setText(aluno.getNome());
        txtCpf.setText(aluno.getCpf());
        if (aluno.getNascimento() != null) {
            txtDataNasc.setText(new SimpleDateFormat("dd/MM/yyyy").format(aluno.getNascimento()));
        } else {
            txtDataNasc.setText("");
        }
        txtTelefone.setText(aluno.getTelefone());
        txtCidade.setText(aluno.getCidade());
        txtEstado.setText(aluno.getEstado());
        txtMatricula.setText(aluno.getMatricula());
        txtPeriodo.setText(String.valueOf(aluno.getPeriodo()));

        if (aluno.getTurma() != null) {
            for (int i = 0; i < cbTurma.getItemCount(); i++) {
                main.Turma t = cbTurma.getItemAt(i);
                if (t.getCodigo().equals(aluno.getTurma().getCodigo())) {
                    cbTurma.setSelectedIndex(i);
                    break;
                }
            }
        }

        // Preenche as notas
        double[] notas = aluno.getNotas();
        for (int i = 0; i < 10; i++) {
            if (notas != null && notas[i] > 0) {
                txtNotas[i].setText(String.format("%.1f", notas[i]));
            } else {
                txtNotas[i].setText("-");
            }
        }
    }

    // Método auxiliar de criação de campos
    private JTextField criarCampo(JPanel painel, String textoLabel, Font fonte) {
        return criarCampoBase(painel, textoLabel, fonte, false);
    }

    // Método auxiliar para campos com ícone de chave
    private JTextField criarCampoComChave(JPanel painel, String textoLabel, Font fonte) {
        return criarCampoBase(painel, textoLabel, fonte, true);
    }

    private JTextField criarCampoBase(JPanel painel, String textoLabel, Font fonte, boolean temChave) {
        JLabel label = new JLabel(textoLabel);
        label.setFont(fonte);
        label.setHorizontalAlignment(SwingConstants.RIGHT); // Configura alinhamento à direita
        
        if (temChave) {
            try {
                ImageIcon iconeOriginal = new ImageIcon(getClass().getResource("/imagens/icone_chave_black.png"));
                Image imgRedimensionada = iconeOriginal.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(imgRedimensionada));
            } catch (Exception ex) {
            LogErro.registrar(CodigoErro.ERRO_SISTEMA, ex);
                System.out.println("Aviso: ícone da chave não encontrado.");
            }
        }

        JTextField txtField = new JTextField();
        txtField.setFont(fonte);
        txtField.setPreferredSize(new Dimension(200, 25));

        painel.add(label);
        painel.add(txtField);
        return txtField;
    }
}
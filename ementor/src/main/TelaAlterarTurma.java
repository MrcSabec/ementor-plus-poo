package main;

import dao.TurmaDAO;
import dao.AlunoDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TelaAlterarTurma extends JFrame {
    private JTextField txtCodigoBusca;
    private JTextField txtCodigo;
    private JTextField txtNome;

    private List<Turma> listaTurmas;
    private int indiceAtual = -1;

    // Elementos da tabela de alunos
    private JTable tabelaAlunos;
    private DefaultTableModel modeloTabelaAlunos;

    public TelaAlterarTurma() {
        this("");
    }

    public TelaAlterarTurma(String codigoInicial) {
        setTitle("eMentor-Plus - Alterar Turma");
        setSize(600, 650); // Aumentado para comportar a tabela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ==========================================
        // TOPO: LOGO E BUSCA
        // ==========================================
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        try {
            ImageIcon iconeLogo = new ImageIcon(getClass().getResource("/imagens/logo.png"));
            Image imgLogo = iconeLogo.getImage().getScaledInstance(182, 100, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(imgLogo));
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
            painelTopo.add(lblLogo, BorderLayout.NORTH);
        } catch (Exception ex) {
            System.out.println("Erro ao carregar a logo.");
        }

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblBusca = new JLabel("Buscar por Código:");
        lblBusca.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtCodigoBusca = new JTextField(15);
        txtCodigoBusca.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(70, 130, 180));
        btnBuscar.setForeground(Color.WHITE);

        try {
            ImageIcon iconeLupa = new ImageIcon(getClass().getResource("/imagens/icone_lupa.png"));
            Image imgLupa = iconeLupa.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            btnBuscar.setIcon(new ImageIcon(imgLupa));
        } catch (Exception ex) {
            System.out.println("Ícone lupa não encontrado.");
        }

        painelBusca.add(lblBusca);
        painelBusca.add(txtCodigoBusca);
        painelBusca.add(btnBuscar);
        painelTopo.add(painelBusca, BorderLayout.SOUTH);

        add(painelTopo, BorderLayout.NORTH);

        // ==========================================
        // CENTRO: FORMULÁRIO, NAVEGAÇÃO E TABELA
        // ==========================================
        JPanel painelCentral = new JPanel(new BorderLayout());
        
        JPanel painelNavegacao = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnAnterior = new JButton("<< Anterior");
        JButton btnProximo = new JButton("Próximo >>");
        
        btnAnterior.setBackground(new Color(230, 230, 230));
        btnProximo.setBackground(new Color(230, 230, 230));
        
        painelNavegacao.add(btnAnterior);
        painelNavegacao.add(btnProximo);
        
        painelCentral.add(painelNavegacao, BorderLayout.NORTH);

        JPanel painelMeio = new JPanel(new BorderLayout());

        // Formulário Superior
        JPanel painelFormularioBox = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel painelFormulario = new JPanel(new GridLayout(2, 2, 10, 10));
        Font fontePadrao = new Font("Segoe UI", Font.PLAIN, 18);

        txtCodigo = criarCampo(painelFormulario, "Código da Turma:", fontePadrao);
        txtCodigo.setEditable(false); // Chave Primária bloqueada
        txtCodigo.setBackground(new Color(240, 240, 240));

        txtNome = criarCampo(painelFormulario, "Nome da Turma:", fontePadrao);

        painelFormularioBox.add(painelFormulario);
        painelMeio.add(painelFormularioBox, BorderLayout.NORTH);
        
        // Tabela Inferior
        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBorder(BorderFactory.createTitledBorder("Alunos Vinculados"));
        
        String[] colunas = {"Matrícula", "Nome do Aluno"};
        modeloTabelaAlunos = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaAlunos = new JTable(modeloTabelaAlunos);
        tabelaAlunos.setRowHeight(25);
        tabelaAlunos.getTableHeader().setBackground(new Color(230, 230, 230));
        tabelaAlunos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scrollTabela = new JScrollPane(tabelaAlunos);
        scrollTabela.setPreferredSize(new Dimension(550, 150));
        
        // Botão Desvincular ao lado ou abaixo da tabela
        JPanel painelAcaoTabela = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnDesvincular = new JButton("Desvincular Aluno");
        btnDesvincular.setBackground(new Color(220, 53, 69)); // Vermelho
        btnDesvincular.setForeground(Color.WHITE);
        try {
            ImageIcon iconDesvincular = new ImageIcon(getClass().getResource("/imagens/icone_excluir.png"));
            Image imgDesvincular = iconDesvincular.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            btnDesvincular.setIcon(new ImageIcon(imgDesvincular));
        } catch (Exception ex) {
            System.out.println("Ícone excluir não encontrado.");
        }
        painelAcaoTabela.add(btnDesvincular);

        painelTabela.add(scrollTabela, BorderLayout.CENTER);
        painelTabela.add(painelAcaoTabela, BorderLayout.SOUTH);
        
        painelMeio.add(painelTabela, BorderLayout.CENTER);

        painelCentral.add(painelMeio, BorderLayout.CENTER);
        add(painelCentral, BorderLayout.CENTER);

        // ==========================================
        // SUL: BOTÕES DE AÇÃO PRINCIPAIS
        // ==========================================
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(new Color(240, 248, 255));

        JButton btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.setBackground(new Color(0, 200, 100));
        btnSalvar.setForeground(Color.WHITE);

        try {
            ImageIcon iconVoltar = new ImageIcon(getClass().getResource("/imagens/icone_voltar_black.png"));
            Image imgVoltar = iconVoltar.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            btnVoltar.setIcon(new ImageIcon(imgVoltar));

            ImageIcon iconSalvar = new ImageIcon(getClass().getResource("/imagens/icone_salvar.png"));
            Image imgSalvar = iconSalvar.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            btnSalvar.setIcon(new ImageIcon(imgSalvar));
        } catch (Exception ex) {
            System.out.println("Erro ao carregar ícones do rodapé.");
        }

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnSalvar);
        add(painelBotoes, BorderLayout.SOUTH);

        carregarListaTurmas();

        // Se veio código inicial da tela de lista, buscar logo de cara
        if (codigoInicial != null && !codigoInicial.isEmpty()) {
            txtCodigoBusca.setText(codigoInicial);
            buscarEPreencher(codigoInicial);
        }

        // ==========================================
        // AÇÕES DOS BOTÕES
        // ==========================================
        btnBuscar.addActionListener(e -> buscarEPreencher(txtCodigoBusca.getText()));

        btnAnterior.addActionListener(e -> {
            if (listaTurmas != null && !listaTurmas.isEmpty() && indiceAtual > 0) {
                indiceAtual--;
                preencherFormulario(listaTurmas.get(indiceAtual));
            }
        });

        btnProximo.addActionListener(e -> {
            if (listaTurmas != null && !listaTurmas.isEmpty() && indiceAtual < listaTurmas.size() - 1) {
                indiceAtual++;
                preencherFormulario(listaTurmas.get(indiceAtual));
            }
        });

        btnVoltar.addActionListener(e -> {
            dispose();
            new MenuPrincipal().setVisible(true);
        });

        // Ação para desvincular o aluno da turma
        btnDesvincular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabelaAlunos.getSelectedRow();
                
                if (linhaSelecionada == -1) {
                    JOptionPane.showMessageDialog(null, "Selecione um aluno na tabela para desvincular.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                String matricula = (String) tabelaAlunos.getValueAt(linhaSelecionada, 0);
                String nome = (String) tabelaAlunos.getValueAt(linhaSelecionada, 1);
                
                int confirmacao = JOptionPane.showConfirmDialog(null, 
                        "Tem certeza que deseja desvincular o aluno(a) " + nome + " desta turma?", 
                        "Confirmar Desvinculação", JOptionPane.YES_NO_OPTION);
                
                if (confirmacao == JOptionPane.YES_OPTION) {
                    try {
                        AlunoDAO alunoDao = new AlunoDAO();
                        alunoDao.desvincularTurma(matricula);
                        JOptionPane.showMessageDialog(null, "Aluno desvinculado com sucesso!");
                        
                        // Atualiza a tabela na interface
                        carregarAlunosNaTabela(txtCodigo.getText());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao desvincular aluno: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                        registrarErroLog("Desvincular: " + ex.getMessage());
                    }
                }
            }
        });

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigo = txtCodigo.getText();
                String nome = txtNome.getText();

                if (codigo.isEmpty() || nome.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Busque uma turma e preencha o nome!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                btnSalvar.setEnabled(false);

                JDialog dialogProgresso = new JDialog();
                dialogProgresso.setTitle("Atualizando...");
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
                            barraProgresso.setValue(30);
                            barraProgresso.setString("Processando atualização...");
                            Thread.sleep(500);

                            Turma turmaAtualizada = new Turma(codigo, nome);
                            TurmaDAO turmaDao = new TurmaDAO();
                            turmaDao.alterar(turmaAtualizada);

                            barraProgresso.setValue(100);
                            barraProgresso.setString("Finalizado!");
                            Thread.sleep(500);

                            dialogProgresso.dispose();
                            JOptionPane.showMessageDialog(null, "Turma atualizada com sucesso!");

                            dispose();
                            new TelaListaTurma().setVisible(true); // Redireciona para a listagem
                        } catch (Exception ex) {
                            dialogProgresso.dispose();
                            btnSalvar.setEnabled(true);
                            JOptionPane.showMessageDialog(null, "Erro ao atualizar turma: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                            registrarErroLog(ex.getMessage());
                        }
                    }
                });
                threadSalvamento.start();
            }
        });
    }

    private void carregarListaTurmas() {
        try {
            TurmaDAO dao = new TurmaDAO();
            listaTurmas = dao.listarTurmas();
        } catch (Exception e) {
            System.out.println("Erro ao carregar lista para navegação: " + e.getMessage());
        }
    }

    private void buscarEPreencher(String codigo) {
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o código da turma para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            TurmaDAO dao = new TurmaDAO();
            Turma turma = dao.buscaSimples(codigo);

            if (turma != null) {
                preencherFormulario(turma);
                atualizarIndice(codigo);
            } else {
                JOptionPane.showMessageDialog(this, "Turma não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);
                limparFormulario();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro na busca: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherFormulario(Turma turma) {
        txtCodigo.setText(turma.getCodigo());
        txtNome.setText(turma.getNome());
        txtCodigoBusca.setText(turma.getCodigo()); // Sincroniza a busca
        
        // Atualiza a tabela de alunos
        carregarAlunosNaTabela(turma.getCodigo());
    }

    private void carregarAlunosNaTabela(String codigoTurma) {
        modeloTabelaAlunos.setRowCount(0); // Limpa a tabela
        try {
            AlunoDAO alunoDAO = new AlunoDAO();
            List<Aluno> alunos = alunoDAO.listarPorTurma(codigoTurma);
            
            for (Aluno a : alunos) {
                modeloTabelaAlunos.addRow(new Object[]{
                    a.getMatricula(),
                    a.getNome()
                });
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar alunos na tabela: " + e.getMessage());
        }
    }

    private void limparFormulario() {
        txtCodigo.setText("");
        txtNome.setText("");
        modeloTabelaAlunos.setRowCount(0); // Limpa tabela
        indiceAtual = -1;
    }

    private void atualizarIndice(String codigo) {
        if (listaTurmas != null) {
            for (int i = 0; i < listaTurmas.size(); i++) {
                if (listaTurmas.get(i).getCodigo().equals(codigo)) {
                    indiceAtual = i;
                    break;
                }
            }
        }
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
            writer.write("[" + dataFormatada + "] Erro na TelaAlterarTurma: " + mensagemErro + "\n");
        } catch (IOException e) {
            System.out.println("Erro ao tentar gravar no arquivo de log: " + e.getMessage());
        }
    }
}

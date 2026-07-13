package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import dao.EgressoDAO;
import dao.TurmaDAO;

public class TelaAlterarEgresso extends JFrame {

    private JTextField txtBuscaMatricula, txtNome, txtCpf, txtDataNasc, txtTelefone, txtCidade, txtEstado, txtMatricula, txtPeriodo;
    private JTextField txtProfissao, txtSalario, txtCursoAnterior, txtCursoAtual;
    private JTextField[] txtNotas = new JTextField[10];
    private JComboBox<main.Turma> cbTurma;
    private Egresso egressoCarregado;
    private java.util.List<Egresso> listaEgressos;
    private int indiceAtual = -1;

    public TelaAlterarEgresso() {
        this("");
    }

    public TelaAlterarEgresso(String matriculaInicial) {
        setTitle("eMentor-Plus - Alterar Dados do Egresso");
        setSize(750, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        try {
            listaEgressos = new EgressoDAO().listarEgressos();
        } catch (Exception ex) {
            System.out.println("Erro ao carregar lista de egressos: " + ex.getMessage());
        }

        // Componentes do painel superior
        JPanel painelTopo = new JPanel(new BorderLayout());

        try {
            ImageIcon iconeLogo = new ImageIcon(getClass().getResource("/imagens/logo.png"));
            Image imgLogo = iconeLogo.getImage().getScaledInstance(182, 100, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(imgLogo));
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
            painelTopo.add(lblLogo, BorderLayout.NORTH);
        } catch (Exception ex) {
            System.out.println("Aviso: Logo não encontrada.");
        }

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBusca.setBorder(BorderFactory.createTitledBorder("Buscar Registro"));

        JLabel lblBusca = new JLabel("Digite a Matrícula:");
        lblBusca.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtBuscaMatricula = new JTextField(15);
        txtBuscaMatricula.setText(matriculaInicial);
        txtBuscaMatricula.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        if (!matriculaInicial.isEmpty() && listaEgressos != null) {
            for (int i = 0; i < listaEgressos.size(); i++) {
                if (listaEgressos.get(i).getMatricula().equals(matriculaInicial)) {
                    indiceAtual = i;
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
        JPanel painelFormulario = new JPanel(new GridLayout(13, 2, 10, 10));
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        Font fontePadrao = new Font("Segoe UI", Font.PLAIN, 14);

        txtNome = criarCampo(painelFormulario, "Nome Completo:", fontePadrao);
        txtCpf = criarCampoComChave(painelFormulario, "CPF:", fontePadrao);
        txtCpf.setEditable(false);
        txtDataNasc = criarCampo(painelFormulario, "Data de Nascimento:", fontePadrao);
        txtTelefone = criarCampo(painelFormulario, "Telefone:", fontePadrao);
        txtCidade = criarCampo(painelFormulario, "Cidade:", fontePadrao);
        txtEstado = criarCampo(painelFormulario, "Estado:", fontePadrao);
        txtMatricula = criarCampoComChave(painelFormulario, "Matrícula:", fontePadrao);
        txtMatricula.setEditable(false);
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

        txtProfissao = criarCampo(painelFormulario, "Profissão Atual:", fontePadrao);
        txtSalario = criarCampo(painelFormulario, "Faixa Salarial (R$):", fontePadrao);
        txtCursoAnterior = criarCampo(painelFormulario, "Curso Anterior:", fontePadrao);
        txtCursoAtual = criarCampo(painelFormulario, "Curso Atual:", fontePadrao);

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
            System.out.println("Ícone de notas não encontrado.");
        }
        painelAcaoNotas.add(btnAtribuirNotas);

        painelNotasContainer.add(painelNotas, BorderLayout.CENTER);
        painelNotasContainer.add(painelAcaoNotas, BorderLayout.SOUTH);

        painelCentroGlobal.add(painelNotasContainer, BorderLayout.SOUTH);

        painelCentralizador.add(painelCentroGlobal);

        JScrollPane scrollPane = new JScrollPane(painelCentralizador);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Componentes do painel inferior
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));

        JButton btnAnterior = new JButton("<< Anterior");
        btnAnterior.setBackground(new Color(240, 248, 255));

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(new Color(70, 130, 180));
        btnVoltar.setForeground(Color.WHITE);

        JButton btnSalvar = new JButton("Salvar Egresso");
        btnSalvar.setBackground(new Color(0, 200, 100));
        btnSalvar.setForeground(Color.WHITE);

        JButton btnProximo = new JButton("Próximo >>");
        btnProximo.setBackground(new Color(240, 248, 255));

        try {
            ImageIcon iconVoltar = new ImageIcon(getClass().getResource("/imagens/icone_voltar.png"));
            Image imgVoltar = iconVoltar.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            if(iconVoltar != null) btnVoltar.setIcon(new ImageIcon(imgVoltar));

            ImageIcon iconSalvar = new ImageIcon(getClass().getResource("/imagens/icone_salvar.png"));
            Image imgSalvar = iconSalvar.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            if(iconSalvar != null) btnSalvar.setIcon(new ImageIcon(imgSalvar));
        } catch (Exception ex) {
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
                JOptionPane.showMessageDialog(null, "Busque um egresso primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Abre a tela de notas passando a matrícula
            TelaNotasAluno telaNotas = new TelaNotasAluno(matricula);
            telaNotas.setVisible(true);
            dispose();
        });

        btnBuscar.addActionListener(e -> {
            String matBusca = txtBuscaMatricula.getText();
            if(matBusca.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Digite uma matrícula para buscar!");
                return;
            }
            if (listaEgressos != null) {
                for (int i = 0; i < listaEgressos.size(); i++) {
                    if (listaEgressos.get(i).getMatricula().equals(matBusca)) {
                        indiceAtual = i;
                        preencherCampos(listaEgressos.get(i));
                        JOptionPane.showMessageDialog(null, "Dados recuperados com sucesso!");
                        return;
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Egresso não encontrado no banco de dados.");
        });

        btnAnterior.addActionListener(e -> {
            if (listaEgressos == null || listaEgressos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhum dado carregado. Realize uma busca primeiro!");
                return;
            }
            if (indiceAtual > 0) {
                indiceAtual--;
                preencherCampos(listaEgressos.get(indiceAtual));
            } else {
                JOptionPane.showMessageDialog(null, "Atenção: Este já é o primeiro registro da lista!", "Navegação", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnProximo.addActionListener(e -> {
            if (listaEgressos == null || listaEgressos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhum dado carregado. Realize uma busca primeiro!");
                return;
            }
            if (indiceAtual < listaEgressos.size() - 1) {
                indiceAtual++;
                preencherCampos(listaEgressos.get(indiceAtual));
            } else {
                JOptionPane.showMessageDialog(null, "Atenção: Este já é o último registro da lista!", "Navegação", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnVoltar.addActionListener(e -> {
            dispose();
            new MenuPrincipal().setVisible(true);
        });

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(txtMatricula.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Busque um egresso primeiro antes de alterar!");
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
                                
                                egressoCarregado.setNome(txtNome.getText());
                                egressoCarregado.setCpf(txtCpf.getText());
                                egressoCarregado.setTelefone(txtTelefone.getText());
                                egressoCarregado.setCidade(txtCidade.getText());
                                egressoCarregado.setEstado(txtEstado.getText());
                                egressoCarregado.setPeriodo(Integer.parseInt(txtPeriodo.getText()));
                                egressoCarregado.setTurma((main.Turma) cbTurma.getSelectedItem());
                                
                                egressoCarregado.setProfissaoAtual(txtProfissao.getText());
                                egressoCarregado.setFaixaSalarial(Double.parseDouble(txtSalario.getText().replace(",", ".")));
                                egressoCarregado.setCursoAnterior(txtCursoAnterior.getText());
                                egressoCarregado.setCursoAtual(txtCursoAtual.getText());
                                
                                try {
                                    egressoCarregado.setNascimento(new SimpleDateFormat("dd/MM/yyyy").parse(txtDataNasc.getText()));
                                } catch (Exception parseEx) {
                                }

                                barraProgresso.setValue(60);
                                barraProgresso.setString("Atualizando registro no banco...");
                                
                                EgressoDAO daoUpdate = new EgressoDAO();
                                daoUpdate.inserir(egressoCarregado);

                                Thread.sleep(500);

                                barraProgresso.setValue(100);
                                barraProgresso.setString("Finalizado!");
                                Thread.sleep(400);

                                dialogProgresso.dispose();
                                JOptionPane.showMessageDialog(null, "Dados do egresso alterados com sucesso!");

                                dispose();
                                new MenuPrincipal().setVisible(true);

                        } catch (Exception ex) {
                            dialogProgresso.dispose();
                            btnSalvar.setEnabled(true);
                            JOptionPane.showMessageDialog(null, "Erro na alteração: " + ex.getMessage());
                        }
                    }
                });
                threadUpdate.start();
            }
        });

        if (indiceAtual != -1 && listaEgressos != null) {
            preencherCampos(listaEgressos.get(indiceAtual));
        }
    }

    private void preencherCampos(Egresso egresso) {
        egressoCarregado = egresso;
        txtBuscaMatricula.setText(egresso.getMatricula());
        txtNome.setText(egresso.getNome());
        txtCpf.setText(egresso.getCpf());
        if (egresso.getNascimento() != null) {
            txtDataNasc.setText(new SimpleDateFormat("dd/MM/yyyy").format(egresso.getNascimento()));
        } else {
            txtDataNasc.setText("");
        }
        txtTelefone.setText(egresso.getTelefone());
        txtCidade.setText(egresso.getCidade());
        txtEstado.setText(egresso.getEstado());
        txtMatricula.setText(egresso.getMatricula());
        txtPeriodo.setText(String.valueOf(egresso.getPeriodo()));

        if (egresso.getTurma() != null) {
            for (int i = 0; i < cbTurma.getItemCount(); i++) {
                main.Turma t = cbTurma.getItemAt(i);
                if (t.getCodigo().equals(egresso.getTurma().getCodigo())) {
                    cbTurma.setSelectedIndex(i);
                    break;
                }
            }
        }
        
        // Preenche as notas
        double[] notas = egresso.getNotas();
        for (int i = 0; i < 10; i++) {
            if (notas != null && notas[i] > 0) {
                txtNotas[i].setText(String.format("%.1f", notas[i]));
            } else {
                txtNotas[i].setText("-");
            }
        }
        
        txtProfissao.setText(egresso.getProfissaoAtual());
        txtSalario.setText(String.valueOf(egresso.getFaixaSalarial()));
        txtCursoAnterior.setText(egresso.getCursoAnterior());
        txtCursoAtual.setText(egresso.getCursoAtual());
    }

    private JTextField criarCampo(JPanel painel, String textoLabel, Font fonte) {
        return criarCampoBase(painel, textoLabel, fonte, false);
    }

    private JTextField criarCampoComChave(JPanel painel, String textoLabel, Font fonte) {
        return criarCampoBase(painel, textoLabel, fonte, true);
    }

    private JTextField criarCampoBase(JPanel painel, String textoLabel, Font fonte, boolean temChave) {
        JLabel label = new JLabel(textoLabel);
        label.setFont(fonte);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        
        if (temChave) {
            try {
                ImageIcon iconeOriginal = new ImageIcon(getClass().getResource("/imagens/icone_chave_black.png"));
                Image imgRedimensionada = iconeOriginal.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(imgRedimensionada));
            } catch (Exception ex) {
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

package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import dao.ProfessorDAO;

public class TelaAlterarProfessor extends JFrame {

    private JTextField txtBuscaCpf, txtNome, txtCpf, txtDataNasc, txtTelefone, txtRua, txtBairro, txtCidade, txtEstado;
    private JTextField txtDataAdmissao, txtSalarioBase, txtAdicionalChefia, txtAdicionalCoordenacao;
    private JComboBox<String> cbChefia, cbCoordenacao;
    
    private Professor professorCarregado;
    private java.util.List<Professor> listaProfessores;
    private int indiceAtual = -1;

    public TelaAlterarProfessor() {
        this(""); 
    }

    public TelaAlterarProfessor(String cpfInicial) {
        setTitle("eMentor-Plus - Alterar Dados do Professor");
        setSize(750, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        try {
            listaProfessores = new ProfessorDAO().listarprofessors();
        } catch (Exception ex) {
            System.out.println("Erro ao carregar lista de professores: " + ex.getMessage());
        }

        // ==========================================
        // Componentes do painel superior
        // ==========================================
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

        JLabel lblBusca = new JLabel("Digite o CPF:");
        lblBusca.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtBuscaCpf = new JTextField(15);
        txtBuscaCpf.setText(cpfInicial);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(70, 130, 180));
        btnBuscar.setForeground(Color.WHITE);

        painelBusca.add(lblBusca);
        painelBusca.add(txtBuscaCpf);
        painelBusca.add(btnBuscar);

        painelTopo.add(painelBusca, BorderLayout.CENTER);
        add(painelTopo, BorderLayout.NORTH);

        // ==========================================
        // Componentes do painel central
        // ==========================================
        JPanel painelCentralizador = new JPanel(new GridBagLayout());
        JPanel painelFormulario = new JPanel(new GridLayout(15, 2, 10, 8));
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        Font fontePadrao = new Font("Segoe UI", Font.PLAIN, 14);

        txtNome = criarCampo(painelFormulario, "Nome Completo:", fontePadrao);
        txtCpf = criarCampoComChave(painelFormulario, "CPF:", fontePadrao);
        txtCpf.setEditable(false); // CPF é chave primária
        txtDataNasc = criarCampo(painelFormulario, "Data de Nascimento:", fontePadrao);
        txtTelefone = criarCampo(painelFormulario, "Telefone:", fontePadrao);
        txtRua = criarCampo(painelFormulario, "Rua:", fontePadrao);
        txtBairro = criarCampo(painelFormulario, "Bairro:", fontePadrao);
        txtCidade = criarCampo(painelFormulario, "Cidade:", fontePadrao);
        txtEstado = criarCampo(painelFormulario, "Estado:", fontePadrao);
        
        txtDataAdmissao = criarCampo(painelFormulario, "Data de Admissão:", fontePadrao);

        // Caixas de Seleção para Cargos
        JLabel lblChefia = new JLabel("Cargo de Chefia?");
        lblChefia.setFont(fontePadrao);
        lblChefia.setHorizontalAlignment(SwingConstants.RIGHT);
        cbChefia = new JComboBox<>(new String[]{"Não", "Sim"});
        painelFormulario.add(lblChefia);
        painelFormulario.add(cbChefia);

        JLabel lblCoord = new JLabel("Cargo de Coordenação?");
        lblCoord.setFont(fontePadrao);
        lblCoord.setHorizontalAlignment(SwingConstants.RIGHT);
        cbCoordenacao = new JComboBox<>(new String[]{"Não", "Sim"});
        painelFormulario.add(lblCoord);
        painelFormulario.add(cbCoordenacao);

        // Campos para Salário
        txtSalarioBase = criarCampo(painelFormulario, "Salário Base (R$):", fontePadrao);
        txtAdicionalChefia = criarCampo(painelFormulario, "Adicional Chefia (R$):", fontePadrao);
        txtAdicionalCoordenacao = criarCampo(painelFormulario, "Adicional Coordenação (R$):", fontePadrao);

        // Lógica de desativar os adicionais se o respectivo combo for "Não"
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
        // Componentes do painel inferior
        // ==========================================
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));

        JButton btnAnterior = new JButton("<< Anterior");
        btnAnterior.setBackground(new Color(240, 248, 255));
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(new Color(240, 248, 255));
        JButton btnProximo = new JButton("Próximo >>");
        btnProximo.setBackground(new Color(240, 248, 255));

        JButton btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.setBackground(new Color(0, 200, 100)); // Verde
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

        painelBotoes.add(btnAnterior);
        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnProximo);
        add(painelBotoes, BorderLayout.SOUTH);

        // ==========================================
        // EVENTOS
        // ==========================================
        btnVoltar.addActionListener(e -> {
            dispose();
            new MenuPrincipal().setVisible(true);
        });

        btnBuscar.addActionListener(e -> buscarProfessor());
        btnAnterior.addActionListener(e -> navegar(-1));
        btnProximo.addActionListener(e -> navegar(1));

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (professorCarregado == null) {
                    JOptionPane.showMessageDialog(null, "Busque um professor antes de salvar!");
                    return;
                }

                try {
                    professorCarregado.setNome(txtNome.getText());
                    professorCarregado.setTelefone(txtTelefone.getText());
                    professorCarregado.setRua(txtRua.getText());
                    professorCarregado.setBairro(txtBairro.getText());
                    professorCarregado.setCidade(txtCidade.getText());
                    professorCarregado.setEstado(txtEstado.getText());

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    professorCarregado.setNascimento(sdf.parse(txtDataNasc.getText()));
                    professorCarregado.setDataAdmissao(sdf.parse(txtDataAdmissao.getText()));
                    
                    boolean isChefia = cbChefia.getSelectedItem().toString().equals("Sim");
                    boolean isCoord = cbCoordenacao.getSelectedItem().toString().equals("Sim");
                    
                    professorCarregado.setCargoChefia(isChefia);
                    professorCarregado.setCargoCoordenacao(isCoord);

                    // Recálculo do Salário exigido
                    double salBase = 0;
                    if(!txtSalarioBase.getText().isEmpty()) {
                        salBase = Double.parseDouble(txtSalarioBase.getText().replace(",", "."));
                    }
                    
                    double adicChefia = isChefia ? Double.parseDouble(txtAdicionalChefia.getText().replace(",", ".")) : 0.0;
                    double adicCoord = isCoord ? Double.parseDouble(txtAdicionalCoordenacao.getText().replace(",", ".")) : 0.0;

                    double salarioBruto = salBase + adicChefia + adicCoord;
                    professorCarregado.setSalarioBruto(salarioBruto);

                    ProfessorDAO dao = new ProfessorDAO();
                    dao.alterar(professorCarregado);

                    JOptionPane.showMessageDialog(null, "Professor alterado com sucesso!\nNovo Salário Bruto: R$ " + String.format("%.2f", salarioBruto));

                    listaProfessores = dao.listarprofessors();
                    atualizarIndiceAtual();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao salvar alterações: " + ex.getMessage());
                }
            }
        });

        if (!cpfInicial.isEmpty()) {
            buscarProfessor();
        }
    }

    private JTextField criarCampo(JPanel painel, String label, Font fonte) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(fonte);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        JTextField txt = new JTextField(20);
        txt.setFont(fonte);
        painel.add(lbl);
        painel.add(txt);
        return txt;
    }

    private JTextField criarCampoComChave(JPanel painel, String label, Font fonte) {
        JTextField txt = criarCampo(painel, label, fonte);
        txt.setBackground(new Color(240, 240, 240)); 
        return txt;
    }

    private void buscarProfessor() {
        String cpf = txtBuscaCpf.getText().trim();
        if (cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite um CPF para buscar.");
            return;
        }

        try {
            ProfessorDAO dao = new ProfessorDAO();
            Professor p = dao.buscar(cpf);

            if (p != null) {
                preencherFormulario(p);
                atualizarIndiceAtual();
            } else {
                JOptionPane.showMessageDialog(this, "Professor não encontrado!");
                limparFormulario();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro na busca: " + e.getMessage());
        }
    }

    private void navegar(int direcao) {
        if (listaProfessores == null || listaProfessores.isEmpty()) return;

        indiceAtual += direcao;

        if (indiceAtual < 0) {
            indiceAtual = 0;
            JOptionPane.showMessageDialog(this, "Este é o primeiro registro.");
        } else if (indiceAtual >= listaProfessores.size()) {
            indiceAtual = listaProfessores.size() - 1;
            JOptionPane.showMessageDialog(this, "Este é o último registro.");
        }

        Professor p = listaProfessores.get(indiceAtual);
        txtBuscaCpf.setText(p.getCpf());
        preencherFormulario(p);
    }

    private void atualizarIndiceAtual() {
        if (professorCarregado != null && listaProfessores != null) {
            for (int i = 0; i < listaProfessores.size(); i++) {
                if (listaProfessores.get(i).getCpf().equals(professorCarregado.getCpf())) {
                    indiceAtual = i;
                    break;
                }
            }
        }
    }

    private void preencherFormulario(Professor p) {
        this.professorCarregado = p;
        txtNome.setText(p.getNome());
        txtCpf.setText(p.getCpf());
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtDataNasc.setText(p.getNascimento() != null ? sdf.format(p.getNascimento()) : "");
        txtDataAdmissao.setText(p.getDataAdmissao() != null ? sdf.format(p.getDataAdmissao()) : "");
        
        txtTelefone.setText(p.getTelefone());
        txtRua.setText(p.getRua());
        txtBairro.setText(p.getBairro());
        txtCidade.setText(p.getCidade());
        txtEstado.setText(p.getEstado());

        cbChefia.setSelectedItem(p.getCargoChefia() ? "Sim" : "Não");
        cbCoordenacao.setSelectedItem(p.getCargoCoordenacao() ? "Sim" : "Não");
        
        txtSalarioBase.setText(String.format("%.2f", p.getSalarioBruto()).replace(".", ","));
        txtAdicionalChefia.setText("0");
        txtAdicionalCoordenacao.setText("0");
        
        // Simula o acionamento para garantir o bloqueio/desbloqueio
        txtAdicionalChefia.setEnabled(p.getCargoChefia());
        txtAdicionalCoordenacao.setEnabled(p.getCargoCoordenacao());
    }

    private void limparFormulario() {
        this.professorCarregado = null;
        txtNome.setText("");
        txtCpf.setText("");
        txtDataNasc.setText("");
        txtDataAdmissao.setText("");
        txtTelefone.setText("");
        txtRua.setText("");
        txtBairro.setText("");
        txtCidade.setText("");
        txtEstado.setText("");
        txtSalarioBase.setText("");
        txtAdicionalChefia.setText("0");
        txtAdicionalCoordenacao.setText("0");
        cbChefia.setSelectedIndex(0);
        cbCoordenacao.setSelectedIndex(0);
    }
}

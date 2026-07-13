package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import dao.EgressoDAO;

public class TelaListaEgresso extends JFrame {

    public TelaListaEgresso() {
        setTitle("eMentor-Plus - Lista de Egressos");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ==========================================
        // Componentes do painel superior
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
        JLabel lblTitulo = new JLabel("Lista de Egressos Cadastrados", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        painelTopo.add(lblTitulo, BorderLayout.SOUTH);
        add(painelTopo, BorderLayout.NORTH);

        // ==========================================
        // Componentes do painel central
        // ==========================================
        String[] colunas = {"Matrícula", "Nome", "CPF", "Profissão", "Faixa Salarial"};

        DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabelaEgressos = new JTable(modeloTabela);
        tabelaEgressos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabelaEgressos.setRowHeight(25);
        tabelaEgressos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane painelRolagem = new JScrollPane(tabelaEgressos);
        painelRolagem.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(painelRolagem, BorderLayout.CENTER);

        // Carrega os dados dos egressos
        try {
            EgressoDAO egressoDao = new EgressoDAO();
            List<Egresso> lista = egressoDao.listarEgressos();
            for (Egresso e : lista) {
                modeloTabela.addRow(new Object[]{
                    e.getMatricula(), 
                    e.getNome(), 
                    e.getCpf(), 
                    e.getProfissaoAtual(), 
                    String.format("R$ %.2f", e.getFaixaSalarial())
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar os egressos: " + e.getMessage());
        }

        // ==========================================
        // Componentes do painel inferior
        // ==========================================
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(new Color(240, 248, 255));

        JButton btnNotas = new JButton("Atribuir Notas");
        btnNotas.setBackground(new Color(70, 130, 180));
        btnNotas.setForeground(Color.WHITE);

        JButton btnEditar = new JButton("Editar Egresso");
        btnEditar.setBackground(new Color(0, 200, 100));
        btnEditar.setForeground(Color.WHITE);

        JButton btnRemover = new JButton("Remover");
        btnRemover.setBackground(new Color(220, 53, 69)); // Vermelho
        btnRemover.setForeground(Color.WHITE);

        try {
            ImageIcon urlVoltar = new ImageIcon(getClass().getResource("/imagens/icone_voltar_black.png"));
            Image urlVoltarRedimensionada = urlVoltar.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            if(urlVoltar != null) btnVoltar.setIcon(new ImageIcon(urlVoltarRedimensionada));

            ImageIcon urlNotas = new ImageIcon(getClass().getResource("/imagens/icone_notas.png"));
            Image urlNotasRedimensionada = urlNotas.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            if(urlNotas != null) btnNotas.setIcon(new ImageIcon(urlNotasRedimensionada));

            ImageIcon urlEditar = new ImageIcon(getClass().getResource("/imagens/icone_editar.png"));
            Image urlEditarRedimensionada = urlEditar.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            if(urlEditar != null) btnEditar.setIcon(new ImageIcon(urlEditarRedimensionada));
            
            ImageIcon urlRemover = new ImageIcon(getClass().getResource("/imagens/icone_excluir.png"));
            Image urlRemoverRedimensionada = urlRemover.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            if(urlRemover != null) btnRemover.setIcon(new ImageIcon(urlRemoverRedimensionada));
        } catch (Exception ex) {
            System.out.println("Erro ao carregar ícones dos botões.");
        }

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnNotas);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnRemover);
        add(painelBotoes, BorderLayout.SOUTH);

        // ==========================================
        // AÇÕES DOS BOTÕES
        // ==========================================
        btnVoltar.addActionListener(e -> {
            dispose();
            new MenuPrincipal().setVisible(true);
        });

        btnNotas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabelaEgressos.getSelectedRow();

                if (linhaSelecionada == -1) {
                    JOptionPane.showMessageDialog(null, "Por favor, selecione um egresso na tabela primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
                } else {
                    String matriculaSelecionada = (String) tabelaEgressos.getValueAt(linhaSelecionada, 0);

                    JOptionPane.showMessageDialog(null, "Abrindo a tela de Notas para a matrícula: " + matriculaSelecionada);
                    TelaNotasAluno telaNotas = new TelaNotasAluno(matriculaSelecionada, true);
                    telaNotas.setVisible(true);
                    dispose();
                }
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabelaEgressos.getSelectedRow();

                if (linhaSelecionada == -1) {
                    JOptionPane.showMessageDialog(null, "Por favor, selecione um egresso na tabela primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
                } else {
                    String matriculaSelecionada = (String) tabelaEgressos.getValueAt(linhaSelecionada, 0);

                    TelaAlterarEgresso telaEditar = new TelaAlterarEgresso(matriculaSelecionada);
                    telaEditar.setVisible(true);
                    dispose();
                }
            }
        });

        // Ação para Remover Egresso
        btnRemover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabelaEgressos.getSelectedRow();

                if (linhaSelecionada == -1) {
                    JOptionPane.showMessageDialog(null, "Por favor, selecione um registro na tabela primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
                } else {
                    String matricula = (String) tabelaEgressos.getValueAt(linhaSelecionada, 0);
                    int confirmacao = JOptionPane.showConfirmDialog(null, 
                            "Tem certeza que deseja remover a matrícula " + matricula + "?", 
                            "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

                    if (confirmacao == JOptionPane.YES_OPTION) {
                        try {
                            dao.EgressoDAO egressoDao = new dao.EgressoDAO();
                            egressoDao.remover(matricula);
                            JOptionPane.showMessageDialog(null, "Egresso removido com sucesso!");
                            
                            // Atualiza a tela
                            dispose();
                            new TelaListaEgresso().setVisible(true);
                        } catch (Exception ex) {
                            String msg = ex.getMessage();
                            if (msg != null && (msg.contains("foreign key constraint") || msg.contains("Cannot delete or update a parent row"))) {
                                JOptionPane.showMessageDialog(null, "Não é possível remover este egresso por restrições de integridade no banco de dados.", "Erro de Integridade", JOptionPane.ERROR_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Erro ao remover egresso: " + msg, "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });
    }
}

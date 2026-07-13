package main;

import dao.TurmaDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TelaListaTurma extends JFrame {
    private JTable tabelaTurmas;
    private DefaultTableModel modeloTabela;

    public TelaListaTurma() {
        setTitle("eMentor-Plus - Listar Turmas");
        setSize(700, 500);
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

        // ==========================================
        // Componentes do painel central
        // ==========================================
        String[] colunas = {"Código da Turma", "Nome da Turma", "Quantidade de Alunos"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaTurmas = new JTable(modeloTabela);
        tabelaTurmas.setRowHeight(30);
        tabelaTurmas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabelaTurmas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabelaTurmas.getTableHeader().setBackground(new Color(230, 230, 230));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabelaTurmas.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tabelaTurmas.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(tabelaTurmas);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        carregarDados();

        // ==========================================
        // Componentes do painel inferior
        // ==========================================
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(new Color(240, 248, 255));

        JButton btnEditar = new JButton("Editar Turma");
        btnEditar.setBackground(new Color(0, 200, 100));
        btnEditar.setForeground(Color.WHITE);

        JButton btnRemover = new JButton("Remover");
        btnRemover.setBackground(new Color(220, 53, 69)); // Vermelho
        btnRemover.setForeground(Color.WHITE);

        try {
            ImageIcon urlVoltar = new ImageIcon(getClass().getResource("/imagens/icone_voltar_black.png"));
            Image urlVoltarRedimensionada = urlVoltar.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            if(urlVoltar != null) btnVoltar.setIcon(new ImageIcon(urlVoltarRedimensionada));

            ImageIcon urlEditar = new  ImageIcon(getClass().getResource("/imagens/icone_editar.png"));
            Image urlEditarRedimensionada = urlEditar.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            if(urlEditar != null) btnEditar.setIcon(new ImageIcon(urlEditarRedimensionada));
            
            ImageIcon urlRemover = new ImageIcon(getClass().getResource("/imagens/icone_excluir.png"));
            Image urlRemoverRedimensionada = urlRemover.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            if(urlRemover != null) btnRemover.setIcon(new ImageIcon(urlRemoverRedimensionada));
        } catch (Exception ex) {
            System.out.println("Erro ao carregar ícones dos botões.");
        }

        painelBotoes.add(btnVoltar);
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

        // Ação para Editar
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabelaTurmas.getSelectedRow();

                if (linhaSelecionada == -1) {
                    JOptionPane.showMessageDialog(null, "Por favor, selecione uma turma na tabela primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
                } else {
                    String codigoSelecionado = (String) tabelaTurmas.getValueAt(linhaSelecionada, 0);

                    TelaAlterarTurma telaEditar = new TelaAlterarTurma(codigoSelecionado);
                    telaEditar.setVisible(true);
                    dispose();
                }
            }
        });

        // Ação para Remover
        btnRemover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabelaTurmas.getSelectedRow();

                if (linhaSelecionada == -1) {
                    JOptionPane.showMessageDialog(null, "Por favor, selecione um registro na tabela primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
                } else {
                    String codigo = (String) tabelaTurmas.getValueAt(linhaSelecionada, 0);
                    int confirmacao = JOptionPane.showConfirmDialog(null, 
                            "Tem certeza que deseja remover a turma " + codigo + "?", 
                            "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

                    if (confirmacao == JOptionPane.YES_OPTION) {
                        try {
                            dao.TurmaDAO turmaDao = new dao.TurmaDAO();
                            turmaDao.remover(codigo);
                            JOptionPane.showMessageDialog(null, "Turma removida com sucesso!");
                            
                            // Atualiza a tela
                            dispose();
                            new TelaListaTurma().setVisible(true);
                        } catch (Exception ex) {
                            String msg = ex.getMessage();
                            if (msg != null && (msg.contains("foreign key constraint") || msg.contains("Cannot delete or update a parent row"))) {
                                JOptionPane.showMessageDialog(null, "Não é possível remover esta turma pois há alunos matriculados nela.", "Erro de Integridade", JOptionPane.ERROR_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Erro ao remover turma: " + msg, "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });
    }

    private void carregarDados() {
        try {
            TurmaDAO dao = new TurmaDAO();
            List<Turma> turmas = dao.listarTurmas();

            for (Turma t : turmas) {
                int qtd = 0;
                if(t.getAlunos() != null){
                    qtd = t.getAlunos().size();
                }
                
                modeloTabela.addRow(new Object[]{
                    t.getCodigo(), 
                    t.getNome(), 
                    qtd
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar as turmas do banco de dados: " + e.getMessage());
        }
    }
}

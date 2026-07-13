package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import dao.ProfessorDAO;

public class TelaListaProfessor extends JFrame {

    public TelaListaProfessor() {
        setTitle("eMentor-Plus - Lista de Professores");
        setSize(900, 600); // Um pouco mais largo para acomodar os campos de salário e cargos
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
        JLabel lblTitulo = new JLabel("Lista de Professores Cadastrados", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        painelTopo.add(lblTitulo, BorderLayout.SOUTH);
        add(painelTopo, BorderLayout.NORTH);

        // ==========================================
        // Componentes do painel central
        // ==========================================
        String[] colunas = {"CPF", "Nome", "Data Admissão", "Chefia?", "Coordenação?", "Salário Bruto", "Salário Líquido"};

        DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        JTable tabelaProfessores = new JTable(modeloTabela);
        tabelaProfessores.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabelaProfessores.setRowHeight(25);
        tabelaProfessores.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane painelRolagem = new JScrollPane(tabelaProfessores);
        painelRolagem.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(painelRolagem, BorderLayout.CENTER);

        // Carrega os dados dos professores a partir do banco de dados
        try {
            ProfessorDAO professorDao = new ProfessorDAO();
            java.util.List<Professor> listaProfessores = professorDao.listarprofessors();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for (Professor p : listaProfessores) {
                String chefia = p.getCargoChefia() ? "Sim" : "Não";
                String coord = p.getCargoCoordenacao() ? "Sim" : "Não";
                String admissao = p.getDataAdmissao() != null ? sdf.format(p.getDataAdmissao()) : "N/A";
                
                double bruto = p.getSalarioBruto();
                String salarioB = String.format("R$ %.2f", bruto);
                
                double inss = bruto * 0.14;
                double irpf = (bruto >= 5000.00) ? (bruto * 0.225) : 0.0;
                double liquido = bruto - inss - irpf;
                String salarioL = String.format("R$ %.2f", liquido);
                
                modeloTabela.addRow(new Object[]{
                    p.getCpf(), 
                    p.getNome(), 
                    admissao, 
                    chefia, 
                    coord,
                    salarioB,
                    salarioL
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar os professores do banco de dados: " + e.getMessage());
        }

        // ==========================================
        // Componentes do painel inferior
        // ==========================================
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(new Color(240, 248, 255));

        JButton btnEditar = new JButton("Alterar Professor");
        btnEditar.setBackground(new Color(70, 130, 180)); // Azul padrão eMentor
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

        // Ação para Editar Professor
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabelaProfessores.getSelectedRow();

                if (linhaSelecionada == -1) {
                    JOptionPane.showMessageDialog(null, "Por favor, selecione um professor na tabela primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
                } else {
                    String cpfSelecionado = (String) tabelaProfessores.getValueAt(linhaSelecionada, 0);

                    // Abre a tela de alteração mandando o CPF
                    JOptionPane.showMessageDialog(null, "Abrindo a tela de Edição para o CPF: " + cpfSelecionado);
                    TelaAlterarProfessor telaAlt = new TelaAlterarProfessor(cpfSelecionado);
                    telaAlt.setVisible(true);
                    dispose();
                }
            }
        });

        // Ação para Remover Professor
        btnRemover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabelaProfessores.getSelectedRow();

                if (linhaSelecionada == -1) {
                    JOptionPane.showMessageDialog(null, "Por favor, selecione um registro na tabela primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
                } else {
                    String cpf = (String) tabelaProfessores.getValueAt(linhaSelecionada, 0);
                    int confirmacao = JOptionPane.showConfirmDialog(null, 
                            "Tem certeza que deseja remover o professor com CPF " + cpf + "?", 
                            "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

                    if (confirmacao == JOptionPane.YES_OPTION) {
                        try {
                            dao.ProfessorDAO professorDao = new dao.ProfessorDAO();
                            professorDao.remover(cpf);
                            JOptionPane.showMessageDialog(null, "Professor removido com sucesso!");
                            
                            // Atualiza a tela
                            dispose();
                            new TelaListaProfessor().setVisible(true);
                        } catch (Exception ex) {
                            String msg = ex.getMessage();
                            if (msg != null && (msg.contains("foreign key constraint") || msg.contains("Cannot delete or update a parent row"))) {
                                JOptionPane.showMessageDialog(null, "Não é possível remover este professor por restrições de integridade no banco de dados.", "Erro de Integridade", JOptionPane.ERROR_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Erro ao remover professor: " + msg, "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });
    }
}

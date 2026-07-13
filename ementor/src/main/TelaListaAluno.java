package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaListaAluno extends JFrame {

    public TelaListaAluno() {
        setTitle("eMentor-Plus - Lista de Alunos");
        setSize(850, 600); // Define o tamanho da janela
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
        JLabel lblTitulo = new JLabel("Lista de Alunos Cadastrados", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        painelTopo.add(lblTitulo, BorderLayout.SOUTH);
        add(painelTopo, BorderLayout.NORTH);

        // ==========================================
        // CENTRO: A TABELA (JTable)
        // ==========================================
        // Definição das colunas da tabela
        String[] colunas = {"Matrícula", "Nome", "CPF", "Turma", "Período"};

        // Criação do modelo da tabela com edição desativada
        DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Desabilita a edição direta das células
            }
        };

        // Instancia a tabela com o modelo criado
        JTable tabelaAlunos = new JTable(modeloTabela);
        tabelaAlunos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabelaAlunos.setRowHeight(25); // Define a altura das linhas
        tabelaAlunos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Adiciona o painel de rolagem à tabela
        JScrollPane painelRolagem = new JScrollPane(tabelaAlunos);
        painelRolagem.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(painelRolagem, BorderLayout.CENTER);

        // Carrega os dados dos alunos a partir do banco de dados
        try {
            dao.AlunoDAO alunoDao = new dao.AlunoDAO();
            java.util.List<Aluno> listaAlunos = alunoDao.listarAlunos();
            for (Aluno a : listaAlunos) {
                String nomeTurma = (a.getTurma() != null) ? a.getTurma().getNome() : "Sem Turma";
                modeloTabela.addRow(new Object[]{
                    a.getMatricula(), 
                    a.getNome(), 
                    a.getCpf(), 
                    nomeTurma, 
                    a.getPeriodo()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar os alunos do banco de dados: " + e.getMessage());
        }

        // ==========================================
        // SUL: BOTÕES DE AÇÃO
        // ==========================================
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(new Color(240, 248, 255));

        JButton btnNotas = new JButton("Atribuir Notas");
        btnNotas.setBackground(new Color(70, 130, 180));
        btnNotas.setForeground(Color.WHITE);

        JButton btnEditar = new JButton("Editar Aluno");
        btnEditar.setBackground(new Color(0, 200, 100));
        btnEditar.setForeground(Color.WHITE);

        JButton btnRemover = new JButton("Remover");
        btnRemover.setBackground(new Color(220, 53, 69)); // Vermelho
        btnRemover.setForeground(Color.WHITE);

        // Configuração e carregamento dos ícones dos botões
        try {


            ImageIcon urlVoltar = new ImageIcon(getClass().getResource("/imagens/icone_voltar_black.png"));
            Image urlVoltarRedimensionada = urlVoltar.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            if(urlVoltar != null) btnVoltar.setIcon(new ImageIcon(urlVoltarRedimensionada));

            ImageIcon urlNotas = new ImageIcon(getClass().getResource("/imagens/icone_notas.png"));
            Image urlNotasRedimensionada = urlNotas.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            if(urlNotas != null) btnNotas.setIcon(new ImageIcon(urlNotasRedimensionada));

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

        // Ação para Atribuir Notas
        btnNotas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabelaAlunos.getSelectedRow();

                // Verifica se há uma linha selecionada
                if (linhaSelecionada == -1) {
                    JOptionPane.showMessageDialog(null, "Por favor, selecione um aluno na tabela primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
                } else {
                    // Obtém a matrícula selecionada
                    String matriculaSelecionada = (String) tabelaAlunos.getValueAt(linhaSelecionada, 0);

                    // Abre a tela de atribuição de notas
                    JOptionPane.showMessageDialog(null, "Abrindo a tela de Notas para a matrícula: " + matriculaSelecionada);
                    TelaNotasAluno telaNotas = new TelaNotasAluno(matriculaSelecionada);
                    telaNotas.setVisible(true);
                    dispose();
                }
            }
        });

        // Ação para Editar Aluno
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabelaAlunos.getSelectedRow();

                if (linhaSelecionada == -1) {
                    JOptionPane.showMessageDialog(null, "Por favor, selecione um aluno na tabela primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
                } else {
                    String matriculaSelecionada = (String) tabelaAlunos.getValueAt(linhaSelecionada, 0);

                    TelaAlterarAluno telaEditar = new TelaAlterarAluno(matriculaSelecionada);
                    telaEditar.setVisible(true);
                    dispose();
                }
            }
        });

        // Ação para Remover Aluno
        btnRemover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabelaAlunos.getSelectedRow();

                if (linhaSelecionada == -1) {
                    JOptionPane.showMessageDialog(null, "Por favor, selecione um registro na tabela primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
                } else {
                    String matricula = (String) tabelaAlunos.getValueAt(linhaSelecionada, 0);
                    int confirmacao = JOptionPane.showConfirmDialog(null, 
                            "Tem certeza que deseja remover a matrícula " + matricula + "?", 
                            "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

                    if (confirmacao == JOptionPane.YES_OPTION) {
                        try {
                            dao.AlunoDAO alunoDao = new dao.AlunoDAO();
                            alunoDao.remover(matricula);
                            JOptionPane.showMessageDialog(null, "Aluno removido com sucesso!");
                            
                            // Atualiza a tela
                            dispose();
                            new TelaListaAluno().setVisible(true);
                        } catch (Exception ex) {
                            String msg = ex.getMessage();
                            if (msg != null && (msg.contains("foreign key constraint") || msg.contains("Cannot delete or update a parent row"))) {
                                JOptionPane.showMessageDialog(null, "Não é possível remover este aluno pois ele possui notas vinculadas ou já se tornou egresso.", "Erro de Integridade", JOptionPane.ERROR_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Erro ao remover aluno: " + msg, "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });
    }
}

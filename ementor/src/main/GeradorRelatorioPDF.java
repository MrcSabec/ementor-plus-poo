package main;

import exception.LogErro;
import exception.CodigoErro;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import dao.ProfessorDAO;
import dao.TurmaDAO;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GeradorRelatorioPDF {

    public void gerarRelatorio(String caminhoArquivo) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(caminhoArquivo));
            document.open();

            // Configuração de fontes do documento
            Font fonteTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font fonteSubTitulo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font fonteNormal = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            Font fonteNegrito = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            // Geração do cabeçalho principal
            Paragraph titulo = new Paragraph("Relatório Geral - eMentor-Plus", fonteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            SimpleDateFormat formatadorData = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Paragraph dataGeracao = new Paragraph("Gerado em: " + formatadorData.format(new Date()), fonteNormal);
            dataGeracao.setAlignment(Element.ALIGN_RIGHT);
            dataGeracao.setSpacingAfter(30);
            document.add(dataGeracao);

            // Seção: Turmas, Alunos e Egressos
            Paragraph subTurmas = new Paragraph("1. Turmas e Alunos Vinculados", fonteSubTitulo);
            subTurmas.setSpacingAfter(15);
            document.add(subTurmas);

            TurmaDAO turmaDAO = new TurmaDAO();
            List<Turma> turmas = turmaDAO.listarTurmas();
            DecimalFormat df = new DecimalFormat("#,##0.00");

            if (turmas != null && !turmas.isEmpty()) {
                for (Turma turma : turmas) {
                    Paragraph pTurma = new Paragraph("Turma: " + turma.getNome() + " (Código: " + turma.getCodigo() + ")", fonteNegrito);
                    pTurma.setSpacingBefore(10);
                    pTurma.setSpacingAfter(5);
                    document.add(pTurma);

                    List<Aluno> alunos = turma.getAlunos();
                    
                    // Busca Egressos da Turma manualmente via SQL para contornar o DAO
                    java.util.List<java.util.Map<String, Object>> listaEgressos = new java.util.ArrayList<>();
                    try (java.sql.Connection conn = database.Conexao.getConnection();
                         java.sql.PreparedStatement stmt = conn.prepareStatement(
                             "SELECT a.matricula, p.nome, a.nota1, a.nota2, a.nota3, a.nota4, a.nota5, a.nota6, a.nota7, a.nota8, a.nota9, a.nota10, e.profissao_atual, e.curso_atual " +
                             "FROM egresso e " +
                             "JOIN aluno a ON e.matricula = a.matricula " +
                             "JOIN pessoa p ON a.cpf_pessoa = p.cpf " +
                             "WHERE a.codigo_turma = ? ORDER BY p.nome")) {
                        stmt.setString(1, turma.getCodigo());
                        try (java.sql.ResultSet rs = stmt.executeQuery()) {
                            while (rs.next()) {
                                java.util.Map<String, Object> map = new java.util.HashMap<>();
                                map.put("matricula", rs.getString("matricula"));
                                map.put("nome", rs.getString("nome"));
                                map.put("profissao_atual", rs.getString("profissao_atual"));
                                map.put("curso_atual", rs.getString("curso_atual"));
                                double[] n = new double[10];
                                for (int i=1; i<=10; i++) n[i-1] = rs.getDouble("nota"+i);
                                map.put("notas", n);
                                listaEgressos.add(map);
                            }
                        }
                    } catch (Exception ex) {
                        LogErro.registrar(CodigoErro.ERRO_SISTEMA, ex);
                    }

                    if ((alunos != null && !alunos.isEmpty()) || !listaEgressos.isEmpty()) {
                        PdfPTable tabelaAlunos = new PdfPTable(6);
                        tabelaAlunos.setWidthPercentage(100);
                        tabelaAlunos.setWidths(new float[]{1.5f, 2.5f, 1.2f, 2f, 3f, 1f});
                        tabelaAlunos.setSpacingBefore(5);
                        tabelaAlunos.setSpacingAfter(15);

                        // Configuração do cabeçalho da tabela de alunos
                        PdfPCell cellMatricula = new PdfPCell(new Phrase("Matrícula", fonteNegrito));
                        PdfPCell cellNome = new PdfPCell(new Phrase("Nome do Aluno", fonteNegrito));
                        PdfPCell cellEgresso = new PdfPCell(new Phrase("Egresso?", fonteNegrito));
                        PdfPCell cellAtributo = new PdfPCell(new Phrase("Profissão/Curso", fonteNegrito));
                        PdfPCell cellNotas = new PdfPCell(new Phrase("Notas (N1-N10)", fonteNegrito));
                        PdfPCell cellMedia = new PdfPCell(new Phrase("Média", fonteNegrito));
                        
                        cellMatricula.setBackgroundColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);
                        cellNome.setBackgroundColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);
                        cellEgresso.setBackgroundColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);
                        cellAtributo.setBackgroundColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);
                        cellNotas.setBackgroundColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);
                        cellMedia.setBackgroundColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);

                        tabelaAlunos.addCell(cellMatricula);
                        tabelaAlunos.addCell(cellNome);
                        tabelaAlunos.addCell(cellEgresso);
                        tabelaAlunos.addCell(cellAtributo);
                        tabelaAlunos.addCell(cellNotas);
                        tabelaAlunos.addCell(cellMedia);

                        if (alunos != null) {
                            for (Aluno aluno : alunos) {
                                tabelaAlunos.addCell(new Phrase(aluno.getMatricula(), fonteNormal));
                                tabelaAlunos.addCell(new Phrase(aluno.getNome(), fonteNormal));

                                // Celulas de Egresso e Atributo (Sempre Não para alunos normais)
                                tabelaAlunos.addCell(new Phrase("Não", fonteNormal));
                                tabelaAlunos.addCell(new Phrase("-", fonteNormal));

                                double[] notas = aluno.getNotas();
                                double soma = 0;
                                int count = 0;
                                StringBuilder notasStr = new StringBuilder();
                                if (notas != null) {
                                    for (int i = 0; i < notas.length; i++) {
                                        if (notas[i] > 0) {
                                            soma += notas[i];
                                            count++;
                                            notasStr.append(df.format(notas[i]));
                                        } else {
                                            notasStr.append("-");
                                        }
                                        if (i < notas.length - 1) notasStr.append(" | ");
                                    }
                                }
                                
                                Font fonteNotas = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
                                tabelaAlunos.addCell(new Phrase(notasStr.toString(), fonteNotas));
                                
                                String mediaStr = (count > 0) ? df.format(soma / count) : "S/ Nota";
                                tabelaAlunos.addCell(new Phrase(mediaStr, fonteNormal));
                            }
                        }

                        // Imprime os egressos
                        for (java.util.Map<String, Object> map : listaEgressos) {
                            tabelaAlunos.addCell(new Phrase((String) map.get("matricula"), fonteNormal));
                            tabelaAlunos.addCell(new Phrase((String) map.get("nome"), fonteNormal));
                            
                            tabelaAlunos.addCell(new Phrase("Sim", fonteNormal));
                            String profissao = (String) map.get("profissao_atual");
                            String curso = (String) map.get("curso_atual");
                            String excl = (profissao != null && !profissao.isEmpty()) ? profissao : curso;
                            tabelaAlunos.addCell(new Phrase(excl != null ? excl : "-", fonteNormal));
                            
                            double[] n = (double[]) map.get("notas");
                            double soma = 0;
                            int count = 0;
                            StringBuilder notasStr = new StringBuilder();
                            for (int i = 0; i < 10; i++) {
                                if (n[i] > 0) {
                                    soma += n[i];
                                    count++;
                                    notasStr.append(df.format(n[i]));
                                } else {
                                    notasStr.append("-");
                                }
                                if (i < 9) notasStr.append(" | ");
                            }
                            
                            Font fonteNotas = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
                            tabelaAlunos.addCell(new Phrase(notasStr.toString(), fonteNotas));
                            
                            String mediaStr = (count > 0) ? df.format(soma / count) : "S/ Nota";
                            tabelaAlunos.addCell(new Phrase(mediaStr, fonteNormal));
                        }
                        document.add(tabelaAlunos);
                    } else {
                        Paragraph pVazia = new Paragraph("Nenhum aluno vinculado a esta turma.", fonteNormal);
                        pVazia.setSpacingAfter(15);
                        document.add(pVazia);
                    }
                }
            } else {
                document.add(new Paragraph("Nenhuma turma cadastrada no sistema.", fonteNormal));
            }

            // Quebra de página para a próxima seção
            document.newPage();

            // Seção: Quadro de Professores
            Paragraph subProfessores = new Paragraph("2. Quadro de Professores", fonteSubTitulo);
            subProfessores.setSpacingAfter(15);
            document.add(subProfessores);

            ProfessorDAO professorDAO = new ProfessorDAO();
            List<Professor> professores = professorDAO.listarprofessors();

            if (professores != null && !professores.isEmpty()) {
                PdfPTable tabelaProf = new PdfPTable(4);
                tabelaProf.setWidthPercentage(100);
                tabelaProf.setWidths(new float[]{2f, 3f, 2f, 2f});
                tabelaProf.setSpacingBefore(10);

                PdfPCell cCpf = new PdfPCell(new Phrase("CPF", fonteNegrito));
                PdfPCell cNome = new PdfPCell(new Phrase("Nome", fonteNegrito));
                PdfPCell cBruto = new PdfPCell(new Phrase("Salário Bruto", fonteNegrito));
                PdfPCell cLiquido = new PdfPCell(new Phrase("Salário Líquido", fonteNegrito));

                cCpf.setBackgroundColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);
                cNome.setBackgroundColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);
                cBruto.setBackgroundColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);
                cLiquido.setBackgroundColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);

                tabelaProf.addCell(cCpf);
                tabelaProf.addCell(cNome);
                tabelaProf.addCell(cBruto);
                tabelaProf.addCell(cLiquido);

                for (Professor prof : professores) {
                    tabelaProf.addCell(new Phrase(prof.getCpf(), fonteNormal));
                    tabelaProf.addCell(new Phrase(prof.getNome(), fonteNormal));
                    tabelaProf.addCell(new Phrase("R$ " + df.format(prof.getSalarioBruto()), fonteNormal));
                    tabelaProf.addCell(new Phrase("R$ " + df.format(prof.getSalarioLiquido()), fonteNormal));
                }
                document.add(tabelaProf);
            } else {
                document.add(new Paragraph("Nenhum professor cadastrado.", fonteNormal));
            }

            document.close();

        } catch (DocumentException | IOException e) {
            registrarErroLog("Falha ao gerar PDF: " + e.getMessage());
            throw new RuntimeException("Erro ao gerar PDF: " + e.getMessage(), e);
        } catch (Exception e) {
            LogErro.registrar(CodigoErro.ERRO_SISTEMA, e);
            registrarErroLog("Erro inesperado na geração do relatório: " + e.getMessage());
            throw new RuntimeException("Erro inesperado: " + e.getMessage(), e);
        }
    }

    private void registrarErroLog(String mensagemErro) {
        try (FileWriter writer = new FileWriter("erros.dat", true)) {
            SimpleDateFormat dataHoraAtual = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String dataFormatada = dataHoraAtual.format(new Date());
            writer.write("[" + dataFormatada + "] Erro: " + mensagemErro + "\n");
        } catch (IOException e) {
            LogErro.registrar(CodigoErro.ERRO_SISTEMA, e);
            System.out.println("Erro Crítico: Não foi possível gravar o log de erro físico. " + e.getMessage());
        }
    }
}

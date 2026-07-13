package exception;

public enum CodigoErro {
    ERRO_CONEXAO(1001, "Erro ao conectar ao banco de dados"),
    
    ERRO_INSERIR(1002, "Erro durante inserção"),

    ERRO_ALTERAR(1003,"Erro durante alteração"),

    ERRO_REMOVER(1004,"Erro durante remoção"),

    ERRO_BUSCAR(1005,"Erro durante busca"),

    ERRO_LISTAR(1006,"Erro ao listar campos do banco de dados"),

    ERRO_LISTAR_FILTRADO(1007,"Erro ao listar campos do banco de dados com um filtro"),

    ERRO_ATUALIZAR_NOTAS(1008,"Erro ao atualizar notas de um aluno"),

    ERRO_DESVINCULAR_TURMA(1009,"Erro ao desvincular um aluno de uma turma"),

    ERRO_OBTER_ID(1010, "Erro ao obter ID"),

    ERRO_AUTENTICAR_USUARIO(1011,"Erro ao autenticar um usuario");

    private final int codigo;
    private final String descricao;

    CodigoErro(int codigo, String descricao){
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
}

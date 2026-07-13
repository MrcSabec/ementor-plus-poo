package exception;

public class DAOException extends RuntimeException {

    public DAOException(CodigoErro erro, Exception causa) {
        super(erro.getDescricao(), causa);

        LogErro.registrar(erro, causa);
    }
}
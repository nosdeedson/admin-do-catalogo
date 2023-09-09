package e3n.com.admin.catalogo.domain.exceptions;

public class NoStacktraceException extends RuntimeException{

    public NoStacktraceException(String message) {
        super(message);
    }

    public NoStacktraceException(String message, Throwable cause) {
        super(message, cause, true, false);
    }
}

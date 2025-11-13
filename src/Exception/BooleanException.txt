package Exception;

public class BooleanException extends AppException {
    private static final long serialVersionUID = 1L;

    public BooleanException() {
        super();
    }

    public BooleanException(String message) {
        super(message);
    }

    public BooleanException(String message, Throwable cause) {
        super(message, cause);
    }

    public BooleanException(Throwable cause) {
        super(cause);
    }
}
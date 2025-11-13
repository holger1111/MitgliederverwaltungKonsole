package Exception;

public class StringException extends AppException {
    private static final long serialVersionUID = 1L;

    public StringException() {
        super();
    }

    public StringException(String message) {
        super(message);
    }

    public StringException(String message, Throwable cause) {
        super(message, cause);
    }

    public StringException(Throwable cause) {
        super(cause);
    }
}
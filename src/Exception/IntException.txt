package Exception;

public class IntException extends NumericException {
    private static final long serialVersionUID = 1L;

    public IntException() {
        super();
    }

    public IntException(String message) {
        super(message);
    }

    public IntException(String message, Throwable cause) {
        super(message, cause);
    }

    public IntException(Throwable cause) {
        super(cause);
    }
}
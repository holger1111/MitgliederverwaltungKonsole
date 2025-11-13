package Exception;

public class ShortException extends NumericException {
    private static final long serialVersionUID = 1L;

    public ShortException() {
        super();
    }

    public ShortException(String message) {
        super(message);
    }

    public ShortException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShortException(Throwable cause) {
        super(cause);
    }
}
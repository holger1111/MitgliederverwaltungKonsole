package Exception;

public class LongException extends NumericException {
    private static final long serialVersionUID = 1L;

    public LongException() {
        super();
    }

    public LongException(String message) {
        super(message);
    }

    public LongException(String message, Throwable cause) {
        super(message, cause);
    }

    public LongException(Throwable cause) {
        super(cause);
    }
}
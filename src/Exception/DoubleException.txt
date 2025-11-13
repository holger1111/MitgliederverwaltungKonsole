package Exception;

public class DoubleException extends NumericException {
    private static final long serialVersionUID = 1L;

    public DoubleException() {
        super();
    }

    public DoubleException(String message) {
        super(message);
    }

    public DoubleException(String message, Throwable cause) {
        super(message, cause);
    }

    public DoubleException(Throwable cause) {
        super(cause);
    }
}
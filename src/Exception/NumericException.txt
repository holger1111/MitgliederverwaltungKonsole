package Exception;

public class NumericException extends AppException {
    private static final long serialVersionUID = 1L;

    public NumericException() {
        super();
    }

    public NumericException(String message) {
        super(message);
    }

    public NumericException(String message, Throwable cause) {
        super(message, cause);
    }

    public NumericException(Throwable cause) {
        super(cause);
    }
}

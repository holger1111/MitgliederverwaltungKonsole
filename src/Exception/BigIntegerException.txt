package Exception;

public class BigIntegerException extends NumericException {
    private static final long serialVersionUID = 1L;

    public BigIntegerException() {
        super();
    }

    public BigIntegerException(String message) {
        super(message);
    }

    public BigIntegerException(String message, Throwable cause) {
        super(message, cause);
    }

    public BigIntegerException(Throwable cause) {
        super(cause);
    }
}
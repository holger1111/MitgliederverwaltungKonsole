package Exception;

public class FloatException extends NumericException {
    private static final long serialVersionUID = 1L;

    public FloatException() {
        super();
    }

    public FloatException(String message) {
        super(message);
    }

    public FloatException(String message, Throwable cause) {
        super(message, cause);
    }

    public FloatException(Throwable cause) {
        super(cause);
    }
}
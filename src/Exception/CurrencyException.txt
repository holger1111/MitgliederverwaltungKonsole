package Exception;

public class CurrencyException extends DirectException {
    private static final long serialVersionUID = 1L;

    public CurrencyException() {
        super();
    }

    public CurrencyException(String message) {
        super(message);
    }

    public CurrencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public CurrencyException(Throwable cause) {
        super(cause);
    }
}
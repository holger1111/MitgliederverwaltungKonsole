package Exception;

public class StartDateException extends DateException {
    private static final long serialVersionUID = 1L;

    public StartDateException() {
        super();
    }

    public StartDateException(String message) {
        super(message);
    }

    public StartDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public StartDateException(Throwable cause) {
        super(cause);
    }
}
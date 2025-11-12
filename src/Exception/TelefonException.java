package Exception;

public class TelefonException extends Exception {
    private static final long serialVersionUID = 1L;

    public TelefonException() {
        super();
    }

    public TelefonException(String message) {
        super(message);
    }

    public TelefonException(String message, Throwable cause) {
        super(message, cause);
    }

    public TelefonException(Throwable cause) {
        super(cause);
    }
}

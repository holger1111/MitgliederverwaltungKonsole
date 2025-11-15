package Exception;

public class RangeException extends AppException {
    private static final long serialVersionUID = 1L;

    public RangeException() {
        super();
    }

    public RangeException(String message) {
        super(message);
    }

    public RangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RangeException(Throwable cause) {
        super(cause);
    }
}

package Exception;

public class ValueNotFoundException extends NotFoundException {
    private static final long serialVersionUID = 1L;
    public ValueNotFoundException() {
        super();
    }

    public ValueNotFoundException(String message) {
        super(message);
    }

    public ValueNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
package New.Exception;

public class IDNotFoundException extends NotFoundException {
    private static final long serialVersionUID = 1L;
    public IDNotFoundException() {
        super();
    }

    public IDNotFoundException(String message) {
        super(message);
    }

    public IDNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
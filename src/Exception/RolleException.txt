package Exception;

public class RolleException extends StringException {
    private static final long serialVersionUID = 1L;

    public RolleException() {
        super();
    }

    public RolleException(String message) {
        super(message);
    }

    public RolleException(String message, Throwable cause) {
        super(message, cause);
    }

    public RolleException(Throwable cause) {
        super(cause);
    }
}

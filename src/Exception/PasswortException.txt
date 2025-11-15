package Exception;

public class PasswortException extends StringException {
    private static final long serialVersionUID = 1L;

    public PasswortException() {
        super();
    }

    public PasswortException(String message) {
        super(message);
    }

    public PasswortException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswortException(Throwable cause) {
        super(cause);
    }
}

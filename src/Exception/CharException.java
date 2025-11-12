package Exception;

public class CharException extends Exception {
    private static final long serialVersionUID = 1L;
    public CharException() { super(); }
    public CharException(String message) { super(message); }
    public CharException(String message, Throwable cause) { super(message, cause); }
    public CharException(Throwable cause) { super(cause); }
}
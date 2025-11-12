package Exception;

public class ByteException extends Exception {
    private static final long serialVersionUID = 1L;
    public ByteException() { super(); }
    public ByteException(String message) { super(message); }
    public ByteException(String message, Throwable cause) { super(message, cause); }
    public ByteException(Throwable cause) { super(cause); }
}
package boundary;

public class GameExitException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public GameExitException(String message) { super(message); }
}
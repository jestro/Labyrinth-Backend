package framework.web;
public class ForbiddenAccessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ForbiddenAccessException(String message) {
        super(message);
    }
}


package jocture.todo.exception;

public class AuthenticationProblemException extends RuntimeException {

    public AuthenticationProblemException(String message) {
        super(message);
    }

    public AuthenticationProblemException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationProblemException(Throwable cause) {
        super(cause);
    }
}

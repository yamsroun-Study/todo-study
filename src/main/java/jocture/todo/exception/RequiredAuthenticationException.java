package jocture.todo.exception;

public class RequiredAuthenticationException extends AuthenticationProblemException {

    public RequiredAuthenticationException(String message) {
        super(message);
    }

    public RequiredAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequiredAuthenticationException(Throwable cause) {
        super(cause);
    }
}

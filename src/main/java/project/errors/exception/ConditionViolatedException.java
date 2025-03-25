package project.errors.exception;

public class ConditionViolatedException extends RuntimeException {
    public ConditionViolatedException(String message) {
        super(message);
    }
}

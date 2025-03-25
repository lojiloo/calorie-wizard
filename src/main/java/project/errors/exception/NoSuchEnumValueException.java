package project.errors.exception;

public class NoSuchEnumValueException extends RuntimeException {
    public NoSuchEnumValueException(String message) {
        super(message);
    }
}

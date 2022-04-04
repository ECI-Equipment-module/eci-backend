package eci.server.exception.route;

public class CannotConvertNestedStructureException extends RuntimeException {
    public CannotConvertNestedStructureException(String message) {
        super(message);
    }
}
package eci.server.ItemModule.exception.route;

public class CannotConvertNestedStructureException extends RuntimeException {
    public CannotConvertNestedStructureException(String message) {
        super(message);
    }
}
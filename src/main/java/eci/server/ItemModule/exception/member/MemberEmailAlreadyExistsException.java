package eci.server.ItemModule.exception.member;

public class MemberEmailAlreadyExistsException extends RuntimeException {
    public MemberEmailAlreadyExistsException(String message) {
        super(message);
    }
}
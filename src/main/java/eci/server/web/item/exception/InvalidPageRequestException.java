package eci.server.web.item.exception;

public class InvalidPageRequestException extends RuntimeException {
    public void InvalidPageRequstException() {
        System.out.println("페이지 수가 적절하지 않습니다");

    }
}
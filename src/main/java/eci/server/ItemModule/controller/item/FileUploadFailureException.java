package eci.server.ItemModule.controller.item;

public class FileUploadFailureException extends RuntimeException {
    public FileUploadFailureException(Throwable cause) {
        super(cause);
    }
}
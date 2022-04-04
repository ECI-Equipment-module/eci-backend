package eci.server.exception.file;

public class FileUploadFailureException extends RuntimeException {
    public FileUploadFailureException(Throwable cause) {
        super(cause);
    }
}
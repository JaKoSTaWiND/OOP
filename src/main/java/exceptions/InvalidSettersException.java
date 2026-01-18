package exceptions;

public class InvalidSettersException extends RuntimeException {
    public InvalidSettersException(String message) {
        super(message);
    }
}

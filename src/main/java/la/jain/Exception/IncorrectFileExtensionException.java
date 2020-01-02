package la.jain.Exception;

public class IncorrectFileExtensionException extends RuntimeException {
    public IncorrectFileExtensionException(String errorMessage) {
        super(errorMessage);
    }
}
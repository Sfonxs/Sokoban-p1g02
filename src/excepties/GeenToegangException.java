package excepties;

public class GeenToegangException extends Exception {

    public GeenToegangException() {
    }

    public GeenToegangException(String message) {
        super(message);
    }

    public GeenToegangException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeenToegangException(Throwable cause) {
        super(cause);
    }

    public GeenToegangException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}

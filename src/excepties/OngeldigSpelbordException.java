package excepties;

public class OngeldigSpelbordException extends Exception {

    public OngeldigSpelbordException() {
    }

    public OngeldigSpelbordException(String message) {
        super(message);
    }

    public OngeldigSpelbordException(String message, Throwable cause) {
        super(message, cause);
    }

    public OngeldigSpelbordException(Throwable cause) {
        super(cause);
    }

    public OngeldigSpelbordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

package excepties;

public class OnbestaandSpelException extends Exception {

    public OnbestaandSpelException() {
    }

    public OnbestaandSpelException(String message) {
        super(message);
    }

    public OnbestaandSpelException(String message, Throwable cause) {
        super(message, cause);
    }

    public OnbestaandSpelException(Throwable cause) {
        super(cause);
    }

    public OnbestaandSpelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

package excepties;

public class BestaandSpelException extends Exception {

    public BestaandSpelException() {
        this("Bestaand spel is al gekozen");
    }

    public BestaandSpelException(String message) {
        super(message);
    }

    public BestaandSpelException(String message, Throwable cause) {
        super(message, cause);
    }

    public BestaandSpelException(Throwable cause) {
        super(cause);
    }

    public BestaandSpelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

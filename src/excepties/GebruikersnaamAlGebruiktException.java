package excepties;

public class GebruikersnaamAlGebruiktException extends Exception {

    public GebruikersnaamAlGebruiktException() {
    }

    public GebruikersnaamAlGebruiktException(String message) {
        super(message);
    }

    public GebruikersnaamAlGebruiktException(String message, Throwable cause) {
        super(message, cause);
    }

    public GebruikersnaamAlGebruiktException(Throwable cause) {
        super(cause);
    }

    public GebruikersnaamAlGebruiktException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

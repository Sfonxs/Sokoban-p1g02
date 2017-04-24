package excepties;

public class OngekendeSpelerExeption extends Exception {

    public OngekendeSpelerExeption() {
    }

    public OngekendeSpelerExeption(String message) {
        super(message);
    }

    public OngekendeSpelerExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public OngekendeSpelerExeption(Throwable cause) {
        super(cause);
    }

    public OngekendeSpelerExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

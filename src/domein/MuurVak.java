package domein;

public class MuurVak extends Vak {

    /**
     *
     * @param x
     * @param y
     */
    public MuurVak(int x, int y) {
        super(x, y);
    }

    /**
     * <p>
     * Bepaalt of een muur toegankelijk is voor een ventje om op te gaan
     * staan.</p>
     *
     * @return False aangezien een muur niet toegankelijk is.
     */
    public boolean isToegankelijk() {
        return false;
    }

}

package domein;

public abstract class Vak {

    private int x;
    private int y;

    public Vak(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return this.y;
    }

    /**
     *
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    /**
     *
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * <p>
     * Returnt de toegankelijkheid van een vak, {@link domein.Ventje} kan een
     * {@link domein.VeldVak} betreden, maar geen {@link domein.MuurVak}.
     * </p>
     *
     * @return true Indien het een VeldVak is, indien niet is het een Muurvak en
     * wordt er false gereturned.
     */
    public abstract boolean isToegankelijk();

}

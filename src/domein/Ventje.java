package domein;

public class Ventje {

    private int x;
    private int y;

    public Ventje(int x, int y) {
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

}

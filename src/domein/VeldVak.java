package domein;

public class VeldVak extends Vak {

    private boolean isDoel;
    private boolean isKist;

    public VeldVak(int x, int y, boolean isDoel, boolean isKist) {
        super(x, y);
        this.isDoel = isDoel;
        this.isKist = isKist;
    }

    /**
     * <p>
     * Controleert of een {@link domein.Vak} een doel is.</p>
     *
     * @return true als het {@link domein.Vak} een doel is, false als het geen
     * doel is.
     */
    public boolean isDoel() {
        return isDoel;
    }

    /**
     * <p>
     * Controleert of een {@link domein.Vak} een kist is.</p>
     *
     * @return isKist als het {@link domein.Vak} een kist is, false als het geen
     * kist is.
     */
    public boolean isKist() {
        return isKist;
    }

    public void setIsKist(boolean isKist) {
        this.isKist = isKist;
    }

    public void setIsDoel(boolean isDoel) {
        this.isDoel = isDoel;
    }

    /**
     * <p>
     * Controleert of een {@link domein.Vak} toegankelijk is.</p>
     *
     * @return true als het {@link domein.Vak} toegankelijk is, false als het
     * niet toegankelijk is.
     */
    @Override
    public boolean isToegankelijk() {
        return true;
    }

}

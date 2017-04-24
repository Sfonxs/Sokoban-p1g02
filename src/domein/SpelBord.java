package domein;

import excepties.OngeldigSpelbordException;
import resources.ResourceBundel;

public class SpelBord {

    private Vak[][] vakken;
    private boolean isVoltooid;
    private int levelNummer;
    private Ventje ventje;
    private ResourceBundel rb;
    private int aantalStappen;

    public SpelBord(Vak[][] vakken, boolean isVoltooid, int levelNummer, Ventje ventje) {
        if (vakken == null) {
            vakken = new Vak[10][10];
            for (int x = 0; x < vakken.length; x++) {
                for (int y = 0; y < vakken[x].length; y++) {
                    vakken[x][y] = new MuurVak(x, y);
                }
            }
        }
        this.vakken = vakken;
        this.isVoltooid = isVoltooid;
        this.levelNummer = levelNummer;
        this.ventje = ventje;
        this.rb = new ResourceBundel();
        this.aantalStappen = 0;
    }

    public boolean getIsVoltooid() {
        return this.isVoltooid;
    }

    public void setIsVoltooid(boolean isVoltooid) {
        this.isVoltooid = isVoltooid;
    }

    public int getLevelNummer() {
        return this.levelNummer;
    }

    public void setLevelNummer(int levelNummer) {
        this.levelNummer = levelNummer;
    }

    public Vak[][] getVakken() {
        return vakken;
    }

    public Ventje getVentje() {
        return ventje;
    }

    /**
     * <p>
     * Deze methode verplaaatst het {@link domein.Ventje} op het
     * {@link domein.SpelBord}. Eerst wordt er gecontroleerd of het of de
     * gevraagde verplaatsing wel kan plaatsvinden (het {@link domein.Ventje}
     * kan namelijk niet buiten het {@link domein.SpelBord} gaan). Vervolgens
     * wordt er gecontroleerd of er op de gevraagde locatie (als het een
     * {@link domein.VeldVak} is) een kist staat. Indien het {@link domein.Vak}
     * 2 hokjes verder een {@link domein.VeldVak} is en geen kist bevat, wordt
     * het {@link domein.Ventje} verplaatst en wordt de kist verschoven. Wanneer
     * het volgend vakje een {@link domein.VeldVak} zonder kist is, wordt het
     * {@link domein.Ventje} normaal verplaatst. </p>
     *
     * @param richting
     */
    public void verplaatsVentje(String richting) {
        int deltaY = 0;
        int deltaX = 0;
        if (richting.equalsIgnoreCase(rb.geefTekst("speel_spel_verplaats_boven"))) {
            deltaY = -1;
        } else if (richting.equalsIgnoreCase(rb.geefTekst("speel_spel_verplaats_onder"))) {
            deltaY = 1;
        } else if (richting.equalsIgnoreCase(rb.geefTekst("speel_spel_verplaats_links"))) {
            deltaX = -1;
        } else if (richting.equalsIgnoreCase(rb.geefTekst("speel_spel_verplaats_rechts"))) {
            deltaX = 1;
        }
        int newX = ventje.getX() + deltaX;
        int newY = ventje.getY() + deltaY;
        int newerX = ventje.getX() + (2 * deltaX);
        int newerY = ventje.getY() + (2 * deltaY);

        // Controlleer ofdat de niewe positie van het ventje binnen de map is.
        if (newY >= 0 && newY < vakken.length && newX >= 0 && newX < vakken[newY].length) { // Als het vakje dat het ventje naar toe wilt in de map ligt

            if (newY >= 0 && newY < vakken.length && newX >= 0 && newX < vakken[newY].length) {
                // TODO controleer ofdat er een muur word geraakt, of een kist word verduwd.

                Vak hetVak = vakken[newY][newX];

                if (newerY >= 0 && newerY < vakken.length && newerX >= 0 && newerX < vakken[newerY].length) {

                    Vak hetNieuwerVak = vakken[newerY][newerX];

                    if (hetVak instanceof VeldVak) {
                        if (((VeldVak) hetVak).isKist()) {

                            if (hetNieuwerVak instanceof VeldVak) {
                                if (!((VeldVak) hetNieuwerVak).isKist()) {
                                    ((VeldVak) hetVak).setIsKist(false);
                                    ((VeldVak) hetNieuwerVak).setIsKist(true);
                                    ventje.setX(newX);
                                    ventje.setY(newY);
                                    aantalStappen++;
                                }

                            }

                        } else {
                            ventje.setX(newX);
                            ventje.setY(newY);
                            aantalStappen++;
                        }
                    }

                } /*else {

                 if (hetVak instanceof VeldVak && (!((VeldVak) hetVak).isKist())) {
                 ventje.setX(newX);
                 ventje.setY(newY);
                 aantalStappen++;
                 }
                 }
                 */

            }
        }

        boolean isVoltooid = true;
        for (Vak[] rij : this.vakken) {
            for (Vak vak : rij) {
                if (vak instanceof VeldVak) {
                    VeldVak vVak = (VeldVak) vak;
                    if (vVak.isKist() && !vVak.isDoel()) {
                        isVoltooid = false;
                        break;
                    }
                }
            }
            if (!isVoltooid) {
                break;
            }
        }
        setIsVoltooid(isVoltooid);
    }

    public SpelBord(int levelNummer) {
        this(null, false, levelNummer, new Ventje(5, 5));
    }

    /**
     * <p>
     * Controleert of het aantal doelen gelijk is aan het aantal kisten.</p>
     *
     * @throws OngeldigSpelbordException Indien het aantal kisten niet gelijk is
     * aan het aantal doelen.
     */
    public void isGeldig() throws OngeldigSpelbordException {
        int aantalDoelen = 0;
        int aantalKisten = 0;
        for (int y = 0; y < vakken.length; y++) {
            for (int x = 0; x < vakken[y].length; x++) {
                Vak vak = vakken[y][x];
                if (vak instanceof VeldVak) {
                    VeldVak vVak = (VeldVak) vak;
                    if (vVak.isDoel()) {
                        aantalDoelen++;
                    }
                    if (vVak.isKist()) {
                        aantalKisten++;
                    }
                }
            }
        }
        if (aantalDoelen != aantalKisten) {
            throw new OngeldigSpelbordException("ongeldigspel_exception");
        }
    }

    public int getAantalStappen() {
        return aantalStappen;
    }
}

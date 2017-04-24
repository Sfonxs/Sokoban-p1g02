package domein;

import java.util.*;
import resources.ResourceBundel;
import excepties.OngeldigSpelbordException;

public class SpelCreator {

    private List<SpelBord> spelBorden;
    private String spelNaam;
    private ResourceBundel rb;

    /**
     *
     * @param spelNaam
     */
    public SpelCreator(String spelNaam) {
        this.spelNaam = spelNaam;
        this.spelBorden = new ArrayList();
        this.rb = new ResourceBundel();
    }

    /**
     * <p>
     * Voegt een nieuw {@link domein.SpelBord} toe aan de lijst van
     * spelborden.</p>
     *
     * @throws OngeldigSpelbordException als het {@link domein.SpelBord} niet
     * geldig is.
     */
    public void maakNieuwSpelbord() throws OngeldigSpelbordException {
        try {
            geefActiefSpelbord().isGeldig();
        } catch (RuntimeException e) {
            // ignore
        }
        spelBorden.add(new SpelBord(this.spelBorden.size()));
    }

    /**
     * <p>
     * Geeft een {@link domein.SpelBord} uit de lijst van spelborden.</p>
     *
     * @return een {@link domein.SpelBord} uit de lijst spelborden.
     */
    public SpelBord geefActiefSpelbord() {

        if (spelBorden.size() == 0) {
            throw new RuntimeException(rb.geefTekst("actief_spelbord_fout"));
        }
        return spelBorden.get(spelBorden.size() - 1);
    }

    /**
     * <p>
     * Geeft de grootte van de lijst spelborden terug.</p>
     *
     * @return het aantal spelborden.
     */
    public int geefAantalSpelborden() {
        return this.spelBorden.size();
    }

    public String getSpelNaam() {
        return spelNaam;
    }

    public List<SpelBord> getSpelborden() {
        return this.spelBorden;
    }

}

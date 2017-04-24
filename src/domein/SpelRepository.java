package domein;

import excepties.BestaandSpelException;
import excepties.OnbestaandSpelException;
import java.util.*;
import persistentie.SpelMapper;
import resources.ResourceBundel;

public class SpelRepository {

    private List<Spel> spelen;
    private SpelMapper spelMapper;
    private ResourceBundel rb;

    public SpelRepository() {
        this.spelMapper = new SpelMapper();
        this.spelen = spelMapper.geefAlleSpellen();
        this.rb = new ResourceBundel();
    }

    /**
     * <p>
     * Geeft een lijst van alle spellen.</p>
     *
     * @return een lijst van alle spellen.
     */
    public List<Spel> geefAlleSpellen() {
        return spelen;
    }

    /**
     * <p>
     * Herlaad alle spellen uit de mysql databse.</p>
     *
     * @param spel Het te herladen spel
     */
    public void herlaadSpellen() {
        this.spelen = spelMapper.geefAlleSpellen();
    }

    /**
     * <p>
     * Geeft een spel terug uit de lijst van alle spellen.</p>
     *
     * @param spelNaam
     * @return {@link domein.Spel}
     * @throws OnbestaandSpelException als het spel niet bestaat.
     */
    public Spel geefSpel(String spelNaam) throws OnbestaandSpelException {
        Spel spel = null;
        for (Spel spol : spelen) {
            if (spol.getNaam().equals(spelNaam)) {
                spel = spol;
            }
        }
        if (spel == null) {
            throw new OnbestaandSpelException(rb.geefTekst("spelnaam_fout").
                    replace("{SPELNAAM}", spelNaam));
        }
        return spel;
    }

    /**
     * <p>
     * Controleert of het {@link domein.Spel} al bestaat.</p>
     *
     * @param spelNaam
     * @return false als het {@link domein.Spel} nog niet bestaat, true als het
     * wel al bestaat.
     * @throws BestaandSpelException als het spel al bestaat.
     */
    public boolean bestaatSpel(String spelNaam) throws BestaandSpelException {
        try {
            Spel spel = geefSpel(spelNaam);
            throw new BestaandSpelException(rb.geefTekst("exception_bestaatSpel").
                    replace("{SPELNAAM}", spelNaam));
        } catch (OnbestaandSpelException ex) {
            // ignore
        }
        return false;
    }

    /**
     * <p>
     * Slaat het nieuwe {@link domein.Spel} op via de
     * {@link persistentie.SpelMapper}</p>
     *
     * @param spel
     */
    public void slaNieuwSpelOp(SpelCreator spel) {
        spelMapper.slaSpelOp(spel);
        this.spelen = spelMapper.geefAlleSpellen();
    }

}

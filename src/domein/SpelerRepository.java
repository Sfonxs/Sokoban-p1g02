package domein;

import excepties.GebruikersnaamAlGebruiktException;
import java.util.*;
import persistentie.SpelerMapper;
import resources.ResourceBundel;

public class SpelerRepository {

    private List<Speler> spelers;
    private SpelerMapper spelerMapper;
    private ResourceBundel rb;

    public SpelerRepository() {
        this.spelers = new ArrayList();
        this.spelerMapper = new SpelerMapper();
        this.rb = new ResourceBundel();
    }

    /**
     * <p>
     * Haalt een {@link domein.Speler} op uit de database via de
     * {@link persistentie.SpelerMapper}.</p>
     *
     * @param gebruikersnaam
     * @param wachtwoord
     * @return {@link domein.Speler}
     */
    public Speler geefSpeler(String gebruikersnaam, String wachtwoord) {
        for (Speler speler : spelers) {
            if (speler.getGebruikersNaam().equals(gebruikersnaam) && speler.getWachtwoord().equals(wachtwoord)) {
                return speler;
            }
        }
        return spelerMapper.geefSpeler(gebruikersnaam, wachtwoord);
    }

    /**
     * <p>
     * Voegt een nieuwe {@link domein.Speler} toe via de
     * {@link persistentie.SpelerMapper}.</p>
     *
     * @param naam
     * @param voornaam
     * @param gebruikersNaam
     * @param wachtwoord
     * @throws IllegalArgumentException als de voornaam en/of naam en/of
     * gebruikersnaam langer dan 45 tekens zijn.. Als de gebruikersnaam en/of
     * wachtwoord null of korter dan 8 tekens zijn. Als het wachtwoord verkeerde
     * tekens bevat.
     * @throws GebruikersnaamAlGebruiktException als de gebruikersnaam al in
     * gebruik is.
     */
    public void voegNieuweSpelerToe(String naam, String voornaam,
            String gebruikersNaam, String wachtwoord)
            throws IllegalArgumentException, GebruikersnaamAlGebruiktException {
        if (naam == null) {
            naam = "";
        }
        if (voornaam == null) {
            voornaam = "";
        }
        if (naam.length() > 45) {
            throw new IllegalArgumentException(rb.geefTekst("naam_fout"));
        }
        if (voornaam.length() > 45) {
            throw new IllegalArgumentException(rb.geefTekst("voornaam_fout"));
        }
        if (gebruikersNaam == null || gebruikersNaam.length() < 8) {
            throw new IllegalArgumentException(rb.geefTekst("gebruikersnaam_fout1"));
        }
        if (gebruikersNaam.length() > 45) {
            throw new IllegalArgumentException(rb.geefTekst("gebruikersnaam_fout2"));
        }
        if (wachtwoord == null || wachtwoord.length() < 8) {
            throw new IllegalArgumentException(rb.geefTekst("wachtwoord_fout1"));
        }
        if (!wachtwoord.matches(".{0,}[a-z]{1,}.{0,}") || !wachtwoord.matches(".{0,}[A-Z]{1,}.{0,}") || !wachtwoord.matches(".{0,}[0-9]{1,}.{0,}")) {
            throw new IllegalArgumentException(rb.geefTekst("wachtwoord_fout2"));
        }

        Speler speler = new Speler(gebruikersNaam, wachtwoord, false, naam, voornaam);
        this.spelerMapper.voegToe(speler);
    }

}

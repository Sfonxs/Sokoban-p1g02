package domein;

import resources.ResourceBundel;

public class Speler {

    private String gebruikersNaam;
    private String wachtwoord;
    private boolean isAdmin;
    private String voornaam;
    private String naam;
    private ResourceBundel rb;

    public Speler(String gebruikersNaam, String wachtwoord, boolean isAdmin, String naam, String voornaam) {
        setGebruikersNaam(gebruikersNaam);
        setWachtwoord(wachtwoord);
        setIsAdmin(isAdmin);
        setNaam(naam);
        setVoornaam(voornaam);
        this.rb = new ResourceBundel();
    }

    /**
     *
     * @param wachtwoord Het gewenste wachtwoord.
     * @throws IllegalArgumentException Indien er niets wordt ingevuld of indien
     * het gewenste wachtwoord te lang is (langer dan 45 karakters).
     */
    private void setWachtwoord(String wachtwoord) {
        if (wachtwoord == null || wachtwoord.isEmpty()) {
            throw new IllegalArgumentException(rb.geefTekst("geen_wachtwoord_fout"));
        }
        if (wachtwoord.length() > 45) {
            throw new IllegalArgumentException(rb.geefTekst("lang_wachtwoord_fout"));
        }
        this.wachtwoord = wachtwoord;
    }

    private void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     *
     * @param gebruikersNaam De gewenste gebruikersnaam.
     * @throws IllegalArgumentException Indien er niets wordt ingevuld of indien
     * de gewenste gebruikersnaam te lang is (langer dan 45 karakters).
     */
    private void setGebruikersNaam(String gebruikersNaam) {
        if (gebruikersNaam == null || gebruikersNaam.isEmpty()) {
            throw new IllegalArgumentException(rb.geefTekst("geen_gebruikersnaam_fout"));
        }
        if (gebruikersNaam.length() > 45) {
            throw new IllegalArgumentException(rb.geefTekst("lange_gebruikersnaam_fout"));
        }
        this.gebruikersNaam = gebruikersNaam;
    }

    public String getGebruikersNaam() {
        return gebruikersNaam;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    /**
     * <p>
     * Controleert of een {@link domein.Speler} admin is.
     *
     * @return isAdmin als de {@link domein.Speler} admin is, false als hij een
     * gewone {@link domein.Speler} is.</p>
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getNaam() {
        return naam;
    }

    private void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    private void setNaam(String naam) {
        this.naam = naam;
    }
}

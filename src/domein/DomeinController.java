package domein;

import excepties.BestaandSpelException;
import excepties.GebruikersnaamAlGebruiktException;
import excepties.GeenToegangException;
import excepties.OnbestaandSpelException;
import excepties.OngekendeSpelerExeption;
import excepties.OngeldigSpelbordException;
import java.util.List;
import resources.ResourceBundel;

public class DomeinController {

    private Speler speler;
    private SpelerRepository spelerRepository;
    private SpelRepository spelRepository;
    private Spel actiefSpel;
    private SpelCreator spelCreator;
    private ResourceBundel rb;

    /**
     * Default constructor
     */
    public DomeinController() {
        this.spelerRepository = new SpelerRepository();
        this.spelRepository = new SpelRepository();
        this.rb = new ResourceBundel();
    }

    /**
     * <p>
     * Meldt de gegeven speler aan.</p>
     * <p>
     * Roep {@link #geefAanmeldResultaten() geefAanmeldResultaten()} op om het
     * resultaat van het aanmelden te weten.</p>
     *
     * @param gebruikersNaam De gebruikersnaam van de gebruiker.
     * @param wachtwoord Het wachtwoord van de gebruiker.
     */
    public void meldAan(String gebruikersNaam, String wachtwoord) {
        Speler speler = spelerRepository.geefSpeler(gebruikersNaam, wachtwoord);
        setSpeler(speler);
    }

    /**
     * <p>
     * Geeft de momenteel aangemelde speler.</p>
     *
     * @return De aangemelde speler of null indien geen speler is aangemeld
     */
    public Speler getSpeler() {
        return speler;
    }

    /**
     * <p>
     * Geeft de momenteel aangemelde speler.</p>
     *
     * @return De aangemelde speler of null indien geen speler is aangemeld
     */
    private void setSpeler(Speler speler) {
        this.speler = speler;
    }

    /**
     * <p>
     * De weergegeven String array bevat</p>
     * <ul><li>0 - de gebruikersnaam</li>
     * <li>1 - menu keuze speel spel</li>
     * <li>2 - menu keuze configureer spel</li>
     * <li>3 - menu keuze wijzig spel</ul>
     *
     * @return De aanmeld gegevens van de aangewelde speler.
     * @throws excepties.OngekendeSpelerExeption Indien er momenteel geen speler
     * is aangemeld.
     */
    public String[] geefAanmeldResultaten() throws OngekendeSpelerExeption {
        Speler speler = getSpeler();
        if (speler == null) {
            throw new OngekendeSpelerExeption(rb.geefTekst("aanmeld_resultaat_fout"));
        } else {
            String[] result = new String[4];
            result[0] = speler.getGebruikersNaam();
            result[1] = rb.geefTekst("aanmeld_resultaat_speel");
            result[2] = rb.geefTekst("aanmeld_resultaat_configureer");
            result[3] = rb.geefTekst("aanmeld_resultaat_wijzig");
            return result;
        }
    }

    /**
     * <p>
     * Voeg een nieuwe {@link domein.Speler} toe aan de
     * {@link domein.SpelerRepository}</p>
     *
     * @param naam De naam van de speler.
     * @param gebruikersNaam De gebruikersnaam van de speler.
     * @param voornaam De voornaam van de speler.
     * @param wachtwoord Het wachtwoord van de speler.
     * @throws excepties.GebruikersnaamAlGebruiktException Indien de
     * gebruikersnaam reeds gebruikt is.
     */
    public void registreer(String naam, String gebruikersNaam, String voornaam, String wachtwoord)
            throws IllegalArgumentException, GebruikersnaamAlGebruiktException {
        spelerRepository.voegNieuweSpelerToe(naam, voornaam, gebruikersNaam, wachtwoord);
        meldAan(gebruikersNaam, wachtwoord);
    }

    /**
     * <p>
     * Geeft een array terug met alle spel namen.</p>
     *
     * @return Een String array met alle spel namen.
     */
    public String[] startSpelSpelen() {
        List<Spel> spellen = spelRepository.geefAlleSpellen();
        String[] result = new String[spellen.size()];
        for (int i = 0; i < spellen.size(); i++) {
            result[i] = spellen.get(i).getNaam();
        }
        return result;
    }

    /**
     * <p>
     * Stel het spel met de opgegeven naam (indien dit spel bestaat in de
     * spelrepository) in als actief spel.</p>
     *
     * @param spelNaam De naam van het actief spel.
     * @throws excepties.OnbestaandSpelException Indien er geen spel bestaat met
     * de opgegeven naam.
     */
    public void speelSpel(String spelNaam) throws IllegalArgumentException, OnbestaandSpelException {
        Spel spel = spelRepository.geefSpel(spelNaam);
        if (spel == null) {
            throw new IllegalArgumentException(rb.geefTekst("speel_spel").replace("{SPELNAAM}", spelNaam));
        }
        setActiefSpel(spel);
    }

    /**
     * <p>
     * Geeft een twee dimentionaal String array van 10x10 terug die de alle
     * {@link domein.Vak} objecten van het actief {@link domein.SpelBord}
     * voorstellen.</p>
     *
     * @return Twee dimentionaal String array (10x10) die een spelbord
     * voorstelt.
     */
    public String[][] geefSpelBordVelden() {
        SpelBord actiefSpelBord = getActiefSpel().getActiefSpelbord();
        Vak[][] vakken = actiefSpelBord.getVakken();
        return zetVakkenOmNaarStrings(vakken, actiefSpelBord.getVentje());
    }

    /**
     * @return true indien het actief spelbord is voltooid, anders false
     */
    public boolean isActiefSpelVoltooid() {
        return getActiefSpel().getIsVoltooid();
    }

    /**
     * <p>
     * Verplaats het ventje van het actief spelbord 1 vakje in de gegeven
     * richting.</p>
     *
     * @param richting De richting van de verplaatsing. (boven, onder, links,
     * rechts)
     */
    public void verplaatsVentje(String richting) {
        getActiefSpel().verplaatsVentje(richting);
    }

    /**
     * @return het aantal voltooide spel borden en het totaal aantal spelborden
     * in het actief spel.
     */
    public int[] geefSpelbordResultaat() {
        int[] result = new int[2];
        result[0] = getActiefSpel().getAantalVoltooideSpelBorden();
        result[1] = getActiefSpel().getAantalTotaalSpelBorden();
        return result;
    }

    /**
     * <p>
     * Het volgende spelbord van het actief spel zal geladen worden en ingesteld
     * worden als actief spelbord.</p>
     * <p>
     * Indien er geen volgende spelborden zijn zal het spel voltooid zijn.</p>
     *
     */
    public void speelVolgendSpelbord() {
        getActiefSpel().creeerVolgendSpelbord();
    }

    /**
     * @return true als het actief spelbord is voltooid, anders false
     */
    public boolean isSpelbordVoltooid() {
        return getActiefSpel().getActiefSpelbord().getIsVoltooid();
    }

    /**
     * Start het configureren van een spel.
     *
     * @throws excepties.GeenToegangException indien de aangemelde speler geen
     * admin is
     */
    public void startConfigurerenSpel() throws GeenToegangException {
        if (!speler.isAdmin()) {
            throw new GeenToegangException("geen_toegang_exception");
        }
    }

    /**
     * <p>
     * Maakt een nieuw {@link domein.SpelCreator} object aan met de gegeven spel
     * naam.</p>
     *
     * @param spelNaam De naam van het te maken spel.
     * @throws excepties.BestaandSpelException Indien er al een spel bestaat met
     * de gegeven naam.
     */
    public void maakNieuwSpel(String spelNaam) throws BestaandSpelException {
        boolean bestaatSpel = this.spelRepository.bestaatSpel(spelNaam);
        if (!bestaatSpel) {
            spelCreator = new SpelCreator(spelNaam);
        }
    }

    /**
     * <p>
     * Voegt een nieuw spelbord toe aan de {@link domein.SpelCreator}.</p>
     */
    public void startMakenNieuwSpelbord() throws OngeldigSpelbordException {
        this.spelCreator.maakNieuwSpelbord();
    }

    /**
     * <p>
     * Geeft een twee dimentionaal String array van 10x10 terug die de alle
     * {@link domein.Vak} objecten van het {@link domein.SpelBord} in opbouw
     * voorstellen.</p>
     *
     * @return Twee dimentionaal String array (10x10) die een spelbord
     * voorstelt.
     */
    public String[][] geefSpelbordInOpbouwVelden() {
        SpelBord spelbordInOpbouw = this.spelCreator.geefActiefSpelbord();
        return zetVakkenOmNaarStrings(spelbordInOpbouw.getVakken(), spelbordInOpbouw.getVentje());
    }

    /**
     * Stelt het Vak object in van het spelbord in opbouw.
     *
     * @param xPos De x positie van het vak.
     * @param yPos De y positie van het vak.
     * @param soortItem Het soort vak/item type.
     */
    public void zetSpelbordItem(int xPos, int yPos, int soortItem) {
        if (xPos < 0 || xPos > 10 || yPos < 0 || yPos > 10) {
            throw new IllegalArgumentException(rb.geefTekst("spelborditem_fout"));
        }
        SpelBord spelbordInOpbouw = this.spelCreator.geefActiefSpelbord();
        Vak[][] vakken = spelbordInOpbouw.getVakken();
        Vak inTeVoegenVak = null;
        Vak oudVak = vakken[yPos][xPos];
        if (soortItem == 4) {
            Ventje ventje = spelbordInOpbouw.getVentje();
            ventje.setX(xPos);
            ventje.setY(yPos);
        } else if (soortItem == 1) {
            inTeVoegenVak = new MuurVak(xPos, yPos);
        } else if (soortItem == 3) {
            boolean isKist = false;
            if (oudVak instanceof VeldVak) {
                isKist = ((VeldVak) oudVak).isKist();
            }
            inTeVoegenVak = new VeldVak(xPos, yPos, true, isKist);
        } else if (soortItem == 2) {
            boolean isDoel = false;
            if (oudVak instanceof VeldVak) {
                isDoel = ((VeldVak) oudVak).isDoel();
            }
            inTeVoegenVak = new VeldVak(xPos, yPos, isDoel, true);
        } else if (soortItem == 5) {
            inTeVoegenVak = new VeldVak(xPos, yPos, false, false);
        }
        if (inTeVoegenVak != null) {
            vakken[yPos][xPos] = inTeVoegenVak;
        }
    }

    /**
     * @return Het aantal spelborden en het spel naam van de
     * {@link domein.SpelCreator}.
     */
    public String[] geefSpelinOpbouwInfo() {
        String[] result = new String[2];
        result[0] = String.valueOf(this.spelCreator.geefAantalSpelborden());
        result[1] = this.spelCreator.getSpelNaam();
        return result;
    }

    /**
     * Slaat het spel in opbouw op.
     */
    public void slaNieuwSpelOp() {
        this.spelRepository.slaNieuwSpelOp(spelCreator);
    }

    /**
     * Meld de aangemelde speler af.
     */
    public void meldAf() {
        setSpeler(null);
    }

    /**
     * Hulp methode om een vakken array en een ventje om te zetten naar een
     * string array.
     */
    private String[][] zetVakkenOmNaarStrings(Vak[][] vakken, Ventje vent) {
        String[][] result = new String[vakken.length][];
        for (int y = 0; y < result.length; y++) {
            result[y] = new String[vakken[y].length];
            for (int x = 0; x < vakken[y].length; x++) {
                Vak vak = vakken[y][x];
                String value;
                if (vak instanceof MuurVak) {
                    value = rb.geefTekst("geef_spelbord_velden_muur");
                } else if (vak instanceof VeldVak) {
                    VeldVak vVak = (VeldVak) vak;
                    if (vVak.isKist() && !vVak.isDoel()) {
                        value = rb.geefTekst("geef_spelbord_velden_kist");
                    } else if (vVak.isDoel() && !vVak.isKist()) {
                        value = rb.geefTekst("geef_spelbord_velden_doel");
                    } else if (vVak.isKist() && vVak.isDoel()) {
                        value = rb.geefTekst("geef_spelbord_velden_doel_op_kist");
                    } else {
                        value = " ";
                    }
                } else {
                    value = rb.geefTekst("geef_speldbord_velden_fout");
                }
                result[y][x] = value;
            }
        }
        result[vent.getY()][vent.getX()] = rb.geefTekst("geef_spelbord_velden_ventje");
        return result;
    }

    private void setActiefSpel(Spel spel) {
        this.actiefSpel = spel;
    }

    private Spel getActiefSpel() {
        return this.actiefSpel;
    }

    /**
     * <p>
     * Geeft een lijst van spelnamen terug zodat er 1 kan gekozen worden om een
     * bepaald {@link domein.Spel} te wijzigen</p>
     *
     * @return De namen van de spellen die reeds bestaan.
     * @throws excepties.GeenToegangException indien de aangemelde speler geen
     * admin is
     */
    public String[] startWijzigspel() throws GeenToegangException {

        if (!speler.isAdmin()) {
            throw new GeenToegangException("geen_toegang_exception");
        }

        List<Spel> spelen;
        spelen = spelRepository.geefAlleSpellen();

        String[] spelNamen = new String[spelen.size()];

        for (int i = 0; i < spelen.size(); i++) {
            spelNamen[i] = spelen.get(i).getNaam();

        }

        return spelNamen;
    }

    /**
     * <p>
     * Geeft het aantal spelborden van het gekozen {@link domein.Spel}
     * terug.</p>
     *
     * @param spelNaam De naam van het gekozen spel.
     * @return Aantal spelborden van het gekozen spel.
     * @throws OnbestaandSpelException Controleert of dit spel wel degelijk
     * bestaat.
     */
    public int kiesSpel(String spelNaam) throws OnbestaandSpelException {
        if (actiefSpel == null || !actiefSpel.getNaam().equals(spelNaam)) {
            if (actiefSpel != null) {
                actiefSpel.slaActiefSpelBordOp();
            }
            actiefSpel = spelRepository.geefSpel(spelNaam);
        }
        return actiefSpel.getAantalTotaalSpelBorden();

    }

    /**
     * <p>
     * Toont het gekozen {@link domein.SpelBord}.</p>
     *
     * @param level Via het level van een spel wordt een spelbord gekozen.
     * @return Het gekozen spelbord.
     */
    public String[][] kiesSpelBord(int level) {
        return zetVakkenOmNaarStrings(actiefSpel.kiesSpelBord(level), actiefSpel.getActiefSpelbord().getVentje());
    }

    /**
     * <p>
     * Plaatst een item op een reeds bestaand {@link domein.SpelBord}.</p>
     *
     * @param xPos De x-coördinaat waar het item zal moeten terechtkomen.
     * @param yPos De y-coördinaat waar het item zal moeten terechtkomen.
     * @param soortItem Ofwel een muurvak, een leeg vak, doel, kist of een
     * ventje.
     */
    public void veranderSpelBordItem(int xPos, int yPos, int soortItem) {
        if (xPos < 0 || xPos > 10 || yPos < 0 || yPos > 10) {
            throw new IllegalArgumentException(rb.geefTekst("spelborditem_fout"));
        }
        SpelBord actiefSpelbord = actiefSpel.getActiefSpelbord();
        Vak[][] vakken = actiefSpelbord.getVakken();
        Vak oudVak = vakken[yPos][xPos];
        Vak inTeVoegenVak = null;
        if (soortItem == 4) {
            Ventje ventje = actiefSpelbord.getVentje();
            ventje.setX(xPos);
            ventje.setY(yPos);
        } else if (soortItem == 1) {
            inTeVoegenVak = new MuurVak(xPos, yPos);
        } else if (soortItem == 3) {
            boolean isKist = false;
            if (oudVak instanceof VeldVak) {
                isKist = ((VeldVak) oudVak).isKist();
            }
            inTeVoegenVak = new VeldVak(xPos, yPos, true, isKist);
        } else if (soortItem == 2) {
            boolean isDoel = false;
            if (oudVak instanceof VeldVak) {
                isDoel = ((VeldVak) oudVak).isDoel();
            }
            inTeVoegenVak = new VeldVak(xPos, yPos, isDoel, true);
        } else if (soortItem == 5) {
            inTeVoegenVak = new VeldVak(xPos, yPos, false, false);
        }
        if (inTeVoegenVak != null) {
            vakken[yPos][xPos] = inTeVoegenVak;
        }
    }

    /**
     * <p>
     * Slaat het gewijzigd {@link domein.Spel} op in de database via de
     * {@link persistentie.SpelBordMapper}.</p>
     */
    public void slaActiefSpelBordOp() {
        if (actiefSpel != null) {
            actiefSpel.slaActiefSpelBordOp();
        }
    }

    /**
     * <p>
     * Geeft weer hoeveel stappen het {@link domein.Ventje} al heeft gezet op
     * het {@link domein.SpelBord}.</p>
     *
     * @return
     */
    public int geefAantalStappenInActiefSpelbord() {
        if (actiefSpel != null) {
            SpelBord sp = actiefSpel.getActiefSpelbord();
            if (sp != null) {
                return sp.getAantalStappen();
            }
        }
        return 0;
    }

    /**
     * <p>
     * Verwijdert het actieve {@link domein.SpelBord} in het actief
     * {@link domein.Spel} via de {@link persistentie.SpelBordMapper}.</p>
     */
    public void verwijderActiefSpelbord() {
        if (actiefSpel != null) {
            actiefSpel.verwijderActiefSpelbord();
            this.actiefSpel = null;
            this.spelRepository.herlaadSpellen();
        }
    }

    /**
     * <p>
     * Reset het actief spel object en zet het actiefspel op null.</p>
     */
    public void resetActiefspel() {
        if (actiefSpel != null) {
            actiefSpel.reset();
        }
        actiefSpel = null;
    }

    /**
     * <p>
     * Alle velden in het actief spelbord worden herladen uit de database.
     * </p>
     */
    public void resetActiefSpelbord() {
        if (actiefSpel != null) {
            actiefSpel.resetActiefSpelbord();
        }
    }

}

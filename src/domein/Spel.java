package domein;

import persistentie.SpelBordMapper;

public class Spel {

    private SpelBord actiefSpelBord;
    private String naam;
    private boolean isVoltooid;
    private int aantalTotaalSpelBorden;
    private int aantalVoltooideSpelBorden;
    private SpelBordMapper sbMapper;

    public boolean getIsVoltooid() {
        return this.isVoltooid;
    }

    public String getNaam() {
        return this.naam;
    }

    /**
     *
     * @param naam
     * @param isVoltooid
     * @param aantalTotaalSpelBorden
     */
    public Spel(String naam, boolean isVoltooid, int aantalTotaalSpelBorden) {
        this.naam = naam;
        this.isVoltooid = isVoltooid;
        this.aantalTotaalSpelBorden = aantalTotaalSpelBorden;
        this.aantalVoltooideSpelBorden = 0;
        this.sbMapper = new SpelBordMapper();
    }

    private void setNaam(String naam) {
        this.naam = naam;
    }

    private void setIsVoltooid(boolean isVoltooid) {
        this.isVoltooid = isVoltooid;
    }

    /**
     * <p>
     * Als het actieve spelbord niet null is, wordt het aantal voltooide
     * spelborden met 1 verhoogd. Indien het {@link domein.Spel} nog een
     * {@link domein.SpelBord} heeft, wordt een nieuw {@link domein.SpelBord}
     * uit de database gehaald en weergegeven. Ook het levelnummer wordt met 1
     * verhoogd.
     *
     * </p>
     */
    public void creeerVolgendSpelbord() {
        if (this.actiefSpelBord != null) {
            this.aantalVoltooideSpelBorden++;
        }
        if (heeftVolgendeSpelbord()) {
            int currentLevel;
            if (this.actiefSpelBord != null) {
                currentLevel = this.actiefSpelBord.getLevelNummer() + 1;
            } else {
                currentLevel = 0;
            }
            SpelBord newBord = sbMapper.geefSpelBord(currentLevel, this.naam);
            setActiefSpelBord(newBord);
        } else {
            setIsVoltooid(true);
        }
    }

    /**
     * <p>
     * Controleert of het {@link domein.spel} nog een volgend
     * {@link domein.SpelBord} heeft.</p>
     *
     * @return Geeft aan of er nog een volgend {@link domein.SpelBord} is.
     */
    private boolean heeftVolgendeSpelbord() {
        return (this.actiefSpelBord == null && this.aantalTotaalSpelBorden > 0) || (getActiefSpelbord().getLevelNummer() < this.aantalTotaalSpelBorden - 1);
    }

    public void setActiefSpelBord(SpelBord actiefSpelBord) {
        this.actiefSpelBord = actiefSpelBord;
    }

//    private SpelBord genereerSpelbord(){
//        boolean isVoltooid = false;
//        int levelNummer = 0;
//        int width = 10;
//        int height = 10;
//        Vak[][] vakken = new Vak[height][width];
//        for (int y = 0; y < vakken.length; y++) {
//            for (int x = 0; x < vakken[y].length; x++) {
//                if (y == 0 || y == height - 1 || x == 0 || x == width - 1) {
//                    vakken[x][y] = new MuurVak(x, y);
//                } else {
//                    vakken[x][y] = new VeldVak(x, y, false, false);
//                }
//            }
//        }
//        List<String> gebruikteLocaties = new ArrayList<>();
//        int aantalKisten = 4;
//        for (int i = 0; i < aantalKisten; i++) {
//            for (int j = 0; j < 2; j++) {
//                int randomX;
//                int randomY;
//                do {
//                    randomX = ((int) Math.floor(Math.random() * ((double) width - 1))) + 1;
//                    randomY = ((int) Math.floor(Math.random() * ((double) height - 1))) + 1;
//                } while (gebruikteLocaties.contains(randomX + ":" + randomY));
//                vakken[randomY][randomX] = new VeldVak(randomX, randomY, j == 0, j == 1);
//
//            }
//        }
//        return new SpelBord(vakken, isVoltooid, levelNummer);
//    }
    public SpelBord getActiefSpelbord() {
        return actiefSpelBord;
    }

    public int getAantalTotaalSpelBorden() {
        return this.aantalTotaalSpelBorden;
    }

    public void setAantalTotaalSpelBorden(int aantalTotaalSpelBorden) {
        this.aantalTotaalSpelBorden = aantalTotaalSpelBorden;
    }

    public int getAantalVoltooideSpelBorden() {
        return this.aantalVoltooideSpelBorden;
    }

    /**
     * <p>
     * Verplaatst het ventje in de meegegeven richting</p>
     *
     * @param richting
     */
    public void verplaatsVentje(String richting) {
        getActiefSpelbord().verplaatsVentje(richting);
    }

    public Spel(String naam, boolean isVoltooid) {
        this.naam = naam;
        this.isVoltooid = isVoltooid;
        setAantalTotaalSpelBorden(getAantalTotaalSpelBorden() + 1);

    }

    private void setAantalVoltooideSpelBorden(int aantalVoltooideSpelBorden) {
        this.aantalVoltooideSpelBorden = aantalVoltooideSpelBorden;
    }

    public SpelBordMapper getSbMapper() {
        return sbMapper;
    }

    public void setSbMapper(SpelBordMapper sbMapper) {
        this.sbMapper = sbMapper;
    }

    /**
     * <p>
     * Roept {@link #slaActiefSpelBordOp()} op om het actieve
     * {@link domein.SpelBord} op te slaan. Haalt een nieuw
     * {@link domein.SpelBord} uit de database.
     *
     * @param level Het spelbord dat de speler wil opslaan.
     * @return Een 2D-Array van {@link domein.Vak}.
     */
    public Vak[][] kiesSpelBord(int level) {
        // TODO: dit aanpassen in de SD's!
        if (actiefSpelBord == null || actiefSpelBord.getLevelNummer() != level) {
            if (actiefSpelBord != null) {
                slaActiefSpelBordOp();
            }
            actiefSpelBord = sbMapper.geefSpelBord(level, this.naam);
        }
        return actiefSpelBord.getVakken();
    }

    /**
     * <p>
     * Slaat het actieve {@link domein.SpelBord} op via
     * {@link persistentie.SpelBordMapper}.</p>
     */
    public void slaActiefSpelBordOp() {
        sbMapper.slaSpelbordOp(actiefSpelBord, naam);
    }

    /**
     * <p>
     * Zorgt dat er geen actief {@link domein.SpelBord} is, dat er geen
     * voltooide spelborden zijn.</p>
     *
     */
    public void reset() {
        actiefSpelBord = null;
        aantalVoltooideSpelBorden = 0;
        isVoltooid = false;
    }

    /**
     * <p>
     * Zorgt dat het actieve {@link domein.SpelBord} teruggezet wordt in zijn
     * oorspronkelijke staat, en geeft het weer. </p>
     */
    public void resetActiefSpelbord() {
        int level;
        if (actiefSpelBord != null) {
            level = actiefSpelBord.getLevelNummer();
        } else {
            level = aantalVoltooideSpelBorden;
        }
        actiefSpelBord = sbMapper.geefSpelBord(level, this.naam);
    }

    /**
     * <p>
     * Verwijdert het actieve {@link domein.SpelBord}.</p>
     */
    public void verwijderActiefSpelbord() {
        if (actiefSpelBord != null) {
            int level = actiefSpelBord.getLevelNummer();
            sbMapper.verwijderSpelbord(level, this.naam);
            actiefSpelBord = null;
        }
    }

}

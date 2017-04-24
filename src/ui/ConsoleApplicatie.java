package ui;

import resources.ResourceBundel;
import domein.DomeinController;
import excepties.BestaandSpelException;
import excepties.GebruikersnaamAlGebruiktException;
import excepties.GeenToegangException;
import excepties.OnbestaandSpelException;
import excepties.OngekendeSpelerExeption;
import excepties.OngeldigSpelbordException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsoleApplicatie {

    private DomeinController domCon;
    private ResourceBundel rb;
    private Scanner scan;
    private String[] aanmeldResultaten;

    public ConsoleApplicatie() {
        domCon = new DomeinController();
        rb = new ResourceBundel();
        scan = new Scanner(System.in);
    }

    public void start() {
        doTaalKeuzeMenu();
        doLoginMenu();
    }

    public void doTaalKeuzeMenu() {
        boolean isValid = false;
        do {
            System.out.println("\nKies uw taal - - Choisissez votre langue - - Choose your language\n\n"
                    + "1. Nederlands\n2. Français\n3. English\n4. Stoppen - Quitter - Quit");
            int taalNummer = -1;
            try {
                taalNummer = scan.nextInt();
            } catch (InputMismatchException e) {
                //ignore
            } finally {
                scan.nextLine();
            }
            if (taalNummer > 0 && taalNummer < 5) {
                isValid = true;
                switch (taalNummer) {
                    case 1:
                        rb.registreerTaal("NL");
                        break;
                    case 2:
                        rb.registreerTaal("FR");
                        break;
                    case 3:
                        rb.registreerTaal("EN");
                        break;
                    case 4:
                        System.out.println("\nTot de volgende keer! - - À la prochaine fois! - - See you next time!\n");
                        System.exit(0);
                        break;
                }
            } else {
                System.out.println("\nGeef een waarde in tussen 1 en 4 - - Entrez une valeur entre 1 et 4 - - Enter a value between 1 and 4\n");
            }
        } while (!isValid);
    }

    public int doLoginMenu() {
        domCon.meldAf();
        boolean isValid = false;
        int keuze = -1;
        while (!isValid) {
            System.out.printf(rb.geefTekst("login_menu_keuze"));
            try {
                keuze = scan.nextInt();
            } catch (InputMismatchException e) {
                // ignore
            } finally {
                scan.nextLine();
            }
            if (keuze > 0 && keuze < 4) {
                isValid = true;
                switch (keuze) {
                    // AANMELDEN
                    case 1:
                        doMeldAan();
                        break;
                    // REGISTREREN
                    case 2:
                        doRegistreer();
                        break;
                    // STOPPEN
                    case 3:
                        doStop();
                        break;
                }
            } else {
                System.out.println(rb.geefTekst("login_menu_fout"));
            }
        }
        return keuze;
    }

    public void doSpelMenu() {
        domCon.resetActiefspel();
        boolean isValid = false;
        int spelKeuze = -1;
        while (!isValid) {
            System.out.println(rb.geefTekst("spel_menu_keuze"));
            for (int x = 1; x < aanmeldResultaten.length; x++) {
                System.out.println(x + ". " + aanmeldResultaten[x]);
            }
            System.out.println("4. " + rb.geefTekst("terug"));
            try {
                spelKeuze = scan.nextInt();
            } catch (InputMismatchException e) {
                System.out.println(rb.geefTekst("spel_menu_fout"));
            } finally {
                scan.nextLine();
            }
            if (spelKeuze > 0 && spelKeuze < 5) {
                isValid = true;
                switch (spelKeuze) {
                    case 1:
                        doSelecteerSpelNaamMenu();
                        break;
                    case 2:
                        doConfigureerSpelMenu();
                        break;
                    case 3:
                        doWijzigSpelMenu();
                        break;
                    case 4:
                        doLoginMenu();
                        break;
                }
            } else {
                System.out.println(rb.geefTekst("spel_menu_fout"));
            }
        }
    }

    public void doSelecteerSpelNaamMenu() {
        boolean isValid = false;
        while (!isValid) {
            System.out.println(rb.geefTekst("selecteer_spel_keuze"));
            String[] spelNamen = domCon.startSpelSpelen();
            for (int i = 0; i < spelNamen.length; i++) {
                System.out.println((i + 1) + ". " + spelNamen[i]);
            }
            System.out.println((spelNamen.length + 1) + ". " + rb.geefTekst("terug"));
            int spelNaamKeuze = -1;
            try {
                spelNaamKeuze = scan.nextInt();
            } catch (InputMismatchException e) {
                System.out.println(rb.geefTekst("selecteer_spel_fout"));
            } finally {
                scan.nextLine();
            }
            if (spelNaamKeuze > 0 && spelNaamKeuze <= spelNamen.length + 1) {
                if (spelNaamKeuze == spelNamen.length + 1) {
                    doSpelMenu();
                    return;
                }
                try {
                    domCon.speelSpel(spelNamen[spelNaamKeuze - 1]);
                    isValid = true;
                } catch (OnbestaandSpelException e) {
                    System.out.println(rb.geefTekst("selecteer_spel_onbestaand"));
                }
            } else {
                System.out.println(rb.geefTekst("selecteer_spel_fout"));
            }
        }
        doSpeelSpel();
    }

    public void doConfigureerSpelMenu() {
        try {
            domCon.startConfigurerenSpel();
        } catch (GeenToegangException ex) {
            System.out.println(rb.geefTekst(ex.getMessage()));
            doSpelMenu();
            return;
        }
        System.out.println(rb.geefTekst("configureer_spel"));
        boolean isValidNaamKeuze = false;
        String naamKeuze = "";
        while (!isValidNaamKeuze) {
            System.out.println(rb.geefTekst("configureer_naam"));
            naamKeuze = scan.nextLine();
            if (naamKeuze != null && naamKeuze.equalsIgnoreCase("1")) {
                doSpelMenu();
                return;
            }
            try {
                domCon.maakNieuwSpel(naamKeuze);
                isValidNaamKeuze = true;
            } catch (BestaandSpelException ex) {
                System.out.printf(rb.geefTekst("configureer_spel_naam_gebruikt"));
            }
        }

        try {
            domCon.startMakenNieuwSpelbord();
        } catch (OngeldigSpelbordException e) {
            // ignore
        }
        System.out.printf(rb.geefTekst("configureer_spel_naam") + " " + naamKeuze + "%n");
        beginPasSpelbordAanLoop(naamKeuze);
    }

    public void beginPasSpelbordAanLoop(String naamKeuze) {
        String[][] velden = domCon.geefSpelbordInOpbouwVelden();
        for (int y = 0; y < velden.length; y++) {
            String[] rij = velden[y];
            for (int x = 0; x < rij.length; x++) {
                System.out.print(rij[x] + " ");
            }
            System.out.println("");
        }
        int vraagKeuze = -1;
        boolean isValid = false;
        while (!isValid) {
            System.out.printf(rb.geefTekst("configureer_spel_aanpaskeuze"));
            try {
                vraagKeuze = scan.nextInt();
            } catch (InputMismatchException e) {
                // ignore
            } finally {
                scan.nextLine();
            }
            if (vraagKeuze > 0 && vraagKeuze <= 3) {
                isValid = true;
                if (vraagKeuze == 1) {
                    doWijzigSpelvakVanSpelbord(-1, naamKeuze, true);
                } else if (vraagKeuze == 2) {
                    System.out.printf(rb.geefTekst("configureer_spel_naam") + " " + naamKeuze + "%n");
                    try {
                        domCon.startMakenNieuwSpelbord();
                        beginPasSpelbordAanLoop(naamKeuze);
                    } catch (OngeldigSpelbordException e) {
                        System.out.println(rb.geefTekst(e.getMessage()));
                        beginPasSpelbordAanLoop(naamKeuze);
                        return;
                    }
                } else if (vraagKeuze == 3) {
                    doEindeConfiguratieSpel(naamKeuze);
                }
            } else {
                System.out.println(rb.geefTekst("configureer_spel_fout"));
            }
        }
    }

    public void doEindeConfiguratieSpel(String naamSpel) {
        int saveKeuze = -1;
        int menuKeuze = -1;
        boolean isValid = false;
        boolean isValid2 = false;

        System.out.printf(rb.geefTekst("configureer_spel_info").replace("{SPELNAAM}", naamSpel));
        String[] result = domCon.geefSpelinOpbouwInfo();
        for (int y = 0; y < result.length; y++) {
            System.out.println(result[y] + " ");
        }
        while (!isValid) {
            System.out.printf(rb.geefTekst("configureer_spel_opslaankeuze").replace("{SPELNAAM}", naamSpel));
            try {
                saveKeuze = scan.nextInt();
            } catch (InputMismatchException e) {
                // ignore
            } finally {
                scan.nextLine();
            }
            if (saveKeuze > 0 && saveKeuze < 3) {
                isValid = true;
                if (saveKeuze == 1) {
                    domCon.slaNieuwSpelOp();
                    System.out.printf(rb.geefTekst("configureer_spel_opgeslagen").replace("{SPELNAAM}", naamSpel));
                } else {
                    System.out.printf(rb.geefTekst("configureer_spel_niet_opgeslagen").replace("{SPELNAAM}", naamSpel));

                }
            } else {
                System.out.println(rb.geefTekst("configureer_spel_keuze_fout"));
            }
        }

        while (!isValid2) {
            System.out.printf(rb.geefTekst("configureer_spel_terug_menu"));
            try {
                menuKeuze = scan.nextInt();
            } catch (InputMismatchException e) {
                // ignore
            } finally {
                scan.nextLine();
            }
            if (menuKeuze > 0 && menuKeuze < 3) {
                isValid2 = true;
                if (menuKeuze == 1) {
                    doSpelMenu();
                } else {
                    doStop();
                }
            } else {
                System.out.println(rb.geefTekst("configureer_spel_terug_menu"));
            }
        }
    }

    public void doWijzigSpelvakVanSpelbord(int spelbordLevel, String naamKeuze, boolean isNieuwSpelbord) {
        int xPos = -1;
        int yPos = -1;
        int vakKeuze = -1;

        boolean isValid2 = false;
        boolean isValid3 = false;
        boolean isValid4 = false;

        while (!isValid2) {
            System.out.printf(rb.geefTekst("configureer_spel_aanpassen"));

            try {
                vakKeuze = scan.nextInt();
            } catch (InputMismatchException e) {
                //ignore
            } finally {
                scan.nextLine();
            }
            if (vakKeuze > 0 && vakKeuze < 6) {

                isValid2 = true;

                while (!isValid3) {
                    System.out.printf(rb.geefTekst("configureer_spel_x"));
                    try {

                        xPos = scan.nextInt();

                    } catch (InputMismatchException e) {
                        // ignore

                    } finally {
                        scan.nextLine();
                    }
                    if (xPos > 0 && xPos < 11) {
                        isValid3 = true;
                        xPos--;
                        while (!isValid4) {

                            System.out.printf(rb.geefTekst("configureer_spel_y"));
                            try {
                                yPos = scan.nextInt();

                            } catch (InputMismatchException e) {
                                // ignore
                            } finally {
                                scan.nextLine();
                            }
                            if (yPos > 0 && yPos < 11) {
                                isValid4 = true;
                                yPos--;
                                if (isNieuwSpelbord) {
                                    domCon.zetSpelbordItem(xPos, yPos, vakKeuze);
                                    beginPasSpelbordAanLoop(naamKeuze);
                                } else {
                                    domCon.veranderSpelBordItem(xPos, yPos, vakKeuze);
                                    doWijzigSpelbord(spelbordLevel);
                                }

                            } else {
                                System.out.println(rb.geefTekst("configureer_spel_keuzefout_10"));
                            }
                        }
                    } else {
                        System.out.println(rb.geefTekst("configureer_spel_keuzefout_10"));
                    }
                }
            } else {
                System.out.println(rb.geefTekst("configureer_spel_keuzefout_5"));
            }
        }
    }

    public void doSpeelSpel() {
        domCon.speelVolgendSpelbord();
        boolean isSpelVoltooid = domCon.isActiefSpelVoltooid();
        while (!isSpelVoltooid) {
            boolean isSpelBordVoltooid = domCon.isSpelbordVoltooid();
            while (!isSpelBordVoltooid) {
                System.out.println(rb.geefTekst("speel_spel_legende"));
                String[][] velden = domCon.geefSpelBordVelden();
                for (int y = 0; y < velden.length; y++) {
                    String[] rij = velden[y];
                    for (int x = 0; x < rij.length; x++) {
                        System.out.print(rij[x] + " ");
                    }
                    System.out.println("");
                }
                System.out.println(rb.geefTekst("aantal_stappen_gezet").replace("{STAPPEN}", String.valueOf(domCon.geefAantalStappenInActiefSpelbord())));
                System.out.println(rb.geefTekst("speel_spel_verplaats"));
                String richting = scan.nextLine();
                if (richting.equalsIgnoreCase(rb.geefTekst("speel_spel_verplaats_b"))) {
                    domCon.verplaatsVentje(rb.geefTekst("speel_spel_verplaats_boven"));
                } else if (richting.equalsIgnoreCase(rb.geefTekst("speel_spel_verplaats_r"))) {
                    domCon.verplaatsVentje(rb.geefTekst("speel_spel_verplaats_rechts"));
                } else if (richting.equalsIgnoreCase(rb.geefTekst("speel_spel_verplaats_o"))) {
                    domCon.verplaatsVentje(rb.geefTekst("speel_spel_verplaats_onder"));
                } else if (richting.equalsIgnoreCase(rb.geefTekst("speel_spel_verplaats_l"))) {
                    domCon.verplaatsVentje(rb.geefTekst("speel_spel_verplaats_links"));
                } else if (richting.equalsIgnoreCase(rb.geefTekst("speel_spel_stoppen"))) {
                    doStop();
                } else if (richting.equalsIgnoreCase("1")) {
                    domCon.resetActiefSpelbord();
                } else if (richting.equalsIgnoreCase("2")) {
                    doSpelMenu();
                } else {
                    System.out.println(rb.geefTekst("selecteer_spel_fout") + "\n");
                }
                isSpelBordVoltooid = domCon.isSpelbordVoltooid();
            }
            domCon.speelVolgendSpelbord();
            int[] resultaten = domCon.geefSpelbordResultaat();
            System.out.println(rb.geefTekst("speel_spel_resultaat")
                    .replace("{WINS}", String.valueOf(resultaten[0]))
                    .replace("{TOTAAL}", String.valueOf(resultaten[1])));
            isSpelVoltooid = domCon.isActiefSpelVoltooid();
        }
        domCon.resetActiefspel();
        doSpelMenu();
    }

    public void doMeldAan() {
        boolean isValid = false;
        String gebruikersNaam;
        String wachtwoord;
        while (!isValid) {
            System.out.println(rb.geefTekst("meld_aan_gebruikersnaam") + "( 1. " + rb.geefTekst("terug") + " )");
            gebruikersNaam = scan.nextLine();
            if (gebruikersNaam.equals("1")) {
                doLoginMenu();
                return;
            }
            System.out.println(rb.geefTekst("meld_aan_wachtwoord") + "( 1. " + rb.geefTekst("terug") + " )");
            wachtwoord = scan.nextLine();
            if (wachtwoord.equals("1")) {
                doLoginMenu();
                return;
            }
            domCon.meldAan(gebruikersNaam, wachtwoord);
            try {
                this.aanmeldResultaten = domCon.geefAanmeldResultaten();
                isValid = true;
            } catch (OngekendeSpelerExeption e) {
                System.out.println(rb.geefTekst("meld_aan_fout"));
            }
        }

        doToonVerwelkoming();
        doSpelMenu();
    }

    public void doRegistreer() {
        String regNaam;
        String regVoornaam;
        String regGebruikersnaam;
        String regWachtwoord;

        System.out.println(rb.geefTekst("registreer_formulier"));
        boolean isValid = false;
        while (!isValid) {
            System.out.println("( 1. " + rb.geefTekst("terug") + " )");
            System.out.println(rb.geefTekst("registreer_naam"));
            regNaam = scan.nextLine();
            if (regNaam.equals("1")) {
                doLoginMenu();
                return;
            }
            System.out.println(rb.geefTekst("registreer_voornaam"));
            regVoornaam = scan.nextLine();
            if (regVoornaam.equals("1")) {
                doLoginMenu();
                return;
            }
            System.out.println(rb.geefTekst("registreer_gebruikersnaam"));
            regGebruikersnaam = scan.nextLine();
            if (regGebruikersnaam.equals("1")) {
                doLoginMenu();
                return;
            }
            System.out.println(rb.geefTekst("registreer_wachtwoord"));
            regWachtwoord = scan.nextLine();
            if (regWachtwoord.equals("1")) {
                doLoginMenu();
                return;
            }
            try {
                domCon.registreer(regNaam, regGebruikersnaam, regVoornaam, regWachtwoord);
                this.aanmeldResultaten = domCon.geefAanmeldResultaten();
                isValid = true;
            } catch (IllegalArgumentException e) {
                System.out.println(rb.geefTekst("registreer_fout_incorrect")
                        + e.getMessage());
            } catch (GebruikersnaamAlGebruiktException e) {
                System.out.println(rb.geefTekst("registreer_fout_gebruikersnaam"));
            } catch (OngekendeSpelerExeption e) {
                // kan niet voorkomen
            }
        }

        doToonVerwelkoming();
        doSpelMenu();
    }

    public void doStop() {
        System.out.println(rb.geefTekst("stop_afsluiter"));
        System.exit(0);
        throw new RuntimeException(rb.geefTekst("stop_stoppen"));
    }

    public void doToonVerwelkoming() {
        System.out.println(rb.geefTekst("verwelkoming_welkom") + " " + this.aanmeldResultaten[0]);
    }

    // ** WIJZIG SPEL STUFF ** //
    public void doWijzigSpelMenu() {
        System.out.println(rb.geefTekst("wijzig_spelkeuze"));
        boolean isValid = false;
        int choice = -1;
        String[] spelNamen;
        try {
            spelNamen = domCon.startWijzigspel();
        } catch (GeenToegangException e) {
            System.out.println(rb.geefTekst(e.getMessage()));
            doSpelMenu();
            return;
        }
        int l = spelNamen.length;
        while (!isValid) {
            for (int i = 0; i < l; i++) {
                System.out.println((i + 1) + rb.geefTekst("wijzig_spel") + " " + spelNamen[i]);
            }
            System.out.println((l + 1) + rb.geefTekst("wijzig_terug_hoofdmenu"));
            String input = scan.nextLine();
            try {
                choice = Integer.parseInt(input);
                isValid = (choice > 0 && choice <= l + 1);
            } catch (NumberFormatException e) {
                isValid = false;
            }
            if (!isValid) {
                System.out.println(rb.geefTekst("wijzig_spelkeuze_fout"));
            }
        }
        if (choice == l + 1) {
            doSpelMenu();
        } else {
            try {
                doKiesSpelbordOmTeWijzigen(spelNamen[choice - 1], domCon.kiesSpel(spelNamen[choice - 1]));
            } catch (OnbestaandSpelException e) {
                System.out.println("This will never happen.");
                doWijzigSpelMenu();
            }
        }
    }

    public void doKiesSpelbordOmTeWijzigen(String spelNaam, int aantalSpelborden) {
        System.out.println(rb.geefTekst("wijzig_spelbordkeuze").replace("{SPELNAAM}", spelNaam));
        boolean isValid = false;
        int choice = -1;
        while (!isValid) {
            for (int i = 0; i < aantalSpelborden; i++) {
                System.out.println((i + 1) + rb.geefTekst("wijzig_spelbord") + " " + (i + 1));
            }
            System.out.println((aantalSpelborden + 1) + rb.geefTekst("wijzig_terug_spelkeuze"));
            String input = scan.nextLine();
            try {
                choice = Integer.parseInt(input);
                isValid = (choice > 0 && choice <= aantalSpelborden + 1);
            } catch (NumberFormatException e) {
                isValid = false;
            }
            if (!isValid) {
                System.out.println(rb.geefTekst("wijzig_spelkeuze_fout"));
            }
        }
        if (choice == aantalSpelborden + 1) {
            doWijzigSpelMenu();
        } else {
            doWijzigSpelbord(choice - 1);
        }
    }

    public void doWijzigSpelbord(int spelbordLevel) {

        boolean isValid = false;
        int choice = -1;

        while (!isValid) {
            String[][] velden = domCon.kiesSpelBord(spelbordLevel);
            for (int y = 0; y < velden.length; y++) {
                String[] rij = velden[y];
                for (int x = 0; x < rij.length; x++) {
                    System.out.print(rij[x] + " ");
                }
                System.out.println("");
            }
            System.out.println(rb.geefTekst("wijzig_wijzig"));
            System.out.println(rb.geefTekst("wijzig_verwijder"));
            System.out.println(rb.geefTekst("wijzig_opslaan"));
            System.out.println(rb.geefTekst("wijzig_nietopslaan"));
            String input = scan.nextLine();
            try {
                choice = Integer.parseInt(input);
                isValid = (choice > 0 && choice <= 4);
            } catch (NumberFormatException e) {
                isValid = false;
            }
            if (!isValid) {
                System.out.println(rb.geefTekst("wijzig_spelkeuze_fout"));
            }
        }
        if (choice == 1) {
            doWijzigSpelvakVanSpelbord(spelbordLevel, null, false);
        } else if (choice == 2) {
            domCon.verwijderActiefSpelbord();
            doWijzigSpelMenu();
        } else {
            if (choice == 3) {
                domCon.slaActiefSpelBordOp();
            }
            domCon.resetActiefspel();
            doWijzigSpelMenu();
        }

    }
    // ** EINDE WIJZIG SPEL STUFF ** //
}

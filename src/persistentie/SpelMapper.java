package persistentie;

import domein.Spel;
import domein.SpelBord;
import domein.SpelCreator;
import excepties.GebruikersnaamAlGebruiktException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SpelMapper {

    /**
     * <p>
     * Geeft al de spelnamen van de spellen die opgeslagen zijn in de
     * database.</p>
     *
     * @return Alle spelnamen van de spellen die opgeslagen zijn in de database.
     */
    public List<Spel> geefAlleSpellen() {
        List<Spel> result = new ArrayList<>();

        Connection conn = null;
        PreparedStatement query = null;
        try {
            conn = DriverManager.getConnection(Connectie.JDBC_URL);

            query = conn.prepareStatement("SELECT * FROM spel;");
            ResultSet rSet = query.executeQuery();
            while (rSet.next()) {
                String spelNaam = rSet.getString("naam");
                int aantalTotaalSpelborden = geefAantalSpelborden(spelNaam);
                result.add(new Spel(spelNaam, false, aantalTotaalSpelborden));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (query != null) {
                    query.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                // ignore
            }
        }

        return result;
    }

    /**
     * <p>
     * Geeft terug hoeveel spelborden van een gegeven {@link domein.Spel} zijn
     * opgeslagen in de database.</p>
     *
     * @param spelNaam Naam van het {@link domein.Spel} waarvan het aantal
     * spelborden geweten wil worden.
     * @return Aantal spelborden die reeds opgeslagen zijn in de database.
     */
    public int geefAantalSpelborden(String spelNaam) {
        int result = 0;

        Connection conn = null;
        PreparedStatement query = null;
        try {
            conn = DriverManager.getConnection(Connectie.JDBC_URL);

            query = conn.prepareStatement("SELECT COUNT(*) as count FROM spelbord WHERE spelNaam LIKE BINARY ?;");
            query.setString(1, spelNaam);

            ResultSet rSet = query.executeQuery();
            if (rSet.next()) {
                result = rSet.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (query != null) {
                    query.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                // ignore
            }
        }
        return result;
    }

    /**
     * <p>
     * Slaat een gegeven spel op in de database.</p>
     *
     * @param spel
     */
    public void slaSpelOp(SpelCreator spel) {
        Connection conn = null;
        PreparedStatement query = null;
        try {
            conn = DriverManager.getConnection(Connectie.JDBC_URL);
            query = conn.prepareStatement("INSERT INTO spel(naam) VALUES(?)");
            query.setString(1, spel.getSpelNaam());
            query.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (query != null) {
                    query.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                // ignore
            }
        }
        SpelBordMapper sbMap = new SpelBordMapper();
        for (SpelBord bord : spel.getSpelborden()) {
            sbMap.slaSpelbordOp(bord, spel.getSpelNaam());
        }
    }
    
    /**
     * <p>Verwijdert een {@link domein.Spel} zonder spelborden uit de database.</p>
     * @param teVerwijderenSpelnaam Naam van het te verwijderen {@link domein.Spel}.
     */

    public void verwijderLeegSpel(String teVerwijderenSpelnaam) {
        int totaalAantalspelborden = geefAantalSpelborden(teVerwijderenSpelnaam);
        if (totaalAantalspelborden == 0) {
            Connection conn = null;
            PreparedStatement query = null;
            try {
                conn = DriverManager.getConnection(Connectie.JDBC_URL);
                query = conn.prepareStatement("DELETE FROM spel WHERE naam LIKE BINARY ? ");
                query.setString(1, teVerwijderenSpelnaam);
                query.execute();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (query != null) {
                        query.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }
}

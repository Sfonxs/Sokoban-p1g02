package persistentie;

import domein.Speler;
import excepties.GebruikersnaamAlGebruiktException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SpelerMapper {

    /**
     *<p>Voegt een nieuwe {@link domein.Speler} toe aan de database.</p>
     * @param speler De nieuwe speler die moet worden opgeslaan in de database.
     * @throws excepties.GebruikersnaamAlGebruiktException Indien de nieuwe {@link domein.Speler} zijn gebruikersnaam reeds bestaat in de database.
     */
    public void voegToe(Speler speler) throws GebruikersnaamAlGebruiktException{
        Connection conn = null;
        PreparedStatement query = null;
        try {
            conn = DriverManager.getConnection(Connectie.JDBC_URL);

            query = conn.prepareStatement(
                    "INSERT INTO speler(gebruikersnaam, wachtwoord, isAdmin, naam, voornaam) "
                    + "VALUES(?,?,?,?,?);");
            query.setString(1, speler.getGebruikersNaam());
            query.setString(2, speler.getWachtwoord());
            query.setBoolean(3, speler.isAdmin());
            query.setString(4, speler.getNaam());
            query.setString(5, speler.getVoornaam());

            query.executeUpdate();
        } catch (SQLException e) {
            // Duplicate key error code: 1062
            if (e.getErrorCode() == 1062){
                throw new GebruikersnaamAlGebruiktException("De gebruikersnaam is al in gebruikt.");
            }
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
    /**
     * <p>Geeft een {@link domein.Speler} terug uit de databse.</p>
     * @param gebruikersnaam De gebruikersnaam die zal worden gezocht in de database.
     * @param wachtwoord Het wachtwoord dat nodig is om te verifiëren dat die gebreuker effectief die persoon is.
     * @return De {@link domein.Speler} indien de gebruikersnaam bestaat in de database en het wachtwoord overeenstemt met die gebruikersnaam.
     */

    public Speler geefSpeler(String gebruikersnaam, String wachtwoord) {
        Speler speler = null;
        Connection conn = null;
        PreparedStatement query = null;
        try {
            conn = DriverManager.getConnection(Connectie.JDBC_URL);
            query = conn.prepareStatement("SELECT isAdmin, naam, voornaam FROM speler WHERE gebruikersnaam LIKE BINARY ? AND wachtwoord LIKE BINARY ?");
            
            query.setString(1, gebruikersnaam);
            query.setString(2, wachtwoord);
            
            ResultSet resultSet = query.executeQuery();
            
            if (resultSet.next()){
                boolean isAdmin = resultSet.getBoolean("isAdmin");
                String naam = resultSet.getString("naam");
                String voornaam = resultSet.getString("voornaam");
                speler = new Speler(gebruikersnaam, wachtwoord, isAdmin, naam, voornaam);
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
        return speler;
    }

}

package persistentie;

import domein.MuurVak;
import domein.SpelBord;
import domein.Speler;
import domein.Vak;
import domein.VeldVak;
import domein.Ventje;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpelBordMapper {

    public SpelBord geefSpelBord(int level, String spelNaam) {
        SpelBord spelBord = null;
        Connection conn = null;
        PreparedStatement query = null;
        try {
            conn = DriverManager.getConnection(Connectie.JDBC_URL);
            query = conn.prepareStatement("SELECT x,y,soort FROM vak WHERE level = ? AND spelNaam LIKE BINARY ?");

            query.setInt(1, level);
            query.setString(2, spelNaam);
            ResultSet resultSet = query.executeQuery();

            Vak[][] vakkenArr = new Vak[10][];
            int xVentje = 5;
            int yVentje = 5;
            while (resultSet.next()) {
                int x = resultSet.getInt("x");
                int y = resultSet.getInt("y");
                String soort = resultSet.getString("soort");

                if (x >= 0 && x < 10 && y >= 0 && y < 10) {
                    if (vakkenArr[y] == null) {
                        vakkenArr[y] = new Vak[10];
                    }
                    Vak vak = null;
                    if (soort.equals("muur")) {
                        vak = new MuurVak(x, y);
                    } else if (soort.equals("kist")) {
                        vak = new VeldVak(x, y, false, true);
                    } else if (soort.equals("veld")) {
                        vak = new VeldVak(x, y, false, false);
                    } else if (soort.equals("doel")) {
                        vak = new VeldVak(x, y, true, false);
                    }else if (soort.equals("doelkist")){                        
                        vak = new VeldVak(x, y, true, true);
                    } else if (soort.equals("ventje")) {
                        vak = new VeldVak(x, y, false, false);
                        xVentje = x;
                        yVentje = y;
                    }
                    vakkenArr[y][x] = vak;
                }
            }
            for (int y = 0; y < 10; y++) {
                if (vakkenArr[y] == null) {
                    vakkenArr[y] = new Vak[10];
                }
                for (int x = 0; x < 10; x++) {
                    if (vakkenArr[y][x] == null) {
                        vakkenArr[y][x] = new VeldVak(x, y, false, false);
                    }
                }
            }
            spelBord = new SpelBord(vakkenArr, false, level, new Ventje(xVentje, yVentje));
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
        return spelBord;
    }
/**
 * <p>Slaat het {@link domein.SpelBord} op in de database.</p>
 * @param spelbord Het {@link domein.SpelBord} dat opgeslagen wenst te worden.
 * @param spelNaam Het {@link domein.Spel} waaruit het {@link domein.SpelBord} opgeslagen wenst te worden.
 */
    public void slaSpelbordOp(SpelBord spelbord, String spelNaam) {
        Connection conn = null;
        PreparedStatement query = null;
        try {
            conn = DriverManager.getConnection(Connectie.JDBC_URL);
            query = conn.prepareStatement("REPLACE INTO spelbord(spelNaam,level) VALUES(?,?)");
            query.setString(1, spelNaam);
            query.setInt(2, spelbord.getLevelNummer());
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
        try {
            conn = DriverManager.getConnection(Connectie.JDBC_URL);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        Vak[][] vakkenArr = spelbord.getVakken();
        Ventje ventje = spelbord.getVentje();
        StringBuilder insertQueryB = new StringBuilder("REPLACE INTO vak(spelNaam,level,y,x,soort) VALUES");
        int levelNummer = spelbord.getLevelNummer();
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Vak vak = vakkenArr[y][x];
                String soort = "veld";
                if (x == ventje.getX() && y == ventje.getY()) {
                    soort = "ventje";
                } else if (vak instanceof MuurVak) {
                    soort = "muur";
                } else if (vak instanceof VeldVak) {
                    VeldVak vVak = (VeldVak) vak;
                    if (vVak.isKist() && !vVak.isDoel()) {
                        soort = "kist";
                    } else if (vVak.isDoel() && !vVak.isKist()) {
                        soort = "doel";
                    } else if (vVak.isDoel() && vVak.isKist()){
                        soort = "doelkist";
                    }else {
                        soort = "veld";
                    }
                }
                insertQueryB.append(" ('")
                        .append(spelNaam)
                        .append("',")
                        .append(levelNummer)
                        .append(",")
                        .append(y)
                        .append(",")
                        .append(x)
                        .append(",'")
                        .append(soort)
                        .append("')");
                if (y != 9 || x != 9) {
                    insertQueryB.append(",");
                }
            }
        }
        try {
            query = conn.prepareStatement(insertQueryB.toString());
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
    
    /**
     * <p>Verwijdert een gekozen {@link domein.SpelBord} uit de database. Indien een {@link domein.Spel} maar 1 {@link domein.SpelBord} heeft, en dus leeg zal 
     * zal worden wanneer het {@link domein.SpelBord} verwijderd wordt, wordt het {@link domein.Spel} verwijderd uit de databse via de {@link persistentie.SpelMapper}. </p>
     * @param level {@link domein.SpelBord dat verwijdert dient te worden.}
     * @param spelNaam Naam van het {@link domein.Spel} waaruit het {@link domein.SpelBord} verwijdert moet worden.
     */

    public void verwijderSpelbord(int level, String spelNaam) {
        int totaalAantalspelborden = new SpelMapper().geefAantalSpelborden(spelNaam);
        if (level >= totaalAantalspelborden) {
            throw new RuntimeException("er is geen level " + level + " in het spel met naam " + spelNaam);
        }
        Connection conn = null;
        PreparedStatement query = null;
        try {
            conn = DriverManager.getConnection(Connectie.JDBC_URL);
            query = conn.prepareStatement("DELETE FROM vak WHERE level = ? AND spelNaam LIKE BINARY ?");
            query.setInt(1, level);
            query.setString(2, spelNaam);
            query.execute();
            query = conn.prepareStatement("DELETE FROM spelbord WHERE level = ? AND spelNaam LIKE BINARY ?");
            query.setInt(1, level);
            query.setString(2, spelNaam);
            query.execute();
            for (int levelToLower = level + 1; levelToLower < totaalAantalspelborden; levelToLower++) {
                SpelBord spelbord = geefSpelBord(levelToLower, spelNaam);
                spelbord.setLevelNummer(levelToLower - 1);
                query = conn.prepareStatement("DELETE FROM vak WHERE level = ? AND spelNaam LIKE BINARY ?");
                query.setInt(1, levelToLower);
                query.setString(2, spelNaam);
                query.execute();
                query = conn.prepareStatement("DELETE FROM spelbord WHERE level = ? AND spelNaam LIKE BINARY ?");
                query.setInt(1, levelToLower);
                query.setString(2, spelNaam);
                query.execute();
                slaSpelbordOp(spelbord, spelNaam);
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
        new SpelMapper().verwijderLeegSpel(spelNaam);
    }

}

package resources;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Klasse van ResourceBundel

 */
public class ResourceBundel
{
    private static Locale deTaal;
    
    /**
     * 
     * @param keynaam
     * @return  
     */
    public String geefTekst(String keynaam)
    {
        ResourceBundle bundle = ResourceBundle.getBundle("resources/Messages", deTaal);
        return bundle.getString(keynaam);
    }
    
    /**
     *
     * @param taal 
     */
    public static void registreerTaal(String taal)
    {
        switch(taal){
            case "NL":
            case "nl": deTaal=new Locale("nl");
                break;
            case "EN":
            case "en": deTaal=new Locale("en");
                break;
            case "FR":
            case "fr": deTaal=new Locale("fr");
                break;
            default: deTaal=new Locale("nl");
        }
        
        deTaal=new Locale(taal.toLowerCase(),taal.toUpperCase());

    }
}
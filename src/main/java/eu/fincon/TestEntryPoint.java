package eu.fincon;

import com.relevantcodes.extentreports.LogStatus;
import eu.fincon.Datenverarbeitung.Config;
import eu.fincon.Datenverarbeitung.Datentreiber;
import eu.fincon.Datenverarbeitung.Testdatum;
import eu.fincon.Datenverarbeitung.Webseite;
import eu.fincon.Logging.ExtendetLogger;
import org.testng.annotations.Test;

import java.util.List;
import java.util.logging.Level;

public class TestEntryPoint {
    public static void runTest() {
        // Eine Liste (Typ Testdatum) wird aus der übergebenen Datei erstellt
        System.out.println("before logger");
        ExtendetLogger.setup("Vakanzengrabber");
        Config.init();
        System.out.println("after logger init");
        List<Testdatum> lTestdatumListe = Datentreiber.getTestdatenEXCEL();
        ExtendetLogger.LogEntry(LogStatus.INFO,"Es werden "+Config.strarrayWebseitenListe.length+" Webseiten geprüft");
        System.out.println("after logger info");
        ExtendetLogger.LogEntry(LogStatus.INFO, "say smth");
        ExtendetLogger.LogEntry(LogStatus.INFO,"Es werden "+lTestdatumListe.size()+" Testdaten pro Seite geprüft");

        // Es wird eine Schleif über alle Einträge des Testdatentreibers gelaufen
        System.out.println("config liste: " + Config.strarrayWebseitenListe.length);
        for (Webseite wWebseite : Config.strarrayWebseitenListe) {
            System.out.println("in der for schleife");
            for (Testdatum tTestdatum : lTestdatumListe) {
                ExtendetLogger.CreateChild(wWebseite.eSeite.toString() + "_" + tTestdatum.strSuchbegriff);
                ExtendetLogger.LogEntry(LogStatus.INFO,"Webseite - "+wWebseite.strURL+
                        " mit dem Suchbegriff -"+tTestdatum.strSuchbegriff+" wird ausgeführt...");

                wWebseite.VakanzenObject.browserVorbereiten();
                wWebseite.VakanzenObject.seiteOeffnen();
                wWebseite.VakanzenObject.benutzerAnmelden();
                wWebseite.VakanzenObject.sucheDurchfuehren(tTestdatum);
                wWebseite.VakanzenObject.suchlisteSichern();
                wWebseite.VakanzenObject.seiteSchließen();
                ExtendetLogger.AppendChild();
            }
            break;
        }
    }
}

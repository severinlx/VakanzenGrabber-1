package eu.fincon;

import com.relevantcodes.extentreports.LogStatus;
import eu.fincon.Datenverarbeitung.Config;
import eu.fincon.Datenverarbeitung.Datentreiber;
import eu.fincon.Datenverarbeitung.Testdatum;
import eu.fincon.Datenverarbeitung.Webseite;
import eu.fincon.Logging.ExtendetLogger;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
                System.out.println("nach der suchliSte sichern in TestEntrypoint");
                wWebseite.VakanzenObject.seiteSchließen();
                ExtendetLogger.AppendChild();
            }
            break;
        }
    }
    public static void persistResults() {
        try {
            Files.copy(
                    new File(System.getProperty("user.dir") + "/"+Config.strDatabaseName).toPath(),
                    new File(System.getProperty("user.dir") + "/efs/data.db").toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void showFiles(String s) {

        // Creates an array in which we will store the names of files and directories
        String[] pathnames;

        File f = new File(s);

        // Populates the array with names of files and directories
        pathnames = f.list();

        // For each pathname in the pathnames array
        for (String pathname : pathnames) {
            // Print the names of files and directories
            System.out.println(pathname);
        }
    }

    public static void writeToFile() {
        try {
            File myObj = new File("./efs/filename.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

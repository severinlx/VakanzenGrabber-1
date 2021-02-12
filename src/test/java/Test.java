import eu.fincon.Datenverarbeitung.Config;
import eu.fincon.Logging.ExtendetLogger;

public class Test {
    //=====================================================================
    // Test Methode
    // =====================================================================
    @org.testng.annotations.Test // Ausführbarkeit von der Funktion als Test
    public void Test() throws InterruptedException {
        // Da wir keine Argumente (bisher) erwarten, wird ein leeres String Array übereben
        ExtendetLogger.setup("Vakanzengrabber");
        Config.init();
        eu.fincon.TestEntryPoint.runTest();
    }
}

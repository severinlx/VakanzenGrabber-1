package eu.fincon.Datenverarbeitung;

import com.relevantcodes.extentreports.LogStatus;
import eu.fincon.Logging.ExtendetLogger;
import org.jetbrains.annotations.NotNull;

import java.io.*;
// SQL Imports
import java.sql.*;
// Time Imports
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
// List Import
import java.util.List;

public class InserateVerwalten {
    public enum SpeicherTypen{
        csv,
        txt,
        sqllite
    }
    //=====================================================================
    // Der Text des Elementes wird versucht auszulesen
    // =====================================================================
    public String ExportPfad = "export";
    List<Inserat> lInserate;

    // Database Functions
    private Connection connectToSQLLiteDatabase()
    {
        // SQLite connection string
        String url = Config.strDatabasePfad + Config.strDatabaseName;
        System.out.println("url: " + url);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("after set connection");
        } catch (SQLException e) {
            ExtendetLogger.LogEntry(LogStatus.ERROR, "Failed to connect to Database - " + url);
        }
        return conn;
    }
    private void dropExistingTable(Connection conn, String pstrTabellenName)
    {
        String sql = "DROP TABLE IF EXISTS " + pstrTabellenName;
        try
        {
            Statement stmt = conn.createStatement();
            // create a new table
            stmt.execute(sql);
            ExtendetLogger.LogEntry(LogStatus.INFO, sql);
            conn.commit();
        } catch (SQLException e) {
            ExtendetLogger.LogEntry(LogStatus.ERROR, "Failed to Drop Table - " + sql);
            ExtendetLogger.LogEntry(LogStatus.ERROR, e.getMessage());
        }
    }
    private String createNewTable(Connection conn, String pstrTabellenName)
    {
        String url = Config.strDatabasePfad + Config.strDatabaseName;
        String strTabellenName = pstrTabellenName + LocalDateTime.now().format(DateTimeFormatter.ofPattern("_yyyy_MM_dd"));
        dropExistingTable(conn, strTabellenName);
        // SQL statement for creating a new table
        String sql = Inserat.getSQLiteCreateTable(strTabellenName);

        try
        {
             Statement stmt = conn.createStatement();
            // create a new table
            stmt.execute(sql);
            ExtendetLogger.LogEntry(LogStatus.INFO, sql);
            conn.commit();
        } catch (SQLException e) {
            ExtendetLogger.LogEntry(LogStatus.ERROR, "Failed to create Table - " + sql);
            ExtendetLogger.LogEntry(LogStatus.ERROR, e.getMessage());
        }
        return strTabellenName;
    }
    public void insertIntoSQLite(Inserat piInserat, Connection pConnection, String pstrTabellenname, int pintID) {
        System.out.println("insert into SQL Lite");
        PreparedStatement stmt = null;

        //String sql = "INSERT INTO "+ pstrTabellenname +" (" + Inserat.getSQLiteSpalten() + ") VALUES(" + piInserat.getInseratStringSQLite() + ")";

        String[] strsplittedValues = piInserat.getInseratStringSQLite().split("\",\"");
        String sql = getBaseInsertString(pstrTabellenname, strsplittedValues);
        String ausgabe = "";

        ExtendetLogger.LogEntry(LogStatus.INFO, "Creating SQL Insert Statement");
        try {
            stmt = pConnection.prepareStatement(sql);
        } catch (SQLException e) {
            ExtendetLogger.LogEntry(LogStatus.FATAL, "Error preparing Statement - " + sql);
            System.out.println("Error preparing Statement - " + sql);
            e.printStackTrace();
        }
        ExtendetLogger.LogEntry(LogStatus.INFO, "Setting Insert Values in Statement");
        for (int i=1; i<=strsplittedValues.length;i++)
        {
            ausgabe = "Replacing " + i + " with - " + strsplittedValues[i-1];
            System.out.println(ausgabe);
            ExtendetLogger.LogEntry(LogStatus.INFO, ausgabe);
            try {
                stmt.setString(i, strsplittedValues[i-1].trim());
            } catch (SQLException e) {
                ExtendetLogger.LogEntry(LogStatus.FATAL, "Error setting String # " + sql + " to " + strsplittedValues[i-1].trim());
                System.out.println("Error setting String # " + sql + " to " + strsplittedValues[i-1].trim());
                e.printStackTrace();
            }
        }

        try {
            ExtendetLogger.LogEntry(LogStatus.INFO, "Insert Statement - " + stmt.toString());
            System.out.println("after preparing insert statement");
            System.out.println("Insert Statement - " + stmt.toString());
            stmt.execute();
            ExtendetLogger.LogEntry(LogStatus.PASS, "Insert Statement Executed");

        } catch (SQLException e) {
            ExtendetLogger.LogEntry(LogStatus.ERROR, "Failed to Execute Insert Statement - " + sql);
            ExtendetLogger.LogEntry(LogStatus.ERROR, e.getMessage());
            e.printStackTrace();
        }

        try {
            stmt.close();
            pConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @NotNull
    private String getBaseInsertString(String pstrTabellenname, String[] strsplittedValues) {
        int intNumberOfValues = strsplittedValues.length;
        String strValuesPlaceHolder = "";
        for (int i=0;i<intNumberOfValues;i++)
        {
            if (i>0)
                strValuesPlaceHolder = strValuesPlaceHolder + ",?";
            else
                strValuesPlaceHolder = "?";
        }
        return "INSERT INTO "+ pstrTabellenname +" (" + Inserat.getSQLiteSpalten() + ") VALUES("+strValuesPlaceHolder+")";
    }

    // End Database Functions
    public InserateVerwalten(List<Inserat> piwInserate)
    {
        //=====================================================================
        // Die bei der Instanziierung übergebene Liste wird in der Klasse gesichert
        // =====================================================================
        lInserate = piwInserate;
    }
    public void inserateSichern(SpeicherTypen pstSpeicherTyp, String pstrTabellenName)
    {
        if (pstSpeicherTyp == SpeicherTypen.csv || pstSpeicherTyp == SpeicherTypen.txt)
            fileOutput(pstSpeicherTyp);
        else if (pstSpeicherTyp == SpeicherTypen.sqllite)
            sqliteOutput(pstrTabellenName);

    }
    private void sqliteOutput(String pstrTabellenName)
    {
        System.out.println("sqlOutput in Inserate Verwalten");
        Connection conn = this.connectToSQLLiteDatabase();
        try {
            // Damit wird die AutoCommit deaktiviert
            // Dieser führte dazu, dass mit jedem Statement ein Commit durchgeführt wurde - Was zu einer Exception geführt hat
            // Der Commit wird "manuell" im Code nach dem Statement ausgeführt
            conn.setAutoCommit(false);
            System.out.println("after set Autocommit");
        }
        catch (SQLException e)
        {
            System.out.println("exception in Autocommit");
            ExtendetLogger.LogEntry(LogStatus.INFO, "Failed to Set AutoCommitMode");
        }
        System.out.println("before create new table");
        pstrTabellenName = createNewTable(conn, pstrTabellenName);
        ExtendetLogger.LogEntry(LogStatus.INFO, "Inserate werden gesichert... ");
        int intIndex = 1;

        System.out.println("before insert loop");
        for (Inserat iInserat:lInserate) {
            ExtendetLogger.LogEntry(LogStatus.INFO, "Inserat sichern: " + iInserat.toString());

            System.out.println("before insert row");
            insertIntoSQLite(iInserat, conn, pstrTabellenName, intIndex);
            System.out.println("after insert row");
            intIndex++;
        }
    }
    private void fileOutput(SpeicherTypen pstSpeicherTyp) {
        OutputStream osExportFile = null;
        File fileExportFile = null;
        String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
        String strAbsoluterPfad = ExportPfad +"_"+ formattedDateTime +"." + pstSpeicherTyp.toString();
        ExtendetLogger.LogEntry(LogStatus.INFO, "Dateipfad für Sicherung = " + strAbsoluterPfad);

        try {
            fileExportFile = new File(strAbsoluterPfad);
            if (fileExportFile.exists()) {
                ExtendetLogger.LogEntry(LogStatus.FATAL, "Die Datei ist bereits vorhanden und wird vor dem Sichern entfernt. Speichertyp = " + pstSpeicherTyp.toString());
                fileExportFile.delete();
            }
            fileExportFile.createNewFile();
        }
        catch (Exception e) {
            ExtendetLogger.LogEntry(LogStatus.FATAL, "Die Export-Datei konnte nicht erzeugt werden");
            return;
        }

        try {
            osExportFile = new FileOutputStream(strAbsoluterPfad);
        } catch (Exception e) {
            ExtendetLogger.LogEntry(LogStatus.FATAL, "Exception in CSV schreiben  - " + e.getMessage());
        }
        inCSVDateiSchreiben (osExportFile, "#;"+ Inserat.getCSVSpalten());
        //=====================================================================
        // Es wird mittels Schleife über jeden Eintrag der Inserate Liste gegangen
        // =====================================================================
        int index = 1;
        for( Inserat i: lInserate )
        {
            switch (pstSpeicherTyp) {
                case csv:
                    ExtendetLogger.LogEntry(LogStatus.INFO, "Speichertyp wird verwendet -> " + pstSpeicherTyp.toString());
                    String strInseratStringCSV = i.getInseratStringCSV();
                    inCSVDateiSchreiben(osExportFile, Integer.toString(index)+";"+strInseratStringCSV);
                    break;
                case txt:
                    // Weitere Exporte möglich
                    break;
                default:
                    ExtendetLogger.LogEntry(LogStatus.WARNING, "Speichertyp wurde nicht gefunden - " + pstSpeicherTyp.toString());
                    return;
            }
            index ++;
        }
        try {
            osExportFile.flush();
            osExportFile.close();
        } catch (Exception e) {
            ExtendetLogger.LogEntry(LogStatus.FATAL, "Exception in CSV schreiben  - " + e.getMessage());
        }
    }

    public void inCSVDateiSchreiben (OutputStream fileExportFile, String pstrInseratStringCSV)
    {
        PrintWriter pwExport = null;
        String strText = "";

        //=====================================================================
        // Der Übergebene String wird für die Ausgabe vorbereitet
        // Zeilenumbrüche werden für die CSV mit HTML-Tags ersetzt
        // =====================================================================
        strText = pstrInseratStringCSV.replace("\r\n","<br>");
        strText = strText.replace("\n","<br>");
        strText = strText.concat("\r\n");
        //=====================================================================
        // Der Aufgearbeitete String wird in die Datei geschrieben
        // Flush garantiert das der Buffer in die Datei geschrieben wird.
        // =====================================================================
        try {
            pwExport = new PrintWriter(new OutputStreamWriter(fileExportFile, "UTF-8"));
            ExtendetLogger.LogEntry(LogStatus.INFO, "Text wird in die Datei geschrieben...");
            pwExport.append(strText);
            ExtendetLogger.LogEntry(LogStatus.INFO, "Text wurde in die Datei geschrieben.<br>" + strText);
            pwExport.flush();
        } catch (Exception e) {
            ExtendetLogger.LogEntry(LogStatus.FATAL, "Exception in CSV schreiben  - " + e.getMessage());

            System.out.println();
        }
        ExtendetLogger.LogEntry(LogStatus.PASS, "Text erfolgreich in die Datei geschrieben  - " + strText);

    }
}
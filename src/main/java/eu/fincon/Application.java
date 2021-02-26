package eu.fincon;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.relevantcodes.extentreports.LogStatus;
import eu.fincon.Datenverarbeitung.Config;
import eu.fincon.Logging.ExtendetLogger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Application {

    public static void main(String[] args) throws InterruptedException {

        SpringApplication.run(Application.class, args);
        TestEntryPoint.runTest();
        TimeUnit.SECONDS.sleep(5);
        persistResults();
        System.out.println("show the files in ./efs");
        showFiles("./efs");
        TimeUnit.SECONDS.sleep(5);
        System.exit(0);
    }

    private static void persistResults() {
        try {
            Files.copy(
                    new File(System.getProperty("user.dir") + "/"+Config.strDatabaseName).toPath(),
                    new File(System.getProperty("user.dir") + "/efs/data.db").toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void showFiles(String s) {

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

    public static void writeToFile(){
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

package eu.fincon;

import java.io.File;
import java.io.IOException;
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

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
        System.out.println("in the application");
        System.out.println("show the files in ./efs");
        showFiles("./efs");
        System.out.println("show the files in /efs");
        showFiles("/efs");
        doFile();
        //TestEntryPoint.runTest();
        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.exit(0);
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

    public static void doFile(){
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

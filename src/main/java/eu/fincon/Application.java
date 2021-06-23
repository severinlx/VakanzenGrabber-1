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

import static eu.fincon.TestEntryPoint.persistResults;
import static eu.fincon.TestEntryPoint.showFiles;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Application {

    public static void main(String[] args) throws InterruptedException {

        SpringApplication.run(Application.class, args);
        /*
        TestEntryPoint.runTest();
        TimeUnit.SECONDS.sleep(5);
        persistResults();
        System.out.println("show the files in ./efs");
        showFiles("./efs");
        TimeUnit.SECONDS.sleep(5);
        System.exit(0);
         */
    }
}

package eu.fincon;

import com.relevantcodes.extentreports.LogStatus;
import eu.fincon.Datenverarbeitung.Config;
import eu.fincon.Logging.ExtendetLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {
    //curl -i http://localhost:8080/get/results
    @RequestMapping(value = "/get/results", method = RequestMethod.GET)
    @ResponseBody
    public String getResults() {
        TestEntryPoint.runTest();
        TestEntryPoint.persistResults();
        return "Greetings from Spring Boot!";
    }
}
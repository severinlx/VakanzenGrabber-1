package eu.fincon;

import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class Controller {
    //curl -i http://localhost:8080/get/results
    @RequestMapping(value = "/get/results", method = RequestMethod.GET)
    @ResponseBody
    public String getResults() {
        try {
            TestEntryPoint.runTest();
        } catch (Exception e){
            return "Greetings from Spring Boot, there was an Error while running the tests!";
        }

        return "Greetings from Spring Boot!";
    }
}
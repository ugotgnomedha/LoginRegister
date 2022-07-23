package reglogweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        String configPath = "/Users/imac/Desktop/all_coding/LoginRegister/logreg.properties";
        //String configPath = System.getProperty("logreg");
        ConfigGetter.getConfig(configPath);
    }
}

// java -Dlogreg=... -jar ...
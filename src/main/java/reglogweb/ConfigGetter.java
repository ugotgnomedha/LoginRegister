package reglogweb;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigGetter {
    public static String urlDB = "";
    public static String userDB = "";
    public static String passwordDB = "";

    public static void getConfig(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            Properties prop = new Properties();
            prop.load(fis);

            urlDB = prop.getProperty("urlDB");
            userDB = prop.getProperty("userDB");
            passwordDB = prop.getProperty("passwordDB");
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

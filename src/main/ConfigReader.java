package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigReader {
    public static final String DB_CONFIG_FILE = "dbconfig.txt";
    private static ConfigReader ourInstance = new ConfigReader();

    private ConfigReader() {
    }

    public static ConfigReader getInstance() {
        return ourInstance;
    }

    public String getFromConfigFile(String fieldName,String configFile) {
        try (Scanner scanner = new Scanner(new FileInputStream(configFile))) {
            Pattern pattern = Pattern.compile(String.format("%s: (\\w+)", fieldName));

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    return matcher.group(1);
                }
            }
            return "";
        } catch (FileNotFoundException e) {
            System.err.println(configFile+" not found.");
            return "";
        }
    }
}

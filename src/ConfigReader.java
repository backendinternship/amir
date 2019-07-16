import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigReader {
    public static final String CONFIG_FILE = "config.txt";
    private static ConfigReader ourInstance = new ConfigReader();

    private ConfigReader() {
    }

    public static ConfigReader getInstance() {
        return ourInstance;
    }

    String getFromConfigFile(String name) {
        try (Scanner scanner = new Scanner(new FileInputStream(CONFIG_FILE))) {
            Pattern pattern = Pattern.compile(String.format("%s: (\\w+)", name));

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    return matcher.group(1);
                }
            }
            return "";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}

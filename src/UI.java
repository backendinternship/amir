import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UI {
    public static UI instance = new UI();

    private UI() {
    }

    public static UI getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        UI.getInstance().startUI();
    }

    public void startUI() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String input = scanner.nextLine();
                if (handleInput(input)) {
                    break;
                }
            }
        }
    }

    private boolean handleInput(String input) {
        Matcher updateMatcher = Pattern.compile("update").matcher(input);
        Matcher idMatcher = Pattern.compile("\\d+").matcher(input);
        Matcher exitMatcher = Pattern.compile("exit").matcher(input);


        if (updateMatcher.matches()) {
            Downloader.main(null);
            System.out.println("done");
            return false;
        } else if (idMatcher.matches()) {
            int id = Integer.valueOf(input);
            if (Database.getInstance().getRecordById(id) != null) {
                Database.getInstance().incrementViewCount(id);
                System.out.println(Database.getInstance().getRecordById(id));
            } else {
                System.out.println("not found.");
            }
            return false;
        } else return exitMatcher.matches();
    }
}

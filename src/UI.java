import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true){
            String input = scanner.nextLine();
            if(handleInput(input)){
                break;
            }
        }
    }

    private static boolean handleInput(String input){
        Matcher updateMatcher = Pattern.compile("update").matcher(input);
        Matcher idMatcher = Pattern.compile("\\d+").matcher(input);
        Matcher exitMatcher = Pattern.compile("exit").matcher(input);


        if(updateMatcher.matches()){
            Downloader.main(null);
            System.out.println("done");
            return false;
        }else if(idMatcher.matches()){
            int id = Integer.valueOf(input);
            if(Database.getRecordById(id)!=null) {
                Database.incrementViewCount(id);
                System.out.println(Database.getRecordById(id));
            }else {
                System.out.println("not found.");
            }
            return false;
        }else return exitMatcher.matches();
    }
}

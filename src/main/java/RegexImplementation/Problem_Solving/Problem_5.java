package RegexImplementation.Problem_Solving;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problem_5 {
    public static void main(String[] args) {

//        5. Check if given date format is valid or not. Format: dd/mm/yyyy [yyyy is valid upto 2014]
//        in : 12/12/1212 : valid
//        in : 12/13/1212 : invalid
//        in : 12/12/12122 : invalid
//        in : 32/12/1212 : invalid

        String input = "31/12/2000";
        String regex = "^((([0]?[1-9])|([12]?[0-9])|(3[01]))([\\/\\-])((0?[1-9])|(1[0-2]))\\6((20[01][0-4])))$";

        Pattern datePattern = Pattern.compile(regex);
        Matcher dateMatcher = datePattern.matcher(input);
        boolean isValidDate = dateMatcher.matches();

        if (isValidDate) {
            System.out.println("Valid!");
        } else {
            System.out.println("Invalid!");
        }

    }
}

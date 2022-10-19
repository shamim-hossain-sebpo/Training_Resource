package RegexImplementation.Problem_Solving;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problem_4 {
    public static void main(String[] args) {

//        4. Check if given time format is valid or not. Format: hh:mm:ss [12 hrs]
//        in : 12:59:59 out: valid
//        in : 12:60:59 out: invalid

        String input = "12.59.59";
        String regex = "^(?:0?[0-9]|1[0-2]):(?:[0-5]?[0-9])(?::[0-5]?[0-9])?\\s?(?:am|pm|AM|PM)?$";

        Pattern timePattern = Pattern.compile(regex);
        Matcher timeMatcher = timePattern.matcher("12:59:59 am");
        boolean isValidTime = timeMatcher.matches();

        if (isValidTime) {
            System.out.println("Valid!");
        } else {
            System.out.println("Invalid!");
        }
    }
}

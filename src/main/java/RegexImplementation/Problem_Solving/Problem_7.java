package RegexImplementation.Problem_Solving;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problem_7 {
    public static void main(String[] args) {

//        7. Validate "Day and month" format
//        in : 2/29 out: invalid
//        in : 1/30 out: invalid

        String input = "31/12";
        String regex = "^(?:([0]?[1-9])|(?:[12][0-9])|(?:3[01]))/((?:0?[1-9])|(?:1[0-2]))$";

        Pattern dayMonthPattern = Pattern.compile(regex);
        Matcher dayMonthMatcher = dayMonthPattern.matcher(input);
        boolean isValid = dayMonthMatcher.matches();

        if (isValid) {
            System.out.println("Valid!");
        } else {
            System.out.println("Invalid!");
        }

    }
}

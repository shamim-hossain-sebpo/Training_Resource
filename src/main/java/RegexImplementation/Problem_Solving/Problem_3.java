package RegexImplementation.Problem_Solving;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problem_3 {
    public static void main(String[] args) {
//        3. Make a Ip(v4) address validator method.
//        in : 10.10.10.10 out: valid
//        in : 310.10.10.10 out: invalid
//        in : 255.10.10.10 out: valid
//        in : 2555.10.10.10 out: invalid

        String regex = "^((?:[01]?[0-9][0-9]?)|(?:[2][0-4][0-9])|(?:25[0-5]))\\.((?:[01]?[0-9][0-9]?)|(?:[2][0-4][0-9])|(?:25[0-5]))\\.((?:[01]?[0-9][0-9]?)|(?:[2][0-4][0-9])|(?:25[0-5]))\\.((?:[01]?[0-9][0-9]?)|(?:[2][0-4][0-9])|(?:25[0-5]))$";
        String input = "00.125.235.255";

        Pattern ipPattern = Pattern.compile(regex);
        Matcher ipMatcher = ipPattern.matcher("255.10.10.10");
        boolean isIP = ipMatcher.matches();

        if (isIP) {
            System.out.println("Valid!");
        } else {
            System.out.println("Invalid!");
        }
    }
}

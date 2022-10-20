package RegexImplementation.Problem_Solving;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problem_11 {
    public static void main(String[] args) {

//        11.Match all words that contain at least one same uppercase and a lowercase letter
//        in: "Mam Did You tesT alL Students"
//        out: "Mam Did tesT alL Students"

        String input = "Mam Did You tesT alL Students";
        String regex = "\\b(\\w*([a-z])\\w*(?=[A-Z])(?i)\\2\\w*)|(\\w*([A-Z])\\w*(?=[a-z])(?i)\\4\\w*)\\b";

        Pattern upperLowerPattern = Pattern.compile(regex);
        Matcher upperLowerMatcher = upperLowerPattern.matcher(input);

        while (upperLowerMatcher.find()) {
            System.out.print(upperLowerMatcher.group() + " ");
        }
        System.out.println();

    }
}

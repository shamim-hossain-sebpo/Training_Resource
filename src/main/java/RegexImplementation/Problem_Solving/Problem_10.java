package RegexImplementation.Problem_Solving;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problem_10 {
    public static void main(String[] args) {

//        10.Match all words where 1st and last char is same
//        in: all ala imi out: ala imi

        String input = "all ala imi does did son mom";
        String regex = "([a-z])[a-z]+\\1";

        Pattern wordPattern = Pattern.compile(regex);
        Matcher wordMatcher = wordPattern.matcher(input);

        while (wordMatcher.find()) {
            System.out.println(wordMatcher.group());
        }
    }
}

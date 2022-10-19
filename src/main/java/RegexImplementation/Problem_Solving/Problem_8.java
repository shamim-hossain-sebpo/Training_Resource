package RegexImplementation.Problem_Solving;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problem_8 {
    public static void main(String[] args) {

        //8. Only match constant in a set using "char set intersection"

        String input = "This is Md. Shamim Hossain";
        String regex = ("([a-z&&[^aeiou]])");

        Pattern consonentPattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher consonentMatcher = consonentPattern.matcher(input);

        while (consonentMatcher.find()) {
            System.out.print(consonentMatcher.group(1) + " ");
        }
        System.out.println();

    }
}

package RegexImplementation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Positive_Look_Ahead {
    public static void main(String[] args) {
        Pattern p = Pattern.compile("\\w+(?=\\.)");
        Matcher matcher = p.matcher("shamim.");
        boolean isMatches = matcher.find();
        System.out.println(isMatches);

        Pattern p2 = Pattern.compile("(?=^[0-6-]+$)\\d{3}-\\d{3}-\\d{4}");  // start to end number range should be between 0-6.
        Matcher matcher2 = p2.matcher("000-123-4563");
        boolean isMatches2 = matcher2.matches();
        System.out.println(isMatches2);
    }
}

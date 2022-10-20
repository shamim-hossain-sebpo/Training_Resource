package RegexImplementation.Problem_Solving;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problem_13 {
    public static void main(String[] args) {

//        13. Match all strings/words that have exactly 3 'b' in total
//        in: "abba abbab baba babubu"
//        out: "abbab"

        String input = "abba abbab baba babubu";
        String regex = "\\b(?=\\w*b\\w*b\\w*b)\\w+\\b";

        Pattern tripleBPattern = Pattern.compile(regex);
        Matcher tripleBMatcher = tripleBPattern.matcher(input);

        while(tripleBMatcher.find()){
            System.out.println(tripleBMatcher.group());
        }

    }
}

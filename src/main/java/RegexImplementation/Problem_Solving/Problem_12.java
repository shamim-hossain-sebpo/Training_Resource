package RegexImplementation.Problem_Solving;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problem_12 {
    public static void main(String[] args) {

//        12. Match all string(words)where theres no sub-string "ab"
//        in abad acdd adabdd
//        out acdd

        String input = "abad acdd adabdd baul";
        String regex = "\\b(?!\\w*ab)\\w+\\b";

        Pattern except_ab_Pattern = Pattern.compile(regex);
        Matcher except_ab_matcher = except_ab_Pattern.matcher(input);

        while(except_ab_matcher.find()){
            System.out.println(except_ab_matcher.group());
        }
    }
}

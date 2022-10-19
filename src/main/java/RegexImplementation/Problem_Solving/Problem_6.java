package RegexImplementation.Problem_Solving;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problem_6 {
    public static void main(String[] args) {

//        6. Check whether a HTML block is unique in a HTML file|string block input.
//        str: "<div>..</div><div>..</div><p>..</p>"
//        in : div out: duplicate
//        in : p out: unique
//        do this with java regex besides ur regex learning

        String input = "<div>I love my country</div><p>hello, how are you?</p> <div> This is shamim hossain <span> lksdjf </span> </div>";

        String regex = "<(div)>.*?</\\1>";

        Pattern duplicateTagPattern = Pattern.compile(regex);
        Matcher duplicateTapMatcher = duplicateTagPattern.matcher(input);

        int count = 0;
        while (duplicateTapMatcher.find()) {
            count++;
            if (count == 2) {
                System.out.println("Duplicate!");
                break;
            }
        }
        if (count == 1) {
            System.out.println("Unique!");
        } else if (count == 0) {
            System.out.println("Not Exist!");
        }
    }
}

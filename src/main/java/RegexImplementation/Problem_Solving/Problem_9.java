package RegexImplementation.Problem_Solving;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problem_9 {
    public static void main(String[] args) {

//        9. Match all odd numbers in a string containing both numbers and non numbers
//        in: abcd12XY15c1552d13 out:15,13

        String input = "als15ksd13ksdj14ldj1225dklfas365djl654dkfs";
        String regex = "(\\d+)";

        Pattern oddPattern = Pattern.compile(regex);
        Matcher oddMathcer = oddPattern.matcher(input);
        int oddNumber;

        System.out.print("Odd Numbers : ");
        while (oddMathcer.find()) {
            if ((oddNumber = Integer.parseInt(oddMathcer.group())) % 2 != 0) {
                System.out.print(oddNumber + " ");
            }
        }
    }
}

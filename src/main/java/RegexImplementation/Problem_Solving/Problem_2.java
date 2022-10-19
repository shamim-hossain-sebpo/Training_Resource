package RegexImplementation.Problem_Solving;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problem_2 {
    public static void main(String[] args) {
        //2. Check whether an email address is valid or not.
        String input = "shamim@gmail.com";

        // ---- (should contain .@ and should not contain double consecutive .(dot))
        String regex = "(?!.*\\.\\.)[\\w.,]+@[\\w]+\\.[a-zA-z]{2,3}(\\.\\w{2,3})?";

        Pattern emailPattern = Pattern.compile(regex);
        Matcher emailMathcer = emailPattern.matcher(input);
        boolean isValidEmail = emailMathcer.matches();
        System.out.println("Is Valid Pattern : " + isValidEmail);
    }
}

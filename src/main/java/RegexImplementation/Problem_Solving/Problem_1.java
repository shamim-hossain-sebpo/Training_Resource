package RegexImplementation.Problem_Solving;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problem_1 {
    public static void main(String[] args) {

        //1. Count duplicate words in a sentence|file|string block input.

        String input = "he he goes to school";
        String regex = "([a-zA-Z]+)";
        Pattern duplicatePattern = Pattern.compile(regex);
        Matcher duplicateMatcher = duplicatePattern.matcher(input);

        Map<String, Integer> map = new LinkedHashMap<>();

        while (duplicateMatcher.find()) {
            String temp = duplicateMatcher.group(1);
            if (!map.containsKey(temp)) {
                map.put(temp, 1);
            } else {
                Integer oldValue = map.get(temp);
                map.put(temp, oldValue + 1);
            }
        }

        for (Map.Entry<String, Integer> data : map.entrySet()) {
            System.out.print(data.getKey() + ": " + data.getValue() + ", ");
        }
        System.out.println();


    }
}

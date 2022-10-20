package RegexImplementation.Problem_Solving;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problem_14 {
    public static void main(String[] args) throws ParseException {

//        14. how to compare two date, which one is greater
//        01.11.2014 31.10.2019

        String input = " 22-11-2020 01-11-2014";
        String regex = "(?:[0]?[1-9]|[12][0-9]|3[01])([\\/\\-])(?:0?[1-9]|1[0-2])\\1(20[01][0-9]|2020)";

        Pattern dateComparePattern = Pattern.compile(regex);
        Matcher dateCompareMatcher = dateComparePattern.matcher(input);

        List<Date> dateList = new ArrayList<>();

        while (dateCompareMatcher.find()) {
            String str_date = dateCompareMatcher.group();
            dateList.add(new SimpleDateFormat("dd-MM-yyyy").parse(str_date));
        }
        int i = dateList.get(0).compareTo(dateList.get(1));
        if (i == 1) {
            System.out.println("Greater Date is : " + new SimpleDateFormat("dd-MM-yyyy").format(dateList.get(0)));
        } else if (i == -1) {
            System.out.println("Greater Date is : " + new SimpleDateFormat("dd-MM-yyyy").format(dateList.get(1)));
        } else {
            System.out.println("Both Date are Same!");
        }

    }
}

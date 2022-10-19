package RegexImplementation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegEx_Exercise {
    public static void main(String[] args) {

        //----- Name Pattern (Name should start with Capital and should not contain consecutive double(space/fullstops/qama)--------
        Pattern p = Pattern.compile("^(?!.*\\s\\s)(?!.*\\.\\.)(?!.*,,)[A-Z][a-zA-Z .,]{3,30}$",Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher("Md. Shamim, Hossain");
        boolean isName = matcher.matches();
        System.out.println("Name Pattern    : " + isName);

        //----- Email Pattern (should contain .@ and should not contain consecutive double dot ) --------

        Pattern emailPattern = Pattern.compile("^(?!.*\\.\\.)[\\w.,]+@[\\w]+\\.[a-zA-z]{2,3}\\.?[a-zA-Z]{0,3}$");
        Matcher emailMatcher = emailPattern.matcher("shami_.,m@gamil.com.us");
        boolean isEmail = emailMatcher.matches();
        System.out.println("Email Pattern   : " + isEmail);


        //----- URL Pattern-------
        Pattern urlPattern = Pattern.compile("^(?:http|https|ftp):\\/\\/([\\w.,\\/~!@#\\<>$%^&\\*?=\\-])+\\.([\\w.,\\/~!@#\\<>$%^&\\*?=\\-])+$");
        Matcher urlMatcher = urlPattern.matcher("https://aftermath.example.com/border");
        boolean isURL = urlMatcher.matches();
        System.out.println("URL Pattern     : " + isURL);

        //------ IP Address Pattern (Range should be between 0-255) --------
        // Pattern for Range(000-100) ---- ^(([0]?[0-9]?[0-9])|([1][0][0]))$ --------
        Pattern ipPattern = Pattern.compile("^(([01]?[0-9][0-9]?)|([2][0-4][0-9])|(25[0-5]))\\.(([01]?[0-9][0-9]?)|([2][0-4][0-9])|(25[0-5]))\\.(([01]?[0-9][0-9]?)|([2][0-4][0-9])|(25[0-5]))\\.(([01]?[0-9][0-9]?)|([2][0-4][0-9])|(25[0-5]))$");
        Matcher ipMatcher = ipPattern.matcher("00.125.235.255");
        boolean isIP = ipMatcher.matches();
        System.out.println("IP Address      : " + isIP);

        //------ Date Pattern -------
        // month pattern -- ^((0?[1-9])|(1[0-2]))$ ---
        // day pattern   -- ^(([0]?[1-9])|([12]?[0-9])|(3[01]))$ --
        // year pattern(2000-2050)  -- ^((20[0-4][0-9])|(2050))$ --

        Pattern datePattern = Pattern.compile("^(((0?[1-9])|(1[0-2]))([\\/\\-])(([0]?[1-9])|([12][0-9])|(3[01]))\\5((20[0-4][0-9])|(2050)))$");
        Matcher dateMatcher = datePattern.matcher("12-25-2010");
        boolean isValidDate = dateMatcher.matches();
        System.out.println("Date Pattern    : " + isValidDate);

        // ------- Time Pattern (12 Hours) ----------
        // Hour Pattern -- ^(0?[0-9]|1[0-2])$ --
        // Mniute/Second Pattern -- ^([0-5]?[0-9])$ --

        Pattern timePattern12 = Pattern.compile("^(?:0?[0-9]|1[0-2]):(?:[0-5]?[0-9])(?::[0-5]?[0-9])?\\s?(?:am|pm|AM|PM)?$");
        Pattern timePattern24 = Pattern.compile("^(?:[01]?[0-9]|2[0-4]):(?:[0-5]?[0-9])(?::[0-5]?[0-9])?\\s?(?:GMT|EST)?$");
        Matcher timeMatcher12 = timePattern12.matcher("12:59:59 am");
        Matcher timeMatcher24 = timePattern24.matcher("24:59:59 GMT");
        boolean isValidTime12 = timeMatcher12.matches();
        boolean isValidTime24 = timeMatcher24.matches();
        System.out.println("Time Pattern 12 : " + isValidTime12);
        System.out.println("Time Pattern 24 : " + isValidTime24);


        // ------ Postal Code Pattern --------
        // ------ pattern for UK ------
        Pattern postalCodePattern = Pattern.compile("^(([A-Z]{1,2}[0-9]{1,2})(\\s[0-9][A-Z]{2}))|(([A-Z]{1,2}[0-9][A-Z])(\\s[0-9][A-Z]{2}))$");
        Matcher postalCodeMatcher = postalCodePattern.matcher("AA99 9AA");
        boolean isValidPostalCode = postalCodeMatcher.matches();
        System.out.println("UK Postal Code  : " + isValidPostalCode);

        // ------ Password Pattern ---------
        Pattern passwordPattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[~!#$%^&amp;*()_+=\\-{}\\[\\];:&lt;&gt;?]).{8,15}$");
        Matcher passwordMatcher = passwordPattern.matcher("Shamim#123sj");
        boolean isValidPassword = passwordMatcher.matches();
        System.out.println("Password Pattern: " + isValidPassword);


        //----- Credit Card Pattern ---------
        // American Express Pattern --  ^(3[47]\d{2}(\-)?\d{6}\2?\d{5})$ ---
        // allCreditCardPattern     --  ^(((?:4\d{3}|5[0-5]\d{2}|6011|62\d{2})(\-)?\d{4}\3?\d{4}\3?\d{4})|(3[47]\d{2}(\-)?\d{6}\5?\d{5}))$ ---
        Pattern creditCardPattern = Pattern.compile("^((?:4\\d{3}|5[0-5]\\d{2}|6011|62\\d{2})(\\-)?\\d{4}\\2?\\d{4}\\2?\\d{4})$");
        Matcher creditCardMatcher = creditCardPattern.matcher("6212-1234-1234-1234");
        boolean isValidCreditCard = creditCardMatcher.matches();
        System.out.println("Card Pattern    : " + isValidCreditCard);

        // ----differnt card no----
//        4123123412341234
//        4123-1234-1234-1234
//
//        5112123412341234
//        5212123412341234
//        5112-1234-1234-1234
//        5212-1234-1234-1234
//
//        6011123412341234
//        6011-1234-1234-1234
//
//        6212123412341234
//        6212-1234-1234-1234
    }
}

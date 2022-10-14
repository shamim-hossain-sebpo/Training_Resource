package StringImplementation.PartA;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Problem_6 {
    public static void main(String[] args) throws Exception{
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");
        String formattedDate = dateFormat.format(date);
        System.out.println(formattedDate);
        System.out.println(date);

        String date1 = "03/22/1993";
        Date parseDate1 = new SimpleDateFormat("MM/dd/yyyy").parse(date1);
        if(parseDate1.before(date)){
            System.out.println(date1+": PAST");
        }else{
            System.out.println(date1+": FUTURE");

        }

        String date2 = "12/19/2022";
        Date parseDate2 = new SimpleDateFormat("MM/dd/yyyy").parse(date2);
        if(parseDate2.before(date)){
            System.out.println(date2+": PAST");
        }else{
            System.out.println(date2+": FUTURE");

        }

        String date3 = "10/05/2010";
        Date parseDate3 = new SimpleDateFormat("MM/dd/yyyy").parse(date3);
        if(parseDate3.before(date)){
            System.out.println(date3+": PAST");
        }else{
            System.out.println(date3+": FUTURE");

        }




    }
}

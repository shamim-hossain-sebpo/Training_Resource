package DSA_Collection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class People {
    String address;

    People(String address) {
        this.address = address;
    }

}

public class Part3_2 {

    public static void main(String[] args) {
        Map<String, List<People>> dictionary = new LinkedHashMap<>();
        List<People> akib = new ArrayList<>();
        List<People> sajeb = new ArrayList<>();
        List<People> niloy = new ArrayList<>();


        akib.add(new People("Mirpur"));
        akib.add(new People("Dhanmondi"));
        akib.add(new People("Shiddeshawri"));

        sajeb.add(new People("Lalmatia"));

        niloy.add(new People("Puran Dhaka"));
        niloy.add(new People("Rajabagh"));


        dictionary.put("Akib", akib);
        dictionary.put("Sajeb", sajeb);
        dictionary.put("Niloy", niloy);
        dictionary.put("Ratul", null);

        int noCont = 1;
        for (Map.Entry<String, List<People>> data : dictionary.entrySet()) {
            if (data.getValue() != null) {
                System.out.print(data.getKey() + ": ");
                noCont = 1;
                for (People value : data.getValue()) {
                    System.out.print((noCont++) + "." + value.address + " ");
                }
                System.out.println();
            } else {
                System.out.print(data.getKey() + ": No Address");
            }


        }

    }
}

package StringImplementation.PartA;

import java.util.ArrayList;
import java.util.List;

public class Problem_partB_1 {
    public static void main(String[] args) {
        String name1 = "Rahim";
        String name2 = "Karim";
        String name3 = "Sofia";
        String name4 = "Karim";

        String name = "name";

      if(name1.equalsIgnoreCase(name2)){
          System.out.println("name1 and name2 matches!");
      }else if(name1.equalsIgnoreCase(name3)){
          System.out.println("name1 and name3 matches!");
      }else if(name1.equalsIgnoreCase(name4)){
          System.out.println("name1 and name4 matches!");
      }

      if(name2.equalsIgnoreCase(name3)){
          System.out.println("name2 and name3 matches!");
      }else if(name2.equalsIgnoreCase(name4)){
          System.out.println("name2 and name4 matches!");
      }

      if(name3.equalsIgnoreCase(name4)){
          System.out.println("name3 and name4 matches!");

      }

    }
}

package DSA_Collection;

import java.util.*;

public class DataStructreProblems {
    public static void main(String[] args) {
        //----- Linear Array Problemls --------
        //---- Problem-1 ------
        int[] linearArray = {10, 20, 30, 40, 50, 60};
        System.out.println(linearArray[4]);

        //---- Problem-2 ------
        int[] array = {2, 6, 1, 5, 9};
        System.out.println(Arrays.toString(array));

        for (int i = array.length - 1; i >= 0; i--) {
            System.out.print(array[i] + " ");
        }
        System.out.println();

        //------ List Problelm ---------
                 //---- problem-1- -----
        List<Character> list = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h','i');

        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + " ");
        }
        System.out.println();

                 //---- problem-2 ------
        int mid = list.size()/2;
        System.out.println(list.get(mid));

                 //---- problem-3 ------
        for (int i =list.size()-1; i >=0 ; i--) {
            System.out.print(list.get(i)+" ");
        }
        System.out.println();

        //------ String Problems --------
                 //---- problem-1 ------
        Scanner input = new Scanner(System.in);
        List<String> listNames = new ArrayList<>();

//        for (int i = 1; i <=5 ; i++) {
//            System.out.print("Please Input-"+i+":");
//            listNames.add(input.nextLine()) ;
//        }

        System.out.println();
        for(String data : listNames){
                System.out.print(data.concat(","));
        }
        System.out.println();

                //---- problem-2 ------
        String country = "Bangladesh";
        char[] chars = country.toCharArray();
        for (int i =chars.length-1; i >=0 ; i--) {
            System.out.print(chars[i]);

        }
        System.out.println();

               //---- problem-3 ------
        String manner = "Manners";
        Map<Character,Integer> map = new LinkedHashMap<>();

        int i =0;
        while(manner.length()!=i){
            char charAt = manner.charAt(i);
            if(!map.containsKey(charAt)){
                map.put(charAt,1);
            }else{
                map.put(charAt,map.get(charAt)+1);
            }
            i++;
        }
        for(Map.Entry<Character,Integer> mapValue : map.entrySet()){
            System.out.print(mapValue.getKey()+"="+mapValue.getValue()+" ");
        }
        System.out.println();

                   //---- problem-4 ------

        String temp = "We rise up while lifting others";
        String[] tempArray = temp.split(" ");

        for (int j = 0; j <tempArray.length ; j++) {
            System.out.println(tempArray[j]);
        }

    }
}

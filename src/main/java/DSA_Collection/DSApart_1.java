package DSA_Collection;

import java.util.*;

public class DSApart_1 {
    public static void main(String[] args) {
        //--1.Find all pairs of an integer array whose sum is equal to a given number.

        int[] array = {2, 5, 8, 6, 5, 2};
        int target_sum = 10;
        int pairs_count = 0;

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                if (array[i] + array[j] == target_sum) {
                    System.out.println(array[i] + " + " + array[j] + " = " + target_sum);
                    pairs_count++;
                }
            }
        }
        System.out.println("Total Pairs Count : " + pairs_count);


        //2.Find the largest and smallest number in an unsorted integer array.
        int[] unsortedArray = {25, 12, 36, 45, 85, 47, 20, 30};
        int max = 0, min = unsortedArray[0];


        for (int i = 0; i < unsortedArray.length; i++) {
            if (max < unsortedArray[i]) {
                max = unsortedArray[i];
            }
            if (min > unsortedArray[i]) {
                min = unsortedArray[i];
            }
        }
        System.out.println("Max Value : " + max);
        System.out.println("Min Value : " + min);

        //4.How to count a number of vowels and consonants in a String?

        String passage = "I am from Bangladesh";
        passage = passage.toLowerCase();
        char[] vowelList = {'a', 'e', 'i', 'o', 'u'};
        char[] passageArray = passage.toCharArray();
        int vowel = 0, consonent = 0, temp = 0;

        for (int i = 0; i < passageArray.length; i++) {
            if (passageArray[i] >= 'a' && passageArray[i] <= 'z') {
                for (int j = 0; j < vowelList.length; j++) {
                    if (passageArray[i] == vowelList[j]) {
                        temp++;
                        break;
                    }
                }
                if (temp == vowel) {
                    consonent++;
                } else {
                    vowel = temp;
                }
            }
        }
        System.out.println("Total Vowel     : " + vowel);
        System.out.println("Total Consonent : " + consonent);

        //5.Search for a (value) in a hash-map, and if presents return its key. Else return null.

        Map<String, String> employee = new HashMap<>();
        employee.put("GM", "Mike");
        employee.put("AGM", "Jhon");
        employee.put("Sales Director", "Mishel");
        employee.put("HR Director", "Sofia");
        boolean found = false;

        for (Map.Entry<String, String> data : employee.entrySet()) {
            if (data.getValue() == "Jhon") {
                System.out.println(data.getKey());
                found = true;
            }
        }
        if (!found) System.out.println("null");


        //6.How do you find the missing number in a given integer array of 1 to 100.

        int[] missingNumArray = {1, 2, 5, 9, 10, 12, 15};

        List<Integer> missingList = new ArrayList<>();
        int count = 1;

        for (int i = 0; i < missingNumArray.length; i++) {
            while (count < missingNumArray[i]) {
                missingList.add(count++);
            }
            count++;
        }

        System.out.println("Missing List : " + missingList);


        // 7.How do you find duplicate numbers in an array if it contains multiple duplicates?

        int[] duplicateArray = {10, 30, 10, 20, 50, 20, 90, 40, 90, 10, 20};

        Set<Integer> uniqueSet = new HashSet<>();
        List<Integer> duplicateList = new ArrayList<>();

        for (int i = 0; i < duplicateArray.length; i++) {
            if (!uniqueSet.add(duplicateArray[i])) {
                duplicateList.add(duplicateArray[i]);
            }
        }
        System.out.println(duplicateList);

        //8.How do you reverse a linked list?

        LinkedList<Integer> reverseList = new LinkedList<>();
        reverseList.add(10);
        reverseList.add(20);
        reverseList.add(30);
        reverseList.add(40);
        reverseList.add(50);

        ListIterator listItr = reverseList.listIterator();
        System.out.println(reverseList.size());



        //9.How do you count the occurrence of a given character in a string?
        String hobby = "Travelling is my favourite hobby";
        hobby = hobby.toLowerCase();
        char findChar = 't';
        int countChar = 0;

        for (int i = 0; i < hobby.length(); i++) {
            if (findChar == hobby.charAt(i))
                countChar++;
        }
        System.out.println("Total Occurance Char " + findChar + " : " + countChar);


        //10.Search for a (key) in the hash-map, and if present return its value. Else return null.
        Map<String, Integer> student = new LinkedHashMap<>();

        student.put("Rahim", 101);
        student.put("Karim", 102);
        student.put("Mitu", 103);
        student.put("Sofia", 104);
        student.put("Jihan", 105);

        final String STR_TARGET_KEY = "Sofia";

        if (student.containsKey(STR_TARGET_KEY)) {
            System.out.println("Roll of " + STR_TARGET_KEY + ": " + student.get(STR_TARGET_KEY));
        } else {
            System.out.println("null");
        }

        // Question 11 is duplicate of Questin 2.
        // Question 12 is duplicate of Question 1.
    }
}

package DSA_Collection;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Part3 {
    List<String> list = new ArrayList<>();
    Set<String> set = new LinkedHashSet<>();

    public static void main(String[] args) {

        Part3 obj = new Part3();
        //Convert the array to a List, and then print the List using enhanced for loop. Do it in a function called 'convertingToListAndPrint(int[] arr)
        String[] groceryArray = {"Egg", "Lemon", "Brocoli", "Carrot", "Cabbage", "Lemon","Potato", "Beef", "Lemon", "Egg","Potato"};
        obj.convertingToListAndPrint(groceryArray);
        obj.removeDuplicateList();
        System.out.println(obj.isPotatoThere("Potato"));
        obj.updateArrayList("Beef","Mutton");
    }

    public void convertingToListAndPrint(String[] array) {

        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }

        System.out.print("GroceryList: ");
        for (String data : list) {
            System.out.print(data + " ");
        }
        System.out.println();
    }

    public void removeDuplicateList() {

        for (int j = 0; j < list.size(); j++) {
            set.add(list.get(j));
        }
        // removing duplicate-----
        list.clear();
        list.addAll(set);

        System.out.print("After Remove Duplicate List : ");
        for (String data : list) {
            System.out.print(data + " ");
        }
        System.out.println();
    }

    public boolean isPotatoThere(String item){
        return list.contains(item);
    }

    public void updateArrayList(String oldItem, String updateItem){
        if(list.contains(oldItem)){
            list.remove(oldItem);
            list.add(updateItem);
        }

        System.out.println("Update List Are: ");
        for(String data : list){
            System.out.print(data+" ");
        }
        System.out.println();
    }
}

package DSA_Collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class List2 {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();

        list.add(25);
        list.add(30);
        list.add(10);
        list.add(8);
        list.add(12);

        //---- traiverse with iterator ----------
        Iterator itr = list.iterator();
        while (itr.hasNext()) {
            System.out.print(itr.next() + " ");
        }
        System.out.println();

        //----- traiverse with enhanced for loop-----
        for (Integer data : list) {
            System.out.print(data + " ");
        }
        System.out.println();

    }
}

package DSA_Collection;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Set3 {
    public static void main(String[] args) {
        Set<Integer> set = new LinkedHashSet<>();

        set.add(10);
        set.add(20);
        set.add(15);
        set.add(25);
        set.add(5);
        set.add(18);

        //----- Traiverse with Iterator-----
        Iterator itr = set.iterator();
        while (itr.hasNext()) {
            System.out.print(itr.next() + " ");
        }
        System.out.println();

        //------ Traiverse with Enhanced for Loop----
        for (Integer data : set) {
            System.out.print(data + " ");
        }
        System.out.println();
    }
}

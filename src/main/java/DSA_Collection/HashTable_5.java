package DSA_Collection;

import java.util.Hashtable;
import java.util.Map;

public class HashTable_5 {
    public static void main(String[] args) {
        Hashtable<String, Integer> hashtable = new Hashtable<>();

        hashtable.put("Step-1", 10);
        hashtable.put("Step-2", 20);
        hashtable.put("Step-3", 30);
        hashtable.put("Step-4", 40);
        hashtable.put("Step-5", 50);
        //hashtable.put(null,60);

        for (Map.Entry<String, Integer> data : hashtable.entrySet()) {
            System.out.println(data.getKey() + "--------" + data.getValue());
        }

        System.out.println(hashtable.get("Step-1"));
        //System.out.println(hashtable.get(null));
    }
}

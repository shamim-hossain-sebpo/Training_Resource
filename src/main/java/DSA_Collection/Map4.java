package DSA_Collection;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Map4 {
    public static void main(String[] args) {
        Map<String, String> employee = new LinkedHashMap<>();

        employee.put("Name", "Shamim");
        employee.put("Designation", "Software Engineer");
        employee.put("Company", "ServiceEngineBPO");
        employee.put("Address", "Middle Badda");

        //--------- Traiverse with Enhanced for loop----
        for (Entry<String, String> data : employee.entrySet()) {
            System.out.println(data.getKey() + "-------" + data.getValue());
        }
        System.out.println();
        // -------- Retrive value according to Key---------

        System.out.println(employee.get("Name"));
        System.out.println(employee.get("Designation"));
        System.out.println(employee.get("Company"));
        System.out.println(employee.get("Address"));
    }
}

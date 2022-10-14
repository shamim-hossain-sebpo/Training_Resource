package StringImplementation.PartA;

import java.util.LinkedHashMap;
import java.util.Map;

public class Problem_4 {
    public static void main(String[] args) {
        String manner = "This is Md. Shamim Hossain";
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
    }
}

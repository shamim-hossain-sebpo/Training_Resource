package Groovy_Documents

class Map_09 {
    public static void main(String[] args) {
        def map = ['name': 'Shamim', 'EID': 4442, 'Address': 'Dhaka']
        map.put('Nationality', "Banglasesh")
        println map

        // ---- retrive Value----
        println map.name
        println map['EID']
        println map.get('Address')
        println map.getAt('Nationality')

        for(data in map){                             // using for in loop
            println "$data.key ---- $data.value"
        }

        //map.each {key,value -> println "$key --- $value"}
        //map.each {item -> println "$item.key ---- $item.value"}

        //map.eachWithIndex{ key,value,i-> println "${i+1} | $key --- $value"}
        map.eachWithIndex { item, i -> println "$i | $item.key -- $item.value" }

        def data = map.entrySet();
        data.each { entry -> println entry.key + "-----" + entry.value }

        //----- Different Methods -------
        println map.containsKey('name')       // if match return true or false.
        println map.containsValue("Shami")   // if match return true or false.
        def map2 = map.clone()                    // clone whole map.
        println map2
        map.clear()                              // clear map.
        println map
        println map.size()
    }
}

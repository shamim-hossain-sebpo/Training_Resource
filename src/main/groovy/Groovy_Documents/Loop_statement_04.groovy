package Groovy_Documents

class Loop_statement_04 {
    public static void main(String[] args) {

        //------ Traditional for Loop ------
        for (int i = 0; i <= 10; i++) {
            print(i + " ")
        }
        println ""

        //------ for in Loop --------
        for (j in 1..5) {
            print j + " "
        }
        println ""

        1.upto(10) { print "$it " }
        println ""

        10.times { print "$it " }
        println ""

        1.step(12, 3) { print "$it " }
        println ""

        // iterate over a list
        def x = 0
        for (i in [0, 1, 2, 3, 4]) {
            x += i
        }
        assert x == 10

        // iterate over an array
        //def array = (0..4).toArray()     // initializing value to an array.
        def array = [0, 1, 2, 3, 4]        // initializing value to an array.
        x = 0
        for (i in array) {
            x += i
        }
        assert x == 10

        //---- iterate over map ------
        def map = ['name': 'shamim', 'id': 101, 'isSingle': "true"]

        for (data in map) {
            println data.key + "-------" + data.value
        }

        //----- iterate over values in map----
        for (data in map.values()) {
            print data + ", "
        }
        println ""


        //---- iterate over the characters in String------

        def char_str = "Babgladesh"
        def char_list = []
        for (data in char_str) {
            char_list.add(data)
        }

        for(data in char_list){
            print data+", "
        }
        println ""


    }
}

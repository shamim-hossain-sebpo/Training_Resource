package Groovy_Documents

class Ranges_10 {
    public static void main(String[] args) {
        def rangeList_incl = 1..10          //Note: Ranges is instance of java.util.List.
        def rangeList_excl = 10..<1

        println rangeList_incl.getFrom()            // return first value of Ranges.
        println rangeList_excl.getFrom()

        println rangeList_incl.getTo()              // return last value of Ranges.
        println rangeList_excl.getTo()

        println rangeList_incl.get(5)
        println rangeList_incl[5]
        println rangeList_incl[2,3,6]             // Retrive random value from Ranges.

        println rangeList_incl.subList(0,5)       // begining index inclusive and Ending index exclusive.

        for(i in rangeList_incl){                 // using for in loop.
            print "$i "
        }
        println ""

        rangeList_incl.each {item -> print "$item "}  // using clousers each
        println ""

        //----Different Methods ----
        println rangeList_excl.contains(5)           // if match return true or false.
        println rangeList_excl.isReverse()           // if ranges is Reverse return true or false.
        println rangeList_incl.size()
        println rangeList_excl.size()

    }
}

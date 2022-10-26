package Groovy_Documents

class List_08 {
    public static void main(String[] args) {
        def nameList = ['Shamim','Rahim','Karim','Sofia',[1,2,3],'Mike','Any']

        //------ add value to list ------
        nameList.push('Babu')          // add value to last.
        println nameList
        nameList.add("Mimi")           // add value to last.
        println nameList
        nameList.add(2,'Jhon')  // add value index wise.
        println nameList
        nameList << 'Harry'                  // concatination
        println nameList
        nameList = nameList + ['Murad','Jasmin','Rubaiya']   // add multiple value at a time.
        println nameList
        nameList = nameList - ['Karim']                      // Remove specific value.
        println nameList
        nameList.pop()
        println nameList                                     // remove from the last.

        // ------ retrive value from list ----------
        println nameList[0]
        println nameList.get(0)
        println nameList[0,3,4]                              // retirve random value from list.
        println nameList[1..5]                               // retrive multiple value at a time.

        for(i in nameList){            // using for in loop
            print ("$i ")
        }
        println ""

        println nameList.intersect(['Mike','Babu','kdsf'])   // return match item

        println nameList.contains('Babu')

        println nameList.reverse()                // reverse list.
        println nameList.sort().reverse()         // sorting ascending and descending

    }

}

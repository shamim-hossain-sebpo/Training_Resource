package Groovy_Documents

class Clousers_07 {
    public static void main(String[] args) {
        Clousers_07 obj = new Clousers_07();
        //obj.myClousers.call("Shamim") // calling closures.
        obj.myMethod(obj.myClousers)   // passing closures as method argument.
        def clousers2 = obj.myClousers2(10, 20, 30)
        println clousers2

        //---- printing List and Map value using closures -----
        def nameList = ['shamim', 'rahim', 'karim', 'sofia']
        println nameList.each { it }
        println nameList.find { item -> item == 'sofia' }    // if found return that value otherwise return null.

        def numList = [2, 5, 6, 10, 15, 20]
        println numList.findAll { item -> item > 6 }         // if found return multiple value.
        println numList.any { item -> item > 20 }            // if found return true otherwise false.
        println numList.every { item ->
            item

            def nameMap = ['name': "Jhon", 'subject': 'Groovy', 'id': 101]
            println nameMap.each { it }

        }
    }
    def str = "Hello"
    def myClousers = { name -> println "${str} This is ${name}" }

    def myClousers2 = {
        a, b, c -> return (a + b + c)
    }

    def myMethod(clos1) {
        clos1("Jhon")
    }


}


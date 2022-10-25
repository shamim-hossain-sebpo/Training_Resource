package Groovy_Documents

class String_05 {
    public static void main(String[] args) {

        String name = 'Md. Shamim Hossain'
        println 'This is ${name}'
        println "This is ${name}"   // interpolation always work with double quotation.

        String multiline = """ Hi, This is Md. Shamim Hossain 
                              I am from Bangladesh!
                              Hope you are well. """
        println multiline


        // retrive single char from string ----

        def chars = name.charAt(4)
        def chars2 = name[4]         // in Groovy doesn't required charAt() to retrive single char from string.
        println chars2.getClass().getName()

        println name[-1]            // printing last char from string.
        def chars3 = name[4..9]     // instead of subStr() 0 and 5 is including.
        println chars3.getClass().getName()
        println chars3
        assert chars == chars2
        println "$chars  $chars2"

        def chars4 = name[0, 4, 9]    // retrive random char from string.
        println chars4

        println name.substring(4)  // using substring();
        println name.subSequence(0, 9)       // beginning index is including and ending index is excluding.

        println name - (" Hossain")           // removing specific string.
        println name - ("ss")

        println name.replace("m", "t");
        def list = name.toList();             // Transform to String to list.
        println list




    }
}

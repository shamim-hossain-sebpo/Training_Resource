package Groovy_Documents

import com.sun.java.util.jar.pack.Instruction

class Conditional_Statement_03 {
    public static void main(String[] args) {

        boolean found = true;
        def found1 = isFound(found)
        println found1
        findValue(5)


    }

    static String isFound(found) {
        if (found) return "This Statement is True!";
        return "This Statement is False!"
    }

    static String findValue(input) {
        switch (input) {
            case 1:
                println "This is value ${input}";
                break;
            case 2:
                println "This is value ${input}";
                break;
            case 3:
                println "This is value ${input}";
                break;
            case [4, 5, 6]:
                println "This is value 4-6(multiple) ${input}";
                break;
            case { input > 6 && input < 9 }:
                println "This is value 7-8(condition) ${input}";
                break;
            default:
                println "This is default value!"
        }
    }
}

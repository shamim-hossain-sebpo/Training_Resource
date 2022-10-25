package Groovy_Documents

class Methods_06 {
    public static void main(String[] args) {

        def result = sum(10,20)
        println result
    }

    static def sum(a,b){          // here def is defing method return/no return type.
        def totalSum = a+b;
        return totalSum;
    }
}

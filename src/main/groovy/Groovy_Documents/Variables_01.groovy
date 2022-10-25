package Groovy_Documents

class Variables_01 {
    public static void main(String[] args) {


        //---- Dynamic type-------
        def name = 'name'
        name = 10
        println name.getClass().getName()

        int data = 20
        println data.getClass().getName()


        //---- Multiple Assignment -----
        println "-----Multiple Assignment!------"
        def(a,b,c) = ['Shamim',20,30]                           // Dynamic type for multiple assignment.
        println "${a} ${b} ${c}"
        println a.getClass().getName()

        def  (String d, int e, double f) = ["shamim",25,30.25]  // type is fixed for multiple assignment.
        println "${d} ${e} ${f}"
        println d.getClass().getName()


        //------ Interpolation ------
        def names = "Md. Shamim Hossain"
        println "My Name is ${names}"          // Interpolation always use with double quotation.
        println "My Name is $names"

        //----- Data Type and it's Default Value (Similar to Java) --------

        String str;
        int num;
        long num_long;
        double num_double;
        boolean isOk;

        println str
        println num
        println num_long
        println num_double
        println isOk

    }
}

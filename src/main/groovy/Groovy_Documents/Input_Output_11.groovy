package Groovy_Documents

class Input_Output_11 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in)
        print "Please Enter Value .."
        //String input =sc.nextLine()
        //println input

        def num =0
        def sum = num;
        for (int i = 0; i <5 ; i++) {
            num = sc.nextInt()
            sum += num
        }
        println sum


    }
}

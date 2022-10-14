package StringImplementation.PartA;

public class Problem_2 {
    public static void main(String[] args) {
        String str1 = "Honda Porsche Mercedes Ford BMW Bentley Bugatti Toyota Audi Mazda Volswagen Lamborgini Renault Volvo";
        String[] items ={"honda","lexus","mazda","bentley","hyundai","jeep","chevrolet"};

        for (int i = 0; i <items.length; i++) {
            if(str1.toLowerCase().contains(items[i])){
                System.out.print("Item "+(i+1)+":"+items[i]+" Inside str1:true ");
            }else{
                System.out.print("Item "+(i+1)+":"+items[i]+" Inside str1:false ");

            }
        }
        System.out.println();
    }
}

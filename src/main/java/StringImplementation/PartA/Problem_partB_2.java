package StringImplementation.PartA;

import java.util.Formatter;

public class Problem_partB_2 {
    public static void main(String[] args) {

        float num = 12.65465565655f;
        double num2 = 12.1165165716511335151;
        double precesionNum = Math.round(num * 10000) / 10000.0;       // Rounding the float number to 4 precesion.
        double precesionNum2 = Math.round(num2 * 1000000) / 1000000.0; // Rounding the double number to 6 precesion.
        System.out.println(precesionNum);
        System.out.println(precesionNum2);
    }
}

package StringImplementation.PartA;

public class Problem_5 {
    public static void main(String[] args) {
        String str1 = "Java is a high-level, class-based, object-oriented programming language that is designed to have as few implementation dependencies as possible.";
        String str2 = " fg Java is a high-level, class-based, object-oriented programming language that is designed to have as few implementation dependencies as possible.";
        System.out.println(str1.equalsIgnoreCase(str2));

        StringBuilder str3 = new StringBuilder("This is "); // StringBuilder and StringBuffer is mutable and can be append other string value.
        str3.append("Md. Shamim Hossain");
        System.out.println(str3);
    }
}

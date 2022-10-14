package StringImplementation.PartA;

public class Problem_3 {
    public static void main(String[] args) {
        String originalText = "123/I New Boston Road, Boston - 12132";
        String extractText  = " 123/I New Boston Rood, Boston - 12132 ";
        String correctionExtractText = extractText.replaceAll("Rood,", "Road,").trim();
        System.out.println(correctionExtractText);
        System.out.println(originalText.equals(correctionExtractText));

    }
}

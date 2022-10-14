package StringImplementation.PartA;

public class Problem_1 {
    public static void main(String[] args) {
        String cricketPlayers = "Tamim Iqbal, Bret Lee, Virender Shewag, Sachin Tendulkar, Ben Stocks";
        String[] cricketPalayersArray = cricketPlayers.split(",");

        for (int i = 0; i <cricketPalayersArray.length; i++) {
            System.out.print("Cricketer "+(i+1)+" - "+cricketPalayersArray[i].trim()+" ");
        }
    }
}

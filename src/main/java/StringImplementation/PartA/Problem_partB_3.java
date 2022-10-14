package StringImplementation.PartA;

import java.util.Arrays;

public class Problem_partB_3 {
    public static void main(String[] args) {
        String[] nameList = {"AFT Logistics, LLC n/k/a Equity One Contractors LLC", "BlueChip Financial d/b/a Spotloan", "Huhn, Douglas Joseph, Jr."};
        String[] reg = {"n/k/a", "f/k/a", "d/b/a"};
        final String STR_JR = "Jr.";
        final String STR_SR = "Sr.";

        for (int i = 0; i < nameList.length; i++) {
            for (int j = 0; j < reg.length; j++) {
                if (nameList[i].contains(reg[j]) || nameList[i].contains(STR_JR) || nameList[i].contains(STR_SR)) {
                    if (nameList[i].contains(reg[j])) {
                        nameList[i] = nameList[i].replace(reg[j], "Alias = ");

                    }
                    if (nameList[i].contains(STR_JR) || nameList[i].contains(STR_SR)) {
                        nameList[i] = nameList[i].replace(STR_JR, "");
                        nameList[i] = nameList[i].replace(STR_SR, "");
                    }
                    break;
                }
            }
            System.out.print("Name = "+nameList[i]+" ");
        }
        System.out.println();
    }
}

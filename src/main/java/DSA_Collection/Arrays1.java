package DSA_Collection;

public class Arrays1 {
    public static void main(String[] args) {
        int[] nums = {10, 5, 2, 84, 65, 32, 25};

        //------ Linear Search --------
        int find = 84;
        for (int i = 0; i < nums.length; i++) {
            if (find == nums[i]) {
                System.out.println("Found Value " + find + " at index: " + i);
                break;
            } else if (i == nums.length - 1) {
                System.out.println("Not Found Target Value " + find);
            }
        }

        // ----- 2D Array ---------
        int[][] array2D = {
                {2, 3, 5, 47, 12, 36, 6},
                {36, 2, 45, 12, 45, 25},
                {14, 65, 32, 15, 41, 3}
        };

        for (int i = 0; i < array2D.length; i++) {
            for (int j = 0; j < array2D[i].length; j++) {
                System.out.print(array2D[i][j] + " ");
            }
            System.out.println();
        }

        // -------  Circular Arrays---------

        String[] circularArrays = {"A", "B", "C", "D", "E", "G"};
        int lenght = circularArrays.length;
        for (int i = 2; i < 15; i++) {
            System.out.print(circularArrays[i % lenght] + " ");
        }
        System.out.println();

    }
}

package File_IO.problem

class CSVHandler {
    public static void main(String[] args) {

        String filePath = "/home/sazzad/Desktop/Jhon/Data3.csv"

        printRowWise(filePath)
    }



    static def printRowWise(def filePath) {

        BufferedReader reader = null;
        String line = "";
        String[] row
        int rowCount =0;

        try {

            reader = new BufferedReader(new FileReader(filePath));

            while ((line = reader.readLine()) != null) {
                if(rowCount == 0){
                    rowCount++
                    continue
                }
                row = line.split(",")
               // println line.replace(",", " ")

                println "${rowCount}Entry Name: ${row[0]} Country: ${row[1]} Address: ${row[2]}"
                rowCount++
            }

        } catch (Exception e) {
            e.printStackTrace()
        } finally {
            reader.close()
        }
    }

    static  def printColumnWise(def filePath){

    }
}

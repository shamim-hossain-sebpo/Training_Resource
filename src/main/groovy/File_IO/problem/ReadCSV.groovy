package File_IO.problem

class ReadCSV {
    public static void main(String[] args) {

        String filePath = "/home/sazzad/Desktop/Jhon/csvFile.csv"


        BufferedReader reader = null;
        String line = "";
        String[] row

        try {

            reader = new BufferedReader(new FileReader(filePath));

            while((line = reader.readLine())!= null){
                row = line.split(",")
                println line.replace(","," ")

//                for(i in row){
//                    print "$i\n"
//                }
            }

        } catch (Exception e) {
            e.printStackTrace()
        }finally{
            reader.close()
        }



    }
}

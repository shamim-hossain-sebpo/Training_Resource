package File_IO.problem

class CreateFile {
    public static void main(String[] args) {

        String folderPath= "/home/sazzad/Desktop/Jhon/javaFile_IO_Problem"
        File folder = new File(folderPath)
        File file = new File("$folderPath/ListOfCountries.txt")

        if(!folder.isDirectory()){
            folder.mkdir()
            file.createNewFile()
        }else{
            file.createNewFile()
        }

        //---- write to file -----
        def input_str = "Bangladesh India Bhutan Russia China Mongolia France Italy Germany hungary Turkey Egypt Mexico Romania ethiopia"

        //storeToFile(input_str,file)
        printDataFromFile(file)


    }

    static def storeToFile(def input_str,def file){
        def inputList  = input_str.split(" ")
        for (int i = 0; i <inputList.size(); i++) {
            inputList[i]= inputList[i].replace(inputList[i][0],inputList[i][0].toUpperCase())
            file.append(inputList[i]+"\n")
        }
    }

    static def printDataFromFile(def file){
        file.eachLine{line,i ->
            println "$i | $line"
        }
    }
}

package File_IO.problem

class FileReadWrite {
    public static void main(String[] args) {
        String filePath = "/home/sazzad/Desktop/Jhon/SEBPO.txt"
        File file = new File(filePath)
        println file.isFile()

        // writing to file
        Scanner sc = new Scanner(System.in)
        println 'Please Enter String Value..'
        String input = sc.nextLine()
        file.append("$input\n")

        // Reading from file

        file.eachLine {line,i->          // Read line by line.
            println "$i | $line"
        }
    }
}

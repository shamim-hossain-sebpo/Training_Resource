package Groovy_Documents

class FileReadWrite_12 {
    public static void main(String[] args) {
        String filePath = ("/home/sazzad/Documents/groovyTest/src/main/groovy/Groovy_Documents/data1")
        File file = new File(filePath)

        println file.text

        def list = file.collect { it }      // read file data and store to list
        println list

        def array = file as String[]      // read file data and store to array.
        println array

        def list_str = file.readLines()   // read file data and store to String list.
        println list_str


        file.eachLine { line, i -> println "Line ${i} |$line" }  // read line by line using clousers.

        def lineRanges = 2..3

        file.eachLine { line, i ->
            if (lineRanges.contains(i)) println "Line ${i} | $line"      // read specific line from file.
        }


        def line
        file.withReader { reader ->                          // read line with reader.
            while ((line = reader.readLine()) != null) {
                println "$line"
            }
        }

        // ------- reading with new reader -------
        def filePath2 = "/home/sazzad/Documents/groovyTest/src/main/groovy/Groovy_Documents/data2"
        def reader = file.newReader()
        //File file2 = new File(filePath2).append(reader)
        File file2 = new File(filePath2)
        println file2.text
        reader.close()

        byte[] content = file2.bytes
        println content

        println file2.isFile()
        println file2.isDirectory()

        //---- copy file data to another file -----

        //def filePath3 = "/home/sazzad/Documents/groovyTest/src/main/groovy/Groovy_Documents/data3"
        //def newFile = new File(filePath3)
        //newFile << file2.text


        //----- writing file -------
        file.write("This is Jhon!\n")
        file << "How are you?\n"
        println file.text
        file.text = 'This is Jhon!'           // Override everything.

        file.withWriter {writer->
            writer.write('This is Rahima!')
        }

        file.append('\nThis is jhon')



    }

}

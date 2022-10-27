package File_IO.problem

class ReadMultipleFile {
    public static void main(String[] args) {

        String filePath = "/home/sazzad/Desktop/MultipleFile"
        File folder = new File(filePath)

        folder.eachFile {file->

            new File(file.getAbsolutePath()).eachLine {line-> println line}
        }
    }
}

package File_IO.problem

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class XLSHandler {
    public static void main(String[] args) {

        String filePath = "/home/sazzad/Desktop/Jhon/Data1.xls"
        printRowWise(filePath)
        printColumWise(filePath)

    }

    static def printRowWise(def filePath) {

        try {

            XSSFWorkbook workbook = new XSSFWorkbook(filePath)
            XSSFSheet sheet = workbook.getSheet("sheet1")
            int rowcount = sheet.getPhysicalNumberOfRows()

            DataFormatter formatter = new DataFormatter()
//            def value = formatter.formatCellValue(sheet.getRow(1).getCell(0))
//            println value
            int rowcounti = 0;

            for (Row row : sheet) {
                if (rowcounti == 0) {
                    rowcounti++
                    continue
                }
                def cellCount = row.getPhysicalNumberOfCells()
                println "${rowcounti++}Entry : Name: ${row.getCell(0)} Country: ${row.getCell(1)} Address: ${row.getCell(2)} "

            }

        } catch (Exception e) {
            e.printStackTrace()
        }
    }


    static def printColumWise(def filePath) {
        try {

            XSSFWorkbook workbook = new XSSFWorkbook(filePath)
            XSSFSheet sheet = workbook.getSheet("sheet1")
            int rowcount = sheet.getPhysicalNumberOfRows()

            DataFormatter formatter = new DataFormatter()
            def value = formatter.formatCellValue(sheet.getRow(1).getCell(0))
            int rowcounti = 0;

            int columCount = sheet.getRow(0).getPhysicalNumberOfCells()
            println "$columCount   $rowcount"

            for (int i = 0; i < columCount; i++) {
                print "${i + 1} Column = "
                for (int j = 1; j < rowcount; j++) {
                    value = formatter.formatCellValue(sheet.getRow(j).getCell(i))
                    print value + ", "
                }
                println ""

            }


        } catch (Exception e) {
            e.printStackTrace()
        }

    }
}

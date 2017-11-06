package az.mm.arbitrage.data;

import az.mm.arbitrage.factory.Data;
import az.mm.arbitrage.model.Bank;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author MM
 */
public class ExcelData extends Data {

    @Override
    public List<Bank> getBankList() {
        List<Bank> bankList = new ArrayList<>();
        Bank b;

        try(FileInputStream file = new FileInputStream(new File("C:\\Users\\MM\\Desktop\\arbitrage.xls")); ) {

            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() == 0) continue;

                b = new Bank(row.getRowNum(),
                        getCellValue(row, 0),
                        getCellValue(row, 1),
                        getCellValue(row, 2),
                        getCellValue(row, 3),
                        getCellValue(row, 4),
                        getCellValue(row, 5),
                        getCellValue(row, 6),
                        getCellValue(row, 7),
                        getCellValue(row, 8),
                        getCellValue(row, 9),
                        getCellValue(row, 10));
                bankList.add(b);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } 

        return bankList;
    }

    
    private <T> T getCellValue(Row row, int i){
        Cell cell = row.getCell(i);
        Object cellValue;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            case Cell.CELL_TYPE_STRING:
                cellValue = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                cellValue = cell.getBooleanCellValue() ? true : false;
                break;
            default: cellValue = -1;
        }
        
        return (T)cellValue;
    } 

}

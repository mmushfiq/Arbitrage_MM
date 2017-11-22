package az.mm.arbitrage.data;

import az.mm.arbitrage.exceptionHandler.ExceptionHandler;
import az.mm.arbitrage.factory.Data;
import az.mm.arbitrage.model.Bank;
import az.mm.arbitrage.resources.Props;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class ExcelData extends Data {
    private int dataId;
    private LocalDate date;

    public ExcelData(int dataId) {
        this.dataId = dataId;
    }
    
    
    @Override
    public int getDataId() {
        return dataId;
    }
    
    @Override
    public LocalDate getDate() {
        return date;
    }
    

    @Override
    public List<Bank> getBankList() {
        List<Bank> bankList = new ArrayList<>();
        Bank b = null;

        try(FileInputStream file = new FileInputStream(new File(Props.getInstance().getProperty("source.excel"))); ) {

            Workbook workbook = new HSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() == 0) continue;

                b = new Bank(getCellValue(row, 0),
                        getCellValue(row, 1),
                        getCellValue(row, 2),
                        getCellValue(row, 3),
                        getCellValue(row, 4),
                        getCellValue(row, 5),
                        getCellValue(row, 6),
                        getCellValue(row, 7),
                        getCellValue(row, 8),
                        getCellValue(row, 9),
                        getCellValue(row, 10),
                        new java.sql.Date(row.getCell(11).getDateCellValue().getTime()).toLocalDate());
                bankList.add(b);
            }

        } catch (IOException ex) {
            ExceptionHandler.catchMessage(this, new Object(){}.getClass().getEnclosingMethod().getName(), ex);
        }
        if(b != null) date = b.getDate();

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

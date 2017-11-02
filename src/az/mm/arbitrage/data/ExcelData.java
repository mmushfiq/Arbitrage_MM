package az.mm.arbitrage.data;

import az.mm.arbitrage.factory.Data;
import az.mm.arbitrage.model.Bank;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author MM
 */
public class ExcelData extends Data {

    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    public final static List<Bank> bankList;

    static { bankList = fillBankList(); }

    @Override
    public List<Bank> getBankList() {
        return bankList;
    }
    
    
    public static List<Bank> fillBankList() {
        List<Bank> bankList = new ArrayList<>();
        Bank b;
        FileInputStream file = null;

        try {
            file = new FileInputStream(new File("C:\\Users\\MM\\Desktop\\arbitrage.xlsx")); 
//            file = new FileInputStream(new File("C:\\Users\\User\\Desktop\\arbitrage.xlsx")); 

            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(3);

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() == 0) continue;

                b = new Bank(row.getRowNum(),
                        getCellStringValue(row, 0),
                        getCellValue(row, 1),
                        getCellValue(row, 2),
                        getCellValue(row, 3),
                        getCellValue(row, 4),
                        getCellValue(row, 5),
                        getCellValue(row, 6),
                        getCellValue(row, 7),
                        getCellValue(row, 8),
                        getCellValue(row, 9),
                        getCellValue(row, 10)
                );
                bankList.add(b);
            }

        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        } finally  {
            try {
                if(file != null) file.close();
            } catch (IOException ex) {
                Logger.getLogger(ExcelData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return bankList;
    }

    
    private static double getCellValue(Row row, int i) {
        Cell kodcell = row.getCell(i);
        if (kodcell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return kodcell.getNumericCellValue();
        }

        return -654;
    }

    //bunu deyisecem..
    private static String getCellStringValue(Row row, int i) {
        Cell kodcell = row.getCell(i);
        String cellvalue = null;
        switch (kodcell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                if (kodcell.getBooleanCellValue()) cellvalue = "true";
                else cellvalue = "false";
                break;
            case Cell.CELL_TYPE_NUMERIC:
                cellvalue = Double.toString((double) kodcell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING:
                cellvalue = kodcell.getStringCellValue();
                break;
        }
        return cellvalue;
    }


    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        new ExcelData().printExcellSenaye(new ArrayList<>());
        
        if(true) return;
        
        FileInputStream file = new FileInputStream(new File("C:\\Users\\MM\\Desktop\\arbitrage.xlsx"));
//        file = new FileInputStream(new File("./arbitrage.xls")); 
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(3);

        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            if (row.getRowNum() == 0) {
                continue;
            }

            System.out.print(row.getRowNum() + "\t");
            System.out.print(getCellStringValue(row, 0) + "\t\t\t");
            System.out.print(getCellValue(row, 1) + "\t");
            System.out.print(getCellValue(row, 2) + "\t");
            System.out.print(getCellValue(row, 3) + "\t");
            System.out.print(getCellValue(row, 4) + "\t");
            System.out.print(getCellValue(row, 5) + "\t");
            System.out.print(getCellValue(row, 6) + "\t");
            System.out.print(getCellValue(row, 7) + "\t");
            System.out.print(getCellValue(row, 8) + "\t");
            System.out.print(getCellValue(row, 9) + "\t");
            System.out.print(getCellValue(row, 10) + "\t");
//            System.out.println(getCellValue(row, 11) + "\t");
//            System.out.println(getCellValue(row, 12) + "\t");
            System.out.println(row.getCell(11).getDateCellValue() == null ? "" : formatter.format(row.getCell(11).getDateCellValue()));
//            System.out.println(row.getCell(6).getDateCellValue() == null ? "" : formatter.format(row.getCell(6).getDateCellValue()));
//            System.out.println(row.getCell(5).getNumericCellValue());

        }

    }
    
    
    //test uchun..
    public String printExcellSenaye(List<List<Object>> list){
        System.out.println("printExcellSenaye metoduna girdi");
//        File f = new File("./table");
        File f = new File("./arbitrage");

        String fileName = f.toString();
        InputStream input = getClass().getResourceAsStream(fileName);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("firstSheet");

        HSSFRow rowhead = sheet.createRow((short) 0);
        rowhead.createCell(0).setCellValue("KOD");
        rowhead.createCell(1).setCellValue("ERAZI");
        rowhead.createCell(2).setCellValue("FN");
        for (int i = 1; i <=25; i++) {
            rowhead.createCell(2+i).setCellValue("A"+i);
        }
        rowhead.createCell(28).setCellValue("N1");
        rowhead.createCell(29).setCellValue("NH1");
        rowhead.createCell(30).setCellValue("N2");
        rowhead.createCell(31).setCellValue("NH2");
        HSSFRow row;
 
        for (List<Object> l : list) {
            row = sheet.createRow(sheet.getLastRowNum()+1);
            for (int i = 0; i < 32; i++) {
                row.createCell(i).setCellValue(String.valueOf(l.get(i)));
            }
        }
        

        try (FileOutputStream fileOut = new FileOutputStream(fileName);) {
            workbook.write(fileOut);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return f.toString();
    }

}

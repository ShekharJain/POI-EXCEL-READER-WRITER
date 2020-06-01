package la.jain;

import la.jain.Exception.IncorrectFileExtensionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExcelReaderWriter {

    private static final int COUNTCOLUMN = 0;
    private static final int CODECOLUMN = 1;
    private static final int NAMECOLUMN = 2;
    private static final int CAPITALCOLUMN = 3;
    private static final int CONTINENTCOLUMN = 4;

    private static final Logger log = LogManager.getLogger();

    private static void writeCountryListToFile(String fileName, List<Country> countryList) throws IncorrectFileExtensionException, IOException{
        final Workbook workbook;
        int rowIndex;

        if (fileName.endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        } else if (fileName.endsWith("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            throw new IncorrectFileExtensionException("Invalid Write To File Name : " + fileName);
        }

        //Create a sheet and set the column widths
        Sheet sheet = workbook.createSheet("Countries");
        sheet.setColumnWidth(COUNTCOLUMN,256*6);
        sheet.setColumnWidth(CODECOLUMN,256*10);
        sheet.setColumnWidth(NAMECOLUMN, 256*30);
        sheet.setColumnWidth(CAPITALCOLUMN, 256*30);
        sheet.setColumnWidth(CONTINENTCOLUMN, 256*15);

        rowIndex = 0;
        for (Country country : countryList) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(COUNTCOLUMN).setCellValue(rowIndex);
            row.createCell(NAMECOLUMN).setCellValue(country.getName());
            row.createCell(CODECOLUMN).setCellValue(country.getShortCode());
            row.createCell(CAPITALCOLUMN).setCellValue(country.getCapital());
            row.createCell(CONTINENTCOLUMN).setCellValue(country.getContinent());
        }

        FileOutputStream fos = new FileOutputStream(fileName);
        workbook.write(fos);
        fos.close();
    }

    private static List<Country> readExcelData(String fileName) throws IOException {
        MessageFormat sheetMessageFormat = new MessageFormat("Processed Sheet {2} Name:{0} Rows:{1}");
        final List<Country> countries = new ArrayList<>();
        int sheetNumber = 0;

        FileInputStream fis = new FileInputStream(fileName);
        Workbook workbook = null;
        if (fileName.toLowerCase().endsWith("xlsx")) {
            workbook = new XSSFWorkbook(fis);
        } else if (fileName.toLowerCase().endsWith("xls")) {
            workbook = new HSSFWorkbook(fis);
        }
        //loop through each of the sheets
        for (Sheet sheet:workbook) {
            sheetNumber++;
            for (Row row : sheet) {
                Country c = new Country(sheet.getSheetName());
                for (Cell cell : row) {
                    switch (cell.getColumnIndex()) {
                        case CODECOLUMN:
                            c.setShortCode(cell.getStringCellValue().trim());
                            break;
                        case NAMECOLUMN:
                            c.setName(cell.getStringCellValue().trim());
                            break;
                        case CAPITALCOLUMN:
                            c.setCapital(cell.getStringCellValue().trim());
                            break;
                        default:
                            log.info("Random data::" + cell.getStringCellValue());
                    }
                }
                countries.add(c);
            }
            log.info(sheetMessageFormat.format(new Object[]{sheet.getSheetName(), sheet.getLastRowNum()+1, sheetNumber}));
        }
        fis.close();
        return countries;
    }

    /**
     *
     * @param args 0-Name of Excel file to read 1-Name of Excel file to write
     * @throws Exception Run time exception
     */
    public static void main(String args[]) throws Exception {
        try {
            List<Country> countryList = ExcelReaderWriter.readExcelData(args[0]);
            if (countryList.size() > 0) {
                Collections.sort(countryList);
                countryList.forEach((country) -> log.info(country.toString()));
                ExcelReaderWriter.writeCountryListToFile(args[1], countryList);
                log.info(MessageFormat.format("Wrote Excel File : {1} : {0} records", countryList.size(), args[1]));
            }
        } catch (FileNotFoundException e) {
            log.error(MessageFormat.format("Input File {0} Not Found", args[0]));
        } catch (IndexOutOfBoundsException i) {
            log.error("Required arguments not passed");
        } catch (IncorrectFileExtensionException ext) {
            log.error(ext.getLocalizedMessage());
        }
    }
}

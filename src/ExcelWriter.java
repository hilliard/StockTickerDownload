import org.apache.poi.hssf.record.chart.ObjectLinkRecord;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hilliard on 1/7/2017.
 */
public class ExcelWriter {

    // this class is for writing an excel spreadsheet using library called POI

    // POI is an apache library, which you can download from the internet
    // Java libraries are dist in jar files
    // that need to be added to the project.


    private OneDayMarketAction m_odma;

    public ExcelWriter(OneDayMarketAction odma) {
        this.m_odma = odma;
    }

    // a constructor
    public void createFile(String excelFileName) {
        // 1. get a list of tickers that move the most today
        List<OneDayMarketAction.OneTickerOneDay> otod = m_odma.getSortedMovers();
        // 2. Next create a data structure with the info

        // 2a. create an excel workbook structure (in POI)
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 2b. create a worksheet inside the workbook;
        HSSFSheet worksheet = workbook.createSheet("Summary");
        // 2c. get the data we want
        Map<String, Object[]> dataInRows = new HashMap<>();

        // key row=number, value=object array of values to write to excel
        // header row
        dataInRows.put("1", new Object[]{"Ticker", "Close", "Prev Close", "%Change"});
        dataInRows.put("2", new Object[]{
                otod.get(0).getM_ticker(),
                otod.get(0).getM_close(),
                otod.get(0).getM_prevClose(),
                otod.get(0).getPctChange()});

        dataInRows.put("3", new Object[]{
                otod.get(1).getM_ticker(),
                otod.get(1).getM_close(),
                otod.get(1).getM_prevClose(),
                otod.get(1).getPctChange()});

        dataInRows.put("4", new Object[]{
                otod.get(2).getM_ticker(),
                otod.get(2).getM_close(),
                otod.get(2).getM_prevClose(),
                otod.get(2).getPctChange()});

        dataInRows.put("5", new Object[]{
                otod.get(3).getM_ticker(),
                otod.get(3).getM_close(),
                otod.get(3).getM_prevClose(),
                otod.get(3).getPctChange()});

        dataInRows.put("6", new Object[]{
                otod.get(4).getM_ticker(),
                otod.get(4).getM_close(),
                otod.get(4).getM_prevClose(),
                otod.get(4).getPctChange()});

        // 2d write the values to cells in the worksheet
        for (int rowNum = 0; rowNum<6; rowNum++) {
            // convert row into string
            String key = (rowNum + 1 ) + "";
            // create a row
            Row row = worksheet.createRow(rowNum + 1);
            Object[] values = dataInRows.get(key);

            // create cell object
            int cellNum =0;
            for (Object oneObject : values) {
                Cell cell = row.createCell(cellNum++);
                if (oneObject instanceof String) {
                    cell.setCellValue((String) oneObject);
                }
                else if (oneObject instanceof Double) {
                    cell.setCellValue((Double) oneObject);
                }
                else if (oneObject instanceof Data) {
                    cell.setCellValue((Date) oneObject);
                }
                else if (oneObject instanceof Boolean) {
                    cell.setCellValue((Boolean) oneObject);
                }

            }

        }

        // 3 use  Poi to actually write the file
        try {
            FileOutputStream fos = new FileOutputStream(new File(excelFileName));
            workbook.write(fos);
            fos.close();
            System.out.println("Excel File Written Succesfully");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }










    }




}

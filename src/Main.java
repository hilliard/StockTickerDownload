import java.util.List;

public class Main {

    public static void main(String[] args) {
        // my code here
        try {
            // String urlString = "http://www.nseindia.com/content/historical/EQUITIES/2015/JUL/cm17JUL2015bhav.csv.zip";
            String urlString = "https://www.nseindia.com/content/historical/EQUITIES/2017/JAN/cm06JAN2017bhav.csv.zip";
            String zipFilePath = "C:/Users/hilliard/Downloads/boo.zip";
            String destinationDirectory = "C:/Users/hilliard/Downloads";

            List<String> unzippedFileList = UnzipUtility.downloadAndUnzip(urlString, zipFilePath, destinationDirectory);

            if (unzippedFileList != null){
                String csvFile = unzippedFileList.get(0);
                OneDayMarketAction odma = new OneDayMarketAction(csvFile);
                // use the constructor class we just defined
                List<OneDayMarketAction.OneTickerOneDay> listOfMovers = odma.getSortedMovers();

                for (OneDayMarketAction.OneTickerOneDay otod:listOfMovers) {
                    System.out.println("Ticker=" + otod.getM_ticker() + ", Moved by: " + otod.getPctChange() * 100 + "%");

                    // Ok lets actually create the excel file
                    ExcelWriter xlWriter = new ExcelWriter(odma);
                    xlWriter.createFile("C:/Users/hilliard/Documents/StockDownload.xls");

                }

                System.out.println("All Done");
            }
        }
        catch (Exception ex){
            ex.printStackTrace();

        }
    }
}

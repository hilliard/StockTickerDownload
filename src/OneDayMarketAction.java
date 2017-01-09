
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Created by hilliard on 1/7/2017.
 */
public class OneDayMarketAction {

    // this class rep the NSE market action on one given day

    // within this class we wil create an inner class to represent the market action for one day
    public static class OneTickerOneDay {

        private String m_ticker;
        private String m_series;
        private double m_open;
        private double m_close;
        private double m_high;
        private double m_low;
        private double m_prevClose;

        public OneTickerOneDay(String[] oneQuote) {
            // this is the constructor
            // generate getters and setters
            setM_ticker(oneQuote[0]);
            setM_series(oneQuote[1]);
            setM_open(Double.parseDouble(oneQuote[2])); // took in a string, need to convert to double
            setM_close(Double.parseDouble(oneQuote[3])); // took in a string, need to convert to double
            setM_high(Double.parseDouble(oneQuote[4])); // took in a string, need to convert to double
            setM_low(Double.parseDouble(oneQuote[5])); // took in a string, need to convert to double
            setM_prevClose(Double.parseDouble(oneQuote[6])); // took in a string, need to convert to double

        }


        public String getM_ticker() {
            return m_ticker;
        }

        public void setM_ticker(String m_ticker) {
            this.m_ticker = m_ticker;
        }

        public String getM_series() {
            return m_series;
        }

        public void setM_series(String m_series) {
            this.m_series = m_series;
        }

        public double getM_open() {
            return m_open;
        }

        public void setM_open(double m_open) {
            this.m_open = m_open;
        }

        public double getM_close() {
            return m_close;
        }

        public void setM_close(double m_close) {
            this.m_close = m_close;
        }

        public double getM_high() {
            return m_high;
        }

        public void setM_high(double m_high) {
            this.m_high = m_high;
        }

        public double getM_low() {
            return m_low;
        }

        public void setM_low(double m_low) {
            this.m_low = m_low;
        }

        public double getM_prevClose() {
            return m_prevClose;
        }

        public void setM_prevClose(double m_prevClose) {
            this.m_prevClose = m_prevClose;
        }

        // create a member function to return the % change on a given day
        public  double getPctChange() {
            if (this.getM_prevClose() != 0) {
                return (this.getM_close() - this.getM_prevClose()) / this.getM_prevClose();
            }
            return Double.NaN;
        }
    }

    private Map<String, OneTickerOneDay> mapOfTickers = new HashMap<>();
    // internally we want an easy way to say please give me the market action today for "XYZ"
    // A map is a key value container
    // key is ticker name

    private String m_fileName;

    public OneDayMarketAction(String csvFile) {

        // 1. parse the csv file (which came from UnzipUtility)
        // 2. go through the file line by line and create objects of OneTickerOneDay
        // 3. populate the internal map we just set up

        // 1. parse the csv file
        this.m_fileName = csvFile;
        BufferedReader br = null;
        String line = null;

        // define the char we wish to split the lines on
        String csvSplitBy = ",";
        // define int to keep track
        int lineNum = 0;

        try {
            // create file handle to read csv file
            br = new BufferedReader(new FileReader(csvFile));
            // while loop read until empty
            // here line is the iterator and br.readline will execute

            while ((line = br.readLine()) != null) {
                // keep track of lineNum to make sure we can skip the header
                lineNum++;
                // if lineNum is 1 don't do anything this is the header
                if (lineNum > 1) {
                    // split into an array of strings
                    String[] oneQuote = line.split(csvSplitBy);
                    OneDayMarketAction.OneTickerOneDay otod = new OneDayMarketAction.OneTickerOneDay(oneQuote);
                    // only care about equities "EQ"
                    if (otod.getM_series().compareTo("EQ") == 0) {

                        mapOfTickers.put(oneQuote[0], otod);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    } // end OneDayMarketAction

        // now let's create a member function that returns tickers for that date, sorted in order
        // of market action.
        // How will we do this? Simple :) by using the comparator class we just set up
        public List<OneTickerOneDay> getSortedMovers(){
            // create an unsorted list
            List<OneTickerOneDay> listOfMarketAction = new ArrayList<>(this.mapOfTickers.values());
            // we have a map(key-value pairs container) of ticker, OneTickerOneDay object
            // we took all the values which is a set of OneTickerOneDay objects
            // and used that set to create a list. we went from map to list.
            Collections.sort(listOfMarketAction, new StockMoveComparator());

            return listOfMarketAction;
        }

        // We forgot to setup the comparator class, lets do that now.

        // Specifically that means it will have a function called compare, which takes 2 objects and returns 1
        // if the first is 'greater' than the second., returns -1 if the second is 'greater' than the first and
        // returns 0 if the two objects are equal.
        // In this implementation we will judge the classes by teh % change in the stock that day.
        // This comparator specifically compares objects os class OneTickerOneDay


        public static class StockMoveComparator implements Comparator<OneTickerOneDay> {

            public int compare(OneTickerOneDay t1, OneTickerOneDay t2) {
                double pctChange1 = t1.getPctChange();
                double pctChange2 = t2.getPctChange();
                if (pctChange1>pctChange2) {
                    return 1;
                }
                else if (pctChange1<pctChange2){
                    return -1;
                }
                return 0;
            }

        }

 } // end OneDayMarketAction




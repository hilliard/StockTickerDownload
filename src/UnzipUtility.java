import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by hilliard on 1/7/2017.
 */
public class UnzipUtility {

    // use this to abstract the rest of the code
    // 1. download the zip file
    // 2. unzip the file from the NSE website to some location on my system

    // note the order, we decided what member functions we needed
    // then we defined out interface for these functions, what inputs and outputs we would like

    // all member functions are static, can be called without instantiating an object

    private  static final int S_BYTE_SIZE = 4096;

    public  static List<String> downloadAndUnzip(String urlString, String zipFilePath, String destDirectory)
    throws IOException {
        //this is the actual method that downloads before we can unzip
        URL tariff = new URL(urlString);
        // the NSE will reject an attempt to download this file unless it has a user-agent property set
        String myUserAgentString = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";
        java.net.URLConnection c = tariff.openConnection();
        c.setRequestProperty("User-agent", myUserAgentString);

        // more boiler plate
        // save the download url to local disk
        ReadableByteChannel zipByteChannel = Channels.newChannel(c.getInputStream());
        FileOutputStream fos = new FileOutputStream(zipFilePath);
        fos.getChannel().transferFrom(zipByteChannel, 0, Long.MAX_VALUE);

        // now lets actually unzip the file
        return unzip(zipFilePath, destDirectory);
    }

    public static List<String> unzip(String zipFilePath, String desDirectory) throws IOException {
        // this will take the name of a zip file and the name of a directory to extract to
        // and return a list of the files that were unzipped

        List<String> unzippedFileList = new ArrayList<>();

        File destDir = new File(desDirectory);

        if (!destDir.exists()) {
            destDir.mkdir();
        }

        // now iterate over entries in zip file
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));

        ZipEntry zipEntry = zipIn.getNextEntry();
        while (zipEntry !=null){
            // construct the path to extract the file to
            String filePath = desDirectory+File.separator+zipEntry.getName();

            if (!zipEntry.isDirectory()) {
                String oneUnzippedFile = extractFile(zipIn, filePath);
                // add file to list to keep track
                unzippedFileList.add(oneUnzippedFile);
            }
            else {
                // if the file is a directory, make the directory and keep going
                File dir = new File(filePath);
                dir.mkdir();
            }
            // advance the iterator to the next value
            zipEntry = zipIn.getNextEntry();
        }
        return unzippedFileList;

    }

    private static String extractFile(ZipInputStream zipIn, String filePath) throws IOException {

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        // pretty standard boilerplace for anytime we need to save a file
        byte[] bytesIn = new byte[S_BYTE_SIZE];
        // we typically need to create (binary) files in chunks of bytes
        // Chunks need to be in powers of 2 so don't hard code, use static member variable

        // low level boiler plate to save file

        int read = 0;
        while ((read=zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn,0,read);
            // read tells us how many bytes were read, if its done we get a -1
        }
        bos.close();
        return filePath;
        // return the filePath so the calling code can keep track of it

    }




}

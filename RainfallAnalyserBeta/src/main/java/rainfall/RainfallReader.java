package rainfall;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Objects;

public class RainfallReader {
    // Main part of the program:
    public static void main(String[] args) throws IOException {
        // Initialize the variables.
        String csvFileName;
        String analyzedFileName;
        double[] monthlyTotal = new double[12];
        double[] dailyMin = new double[12];
        double[] dailyMax = new double[12];
        final String[] filePathList = {"src/main/Copperlode_Dam_Data.csv", "src/main/Kuranda_Railway_Data.csv", "src/main/Tinaroo_Falls_Dam_Data.csv"};
        final String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        displayWelcomeMessages(filePathList); // Display welcome messages.

        // Calling methods in and from other classes to load, process, and hold the rainfall data.
        // The for loop will iterate to load, process, and hold the rainfall data from multiple CSV files.
        for (int i = 0; i < filePathList.length; i++) {
            csvFileName = filePathList[i].substring(9); // Get the name of the CSV file.
            RainfallProcessData csvTempFile = new RainfallProcessData(); // Create a RainfallProcessData object.
            CSVReadData(csvTempFile, filePathList[i]); // Use method to read data from CSV file.
            loadProcessedData(csvTempFile, monthlyTotal, dailyMin, dailyMax); // Load the processed data to memory.
            CSVPrintData(csvFileName, monthName, monthlyTotal, dailyMin, dailyMax); // Display the processed data.

            analyzedFileName = "data" + (i + 1) + ".txt"; // Set the name of a file where the analyzed result will be saved.
            RainfallSaveFile saveFile = new RainfallSaveFile(analyzedFileName, monthName, monthlyTotal, dailyMin, dailyMax);
            saveFile.saveFile(); // Save analyzed file in a new txt file named analyzedFileName.
            saveFile.displaySavedMessage(); // Tell the user the file is saved successfully and to which file.
        }
        displayEndingMessages(); // Display ending message after display all the analyzed information.
    }

    // Methods used:

    /**
     * Read data from the pass in the CSV file, and use RainfallProcessData object to process the data with exception handling. The processed data will be stored in the RainfallProcessData object afterward.
     */
    private static void CSVReadData(RainfallProcessData csvTempFile, String fileName) {
        try {
            // Read data from the CSV file.
            Reader inFile = new FileReader(fileName);
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(inFile);
            // Use RainfallProcessData object to process data.
            for (CSVRecord record : records) {
                int monthNum = Integer.parseInt(record.get("Month")); // Get the month number of the rainfall.
                String rainfall = record.get("Rainfall amount (millimetres)"); // Get the rainfall number.

                if (!Objects.equals(rainfall, "")) {
                    // Process data with RainfallProcessData object。
                    double rainfallNum = Double.parseDouble(rainfall);
                    csvTempFile.calcMonthlyTotal(monthNum, rainfallNum); // Calculate monthly total rainfall.
                    csvTempFile.calcDailyMin(monthNum, rainfallNum); // Calculate monthly daily minimum rainfall.
                    csvTempFile.calcDailyMax(monthNum, rainfallNum); // Calculate monthly daily maximum rainfall.
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found."); // Display the message when the CSV file is not found.
        } catch (NumberFormatException e) {
            System.out.println("Incorrect Data Format."); // Display the message when the data format is wrong.
        } catch (Exception e) {
            System.out.println("Error: there was an issue."); // Display the message when any other errors occur.
        }
    }

    /**
     * Load the processed data, monthly total, minimum, and maximum rainfall from the RainfallProcessData object.
     */
    private static void loadProcessedData(RainfallProcessData csvTempFile, double[] monthlyTotal, double[] dailyMin, double[] dailyMax) {
        for (int i = 0; i < 12; i++) {
            // Iterate 12 times, each time get one monthly total, minimum, and maximum rainfall.
            monthlyTotal[i] = csvTempFile.getMonthlyTotal(i); // Get monthly total rainfall.
            dailyMin[i] = csvTempFile.getDailyMin(i); // Get monthly daily minimum rainfall.
            dailyMax[i] = csvTempFile.getDailyMax(i);// Get monthly daily maximum rainfall.
        }
    }

    /**
     * Print the analyzed data including monthly total, daily minimum abd daily maximum rainfall of a CSV file.
     */
    private static void CSVPrintData(String csvFileName, String[] month, double[] monthlyTotal, double[] dailyMin, double[] dailyMax) {
        System.out.printf("For CSV file <%s>:\n", csvFileName);
        for (int i = 0; i < 12; i++) {
            // Iterate 12 times (as there are 12 months). In each iteration, the analysis result that shows the monthly total, minimum, and maximum rainfall will display.
            System.out.printf("%-11s: Total rainfall = %8.2f, Min rainfall = %5.2f, Max rainfall = %-6.2f\n", month[i], monthlyTotal[i], dailyMin[i], dailyMax[i]);
        }
    }

    /**
     * Display welcome messages that show all the csv files going to be analyzed.
     */
    private static void displayWelcomeMessages(String[] filePathList) {
        // Welcome message
        System.out.println("Welcome to RainfallAnalyser Alpha-Release.");
        System.out.println("-------------------------------------------------------------------------------"); // Leave a line between two messages

        // Display all the CSV filenames that going to be analyzed.
        System.out.printf("There are %d files going to be analyzed. They are:\n", filePathList.length);
        for (int i = 0; i < filePathList.length; i++) {
            int fileNumber = i + 1;
            String csvFileName = filePathList[i].substring(9); // Get the name of the CSV file.
            System.out.printf("%d. %s\n", fileNumber, csvFileName);
        }
        System.out.println(); // Leave a line between two messages

        // Processing message
        System.out.println("Analysing Data...");
        System.out.println("Analysing Complete.");
        System.out.println("-------------------------------------------------------------------------------"); // Leave a line between two messages
        System.out.println(); // Leave a line between two messages
    }

    /**
     * Display ending messages of the program.
     */
    private static void displayEndingMessages() {
        System.out.println("-------------------------------------------------------------------------------"); // Leave a line between two messages
        System.out.println("End of analysis. Thank you for use"); // Closing message
    }
}
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;

/**
 * CP2406 Assignment - Tianyi Zhang
 * Alpha release
 * This program will get rainfall data from a given CSV file, then analyze the rainfall for the year user selected.
 * The analysis will be display in text form in this program, and will also store into a txt file for RainfallVisualiser to process.
 */

public class RainfallAnalyser {
    public static void main(String[] args) {

        // Initialize using variables.
        final int totalMonthNumber = 12;
        final String analysisFilename = "src/main/resources/Data Copperlode Dam.csv";
        final String analysisResultFilename = "src/main/resources/data.txt";
        final String analysisResultStoreColumnNames = "year,month,total,minimum,maximum\n";
        String selectedYear;
        Scanner scanner = new Scanner(System.in);
        double[] monthlyTotal = new double[totalMonthNumber];
        double[] dailyMin = new double[totalMonthNumber];
        double[] dailyMax = new double[totalMonthNumber];
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        // Get the selected year while displaying welcome message.
        selectedYear = displayWelcomeMessages(scanner);

        // Main part of the program -- Read, process, and save data.
        try {
            // Use try and catch statement to handle situations such as corrupted text files and some other related IO issues.
            // Read data from given CSV file.
            Reader in = new FileReader(analysisFilename);
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);

            // Perform calculations with the data in the CSV file to get monthly, minimum, and maximum rainfalls.
            for (CSVRecord record : records) {
                // Get data from CSV file.
                String yearNum = record.get("Year");
                int monthNum = Integer.parseInt(record.get("Month"));
                String rainfall = record.get("Rainfall amount (millimetres)");
                // Call methods for calculation only execute when it is not blank and from the selected year.
                if (!Objects.equals(rainfall, "") && ((selectedYear.equals(yearNum) || selectedYear.equals("999")))) {
                    double rainfallNum = Double.parseDouble(rainfall);
                    getMonthlyRainfallTotal(monthlyTotal, monthNum, rainfallNum); // Monthly rainfall totals of the year.
                    getDailyMinRainfall(dailyMin, monthNum, rainfallNum); // Minimum daily rainfall in each month.
                    getDailyMaxRainfall(dailyMax, monthNum, rainfallNum); // Maximum daily rainfall in each month.
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found."); // Display the message when the CSV file is not found.
        } catch (NumberFormatException e) {
            System.out.println("Incorrect Data Format."); // Display the message when the data format is wrong.
        } catch (Exception e) {
            System.out.println("Error: there was an issue."); // Display the message when any other errors occur.
        }

        displayAnalysisResult(monthName, monthlyTotal, dailyMin, dailyMax, totalMonthNumber); // Display the analyzed annual rainfall
        saveCalculatedInformation(analysisResultFilename, analysisResultStoreColumnNames, selectedYear, monthlyTotal, dailyMin, dailyMax, totalMonthNumber); // Save the analysis to a text file.
    }

    // Methods used in the program are listed below with descriptions for each.

    /**
     * Display welcome messages and get the user's selected year for analysis.
     */
    public static String displayWelcomeMessages(Scanner scanner) {
        // Welcome message
        System.out.println("Welcome to RainfallAnalyser Alpha-Release.");
        System.out.println("-------------------------------------------------------------------------------"); // Leave a line between two messages

        // User input -- get the year in which the user wants to analyze for rainfall
        System.out.println("Which year do you want to know about the monthly rainfall analysis?\n 1. Enter the year (Exp: 2020)\n 2. Enter 999 to display monthly rainfalls totals from all years");
        System.out.print("The year: ");
        String selectedYear = scanner.nextLine();
        System.out.println(); // Leave a line between two messages

        // Processing message
        System.out.println("Analysing Data...");
        System.out.println("-------------------------------------------------------------------------------"); // Leave a line between two messages

        return selectedYear;
    }

    /**
     * Get the monthly rainfall totals for the year user selected.
     */
    public static void getMonthlyRainfallTotal(double[] monthlyTotal, int month, double dailyRainfall) {
        monthlyTotal[month - 1] += dailyRainfall; // Add the daily rainfall for a month together.
    }

    /**
     * Get the monthly minimum daily rainfall for the year user selected.
     */
    public static void getDailyMinRainfall(double[] dailyMin, int month, double dailyRainfall) {
        if (dailyRainfall < dailyMin[month - 1]) {
            // Execute when the daily rainfall is smaller than the current minimum rainfall for that month.
            dailyMin[month - 1] = dailyRainfall; // Set the dailyMin to the smaller amount of rainfall.
        }
    }

    /**
     * Get the monthly maximum daily rainfall for the year user selected.
     */
    public static void getDailyMaxRainfall(double[] dailyMin, int month, double dailyRainfall) {
        if (dailyRainfall > dailyMin[month - 1]) {
            // Execute when the daily rainfall is bigger than the current minimum rainfall for that month.
            dailyMin[month - 1] = dailyRainfall; // Set the dailyMin to the smaller amount of rainfall.
        }
    }

    /**
     * Display the annual rainfall analysis for the selected year.
     */
    public static void displayAnalysisResult(String[] monthName, double[] monthlyTotal, double[] dailyMin, double[] dailyMax, int totalMonthNumber) {
        for (int i = 0; i < totalMonthNumber; i++) {
            // Iterate 12 times (as there are 12 months). In each iteration, the analysis result that shows the monthly total, minimum, and maximum rainfall will display.
            System.out.printf("%-11s: Total rainfall = %8.2f, Min rainfall = %5.2f, Max rainfall = %-6.2f\n", monthName[i], monthlyTotal[i], dailyMin[i], dailyMax[i]);
        }
        System.out.println("-------------------------------------------------------------------------------"); // Leave a line between two messages
        System.out.println("End of analysis. Thank you for use"); // Closing message
    }

    /**
     * Save the calculated information into the analysis result text file.
     */
    public static void saveCalculatedInformation(String filename, String analysisResultStoreColumnNames, String year, double[] monthlyTotal, double[] dailyMin, double[] dailyMax, int totalMonthNumber) {
        try {
            // Create a file to save the calculated information.
            FileWriter writer = new FileWriter(filename);
            writer.write(analysisResultStoreColumnNames);
            if (year.equals("999")) { // Assign value to String year, if the user chose analyse rainfall data from all years.
                year = "From all years";
            }
            for (int i = 0; i < totalMonthNumber; i++) {
                // Iterate 12 times (as there are 12 months). In each iteration, the analysis result of the monthly total, minimum, and maximum rainfall will save to the text file.
                writer.write(year + "," + i + "," + String.format("%.2f", monthlyTotal[i]) + "," + String.format("%.2f", dailyMin[i]) + "," + String.format("%.2f", dailyMax[i]) + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("There is an error!");
            e.printStackTrace(); // Display the message and where it happens when there is an error.
        }
    }

}

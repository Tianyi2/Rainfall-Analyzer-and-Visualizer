package rainfall;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RainfallSaveFile {
    // Initialization.
    private final String filename;
    private final double[] monthlyTotal;
    private final double[] dailyMin;
    private final double[] dailyMax;
    private final String[] monthName;
    final String analysisResultStoreColumnNames = "month,total,min,max\n";

    /**
     * The constructor of the class, called when an object is instantiated.
     */
    RainfallSaveFile(String filename, String[] monthName, double[] monthlyTotal, double[] dailyMin, double[] dailyMax) {
        this.filename = filename;
        this.monthName = monthName;
        this.monthlyTotal = monthlyTotal;
        this.dailyMin = dailyMin;
        this.dailyMax = dailyMax;
    }

    /**
     * Save the processed data into a txt file.
     */
    public void saveFile() throws IOException {
        // Create a file to save the calculated rainfall.
        File saveFile = new File(filename);
        if (saveFile.createNewFile()) {
            // Only execute when the file is not created.
            FileWriter writer = new FileWriter(filename);
            writer.write(analysisResultStoreColumnNames); // Write the column name into the txt file.
            for (int i = 0; i < 12; i++) {
                // Iterate 12 times (as there are 12 months). In each iteration, analysis result of the monthly total, minimum, and maximum rainfall will save to the text file.
                writer.write(monthName[i] + "," + String.format("%.2f", monthlyTotal[i]) + "," + String.format("%.2f", dailyMin[i]) + "," + String.format("%.2f", dailyMax[i]) + "\n");
            }
            writer.close();
        } else {
            System.out.println("File already exists.\n Save failure."); // Display a message tell the user the file is already created, and cannot save the data.
        }
    }

    /**
     * Display messages that tell use the processed data is successfully saved and where does it save at.
     */
    public void displaySavedMessage() {
        System.out.println("Successfully saved");
        System.out.printf("The information is saved in: %s\n", filename);
        System.out.println(); // Leave a line between two messages
    }
}
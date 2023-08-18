import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

/**
 * CP2406 Assignment - Tianyi Zhang
 * Alpha release
 * This program will present the rainfall analysis visually using a bar graph with the data obtained from RainfallAnalyser.
 */

public class RainfallVisualiser extends Application {
    // Initialize using variables.
    final int totalMonthNumber = 12;
    final String[] xAxisName = {"Total Rainfall", "Max Rainfall", "Min Rainfall"};
    String[] monthlyTotal = new String[totalMonthNumber];
    String[] monthlyMin = new String[totalMonthNumber];
    String[] monthlyMax = new String[totalMonthNumber];
    final String[][] xAxisDataListName = {monthlyTotal, monthlyMax, monthlyMin}; // The types of variable that will be shown in x-axis.
    String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    BarChart<String, Number> barChart; // Declare a BarChart object.


    @Override
    public void start(Stage stage) {
        stage.setTitle("Rainfall Visualiser"); // Set the title of the popup window that contain the graphical chart.
        setupBarChart(); // Set up the barchart by calling the method setupBarChart.
        TextIO.getln(); // ignore the header line (first line in the txt file).

        processData(totalMonthNumber, monthlyTotal, monthlyMin, monthlyMax);  // Process the data from the txt file to get the three rainfall details.
        createXAxesOfBarChart(totalMonthNumber, barChart, monthName, xAxisName, xAxisDataListName); // Pass the data processed from the above method to the bar chart.

        // Add the chart to the scene to present in window.
        Scene scene = new Scene(barChart, 1200, 600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Create a barchart with x-axis nad y-axis labels.
     */
    private void setupBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month"); // x-axis label.
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Rainfall Amount"); // y-axis label.
        barChart = new BarChart<>(xAxis, yAxis); // pass in the two labels to bar chart.
        barChart.setTitle("Rainfall Analysis"); // Set title of the bar chart.
    }

    /**
     * Process the data from the analyzed txt file to get the monthly total, minimum and maximum rainfall.
     */
    public static void processData(int totalMonthNumber, String[] monthlyTotal, String[] monthlyMin, String[] monthlyMax) {
        // Execute 12 times as there are 12 months, every time save the total, minimum and maximum rainfall of a month to the corresponding list.
        for (int i = 0; i < totalMonthNumber; i++) {
            String[] rainfallData = TextIO.getln().split(","); // Get data from the txt file.
            String total = rainfallData[2]; // Get the monthly rainfall.
            monthlyTotal[i] = total; // Store the monthly rainfall to string list monthlyTotal.
            String min = rainfallData[3]; // Get the monthly minimum daily rainfall.
            monthlyMin[i] = min; // Store the monthly minimum daily rainfall to string list monthlyMin.
            String max = rainfallData[4]; // Get the monthly maximum daily rainfall.
            monthlyMax[i] = max; // Store the monthly maximum daily rainfall to string list monthlyMax.
        }
    }

    /**
     * Input the data for an x-axis of the bar chart.
     */
    public static void inputDataToBarChart(int totalMonthNumber, XYChart.Series<String, Number> dataSeries, String[] dataList, String[] monthName) {
        // Execute 12 times as there are 12 months, every time pass the monthly rainfall to the bar chart.
        for (int i = 0; i < totalMonthNumber; i++) {
            String rainfall = dataList[i]; // Get the rainfall number.
            dataSeries.getData().add(new XYChart.Data<>(monthName[i], Double.parseDouble(rainfall))); //Save the rainfall number to a data series.
        }
    }

    /**
     * Create the x-axes of the bar chart.
     */
    public static void createXAxesOfBarChart(int totalMonthNumber, BarChart<String, Number> barChart, String[] monthName, String[] xAxisName, String[][] xAxisDataListName) {
        int numberOfVariablesInXAxis = xAxisDataListName.length; // The number of variables will be presented in x-axis.
        // Execute the number of variables times. Every time give a name to the variable, get the rainfall numbers for 12 months, and save them to the bar chart.
        for (int i = 0; i < numberOfVariablesInXAxis; i++) {
            XYChart.Series<String, Number> dataSeries = new XYChart.Series<>(); // Create a data series.
            dataSeries.setName(xAxisName[i]); // Give a name to the data series.
            inputDataToBarChart(totalMonthNumber, dataSeries, xAxisDataListName[i], monthName); // Fill the data series with rainfall numbers.
            barChart.getData().add(dataSeries); // Add the data series to bar chart.
        }
    }

    /**
     * Read the data file and launch the graphical chart.
     */
    public static void main(String[] args) {
        // Load data from file.
        var path = "src/main/resources/data.txt";
        TextIO.readFile(path);
        launch(); // Launch the graphical chart.
    }
}
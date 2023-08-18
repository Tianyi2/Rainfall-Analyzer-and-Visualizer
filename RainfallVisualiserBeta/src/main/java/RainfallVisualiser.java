import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import rainfall.RainfallProcessData;
import rainfall.RainfallReader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class RainfallVisualiser extends Application {
    private final double[] monthlyTotal = new double[12];
    private final double[] dailyMin = new double[12];
    private final double[] dailyMax = new double[12];
    final double[][] xAxisDataListName = {monthlyTotal, dailyMax, dailyMin};
    final String[] xAxisName = {"Total Rainfall", "Max Rainfall", "Min Rainfall"};
    String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    BarChart<String, Number> barChart; // Declare a BarChart object.
    File selectedFile; // Declare a FIle object.
    TextArea fileData = new TextArea(); // Declare a TextArea object that display rainfall information.
    private final FileChooser fileChooser = new FileChooser();
    private final Stage homeStage = new Stage(); // Set up a stage object for home page.
    private final Stage visualiserStage = new Stage(); // Set up a stage object for bar chart page.
    private final Stage selectCSVFileStage = new Stage(); // Set up a stage object for select CSV file page.
    private final String[] filePathList = {"src/main/Copperlode_Dam_Data.csv", "src/main/Kuranda_Railway_Data.csv", "src/main/Tinaroo_Falls_Dam_Data.csv"};
    private final Label statusBar = new Label();
    XYChart.Series<String, Number> dataSeries1 = new XYChart.Series<>(); // Initialize the dataSeries that will be used in bar chart.
    XYChart.Series<String, Number> dataSeries2 = new XYChart.Series<>();
    XYChart.Series<String, Number> dataSeries3 = new XYChart.Series<>();
    XYChart.Series<String, Number>[] dataSeriesList = new XYChart.Series[]{dataSeries1, dataSeries2, dataSeries3}; // List of dataSeries.
    RainfallProcessData csvTempFile = new RainfallProcessData(); // Create a RainfallProcessData object.


    @Override
    public void start(Stage stage) {
        setupBarChart(); // Set up the bar chart.
        buildHomeStage(); // Build the home page.
        buildVisualiserStage(); // Build the visualization page.
        homeStage.show(); // Display home page.
    }

    /**
     * Launch the rainfall visualiser that read a CSV file and display a graphical chart of the data in the file.
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Input the analyzed rainfall data to data series that used in the bar chart.
     */
    private void inputDataToBarChart(XYChart.Series<String, Number> dataSeries, double[] dataList) {
        // Execute 12 times as there are 12 months, every time pass the monthly rainfall data to the data series passed in.
        for (int i = 0; i < monthName.length; i++) {
            double rainfall = dataList[i]; // Get the rainfall number.
            dataSeries.getData().add(new XYChart.Data<>(monthName[i], rainfall)); //Save the rainfall number to the data series.
        }
    }

    /**
     * Create all the x-axis variables that going to show in the bar chart.
     */
    private void createXAxesOfBarChart() {
        // Execute the number of x-axis variables times. Every time give a name to the variable (data series), get the rainfall numbers of 12 months, and save them to the bar chart.
        for (int i = 0; i < xAxisName.length; i++) {
            dataSeriesList[i].setName(xAxisName[i]); // Give a name to the data series.
            inputDataToBarChart(dataSeriesList[i], xAxisDataListName[i]); // Fill the data series with rainfall numbers of 12 months.
            barChart.getData().add(dataSeriesList[i]); // Add the data series to bar chart.
        }
    }

    /**
     * Load rainfall data from CSV file and processed the data with RainfallReader object.
     */
    private void loadDataFromFile(String filePath) {
        RainfallReader.CSVReadData(csvTempFile, filePath); // Read the CSV file.
        RainfallReader.loadProcessedData(csvTempFile, monthlyTotal, dailyMin, dailyMax); // Process rainfall data from read CSV file.
    }

    /**
     * Set up a bar chart with x-axis nad y-axis labels.
     */
    private void setupBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month"); // x-axis label.
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Rainfall Amount"); // y-axis label.
        barChart = new BarChart<>(xAxis, yAxis); // Pass in the two labels to bar chart.
        barChart.setTitle("Rainfall Analysis"); // Set title of the bar chart.
    }

    /**
     * Create a popup window that allow users to select the CSV file from their device.
     */
    private void createPopupWindowToSelectFileFromDevice() {
        selectedFile = fileChooser.showOpenDialog(homeStage); // Assign value to selectedFile.
        if (selectedFile != null) {
            barChart.setAnimated(false); // Close the animation.
            String filePath = selectedFile.getPath(); // Get the file path.
            loadDataFromFile(filePath); // Load analyzed rainfall data with loadDataFromFile method.
            createXAxesOfBarChart(); // Create x-axes of the bar chart.
            statusBar.setText("You have loaded the CSV file: " + selectedFile.getPath().substring(3)); // Display message that tell user which file he loaded.
        }
    }

    /**
     * Clean all the data series from the bar chart and clean the data stored in all the data series.
     */
    private void cleanGraph() {
        for (XYChart.Series<String, Number> stringNumberSeries : dataSeriesList) { // Remove all the data series from bar chart, and clean all value in every data series.
            barChart.getData().remove(stringNumberSeries); // Remove current bars.
            stringNumberSeries.getData().clear(); // Clean the data in the data series.
        }
        csvTempFile = new RainfallProcessData(); // Reload a csvTempFile object to clean data stored in memory.
    }

    /**
     * Display monthly total, daily minimum, and daily maximum rainfall information in formatted text.
     */
    private String displayProcessedRainfallInfo() {
        StringBuilder processedRainfallInfo = new StringBuilder();
        for (int i = 0; i < 12; i++) { // Iterate 12 times, every time display a text message about the analyzed rainfall information.
            processedRainfallInfo.append(String.format("%-11s: Total rainfall = %8.2f, Min rainfall = %5.2f, Max rainfall = %-6.2f\n", monthName[i], monthlyTotal[i], dailyMin[i], dailyMax[i]));
        }
        return processedRainfallInfo.toString();
    }

    /**
     * Create the home page that show welcome message, buttons for user to load CSV file and display graphical chart.
     */
    public void buildHomeStage() {
        Label message = new Label("Welcome to the Rainfall Visualiser"); // Welcome message.
        message.setFont(new Font(25)); // Set the font size of the welcome message.
        statusBar.setText("Please load a rainfall csv to be analysed"); // Set the start statusBar text.
        VBox labelBar = new VBox(message, statusBar); // Vertically display the two messages (welcome message above).
        labelBar.setAlignment(Pos.CENTER); // Center the two messages.

        // Create buttons.
        Button startButton = new Button("Start Visualiser");
        Button loadFromComputerButton = new Button("Load Rainfall Data from computer");
        Button loadFromFileList = new Button("Load Rainfall Data From List");
        startButton.setStyle("-fx-background-color: #00ffff; "); // Set the startButton color to #00ffff.

        // Assign the actions to each of the buttons created above.
        startButton.setOnAction(e -> { // Display the bar chart after press.
            homeStage.hide();
            fileData.setText(displayProcessedRainfallInfo());
            visualiserStage.show();
        });
        loadFromComputerButton.setOnAction(e -> createPopupWindowToSelectFileFromDevice()); // Load CSV file from device.
        loadFromFileList.setOnAction(e -> buildSelectCSVFileStage()); // Load CSV file from given list.

        // Create HBox and VBox to align buttons' position.
        HBox loadButtons = new HBox(40, loadFromComputerButton, loadFromFileList); // Display the two load file buttons horizontally.
        loadButtons.setAlignment(Pos.CENTER); // Center the two load file buttons.
        VBox buttonBar = new VBox(20, loadButtons, startButton); // Display the startButton below the two load file buttons.
        buttonBar.setAlignment(Pos.CENTER); // Center the startButton button.
        VBox.setMargin(startButton, new Insets(0, 0, 20, 0)); // Add bottom margin to the button bar.

        // Set up the homeStage root and scene.
        BorderPane homeRoot = new BorderPane();
        homeRoot.setBottom(buttonBar); // Add the three buttons to the home page.
        homeRoot.setCenter(labelBar); // Add the text messages to the home page.
        Scene homeScene = new Scene(homeRoot, 600, 400);

        // Set the scene, title, and other features for the homeStage.
        homeStage.setScene(homeScene); // Add the scene to the stage.
        homeStage.setTitle("Rainfall Visualiser -- Beta Release"); // Set title of the home page.
        homeStage.centerOnScreen(); // Make the window display at the center of the computer screen.
        homeStage.setResizable(false); // Make the window not resizable.
    }

    /**
     * Create the visualization page that show the graphical information about the rainfall data found in the file user input.
     */
    private void buildVisualiserStage() {
        Button backToHomePage = new Button("Create chart for other CSV file");
        backToHomePage.setOnAction(e -> { // Clean the bar chart, close the visualization page, and display the home page when pressed.
            visualiserStage.hide();
            cleanGraph();
            buildHomeStage();
            homeStage.show();
        });

        // Set content in the visualiser stage.
        fileData = new TextArea(); // Create a text area to display processed rainfall information.
        fileData.setMaxWidth(600); // Set the width of the text area.
        VBox chartAndReturnButton = new VBox(barChart, backToHomePage); // Display the bar chart and backToHomePage button vertically.
        chartAndReturnButton.setMinWidth(880); // Set the width of the VBox.
        chartAndReturnButton.setAlignment(Pos.CENTER); // Center the bar chart and backToHomePage button.
        VBox.setMargin(backToHomePage, new Insets(0, 0, 20, 0)); // Add bottom margin to the backToHomePage button.
        HBox chartStageMainBox = new HBox(chartAndReturnButton, fileData); // Display the chartAndReturnButton VBox and the text area horizontally.

        Scene scene = new Scene(chartStageMainBox, 400, 200); // Create scene for the visualiser stage.
        visualiserStage.setScene(scene); // Add above scene to the stage.
        visualiserStage.setHeight(400); // Set height of the visualization window.
        visualiserStage.setWidth(1370); // Set width of the visualization window.
        visualiserStage.setTitle("Rainfall Visualiser -- Beta Release"); // Set title of the visualization window.
    }

    /**
     * Build the selection page that load a list of CSV files from the memory for user to select a CSV file to analyze.
     */
    public void buildSelectCSVFileStage() {
        // Create check boxes that show the every CSV file in the list.
        VBox vbox = new VBox(); // Display the buttons vertically.
        ToggleGroup group = new ToggleGroup();
        for (String radioButtonName : filePathList) {
            RadioButton radioButton = new RadioButton(radioButtonName); // Create a radio button that display the name of a CSV file from the file list.
            vbox.getChildren().add(radioButton); // Add the radio button to the vbox.
            radioButton.setToggleGroup(group);
        }
        group.selectedToggleProperty().addListener((observableValue, old_toggle, new_toggle) -> {
            String selectedFileName = ((RadioButton) new_toggle).getText(); // Get the path of selected file.
            selectedFile = new File(selectedFileName); // Assign value to the selectedFile.
        });

        // Create button in the stage.
        Button saveSelectedFile = new Button("Start Analyze"); // Create a save selected file button (the button will load the processed rainfall data and display the home page).
        saveSelectedFile.setOnAction(e -> { // Analyze selected file and display home page.
            String filePath = selectedFile.getPath();
            loadDataFromFile(filePath); // Load analyzed rainfall data with loadDataFromFile method.
            createXAxesOfBarChart(); // Create x-axes of the bar chart.
            statusBar.setText("You have loaded the CSV file: " + selectedFile.getPath().substring(9)); // Change the text in status bar to tell the user which CSV file did he load.
            selectCSVFileStage.hide();
            homeStage.show();
        });
        vbox.getChildren().add(saveSelectedFile); // Add the button to the vbox.

        // Create scene.
        Scene selectCSVFileScene = new Scene(vbox, 250, 70);
        selectCSVFileStage.setScene(selectCSVFileScene); // Add the above to the stage.
        selectCSVFileStage.setTitle("Select CSV file from provided list."); // Set title of the page.
        selectCSVFileStage.show(); // Display select CSV file page.
    }
}
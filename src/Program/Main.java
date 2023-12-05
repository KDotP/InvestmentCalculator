package Program;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    private static InputManager manager;
    private static FileManager fileManager;

    private static final boolean VERBOSE = false; // For testing, should be false by default
    private static String currencySymbol = "$";

    // Default values
    private static double initialInvestment = 1000; // $1000
    private static double bondDuration = 0.5; // Every 6 Months
    private static double interestRate = 0.05; // 5%
    private static double investmentDuration = 3; // 3 Years

    // String version for *stuff*
    private static String initialInvestmentString = "1000";
    private static String bondDurationString = "6M";
    private static String interestRateString = "5";
    private static String investmentDurationString = "3Y";

    private static LineChart<Number, Number> chart;
    private static NumberAxis xAxis; // Time axis
    private static NumberAxis yAxis; // Money axis

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Parent Border Pane
        BorderPane parent = new BorderPane();
        parent.prefHeight(400);
        parent.prefWidth(772);

        // Top Border Pane
        BorderPane topPane = new BorderPane();

        // Top Left
        HBox topLeft = new HBox();

        ComboBox<String> modeDropdown = new ComboBox<>();
        modeDropdown.setPromptText("Bond");
        modeDropdown.getItems().addAll("Bond", "Stock", "Dividend Stock");
        modeDropdown.setPrefWidth(200);
        modeDropdown.setPrefHeight(40);
        Button saveConfigButton = new Button("Save Config");
        saveConfigButton.setPrefWidth(140);
        saveConfigButton.setPrefHeight(40);
        Button loadConfigButton = new Button("Load Config");
        loadConfigButton.setPrefWidth(140);
        loadConfigButton.setPrefHeight(40);

        topLeft.getChildren().addAll(modeDropdown, saveConfigButton, loadConfigButton);

        topPane.setLeft(topLeft);

        // Top Right Currency Select Button
        ComboBox<String> currencySelection = new ComboBox<>();
        currencySelection.setPromptText("$");
        currencySelection.setPrefWidth(5);
        currencySelection.setPrefHeight(40);
        currencySelection.getItems().addAll("$", "€", "¥", "£");
        topPane.setRight(currencySelection);

        parent.setTop(topPane);

        // Side Menu

        VBox sideBox = new VBox();
        sideBox.setPrefWidth(180);
        sideBox.setAlignment(Pos.TOP_CENTER);

        Label initialLabel = new Label("Initial Investment");
        TextField initialInvestmentField = createField("Default: $1000", 150);

        Label durationLabel = new Label("Bond Duration");
        TextField bondDurationField = createField("Default: 6M", 80);

        Label apyLabel = new Label("Expected APY");
        TextField expectedApyField = createField("Default: 5%", 80);
        CheckBox useApiBox = new CheckBox();
        useApiBox.setText("Use Current Rates");

        Text gapText = new Text(); // Empty line, easier than using padding or anything like that

        Label totalDurationLablel = new Label("Investment Duration");
        TextField totalDurationField = createField("Default: 3Y", 80);

        Text gapText2 = new Text(); // Guess what? More code crimes!

        Button calculateButton = new Button("Calculate");

        Text returnText = new Text();
        Text resultText = new Text();

        sideBox.getChildren().addAll(initialLabel, initialInvestmentField, durationLabel, bondDurationField, apyLabel, expectedApyField,
        useApiBox, gapText, totalDurationLablel, totalDurationField, gapText2, calculateButton, returnText, resultText);

        parent.setLeft(sideBox);

        // Right Data Chart
        // https://docs.oracle.com/javafx/2/charts/line-chart.htm
        // Why must we make things so unnecessarily difficult?

        xAxis = new NumberAxis(); // Time axis
        xAxis.setLabel("Years");
        yAxis = new NumberAxis(); // Money axis
        yAxis.setLabel("Returns");
        chart = new LineChart<>(xAxis, yAxis);
        chart.setLegendVisible(false); // Hide the little thing at the bottom

        parent.setCenter(chart);

        // Activate the scene

        Scene scene = new Scene(parent, 800, 400);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Problem2");
        primaryStage.show();

        // Actions

        // Initial Investment
        initialInvestmentField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                initialInvestmentString = "1000";
            } else {
                initialInvestmentString = newValue;
            }
            if (VERBOSE) {
                System.out.println("Initial Investment Changed: " + initialInvestmentString);
            }
        });

        // Bond Duration
        bondDurationField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                bondDurationString = "6M";
            } else {
                bondDurationString = newValue;
            }
            if (VERBOSE) {
                System.out.println("Bond Duration Changed: " + bondDuration);
            }
        });

        // Expected APY
        expectedApyField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                interestRateString = "5";
            } else {
                interestRateString = newValue;
            }
            if (VERBOSE) {
                System.out.println("Interest Rate Changed: " + interestRate);
            }
        });

        // Use API checkbox
        useApiBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            expectedApyField.setDisable(newValue);
            interestRate = getApy();
            if (VERBOSE) {
                System.out.println("Use API: " + newValue);
            }
        });

        // Investment Duration
        totalDurationField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                investmentDurationString = "3Y";
            } else {
                investmentDurationString = newValue;
            }
            
            if (VERBOSE) {
                System.out.println("Investment Duration Changed: " + investmentDuration);
            }
        });

        // Dropdown Box
        modeDropdown.setOnAction(event -> {
            // Change menu
        });

        /*
        * Calculate Results
        * In retrospect, this should be a function, but now I'm in too deep
        */
        calculateButton.setOnAction(event -> {
            updateVarsFromStrings();

            // This is what happens when you choose a bad way a calculating stuff early on and now you have to live with your mistakes
            String totalDurationString;
            if (totalDurationField.getText().equals("")) {
                totalDurationString = "3Y";
            } else {
                totalDurationString = totalDurationField.getText();
            }
            returnText.setText("Expected Return After " + manager.interpString(totalDurationString) + ":");

            // This is not a good way to do this but my brain has shut off
            // Calculate number of times to run chart loop
            int runs = 0;
            String bondDurationString;
            if (bondDurationField.getText().equals("")) {
                bondDurationString = "6M";
            } else {
                bondDurationString = bondDurationField.getText();
            }
            runs = manager.totalRuns(manager.convertTime(totalDurationString), manager.convertTime(bondDurationString));
            if (VERBOSE) {
                System.out.println("Running " + runs + " times");
            }

            // Reset chart
            chart.getData().clear();
            double results = initialInvestment; // If it runs 0 times, you still have your initial investment
            XYChart.Series<Number, Number> newSeries = new XYChart.Series<>();

            newSeries.getData().add(new XYChart.Data<>(0, initialInvestment)); // Starting data point

            // Populate chart with data
            for (int i = 1; i <= runs; i++) {
                results = manager.calculate(initialInvestment, interestRate, (investmentDuration / runs) * i, bondDuration); // Last run should yield correct final results
                newSeries.getData().add(new XYChart.Data<>(i * bondDuration, results)); // Scale the model to a year duration
            }
            
            chart.getData().add(newSeries);

            // https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Tooltip.html
            // https://stackoverflow.com/questions/14117867/tooltips-for-datapoints-in-a-scatter-chart-in-javafx-2-2
            // https://gist.github.com/jewelsea/4681797
            // This took a bunch of references to figure out and get working
            for (XYChart.Data<Number, Number> d : newSeries.getData()) {
                StackPane dataPoint = (StackPane) d.getNode();
                Text label = new Text("$" + d.getYValue().toString()); // Create the label text that reflects the node's value

                // On hover over
                dataPoint.setOnMouseEntered(event2 -> {
                    label.setVisible(true);
                    
                    // I wish I could explain why this is done like this
                    // It would make more since to add the child directly to the datapoint instead onMouseEntered, but for some reason it breaks everything
                    if (dataPoint.getChildren().size() == 0) {
                        dataPoint.getChildren().add(label);
                    }
                    
                    label.setTranslateY(20);
                    label.setText(d.getYValue().toString());

                    if (VERBOSE) {
                        System.out.println("Hovered over node " + currencySymbol + d.getYValue());
                    }
                });

                // Hide the label when the mouse leaves (bye bye mouse)
                dataPoint.setOnMouseExited(event2 -> {
                    label.setVisible(false);
                });
            }
                

            // Final results THE LAST CALCULATION SHOULD BE THE ONLY ONE THAT MATTERS
            // results = manager.calculate(initialInvestment, interestRate, investmentDuration, bondDuration);
            resultText.setText(currencySymbol + results);
            if (VERBOSE) {
                System.out.println("Results: " + results);
            }
        });

        // Save config button
        saveConfigButton.setOnAction(event -> {
            Stage test = fileManager.savePopup(initialInvestmentString, bondDurationString, interestRateString, investmentDurationString);
            test.show();
        });

        // Load config button
        loadConfigButton.setOnAction(event -> {
            Stage test = loadPopup(initialInvestmentField, bondDurationField, expectedApyField, totalDurationField);
            test.show();
        });

        // Change currency
        currencySelection.setOnAction(event -> {
            currencySymbol = currencySelection.getValue();
            updateCurrency(initialInvestmentField);
        });
    }

    private double getApy() {
        // TO DO
        return 0;
    }

    // Create a text field with specific parameters because it's done multiple times
    private static TextField createField(String promt, int width) {
        TextField returnField = new TextField();
        returnField.setMaxWidth(width);
        returnField.setAlignment(Pos.CENTER);
        returnField.setPromptText(promt);

        return returnField;
    }

    private Stage loadPopup(TextField initialInvestmentField, TextField bondDurationField, TextField expectedApyField, TextField investmentDurationField) {
        Stage popup = new Stage();
        popup.setTitle("Load Config from File");

        Text text = new Text("Enter Name of Save File:");

        TextField textField = new TextField();
        textField.setPromptText("Default: save");
        textField.setId("loadTextfield");

        Button button = new Button("Load");
        button.setId("loadButton");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.BASELINE_CENTER);
        vbox.getChildren().addAll(text, textField, button);

        Scene scene = new Scene(vbox, 200, 150);
        popup.setScene(scene);

        // Confirm load button
        button.setOnAction(event -> {
            String fieldString = textField.getText();
            if (fieldString.equals("")) {
                // If no file name is loaded, load default file
                String[] loadOutput = fileManager.loadFromFile();
                setFromLoad(loadOutput);
            } else {
                // If file is specified, load the new files into the string values
                String[] loadOutput = fileManager.loadFromFile(fieldString);
                setFromLoad(loadOutput);
            }
            updateFields(initialInvestmentField, bondDurationField, expectedApyField, investmentDurationField);

            popup.close();
        });
        
        return popup;
    }

    // Set the string values to the new ones retrieved from loading file
    private void setFromLoad(String[] loadOutput) {
        initialInvestmentString = loadOutput[0];
        bondDurationString = loadOutput[1];
        interestRateString = loadOutput[2];
        investmentDurationString = loadOutput[3];
    }

    // Set the textfield text to the new String values
    private void updateFields(TextField initialInvestmentField, TextField bondDurationField, TextField expectedApyField, TextField investmentDurationField) {
        // I use "" + double to convert to a string despite the fact that the compiler really should be able to do that for me
        initialInvestmentField.setText(initialInvestmentString);
        bondDurationField.setText(bondDurationString);
        expectedApyField.setText(interestRateString);
        investmentDurationField.setText(investmentDurationString);
    }

    // Extract the values to the global doubles
    private void updateVarsFromStrings() {
        initialInvestment = manager.sanitizeDouble(initialInvestmentString);
        bondDuration = manager.convertTime(bondDurationString);
        interestRate = ((manager.sanitizeDouble(interestRateString)) / 100);
        investmentDuration = manager.convertTime(investmentDurationString);
    }

    private void updateCurrency(TextField field) {
        field.setPromptText("Default: " + currencySymbol + "1000");
    }

    public static void main(String[] args) {
        manager = new InputManager();
        fileManager = new FileManager();
        launch(args);
    }
}

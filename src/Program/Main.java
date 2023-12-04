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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    private static InputManager manager;
    private static FileManager fileManager;

    private static final boolean VERBOSE = false; // For testing, should be false by default

    // Default values
    private static double initialInvestment = 1000; // $1000
    private static double bondDuration = 0.5; // Every 6 Months
    private static double interestRate = 0.05; // 5%
    private static double investmentDuration = 3; // 3 Years



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
        modeDropdown.setPrefHeight(50);
        Button saveConfigButton = new Button("Save Config");
        saveConfigButton.setPrefWidth(140);
        saveConfigButton.setPrefHeight(50);
        Button loadConfigButton = new Button("Load Config");
        loadConfigButton.setPrefWidth(140);
        loadConfigButton.setPrefHeight(50);

        topLeft.getChildren().addAll(modeDropdown, saveConfigButton, loadConfigButton);

        topPane.setLeft(topLeft);

        // Top Right Button
        Button saveGraphButton = new Button("Save Graph");
        saveGraphButton.setPrefWidth(140);
        saveGraphButton.setPrefHeight(50);
        topPane.setRight(saveGraphButton);
        
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

        NumberAxis xAxis = new NumberAxis(); // Time axis
        NumberAxis yAxis = new NumberAxis(); // Money axis
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
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
                initialInvestment = 1000;
            } else {
                initialInvestment = manager.sanitizeDouble(newValue);
            }
            if (VERBOSE) {
                System.out.println("Initial Investment Changed: " + initialInvestment);
            }
        });

        // Bond Duration
        bondDurationField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                bondDuration = 0.5;
            } else {
                bondDuration = manager.convertTime(newValue);
            }
            if (VERBOSE) {
                System.out.println("Bond Duration Changed: " + bondDuration);
            }
        });

        // Expected APY
        expectedApyField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                interestRate = 0.05;
            } else {
                interestRate = (manager.sanitizeDouble(newValue) / 100);
            }
            if (VERBOSE) {
                System.out.println("Interest Rate Changed: " + interestRate);
            }
        });

        useApiBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            expectedApyField.setEditable(!newValue);
            interestRate = getApy();
            if (VERBOSE) {
                System.out.println("Use API: " + newValue);
            }
        });

        // Investment Duration
        totalDurationField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                investmentDuration = 3;
            } else {
                investmentDuration = manager.convertTime(newValue);
            }
            
            if (VERBOSE) {
                System.out.println("Investment Duration Changed: " + investmentDuration);
            }
        });

        // Dropdown Box
        modeDropdown.setOnAction(event -> {
            // Change menu
        });

        // Calculate Results
        calculateButton.setOnAction(event -> {
            

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

            // Populate chart
            for (int i = 1; i <= runs; i++) {
                results = manager.calculate(initialInvestment, interestRate, (investmentDuration / runs) * i, bondDuration); // Last run should yield correct final results
                newSeries.getData().add(new XYChart.Data<>(i, results));
            }
            
            chart.getData().add(newSeries);

            // Final results
            results = manager.calculate(initialInvestment, interestRate, investmentDuration, bondDuration);
            resultText.setText("$" + results);
            if (VERBOSE) {
                System.out.println("Results: " + results);
            }
        });

        // Save config button
        saveConfigButton.setOnAction(event -> {
            Stage test = fileManager.savePopup(initialInvestment, bondDuration, interestRate, investmentDuration);
            test.show();
        });

        // Load config button
        loadConfigButton.setOnAction(event -> {
            Stage test = fileManager.loadPopup();
            test.show();
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

    public static void main(String[] args) {
        manager = new InputManager();
        fileManager = new FileManager();
        launch(args);
    }
}

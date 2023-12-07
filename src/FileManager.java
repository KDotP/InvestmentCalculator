/**
 * Computer Science Final Project
 * Investment Calculator
 * <p>
 * CSC 1061 - Computer Science II - Java
 * 
 * This file manages most file operations
 * Save / Loading Files
 *
 * @author  Kieran Persoff
 * @version %I%, %G%
 * @since   1.0
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FileManager {
    private final String DEFAULT_FILE_NAME = "save";

    public FileManager() {
    }

    // Save to default file location
    public void saveToFile(String initialInvestment, String bondDuration, String interestRate, String investmentDuration, String investmentMode) {
        saveToFile(initialInvestment, bondDuration, interestRate, investmentDuration, investmentMode, DEFAULT_FILE_NAME);
    }

    // Save to location with specified name
    public void saveToFile(String initialInvestment, String bondDuration, String interestRate, String investmentDuration, String investmentMode, String fileName) {
        try {
            String pathString = getSavePath() + "\\" + fileName + ".txt";

            BufferedWriter writer = new BufferedWriter(new FileWriter(pathString));
            writer.write(initialInvestment + "\n" + bondDuration + "\n" + interestRate + "\n" + investmentDuration + "\n" + investmentMode);
            System.out.println("Successfully saved to " + pathString);
            writer.close();
        } catch (IOException e) {
            System.out.println("Failed to save to file");
            e.printStackTrace();
        }
    }

    // Get the path of Investment Calculator folder, then navigate to the saves folder
    private String getSavePath() {
        try {
            // https://stackoverflow.com/questions/320542/how-to-get-the-path-of-a-running-jar-file
            // Get path of master folder
            Path path = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            String pathString = path.getParent().toString() + "\\saves";
            // System.out.println(pathString);

            return pathString;
        } catch (URISyntaxException e) {
            System.out.println("Could not get file path!");
            e.printStackTrace();
            return "";
        }
    }

    // Pop Up Menu for Saving to Specific Location
    public Stage savePopup(String initialInvestment, String bondDuration, String interestRate, String investmentDuration, String investmentMode) {
        Stage popup = new Stage();
        popup.setTitle("Save Config to File");

        Text text = new Text("Enter Name of Save File:");

        TextField textField = new TextField();
        textField.setPromptText("Default: save");

        Button button = new Button("Save");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.BASELINE_CENTER);
        vbox.getChildren().addAll(text, textField, button);

        Scene scene = new Scene(vbox, 200, 150);
        popup.setScene(scene);

        button.setOnAction(event -> {
            String innerText = textField.getText();
            if (innerText.equals("")) {
                saveToFile(initialInvestment, bondDuration, interestRate, investmentDuration, investmentMode);
            } else {
                saveToFile(initialInvestment, bondDuration, interestRate, investmentDuration, investmentMode, innerText);
            }
            popup.close();
        });     

        return popup;
    }

    public String[] loadFromFile() {
        return loadFromFile(DEFAULT_FILE_NAME);
    }

    public String[] loadFromFile(String fileName) {
        String savePath = getSavePath() + "\\" + fileName + ".txt";
        String[] doubleArray = new String[5];

        try {
            Scanner reader = new Scanner(new File(savePath));
            for (int i = 0; i < doubleArray.length; i++) {
                doubleArray[i] = reader.nextLine();
            }

            System.out.println("Successfully loaded from " + savePath);
            return doubleArray;
        } catch (FileNotFoundException e) {
            System.out.println("Could Not Find File: " + savePath);
            e.printStackTrace();
            return null;
        }        
    }
}

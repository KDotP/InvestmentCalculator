package Program;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FileManager {
    public FileManager() {
    }

    // Save to default file location
    void saveToFile(double initialInvestment, double bondDuration, double interestRate, double investmentDuration) {
        try {
            String pathString = getSavePath() + "\\default.txt";

            BufferedWriter writer = new BufferedWriter(new FileWriter(pathString));
            writer.write("Test");
            System.out.println("Wrote to file");
            writer.close();
        } catch (Exception e) {
            System.out.println("Failed to save to file");
            e.printStackTrace();
        }
    }

    void saveToFile(double initialInvestment, double bondDuration, double interestRate, double investmentDuration, String fileName) {
        try {
            String pathString = getSavePath() + "\\" + fileName + ".txt";

            BufferedWriter writer = new BufferedWriter(new FileWriter(pathString));
            writer.write(initialInvestment + "|" + bondDuration + "|" + interestRate + "|" + investmentDuration);
            System.out.println("Wrote to file");
            writer.close();
        } catch (IOException e) {
            System.out.println("Failed to save to file");
            e.printStackTrace();
        }
    }

    private String getSavePath() {
        try {
            // https://stackoverflow.com/questions/320542/how-to-get-the-path-of-a-running-jar-file
            // Get path of master folder
            Path path = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            String pathString = path.getParent().toString() + "\\Saves";
            // System.out.println(pathString);

            return pathString;
        } catch (URISyntaxException e) {
            System.out.println("Could not get file path!");
            e.printStackTrace();
            return "";
        }
    }

    public Stage savePopup(double initialInvestment, double bondDuration, double interestRate, double investmentDuration) {
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
                saveToFile(initialInvestment, bondDuration, interestRate, investmentDuration);
            } else {
                saveToFile(initialInvestment, bondDuration, interestRate, investmentDuration, innerText);
            }
            popup.close();
        });     

        return popup;
    }
}

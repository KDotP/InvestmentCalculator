rem Compile and Run Batch
@echo off

echo reached 1

javac --module-path lib --add-modules javafx.controls,javafx.fxml -d bin src\*.java

echo reached 2

jar cfm bin\Application.jar bin\manifest.txt

echo reached 3

java --module-path lib --add-modules javafx.controls,javafx.fxml -jar bin\Application.jar

echo reached 4
@echo off
echo Running Investment Calculator
echo Ver 1.0.2
echo By Kieran Persoff
echo --- Program Outputs ---

rem This took hours to make, but not because it was difficult
rem While I have previous experience making batch files for java projects,
rem I didn't realize I had to put the entire javaFX folder in the lib folder
rem But hey, it works and requires no input from the user!

javac --module-path "lib\javafx-sdk-17.0.8\lib" --add-modules javafx.controls,javafx.fxml -d bin -cp "%lib\javafx-sdk-17.0.8\lib\*" src\*.java

java --module-path "lib\javafx-sdk-17.0.8\lib" --add-modules javafx.controls,javafx.fxml -Dprism.order=sw -cp bin;"lib\javafx-sdk-17.0.8\lib\*" Main


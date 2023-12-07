@echo off
echo Running Investment Calculator
echo Ver 1.0.2
echo By Kieran Persoff
echo --- Program Outputs ---

javac --module-path "lib\javafx-sdk-17.0.8\lib" --add-modules javafx.controls,javafx.fxml -d bin -cp "%lib\javafx-sdk-17.0.8\lib\*" src\*.java

java --module-path "lib\javafx-sdk-17.0.8\lib" --add-modules javafx.controls,javafx.fxml -Dprism.order=sw -cp bin;"lib\javafx-sdk-17.0.8\lib\*" Main


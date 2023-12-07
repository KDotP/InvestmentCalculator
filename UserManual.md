>>> Disclaimer
All investment involves risk
This is simply intended as a tool to project potential returns over a given time
This is not financial advice
For financial advice, speak to a qualified financial advisor

>>> If Launch Files
If you've launched run.bat and not gotten any results, it's possible you either have a bad version of java or don't have java installed at all
To check, go to a command line (cmd on windows) and enter 'java -version' without the quotation marks
If you get an error message or any version that doesn't start with 17.0, go to https://www.oracle.com/java/technologies/downloads/#jdk17-windows
From there, navigate to JDK 17, then select your operating system
Either install the installer (suggested) or follow the directions to install java
If this still doesn't work, try restarting your computer

>>> Investment Calculator
This manual details how to use the Investment Calculator app
If any text field is left empty, it will use its default value

>> Mode Selection (Top Left)
> Default: 'Compounding Security'
To change between an compounding asset (such as a bond) or appeciating asset (such as a stock)
Click on the dropdown menu at the top left (it should say "Compounding Security" by default) and select your prefered mode
In appeciating asset mode, the reinvestment period menu is disabled as appeciating assets increase continously

>> Save Config (Top)
> Default: 'save'
Clicking this button should open a save window that asks for a name
Enter your config's name in the text box and then press the 'Save' button to save to file
You do not need to add '.txt' or any other file extension at the end, it will automatically save to a .txt file with the config name

>> Load Config (Top)
> Default: 'save'
Clicking this button should open a load window that asks for a name
Enter a previous config's name in the text box and then press the 'Load' button to retrieve all settings from the file
You do not need to add '.txt' or any other file extension at the end, it will automatically load from .txt file with the config name

>> Currency Select (Top Right)
> Default: '$'
Clicking this menu will open a dropdown menu with several currencies
Click on any option to change your prefered currency

>> Initial Investment (Left Menu, First Item)
> Default: '$1000'
Typing any number into this field will change the starting amount for return calculations
You can type any rational number into this field
You can type a '$' before entering your number, but it is not necessary as the program will automatically add one if it is not present

>> Reinvestment Period (Left Menu, Second Item)
> Default: '6M'
> Disabled By: Appeciating Security mode
Typing any number into this field will change the how often money is reinvested in compounding security mode
A number without any suffix will be converted into years i.e. 7 -> 7 Years
End the number with a Y to convert the number into years i.e. 7Y -> 7 Years
End the number with a M to convert the number into months i.e. 7M -> 7 Months
End the number with a D to convert the number into months i.e. 7D -> 7 Days

>> Expected APY (Left Menu, Third Item)
> Default: '5%'
> Disabled By: Use Current Rates checkbox
Typing any number into this field will change the percentage yield for your investment
You can type a '%' before entering your number, but it is not necessary as the program will automatically add one if it is not present

>> Use Current Rates (Left Menu, Fourth Item)
> Default: Off
When checked, disables manual expected apy entry
Sets the expected apy to the most recent 6-month treasury bill return rates
Uncheck to re-enable manual apy entry
NOTE: 6-Month Treasury Bill Yield Rates are not a perfect indicator of expected return rates

>> Investment Duration (Left Menu, Fifth Item)
> Default: '3Y'
Typing any number into this field will change how long your investment will last
A number without any suffix will be converted into years i.e. 7 -> 7 Years
End the number with a Y to convert the number into years i.e. 7Y -> 7 Years
End the number with a M to convert the number into months i.e. 7M -> 7 Months
End the number with a D to convert the number into months i.e. 7D -> 7 Days

>> Calculate Button (Left Menu, Last Item)
Upon pressing, calculates expected returns for a given investment using given parameters and showing futures over a graph

>> Yield Graph
Upon pressing the 'Calculate' button, shows the futures of a given investment over time
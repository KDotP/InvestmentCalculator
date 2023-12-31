/**
 * Computer Science Final Project
 * Investment Calculator
 * <p>
 * CSC 1061 - Computer Science II - Java
 *
 * This file manages user inputs and most math
 * 
 * @author  Kieran Persoff
 * @version %I%, %G%
 * @since   1.0
 */

public class InputManager {
    char[] dateChars = "dmy".toCharArray();

    public InputManager() {
    }

    // Convert a messy int input like $21 to just 21
    int sanitizeInt(String input) {
        input = input.replaceAll("[^0-9]", ""); // https://community.qualtrics.com/custom-code-12/looking-for-regex-to-match-any-string-that-isn-t-all-numeric-17422 Regex is pain
        int output = Integer.valueOf(input);

        return output;
    }

    // Convert a messy double like 5.5% to just 5.5
    double sanitizeDouble(String input) {
        input = input.replaceAll("[^0-9\\.]", ""); // This statement took me way too long
        double output = Double.valueOf(input);

        return output;
    }

    double convertTime(String input) {
        try {
            int inputLength = input.toCharArray().length;

            // If there's only one character, just return it
            // If it's a not a valid entry anyway
            if (inputLength < 2) {
                try {
                    return Double.valueOf(sanitizeDouble(input));
                } catch (Exception e) {
                    // User did not enter a number :(
                    System.out.println("Bad Time Input!");
                    return 0d;
                }
            }

            String lastChar = String.valueOf(input.charAt(inputLength - 1)).toLowerCase();
            String subInput = input.substring(0, inputLength - 1);
            double subDouble = Double.valueOf(sanitizeDouble(subInput));

            if (lastChar.equals("d")) {
                return subDouble / 365d;
            } else if (lastChar.equals("m")) {
                return subDouble / 12d;
            } else if (lastChar.equals("y")) {
                return subDouble;
            } else {
                return Double.valueOf(input);
            }
        } catch (Exception e) {
            System.out.println("Bad Time Input!");
            return 0;
        }
    }

    // Legacy
    double convertInvserseTime(String input) {
        try {
            int inputLength = input.toCharArray().length;

            if (inputLength < 2) {
                return 1d / Double.valueOf(input);
            }

            String lastChar = String.valueOf(input.charAt(inputLength - 1)).toLowerCase();
            String subInput = input.substring(0, inputLength - 1);
            int subInt = Integer.valueOf(subInput);

            if (lastChar.equals("d")) {
                return 365d / subInt;
            } else if (lastChar.equals("m")) {
                return 12d / subInt;
            } else if (lastChar.equals("y")) {
                return 1d / subInt;
            } else {
                return 1d / Double.valueOf(input);
            }
        } catch (Exception e) {
            System.out.println("Bad Time Input!");
            return 0;
        }
    }

    int totalRuns(double investmentDuration, double bondDuration) {
        double runs = investmentDuration /  bondDuration;
        int intRuns = (int) Math.floor(runs);

        return intRuns;
    }

    String interpString(String input) {
        int inputLength = input.toCharArray().length;
        String lastChar = String.valueOf(input.charAt(inputLength - 1)).toLowerCase();
        try {
            String subInput = input.substring(0, inputLength - 1);
            double subDouble = Double.valueOf(subInput);
            if (lastChar.equals("d")) {
                return subDouble + " Days";
            } else if (lastChar.equals("m")) {
                return subDouble + " Months";
            } else {
                return subDouble + " Years";
            }
        } catch (Exception e) {
            return sanitizeDouble(input) + " Years";
        }
    }

    double calculate(double initialInvestment, double interestRate, double investmentDuration, double bondDuration) {
        double finalAmount = initialInvestment * Math.pow(1 + (interestRate / (1 / bondDuration)), (1 / bondDuration) * investmentDuration); // 1 / bondDuration is for the number of times per year
        return roundNumber(finalAmount);
    }

    double contCalculate(double initialInvestment, double interestRate, double investmentDuration) {
        double finalAmount = initialInvestment * Math.pow(Math.E, interestRate * investmentDuration);
        return roundNumber(finalAmount);
    }

    private double roundNumber(double input) {
        // Round to 2 decimals
        input *= 100;
        input = Math.floor(input);
        input /= 100;
        return input;
    }
}

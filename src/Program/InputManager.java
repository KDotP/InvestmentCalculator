package Program;

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

            if (inputLength < 2) {
                return Double.valueOf(input);
            }

            String lastChar = String.valueOf(input.charAt(inputLength - 1)).toLowerCase();
            String subInput = input.substring(0, inputLength - 1);
            int subInt = Integer.valueOf(subInput);

            if (lastChar.equals("d")) {
                return subInt / 365d;
            } else if (lastChar.equals("m")) {
                return subInt / 12d;
            } else if (lastChar.equals("y")) {
                return subInt;
            } else {
                return Double.valueOf(input);
            }
        } catch (Exception e) {
            System.out.println("Bad Time Input!");
            return 0;
        }
        // d = input/365, default
        // m = input/12
        // y = input
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
        // d = 365/input, default
        // m = 12/input
        // y = input
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
        // Round to 2 decimals
        finalAmount *= 100;
        finalAmount = Math.floor(finalAmount);
        finalAmount /= 100;
        return finalAmount;
    }
}

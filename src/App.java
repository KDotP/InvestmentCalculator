import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter investment: $");
        double investment = scanner.nextDouble();
        System.out.print("What is the average investment return (%): ");
        double rate = scanner.nextDouble() / 100;
        System.out.print("How often investment compounds per year: ");
        double compoundRate = scanner.nextDouble();
        System.out.print("How many years: ");
        double duration = scanner.nextDouble();

        Investment one = new Investment(investment, rate, compoundRate, duration);

        System.out.println("Total returns for compounding investment: $" + one.getTotal() + "\n\n");

        System.out.print("Enter investment: $");
        investment = scanner.nextDouble();
        System.out.print("What is the average APY (%): ");
        rate = scanner.nextDouble() / 100;
        System.out.print("How many years: ");
        duration = scanner.nextDouble();

        Investment two = new Investment(investment, rate, false, duration);

        System.out.println("Total returns for compounding investment: $" + two.getTotal());

        scanner.close();
    }
}

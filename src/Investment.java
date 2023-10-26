public class Investment {
    private double principle = 0;
    private double yield = 0.01; // 0.05 = 5%
    private double compoundRate = 1; // 1 = 1 year, 0.5 = 6 months
    private boolean compounds = false;
    private double investmentDuration = 1; // 1 = 1 year

    // Compounding investment i.e. bonds
    public Investment(double principle, double yield, double coumpoundRate, double investmentDuration)
    {
        this.principle = principle;
        this.yield = yield;
        this.compoundRate = coumpoundRate;
        this.investmentDuration = investmentDuration;
        compounds = true;
    }

    // Simple investment i.e. stocks
    public Investment(double principle, double yield, boolean compounds, double investmentDuration)
    {
        this.principle = principle;
        this.yield = yield;
        this.compounds = compounds;
        this.investmentDuration = investmentDuration;
    }

    public double getTotal()
    {
        double total = -1;
        if (compounds)
        {
            total = principle * Math.pow(1 + (yield / (1 / compoundRate)), (1 / compoundRate) * investmentDuration); // 1 over compound rate returns the proper math stuff
        }
        else
        {
            total = principle * (1 + (yield * investmentDuration));
        }
        total = Math.round(total * 100) / 100; // Rounds to 2 decimals
        return total;
    }
}

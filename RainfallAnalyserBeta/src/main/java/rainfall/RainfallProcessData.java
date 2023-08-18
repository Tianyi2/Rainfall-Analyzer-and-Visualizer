package rainfall;

public class RainfallProcessData {
    // Initialization.
    private final double[] monthlyTotal = new double[12];
    private final double[] dailyMin = new double[12];
    private final double[] dailyMax = new double[12];

    /**
     * Calculate monthly total rainfall.
     */
    public void calcMonthlyTotal(int monthNum, double dailyRainfall) {
        monthlyTotal[monthNum - 1] += dailyRainfall;
    }

    /**
     * Calculate monthly daily minimum rainfall.
     */
    public void calcDailyMin(int monthNum, double dailyRainfall) {
        if (dailyRainfall < dailyMin[monthNum - 1]) // Execute when the daily rainfall is smaller than the current smallest daily rainfall.
            dailyMin[monthNum - 1] = dailyRainfall;
    }

    /**
     * Calculate monthly daily maximum rainfall.
     */
    public void calcDailyMax(int monthNum, double dailyRainfall) {
        if (dailyRainfall > dailyMax[monthNum - 1]) // Execute when the daily rainfall is higher than the current highest daily rainfall.
            dailyMax[monthNum - 1] = dailyRainfall;
    }

    /**
     * Pass the monthly total rainfall to where calls.
     */
    public double getMonthlyTotal(int monthNum) {
        return monthlyTotal[monthNum];
    }

    /**
     * Pass the monthly daily minimum rainfall to where calls.
     */
    public double getDailyMin(int monthNum) {
        return dailyMin[monthNum];
    }

    /**
     * Pass the monthly daily maximum rainfall to where calls.
     */
    public double getDailyMax(int monthNum) {
        return dailyMax[monthNum];
    }
}

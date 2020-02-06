package sumo;

public class SimConfig {
    public static final String RICH_TYPE = "rich";
    public static final double RICH_BUDGET = 500;
    public static final String MEDIUM_TYPE = "medium";
    public static final double MEDIUM_BUDGET = 150;
    public static final String POOR_TYPE = "poor";
    public static final double POOR_BUDGET = 10;

    /**
     * speed limit factor <= 1 indicates that the speed limit in the net file corresponds to the enforced norm (regimented)
     * speed limit factor = x > 1 indicates that the enforced norm should be (speed in the net file / x )
     */
    public static double SPEED_LIMIT_FACTOR = 2;

    public static double PCT_RICH;
    public static double PCT_MEDIUM;
    public static double PCT_POOR;

    public static int CAR_NR;
    public static double MIN_GAP;


    public SimConfig() {
    }

    public static void setSpeedLimitFactor(double speedLimitFactor) {
        SPEED_LIMIT_FACTOR = speedLimitFactor;
    }

    public static void setMinGap(double minGap) {
        MIN_GAP = minGap;
    }

    public static void setPctRich(double pctRich) {
        PCT_RICH = pctRich;
    }

    public static void setPctMedium(double pctMedium) {
        PCT_MEDIUM = pctMedium;
    }

    public static void setPctPoor(double pctPoor) {
        PCT_POOR = pctPoor;
    }

    public static void setNrOfCars(int desiredNOfCars) {
        CAR_NR = desiredNOfCars;
    }

    public static String getParamString() {
        return String.format(
                "{car: %d, rich pct: %.2f%%, poor pct: %.2f%%, speed limit factor: %.2f, min gap: %.2f}",
                CAR_NR,
                PCT_RICH,
                PCT_POOR,
                SPEED_LIMIT_FACTOR,
                MIN_GAP);
    }
}

package sumo;

public class Institution {
    /**
     * parameters for calculating the sanction
     */
    private static double SANCTION_BASE = 2;
    private static double SANCTION_EXP = 2;

    /**
     * sanction for violating speed limit is determined as SANCTION_BASE^((monitored_speed - speed_limit)/SANCTION_EXP)
     * It's similar to the idea of Switzerland regulation
     *
     * @param vehicleSpeed
     * @param laneMaxSpeed
     * @return
     */
    public double getSpeedLimitSanction(double vehicleSpeed, double laneMaxSpeed) {
        double enforcedSpeed = getMaxSpeedNorm(laneMaxSpeed);

        if (vehicleSpeed <= enforcedSpeed)
            return 0.0;
        else
            return Math.pow(SANCTION_BASE, (vehicleSpeed - enforcedSpeed) / SANCTION_EXP);
    }

    /**
     * the speed limit from the map fixed with the factor in the simulation parameters
     *
     * @param laneMaxSpeed
     * @return
     */
    public double getMaxSpeedNorm(double laneMaxSpeed) {
        return SimConfig.SPEED_LIMIT_FACTOR > 0 ?
                laneMaxSpeed / SimConfig.SPEED_LIMIT_FACTOR :
                laneMaxSpeed;
    }

    /**
     * The MinGap norm is: ((speed in km/h)/10)^2 (similar to the idea of Italian regulation)
     * For the sanction I apply same similar rule as per speed limit
     *
     * @param vehicleGap
     * @param currSpeed
     * @return
     */
    public double getMinGapSanction(double vehicleGap, double currSpeed) {
        double enforcedMinGap = getMinGapNorm(currSpeed);

        if (vehicleGap >= enforcedMinGap)
            return 0.0;
        else
            return Math.pow(SANCTION_BASE, (enforcedMinGap - vehicleGap) / SANCTION_EXP);
    }

    /**
     * The MinGap norm is: ((speed in km/h)/10)^2 (similar to the idea of Italian regulation)
     *
     * @param speed
     * @return
     */
    public double getMinGapNorm(double speed) {
        return Math.pow((speed * 3.6) / 10, 2);
    }

    /**
     * using this to simplify the experiments
     * rich people always violate all norms, poor people never violate
     *
     * @return
     */
    public double getFixedGapSanction(double gap, double speed) {
        if (gap >= getMinGapNorm(speed))
            return 0.0;
        return SimConfig.RICH_BUDGET - 1;
    }

    public double getFixedSpeedSanction(double carSpeed, double laneSpeed) {
        if (carSpeed <= getMaxSpeedNorm(laneSpeed))
            return 0.0;
        return SimConfig.RICH_BUDGET - 1;
    }


}

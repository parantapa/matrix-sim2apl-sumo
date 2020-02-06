import org.apache.commons.cli.*;
import sumo.EnvironmentAgentInterface;
import sumo.SimConfig;

public class Main {

    public static void main(String[] args) {
        CommandLine parsedArguments = Main.parseArguments(args);
        EnvironmentAgentInterface environmentAgentInterface = new EnvironmentAgentInterface(parsedArguments);
    }

    private static CommandLine parseArguments(String[] args) {
        Options options = Main.createOptions();

        CommandLine parsedArguments = null;

        DefaultParser parser = new DefaultParser();

        try {
            parsedArguments = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getLocalizedMessage());
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.setWidth(120);
            helpFormatter.printHelp("Sim2APL SUMO", options, true);
            System.exit(2);
        }

        return parsedArguments;
    }

    private static Options createOptions() {

        /**
         * SUMO
         */

        final Option sumoBinary = Option.builder("s")
                .argName("SUMO binary")
                .hasArg()
                .required()
                .longOpt("sumo-binary")
                .desc("The exact location of the SUMO binary to execute the SUMO environment")
                .build();

        final Option config = Option.builder("c")
                .argName("Configuration file location")
                .hasArg()
                .required()
                .longOpt("configuration-file")
                .desc("Loads the named config on startup")
                .build();

        final Option netFile = Option.builder("n")
                .argName("Network file location")
                .hasArg()
                .required(false)
                .longOpt("net-file")
                .desc("Load road network description from FILE")
                .build();

        final Option carIDPrefix = Option.builder("p")
                .argName("Car ID prefix")
                .hasArg()
                .required()
                .longOpt("car-id-prefix")
                .desc("Prefix of the car agent ID strings")
                .build();

        final Option stepLength = Option.builder()
                .argName("Step length in seconds")
                .hasArg()
                .required(false)
                .longOpt("step-length")
                .type(Long.TYPE)
                .desc("Defines the step duration in seconds")
                .build();

        final Option collisionAction = Option.builder()
                .argName("none,warn,teleport,remove")
                .hasArg()
                .required(false)
                .longOpt("collision.action")
                .type(String.class)
                .desc("How to deal with collisions: [none,warn,teleport,remove]")
                .build();

        final Option useMatrix = Option.builder()
                .argName("Use Matrix")
                .hasArg()
                .required(false)
                .longOpt("use-matrix")
                .type(Boolean.TYPE)
                .desc("If true use matrix")
                .build();

        /**
         * Instantiation
         */
        final Option nCars = Option.builder()
                .argName("number of cars")
                .hasArg()
                .longOpt("number-of-cars")
                .required()
                .type(Integer.TYPE)
                .desc("The number of cars to place in the environment")
                .build();

        final Option nIterations = Option.builder("i")
                .argName("number of iterations")
                .hasArg()
                .longOpt("number-of-iterations")
                .required(false)
                .type(Long.TYPE)
                .desc("The number of iterations / ticks the simulation should perform. If not specified, simulation will " +
                        "run until interrupted manually")
                .build();

        final Option richPct = Option.builder()
                .argName("percentage of " + SimConfig.RICH_TYPE + "types")
                .hasArg()
                .longOpt(SimConfig.RICH_TYPE)
                .required()
                .type(Double.TYPE)
                .desc("The percentage of cars with type" + SimConfig.RICH_TYPE)
                .build();

        final Option mediumPct = Option.builder()
                .argName("percentage of " + SimConfig.MEDIUM_TYPE + "types")
                .hasArg()
                .longOpt(SimConfig.MEDIUM_TYPE)
                .required()
                .type(Double.TYPE)
                .desc("The percentage of cars with type" + SimConfig.MEDIUM_TYPE)
                .build();

        final Option poorPct = Option.builder()
                .argName("percentage of " + SimConfig.POOR_TYPE + "types")
                .hasArg()
                .longOpt(SimConfig.POOR_TYPE)
                .required()
                .type(Double.TYPE)
                .desc("The percentage of cars with type" + SimConfig.POOR_TYPE)
                .build();

        final Option speedFact = Option.builder()
                .argName("Speed reduction factor")
                .hasArg()
                .longOpt("speed-reduction")
                .required()
                .type(Double.TYPE)
                .desc("The speed reduction factor from the original map speed")
                .build();

        final Option minGap = Option.builder()
                .argName("Minimum Gap")
                .hasArg()
                .longOpt("min-gap")
                .required()
                .type(Double.TYPE)
                .desc("The minimum gap between cars")
                .build();

        /**
         * SIMULATION
         * **/

        final Option agentSeed = Option.builder()
                .argName("Agent seed")
                .hasArg()
                .longOpt("agent-seed")
                .required(false)
                .type(Long.TYPE)
                .desc("The seed used by agents for random operations. This seed should be set to ensure reproducibility." +
                        "If reproducibility is not required, this seed is not necessary")
                .build();

        final Option agentStatistics = Option.builder()
                .argName("Include agent level statistics")
                .hasArg(true)
                .optionalArg(true)
                .longOpt("agent-statistics")
                .required(false)
                .type(boolean.class)
                .desc("Use SUMO logging to log agent statistics at each time step. If no file is specified, a file " +
                        "name will be generated")
                .build();

        final Option routeStatistics = Option.builder()
                .argName("Include agent level statistics")
                .hasArg(true)
                .optionalArg(true)
                .longOpt("route-statistics")
                .required(false)
                .type(boolean.class)
                .desc("Use SUMO logging to log route statistics at each time step. If no file is specified, a file " +
                        "name will be generated")
                .build();

        final Option emissionStatistics = Option.builder()
                .argName("Include agent level statistics")
                .hasArg(true)
                .optionalArg(true)
                .longOpt("emission-statistics")
                .required(false)
                .type(boolean.class)
                .desc("Use SUMO logging to log emission statistics at each time step. If no file is specified, a file " +
                        "name will be generated")
                .build();

        final Option summaryStatistics = Option.builder()
                .argName("Include agent level statistics")
                .hasArg(true)
                .optionalArg(true)
                .longOpt("summary-statistics")
                .required(false)
                .type(boolean.class)
                .desc("Use SUMO logging to log summary statistics at each time step. If no file is specified, a file " +
                        "name will be generated")
                .build();

        final Option statistics = Option.builder()
                .argName("Include all possible statistics")
                .hasArg(false)
                .longOpt("full-statistics")
                .required(false)
                .desc("Use SUMO logging to log all statistics that can individually be toggled in this program." +
                        "Optionally --statistics-directory to specify where the statistics will end up. File names" +
                        "will be generated automatically. To specify individual file names, all statistics have to" +
                        "be individually enabled")
                .build();

        final Option statisticsDir = Option.builder()
                .argName("Directory")
                .hasArg(true)
                .longOpt("statistics-directory")
                .desc("Specify the output directory of statistics files. Default is \"output\"." +
                        "This option is only useful if you let this application generate file names for the" +
                        "statistics automatically")
                .build();

        final Options options = new Options();

        options.addOption(sumoBinary);
        options.addOption(config);
        options.addOption(netFile);
        options.addOption(nCars);
        options.addOption(nIterations);
        options.addOption(stepLength);
        options.addOption(collisionAction);
        options.addOption(richPct);
        options.addOption(mediumPct);
        options.addOption(poorPct);
        options.addOption(speedFact);
        options.addOption(minGap);
        options.addOption(agentSeed);
        options.addOption(agentStatistics);
        options.addOption(routeStatistics);
        options.addOption(useMatrix);
        options.addOption(carIDPrefix);
        options.addOption(emissionStatistics);
        options.addOption(summaryStatistics);
        options.addOption(statistics);
        options.addOption(statisticsDir);

        return options;
    }
}

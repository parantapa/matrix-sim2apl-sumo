package sumo;

import agent.SumoAPLAgent;
import agent.SumoCar2APLAgent;
import agent.context.CarContext;
import agent.plan.CreateRoutePlan;
import agent.planscheme.SumoCarExternalTriggerPlanScheme;
import agent.planscheme.SumoCarGoalPlanScheme;
import agent.trigger.external.EnteredWorldExternalTrigger;
import agent.trigger.external.LeftWorldExternalTrigger;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Agent;
import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentArguments;
import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID;
import nl.uu.cs.iss.ga.sim2apl.core.fipa.FIPAMessenger;
import nl.uu.cs.iss.ga.sim2apl.core.platform.Platform;
import nl.uu.cs.iss.ga.sim2apl.core.tick.DefaultBlockingTickExecutor;
import nl.uu.cs.iss.ga.sim2apl.core.tick.DefaultSimulationEngine;
import nl.uu.cs.iss.ga.sim2apl.core.tick.MatrixTickExecutor;
import nl.uu.cs.iss.ga.sim2apl.core.tick.TickExecutor;
import org.apache.commons.cli.CommandLine;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The EnvironmentAgentInterface is the coupling between the Sim2APL platform and the SUMO environment.
 * It keeps track of creating the 2APL agents, and, for each agent, stores references to both the 2APL agent instance
 * and the SUMO ID, so the corresponding 2APL agent can be found for any SUMO agent, and vice versa.
 */
public class EnvironmentAgentInterface {

    private static final Logger LOG = Logger.getLogger(EnvironmentAgentInterface.class.getName());

    /**
     * Basic Simulation classes
     **/
    private Platform platform;
    private SumoEnvironmentInterface environmentInterface;

    /**
     * Basis for agent poor/rich distribution
     */
    private WeightedRandomBag<String> agentTypesDistribution;
    private Institution institution = new Institution();

    /** **/
    private final int desiredNOfCars;
    private int routesCounter = 0;

    /**
     * Maps to map agents from SUMO to 2APL. Vice versa is not required, since SUMO-ID is present
     * on the SumoAPLAgent interface
     **/
    private Map<String, SumoAPLAgent> sumoAgents = new HashMap<>();

    /**
     * Default constructor. Creates the Sim2APL platform, simulation engine and environment interface.
     * Starts the simulation when everything is ready automatically
     *
     * @param parsedArguments Parsed command line arguments
     */
    public EnvironmentAgentInterface(CommandLine parsedArguments) {
        LOG.fine("Constructing EnvironmentAgentInterface");
        this.desiredNOfCars = Integer.parseInt(parsedArguments.getOptionValue("number-of-cars"));
        int nIterations = -1;
        if (parsedArguments.hasOption("number-of-iterations"))
            nIterations = Integer.parseInt(parsedArguments.getOptionValue("number-of-iterations"));

        String seed = parsedArguments.getOptionValue("agent-seed");
        Random rnd = new Random();
        if (seed != null) {
            rnd.setSeed(Long.parseLong(seed));
            LOG.info("Seed provided for random agent actions was " + seed);
        } else {
            LOG.info("No agent seed specified. Using random number generator");
        }

        parseDistribution(parsedArguments, rnd);

        TickExecutor executor;
        if (parsedArguments.hasOption("use-matrix") && Boolean.parseBoolean(parsedArguments.getOptionValue("use-matrix"))) {
            executor = new MatrixTickExecutor(4, rnd);
        } else {
            executor = new DefaultBlockingTickExecutor(4, rnd);
        }

        this.platform = Platform.newPlatform(executor, new FIPAMessenger());
        this.environmentInterface = new SumoEnvironmentInterface(parsedArguments, rnd);
        this.environmentInterface.addEnvironmentListener(this);
        createInitialAgents(parsedArguments.getOptionValue("car-id-prefix"));

        LOG.info("Starting simulation with " + nIterations + " steps");

        DefaultSimulationEngine engine = new DefaultSimulationEngine(platform, nIterations, this.environmentInterface);
        engine.start();
    }

    /**
     * Obtain a reference to the 2APL platform
     *
     * @return Platform
     */
    public Platform getPlatform() {
        return platform;
    }

    /**
     * Obtain a reference to the environment interface
     *
     * @return Environment interface
     */
    public SumoEnvironmentInterface getEnvironmentInterface() {
        return environmentInterface;
    }

    /**
     * Obtain a reference to the institution enforcing norms
     */
    public Institution getInstitution() {
        return institution;
    }

    /**
     * Create a unique route ID. This is just an incremental ID string.
     *
     * @return Unique route ID for use in SUMO
     */
    public String getNewRouteID() {
        String routeId = String.format("route-%d", this.routesCounter);
        routesCounter++;
        return routeId;
    }

    /**
     * Instantiate the SimConfig parameters from command line arguments
     * @param args  Reference to command line arguments
     */
    private void parseDistribution(CommandLine args, Random rnd) {
        double pctRich = Double.parseDouble(args.getOptionValue(SimConfig.RICH_TYPE));
        double pctMedium = Double.parseDouble(args.getOptionValue(SimConfig.MEDIUM_TYPE));
        double pctPoor = Double.parseDouble(args.getOptionValue(SimConfig.POOR_TYPE));

        double total = pctRich + pctMedium + pctPoor;

        if(total != 100) {
            throw new IllegalStateException("Total percentage of agent types (rich + medium + poor) needs to be 100");
        }

        this.agentTypesDistribution = new WeightedRandomBag<>(rnd);

        agentTypesDistribution.addEntry(SimConfig.RICH_TYPE, pctRich);
        agentTypesDistribution.addEntry(SimConfig.POOR_TYPE, pctPoor);
        agentTypesDistribution.addEntry(SimConfig.MEDIUM_TYPE, pctMedium);

        SimConfig.setPctRich(pctRich);
        SimConfig.setPctPoor(pctPoor);
        SimConfig.setPctMedium(pctMedium);

        SimConfig.setSpeedLimitFactor(Double.parseDouble(args.getOptionValue("speed-reduction")));
        SimConfig.setMinGap(Double.parseDouble(args.getOptionValue("min-gap")));
    }

    /**
     * Construct the initial set of agents, based on the number of agents specified in the command line arguments
     */
    private void createInitialAgents(String carIdPrefix) {
        LOG.info("Creating " + this.desiredNOfCars + " cars");
        for (int i = 0; i < this.desiredNOfCars; i++) {
            SumoAPLAgent agent = InstantiateAgent(carIdPrefix, i, agentTypesDistribution.getRandom());
            if (agent != null) {
                this.sumoAgents.put(agent.getSumoID(), agent);
            }
        }
    }

    /**
     * Instantiates a new 2APL agent in the 2APL platform, and creations the SumoAPLAgent object
     *
     * @param agentIndex Index of the agent (used for incremental ID generation)
     * @return SumoAPLAgent object
     */
    private SumoAPLAgent InstantiateAgent(String carIdPrefix, int agentIndex, String type) {
        String agentID = String.format("%s-%s-%d", SumoCar2APLAgent.TYPE_ID, carIdPrefix, agentIndex);
        LOG.fine("Constructing agent " + agentID);
        SumoCar2APLAgent agentInterface = new SumoCar2APLAgent(agentID);

        AgentArguments args = new AgentArguments();
        args.addContext(new CarContext(this, agentInterface, type));
        args.addExternalTriggerPlanScheme(new SumoCarExternalTriggerPlanScheme());
        args.addGoalPlanScheme(new SumoCarGoalPlanScheme());
        args.addInitialPlan(new CreateRoutePlan());

        Agent agent;
        try {
            AgentID id = AgentID.createEmpty();
            id.setName(agentID);
            agent = new Agent(this.platform, args, id);
            agentInterface.setAgent(agent);
        } catch (URISyntaxException e) {
            LOG.log(Level.SEVERE, "Error creating agent " + agentID, e);
            return null;
        }

        return agentInterface;
    }

    /**
     * Listens to updates from the environment about agents that have arrived at their destination and thus left the
     * environment. Notifies all these agents through an external trigger.
     *
     * @param arrivedAgents SUMO agent ID's of agents that have arrived at their destination and thus been removed from
     *                      the SUMO environment
     */
    void notifyAgentsArrived(List<String> arrivedAgents) {
        arrivedAgents.forEach(ra -> {
            SumoAPLAgent agentInterface = this.sumoAgents.get(ra);
            if (agentInterface != null) {
                Agent a = agentInterface.getAgent();
                a.addExternalTrigger(new LeftWorldExternalTrigger());
            }
        });
    }

    /**
     * Listens to updates form the environment about agents that have succesfully entered the SUMO environment.
     * Notifies all these agents through an external trigger
     *
     * @param enteredAgents SUMO agent ID's of successfully entered agents
     */
    void notifyAgentsEntered(List<String> enteredAgents) {
        enteredAgents.forEach(ea -> {
            SumoAPLAgent agentInterface = this.sumoAgents.get(ea);
            if (agentInterface != null) {
                Agent a = agentInterface.getAgent();
                a.addExternalTrigger(new EnteredWorldExternalTrigger());
            }
        });
    }
}

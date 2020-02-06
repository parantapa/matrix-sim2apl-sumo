package agent.plan;

import agent.context.CarContext;
import de.tudresden.sumo.cmd.Route;
import nl.uu.cs.iss.ga.sim2apl.core.agent.PlanToAgentInterface;
import nl.uu.cs.iss.ga.sim2apl.core.plan.Plan;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanExecutionError;
import sumo.EnvironmentAgentInterface;

import java.util.List;
import java.util.logging.Logger;

/**
 * A plan to create a new random route in the SUMO environment. This plan should be executed always before
 * trying to enter the simulation environment.
 *
 * If a previous route exists, the agent will first try to find a route in the reverse direction, only falling back
 * on trying to find a route between two random edges if that reversed route could not be created. If a previous
 * route already existed, that is also an indication the agent has previously left the environment. Therefor, the
 * agent adopts the plan to enter the world right after it has created and uploaded its new route.
 */
public class CreateRoutePlan extends Plan {
    private static final Logger LOG = Logger.getLogger(CreateRoutePlan.class.getName());

    public CreateRoutePlan() { }

    @Override
    public String execute(PlanToAgentInterface planToAgentInterface) throws PlanExecutionError {
        LOG.fine("Agent " + planToAgentInterface.getAgentID().getUuID() + " executing CreateRoutePlan plan");
        CarContext context = planToAgentInterface.getContext(CarContext.class);
        EnvironmentAgentInterface eaInterface = context.getEnvironmentAgentInterface();

        String routeID = eaInterface.getEnvironmentInterface().getRandomRoute();
        List<String> routeEdges = (List<String>) eaInterface.getEnvironmentInterface().do_job_get(Route.getEdges(routeID));
        LOG.fine("Agent " + planToAgentInterface.getAgentID().getUuID() + "picked random route " +
                routeID + " with " +routeEdges.size() + " edges");
        context.updateRoute(routeEdges.get(0), routeEdges, routeID);
        planToAgentInterface.adoptPlan(new EnterWorldPlan());
        setFinished(true);
        return  null;
    }

}

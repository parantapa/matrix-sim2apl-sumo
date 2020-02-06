package agent.plan;

import agent.context.CarContext;
import agent.trigger.goal.SetMaxSpeedMaintenanceGoal;
import agent.trigger.goal.SetMinGapMaintenanceGoal;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Goal;
import nl.uu.cs.iss.ga.sim2apl.core.agent.PlanToAgentInterface;
import nl.uu.cs.iss.ga.sim2apl.core.plan.Plan;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanExecutionError;
import sumo.EnvironmentAgentInterface;

import java.util.List;
import java.util.logging.Logger;

/**
 * A plan that allows the agent to enter the SUMO simulation environment.
 * <p>
 * Before this plan is performed, a route should have been created and the SUMO environment should have been informed
 * of that route.
 */
public class EnterWorldPlan extends Plan {
    private static final Logger LOG = Logger.getLogger(EnterWorldPlan.class.getName());

    @Override
    public String execute(PlanToAgentInterface planToAgentInterface) throws PlanExecutionError {
        LOG.fine("Agent " + planToAgentInterface.getAgentID().getUuID() + " executing EnterWorldPlan plan");
        CarContext context = planToAgentInterface.getContext(CarContext.class);
        EnvironmentAgentInterface eaInterface = context.getEnvironmentAgentInterface();
        
        EnterWorldPlanMessage message = new EnterWorldPlanMessage(
            context.getAgentInterface().getSumoID(),
            context.getAgentInterface().getTypeID(),
            context.getRouteID(),
            eaInterface.getEnvironmentInterface().simulationTime,
            eaInterface.getEnvironmentInterface().getLaneForEdge(context.getCurrentLocation())
        );

        LOG.fine("Agent " + planToAgentInterface.getAgentID().getUuID() + " entered world at route " + context.getRouteID());

        List<Goal> goals = planToAgentInterface.getAgent().getGoals();

        boolean hasSpeedGoal = false;
        boolean hasMinGapGoal = false;

        for (Goal g : goals) {
            if (g instanceof SetMaxSpeedMaintenanceGoal) hasSpeedGoal = true;
            if (g instanceof SetMinGapMaintenanceGoal) hasMinGapGoal = true;
            if (hasSpeedGoal && hasMinGapGoal) break;
        }

        if (!hasSpeedGoal) {
            planToAgentInterface.adoptGoal(new SetMaxSpeedMaintenanceGoal());
            LOG.fine("Agent " + planToAgentInterface.getAgentID().getUuID() + " adopted speed maintenance goal");
        } else {
            LOG.fine("Agent " + planToAgentInterface.getAgentID().getUuID() + "already has a max speed " +
                    "maintenance goal. Not adopting");
        }

        if (!hasMinGapGoal) {
            planToAgentInterface.adoptGoal(new SetMinGapMaintenanceGoal());
            LOG.fine("Agent " + planToAgentInterface.getAgentID().getUuID() + " adopted min gap maintenance goal");
        } else {
            LOG.fine("Agent " + planToAgentInterface.getAgentID().getUuID() + "already has a min gap " +
                    "maintenance goal. Not adopting");
        }

        setFinished(true);
        
        return message.toJson();
    }
}

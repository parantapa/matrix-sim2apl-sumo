package agent.plan;

import agent.context.CarContext;
import de.tudresden.sumo.cmd.Vehicle;
import nl.uu.cs.iss.ga.sim2apl.core.agent.PlanToAgentInterface;
import nl.uu.cs.iss.ga.sim2apl.core.plan.Plan;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanExecutionError;
import sumo.EnvironmentAgentInterface;
import sumo.SimConfig;

public class SetMinGapPlan extends Plan {

    @Override
    public String execute(PlanToAgentInterface planToAgentInterface) throws PlanExecutionError {
        CarContext context = planToAgentInterface.getContext(CarContext.class);
        EnvironmentAgentInterface eaInterface = context.getEnvironmentAgentInterface();

        if (!eaInterface.getEnvironmentInterface().isAgentActive(context.getAgentInterface().getSumoID())) {
            // Do not try to take actions in the environment if you do not exist there
            return null;
        }

        //this ideally should be kept in the agent context instead of retrieved from the env. it's the CURRENT MIN GAP SET BY THE AGENT
        Object currSpeedObject = eaInterface.getEnvironmentInterface().do_job_get(
                Vehicle.getSpeed(context.getAgentInterface().getSumoID()));

        if (currSpeedObject == null) return null;
        double currSpeed = (double) currSpeedObject;

        /**
         * Reasoning/planning of a norm-aware agent: uses info from
         * its belief base (car's current speed, budget)
         * NOTHING FROM THE ENVIRONMENT. WE COULD MAKE IT MORE complex and sense if there is a ca ahead
         * institution (the norm enforced in the city and the sanction)
         * The agent tries to minimize its gap as much it can afford
         * (here i'm simplifying, increasing the complexity)
         */
        double increment = 0.1;
        double new_gap = 200;

        //rich people always violate all norms, poor people never violate
        while (new_gap > SimConfig.MIN_GAP + increment && context.getBudget() > eaInterface.getInstitution().getFixedGapSanction(new_gap, currSpeed)) {
            new_gap -= increment;
        }

        setFinished(true);

        SetMinGapPlanMessage message = new SetMinGapPlanMessage(context.getAgentInterface().getSumoID(), new_gap);

        return message.toJson();
    }
}

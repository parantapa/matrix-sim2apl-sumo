package agent.plan;

import agent.context.CarContext;
import de.tudresden.sumo.cmd.Vehicle;
import nl.uu.cs.iss.ga.sim2apl.core.agent.PlanToAgentInterface;
import nl.uu.cs.iss.ga.sim2apl.core.plan.Plan;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanExecutionError;
import sumo.EnvironmentAgentInterface;

public class SetMaxSpeedPlan extends Plan {

    @Override
    public String execute(PlanToAgentInterface planToAgentInterface) throws PlanExecutionError {
        CarContext context = planToAgentInterface.getContext(CarContext.class);
        EnvironmentAgentInterface eaInterface = context.getEnvironmentAgentInterface();

        if (!eaInterface.getEnvironmentInterface().isAgentActive(context.getAgentInterface().getSumoID())) {
            // Do not try to take actions in the environment if you do not exist there
            return null;
        }

        Object laneIDObject = eaInterface.getEnvironmentInterface()
                .do_job_get(Vehicle.getLaneID(context.getAgentInterface().getSumoID()));
        if (laneIDObject == null) return null;
        String laneID = (String) laneIDObject;
        if (laneID.isEmpty()) return null;

        double currLaneMaxSpeed = eaInterface.getEnvironmentInterface().getLaneMaxSpeed(laneID); //sense the environment (e.g., street signs)

        /**
         * Reasoning/planning of a norm-aware agent: uses info from
         * its belief base (car's physical max speed, budget)
         * environment (the maximum speed of the current lane)
         * institution (the norm enforced in the current lane and the sanction)
         * The agent tries to maximize its speed as much the vehicle can go and as much it can afford
         * (here i'm simplifying, increasing the complexity)
         */
        double increment = 0.1;
        double newSpeed = 0.1;

        //rich people always violate all norms, poor people never violate
        while (newSpeed < CarContext.CAR_MAX_SPEED - increment &&
                context.getBudget() > eaInterface.getInstitution().getFixedSpeedSanction(newSpeed, currLaneMaxSpeed)) {
            newSpeed += increment;
        }

        setFinished(true);

        SetMaxSpeedPlanMessage message = new SetMaxSpeedPlanMessage(context.getAgentInterface().getSumoID(), newSpeed);

        return message.toJson();
    }
}

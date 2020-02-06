package agent.planscheme;

import agent.context.CarContext;
import agent.plan.CreateRoutePlan;
import agent.trigger.external.LeftWorldExternalTrigger;
import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentContextInterface;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Trigger;
import nl.uu.cs.iss.ga.sim2apl.core.plan.Plan;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanScheme;

public class SumoCarExternalTriggerPlanScheme implements PlanScheme {
    @Override
    public Plan instantiate(Trigger trigger, AgentContextInterface agentContextInterface) {
        Plan plan = Plan.UNINSTANTIATED;

        if(trigger instanceof LeftWorldExternalTrigger) {
            CarContext context = agentContextInterface.getContext(CarContext.class);
            context.setInWorld(false);

            plan = new CreateRoutePlan();
        }

        return plan;
    }
}

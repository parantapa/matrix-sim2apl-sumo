package agent.planscheme;

import agent.plan.SetMaxSpeedPlan;
import agent.plan.SetMinGapPlan;
import agent.trigger.goal.SetMaxSpeedMaintenanceGoal;
import agent.trigger.goal.SetMinGapMaintenanceGoal;
import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentContextInterface;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Trigger;
import nl.uu.cs.iss.ga.sim2apl.core.plan.Plan;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanScheme;

public class SumoCarGoalPlanScheme implements PlanScheme {

    @Override
    public Plan instantiate(Trigger trigger, AgentContextInterface agentContextInterface) {
        Plan plan = Plan.UNINSTANTIATED;

        if(trigger instanceof SetMaxSpeedMaintenanceGoal) {
            plan = new SetMaxSpeedPlan();
        } else if (trigger instanceof SetMinGapMaintenanceGoal) {
            plan = new SetMinGapPlan();
        }

        if(plan != Plan.UNINSTANTIATED) {
            plan.setPlanGoal(trigger);
        }

        return plan;
    }
}

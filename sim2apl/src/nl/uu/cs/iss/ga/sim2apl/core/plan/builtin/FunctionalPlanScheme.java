package nl.uu.cs.iss.ga.sim2apl.core.plan.builtin;

import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentContextInterface;
import nl.uu.cs.iss.ga.sim2apl.core.agent.PlanToAgentInterface;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Trigger;
import nl.uu.cs.iss.ga.sim2apl.core.plan.Plan;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanExecutionError;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanScheme;

/**
 * A premade plan scheme to make code more concise when developing an agent. 
 * @author Bas Testerink
 */
public final class FunctionalPlanScheme implements PlanScheme {
	private final FunctionalPlanSchemeInterface myInterface;
	
	public FunctionalPlanScheme(final FunctionalPlanSchemeInterface myInterface){
		this.myInterface = myInterface;
	}
	
	@Override
	public final Plan instantiate(final Trigger trigger, final AgentContextInterface contextInterface){
		SubPlanInterface plan = this.myInterface.getPlan(trigger, contextInterface);
		if(plan == SubPlanInterface.UNINSTANTIATED) return Plan.UNINSTANTIATED;
		else return new RunOncePlan() {
			@Override
			public final Object executeOnce(final PlanToAgentInterface planInterface)
					throws PlanExecutionError {
				return plan.execute(planInterface);
			}
		};
	}

}

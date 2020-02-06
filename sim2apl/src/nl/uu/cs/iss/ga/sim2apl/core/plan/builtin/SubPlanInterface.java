package nl.uu.cs.iss.ga.sim2apl.core.plan.builtin;

import nl.uu.cs.iss.ga.sim2apl.core.agent.PlanToAgentInterface;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanExecutionError;

/**
 * This functional interface is used to create subplans that are executed across multiple deliberation cycles. 
 * 
 * @author Bas Testerink
 */
public interface SubPlanInterface {
	/** Specification of the plan to be executed. */
	public Object execute(final PlanToAgentInterface planInterface) throws PlanExecutionError;
	/** A token that indicates that whatever scheme tries to make an interface did not fire. */ 
	public final static SubPlanInterface UNINSTANTIATED = new SubPlanInterface(){@Override
	public final Object execute(final PlanToAgentInterface planInterface){return null;}};
}

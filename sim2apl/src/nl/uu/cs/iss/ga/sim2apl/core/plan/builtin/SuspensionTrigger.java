package nl.uu.cs.iss.ga.sim2apl.core.plan.builtin;

import nl.uu.cs.iss.ga.sim2apl.core.agent.Trigger;
/** 
 * This trigger is an auxiliary trigger that is used by the interceptor toolkit in order to suspend plans over one or more deliberation cycles. 
 * @author Bas Testerink
 */
public final class SuspensionTrigger implements Trigger {
	private static final SuspensionTrigger INSTANCE = new SuspensionTrigger();
	private SuspensionTrigger(){}
	public static final SuspensionTrigger getInstance(){ return INSTANCE; }
}

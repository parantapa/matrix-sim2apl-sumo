package nl.uu.cs.iss.ga.sim2apl.core.defaults.deliberationsteps;

import java.util.List;

import nl.uu.cs.iss.ga.sim2apl.core.deliberation.DeliberationStepException;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Agent;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Trigger;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanScheme;

/**
 * Step that applies the internal trigger plan schemes to the internal triggers.
 * @author Bas Testerink
 */
public final class ApplyInternalTriggerPlanSchemes extends DefaultDeliberationStep {
	
	public  ApplyInternalTriggerPlanSchemes(final Agent agent){
		super(agent);
	}
	
	/** Simply grab the internal triggers and relevant plan schemes and try their application. */
	@Override
	public final void execute() throws DeliberationStepException{
		List<Trigger> triggers = super.agent.getAndRemoveInternalTriggers();
		super.applyTriggerInterceptors(triggers, super.agent.getInternalTriggerInterceptors()); 
		List<PlanScheme> planSchemes = super.agent.getInternalTriggerPlanSchemes();
		super.applyPlanSchemes(triggers, planSchemes);
	}
}
package nl.uu.cs.iss.ga.sim2apl.core.defaults.deliberationsteps;
 
import java.util.List;

import nl.uu.cs.iss.ga.sim2apl.core.deliberation.DeliberationStepException;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Agent;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Trigger;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanScheme;

/**
 * Step that applies the external trigger plan schemes to the external triggers.
 * @author Bas Testerink
 */
public final class ApplyExternalTriggerPlanSchemes extends DefaultDeliberationStep { 
	
	public  ApplyExternalTriggerPlanSchemes(final Agent agent){
		super(agent);
	}
	
	/** Simply grab the external triggers and relevant plan schemes and try their application. */
	@Override
	public final void execute() throws DeliberationStepException{
		List<Trigger> triggers = super.agent.getAndRemoveExternalTriggers();
		super.applyTriggerInterceptors(triggers, super.agent.getExternalTriggerInterceptors());
		List<PlanScheme> planSchemes = super.agent.getExternalTriggerPlanSchemes();
		super.applyPlanSchemes(triggers, planSchemes);
	}
}

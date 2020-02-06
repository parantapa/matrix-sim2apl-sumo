package nl.uu.cs.iss.ga.sim2apl.core.defaults.deliberationsteps;

import java.util.List;

import nl.uu.cs.iss.ga.sim2apl.core.deliberation.DeliberationStepException;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Agent;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Trigger;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanScheme;

/**
 * Step that applies the goal plan schemes to the current goals.
 * @author Bas Testerink
 */

public final class ApplyGoalPlanSchemes extends DefaultDeliberationStep {
	
	public  ApplyGoalPlanSchemes(final Agent agent){
		super(agent);
	}
	
	/** First clears all achieved goals and then grabs the goals and goal plan schemes and tries to apply the plan schemes. */
	@Override
	public final void execute() throws DeliberationStepException{
		super.agent.clearAchievedGoals();
		List<? extends Trigger> triggers = super.agent.getGoals();
		super.applyTriggerInterceptors(triggers, super.agent.getGoalInterceptors());
		List<PlanScheme> planSchemes = super.agent.getGoalPlanSchemes();
		super.applyPlanSchemes(triggers, planSchemes);
	}
} 
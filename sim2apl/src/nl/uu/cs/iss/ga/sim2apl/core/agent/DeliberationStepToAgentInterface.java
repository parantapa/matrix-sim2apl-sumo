package nl.uu.cs.iss.ga.sim2apl.core.agent;
  
import java.util.Iterator;
import java.util.List;

import nl.uu.cs.iss.ga.sim2apl.core.fipa.MessageInterface;
import nl.uu.cs.iss.ga.sim2apl.core.plan.Plan;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanExecutionError;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanScheme;
import nl.uu.cs.iss.ga.sim2apl.core.plan.TriggerInterceptor;

/**
 * This interface exposes all functionalities of an agent to perform a deliberation step. 
 * This includes mainly obtain certain specifications of the agent such as its plan schemes 
 * and the lists of current triggers. This interface also exposes the possibility to execute 
 * plans.
 * 
 * @author Bas Testerink
 */
public final class DeliberationStepToAgentInterface {
	/** The agent that is exposed by this interface. */
	private final Agent agent;
	
	public DeliberationStepToAgentInterface(final Agent agent){
		this.agent = agent;
	}
	
	/** Remove a plan from the list of current plans. */
	public final void removePlan(final nl.uu.cs.iss.ga.sim2apl.core.plan.Plan plan){ this.agent.removePlan(plan); }
	
	/** Add an execution error to the internal triggers of the agent. */
	public final void addPlanExecutionError(final nl.uu.cs.iss.ga.sim2apl.core.plan.PlanExecutionError error){ agent.addInternalTrigger(error); }
	
	/** Remove all goals that are achieved given the contexts of the agent. */
	public final void clearAchievedGoals(){ this.agent.clearAchievedGoals(); }
	
	/** Obtain new list that contains the current goals. Manipulating the returned list 
	 * will not add/remove goals to the agent. The goals itself though are not cloned. */
	public final List<Goal> getGoals(){ return this.agent.getGoals(); }
	
	/** Obtain and remove the current external triggers. This will return a new 
	 * list of triggers. */
	public final List<Trigger> getAndRemoveExternalTriggers(){ return this.agent.getAndRemoveExternalTriggers(); }
	
	/** Obtain and remove the current internal triggers. This will return a new 
	 * listof triggers. */
	public final List<Trigger> getAndRemoveInternalTriggers(){ return this.agent.getAndRemoveInternalTriggers(); }
	
	/** Obtain and remove the current message triggers. This will return a new 
	 * list of triggers. */
	public final List<MessageInterface> getAndRemoveMessages(){ return this.agent.getAllMessages(); } 
	
	/** Get the goal plan schemes of the plan scheme base. */
	public final List<nl.uu.cs.iss.ga.sim2apl.core.plan.PlanScheme> getGoalPlanSchemes(){ return this.agent.getGoalPlanSchemes(); }
	
	/** Get the external trigger plan schemes of the plan scheme base. */
	public final List<nl.uu.cs.iss.ga.sim2apl.core.plan.PlanScheme> getExternalTriggerPlanSchemes(){ return this.agent.getExternalTriggerPlanSchemes(); }
	
	/** Get the internal trigger plan schemes of the plan scheme base. */
	public final List<nl.uu.cs.iss.ga.sim2apl.core.plan.PlanScheme> getInternalTriggerPlanSchemes(){ return this.agent.getInternalTriggerPlanSchemes(); }
	
	/** Get the message plan schemes of the plan scheme base. */
	public final List<nl.uu.cs.iss.ga.sim2apl.core.plan.PlanScheme> getMessagePlanSchemes(){ return this.agent.getMessagePlanSchemes(); }

	/** Get the goal interceptors. */
	public final Iterator<nl.uu.cs.iss.ga.sim2apl.core.plan.TriggerInterceptor> getGoalInterceptors(){ return this.agent.getGoalInterceptors(); }

	/** Get the external trigger interceptors. */
	public final Iterator<nl.uu.cs.iss.ga.sim2apl.core.plan.TriggerInterceptor> getExternalTriggerInterceptors(){ return this.agent.getExternalTriggerInterceptors(); }

	/** Get the internal trigger interceptors. */
	public final Iterator<nl.uu.cs.iss.ga.sim2apl.core.plan.TriggerInterceptor> getInternalTriggerInterceptors(){ return this.agent.getInternalTriggerInterceptors(); }

	/** Get the message interceptors. */
	public final Iterator<TriggerInterceptor> getMessageInterceptors(){ return this.agent.getMessageInterceptors(); }
	
	/**
	 * Try to apply for a given trigger a given plan scheme. If the plan scheme can 
	 * instantiate given the trigger and the current contexts of the agent, then the 
	 * plan will be inserted into the list of current plans. 
	 * @param trigger Trigger that may trigger the plan scheme. 
	 * @param planScheme Plan scheme to try out.
	 * @return True iff the plan scheme was instantiated. 
	 */
	public final boolean tryApplication(final Trigger trigger, final PlanScheme planScheme){ return this.agent.tryApplication(trigger, planScheme); }
	
	/** Get a new list with the current instantiated plans of the agent.	 */
	public final List<nl.uu.cs.iss.ga.sim2apl.core.plan.Plan> getPlans(){ return this.agent.getPlans(); }
	
	/**
	 * Execute a given plan. This method will first check whether the plan has a goal 
	 * and if so, whether that goal is still relevant. In case the plan has a goal and the 
	 * goal is not relevant anymore (because it is not in the list of current goals anymore) 
	 * then the plan will not be executed.
	 */
	public final Object executePlan(final Plan plan) throws PlanExecutionError {
		return this.agent.executePlan(plan);
	}  
}

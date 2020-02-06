package nl.uu.cs.iss.ga.sim2apl.core.agent;

/**
 * This interface exposes the possiblity to obtain an agents contexts.
 * 
 * @author Bas Testerink
 */
// TODO: We refactored a lot: check if this interface does anything useful.
public final class AgentContextInterface {
	/** The agent that is exposed by this interface. */
	private final nl.uu.cs.iss.ga.sim2apl.core.agent.Agent agent;
	
	public AgentContextInterface(final Agent agent){
		this.agent = agent;
	}
	
	/** Obtain the context that belongs to a given class. */
	public final <C extends Context> C getContext(final Class<C> klass){
		return agent.getContext(klass);
	}
}

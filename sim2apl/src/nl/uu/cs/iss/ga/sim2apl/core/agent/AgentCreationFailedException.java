package nl.uu.cs.iss.ga.sim2apl.core.agent;
/**
 * Exeption that can be thrown during the creation of an agent.
 * 
 * @author Bas Testerink
 */
public final class AgentCreationFailedException extends Exception { 
	private static final long serialVersionUID = 2L;
	
	// TODO: expand exceptions in the code
	
	public AgentCreationFailedException(final String detailedMessage){
		super(detailedMessage);
	}
	
	public AgentCreationFailedException(final String detailedMessage, final Throwable cause){
		super(detailedMessage, cause);
	}
}
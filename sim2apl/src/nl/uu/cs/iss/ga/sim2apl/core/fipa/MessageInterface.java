package nl.uu.cs.iss.ga.sim2apl.core.fipa;

import java.util.Collection;

import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Trigger;

public interface MessageInterface extends Trigger {
	
	public Collection<nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID> getReceiver();

	public AgentID getSender();
	
	public void addUserDefinedParameter(String key, String value);
	
	public String getUserDefinedParameter(String key);
	
	public String getContent();
}

package nl.uu.cs.iss.ga.sim2apl.core.fipa.ams;

import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Trigger;

public class ReceiveRemoteAddress implements Trigger {

	private AgentID agentID;
	public AgentID getAgentID() { return agentID; }
	
	public ReceiveRemoteAddress(AgentID agentID) {
		this.agentID = agentID;
	}
}

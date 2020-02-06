package nl.uu.cs.iss.ga.sim2apl.core.agent;

import nl.uu.cs.iss.ga.sim2apl.core.fipa.MessageInterface;

public interface AgentInterface {
	void receiveMessage(MessageInterface message);
	void forceStop();
}

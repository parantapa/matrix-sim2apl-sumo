package nl.uu.cs.iss.ga.sim2apl.core.defaults.messenger;

import java.util.HashMap;
import java.util.Map;

import nl.uu.cs.iss.ga.sim2apl.core.fipa.MessageInterface;
import nl.uu.cs.iss.ga.sim2apl.core.messaging.Messenger;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Agent;
import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID;

/**
 * The default messenger is a very simple implementation for communication between 
 * agents on the same JVM instance. 
 * 
 * @author Bas Testerink
 */
public final class DefaultMessenger implements Messenger<MessageInterface> { 
	/** Stores the interfaces to agents to inject messages. */
	private final Map<nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID, nl.uu.cs.iss.ga.sim2apl.core.agent.Agent> agents;

	public DefaultMessenger(){
		this.agents = new HashMap<nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID, nl.uu.cs.iss.ga.sim2apl.core.agent.Agent>();
	} 

	/** Store the agent interface. */
	@Override
	public final void register(nl.uu.cs.iss.ga.sim2apl.core.agent.Agent agent){
		synchronized(this.agents){ 
			this.agents.put(agent.getAID(), agent);
		}
	}

	/** Remove the agent interface from the messenger. */
	@Override
	public final void deregister(final nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID agentID){
		synchronized(this.agents){ 
			this.agents.remove(agentID); 
		}
	} 
	
	/** Grab the agent interface of the receiver and add the message in the receiving agent. */
	public final void sendMessage(final nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID receiver, final MessageInterface message) throws nl.uu.cs.iss.ga.sim2apl.core.defaults.messenger.MessageReceiverNotFoundException {
		synchronized(this.agents){
			Agent agent = this.agents.get(receiver);
			if(agent == null){
				//TODO send message to sender that receiver is unknown instead of exception
				throw new nl.uu.cs.iss.ga.sim2apl.core.defaults.messenger.MessageReceiverNotFoundException("Trying to send to non-existent agent "+receiver+".");
			} else {
				agent.receiveMessage(message);
			}
		} 
	}

	@Override
	public void agentDied(nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID agentID) {
		this.deregister(agentID);		
	}

	@Override
	public void deliverMessage(nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID receiver, MessageInterface message) throws nl.uu.cs.iss.ga.sim2apl.core.defaults.messenger.MessageReceiverNotFoundException {
		this.sendMessage(receiver, message);
		
	}

	@Override
	public void deliverMessage(MessageInterface message) throws MessageReceiverNotFoundException {
		if(message.getReceiver()!=null && message.getReceiver().size()>0) {
			for (nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID toID : message.getReceiver()) {
				this.sendMessage(toID, message);
			}
		}else {
			for (AgentID agentID : agents.keySet()) {
				this.sendMessage(agentID, message);
			}
		}
	}
	
	@Override
	public boolean implementsEncoding() {
		return false;
	}
	
	@Override
	public byte[] encodeMessage(MessageInterface message) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("DefaultMessage does not implement encode/decode.");
	}

	@Override
	public MessageInterface decodeMessage(byte[] asBytes) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("DefaultMessage does not implement encode/decode.");
	}
} 
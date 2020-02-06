package nl.uu.cs.iss.ga.sim2apl.core.fipa.acl;

import java.util.HashSet;

import nl.uu.cs.iss.ga.sim2apl.core.agent.Agent;

import java.io.Serializable;

/**
 * This class represents a list of conversations that an agent is currently
 * carrying out and allows creating a <code>MessageTemplate</code> that matches
 * only messages that do not belong to any of these conversations.
 */
public class ConversationList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4816153325526986564L;

	private HashSet<String> conversations = new HashSet<>();
	protected Agent myAgent = null;
	protected int cnt = 0;

	private nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.MessageTemplate myTemplate = new nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.MessageTemplate(new nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.MessageTemplate.MatchExpression() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2170563179112844348L;

		@Override
		public boolean match(ACLMessage msg) {
			String convId = msg.getConversationId();
			return (convId == null || (!conversations.contains(convId)));
		}
	});

	/**
	 * Construct a ConversationList to be used inside a given agent.
	 */
	public ConversationList(Agent a) {
		myAgent = a;
	}

	/**
	 * Register a conversation creating a new unique ID.
	 */
	public String registerConversation() {
		String id = createConversationId();
		conversations.add(id);
		return id;
	}

	/**
	 * Register a conversation with a given ID.
	 */
	public void registerConversation(String convId) {
		if (convId != null) {
			conversations.add(convId);
		}
	}

	/**
	 * Deregister a conversation with a given ID.
	 */
	public void deregisterConversation(String convId) {
		if (convId != null) {
			conversations.remove(convId);
		}
	}

	/**
	 * Deregister all conversations.
	 */
	public void clear() {
		conversations.clear();
	}

	/**
	 * Return a template that matches only messages that do not belong to any of the
	 * conversations in this list.
	 */
	public MessageTemplate getMessageTemplate() {
		return myTemplate;
	}

	@Override
	public String toString() {
		return "CL" + conversations;
	}

	protected String createConversationId() {
		return myAgent.getName().toString() + (cnt++);
	}
}

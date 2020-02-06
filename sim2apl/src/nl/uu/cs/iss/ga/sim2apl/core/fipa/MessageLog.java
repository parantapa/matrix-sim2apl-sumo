package nl.uu.cs.iss.ga.sim2apl.core.fipa;

import java.time.ZonedDateTime;
import java.util.UUID;

import nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.Performative;
import nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.ACLMessage;

public class MessageLog implements Comparable<MessageLog> {

	private ZonedDateTime time = ZonedDateTime.now();
	private UUID id;
	private nl.uu.cs.iss.ga.sim2apl.core.fipa.MessageInterface message;
	boolean received;

	@Override
	public int compareTo(MessageLog o) {
		if (this.equals(o)) {
			return 0;
		} else {
			int comp = ((Long) this.getTime().toInstant().toEpochMilli())
					.compareTo(o.getTime().toInstant().toEpochMilli());
			if (comp == 0) {
				return this.id.compareTo(o.id); // 1;
			} else {
				return comp;
			}
		}
	}
	
	public UUID getID() {
		return id;
	}

	public ZonedDateTime getTime() {
		return time;
	}

	public nl.uu.cs.iss.ga.sim2apl.core.fipa.MessageInterface getMessage() {
		return this.message;
	}

	public boolean isReceived() {
		return received;
	}

	public void setReceived(boolean received) {
		this.received = received;
	}

	public MessageLog(MessageInterface message, boolean received) {
		this.message = message;
		this.id = UUID.fromString(message.getUserDefinedParameter("X-messageID"));
		this.time = ZonedDateTime.now();
		this.received = received;
	}

	public MessageLog() {
		this.message = new ACLMessage(Performative.UNKNOWN);
		this.time = ZonedDateTime.now();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().equals(this.getClass()) && this.getMessage() != null && this.getMessage().equals(obj)
				&& this.getTime().equals(((MessageLog) obj).getTime())) {
			return true;
		} else {
			return false;
		}
	}

}

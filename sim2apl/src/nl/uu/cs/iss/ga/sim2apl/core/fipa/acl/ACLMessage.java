/*****************************************************************
 JADE - Java Agent DEvelopment Framework is a framework to develop 
 multi-agent systems in compliance with the FIPA specifications.
 Copyright (C) 2000 CSELT S.p.A. 
 
 GNU Lesser General Public License
 
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation, 
 version 2.1 of the License. 
 
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the
 Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 Boston, MA  02111-1307, USA.
 *****************************************************************/

package nl.uu.cs.iss.ga.sim2apl.core.fipa.acl;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import java.util.Date;
import java.util.HashSet;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import nl.uu.cs.iss.ga.sim2apl.core.fipa.MessageInterface;
import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID;
import nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Envelope;
import nl.uu.cs.iss.ga.sim2apl.core.logging.Loggable;
import nl.uu.cs.iss.ga.sim2apl.core.platform.Platform;

/**
 * The class ACLMessage implements an ACL message compliant to the <b>FIPA
 * 2000</b> "FIPA ACL Message Structure Specification" (fipa000061)
 * specifications. All parameters are couples <em>keyword: value</em>. All
 * keywords are <code>private final String</code>. All values can be set by
 * using the methods <em>set</em> and can be read by using the methods
 * <em>get</em>.
 * <p>
 * <b>Warning: </b> since JADE 3.1 an exception might be thrown during the
 * serialization of the ACLMessage parameters (with exception of the content of
 * the ACLMessage) because of a limitation to 65535 in the total number of bytes
 * needed to represent all the characters of a String (see also
 * java.io.DataOutput#writeUTF(String)).
 * <p>
 * The methods <code> setByteSequenceContent() </code> and
 * <code> getByteSequenceContent() </code> allow to send arbitrary sequence of
 * bytes over the content of an ACLMessage.
 * <p>
 * The couple of methods <code> setContentObject() </code> and
 * <code> getContentObject() </code> allow to send serialized Java objects over
 * the content of an ACLMessage. These method are not strictly FIPA compliant so
 * their usage is not encouraged.
 * 
 * @author Fabio Bellifemine - CSELT
 * @version $Date: 2016-07-21 13:44:10 +0200 (Thu, 21 Jul 2016) $ $Revision:
 *          6804 $
 * @see <a href=http://www.fipa.org/specs/fipa00061/XC00061D.html>FIPA Spec</a>
 */
// #MIDP_EXCLUDE_BEGIN
public class ACLMessage implements Cloneable, Serializable, MessageInterface {
	
	private static final Loggable logger = Platform.getLogger();
	
	// #MIDP_EXCLUDE_END
	/*
	 * #MIDP_INCLUDE_BEGIN public class ACLMessage implements Serializable {
	 * #MIDP_INCLUDE_END
	 */
	// Explicitly set for compatibility between standard and micro version
	private static final long serialVersionUID = 3945353187608998130L;

	/**
	 * User defined parameter key specifying, when set to "true", that if the
	 * delivery of a message fails, no failure handling action must be performed.
	 */
	public static final String IGNORE_FAILURE = "JADE-ignore-failure";

	/**
	 * User defined parameter key specifying, when set to "true", that if the
	 * delivery of a message fails, no FAILURE notification has to be sent back to
	 * the sender. This differs from IGNORE_FAILURE since it does not inhibit the
	 * delivery failure handling mechanism (based on the NOTIFY_FAILURE VCommand) at
	 * all, but just the delivery of the automatic AMS FAILURE reply.
	 */
	public static final String DONT_NOTIFY_FAILURE = "JADE-dont-notify-failure";

	/**
	 * User defined parameter key specifying that the JADE tracing mechanism should
	 * be activated for this message.
	 */
	public static final String TRACE = "JADE-trace";

	/**
	 * User defined parameter key specifying that this message does not need to be
	 * cloned by the message delivery service. This should be used ONLY when the
	 * message object will not be modified after being sent
	 */
	public static final String NO_CLONE = "JADE-no-clone";

	/**
	 * User defined parameter key specifying that this message must be delivered
	 * synchronously. It should be noticed that when using synchronous delivery
	 * message order is not guaranteed.
	 */
	public static final String SYNCH_DELIVERY = "JADE-synch-delivery";

	/**
	 * User defined parameter key specifying the AID of the real sender of a
	 * message. This is automatically set by the MessagingService when posting a
	 * message where the sender field is different than the real sender.
	 */
	public static final String REAL_SENDER = "JADE-real-sender";

	/**
	 * User defined parameter key specifying that this message must be stored for a
	 * given timeout (in ms) in case it is sent to/from a temporarily disconnected
	 * split container. After that timeout a FAILURE message will be sent back to
	 * the sender.<br>
	 * 0 means store and forward disabled -1 means infinite timeout
	 */
	public static final String SF_TIMEOUT = "JADE-SF-timeout";

	/**
	 * AMS failure reasons
	 */
	public static final String AMS_FAILURE_AGENT_NOT_FOUND = "Agent not found";
	public static final String AMS_FAILURE_AGENT_UNREACHABLE = "Agent unreachable";
	public static final String AMS_FAILURE_SERVICE_ERROR = "Service error";
	public static final String AMS_FAILURE_UNAUTHORIZED = "Not authorized";
	public static final String AMS_FAILURE_FOREIGN_AGENT_UNREACHABLE = "Foreign agent unreachable";
	public static final String AMS_FAILURE_FOREIGN_AGENT_NO_ADDRESS = "Foreign agent with no address";
	public static final String AMS_FAILURE_UNEXPECTED_ERROR = "Unexpected error";

	/**
	 * @serial
	 */
	private int performativeIndex;
	
	/**
	 * @serial
	 */
	private nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID source = null;

	/**
	 * These constants represent the expected size of the 2 array lists used by this
	 * class
	 **/
	private static final int RECEIVERS_EXPECTED_SIZE = 1;
	private static final int REPLYTO_EXPECTED_SIZE = 1;

	// #MIDP_EXCLUDE_BEGIN
	private Set<nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID> dests = new HashSet<nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID>(RECEIVERS_EXPECTED_SIZE);
	private Set<nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID> reply_to = null;
	// #MIDP_EXCLUDE_END
	/*
	 * #MIDP_INCLUDE_BEGIN private Vector dests = new
	 * Vector(RECEIVERS_EXPECTED_SIZE); private Vector reply_to = null;
	 * #MIDP_INCLUDE_END
	 */

	/**
	 * @serial
	 */
	// At a given time or content or byteSequenceContent are != null,
	// it is not allowed that both are != null
	private StringBuffer content = null;
	private byte[] byteSequenceContent = null;

	/**
	 * @serial
	 */
	private String reply_with = null;

	/**
	 * @serial
	 */
	private String in_reply_to = null;

	/**
	 * @serial
	 */
	private String encoding = null;

	/**
	 * @serial
	 */
	private String language = null;

	/**
	 * @serial
	 */
	private String ontology = null;

	/**
	 * @serial
	 */
	private long reply_byInMillisec = 0;

	/**
	 * @serial
	 */
	private String protocol = null;

	/**
	 * @serial
	 */
	private String conversation_id = null;

	private Properties userDefProps = null;

	private long postTimeStamp = -1;

	// #CUSTOM_EXCLUDE_BEGIN
	private nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Envelope messageEnvelope;
	
	public byte[] encode()
	{
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			
			if (messageEnvelope != null) {
				messageEnvelope.encode(dos);
			} else {
				(new nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Envelope()).encode(dos);
			}
			LEAPACLCodec.serializeACL(this, dos);
			
			return baos.toByteArray();
		} catch (IOException ex) {
			// TODO err handling
			logger.log(getClass(), ex);
		}
		return new byte[0];
	}
    
	public static ACLMessage decode(byte[] data)
	{
		try {
			DataInputStream din = new DataInputStream(new ByteArrayInputStream(data));
			nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Envelope envelope = nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Envelope.decode(din); //(Envelope) ois.readObject();
			ACLMessage result = LEAPACLCodec.deserializeACL(din);
						
			result.messageEnvelope = envelope;
			return result;
		} catch (IOException | URISyntaxException ex) { //| ClassNotFoundException ex) {
			// TODO err handling
            logger.log(ACLMessage.class, ex);
		}
		
		return new ACLMessage(nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.Performative.UNKNOWN);
	}
	// #CUSTOM_EXCLUDE_END

	/**
	 * Returns the list of the communicative acts as an array of
	 * <code>String</code>.
	 */
	public static String[] getAllPerformativeNames() {
		return nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.Performative.names();
	}

	/**
	 * deprecated as 'public': Since every ACL Message must have a message type, you should use
	 *             the new constructor which gets a message type as a parameter. To
	 *             avoid problems, now this constructor silently sets the message
	 *             type to <code>not-understood</code>.
	 * @see jade.lang.acl.ACLMessage#ACLMessage(int)
	 */
	protected ACLMessage() { // Used by persistence service: do not remove it, but make it private
		this(nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.Performative.UNKNOWN);
	}
	
	/**
	 * Note the explicit way to construct an empty; this doesn't result in an usable message!
	 * For use with decode-method/persistence-service only! 
	 */
	public static ACLMessage getEmpty()
	{
		return new ACLMessage();
	}
	
	public ACLMessage(int perfIndex) {
		this(nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.Performative.values()[perfIndex]);
	}

	/**
	 * This constructor creates an ACL message object with the specified
	 * performative. If the passed integer does not correspond to any of the known
	 * performatives, it silently initializes the message to
	 * <code>not-understood</code>.
	 **/
	public ACLMessage(nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.Performative perf) {
		setPerformative(perf);
		setConversationId(UUID.randomUUID().toString());
	}

	/**
	 * Writes the <code>:sender</code> slot. <em><b>Warning:</b> no checks are made
	 * to validate the slot value.</em>
	 * 
	 * @param source
	 *            The new value for the slot.
	 * @see jade.lang.acl.ACLMessage#getSender()
	 */
	public void setSender(nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID s) {
		source = s;
	}

	/**
	 * Adds a value to <code>:receiver</code> slot. <em><b>Warning:</b> no checks
	 * are made to validate the slot value.</em>
	 * 
	 * @param r
	 *            The value to add to the slot value set.
	 */
	public void addReceiver(nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID r) {
		if (r != null) {
			// #MIDP_EXCLUDE_BEGIN
			dests.add(r);
			// #MIDP_EXCLUDE_END
			/*
			 * #MIDP_INCLUDE_BEGIN dests.addElement(r); #MIDP_INCLUDE_END
			 */
		}
	}

	/**
	 * Removes a value from <code>:receiver</code> slot. <em><b>Warning:</b> no
	 * checks are made to validate the slot value.</em>
	 * 
	 * @param r
	 *            The value to remove from the slot value set.
	 * @return true if the AID has been found and removed, false otherwise
	 */
	public boolean removeReceiver(nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID r) {
		if (r != null) {
			// #MIDP_EXCLUDE_BEGIN
			return dests.remove(r);
			// #MIDP_EXCLUDE_END
			/*
			 * #MIDP_INCLUDE_BEGIN return dests.removeElement(r); #MIDP_INCLUDE_END
			 */
		} else {
			return false;
		}
	}

	/**
	 * Removes all values from <code>:receiver</code> slot. <em><b>Warning:</b> no
	 * checks are made to validate the slot value.</em>
	 */
	public void clearAllReceiver() {
		// #MIDP_EXCLUDE_BEGIN
		dests.clear();
		// #MIDP_EXCLUDE_END
		/*
		 * #MIDP_INCLUDE_BEGIN dests.removeAllElements(); #MIDP_INCLUDE_END
		 */
	}

	/**
	 * Adds a value to <code>:reply-to</code> slot. <em><b>Warning:</b> no checks
	 * are made to validate the slot value.</em>
	 * 
	 * @param dest
	 *            The value to add to the slot value set.
	 */
	public void addReplyTo(nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID dest) {
		if (dest != null) {
			// #MIDP_EXCLUDE_BEGIN
			reply_to = (reply_to == null ? new HashSet<nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID>(REPLYTO_EXPECTED_SIZE) : reply_to);
			reply_to.add(dest);
			// #MIDP_EXCLUDE_END
			/*
			 * #MIDP_INCLUDE_BEGIN reply_to = (reply_to == null ? new
			 * Vector(REPLYTO_EXPECTED_SIZE) : reply_to); reply_to.addElement(dest);
			 * #MIDP_INCLUDE_END
			 */
		}
	}

	/**
	 * Removes a value from <code>:reply_to</code> slot. <em><b>Warning:</b> no
	 * checks are made to validate the slot value.</em>
	 * 
	 * @param dest
	 *            The value to remove from the slot value set.
	 * @return true if the AID has been found and removed, false otherwise
	 */
	public boolean removeReplyTo(nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID dest) {
		if ((dest != null) && (reply_to != null)) {
			// #MIDP_EXCLUDE_BEGIN
			return reply_to.remove(dest);
			// #MIDP_EXCLUDE_END
			/*
			 * #MIDP_INCLUDE_BEGIN return reply_to.removeElement(dest); #MIDP_INCLUDE_END
			 */
		} else {
			return false;
		}
	}

	/**
	 * Removes all values from <code>:reply_to</code> slot. <em><b>Warning:</b> no
	 * checks are made to validate the slot value.</em>
	 */
	public void clearAllReplyTo() {
		if (reply_to != null) {
			// #MIDP_EXCLUDE_BEGIN
			reply_to.clear();
			// #MIDP_EXCLUDE_END
			/*
			 * #MIDP_INCLUDE_BEGIN reply_to.removeAllElements(); #MIDP_INCLUDE_END
			 */
		}
	}
	
	/**
	 * set the performative of this ACL message object to the passed constant.
	 * Remind to use the set of constants (i.e. <code> INFORM, REQUEST, ... </code>)
	 * defined in this class
	 */
	//public void setPerformative(int perfIndex) {
	//	performative = perfIndex;
	//}
	
	/**
	 * set the performative of this ACL message object to the passed constant.
	 * Remind to use the set of constants (i.e. <code> INFORM, REQUEST, ... </code>)
	 * defined in this class
	 */
	public void setPerformative(nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.Performative perf) {
		performativeIndex = perf.index();
	}
	
	public void setPerformativeIndex(int perfIndex) {
		performativeIndex = perfIndex;
	}

	/**
	 * Writes the <code>:content</code> slot. <em><b>Warning:</b> no checks are made
	 * to validate the slot value.</em>
	 * <p>
	 * <p>
	 * Notice that, in general, setting a String content and getting back a byte
	 * sequence content - or viceversa - does not return the same value, i.e. the
	 * following relation does not hold <code>
	 * getByteSequenceContent(setByteSequenceContent(getContent().getBytes())) 
	 * is equal to getByteSequenceContent()
	 * </code>
	 * 
	 * @param content
	 *            The new value for the slot.
	 * @see jade.lang.acl.ACLMessage#getContent()
	 * @see jade.lang.acl.ACLMessage#setByteSequenceContent(byte[])
	 * @see jade.lang.acl.ACLMessage#setContentObject(Serializable s)
	 */
	public void setContent(String content) {
		byteSequenceContent = null;
		if (content != null) {
			this.content = new StringBuffer(content);
		} else {
			this.content = null;
		}
	}

	/**
	 * Writes the <code>:content</code> slot. <em><b>Warning:</b> no checks are made
	 * to validate the slot value.</em>
	 * <p>
	 * <p>
	 * Notice that, in general, setting a String content and getting back a byte
	 * sequence content - or viceversa - does not return the same value, i.e. the
	 * following relation does not hold <code>
	 * getByteSequenceContent(setByteSequenceContent(getContent().getBytes())) 
	 * is equal to getByteSequenceContent()
	 * </code>
	 * 
	 * @param byteSequenceContent
	 *            The new value for the slot.
	 * @see jade.lang.acl.ACLMessage#setContent(String s)
	 * @see jade.lang.acl.ACLMessage#getByteSequenceContent()
	 * @see jade.lang.acl.ACLMessage#setContentObject(Serializable s)
	 */
	public void setByteSequenceContent(byte[] byteSequenceContent) {
		content = null;
		this.byteSequenceContent = byteSequenceContent;
	}

	// #MIDP_EXCLUDE_BEGIN
	/**
	 * This method sets the content of this ACLMessage to a Java object. It is not
	 * FIPA compliant so its usage is not encouraged. For example:<br>
	 * 
	 * <PRE>
	 * ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
	 * Date d = new Date();
	 * try {
	 * 	msg.setContentObject(d);
	 * } catch (IOException e) {
	 * }
	 * </PRE>
	 *
	 * @param s
	 *            the object that will be used to set the content of the ACLMessage.
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	public void setContentObject(nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.FIPASendableObject s) throws IOException {
		
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
				ObjectOutputStream oos = new ObjectOutputStream(bos)) {
			oos.writeObject(s);
			oos.flush();
			setByteSequenceContent(bos.toByteArray());
		}
	}
	
	
	public void setContentStream(ByteArrayOutputStream c) throws IOException {
		setByteSequenceContent(c.toByteArray());
	}

	/**
	 * This method returns the content of this ACLMessage when they have been
	 * written via the method <code>setContentObject</code>. It is not FIPA
	 * compliant so its usage is not encouraged. For example to read Java objects
	 * from the content
	 * 
	 * <PRE>
	 * ACLMessage msg = blockingReceive();
	 * try {
	 * 	Date d = (Date) msg.getContentObject();
	 * } catch (UnreadableException e) {
	 * }
	 * </PRE>
	 * 
	 * @return the object read from the content of this ACLMessage
	 * @exception nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.UnreadableException
	 *                when an error occurs during the decoding.
	 */
	public nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.FIPASendableObject getContentObject() throws nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.UnreadableException {
		
		byte[] data = getByteSequenceContent();
		if (data == null)
			return null;
		
		try(ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(data))) {
			return (FIPASendableObject) oin.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new UnreadableException(e.getMessage());
		} 

	}
	// #MIDP_EXCLUDE_END

	/**
	 * Writes the <code>:reply-with</code> slot. <em><b>Warning:</b> no checks are
	 * made to validate the slot value.</em>
	 * 
	 * @param reply
	 *            The new value for the slot.
	 * @see jade.lang.acl.ACLMessage#getReplyWith()
	 */
	public void setReplyWith(String reply) {
		reply_with = reply;
	}

	/**
	 * Writes the <code>:in-reply-to</code> slot. <em><b>Warning:</b> no checks are
	 * made to validate the slot value.</em>
	 * 
	 * @param reply
	 *            The new value for the slot.
	 * @see jade.lang.acl.ACLMessage#getInReplyTo()
	 */
	public void setInReplyTo(String reply) {
		in_reply_to = reply;
	}

	/**
	 * Writes the <code>:encoding</code> slot. <em><b>Warning:</b> no checks are
	 * made to validate the slot value.</em>
	 * 
	 * @param str
	 *            The new value for the slot.
	 * @see jade.lang.acl.ACLMessage#getEncoding()
	 */
	public void setEncoding(String str) {
		encoding = str;
	}

	/**
	 * Writes the <code>:language</code> slot. <em><b>Warning:</b> no checks are
	 * made to validate the slot value.</em>
	 * 
	 * @param str
	 *            The new value for the slot.
	 * @see jade.lang.acl.ACLMessage#getLanguage()
	 */
	public void setLanguage(String str) {
		language = str;
	}

	/**
	 * Writes the <code>:ontology</code> slot. <em><b>Warning:</b> no checks are
	 * made to validate the slot value.</em>
	 * 
	 * @param str
	 *            The new value for the slot.
	 * @see jade.lang.acl.ACLMessage#getOntology()
	 */
	public void setOntology(String str) {
		ontology = str;
	}

	/**
	 * Writes the <code>:reply-by</code> slot. <em><b>Warning:</b> no checks are
	 * made to validate the slot value.</em>
	 * 
	 * @param date
	 *            The new value for the slot.
	 * @see jade.lang.acl.ACLMessage#getReplyByDate()
	 */
	public void setReplyByDate(Date date) {
		reply_byInMillisec = (date == null ? 0 : date.getTime());
	}

	/**
	 * Writes the <code>:protocol</code> slot. <em><b>Warning:</b> no checks are
	 * made to validate the slot value.</em>
	 * 
	 * @param str
	 *            The new value for the slot.
	 * @see jade.lang.acl.ACLMessage#getProtocol()
	 */
	public void setProtocol(String str) {
		protocol = str;
	}

	/**
	 * Writes the <code>:conversation-id</code> slot. <em><b>Warning:</b> no checks
	 * are made to validate the slot value.</em>
	 * 
	 * @param str
	 *            The new value for the slot.
	 * @see jade.lang.acl.ACLMessage#getConversationId()
	 */
	public void setConversationId(String str) {
		conversation_id = str;
	}

	/**
	 * Reads <code>:receiver</code> slot.
	 * 
	 * @return An <code>Iterator</code> containing the Agent IDs of the receiver
	 *         agents for this message.
	 */
	public Iterator<nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID> getAllReceiver() {
		// #MIDP_EXCLUDE_BEGIN
		return dests.iterator();
		// #MIDP_EXCLUDE_END
		/*
		 * #MIDP_INCLUDE_BEGIN return new EnumIterator(dests.elements());
		 * #MIDP_INCLUDE_END
		 */
	}

	/**
	 * Reads <code>:reply_to</code> slot.
	 * 
	 * @return An <code>Iterator</code> containing the Agent IDs of the reply_to
	 *         agents for this message.
	 */
	public Iterator<nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID> getAllReplyTo() {
		if (reply_to == null) {
			return Collections.emptyIterator();
		} else {
			// #MIDP_EXCLUDE_BEGIN
			return reply_to.iterator();
			// #MIDP_EXCLUDE_END
			/*
			 * #MIDP_INCLUDE_BEGIN return new EnumIterator(reply_to.elements());
			 * #MIDP_INCLUDE_END
			 */
		}
	}

	/**
	 * Reads <code>:sender</code> slot.
	 * 
	 * @return The value of <code>:sender</code>slot.
	 * @see jade.lang.acl.ACLMessage#setSender(AID).
	 */
	@Override
	public nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID getSender() {
		if (source != null) {
			return source;
		} else if (this.getEnvelope() != null) {
			return this.getEnvelope().getFrom();
		} else {
			return null;
		}
	}


	/**
	 * return the enum object representing the performative of this object
	 * 
	 * @return an enum object representing the performative of this object
	 */
	public nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.Performative getPerformative() {
		return nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.Performative.values()[performativeIndex];
	}
	
	/**
	 * return the integer representing the performative of this object
	 * 
	 * @return an integer representing the performative of this object
	 */
	public int getPerformativeIndex() {
		return performativeIndex;
	}

	/**
	 * This method allows to check if the content of this ACLMessage is a
	 * byteSequence or a String
	 * 
	 * @return true if it is a byteSequence, false if it is a String
	 */
	public boolean hasByteSequenceContent() {
		return (byteSequenceContent != null);
	}

	/**
	 * Reads <code>:content</code> slot.
	 * <p>
	 * <p>
	 * Notice that, in general, setting a String content and getting back a byte
	 * sequence content - or viceversa - does not return the same value, i.e. the
	 * following relation does not hold <code>
	 * getByteSequenceContent(setByteSequenceContent(getContent().getBytes())) 
	 * is equal to getByteSequenceContent()
	 * </code>
	 * 
	 * @return The value of <code>:content</code> slot.
	 * @see jade.lang.acl.ACLMessage#setContent(String)
	 * @see jade.lang.acl.ACLMessage#getByteSequenceContent()
	 * @see jade.lang.acl.ACLMessage#getContentObject()
	 */
	public String getContent() {
		if (content != null)
			return new String(content);
		else if (byteSequenceContent != null)
			return new String(byteSequenceContent);
		return null;
	}

	/**
	 * Reads <code>:content</code> slot.
	 * <p>
	 * <p>
	 * Notice that, in general, setting a String content and getting back a byte
	 * sequence content - or viceversa - does not return the same value, i.e. the
	 * following relation does not hold <code>
	 * getByteSequenceContent(setByteSequenceContent(getContent().getBytes())) 
	 * is equal to getByteSequenceContent()
	 * </code>
	 * 
	 * @return The value of <code>:content</code> slot.
	 * @see jade.lang.acl.ACLMessage#getContent()
	 * @see jade.lang.acl.ACLMessage#setByteSequenceContent(byte[])
	 * @see jade.lang.acl.ACLMessage#getContentObject()
	 */
	public byte[] getByteSequenceContent() {
		if (content != null)
			return content.toString().getBytes();
		else if (byteSequenceContent != null)
			return byteSequenceContent;
		return null;
	}

	/**
	 * Reads <code>:reply-with</code> slot.
	 * 
	 * @return The value of <code>:reply-with</code>slot.
	 * @see jade.lang.acl.ACLMessage#setReplyWith(String).
	 */
	public String getReplyWith() {
		return reply_with;
	}

	/**
	 * Reads <code>:reply-to</code> slot.
	 * 
	 * @return The value of <code>:reply-to</code>slot.
	 * @see jade.lang.acl.ACLMessage#setInReplyTo(String).
	 */
	public String getInReplyTo() {
		return in_reply_to;
	}

	/**
	 * Reads <code>:encoding</code> slot.
	 * 
	 * @return The value of <code>:encoding</code>slot.
	 * @see jade.lang.acl.ACLMessage#setEncoding(String).
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * Reads <code>:language</code> slot.
	 * 
	 * @return The value of <code>:language</code>slot.
	 * @see jade.lang.acl.ACLMessage#setLanguage(String).
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Reads <code>:ontology</code> slot.
	 * 
	 * @return The value of <code>:ontology</code>slot.
	 * @see jade.lang.acl.ACLMessage#setOntology(String).
	 */
	public String getOntology() {
		return ontology;
	}

	// #MIDP_EXCLUDE_BEGIN
	/**
	 * Reads <code>:reply-by</code> slot.
	 * 
	 * @return The value of <code>:reply-by</code>slot, as a string.
	 * @see jade.lang.acl.ACLMessage#getReplyByDate().
	 * @deprecated Since the value of this slot is a Date by definition, then the
	 *             <code>getReplyByDate</code> should be used that returns a Date
	 */
	@Deprecated
	public String getReplyBy() {
		if (reply_byInMillisec != 0)
			return ISO8601.toString(new Date(reply_byInMillisec));
		else
			return null;
	}
	// #MIDP_EXCLUDE_END

	/**
	 * Reads <code>:reply-by</code> slot.
	 * 
	 * @return The value of <code>:reply-by</code>slot, as a <code>Date</code>
	 *         object.
	 * @see jade.lang.acl.ACLMessage#setReplyByDate(Date).
	 */
	public Date getReplyByDate() {
		if (reply_byInMillisec != 0)
			return new Date(reply_byInMillisec);
		else
			return null;
	}

	/**
	 * Reads <code>:protocol</code> slot.
	 * 
	 * @return The value of <code>:protocol</code>slot.
	 * @see jade.lang.acl.ACLMessage#setProtocol(String).
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * Reads <code>:conversation-id</code> slot.
	 * 
	 * @return The value of <code>:conversation-id</code>slot.
	 * @see jade.lang.acl.ACLMessage#setConversationId(String).
	 */
	public String getConversationId() {
		return conversation_id;
	}

	/**
	 * Add a new user defined parameter to this ACLMessage. Notice that according to
	 * the FIPA specifications, the keyword of a user-defined parameter must not
	 * contain space inside. Note that the user does not need to (and shall not) add
	 * the prefix "X-" to the keyword. This is automatically added by the
	 * StringACLCodec.
	 * 
	 * @param key
	 *            the property key.
	 * @param value
	 *            the property value
	 */
	@Override
	public void addUserDefinedParameter(String key, String value) {
		userDefProps = (userDefProps == null ? new Properties() : userDefProps);
		userDefProps.setProperty(key, value);
	}

	/**
	 * Searches for the user defined parameter with the specified key. The method
	 * returns <code>null</code> if the parameter is not found.
	 *
	 * @param key
	 *            the parameter key.
	 * @return the value in this ACLMessage with the specified key value.
	 */
	@Override
	public String getUserDefinedParameter(String key) {
		if (userDefProps == null)
			return null;
		else
			return userDefProps.getProperty(key);
	}

	/**
	 * Return all user defined parameters of this ACLMessage in form of a Properties
	 * object
	 **/
	public Properties getAllUserDefinedParameters() {
		userDefProps = (userDefProps == null ? new Properties() : userDefProps);
		return userDefProps;
	}

	/**
	 * Replace all user defined parameters of this ACLMessage with the specified
	 * Properties object.
	 **/
	public void setAllUserDefinedParameters(Properties userDefProps) {
		this.userDefProps = userDefProps;
	}

	/**
	 * Removes the key and its corresponding value from the list of user defined
	 * parameters in this ACLMessage.
	 * 
	 * @param key
	 *            the key that needs to be removed
	 * @return true if the property has been found and removed, false otherwise
	 */
	public boolean removeUserDefinedParameter(String key) {
		return (clearUserDefinedParameter(key) != null);
	}

	/**
	 * Removes the key and its corresponding value from the list of user defined
	 * parameters in this ACLMessage.
	 * 
	 * @param key
	 *            the key that needs to be removed
	 * @return the value to which the key had been mapped or null if the key was not
	 *         present
	 */
	public Object clearUserDefinedParameter(String key) {
		if (userDefProps == null)
			return null;
		else
			return userDefProps.remove(key);
	}

	public void setPostTimeStamp(long time) {
		postTimeStamp = time;
	}

	public long getPostTimeStamp() {
		return postTimeStamp;
	}

	// #CUSTOM_EXCLUDE_BEGIN
	/**
	 * Attaches an envelope to this message. The envelope is used by the
	 * <b><it>ACC</it></b> for inter-platform messaging.
	 * 
	 * @param e
	 *            The <code>Envelope</code> object to attach to this message.
	 * @see jade.lang.acl#getEnvelope()
	 * @see jade.lang.acl#setDefaultEnvelope()
	 */
	public void setEnvelope(nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Envelope e) {
		messageEnvelope = e;
	}

	/**
	 * Writes the message envelope for this message, using the <code>:sender</code>
	 * and <code>:receiver</code> message slots to fill in the envelope.
	 * 
	 * @see jade.lang.acl#setEnvelope(nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Envelope e)
	 * @see jade.lang.acl#getEnvelope()
	 */
	public void setDefaultEnvelope() {
		messageEnvelope = new nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Envelope();
		messageEnvelope.setFrom(source);
		// #MIDP_EXCLUDE_BEGIN
		Iterator<nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID> it = dests.iterator();
		// #MIDP_EXCLUDE_END
		/*
		 * #MIDP_INCLUDE_BEGIN Iterator it = new EnumIterator(dests.elements());
		 * #MIDP_INCLUDE_END
		 */
		while (it.hasNext()) {
			nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID aid = it.next();
			messageEnvelope.addTo(aid);
			messageEnvelope.addIntendedReceiver(aid);
		}
		// #MIDP_EXCLUDE_BEGIN
		messageEnvelope.setAclRepresentation(nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.StringACLCodec.NAME);
		// #MIDP_EXCLUDE_END
		messageEnvelope.setDate(new Date());
	}

	/**
	 * Reads the envelope attached to this message, if any.
	 * 
	 * @return The envelope for this message.
	 * @see jade.lang.acl#setEnvelope(nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Envelope e)
	 * @see jade.lang.acl#setDefaultEnvelope()
	 */
	public nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Envelope getEnvelope() {
		return messageEnvelope;
	}
	// #CUSTOM_EXCLUDE_END

	// #MIDP_EXCLUDE_BEGIN

	/**
	 * Convert an ACL message to its string representation. This method writes a
	 * representation of this <code>ACLMessage</code> into a character string. If
	 * the content is a bytesequence, then it is automatically converted into Base64
	 * encoding.
	 * 
	 * @return A <code>String</code> representing this message.
	 */
	@Override
	public String toString() {
		return StringACLCodec.toString(this);
	}
	// #MIDP_EXCLUDE_END

	/**
	 * Clone an <code>ACLMessage</code> object.
	 * 
	 * @return A copy of this <code>ACLMessage</code> object. The copy must be
	 *         casted back to <code>ACLMessage</code> type before being used.
	 */
	// #MIDP_EXCLUDE_BEGIN
	@Override
	public synchronized Object clone() {

		ACLMessage result;

		try {
			result = (ACLMessage) super.clone();
			result.persistentID = null;
			if (source != null) {
				result.source = source.clone();
			}

			// Deep clone receivers
			if (dests != null) {
				result.dests = new HashSet<nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID>(dests.size());
				Iterator<nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID> it = dests.iterator();
				while (it.hasNext()) {
					nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID id = it.next();
					result.dests.add(id.clone());
				}
			}

			// Deep clone reply_to
			if (reply_to != null) {
				result.reply_to = new HashSet<nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID>(reply_to.size());
				Iterator<nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID> it = reply_to.iterator();
				while (it.hasNext()) {
					nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID id = it.next();
					result.reply_to.add(id.clone());
				}
			}

			// Deep clone user-def-properties if present
			if (userDefProps != null)
				result.userDefProps = (Properties) userDefProps.clone();
			// Deep clone envelope if present
			if (messageEnvelope != null)
				result.messageEnvelope = (nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Envelope) messageEnvelope.clone();
		} catch (CloneNotSupportedException cnse) {
			throw new InternalError(); // This should never happen
		}

		return result;
	}
	// #MIDP_EXCLUDE_END
	/*
	 * #MIDP_INCLUDE_BEGIN public synchronized Object clone() { ACLMessage result =
	 * new ACLMessage(NOT_UNDERSTOOD); result.performative = performative;
	 * result.source = source; result.content = content; result.byteSequenceContent
	 * = byteSequenceContent; result.reply_with = reply_with; result.in_reply_to =
	 * in_reply_to; result.encoding = encoding; result.language = language;
	 * result.ontology = ontology; result.reply_byInMillisec = reply_byInMillisec;
	 * result.protocol = protocol; result.conversation_id = conversation_id;
	 * result.userDefProps = userDefProps; //#CUSTOM_EXCLUDE_BEGIN
	 * if(messageEnvelope != null) { result.messageEnvelope =
	 * (Envelope)messageEnvelope.clone(); } //#CUSTOM_EXCLUDE_END result.dests = new
	 * Vector(dests.size()); for (int i=0; i<dests.size(); i++)
	 * result.dests.addElement(dests.elementAt(i)); if (reply_to != null) {
	 * result.reply_to = new Vector(reply_to.size()); for (int i=0;
	 * i<reply_to.size(); i++) result.reply_to.addElement(reply_to.elementAt(i)); }
	 * return result; } #MIDP_INCLUDE_END
	 */

	/**
	 * Normal clone() method actually perform a deep-clone of the ACLMessage object.
	 * This method instead clones the ACLMessage object itself but not the objects
	 * pointed to by the ACLMessage fields.
	 * 
	 * @return A new ACLMessage whose fields points to the same object as the
	 *         original ACLMessage object
	 */
	public ACLMessage shallowClone() {
		ACLMessage result = new ACLMessage(getPerformative());
		result.source = source;
		result.dests = dests;
		result.reply_to = reply_to;

		result.content = content;
		result.byteSequenceContent = byteSequenceContent;

		result.encoding = encoding;
		result.language = language;
		result.ontology = ontology;

		result.reply_byInMillisec = reply_byInMillisec;
		result.reply_with = reply_with;
		result.in_reply_to = in_reply_to;
		result.protocol = protocol;
		result.conversation_id = conversation_id;

		result.userDefProps = userDefProps;

		result.messageEnvelope = messageEnvelope;

		return result;
	}

	/**
	 * Resets all the message slots.
	 */
	public void reset() {
		source = null;
		// #MIDP_EXCLUDE_BEGIN
		dests.clear();
		if (reply_to != null)
			reply_to.clear();
		// #MIDP_EXCLUDE_END
		/*
		 * #MIDP_INCLUDE_BEGIN dests.removeAllElements(); if (reply_to != null)
		 * reply_to.removeAllElements(); #MIDP_INCLUDE_END
		 */
		performativeIndex = nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.Performative.NOT_UNDERSTOOD.index();
		content = null;
		byteSequenceContent = null;
		reply_with = null;
		in_reply_to = null;
		encoding = null;
		language = null;
		ontology = null;
		reply_byInMillisec = 0;
		protocol = null;
		conversation_id = null;
		if (userDefProps != null) {
			userDefProps.clear();
		}

		postTimeStamp = -1;
	}

	/**
	 * <p>
	 * Create a new ACLMessage that is a reply to this message.
	 * </p>
	 * <p>
	 * This method sets the following parameters of the new message: performative,
	 * receiver, language, ontology, protocol, conversation-id, in-reply-to,
	 * reply-with.
	 * </p>
	 * <p>
	 * The programmer needs to set the communicative-act and the content. Of course,
	 * if he wishes to do that, he can reset any of the fields.
	 * </p>
	 * 
	 * @return the ACLMessage to send as a reply
	 */
	public ACLMessage createReply(nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID sender) {
		return createReply(sender, getPerformative());
	}
	
	public ACLMessage createForward(nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.Performative perf, nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID sender, nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID receiver) {
		ACLMessage m = createReply(sender, perf);
		m.clearAllReceiver();
		m.clearAllReplyTo();
		m.addReceiver(receiver);
		m.setSender(sender);
		
		nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Envelope envelope = new nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Envelope();
		envelope.setFrom(sender);
		envelope.addTo(receiver);
		envelope.addIntendedReceiver(receiver);
		
		m.setEnvelope(envelope);
		
		return m;
	}
	
	public ACLMessage createForward(nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID sender, nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID receiver) {
		return createForward(getPerformative(), sender, receiver);
	}

	/**
	 * <p>
	 * Create a new ACLMessage that is a reply to this message, with a different
	 * performative.
	 * </p>
	 * <p>
	 * This method sets the following parameters of the new message: receiver,
	 * language, ontology, protocol, conversation-id, in-reply-to, reply-with.
	 * </p>
	 * <p>
	 * The programmer needs to set the communicative-act and the content. Of course,
	 * they can reset any of the fields.
	 * </p>
	 * 
	 * @param perf
	 * @return the ACLMessage to send as a reply
	 */
	public ACLMessage createReply(nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID sender, Performative perf) {
		ACLMessage m = new ACLMessage(perf);
		m.setSender(sender);
		Iterator<nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID> it = getAllReplyTo();
		while (it.hasNext())
			m.addReceiver(it.next());
		if ((reply_to == null) || reply_to.isEmpty())
			m.addReceiver(getSender());
		m.setLanguage(getLanguage());
		m.setOntology(getOntology());
		m.setProtocol(getProtocol());
		m.setInReplyTo(getReplyWith());
		if (source != null)
			m.setReplyWith(source.getName().toString() + java.lang.System.currentTimeMillis());
		else
			m.setReplyWith("X" + java.lang.System.currentTimeMillis());
		m.setConversationId(getConversationId());
		// Copy only well defined user-def-params
		String trace = getUserDefinedParameter(TRACE);
		if (trace != null) {
			m.addUserDefinedParameter(TRACE, trace);
		}
		// #CUSTOM_EXCLUDE_BEGIN
		// Set the Aclrepresentation of the reply message to the aclrepresentation of
		// the sent message
		if (messageEnvelope != null) {
			m.setDefaultEnvelope();
			String aclCodec = messageEnvelope.getAclRepresentation();
			if (aclCodec != null)
				m.getEnvelope().setAclRepresentation(aclCodec);
		} else
			m.setEnvelope(null);
		// #CUSTOM_EXCLUDE_END
		return m;
	}

	/**
	 * retrieve the whole list of intended receivers for this message.
	 * 
	 * @return An Iterator over all the intended receivers of this message taking
	 *         into account the Envelope ":intended-receiver" first, the Envelope
	 *         ":to" second and the message ":receiver" last.
	 */
	public Iterator<nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID> getAllIntendedReceiver() {
		Iterator<nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID> it = null;
		// #CUSTOM_EXCLUDE_BEGIN
		Envelope env = getEnvelope();
		if (env != null) {
			it = env.getAllIntendedReceiver();
			if (!it.hasNext()) {
				// The ":intended-receiver" field is empty --> try with the ":to" field
				it = env.getAllTo();
			}
		}
		// #CUSTOM_EXCLUDE_END
		if (it == null || !it.hasNext()) {
			// Both the ":intended-receiver" and the ":to" fields are empty -->
			// Use the ACLMessage receivers
			it = getAllReceiver();
		}
		return it;
	}

	// #MIDP_EXCLUDE_BEGIN

	// For persistence service
	private Long persistentID;

	// For persistence service
	protected Long getPersistentID() {
		return persistentID;
	}

	// For persistence service
	protected void setPersistentID(Long l) {
		persistentID = l;
	}

	public void setReceivers(Collection<nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID> al) {
		if (dests == null) {
		    dests = new HashSet<>();
		}
		dests.clear();
		dests.addAll(al);
	}

	@Override
	public Collection<nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID> getReceiver() {
		return dests;
	}

	// For persistence service
	public void setReplyTo(Collection<nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID> al) {
		reply_to.clear();
		reply_to.addAll(al);
	}

	// For persistence service
	public Collection<AgentID> getReplyTo() {
		return reply_to;
	}

	// For persistence service
	protected void setUserDefinedProperties(Serializable p) {
		userDefProps = (Properties) p;
	}

	// For persistence service
	protected Serializable getUserDefinedProperties() {
		return userDefProps;
	}

	// #MIDP_EXCLUDE_END

}

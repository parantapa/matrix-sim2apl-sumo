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

package nl.uu.cs.iss.ga.sim2apl.core.fipa.mts;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID;
import nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.ACLMessage;
import nl.uu.cs.iss.ga.sim2apl.core.logging.Loggable;
import nl.uu.cs.iss.ga.sim2apl.core.platform.Platform;
import nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.LEAPACLCodec;;

/**
 * This class models a FIPA Envelope.
 * 
 * @author Mohammad Shafahi
 */
public class Envelope implements Serializable {

	private static final Loggable logger = Platform.getLogger();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7047505902150401169L;
	private final static int EXPECTED_LIST_SIZE = 2;

	private Set<AgentID> to = new HashSet<AgentID>(EXPECTED_LIST_SIZE);
	private AgentID from;
	private String comments;
	private String aclRepresentation;
	private Long payloadLength;
	private String payloadEncoding;
	private Date date;
	private Set<AgentID> intendedReceiver = new HashSet<AgentID>(EXPECTED_LIST_SIZE);
	private Properties transportBehaviour;
	private ArrayList<nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Received> stamps = new ArrayList<nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Received>(EXPECTED_LIST_SIZE); // received parameter in FIPA
	private ArrayList<nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Property<?>> properties = new ArrayList<nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Property<?>>(EXPECTED_LIST_SIZE);

	/*
	public static ACLMessage decode(byte[] data)
	{
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			
			ObjectInputStream ois = new ObjectInputStream(bais);
			Envelope envelope = (Envelope) ois.readObject();
			// TODO: Decode envelope (make method in that class?) with DataInputStream instead (to keep the size down).
			
			DataInputStream din = new DataInputStream(bais);
			ACLMessage result = LEAPACLCodec.deserializeACL(din);
			
			result.messageEnvelope = envelope;
			
			return result;
		} catch (IOException | URISyntaxException | ClassNotFoundException ex) {
			// TODO err handling
           logger.log(ACLMessage.class, ex);
		}
		
		return new ACLMessage(Performative.UNKNOWN);
	} 
	*/
	
	// TODO?: Move to LEAPACLCodec?
	public static Envelope decode(DataInputStream din) { //byte[] data) {
		Envelope env = new Envelope();
		
		//try ( DataInputStream din = new DataInputStream(new ByteArrayInputStream(data)) ) {
		try {
			final int flagsA = din.readByte();
			final int flagsB = din.readByte();
			
			//Set<AgentID> to
			if ((flagsA & 0x01) != 0) {
				while(din.readBoolean()) {
					env.to.add( nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.LEAPACLCodec.deserializeAID(din) );
				}
			}
						
			//AgentID from
			if ((flagsA & 0x02) != 0) {
				env.from = nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.LEAPACLCodec.deserializeAID(din);
			}
			
			//String comments
			if ((flagsA & 0x04) != 0) {
				env.comments = din.readUTF();
			}
						
			//String aclRepresentation
			if ((flagsA & 0x08) != 0) {
				env.aclRepresentation = din.readUTF();
			}
						
			//Long payloadLength
			if ((flagsA & 0x10) != 0) {
				env.payloadLength = din.readLong();
			}
						
			//String payloadEncoding
			if ((flagsA & 0x20) != 0) {
				env.payloadEncoding = din.readUTF();
			}
									
			//Date date
			if ((flagsA & 0x40) != 0) {
				env.date = new Date(din.readLong());
			}
			
			//Set<AgentID> intendedReceiver
			if ((flagsA & 0x80) != 0) {
				while (din.readBoolean()) {
					env.intendedReceiver.add( nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.LEAPACLCodec.deserializeAID(din) );
				}
			}
						
			//Properties transportBehaviour
			if ((flagsB & 0x01) != 0) {
				while (din.readBoolean()) {
					// NOTE!/TODO?: Seems like 'Strings' are enforced for both key and value, but the set itself is 'Object/Object', not completely sure this is right.
					String key = din.readUTF();
					String val = din.readUTF();
					env.transportBehaviour.setProperty(key, val);
				}
			}
						
			//ArrayList<Received> stamps
			if ((flagsB & 0X02) != 0) {
				while (din.readBoolean()) {
//					final short len = din.readShort();
//					byte[] receivedBytes = new byte[len];
//					din.readNBytes(receivedBytes, 0, len);
//					env.stamps.add( Received.decode(receivedBytes) );
					nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Received.decode(din);
				}
			}
						
			//ArrayList<Property<?>> properties
			if ((flagsB & 0x04) != 0) {
				//Property-type:
				//  0 = last property, break.
				//  1 = String
				//  2 = long
				//  3 = double
				//  7 = Agent-id
				//  ? = also String
				
				byte propType = 0;
				do {
					propType = din.readByte();
					
					String name = din.readUTF();
					nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Property<?> prop;
					
					switch (propType) {
					case 2: prop = new nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Property<Long>(name, din.readLong()); break;
					case 3: prop = new nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Property<Double>(name, din.readDouble()); break;
					case 7: prop = new nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Property<AgentID>(name, nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.LEAPACLCodec.deserializeAID(din)); break;
					
					case 1:
					default: prop = new nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Property<String>(name, din.readUTF()); break;
					}
					
					env.properties.add( prop );
					
				} while(propType != 0);
			}
			
		}  catch (IOException | URISyntaxException ex ) { //| ClassNotFoundException ex) {
			// TODO err handling
	        logger.log(ACLMessage.class, ex);
		}
		
		return env;
	}
	
	// TODO?: Move to LEAPACLCodec?
	public void encode(DataOutputStream dos)
	{		
		try {
		// 'Present' flags:
		byte flagsA = 0;
		byte flagsB = 0;
		flagsA |= to.isEmpty() ? 0x0 : 0x1;
		flagsA |= from == null ? 0x0 : 0x2;
		flagsA |= comments == null || comments.isEmpty() ? 0x0 : 0x4;
		flagsA |= aclRepresentation == null || aclRepresentation.isEmpty() ? 0x0 : 0x8;
		flagsA |= payloadLength <= 0 ? 0x0 : 0x10;
		flagsA |= payloadEncoding == null || payloadEncoding.isEmpty() ? 0x0 : 0x20;
		flagsA |= date == null ? 0x0 : 0x40;
		flagsA |= intendedReceiver == null || intendedReceiver.isEmpty() ? 0x0 : 0x80;
		flagsB |= transportBehaviour == null || transportBehaviour.isEmpty() ? 0x0 : 0x1;
		flagsB |= stamps == null || stamps.isEmpty() ? 0x0 : 0x2;
		flagsB |= properties == null || properties.isEmpty() ? 0x0 : 0x4;
		dos.writeByte(flagsA);
		dos.writeByte(flagsB);
				
		//Set<AgentID> to
		if ((flagsA & 0x01) != 0) {
			for (AgentID id : to) {
				dos.writeBoolean(true);
				nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.LEAPACLCodec.serializeAID(id, dos);
			}
			dos.writeBoolean(false);
		}
		
		//AgentID from
		if ((flagsA & 0x02) != 0) {
			nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.LEAPACLCodec.serializeAID(from, dos);
		}
		
		//String comments
		if ((flagsA & 0x04) != 0) {
			dos.writeUTF(comments);
		}
		
		//String aclRepresentation
		if ((flagsA & 0x08) != 0) {
			dos.writeUTF(aclRepresentation);
		}
		
		//Long payloadLength
		if ((flagsA & 0x10) != 0) {
			dos.writeLong(payloadLength);
		}
		
		//String payloadEncoding
		if ((flagsA & 0x20) != 0) {
			dos.writeUTF(payloadEncoding);
		}
					
		//Date date
		if ((flagsA & 0x40) != 0) {
			dos.writeLong(date.getTime());
		}
		
		//Set<AgentID> intendedReceiver
		if ((flagsA & 0x80) != 0) {
			for (AgentID id : intendedReceiver) {
				dos.writeBoolean(true);
				nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.LEAPACLCodec.serializeAID(id, dos);
			}
			dos.writeBoolean(false);
		}
		
		//Properties transportBehaviour
		if ((flagsB & 0x01) != 0) {
			// NOTE!/TODO?: Seems like 'Strings' are enforced for both key and value, but the set itself is 'Object/Object', not completely sure this is right.
			final Set<Object> keys = transportBehaviour.keySet();
			for (Object key : keys) {
				dos.writeBoolean(true);
				Object value = transportBehaviour.get(key);
				dos.writeUTF((String) key);
				dos.writeUTF((String) value);
			}
			dos.writeBoolean(false);
		}
		
		//ArrayList<Received> stamps
		if ((flagsB & 0X02) != 0) {
			for (nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Received stamp : stamps) {
				dos.writeBoolean(true);
				stamp.encode(dos);
			}
			dos.writeBoolean(false);
		}
		
		//ArrayList<Property<?>> properties
		if ((flagsB & 0x04) != 0) {
			
			for (nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Property<?> prop : properties) {
				dos.writeBoolean(true);
				
				dos.writeUTF(prop.getName());
				
				Class<?> clazz = prop.getValue().getClass();
				if (clazz == String.class) {
					dos.writeUTF((String) prop.getValue()); 
				} else if (clazz == Long.class) {
					dos.writeLong((Long) prop.getValue());
				} else if (clazz == Double.class) {
					dos.writeDouble((Double) prop.getValue());
				} else if (clazz == AgentID.class) {
					LEAPACLCodec.serializeAID((AgentID) prop.getValue(), dos);
				} else {
					dos.writeUTF(prop.getValue().toString());
				}
			}
			dos.writeBoolean(false);
		}
		
		} catch (IOException ex) {
			// TODO err handling
	        logger.log(ACLMessage.class, ex);
		}
	}
	
	/**
	 * Default constructor. Initializes the payloadLength to -1.
	 **/
	public Envelope() {
		payloadLength = -1L;
	}

	/**
	 * Add an agent identifier to the <code>to</code> slot collection of this
	 * object.
	 * 
	 * @param id
	 *            The agent identifier to add to the collection.
	 */
	public void addTo(AgentID id) {
		to.add(id);
	}

	/**
	 * Remove an agent identifier from the <code>to</code> slot collection of this
	 * object.
	 * 
	 * @param id
	 *            The agent identifierto remove from the collection.
	 * @return A boolean, telling whether the element was present in the collection
	 *         or not.
	 */
	public boolean removeTo(AgentID id) {
		return to.remove(id);
	}

	/**
	 * Remove all agent identifiers from the <code>to</code> slot collection of this
	 * object.
	 */
	public void clearAllTo() {
		to.clear();
	}

	/**
	 * Access all agent identifiers from the <code>to</code> slot collection of this
	 * object.
	 * 
	 * @return An iterator over the agent identifiers collection.
	 */
	public Iterator<AgentID> getAllTo() {
		return to.iterator();
	}

	/**
	 * Count the number of agent identifiers from the <code>to</code> slot
	 * collection of this object.
	 * 
	 * @return An int of the number of agent identifiers in the <code>to</code>
	 *         slot.
	 */
	public int getCountOfTo() {
		if (to != null) {
			return to.size();
		} else {
			return 0;
		}
	}

	/**
	 * Set the <code>from</code> slot of this object.
	 * 
	 * @param id
	 *            The agent identifier for the envelope sender.
	 */
	public void setFrom(AgentID id) {
		from = id;
	}

	/**
	 * Retrieve the <code>from</code> slot of this object.
	 * 
	 * @return The value of the <code>from</code> slot of this envelope, or
	 *         <code>null</code> if no value was set.
	 */
	public AgentID getFrom() {
		return from;
	}

	/**
	 * Set the <code>comments</code> slot of this object.
	 * 
	 * @param c
	 *            The string for the envelope comments.
	 */
	public void setComments(String c) {
		comments = c;
	}

	/**
	 * Retrieve the <code>comments</code> slot of this object.
	 * 
	 * @return The value of the <code>comments</code> slot of this envelope, or
	 *         <code>null</code> if no value was set.
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * Set the <code>acl-representation</code> slot of this object.
	 * 
	 * @param r
	 *            The string for the ACL representation.
	 */
	public void setAclRepresentation(String r) {
		aclRepresentation = r;
	}

	/**
	 * Retrieve the <code>acl-representation</code> slot of this object.
	 * 
	 * @return The value of the <code>acl-representation</code> slot of this
	 *         envelope, or <code>null</code> if no value was set.
	 */
	public String getAclRepresentation() {
		return aclRepresentation;
	}

	/**
	 * Set the <code>payload-length</code> slot of this object.
	 * 
	 * @param l
	 *            The payload length, in bytes.
	 */
	// TODO?: payloadLength is never set anywhere or used outside of this class! (What else? payloadEncoding?)
	public void setPayloadLength(Long l) {
		payloadLength = l;
	}

	/**
	 * Retrieve the <code>payload-length</code> slot of this object.
	 * 
	 * @return The value of the <code>payload-length</code> slot of this envelope,
	 *         or <code>null</code> or a negative value if no value was set.
	 */
	public Long getPayloadLength() {
		return payloadLength;
	}

	/**
	 * Set the <code>payload-encoding</code> slot of this object. This slot can be
	 * used to specify a different charset than the standard one (US-ASCII) in order
	 * for instance to support accentuated characters in the content slot of the ACL
	 * message (e.g. setPayloadEncoding("UTF-8")).
	 * 
	 * @param e
	 *            The string for the payload encoding.
	 */
	public void setPayloadEncoding(String e) {
		payloadEncoding = e;
	}

	/**
	 * Retrieve the <code>payload-encoding</code> slot of this object.
	 * 
	 * @return The value of the <code>payload-encoding</code> slot of this envelope,
	 *         or <code>null</code> if no value was set.
	 */
	public String getPayloadEncoding() {
		return payloadEncoding;
	}

	/**
	 * Set the <code>date</code> slot of this object.
	 * 
	 * @param d
	 *            The envelope date.
	 */
	public void setDate(Date d) {
		date = d;
	}

	/**
	 * Retrieve the <code>date</code> slot of this object.
	 * 
	 * @return The value of the <code>date</code> slot of this envelope, or
	 *         <code>null</code> if no value was set.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Add an agent identifier to the <code>intended-receiver</code> slot collection
	 * of this object.
	 * 
	 * @param id
	 *            The agent identifier to add to the collection.
	 */
	public void addIntendedReceiver(AgentID id) {
		intendedReceiver.add(id);
	}

	/**
	 * Remove an agent identifier from the <code>intended-receiver</code> slot
	 * collection of this object.
	 * 
	 * @param id
	 *            The agent identifier to remove from the collection.
	 * @return A boolean, telling whether the element was present in the collection
	 *         or not.
	 */
	public boolean removeIntendedReceiver(AgentID id) {
		return intendedReceiver.remove(id);
	}

	/**
	 * Remove all agent identifiers from the <code>intended-receiver</code> slot
	 * collection of this object.
	 */
	public void clearAllIntendedReceiver() {
		intendedReceiver.clear();
	}

	/**
	 * Access all agent identifiers from the <code>intended
	 receiver</code> slot collection of this object.
	 * 
	 * @return An iterator over the agent identifiers collection.
	 */
	public Iterator<AgentID> getAllIntendedReceiver() {
		return intendedReceiver.iterator();
	}

	/**
	 * Count the number of agent identifiers from the <code>intended
	 receiver</code> slot collection of this object.
	 * 
	 * @return An int of the number of agent identifiers in the code>intended
	 *         receiver</code> slot.
	 */
	public int getCountOfIntendedReceiver() {
		if (intendedReceiver != null) {
			return intendedReceiver.size();
		} else {
			return 0;
		}
	}

	/**
	 * Set the <code>received</code> slot of this object.
	 * 
	 * @param ro
	 *            The received object for the <code>received</code> slot.
	 */
	public void setReceived(nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Received ro) {
		addStamp(ro);
	}

	/**
	 * Retrieve the <code>received</code> slot of this object.
	 * 
	 * @return The value of the <code>received</code> slot of this envelope, or
	 *         <code>null</code> if no value was set.
	 */
	public nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Received getReceived() {
		if (stamps.isEmpty())
			return null;
		else
			return stamps.get(stamps.size() - 1);
	}

	/**
	 * Add a <code>received-object</code> stamp to this message envelope. This
	 * method is used by the ACC to add a new stamp to the envelope at every routing
	 * hop.
	 * 
	 * @param ro
	 *            The <code>received-object</code> to add.
	 */
	public void addStamp(nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Received ro) {
		stamps.add(ro);
	}

	/**
	 * Access the list of all the stamps. The <code>received-object</code> stamps
	 * are sorted according to the routing path, from the oldest to the newest.
	 */
	public nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Received[] getStamps() {
		nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Received[] ret = new nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Received[stamps.size()];
		int counter = 0;

		for (Iterator<nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Received> it = stamps.iterator(); it.hasNext();)
			ret[counter++] = it.next();

		return ret;
	}

	/**
	 * Add a property to the <code>properties</code> slot collection of this object.
	 * 
	 * @param p
	 *            The property to add to the collection.
	 */
	public void addProperties(nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Property<?> p) {
		properties.add(p);
	}

	/**
	 * Remove a property from the <code>properties</code> slot collection of this
	 * object.
	 * 
	 * @param p
	 *            The property to remove from the collection.
	 * @return A boolean, telling whether the element was present in the collection
	 *         or not.
	 */
	public boolean removeProperties(nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Property<?> p) {
		return properties.remove(p);
	}

	/**
	 * Remove all properties from the <code>properties</code> slot collection of
	 * this object.
	 */
	public void clearAllProperties() {
		properties.clear();
	}

	/**
	 * Access all properties from the <code>properties</code> slot collection of
	 * this object.
	 * 
	 * @return An iterator over the properties collection.
	 */
	public Iterator<?> getAllProperties() {
		return properties.iterator();
	}

	// #MIDP_EXCLUDE_BEGIN
	/**
	 * Retrieve a string representation for this platform description.
	 * 
	 * @return an SL0-like String representation of this object
	 **/
	@Override
	public String toString() {
		String s = "(Envelope ";
		Iterator<AgentID> i = getAllTo();
		if (i.hasNext()) {
			s = s + " :to (sequence ";
			for (Iterator<AgentID> ii = i; ii.hasNext();)
				s = s + " " + ii.next().toString();
			s = s + ") ";
		}
		if (getFrom() != null)
			s = s + " :from " + getFrom().toString();
		if (getComments() != null)
			s = s + " :comments " + getComments();
		if (getAclRepresentation() != null)
			s = s + " :acl-representation " + getAclRepresentation();
		if (getPayloadLength() != null)
			s = s + " :payload-length " + getPayloadLength().toString();
		if (getPayloadEncoding() != null)
			s = s + " :payload-encoding " + getPayloadEncoding();
		if (getDate() != null)
			s = s + " :date " + getDate().toString();
		i = getAllIntendedReceiver();
		if (i.hasNext()) {
			s = s + " :intended-receiver (sequence ";
			for (Iterator<AgentID> ii = i; ii.hasNext();)
				s = s + " " + ii.next().toString();
			s = s + ") ";
		}
		nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Received[] ro = getStamps();
		if (ro.length > 0) {
			s = s + " :received-object (sequence ";
			for (int j = 0; j < ro.length; j++) {
				if (ro[j] != null) {
					s = s + " " + ro[j].toString();
				}
			}
			s = s + ") ";
		}
		if (properties.size() > 0) {
			s = s + " :properties (set";
			for (int j = 0; j < properties.size(); j++) {
				nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Property<?> p = properties.get(j);
				s = s + " " + p.getName() + " " + p.getValue();
			}
			s = s + ")";
		}
		return s + ")";
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Object clone() {
		Envelope env = new Envelope();

		// Deep clone
		env.to = new HashSet<AgentID>(to.size());
		to.forEach( (id) -> env.to.add(id.clone()) );

		// Deep clone
		env.intendedReceiver = new HashSet<AgentID>(intendedReceiver.size());
		intendedReceiver.forEach( (id) -> env.intendedReceiver.add(id.clone()) );

		// TODO: Are we sure we can suppress this unchecked warning?
		env.stamps = (ArrayList<Received>) stamps.clone();

		if (from != null) {
			env.from = from.clone();
		}
		env.comments = comments;
		env.aclRepresentation = aclRepresentation;
		env.payloadLength = payloadLength;
		env.payloadEncoding = payloadEncoding;
		env.date = date;
		env.transportBehaviour = transportBehaviour;

		// Deep clone. Particularly important when security is enabled as
		// SecurityObject-s (that are stored as
		// Envelope properties) are modified in the encryption process
		env.properties = new ArrayList<nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Property<?>>(properties.size());
		for (int i = 0; i < properties.size(); i++) {
			nl.uu.cs.iss.ga.sim2apl.core.fipa.mts.Property<?> p = properties.get(i);
			env.properties.add((Property<?>) p.clone());
		}

		return env;
	}

}

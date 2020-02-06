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
import java.util.Date;

import nl.uu.cs.iss.ga.sim2apl.core.fipa.acl.ACLMessage;
import nl.uu.cs.iss.ga.sim2apl.core.logging.Loggable;
import nl.uu.cs.iss.ga.sim2apl.core.platform.Platform;

/**
 * This class implements the <code>received-object</code> object from the FIPA
 * Agent Management ontology.
 * 
 * @see jade.domain.FIPAAgentManagement.FIPAManagementOntology
 * @author Fabio Bellifemine - CSELT S.p.A.
 * @version $Date: 2008-10-09 14:04:02 +0200 (Thu, 09 Oct 2008) $ $Revision:
 *          6051 $
 */
public class Received implements Serializable {

	private static final Loggable logger = Platform.getLogger();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6061044825636879389L;

	private String by;
	private String from;
	private Date date;
	private String id;
	private String via;

	/**
	 * Decode a 'Received' object from an array of bytes.
	 */
	public static Received decode(DataInputStream din) {  	//public static Received decode(byte[] data) {
		Received rec = new Received();
		
		//try ( DataInputStream din = new DataInputStream(new ByteArrayInputStream(data)) ) {
		try {
			rec.by = din.readUTF();
			rec.from = din.readUTF();
			rec.date = new Date(din.readLong());
			rec.id = din.readUTF();
			rec.via = din.readUTF();
		}  catch (IOException ex) {
	        logger.log(ACLMessage.class, ex);
		}
		
		return rec;
	}
	
	/**
	 * The constructor initializes the date to current time and all the Strings to
	 * empty strings.
	 **/
	public Received() {
		date = new Date();
		by = "";
		from = "";
		id = "";
		via = "";
	}

	/**
	 * Encode this 'Received' object to an array of bytes. 
	 */
	public void encode(DataOutputStream dos) { //public byte[] encode() {
		//try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			//try (DataOutputStream dos = new DataOutputStream(baos)) {
		try {
				dos.writeUTF(this.by);
				dos.writeUTF(this.from);
				dos.writeLong(this.date.getTime());
				dos.writeUTF(this.id);
				dos.writeUTF(this.via);
				//return baos.toByteArray();
//			} catch (IOException ex) {
//				logger.log(getClass(), ex);
//				return new byte[0];
//			}
		} catch (IOException ex) {
			logger.log(getClass(), ex);
			//return new byte[0];
		}
	}
	
	/**
	 * Set the <code>by</code> slot of this object.
	 * 
	 * @param b
	 *            The identifier for the ACC that received the envelope containing
	 *            this object.
	 */
	public void setBy(String b) {
		by = b;
	}

	/**
	 * Retrieve the <code>by</code> slot of this object. This slot identifies the
	 * ACC that received the envelope containing this object.
	 * 
	 * @return The value of the <code>by</code> slot of this object, or
	 *         <code>null</code> if no value was set.
	 */
	public String getBy() {
		return by;
	}

	/**
	 * Set the <code>from</code> slot of this object.
	 * 
	 * @param f
	 *            The identifier for the ACC that sent the envelope containing this
	 *            object.
	 */
	public void setFrom(String f) {
		from = f;
	}

	/**
	 * Retrieve the <code>from</code> slot of this object. This slot identifies the
	 * ACC that sent the envelope containing this object.
	 * 
	 * @return The value of the <code>from</code> slot of this object, or
	 *         <code>null</code> if no value was set.
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * Set the <code>date</code> slot of this object.
	 * 
	 * @param d
	 *            The date when the envelope containing this object was sent.
	 */
	public void setDate(Date d) {
		date = d;
	}

	/**
	 * Retrieve the <code>date</code> slot of this object. This slot identifies the
	 * date when the envelope containing this object was sent.
	 * 
	 * @return The value of the <code>date</code> slot of this object, or
	 *         <code>null</code> if no value was set.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Set the <code>id</code> slot of this object.
	 * 
	 * @param i
	 *            A unique id for the envelope containing this object.
	 */
	public void setId(String i) {
		id = i;
	}

	/**
	 * Retrieve the <code>id</code> slot of this object. This slot uniquely
	 * identifies the envelope containing this object.
	 * 
	 * @return The value of the <code>id</code> slot of this object, or
	 *         <code>null</code> if no value was set.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the <code>via</code> slot of this object.
	 * 
	 * @param v
	 *            The name of the MTP over which the envelope containing this object
	 *            was sent.
	 */
	public void setVia(String v) {
		via = v;
	}

	/**
	 * Retrieve the <code>via</code> slot of this object. This slot describes the
	 * MTP over which the envelope containing this object was sent.
	 * 
	 * @return The value of the <code>via</code> slot of this envelope, or
	 *         <code>null</code> if no value was set.
	 */
	public String getVia() {
		return via;
	}

	/**
	 * Retrieve a string representation for this received object.
	 * 
	 * @return an SL0-like String representation of this object
	 **/
	@Override
	public String toString() {
		String s = "(ReceivedObject ";
		if (date != null)
			s = s + " :date " + date.toString();
		if ((by != null) && (by.trim().length() > 0))
			s = s + " :by " + by;
		if ((from != null) && (from.trim().length() > 0))
			s = s + " :from " + from;
		if ((id != null) && (id.trim().length() > 0))
			s = s + " :id " + id;
		if ((via != null) && (via.trim().length() > 0))
			s = s + " :via " + via;
		return s;
	}

}

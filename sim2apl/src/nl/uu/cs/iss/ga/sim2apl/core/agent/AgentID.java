package nl.uu.cs.iss.ga.sim2apl.core.agent;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Properties;

/**
 * This class represents a NET2APL Agent Identifier. This class complies with
 * FIPA
 * 
 * @author Mohammad Shafahi
 */
public class AgentID implements Serializable {

	private static final String dummyHost = "127.0.0.1"; //GetInitialLocalHost(); //"net2apl.uu.nl";
	private static final int dummyPort = 44444; //40400;
	private static final long serialVersionUID = 8340622959725062448L;

	private URI name;
	private List<URL> addresses = new ArrayList<>();
	private List<AgentID> resolvers = new ArrayList<>();
	private Properties userDefSlots = new Properties();

	public AgentID(URI uri) throws URISyntaxException {
		this.name = uri;
	}
	
	public AgentID(UUID uuID, String host, int port) throws URISyntaxException {
		this(new URI(null, uuID.toString(), host, port, null, null, "Agent-" + uuID.toString().substring(9, 13)));
	}

	public AgentID(String nickname, UUID uuID, String host, int port) throws URISyntaxException {
		this(new URI(null, uuID.toString(), host, port, null, null, nickname));
	}
	
	public AgentID(String uri) throws URISyntaxException {
		this(new URI(uri));
	}
	

	
//	public AgentID(String localname, String host) throws URISyntaxException {
//		this(localname, host, defaultPort);
//	}
//
//	public AgentID(String localname) throws URISyntaxException {
//		this(localname, defaultHost);
//	}
//
//	public AgentID(UUID localname) throws URISyntaxException {
//		this(localname.toString());
//	}

	public static AgentID createEmpty() throws URISyntaxException {
		return new AgentID();
	}
	
	private AgentID() throws URISyntaxException {
		this(UUID.randomUUID(), dummyHost, dummyPort);
	}

	public URI getName() {
		return name;
	}

	public void setName(String localname, String host) throws URISyntaxException {
		this.name = new URI(null, localname, host, -1, null, null, null);
	}

	public void setName(URI name) {
		this.name = name;
	}

	public void setName(String localname) throws URISyntaxException {
		this.setName(localname, this.getHost());
	}

	public List<URL> getAddresses() {
		return addresses;
	}

	public URL getFirstAddress() {
		return addresses.get(0);
	}

	public String getHost() {
		return this.getName().getHost();
	}

	public int getPort() {
		return this.getName().getPort();
	}
	
	public String getUuID() {
		return this.getName().getUserInfo();
	}

	public String getShortLocalName() {
		String word = this.getName().getUserInfo().replace("-", "");
		if (word.length() == 5) {
			return " " + word;
		} else if (word.length() > 5) {
			return " " + word.substring(word.length() - 5);
		} else {
			return word;
		}
	}

	public void addAddress(String address) throws MalformedURLException {
		this.addAddress(new URL(address));
	}

	public void addAddress(URL address) {
		if (!addresses.contains(address)) {
			this.addresses.add(address);
		}
	}

	public void setAddresses(List<URL> addresses) {
		this.addresses = addresses;
	}

	public List<AgentID> getResolvers() {
		return resolvers;
	}

	public void setResolvers(List<AgentID> resolvers) {
		this.resolvers = resolvers;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!AgentID.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		final AgentID aid = (AgentID) obj;
		
		if(this.getUuID() == null) {
			throw new IllegalStateException("Local name is null for this agent!");
		}
		else if(aid.getUuID() == null){
			throw new IllegalStateException("Local name is null for comparing with agent " + this.getUuID());
		}
		else if(!this.getUuID().equals(aid.getUuID())) {
			return false;
		}
		
		//old version
		//if ((this.getLocalName() == null) ? (aid.getLocalName() != null) : !this.getLocalName().equals(aid.getLocalName())) {
		//	return false;
		//}
		return true;
	}

	@Override
	public synchronized AgentID clone() {
		AgentID result = null;
		try {
			result = new AgentID(this.name);
			result.setAddresses(this.addresses);
			result.setResolvers(this.resolvers);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public Properties getUserDefSlots() {
		return userDefSlots;
	}

	public void setUserDefSlots(Properties userDefSlots) {
		this.userDefSlots = userDefSlots;
	}

	public void addUserDefinedSlot(String key, String value) {
		userDefSlots.setProperty(key, value);
	}

	public void addResolver(AgentID resolver) {
		if (!this.resolvers.contains(resolver)) {
			this.resolvers.add(resolver);
		}
	}

	@Override
	public String toString() {
		return this.getName().toString();
	}

	@Override
	public int hashCode() {

		return this.getUuID().hashCode();

	}

}

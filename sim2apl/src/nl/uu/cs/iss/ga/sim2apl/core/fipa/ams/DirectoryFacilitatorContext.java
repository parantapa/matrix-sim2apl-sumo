package nl.uu.cs.iss.ga.sim2apl.core.fipa.ams;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Context;

public class DirectoryFacilitatorContext implements Context {

	public final static String serviceTypeDF = "directory-facilitator";

	private final static Set<String> noService = new HashSet<>();
	private final static Set<AgentID> noAgent = new HashSet<>();

	private Map<String, Set<AgentID>> serviceTypeToAgent = new /* Concurrent */HashMap<>();
	private Map<AgentID, Set<String>> agentToServiceType = new /* Concurrent */HashMap<>();
	private Set<AgentID> otherDFs = new /* Concurrent */HashSet<>();
	// NOTE: keeping service-types of other DF's here is not ideal... (so we don't).

	private Map<String, Set<AgentID>> subscriptionToAgent = new HashMap<>();
	private Map<AgentID, Set<String>> agentToSubscription = new HashMap<>();

	//private Map<AgentID, String> nicknames = new HashMap<>();
	
	DirectoryFacilitatorContext(Set<AgentID> others) {
		if (others != null) {
			otherDFs.addAll(others);
		}
	}

	public synchronized void getAllServicesDisplay(Collection<String> allAgents, Map<String, String> serviceMap) {
		agentToServiceType.forEach((aid, set) -> {
			allAgents.add(aid.toString());

			String services = "";
			for (String service : set) {
				services += " " + service;
			}
			serviceMap.put(aid.getName().toString(), services);
		});
		otherDFs.forEach((other) -> {
			allAgents.add(other.toString());
			serviceMap.put(other.getName().toString(), " [[ directory facilitator ]] ");
		});
	}

	public synchronized void getAllSubscriptionsDisplay(Collection<String> allAgents, Map<String, String> serviceMap) {
		agentToSubscription.forEach((aid, set) -> {
			allAgents.add(aid.toString());

			String services = "";
			for (String service : set) {
				services += " " + service;
			}
			serviceMap.put(aid.getName().toString(), services);
		});
	}

	// Returns the set of agents that need to be notified (subscribers need to know
	// of new services).
	// TODO! Subscriber should be informed /which/ subscription the agent-aid it got
	// for is a service of, in case it's subscribed to multiple services!
	public synchronized Collection<AgentID> register(String serviceType, AgentID aid) {
		Set<AgentID> shouldSubscribe = new HashSet<>();

		if (serviceType.equals(serviceTypeDF)) {
			otherDFs.add(aid);
			// NOTE: The DF should act as a front for all other DF's, so no subscribers are
			// notified here.
		} else {
			serviceTypeToAgent.computeIfAbsent(serviceType, (key) -> new HashSet<>()).add(aid);
			agentToServiceType.computeIfAbsent(aid, (key) -> new HashSet<>()).add(serviceType);
			// NOTE: Don't use 'noService' or 'noAgent' here ^^^ (instead of 'new HashSet'),
			// it's important any new inserts are a new instance!

			subscriptionToAgent.getOrDefault(serviceType, noAgent)
					.forEach((subscriber) -> shouldSubscribe.add(subscriber));
		}

		return shouldSubscribe;
	}

	// Returns the set of agents that need to be notified (subscribers might want to
	// know if a particular agent discontinues a service).
	public synchronized Collection<AgentID> deregister(AgentID aid) {
		Set<AgentID> shouldNotify = new HashSet<>();

		otherDFs.remove(aid);
		agentToServiceType.remove(aid).forEach((canceledService) -> {
            shouldNotify.addAll( deregister(canceledService, aid) );
		});
		// NOTE: In order to remove 'empty' categories even as set, we should probably
		// use '<map>.computeIfPresent', but the ammount of service-types is probably
		// not going to explode, especially locally.

		return shouldNotify;
	}

	public synchronized Collection<AgentID> deregister(String serviceType, AgentID aid) {
		Set<AgentID> shouldNotify = new HashSet<>();
		
		if (serviceType.equals(serviceTypeDF)) {
			otherDFs.remove(aid);
		} else {
			serviceTypeToAgent.getOrDefault(serviceType, noAgent).remove(aid);
			subscriptionToAgent.getOrDefault(serviceType, noAgent).forEach((subscriber) -> shouldNotify.add(subscriber));
		}
		
		return shouldNotify;
	}
	
	public synchronized Collection<AgentID> subscribe(String serviceType, AgentID aid) {
		Set<AgentID> subscriptions = new HashSet<>();

		subscriptionToAgent.computeIfAbsent(serviceType, (key) -> new HashSet<>()).add(aid);
		agentToSubscription.computeIfAbsent(aid, (key) -> new HashSet<>()).add(serviceType);
		// NOTE: Don't use 'noService' or 'noAgent' here ^^^ (instead of 'new HashSet'),
		// it's important any new inserts are a new instance!

		serviceTypeToAgent.getOrDefault(serviceType, noAgent).forEach((serviceAid) -> subscriptions.add(serviceAid));

		return subscriptions;
	}

	public synchronized void unsubscribe(AgentID aid) {
		agentToSubscription.remove(aid).forEach((canceledSub) -> {
			unsubscribe(canceledSub, aid);
		});
	}

	public synchronized void unsubscribe(String serviceType, AgentID aid) {
		subscriptionToAgent.getOrDefault(serviceType, noAgent).remove(aid);
	}
	
	// NOTE: Returns at least ALL of the possible agents that can provide that are
	// registered to this DF.
	// The issues with this are:
	// - What if it's a huge list, and we only needed one?
	// - Doesn't always look in other DF's (case: there's one ore more service of
	// that type service in this DF),
	// but what if we /do/ want /all/ agents that provide that service?
	public synchronized Set<AgentID> searchLocal(String serviceType) {
		return serviceTypeToAgent.getOrDefault(serviceType, noAgent);
	}

	public synchronized Set<AgentID> getOtherDFs() {
		return otherDFs;
	}

	public static Set<String> getNoservice() {
		return noService;
	}

	public void addOtherDF(AgentID other) {
		otherDFs.add(other);
	}
	
	public void removeOtherDF(AgentID other) {
		otherDFs.remove(other);
	}
	/*
	public void saveNickname(AgentID aid) {
		nicknames.put(aid, aid.getNickName());
	}
	
	public void resetNickName(AgentID aid) {
		String nick = nicknames.get(aid);
		if (nick != null) {
		    aid.setNickName(nick);
		} else {
			nick = aid.getNickName();
			if (nick != null) {
				nicknames.put(aid, nick);
			}
		}
	}
	 */
	public Map<AgentID, Set<String>> getAgentToServiceType() {
		return agentToServiceType;
	}

	public Map<AgentID, Set<String>> getAgentToSubscription() {
		return agentToSubscription;
	}

	public Map<String, Set<AgentID>> getServiceTypeToAgent() {
		return serviceTypeToAgent;
	}

	public Map<String, Set<AgentID>> getSubscriptionToAgent() {
		return subscriptionToAgent;
	}
	
	
}

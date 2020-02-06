package agent;

import nl.uu.cs.iss.ga.sim2apl.core.agent.Agent;
import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID;

/**
 * A wrapper object that stores a reference to both a 2APL agent and a SUMO agent
 */
public class SumoAPLAgent {

    public static final String TYPE_ID = "agent";

    protected String sumoID;
    protected AgentID agentID;
    protected Agent agent;

    public SumoAPLAgent(String sumoID) {
        this.sumoID = sumoID;
    }

    public AgentID getAgentID() {
        return agentID;
    }

    public void setAgentID(AgentID agentID) {
        this.agentID = agentID;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;this.agentID = this.agent.getAID();
    }

    public String getSumoID() {
        return sumoID;
    }

    public String getTypeID() {
        return SumoAPLAgent.TYPE_ID;
    }
}

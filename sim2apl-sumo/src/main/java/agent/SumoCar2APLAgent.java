package agent;

import java.util.List;

/**
 * A wrapper object that stores a reference to both a 2APL agent and a car-type SUMO agent, including
 * relevant fields for cars in the SUMO environment that the 2APL agent should have access to
 */
public class SumoCar2APLAgent extends SumoAPLAgent {

    public static final String TYPE_ID = "car";

    protected String routeID;
    protected String destination;
    protected List<String> routeEdges;

    public SumoCar2APLAgent(String sumoCarID) {
        super(sumoCarID);
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<String> getRouteEdges() {
        return routeEdges;
    }

    public void setRouteEdges(List<String> routeEdges) {
        this.routeEdges = routeEdges;
    }

    @Override
    public String getTypeID() {
        return SumoCar2APLAgent.TYPE_ID;
    }
}

package agent.plan;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.util.SumoCommand;

public class EnterWorldPlanMessage implements PlanMessage {
    public static Gson gson = new Gson();
    
    public String sumoID;
    public String typeID;
    public String routeID;
    public int simulationTime;
    public byte laneIndex;
    
    public EnterWorldPlanMessage() {
        this.sumoID = null;
        this.typeID = null;
        this.routeID = null;
        this.simulationTime = 0;
        this.laneIndex = 0;
    }

    public EnterWorldPlanMessage(String sumoID, String typeID, String routeID, int simulationTime, byte laneIndex) {
        this.sumoID = sumoID;
        this.typeID = typeID;
        this.routeID = routeID;
        this.simulationTime = simulationTime;
        this.laneIndex = laneIndex;
    }

    @Override
    public SumoCommand getSumoCommand() {
        return Vehicle.add(
                sumoID,
                typeID,
                routeID,
                simulationTime,
                0, 2, // TODO need to do anything here?
                laneIndex
        );
    }
    
    @Override
    public String toJson() {
        JsonObject message = new JsonObject();
        message.addProperty("class", "EnterWorldPlanMessage");
        
        JsonElement instance = gson.toJsonTree(this);
        message.add("instance", instance);
        
        return gson.toJson(message);
    }
}

package agent.plan;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.util.SumoCommand;

public class SetMinGapPlanMessage implements PlanMessage {
    public static Gson gson = new Gson();
    
    public String sumoID;
    public double minGap;
    
    public SetMinGapPlanMessage() {
        this.sumoID = null;
        this.minGap = 0.0;
    }

    public SetMinGapPlanMessage(String sumoID, double minGap) {
        this.sumoID = sumoID;
        this.minGap = minGap;
    }
    
    @Override
    public SumoCommand getSumoCommand() {
        return Vehicle.setMinGap(sumoID, minGap);
    }

    @Override
    public String toJson() {
        JsonObject message = new JsonObject();
        message.addProperty("class", "SetMinGapPlanMessage");
        
        JsonElement instance = gson.toJsonTree(this);
        message.add("instance", instance);
        
        return gson.toJson(message);
    }
}

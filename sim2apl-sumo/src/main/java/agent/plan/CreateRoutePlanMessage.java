package agent.plan;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.tudresden.sumo.cmd.Route;
import de.tudresden.sumo.util.SumoCommand;
import de.tudresden.ws.container.SumoStage;

public class CreateRoutePlanMessage implements PlanMessage {
    public static Gson gson = new Gson();
    
    public String routeID;
    public SumoStage plannedRoute;
    
    public CreateRoutePlanMessage() {
        this.routeID = null;
        this.plannedRoute = new SumoStage();
    }

    public CreateRoutePlanMessage(String routeID, SumoStage plannedRoute) {
        this.routeID = routeID;
        this.plannedRoute = plannedRoute;
    }

    @Override
    public SumoCommand getSumoCommand() {
        return Route.add(routeID, plannedRoute.edges);
    }
    
    @Override
    public String toJson() {
        JsonObject message = new JsonObject();
        message.addProperty("class", "CreateRoutePlanMessage");
        
        JsonElement instance = gson.toJsonTree(this);
        message.add("instance", instance);
        
        return gson.toJson(message);
    }
}

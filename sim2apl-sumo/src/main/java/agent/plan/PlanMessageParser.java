package agent.plan;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PlanMessageParser {
    public static Gson gson = new Gson();
    
    public static PlanMessage parse(String mstr) {
        JsonObject mobj = JsonParser.parseString(mstr).getAsJsonObject();
        
        if (!mobj.has("class") || !mobj.has("instance")) {
            throw new RuntimeException(String.format("Message not of type PlanMessage: %s", mstr));
        }
        
        String klass = mobj.get("class").getAsString();
        JsonElement instance = mobj.get("instance");
        
        PlanMessage message;
        if ("SetMinGapPlanMessage".equals(klass)) {
            message = gson.fromJson(instance, SetMinGapPlanMessage.class);
        } else if ("SetMaxSpeedPlanMessage".equals(klass)) {
            message = gson.fromJson(instance, SetMaxSpeedPlanMessage.class);
        } else if ("CreateRoutePlanMessage".equals(klass)) {
            message = gson.fromJson(instance, CreateRoutePlanMessage.class);
        } else if ("EnterWorldPlanMessage".equals(klass)) {
            message = gson.fromJson(instance, EnterWorldPlanMessage.class);
        } else {
            throw new RuntimeException(String.format("Message class unknown: %s", klass));
        }
        
        return message;
    }
}

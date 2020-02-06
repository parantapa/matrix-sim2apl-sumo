package agent.plan;

import de.tudresden.sumo.util.SumoCommand;

public interface PlanMessage {
    public SumoCommand getSumoCommand();
    public String toJson();
}


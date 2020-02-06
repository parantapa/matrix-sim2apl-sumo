package nl.uu.cs.iss.ga.sim2apl.core.fipa;

/**
 * FIPA complient agent states
 * {@see http://www.fipa.org/specs/fipa00023/SC00023J.html} Section 5.1
 */
public enum FIPAAgentState {

    /**
     String constant for the <code>initiated</code> agent life-cycle
     state.

     When the agent is created or installed, it is put in <code>INITIATED</code> state.
     */
    INITIATED(true),

    /**
     String constant for the <code>active</code> agent life-cycle
     state.

     This state means the deliberation cycle of the agent should continue.
     */
    ACTIVE(true),

    /**
     String constant for the <code>suspended</code> agent life-cycle
     state.

     The agent can be put in the <code>SUSPENDED</code> state by the <code>SUSPEND</code> action, which can only be
     initiated by the agent itself, or by the Agent Messaging Service (AMS). From this state, the agent can be brought
     back in the <code>ACTIVE</code> state with the <code>RESUME</code> action, which can only be initiated by the AMS.
     */
    SUSPENDED(false),

    /**
     String constant for the <code>waiting</code> agent life-cycle
     state.

     This can only be initiated by an agent. The agent can be brought back in the active state with the
     <code>WAKE UP</code> action, which also can only be Agent itself.
     */
    WAITING(false),

    /**
     String constant for the <code>transit</code> agent life-cycle
     state.

     This state is only relevant for mobile agents {@see http://www.fipa.org/specs/fipa00005/index.html}. When the agent
     transports itself to another location, it puts itself in the <code>TRANSIT</code> state through the <code>MOVE</code>
     action. In this state, no deliberation can take place. The agent can be brought back to the <code>ACTIVE</code>
     state through the <code>EXECUTE</code> action. Both actions can be executed only by the agent.
     */
    TRANSIT(false);

    private boolean active;

    FIPAAgentState(boolean active) {
        this.active = active;
    }

	public boolean isActive() {
	    return this.active;
    }
}

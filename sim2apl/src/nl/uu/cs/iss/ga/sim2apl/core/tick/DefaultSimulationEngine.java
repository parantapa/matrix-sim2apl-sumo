package nl.uu.cs.iss.ga.sim2apl.core.tick;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID;
import nl.uu.cs.iss.ga.sim2apl.core.platform.Platform;

import java.util.HashMap;
import java.util.List;

/**
 * The default simulation engine starts the simulation, and requests the tick executor to advance
 * immediately after the previous tick has finished.
 *
 * The execution of the pre-tick hook and post-tick hook, as well as the agent tick itself are deliberately
 * made blocking. This ensures that the environment is only updated after <i>all</i> agents have finished their
 * sense-reason-act cycle, as well as that the agents only start a new cycle when the environment has completely
 * finished processing.
 *
 * The sense-reason-act cycles are executed using an ExecutorService, since agents are allowed to run in parallel.
 * This speeds up the time it takes to perform one tick for all the agents.
 */
public class DefaultSimulationEngine extends AbstractSimulationEngine {

    /** The TickExecutor is obtained from the platform. By default, the DefaultTickExecutor is used, but this can
     * be overridden by specifying a custom TickExecutor at platform creation */
    private final TickExecutor executor;
    
    private final PrintWriter time_log;
    
    /**
     * {@inheritDoc}
     */
    public DefaultSimulationEngine(Platform platform, int nIterations, TickHookProcessor... hookProcessors) {
        super(platform, nIterations, hookProcessors);
        this.executor = platform.getTickExecutor();
        
        String log_fname = System.getenv("SIM2APL_TIMELOG");
        if (log_fname == null) {
            time_log = null;
        } else {
            FileWriter fwriter;
            try {
                fwriter = new FileWriter(log_fname);
            } catch (IOException ex) {
                throw new RuntimeException("Failed to open time_log file:" + ex.toString());
            }
            BufferedWriter bwriter = new BufferedWriter(fwriter);
            time_log = new PrintWriter(bwriter);
        }
    }

    /**
     * {@inheritDoc}
     */
    public DefaultSimulationEngine(Platform platform) {
        super(platform);
        this.executor = platform.getTickExecutor();
        
        String log_fname = System.getenv("SIM2APL_TIMELOG");
        if (log_fname == null) {
            time_log = null;
        } else {
            FileWriter fwriter;
            try {
                fwriter = new FileWriter(log_fname);
            } catch (IOException ex) {
                throw new RuntimeException("Failed to open time_log file:" + ex.toString());
            }
            BufferedWriter bwriter = new BufferedWriter(fwriter);
            time_log = new PrintWriter(bwriter);
        }
    }

    /**
     * {@inheritDoc}
     */
    public DefaultSimulationEngine(Platform platform, TickHookProcessor... processors) {
        super(platform, processors);
        this.executor = platform.getTickExecutor();
        
        String log_fname = System.getenv("SIM2APL_TIMELOG");
        if (log_fname == null) {
            time_log = null;
        } else {
            FileWriter fwriter;
            try {
                fwriter = new FileWriter(log_fname);
            } catch (IOException ex) {
                throw new RuntimeException("Failed to open time_log file:" + ex.toString());
            }
            BufferedWriter bwriter = new BufferedWriter(fwriter);
            time_log = new PrintWriter(bwriter);
        }
    }

    /**
     * {@inheritDoc}
     */
    public DefaultSimulationEngine(Platform platform, int iterations) {
        super(platform, iterations);
        this.executor = platform.getTickExecutor();
        
        String log_fname = System.getenv("SIM2APL_TIMELOG");
        if (log_fname == null) {
            time_log = null;
        } else {
            FileWriter fwriter;
            try {
                fwriter = new FileWriter(log_fname);
            } catch (IOException ex) {
                throw new RuntimeException("Failed to open time_log file:" + ex.toString());
            }
            BufferedWriter bwriter = new BufferedWriter(fwriter);
            time_log = new PrintWriter(bwriter);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean start() {
        if(this.nIterations <= 0) {
            // Run until actively interrupted
            while(true) doTick();
        } else {
            // Run for fixed number of ticks
            for (int i = 0; i < this.nIterations; i++) doTick();
        }
        this.processSimulationFinishedHook(this.nIterations, executor.getLastTickDuration());
        this.executor.shutdown();
        if (time_log != null) {
            time_log.flush();
            time_log.close();
        }
        return true;
    }

    /**
     * Performs a single tick, and notifies all tickHookProcessors before and after the tick execution
     */
    private void doTick() {
        int tick = this.executor.getCurrentTick();
        if (time_log != null) {
            time_log.printf("TIME_LOG: TICK %d PREHOOK %d\n", tick, System.currentTimeMillis());
        }
        this.processTickPreHooks(tick);
        if (time_log != null) {
            time_log.printf("TIME_LOG: TICK %d ACT %d\n", tick, System.currentTimeMillis());
        }
        HashMap<AgentID, List<String>> agentActions = this.executor.doTick();
        if (time_log != null) {
            time_log.printf("TIME_LOG: TICK %d POSTHOOK %d\n", tick, System.currentTimeMillis());
        }
        this.processTickPostHook(tick, executor.getLastTickDuration(), agentActions);
        if (time_log != null) {
            time_log.printf("TIME_LOG: TICK %d FINISHED %d\n", tick, System.currentTimeMillis());
            time_log.flush();
        }
    }
}

package nl.uu.cs.iss.ga.sim2apl.core.logging;

import java.util.logging.Level;

/**
 * This logger will discard all messages
 * @author Jurian Baas
 *
 */
public class NullLogger extends Loggable  {

	/**
	 * This method does not log anything
	 */
	@Override
	public void log(Class<?> c, Level level, Object message) {
		// Do nothing on purpose
	}

}

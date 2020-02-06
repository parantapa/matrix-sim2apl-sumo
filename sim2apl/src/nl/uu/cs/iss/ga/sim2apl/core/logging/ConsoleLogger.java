package nl.uu.cs.iss.ga.sim2apl.core.logging;

import java.util.logging.Level;

/**
 * This logger will write messages to the console
 * @author Jurian Baas
 *
 */
public class ConsoleLogger extends Loggable {

	/**
	 * Log to the console
	 */
	@Override
	public void log(Class<?> c, Level level, Object message) {
		
		if(level == Level.SEVERE) {
			System.err.println(level + "\t|\t" +c.getName() + ":\t" + message.toString());
		} else {
			System.out.println(level + "\t|\t" +c.getName() + ":\t" + message.toString());
		}

	}

}

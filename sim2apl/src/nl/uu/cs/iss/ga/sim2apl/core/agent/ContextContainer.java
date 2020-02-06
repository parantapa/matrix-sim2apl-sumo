package nl.uu.cs.iss.ga.sim2apl.core.agent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The context container maintains the different contexts for the agent. Agents
 * can be composed of different pre-programmed agent modules, among which are
 * contexts. This container allows the programmer to obtain a context of a
 * specific type. Hence it is intended to only have 1 context instantiation of a
 * context class per agent.
 * 
 * The implementation of this class is based on Joshua Bloch's typesafe
 * hetereogeneous container pattern.
 * 
 * @author Bas Testerink
 */
public final class ContextContainer {
	
	/** Map to store the contexts. */
	private final Map<Class<?>, Context> map;

	public ContextContainer() {
		this.map = new HashMap<>();
	}

	/**
	 * Store a new context. Will overwrite any context with the same class that was
	 * previously added.
	 */
	public final void addContext(final Context context) {
		this.map.put(context.getClass(), context);
	}

	/**
	 * Store a new context with one or more explicitly provided classes as keys. Useful if one context extends or
	 * implements multiple (context) classes or interfaces, which should be used for lookup
	 *
	 * Contexts for existing keys will be overwritten!
	 *
	 * @param context 	Context to store
	 * @param keys 		List of keys this context should be associated with.
	 */
	public final void addImplementedContext(final Context context, Class<? extends Context> ... keys) {
		boolean mainClassGiven = false;
		for (Class<? extends Context> key : keys) {
			this.map.put(key, context);
			if(context.getClass().equals(key)) mainClassGiven = true;
		}
		if(!mainClassGiven) this.map.put(context.getClass(), context);
	}

	/**
	 * Suppressed unchecked warning because the add method ensures that the cast
	 * will always be successful.
	 * 
	 * @param klass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final <C extends Context> C getContext(final Class<C> klass) {
		return (C) this.map.get(klass);
	}
	
	public final Collection<Context> getAllContext() {
		return this.map.values();
	}
}
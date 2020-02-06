package nl.uu.cs.iss.ga.sim2apl.core.platform;

public class PlatformNotFoundException extends Exception {
	private final String details;
	
	public PlatformNotFoundException(final String details) { 
		this.details = details;
	}
	
	@Override
	public final String toString(){ return this.details; }
	
	private static final long serialVersionUID = 1L;

}

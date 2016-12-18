package gestureListeners.interaction;

import home.Home;

import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;

/**
 * This listener is dedicated to load a home when tapped On its target.
 * 
 * @author langenhagen
 * @version 20110928
 */
public class LoadHomeTapListener implements IGestureEventListener{

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the home to load */
	Home home = null;
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Default constructor
	 * @home
	 * The <i>Home</i> to save
	 */
	public LoadHomeTapListener( Home home){
		this.home = home;
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	public boolean processGestureEvent(MTGestureEvent ge){
		home.loadState();
		return false;
	}

}

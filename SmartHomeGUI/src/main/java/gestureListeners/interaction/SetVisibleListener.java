package gestureListeners.interaction;

import org.mt4j.components.MTComponent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;

/**
 * This gestureListeners purpose is to set specified, arbitrary <i>MTComponents</i> visibility.
 * 
 * @author langenhagen
 * @version 20113009
 */
public class SetVisibleListener implements IGestureEventListener{

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the components to be set (in)visibility */
	Iterable<MTComponent> components;
	/** indicates whether to be set visibility or invisible */
	boolean visibility;
	/** indicates when to trigger the process-method */
	Integer triggerEventID;
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main constructor
	 * @param setVisible
	 * A <i>boolean</i> that indicates whether to set the components visible or invisible:<br>
	 * TRUE - set visible<br>
	 * FALSE - set invisible
	 * @param components
	 * An <i>Iterable</i> of <i>MTComponents</i> to set (in)visible
	 * @param triggerEventID
	 * An ID of an <i>GestureEvent</i> e.g. <i>GESTURE_DETECTED</i>. As an <i>Integer</i>.
	 * The listener will only be triggered, if an event with this specified ID occurs.
	 * If you want to use the listener regardless of the incoming event ID then may use the param NULL.
	 */
	public SetVisibleListener ( boolean setVisible, Iterable<MTComponent> components, Integer triggerEventID){

		this.components = components;
		this.visibility = setVisible;
		this.triggerEventID = triggerEventID;
	}
	
	/**
	 * Second constructor. This constructor activates the listener regardless of the incoming event ID.
	 * That means, that the listener will be called when a gesture starts, or ends, or will be updated.
	 * If you want to specify when exactly the event shall occur, use the main constructor.
	 * @param setVisible
	 * A <i>boolean</i> that indicates whether to set the components visible or invisible:<br>
	 * TRUE - set visible<br>
	 * FALSE - set invisible
	 * @param components
	 * An <i>Iterable</i> of <i>MTComponents</i> to set (in)visible
	 */
	public SetVisibleListener( boolean setVisible, Iterable<MTComponent> components){
		this( setVisible, components, null);
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean processGestureEvent(MTGestureEvent ge){
		
		if( triggerEventID == null || triggerEventID == ge.getId()){
			for(MTComponent c : components){
				c.setVisible(visibility);
			}
		}
		
		return false;
	}
}

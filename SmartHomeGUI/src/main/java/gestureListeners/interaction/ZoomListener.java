package gestureListeners.interaction;

import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleEvent;
import org.mt4j.util.camera.Icamera;
import org.mt4j.util.math.Vector3D;

import widgets.AbstractGUIWidget;

/**
 * This class represents an GestureEventListener, which can zoom
 * by replacing the camera along a ray. The gesture according gesture
 * can be done with two fingers.
 * 
 * @author langenhagen
 * @version 20111209
 */
public class ZoomListener implements IGestureEventListener{

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the camera that is to be moved */
	private Icamera cam;
	/** the difference between the positions of cursor 1 and cursor 2 */
	private float lastdiff;
	/** scaling factor multiplier */
	private float factor;
	
	// CONSTRUCTOS ////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main constructor.
	 * @param cam
	 * The <i>Icamera</i> which is to move back and forth.
	 * @param factor
	 * A factor that can multiply the strength of the zoom-function.
	 */
	public ZoomListener( Icamera cam, float factor){
		if( cam == null){
			System.err.println("Error in ZoomListener#ZoomListener(Icamera)! The provided camera is null.");
		}else if( factor <= 0){
			System.err.println("Error in ZoomListener#ZoomListener(Icamera)! The provided scale multiplier is 0 or negative.");
		}
		this.cam = cam;
		this.factor = factor;
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean processGestureEvent(MTGestureEvent ge){		
		
		ScaleEvent se = (ScaleEvent)ge;
		float diff =	(Math.abs( se.getFirstCursor().getCurrentEvtPosX() - se.getSecondCursor().getCurrentEvtPosX())) +
						(Math.abs( se.getFirstCursor().getCurrentEvtPosY() - se.getSecondCursor().getCurrentEvtPosY()));
		
		
		
		// Check, if the target AbstractGUIComponent is within the camera's frustrum
		if( se.getTarget() instanceof AbstractGUIWidget){
			
			Vector3D camViewDirection = cam.getViewCenterPos().getSubtracted( cam.getPosition()).normalizeLocal();
			
			//TODO
		}
		
		
		if(ge.getId() != MTGestureEvent.GESTURE_STARTED){
			cam.zoomAmount( (diff-lastdiff)*factor);
		}
			
		lastdiff = diff;

		return false;
	}
}
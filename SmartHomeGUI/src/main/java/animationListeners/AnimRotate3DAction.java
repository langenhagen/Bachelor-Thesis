package animationListeners;

import org.mt4j.components.TransformSpace;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;


import util.Axis;
import widgets.MT3DObject;


/**
 * Sets up a rotation animation around the center of a given <i>MT3DObject_old</i>-object 
 * while the axes are defined by the global coordinate space
 * 
 * @author langenhagen
 * @version 20101212
 */ 
public class AnimRotate3DAction implements IAnimationListener{
	
	private MT3DObject obj = null;	// the 3D-Object to animate
	private Axis axis;				// the axis to rotate around
	private byte dir;				// the direction in which to turn: -1: clockwise; 1: counterclockwise
	
	/**
	 * Main Constructor
	 * This the <i>AnimRotate3DAction</i>-object will know its target object, so don't care about.
	 * @param rotAxis
	 * The rotation axis: either X,Y or Z as an <i>Axis</i>-value
	 * @param clockwise
	 * Specifies, if the rotation direction shall be clockwise or counterclockwise as a <i>boolean</i>
	 */
	public AnimRotate3DAction( Axis rotAxis, boolean clockwise){
		
		axis = rotAxis;
		dir = (byte)( clockwise ? -1 : 1);
	}

	/**
	 * processes the animation Event: controls the animation
	 */
	public void processAnimationEvent(AnimationEvent event){

		if(obj == null)
			obj = (MT3DObject)event.getTarget();
		
		switch( axis){
		case X:
			obj.rotateX( obj.getPosition(), event.getDelta()*dir, TransformSpace.GLOBAL);
			break;
		case Y:
			obj.rotateY( obj.getPosition(), event.getDelta()*dir, TransformSpace.GLOBAL);
			break;
		case Z:
			obj.rotateZ( obj.getPosition(), event.getDelta()*dir, TransformSpace.GLOBAL);
			break;
		}
	}	
}

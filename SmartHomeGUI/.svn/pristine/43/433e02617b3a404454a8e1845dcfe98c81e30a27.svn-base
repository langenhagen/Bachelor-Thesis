package animationListeners;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.math.Vector3D;

import widgets.AbstractGUIWidget;


/**
 * Sets up a movement animation for a given <i>AbstractGUIComponent</i>-object 
 * while the destination is defined as a vector in global coordinate space
 * 
 * @author langenhagen
 * @version 20110103
 */ 
public class AnimMoveAction implements IAnimationListener{
	
	Vector3D origin = null;		// the vector of the target before the animation starts
	Vector3D destination;		// the vector where to move the target to
	float maxVal;				// the maximum interpolation value. since the MultiPurposeInterpolator is shitty
	
	/**
	 * Main constructor
	 * @param destination
	 * the global position to move the <i>AbstractGUIComponent</i> to
	 * @param maxVal
	 * the maxVal used to split up the Animation into steps.<br>
	 * <strongCaution:</strong> Since a <i>MultiPurposeInterpolater</i>
	 * cannot return its maximum interpolation-value,
	 * this value must have the same value as the "to"-value in the 
	 * <i>MultiPurposeInterpolater</i>-constructor for correct effects!
	 */
	public AnimMoveAction(Vector3D destination, float maxVal){ 
		this.destination = destination;
		this.maxVal = maxVal;
	}
	
	/**
	 * processes the animation Event: controls the animation
	 */
	public void processAnimationEvent(AnimationEvent event){
		        		
		if(origin==null){
			if( AbstractGUIWidget.class.isAssignableFrom( event.getTarget().getClass()) ){
				
				origin = ((AbstractGUIWidget)event.getTarget()).getPosition();
				
			}else if( MTRectangle.class.isAssignableFrom( event.getTarget().getClass()) ){
				
				MTRectangle rect = (MTRectangle)event.getTarget();
				origin = rect.getPosition( TransformSpace.GLOBAL);
			}
		}
		
		Vector3D step = new Vector3D(destination).subtractLocal( origin).divideLocal( maxVal/event.getValue());
			
		( (AbstractGUIWidget)event.getTarget()).setPosition( step.addLocal(origin));	
	}	
}
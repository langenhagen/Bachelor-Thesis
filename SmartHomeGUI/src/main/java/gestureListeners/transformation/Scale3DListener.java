package gestureListeners.transformation;

import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleEvent;
import org.mt4j.util.math.Vector3D;

/**
 * This class represents an GestureEventListener, which can scale a gui-component
 * appropriate to drawn gestures. It avoids some problems you are faced with, if you
 * use the <i>DefaultScaleAction</i> for 3D-Objects. As centerpoint, it uses the center
 * of the <i>MTComponent</i>. Although this class is mainly designed to scale objects
 * of type <i>MT3DObject</i>, nothing speaks against usage with other <i>MTComponents</i>.
 * 
 * @author langenhagen
 * @version 20110702
 */
public class Scale3DListener implements IGestureEventListener{

	/**
	 * the method which handles an incoming event.
	 */
	@Override
	public boolean processGestureEvent(MTGestureEvent ge){		

		try{
		
			float factor = ((ScaleEvent)ge).getScaleFactorX();
			((MTComponent)ge.getTarget()).scale( factor, factor, factor, Vector3D.ZERO_VECTOR, TransformSpace.LOCAL);
				
		}catch (Exception e) {
			System.err.println("Error at Scale3DListener.java...");
		}
		
		return false;
	}
}
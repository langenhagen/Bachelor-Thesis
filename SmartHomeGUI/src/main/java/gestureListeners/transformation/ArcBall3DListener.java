package gestureListeners.transformation;

import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.arcballProcessor.ArcBallGestureEvent;
import org.mt4j.util.math.Matrix;

import widgets.AbstractGUIWidget;


/**
 * This class represents a <i>GestureEventListener</i>, which can rotate an <i>MT3DObject</i>
 * appropriate to drawn gestures. It avoids some problems you are faced with, if you
 * use the <i>DefaultArcballAction</i> On <i>MT3DObject</i>-objects.
 * Although this class is mainly designed to arcball-rotate MT3DObjects, you can use
 * there should be no problem to use it with other components, as long as you can provide
 * an <i>AbstractShape</i> for the target-component.
 * 
 * @author langenhagen
 * @version 20110702
 */
public class ArcBall3DListener implements IGestureEventListener {

	/**
	 * Processes an incoming Event. And makes an 3D-Object of type <i>MT3DObject</i> rotate 
	 * according to drawn gestures around the center-point of the biggest mesh of the 3D-object.
	 * @param ge
	 * The incoming gesture Event as an <i>MTGestureEvent</i>
	 */
	@Override
	public boolean processGestureEvent(MTGestureEvent ge){
		AbstractGUIWidget obj = (AbstractGUIWidget)ge.getTarget();
		Matrix m = ((ArcBallGestureEvent)ge).getTransformationMatrix().toRotationMatrix();
		Matrix.toRotationAboutPoint( m, obj.getPosition());
		obj.transform( m);
		return false;
	}
}

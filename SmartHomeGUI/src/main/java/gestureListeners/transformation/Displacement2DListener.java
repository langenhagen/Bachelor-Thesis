package gestureListeners.transformation;

import org.mt4j.components.MTComponent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;

/**
 * This class is a gesture listener which is able to move an arbitrary GUI-component parallel to the 
 * view-plane with one fingertip.
 * TODO: This class also writes the postions of the tipped component into a file
 * which later can be viewed oder parsed. This allows you to change the position 
 * of your GUI-components at runtime, making the user the designer.
 * 
 * @author langenhagen
 * @version 20110702
 */
public class Displacement2DListener implements IGestureEventListener {

	/**
	 * the handler processing an incoming event
	 */
	@Override
	public boolean processGestureEvent(MTGestureEvent ge){
		switch(ge.getId()){
		case MTGestureEvent.GESTURE_STARTED:
		case MTGestureEvent.GESTURE_UPDATED:
			((MTComponent)ge.getTarget()).translateGlobal( ((DragEvent)ge).getTranslationVect());
			break;
		}
		return false;
	}
}

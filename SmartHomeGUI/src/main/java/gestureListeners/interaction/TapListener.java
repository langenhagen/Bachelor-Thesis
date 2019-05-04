package gestureListeners.interaction;

import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;

import util.Mode;
import widgets.AbstractGUIWidget;

/**
 * This class is used for standard input detection 
 * and style management an arbitrary <i>AbstractGUIComponent</i>.
 * 
 * @author langenhagen
 * @version 20110530
 */
public class TapListener implements IGestureEventListener {
	
	@Override
	public boolean processGestureEvent(MTGestureEvent ge){

		AbstractGUIWidget obj = (AbstractGUIWidget)ge.getTarget();
		
		if( !obj.isEnabled())
			return false;
		
		switch (ge.getId()){
		case MTGestureEvent.GESTURE_STARTED:	// On clicking
			obj.setMode( Mode.TAP);
			break;
		case MTGestureEvent.GESTURE_ENDED:		// On letting go
			obj.setMode( Mode.NORMAL);
			break;
		}
		return false;
	}
}
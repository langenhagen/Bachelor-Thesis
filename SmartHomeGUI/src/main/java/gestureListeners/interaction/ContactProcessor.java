package gestureListeners.interaction;

import org.mt4j.MTApplication;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.globalProcessors.AbstractGlobalInputProcessor;
import org.mt4j.sceneManagement.Iscene;

/**
 * This class provides a global input processor which tracks every
 * cursor-input and logs information according to this input,
 * like the position, where the cursor is at the moment, if some objects 
 * are tapped and so On.
 * This implementation is solely intended to support Ali Nazari with his work.
 * 
 * TODO; finish
 * 
 * @author langenhagen
 * @version 20110702
 */
public class ContactProcessor extends AbstractGlobalInputProcessor{

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** The app */
	private MTApplication app;
	/** The scene */
	private Iscene scene;
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructor #1
	 * @param mtApp
	 * The current application as an <i>MTApplication</i>
	 * @param currentScene
	 * the scene On which to register this global input processor as an <i>Iscene</i>
	 */
	public ContactProcessor( MTApplication mtApp, Iscene currentScene){
		app = mtApp;
		scene = currentScene;
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This method handles incoming events.
	 * @param inputEvent
	 * An incoming event of type <i>MTInputEvent</i>
	 */
	@Override
	public void processInputEvtImpl(MTInputEvent inputEvent){
		
		if ( ! (inputEvent instanceof AbstractCursorInputEvt))
			return;
		
		AbstractCursorInputEvt event = (AbstractCursorInputEvt)inputEvent;
		InputCursor cursor = ((AbstractCursorInputEvt)inputEvent).getCursor();
		
		event.getTimeStamp();	// TIMESTAMP
		
		switch( event.getId()){
		case AbstractCursorInputEvt.INPUT_STARTED:
			break;
		case AbstractCursorInputEvt.INPUT_UPDATED:
			break;
		case AbstractCursorInputEvt.INPUT_ENDED:
			break;
		}
		
	}

	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Logs the specified string and writes the information into an xml-file.
	 * @infoType
	 * The type of information to be stored, e.g. cursorPos, targetComponent, what you want.
	 * @param string
	 * The text to log as a <i>String</i>.
	 */
	private void log(String infoType, String string){
		// TODO
	}
}
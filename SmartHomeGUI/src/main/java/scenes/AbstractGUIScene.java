package scenes;

import java.awt.event.KeyEvent;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

/**
 * This class represents the base class for all GUI-scenes in the 3D-GUI context. 
 * It defines a common look and feel and provides simple keyboard handling.
 * It also augments the standard scene interface with some Helper-function,
 * which are pretty messed up in MT4j.
 * 
 * @author langenhagen
 * @version 20110930
 */
public abstract class AbstractGUIScene extends AbstractScene{

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the initial camera position */
	protected Vector3D initCamPos;
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main Constructor #1
	 * @param mtApp
	 * The corresponding application as an <i>MTApplication</i>.
	 * @param name
	 * The name of the scene as a String.
	 * @param bgcolor
	 * The background color of this scene as an <i>MTColor</i>.
	 * @param showCursor
	 * Specifies, if an icon for the cursor shall be shown as a <i>boolean</i>.
	 */
	public AbstractGUIScene(
			MTApplication mtApp,
			String name,
			MTColor bgColor,
			boolean showCursor){
		
		super(mtApp, name);
		
		// set background color
		setClearColor( bgColor);

		// set Background TODO: Set me O,�
		//MTBackgroundImage bg = new MTBackgroundImage( mtApp, GUITextures.instance().getAppBG(), true);
		
		//this.getCanvas().addChild( bg);
		
		// shows cursors?
		if( showCursor)
			this.registerGlobalInputProcessor( new CursorTracer(mtApp, this));
		
		// if OpenGL is disabled or the Settings.txt is missing
		if(!(MT4jSettings.getInstance().isOpenGlMode())){
			System.err.println(this.getClass().getName() + " Programm runs only in OpenGL-Mode.");
			System.err.println("Or Maybe the Settings.txt-File is missing?");
			return;
		}
	} // END Main Constructor #1
	
	/**
	 * Constructor #2<br>
	 * Creates an AbstractGUIScene-Instance with the standard background-color and
	 * with an icon for the cursor.
	 * 
	 * @param mtApp
	 * The corresponding application as an <i>MTApplication</i>.
	 * @param name
	 * The name of the scene as a String.
	 */
	public AbstractGUIScene(MTApplication mtApp, String name){
		this( mtApp, name, new MTColor(40,40,40,255), true);
	}
	
	
	// PUBLIC METHOS //////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Initialies the scene. 
	 * Doesn't have to be called explicitly.
	 */
	//@Override
	public void init(){
		getMTApplication().registerKeyEvent(this);
		initCamPos = this.getSceneCam().getPosition();
	}

	/**
	 * Shuts the scene down. 
	 * Doesn't have to be called explicitly.
	 */
	//@Override
	public void shutDown(){
		getMTApplication().unregisterKeyEvent(this);
	}
	
	/**
	 * Removes all gestures from the given MTComponent.
	 * @param component
	 * The <i>MTComponent</i> to remove all <i>InputProcessors</i> and <i>GestureEventListeners</i> from.
	 */
	void clearAllGestures(MTComponent component){
		component.unregisterAllInputProcessors();
		component.removeAllGestureEventListeners();
	}
	
	/**
	 * Gets a child of the canvas of this scene by its name.
	 * Not like the getCanvas().getChildByName()-method,
	 * this method looks in the scenegraph recursively,
	 * giving you the freedom you need to find items
	 * in a bigger scene graph-hierarchy.
	 * 
	 * @param name
	 * The name of the component as a <i>String</i>.
	 * @return
	 * The (maybe indirect) child of the canvas with the specified name as an <i>MTComponent</i>.
	 */
	public MTComponent getComponentByName(String name){

		for( MTComponent c : this.getCanvas().getChildren()){
			MTComponent child = getChildRecursive( c, name);
			if( child != null)
				return child;
		}
		return null;
	}

	// KEYBOARD INPUT /////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Manages the keyboard input, mainly for debugging reasons.
	 * @param e
	 * The <i>KeyEvent</i> that happened.<br><br>
	 * 
	 * At the moment, following keys commands can be used:<br>
	 * F - console output framerate<br>
	 * + - zoom in<br>
	 * - - zoom out<br>
	 * Arrow keys - move cam<br>
	 * R - reset camera postition to initial one<br>
	 * F12 - make screenshot<br>
	 * Q - quit application<br>
	 * 
	 */
	public void keyEvent(KeyEvent e){

		if (e.getID() != KeyEvent.KEY_PRESSED)
			return;
		switch (e.getKeyCode()){
		case KeyEvent.VK_F:
			System.out.println("FPS: " + getMTApplication().frameRate);
			break;
		case KeyEvent.VK_PLUS:
			this.getSceneCam().zoomAmount( 10);
			break;
		case KeyEvent.VK_MINUS:
			this.getSceneCam().zoomAmount( -10);
			break;
		case KeyEvent.VK_UP:
			Vector3D oldposUp = this.getSceneCam().getPosition();
			this.getSceneCam().setPosition( 
					new Vector3D(
							oldposUp.getX(),
							oldposUp.getY() - 20,
							oldposUp.getZ()
							));
			break;
		case KeyEvent.VK_DOWN:
			Vector3D oldposDown = this.getSceneCam().getPosition();
			this.getSceneCam().setPosition( 
					new Vector3D(
							oldposDown.getX(),
							oldposDown.getY() + 20,
							oldposDown.getZ()
							));
			break;
		case KeyEvent.VK_LEFT:
			Vector3D oldposLeft = this.getSceneCam().getPosition();
			this.getSceneCam().setPosition( 
					new Vector3D(
							oldposLeft.getX() - 20,
							oldposLeft.getY(),
							oldposLeft.getZ()
							));
			break;
		case KeyEvent.VK_RIGHT:
			Vector3D oldposRight = this.getSceneCam().getPosition();
			this.getSceneCam().setPosition( 
					new Vector3D(
							oldposRight.getX() + 20,
							oldposRight.getY(),
							oldposRight.getZ()
							));
			break;
		case KeyEvent.VK_R:
			this.getSceneCam().setPosition( initCamPos);
			break;
		case KeyEvent.VK_F12:
			getMTApplication().saveFrame();
			break;
		case KeyEvent.VK_Q:
			System.exit( 0);
		default:
			break;
		}
		
		// let subclasses manage keyEvents
		keyEventHelper(e);
	}
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Enables augmenting keyboard input by subclasses.
	 * This method will be called after keyEvent().<br>
	 * Note: This method will only be called if the
	 * event id of the given <i>KeyEvent</i> is KeyEvent.KEY_PRESSED.
	 * @param e
	 * The <i>KeyEvent</i> that happened.
	 */
	protected abstract void keyEventHelper(KeyEvent e);
	
	/**
	 * Looks for a special named (maybe indirect) child of the specified component.
	 * @param parent
	 * The parent of the component you are looking for as an <i>MTComponent</>.
	 * @param name
	 * The name of the child to look for as a <i>String</>.
	 * @return 
	 * The <i>MTComponent</i> with the given name, or NULL, if no such child exists.
	 */
	private MTComponent getChildRecursive( MTComponent parent, String name){

		for(MTComponent c : parent.getChildren()){
			if( c.getName().equals( name)){
				return c;
			}else{
				MTComponent child = getChildRecursive( c, name);
				if(child != null)
					return child;
			}
		}
		return null;
	}
}
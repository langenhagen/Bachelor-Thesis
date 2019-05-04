package home;


import org.mt4j.components.MTComponent;
import org.mt4j.input.inputProcessors.componentProcessors.AbstractComponentProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.util.math.Vector3D;
import org.sercho.masp.models.Context.PhysicalDevice;

import provider.GUI3DModels;

import util.DeviceType;
import widgets.MT3DObject;


import gestureListeners.transformation.CentricRotation3DListener;
import gestureListeners.transformation.Displacement3DPlaneListener;
import gestureListeners.transformation.Displacement3DLineListener;
import gestureListeners.transformation.Rotation3DListener;
import gestureListeners.transformation.Scale3DListener;
import home.deviceCommands.DeviceCommand;
import home.deviceCommands.factories.CommandFactory;
import home.stateVisualizers.DummyVisualizer;
import home.stateVisualizers.StateVisualizer;
import home.stateVisualizers.factories.StateVisualizerFactory;


/**
 * This class represents an itemID in the smart home context.
 * It aggregates the physical device which is provided through
 * Grzegorz Lehmann's Model API and its view in the program.
 * The itemID also stores <i>Commands</i> which provide functionality
 * when interacting with the itemID within the gui. Items also carry
 * a <i>StateVisualizer</i> which manages the appearance of the
 * itemID depending On their state.
 * 
 * @author langenhagen
 * @version 20120512
 */
public class Item {
	
	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** a friendly name for this itemID */
	private String name;
	/** the home to which this item belongs */
	private Home home;
	/** the physical device of this item */
	private PhysicalDevice device;
	/** the command to be executed when interacting */
	private DeviceCommand command;
	/** the state visualizer of this itemID */
	private StateVisualizer stateVisualizer;
	/** the view af this Item */
	private MT3DObject view;
	/** The axis of the plane On which items can be moved in 3D-space */
	private Vector3D movingplanenormal;
	/** indicates if edit mode is On */
	private boolean editMode = false;
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main constructor.<br>
	 * Creates an item with the 3D object accordint to the physical device's type.
	 * Adds commands and state visualizers.
	 * @param name
	 * A friendly name as a <i>String</i>
	 * @param device
	 * The <i>PhysicalDevice</i> of this itemID.
	 * @param home
	 * The home tho which this item belongs to. It will not be used
	 * directly by the item but can be used by clients of the item.
	 */
	public Item( String name, PhysicalDevice device, Home home){
		
		MT3DObject obj = null;
		DeviceType type = DeviceType.getDeviceType( device);
    	
    	obj = GUI3DModels.instance().getModel( type.toString(), 0, 1000);
    	if(obj == null)
    		obj = GUI3DModels.instance().getModel( "defaultDevice", 0, 1000);
    	
		setName( name);
		setDevice( device);
		setView( obj);
		setHome( home);

		StateVisualizerFactory.instance().createStateVisualizer( this, type, true);
		CommandFactory.instance().createCommand(this, device, true);
	}
	
	/**
	 * Constructor #2.<br>
	 * Creates an item with the 3D object accordint to the physical device's type.
	 * Adds commands and state visualizers. Sets the home to null.
	 * @param name
	 * A friendly name as a <i>String</i>
	 * @param device
	 * The <i>PhysicalDevice</i> of this itemID.
	 * @param home
	 * The home tho which this item belongs to.
	 */
	public Item( String name, PhysicalDevice device){
		this(name, device, null);
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Updates the state of the physical device and therefore 
	 * the view according to the itemID's state-visualizer.
	 */
	public void update(){
		
		this.getStateVisualizer().notifyVisualizer();
	}
	
	// GETTERS & SETTERS //////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the ID of the corresponding physical device.
	 * @return
	 * the ID of the corresponding <i>PhysicalDevice</i> as a <i>String</i>.
	 */
	public String getID(){
		return device.getId();
	}
	
	/**
	 * Sets the home.
	 * @param home
	 * The home tho which this item belongs to. It will not be used
	 * directly by the item but can be used by clients of the item.
	 */
	public void setHome( Home home){
		this.home = home;
	}
	
	/**
	 * Retrieves the home that corresponds to this item.
	 * @return
	 * The corresponding <i>Home</i>.
	 */
	public Home getHome(){
		return home;
	}
	
	/**
	 * Sets a friendly name for this itemID.
	 * @param name
	 * The new name as a <i>String</i>.
	 */
	public void setName( String name){
		this.name = name;
	}
	
	/**
	 * Retrieves the friendly name of this itemID
	 * @return
	 * The itemID's name as a <i>String</i>
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Sets the axis of the moving plane On
	 * which this itemID can be translated & rotated (in global coordinates)
	 * @param normal
	 * The axis vector of the moving-plane as a <i>Vector3D</i>
	 */
	public void setMovingPlaneNormal( Vector3D normal){
		setMovingPlaneNormal( normal, null);
	}
	
	/**
	 * Sets the axis of the moving plane On
	 * which this itemID can be translated & rotated
	 * @param normal
	 * The axis vector of the moving-plane as a <i>Vector3D</i>
	 */
	public void setMovingPlaneNormal( Vector3D normal, MTComponent comp){
		
		// prevent double gestureListeners
		view.removeListenerType( DragProcessor.class, Displacement3DPlaneListener.class);
		view.removeListenerType( RotateProcessor.class, CentricRotation3DListener.class);
		view.removeListenerType( RotateProcessor.class, Rotation3DListener.class);
		view.removeListenerType( ScaleProcessor.class, Scale3DListener.class);
		
		if(editMode){
			view.addGestureListener( ScaleProcessor.class, new Scale3DListener());
			view.addGestureListener( RotateProcessor.class, new CentricRotation3DListener( normal, comp));
			view.addGestureListener( DragProcessor.class, new Displacement3DPlaneListener( normal, comp));
		}
		
		movingplanenormal = normal;
	}
	
	/**
	 * Retrieves the axis of the moving plane On
	 * which items can be moved along in the smart home.
	 * @return
	 * The axis vector of the moving-plane as a <i>Vector3D</i>
	 */
	public Vector3D getMovingPlaneNormal(){
		return movingplanenormal;
	}
	
	/**
	 * Sets the state visualizer.
	 * @param stateVisualizer
	 * A <i>StateVisualizer</i> or NULL. If you set the parameter NULL, an <i>DummyVisualizer</i>
	 * will be installed automatically. This <i>DummyVisualizer</i> does in fact nothing, but avoids
	 * null-pointer exceptions.
	 */
	public void setStateVisualizer( StateVisualizer stateVisualizer){
		if(stateVisualizer == null){
			this.stateVisualizer = new DummyVisualizer( this);
		}
		else{
			this.stateVisualizer = stateVisualizer;
		}
	}
	
	/**
	 * Gets the state visualizer.
	 * @return
	 * The corresponding <i>StateVisualizer</i>
	 */
	public StateVisualizer getStateVisualizer(){
		return stateVisualizer;
	}
	
	/**
	 * Sets the view. If necessary, registers DragProcessor, RotateProcessor and ScaleProcessor
	 * On the new view. Doesn't unregister the processors On the old view!
	 * @param obj
	 * An <i>MT3DObject</i> as the new view
	 */
	public void setView( MT3DObject obj){
		
		// beware of double InputProcessors
		AbstractComponentProcessor[] procs = obj.getInputProcessors();
		for( AbstractComponentProcessor proc : procs)
			if(proc instanceof DragProcessor || proc instanceof RotateProcessor || proc instanceof ScaleProcessor)
				obj.unregisterInputProcessor( proc);
		
		obj.registerInputProcessor( new ScaleProcessor( obj.getRenderer()));
		obj.registerInputProcessor( new RotateProcessor( obj.getRenderer()));
		obj.registerInputProcessor( new DragProcessor( obj.getRenderer()));
		
		view = obj;
	}
	
	/**
	 * Gets the view.
	 * @return
	 * An <i>MT3DObject</i>
	 */
	public MT3DObject getView(){
		return view;
	}
	
	/**
	 * Gets the physical device
	 * @return
	 * Returns the <i>PhysicalDevice</i> which is attached On this <i>Item</i>.
	 */
	public PhysicalDevice getDevice(){
		return device;
	}
		
	/**
	 * Sets the command for this item.
	 * @param command
	 * The <i>DeviceCommand</i>
	 */
	public void setCommand( DeviceCommand command){
		this.command = command;
	}
	
	/**
	 * Returns the command of this itemID
	 * @return
	 * A <i>DeviceCommand</i>
	 */
	public DeviceCommand getCommand(){
		return command;
	}
	
	/**
	 * Activates or deactivates a scale-listener On this item.
	 * @param mode
	 * A <i>boolean</i> value, where TRUE activates the listener
	 * and FALSE deactivates it
	 */
	public void setScaleMode( boolean mode){
		view.removeListenerType( ScaleProcessor.class, Scale3DListener.class);
		if( mode)
			view.addGestureListener( ScaleProcessor.class, new Scale3DListener());
	}
	
	/**
	 * Activates or deactivates a Displacement3D-listener On this item
	 * @param mode
	 * A <i>boolean</i> value, where TRUE activates the listener
	 * and FALSE deactivates it
	 */
	public void setMovePlanarMode( boolean mode){
		view.removeListenerType( DragProcessor.class, Displacement3DPlaneListener.class);
		if( mode)
			view.addGestureListener( DragProcessor.class, new Displacement3DPlaneListener(this.movingplanenormal));
	}
	
	/**
	 * Activates or deactivates a Displacement3DVertical-listener On this item.
	 * @param mode
	 * A <i>boolean</i> value, where TRUE activates the listener
	 * and FALSE deactivates it
	 */
	public void setMoveVerticalMode( boolean mode){
		view.removeListenerType( DragProcessor.class, Displacement3DPlaneListener.class);
		view.removeListenerType( DragProcessor.class, Displacement3DLineListener.class);
		if( mode)
			view.addGestureListener( DragProcessor.class, new Displacement3DLineListener(this.movingplanenormal));
	}
	
	/**
	 * Activates or deactivates a Rotation3D-listener On this item.
	 * @param mode
	 * A <i>boolean</i> value, where TRUE activates the listener
	 * and FALSE deactivates it
	 */
	public void setRotateMode( boolean mode){
		view.removeListenerType( RotateProcessor.class, CentricRotation3DListener.class);
		if( mode)
			view.addGestureListener( RotateProcessor.class, new CentricRotation3DListener(this.movingplanenormal));
	}
	
	/**
	 * Switches between the edit mode, in which you can translate and rotate the itemID
	 * and the interaction mode, in which you can interact with the itemID as defined in its command.
	 * @param editMode
	 * TRUE - sets edit mode<br>
	 * FALSE - sets interaction mode
	 */
	public void setEditMode( boolean editMode){
		
		// removal of _ALL_ relevant gestureListeners
		view.removeListenerType( DragProcessor.class, Displacement3DPlaneListener.class);
		view.removeListenerType( ScaleProcessor.class, Scale3DListener.class);
		view.removeListenerType( RotateProcessor.class, CentricRotation3DListener.class);
		
		// if edit mode
		if(editMode){
			view.addGestureListener( ScaleProcessor.class, new Scale3DListener());
			view.addGestureListener( DragProcessor.class, new Displacement3DPlaneListener( movingplanenormal));
			view.addGestureListener( RotateProcessor.class, new CentricRotation3DListener( movingplanenormal));
		}
		// if interaction mode
		else{
			
			//TODO
		}
		
		this.editMode = editMode;
	}
	
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Sets the physical device.
	 * @param device
	 * The <i>PhysicalDevice</i> which shall be attached On this <i>Item</i>.
	 */
	private void setDevice( PhysicalDevice device){
		this.device = device;
	}
	
}

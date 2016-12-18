package home;

import gestureListeners.transformation.Displacement3DPlaneListener;
import gestureListeners.transformation.Rotation3DListener;
import home.stateReadersWriters.StateReader;
import home.stateReadersWriters.StateWriter;

import java.util.HashMap;
import java.util.Map;


import org.mt4j.components.MTComponent;
import org.mt4j.input.inputProcessors.componentProcessors.AbstractComponentProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;

import org.sercho.masp.models.Context.Device;
import org.sercho.masp.models.Context.Environment;
import org.sercho.masp.models.Context.PhysicalDevice;

import widgets.MT3DObject;



/**
 * This class represents the smart home itself 
 * in the gui context. It stores its items and its own view.
 * The home also stores the axis vector of the plane,
 * on which the items - and the camera - can be moved in 3-dimensional space.
 * 
 * @author langenhagen
 * @version 20110919
 */
public class Home {	
	
	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** Sets a unique identification-name for this home */
	private final String name;
	/** the environment which corresponds to this home */
	private Environment environment;
	/** All Items and their corresponding IDs*/
	private Map<String,Item> items;
	/** the active itemID */
	private Item activeItem = null;
	/** the view of the smart home */
	private MT3DObject view;	
	/** The axis of the plane On which items can be moved in 3D-space */
	private Vector3D movingplanenormal = null;
	/** the reader which can load the home's state */
	private StateReader reader = null;
	/** the writer which can save the home's state */
	private StateWriter writer = null;
	/** the initial position of the home's view */
	private Vector3D initialPosition;
	/** describes the maximum movement radius with drag gestures of the home's view */
	private float maxMovementRadius = 100000;
	
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main constructor.
	 * @param name
	 * The name of this home as a <i>String</i>.
	 * @param environment
	 * The corresponding <i>Environment</i>.
	 * @param view
	 * The view as an MT3DObject.
	 */
	public Home( String name, Environment environment, MT3DObject view){
		
		this.name = name;
		this.environment = environment;
		this.items = new HashMap<String, Item>();
		
		this.setView( view);
		
		// Process every device
		for( Device d : environment.getDevices()){
        	if(d instanceof PhysicalDevice){
        		processDevice( (PhysicalDevice)d );
        	}
        }
	}
	

	/**
	 * Constructor #2.
	 * @param name
	 * The name of this home as a <i>String</i>.
	 * @param environment
	 * The corresponding <i>Environment</i>.
	 * @param view
	 * The view as an MT3DObject.
	 * @param movingplanenormal
	 * The normal vector of the moving-plane as a <i>Vector3D</i>.
	 */
	public Home( String name, Environment environment, MT3DObject view, Vector3D movingplanenormal){
		this( name, environment, view);
		setMovingPlaneNormal( movingplanenormal);
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Loads the state (meaning the position-offsets of the items
	 * and such stuff) of this home according to the set <i>StateReader</i>.<br>
	 * <strong>Caution:</strong> In order to provoke no crash, a valid StateReader
	 * has to be set with the method setStateReader()!
	 * @return
	 * Returns TRUE, if loading was successful, otherwise returns FALSE.
	 */
	public boolean loadState(){
		return reader.load( this);
	}
	
	/**
	 * Saves the state (meaning the position-offsets of the items
	 * and such stuff) of this home according to the set <i>StateWriter</i>.
	 * <strong>Caution:</strong> In order to provoke no crash, a valid StateWriter
	 * has to be set with the method setStateWriter()!.
	 * @return
	 * Returns TRUE, if saving was successful, otherwise returns FALSE.
	 */
	public boolean saveState(){
		return writer.save( this);
	}
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Sets the view.
	 * @param obj
	 * The new view as an <i>MT3DObject</i>.
	 */
	private void setView( MT3DObject obj){
		
		// behold of double InputProcessors
		AbstractComponentProcessor[] procs = obj.getInputProcessors();
		for( AbstractComponentProcessor proc : procs)
			if(proc instanceof DragProcessor || proc instanceof RotateProcessor)
				obj.unregisterInputProcessor( proc);
		
		obj.registerInputProcessor( new DragProcessor( obj.getRenderer()));
		obj.registerInputProcessor( new RotateProcessor( obj.getRenderer()));
		
		view = obj;
		
		//TODO: delegate new axis to items
	}

	/**
	 * Processes a device for the constructor, 
	 * it creates an corresponding <i>Item</i>
	 * and sets up the corresponding functionality.
	 * @param device
	 * The <i>PhysicalDevice</i> to process.
	 * 
	 */
	private void processDevice(PhysicalDevice device) {
		
    	Item item = new Item(device.getName(), device, this);
    	
		item.getView().setLight( view.getLight());
    	this.addItem( item);
	}
	
	/**
	 * Adds an item to the set of items and adds a <i>TapProcessor</i>,
	 * if necessery. If a moving-plane is specified, also adds a
	 * <i>DragProcessor</i> and <i>RotateProcessor</i>, but doesn't
	 * create redundant ones.
	 * @param item
	 * A new domestic <i>Item</i> to be added.
	 */
	private void addItem( Item item){
		
		// register TapProcessor, if necessary
		MT3DObject obj = item.getView();
		for( AbstractComponentProcessor p : obj.getInputProcessors()){
			if( p instanceof TapProcessor){
				obj.unregisterInputProcessor( p);
			}
		}
		
		obj.registerInputProcessor( new TapProcessor( obj.getRenderer()));
		
		item.setMovingPlaneNormal( movingplanenormal);
		items.put( item.getDevice().getId(), item);
	}
	
	// GETTERS & SETTERS //////////////////////////////////////////////////////////////////////////
	
	/**
	 * Gets the view
	 * @return obj
	 * The view as an <i>MT3DObject</i>
	 */
	public MT3DObject getView(){
		return view;
	}
	
	/**
	 * Retrieves the item with the given name,
	 * if it doesn't exist, null will be returned.
	 * @param id
	 * The ID of the <i>Item</i>'s <i>PhysicalDevice</i> as a <i>String</i>.
	 * @return
	 * Returns an <i>Item</i> or NULL if an item
	 * with the specified name doesn't exist.
	 */
	public Item getItem( String id){
		return items.get( id);
	}
	
	/**
	 * Returns all items in this home.
	 * @return
	 * All items as an <i>Iterable</i> of type <i>Item</i>.
	 */
	public Iterable<Item> getAllItems(){
		return items.values();
	}
	
	/**
	 * Removes the itemID from the set of items.
	 * @param id
	 * The ID of the <i>Item</i>'s <i>PhysicalDevice</i> as a <i>String</i>.
	 * @return
	 * Thw Item associated with the ID, 
	 * or null if there was no mapping for id.
	 */
	public Item removeItem( String id){
		return items.remove( id);
	}
	
	/**
	 * Sets the axis of the moving plane On
	 * which items can be moved along in the smart home (in global coordinates).
	 * This axis also makes translations rotations around this normal possible.
	 * @param normal
	 * The axis vector of the moving-plane as a <i>Vector3D</i>.
	 */
	public void setMovingPlaneNormal( Vector3D normal){
		 setMovingPlaneNormal( normal, null);
	}
	
	/**
	 * Sets the axis of the moving plane On
	 * which items can be moved along in the smart home.
	 * This axis also makes translations rotations around this normal possible.
	 * @param normal
	 * The axis vector of the moving-plane as a <i>Vector3D</i>.
	 * @param comp
	 * An <i>MTComponent</i> to which the normal is relative to. Can be NULL for global coordinates.
	 */
	public void setMovingPlaneNormal( Vector3D normal, MTComponent comp){

		// prevent double gestureListeners
		view.removeListenerType( DragProcessor.class, Displacement3DPlaneListener.class);
		view.removeListenerType( RotateProcessor.class, Rotation3DListener.class);
		 
		movingplanenormal = normal;

		view.addGestureListener( DragProcessor.class, new Displacement3DPlaneListener( normal, comp).
				setHome( this).setMovementRadius(maxMovementRadius));
		view.addGestureListener( RotateProcessor.class, new Rotation3DListener( normal, comp).setHome( this));
		
		for( Item i : items.values())
			i.setMovingPlaneNormal( movingplanenormal);
	}
	
	/**
	 * Retrieves the axis of the moving plane On
	 * which items can be moved along in the smart home.
	 * @return
	 * The axis vector of the moving-plane as a <i>Vector3D</i>.
	 */
	public Vector3D getMovingPlaneNormal(){
		return movingplanenormal;
	}

	/**
	 * Sets the reader, which can load this home's state.
	 * @param reader
	 * The reader as a <i>StateReader</i>.
	 */
	public void setStateReader( StateReader reader){
		this.reader = reader;
	}
	
	/**
	 * Sets the writer, which can save this home's state.
	 * @param writer
	 * The writer as a <i>StateReader</i>.
	 */
	public void setStateWriter( StateWriter writer){
		this.writer = writer;
	}
	
	/**
	 * Retrieves the name-identifier of this home.
	 * @return
	 * the home's name as a <i>String</i>.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Sets the active itemID of this home.
	 * @param itemID
	 * The name of the itemID as a <i>String</i>.
	 * If the given string doesn't belong to an itemID
	 * in this home or is empty, the active itemID will be set to NULL.
	 */
	public void setActiveItem( String itemID){
		if(itemID.equals( "")){
			activeItem = null;
		}else{
			activeItem = this.getItem( itemID);
		}
	}
	
	/**
	 * Retrieves the active item.
	 * @return
	 * The active itemID as an <i>Item</i> or NULL, if there is no active item.
	 */
	public Item getActiveItem(){
		return activeItem;
	}
	
	/**
	 * Retrieves the corresponding Environment.
	 * @return
	 * The corresponding <i>Environment</i>
	 */
	public Environment getEnvironment(){
		return environment;
	}
	
	/**
	 * Sets the maximum movement radius in which the home can be moved around.
	 * Also forwards this radius to its items.
	 * @param radius
	 * The maximum movement radius in global coordinates as a <i>float</i>.
	 */
	public void setMaxMovementRadius( float radius){
		maxMovementRadius = radius;
	}
	
	/**
	 * Retrieves the maximum movement radius in which the home can be moved around.
	 * @return
	 * The maximum movement radius in global coordinates as a <i>float</i>.
	 */
	public float getMaxMovementRadius(){
		return maxMovementRadius;
	}
	
	/**
	 * Sets the initial position of the home's view.
	 * @param pos
	 * The initial position as a <i>Vector3D</i>.
	 */
	public void setInitialPosition( Vector3D pos){
		initialPosition = pos;
	}
	
	/**
	 * Gets the initial position of the home's view.
	 * @return
	 * The initial position as a <i>Vector3D</i>. 
	 */
	public Vector3D getInitialPosition(){
		return initialPosition;
	}
	
}
package gestureListeners.transformation;

import home.Home;
import home.Item;

import org.mt4j.components.MTComponent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.AbstractComponentProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateEvent;
import org.mt4j.util.math.Matrix;
import org.mt4j.util.math.Plane;
import org.mt4j.util.math.Ray;
import org.mt4j.util.math.Tools3D;
import org.mt4j.util.math.Vector3D;

import util.MathUtil;
import widgets.AbstractGUIWidget;


/**
 * This class provides ways and means to rotate an <i>AbstractGUIComponent</i> in 3-dimensional space
 * around an arbitrary vector. The rotation-axis will be defined through a given vector 
 * with its origin in the middle of the user's fingers On a virtual plane defined by the 
 * given vector the position-coordinates of the Object to be rotated.
 * 
 * @author langenhagen
 * @version 20120204
 */
public class Rotation3DListener implements IGestureEventListener {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the axis of the repositioning-plane */
	private Vector3D axis;
	/** the position-matrix of its target */
	private Matrix posm;
	/** the position matrix of its target, where the position is negative */
	private Matrix nposm;
	/** XXX: this is some sort of hack: because some mt4j jerk fucked up the composite fuckup, i have to fuckup the upfuck by my fucking self */
	private Home home = null;
	/** indicates, that a drag processor belongs to the target */
	boolean hasDragProcessor = false;
	/** The vector between the two cursors in the last triggered event */
	Vector3D rotationVector0;

	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructor #1
	 * Creates a new listener with the speceified plane axis relative to 
	 * the given component's space. If the given component is null,
	 * the listener just user global coordinate space.
	 * @param axis
	 * The rotation-axis' direction vector as a <i>Vector3D</i>
	 * @param comp
	 * The component specifying the coordinate space as an <i>MTComponent</i>
	 */
	public Rotation3DListener( Vector3D axis, MTComponent comp){
		setAxis( axis, comp);
	}
	
	/**
	 * Constructor #2<br>
	 * Creates a new listener with the specified plane axis in global coordinates.
	 * @param axis
	 * The rotation-axis' direction vector as a <i>Vector3D</i>
	 */
	public Rotation3DListener( Vector3D axis){
		this(axis, null);
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This method will be triggered when the listener will be activated.
	 */
	@Override
	public boolean processGestureEvent(MTGestureEvent ge){
				
		RotateEvent re = (RotateEvent)ge;
		AbstractGUIWidget obj = (AbstractGUIWidget)re.getTarget();
		Vector3D pos = obj.getPosition();
		
		try{
			
			// calculate difference vector for picking -> rotation center & offset
			Plane plane = new Plane( pos, axis);
			Ray campickray;
			
			campickray = Tools3D.getCameraPickRay( 
					obj.getRenderer(),
					obj.getViewingCamera(),
					re.getFirstCursor().getCurrentEvtPosX(),
					re.getFirstCursor().getCurrentEvtPosY());
			Vector3D cursorPos1 =  plane.getIntersectionLocal( campickray);
			
			campickray = Tools3D.getCameraPickRay( 
					obj.getRenderer(),
					obj.getViewingCamera(),
					re.getSecondCursor().getCurrentEvtPosX(),
					re.getSecondCursor().getCurrentEvtPosY());
			Vector3D cursorPos2 =  plane.getIntersectionLocal( campickray);
			
			// return in case of problem
			if( cursorPos1 == null || cursorPos2 == null){
				return false;
			}
			
			Vector3D fingerAxis = cursorPos1.getSubtracted( cursorPos2).getNormalized();
			
			switch( ge.getId()){
			
			case MTGestureEvent.GESTURE_STARTED:{
				
				// beware of DragProcessors and deregister them to prevent errrors
				for( AbstractComponentProcessor proc :  obj.getInputProcessors()){
					if(proc instanceof DragProcessor){
						hasDragProcessor = true;
						obj.unregisterInputProcessor( proc);
					}
				}
				
				rotationVector0 = fingerAxis;
				Vector3D offset = pos.getSubtracted( cursorPos1.getAdded( cursorPos2).divideLocal( 2));
				
				posm = new Matrix(	// translation matrix for rotation around the specified point
						1,	0,	0,	pos.getX() - offset.getX(),
						0,	1,	0,	pos.getY() - offset.getY(),
						0,	0,	1,	pos.getZ() - offset.getZ(),
						0,	0,	0,	1);
				nposm = new Matrix( // back-translation matrix for rotation around the specified point
						1,	0,	0,	-pos.getX() + offset.getX(),
						0,	1,	0,	-pos.getY() + offset.getY(),
						0,	0,	1,	-pos.getZ() + offset.getZ(),
						0,	0,	0,	1);
			}
			case MTGestureEvent.GESTURE_UPDATED:{
				
				// positive or negative angle?
				float clockwise = rotationVector0.getCross( fingerAxis).distance( axis.getNormalized()) > rotationVector0.getCross( fingerAxis).distance( axis.getNormalized().getInverted()) ? 1 : -1;
				
				// do the transformation
				Matrix m = posm.mult( MathUtil.createRotationMatrix( axis, (float)( clockwise * 180/Math.PI*rotationVector0.angleBetween( fingerAxis))).mult(  nposm));
				rotationVector0 = fingerAxis;
				obj.transform( m);
				
				// beware of the home instance var
				if( home != null){
					for( Item i : home.getAllItems()){
						if(i.getView() == re.getTarget()){
							home.getView().transform( m);
							continue;
						}
						i.getView().transform( m);
					}
				}
				break;
			}
			case MTGestureEvent.GESTURE_ENDED:

				// register a new Displacement3DPlaneListener
				if( hasDragProcessor){
					
					// setup new Displacement3DPlaneListener
					Displacement3DPlaneListener newListener = null;
					for( IGestureEventListener l : obj.getGestureListeners()){
						if( l instanceof Displacement3DPlaneListener){
							Displacement3DPlaneListener oldListener = (Displacement3DPlaneListener)l;
							Home home = oldListener.getHome();
							
							newListener = new Displacement3DPlaneListener( oldListener.getPlaneNormal());
							if( home != null){
								newListener.setHome( home).setMovementRadius( home.getMaxMovementRadius());
							}
						}
					}
					obj.registerInputProcessor( new DragProcessor( obj.getRenderer()));
					obj.addGestureListener( DragProcessor.class, newListener);
				}
			} // END switch
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// GETTERS & SETTERS //////////////////////////////////////////////////////////////////////////
	
	/**
	 * Sets the rotation axis according to the specified vector
	 * in the coordinate system of the specified component. 
	 * If the given component is <i>null</i>, global coordinaes are assumed.
	 * @param axis
	 * The rotation-axis' direction vector as a <i>Vector3D</i>
	 * @param comp
	 * The component specifying the coordinate space as an <i>MTComponent</i>
	 */
	public void setAxis( Vector3D axis, MTComponent comp){
		if( comp != null)
			this.axis = comp.localToGlobal( axis).normalizeLocal();
		else
			this.axis = axis.normalizeLocal();
	}
	
	/**
	 * Sets the rotation axis according to the specified vector
	 * in global coordinate space.
	 * @param axis
	 * The rotation-axis' direction vector as a <i>Vector3D</i>.
	 */
	public void setAxis( Vector3D axis){
		setAxis( axis, null);
	}
		
	/**
	 * Retrieves the repositioning-plane's axis used in this listener 
	 * in the coordinates specified with setAxis(), or in the constructor.
	 * @return
	 * The rotation axis as a <i>Vector3D</i>.
	 */
	public Vector3D getAxis(){
		return axis;
	}
	
	/**
	 * If you bind this listener On a home,
	 * you can forward this home to the listener,
	 * to manage movement of all items in the home,
	 * although they are no real children.
	 * @param home
	 * The home as a <i>Home</i>.
	 * @return
	 * Returns itself.
	 */
	public Rotation3DListener setHome( Home home){
		this.home = home;
		return this;
	}
}
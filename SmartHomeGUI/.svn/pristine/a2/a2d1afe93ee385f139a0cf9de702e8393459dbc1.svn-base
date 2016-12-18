package gestureListeners.transformation;

import org.mt4j.components.MTComponent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.util.camera.Icamera;
import org.mt4j.util.math.Plane;
import org.mt4j.util.math.Ray;
import org.mt4j.util.math.Tools3D;
import org.mt4j.util.math.Vector3D;

import widgets.AbstractGUIWidget;


/**
 * This class lets you displace objects of type <i>AbstractGUIComponent</i>
 * along a vector which you can specify. This means, you can move an arbitrary
 * GUI-component along an arbitrary vector which cuts the position of the
 * GUI-component. This lets you move an object in 3-dimensional space using just one 
 * degree of freedom, so this complements nicely with the ordinary 
 * <i>Displacement3DPlaneListener</i> which gives you 2 dof in 3 dimensional space.
 * 
 * @author langenhagen
 * @version 20110924
 */
public class Displacement3DLineListener implements IGestureEventListener {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the direction vector */
	private Vector3D direction;
	/** the old position offset */
	private Vector3D oldpos = null;
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructor #1<br>
	 * Creates a new listener with the speceified direction relative to 
	 * the given component's space. If the given component is null,
	 * the listener just user global coordinate space.
	 * @param direction
	 * The direction a <i>Vector3D</i>
	 * @param comp
	 * The component specifying the coordinate space as an <i>MTComponent</i>
	 */
	public Displacement3DLineListener( Vector3D direction, MTComponent comp){
		setDirectionVector( direction, comp);
	}
	
	/**
	 * Constructor #2<br>
	 * Creates a new listener with the specified direction in global coordinates.
	 * @param direction
	 * The direction a <i>Vector3D</i>
	 */
	public Displacement3DLineListener( Vector3D direction){
		this(direction, null);
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This method will be triggered when the listener will be activated.
	 */
	@Override
	public boolean processGestureEvent(MTGestureEvent ge){
		
		DragEvent de = (DragEvent)ge;
		AbstractGUIWidget obj = (AbstractGUIWidget)de.getTarget();
		
		try{
			
			switch( ge.getId()){
			case MTGestureEvent.GESTURE_STARTED:				
			case MTGestureEvent.GESTURE_UPDATED:
				
				// if camera vector & direction are parallel, abort
				Icamera cam = obj.getViewingCamera();
				Vector3D camvec = cam.getViewCenterPos().getSubtracted( cam.getPosition()).getNormalized();
				if( camvec.dot( direction) == 1)
					return false;
				
				// calculate point in the near of the movement-ray: the pickpoint
				Vector3D xcamvec = Vector3D.Y_AXIS.getCross( camvec);
				
				Ray campickray = Tools3D.getCameraPickRay( 
						obj.getRenderer(),
						cam,
						de.getDragCursor().getCurrentEvtPosX(),
						de.getDragCursor().getCurrentEvtPosY());
				Plane billboardplane = new Plane( obj.getPosition(), xcamvec.getCross( direction));
				
				Vector3D pickpoint = billboardplane.getIntersectionLocal( campickray);
				
				// calculate nearest point to the pickpoint
				Ray directionray1 = new Ray( obj.getPosition(), obj.getPosition().getAdded( direction));
				Ray directionray2 = new Ray( obj.getPosition(), obj.getPosition().getSubtracted( direction));
				Plane helperplane = new Plane( pickpoint, direction);
	
				// have to try/catch because this piece of shit is not robust by itself!!!!
				Vector3D newpos = null;
				try{
					newpos = helperplane.getIntersectionLocal( directionray1);
					if(newpos == null)
						newpos = helperplane.getIntersectionLocal( directionray2);
				}catch(Exception e){}
				
				if( oldpos == null)
					oldpos = newpos;
						
				obj.setPosition( obj.getPosition().getAdded( newpos.getSubtracted( oldpos)));
				oldpos = newpos;
				
				break;
			case MTGestureEvent.GESTURE_ENDED:
				oldpos = null;
			}
		
		}catch (Exception e) {
			System.err.println("Error at Displacement3DLineListener.java...");
			e.getMessage();
			e.printStackTrace();
		}
			
		return false;
	}
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
	// GETTERS & SETTERS //////////////////////////////////////////////////////////////////////////
	
	/**
	 * Sets the direction vector according to the given one
	 * in the coordinate system of the specified component. 
	 * If the given component is <i>null</i>, global coordinaes are assumed.
	 * @param direction
	 * The direction vector as a <i>Vector3D</i>
	 * @param comp
	 * The component specifying the coordinate space as an <i>MTComponent</i>
	 */
	public void setDirectionVector( Vector3D direction, MTComponent comp){
		if( comp != null)
			this.direction = comp.localToGlobal( direction).normalizeLocal();
		else
			this.direction = direction.normalizeLocal();
	}
	
	/**
	 * Sets the movement-direction according to the specified vector
	 * in global coordinate space.
	 * @param direction
	 * The new axis vector of the repositioning-plane as a <i>Vector3D</i>
	 */
	public void setDirectionVector( Vector3D direction){
		setDirectionVector( direction, null);
	}
		
	/**
	 * Retrieves the direction vector used in this listener
	 * in the coordinates specified with setDirectionVector(), or in the constructor.
	 * @return
	 * The direction vector as a <i>Vector3D</i>
	 */
	public Vector3D getDirectionVector(){
		return direction;
	}
	
}

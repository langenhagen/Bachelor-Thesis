package gestureListeners.transformation;

import home.Home;
import home.Item;

import org.mt4j.components.MTComponent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.util.math.Plane;
import org.mt4j.util.math.Ray;
import org.mt4j.util.math.Tools3D;
import org.mt4j.util.math.Vector3D;

import widgets.AbstractGUIWidget;
import widgets.MT3DObject;


/**
 * This class lets you displace objects of type <i>AbstractGUIComponent</i>
 * along a plane which you can specify. This means, you can move an arbitrary
 * GUI-component along an arbitrary plane which cuts the position of the
 * GUI-component. This lets you move an object in 3-dimensional space using just two 
 * degrees of freedom, so that you can work properly On an ordinary display.
 * 
 * @author langenhagen
 * @version 201201029
 */
public class Displacement3DPlaneListener implements IGestureEventListener{
	
	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the axis of the repositioning-plane */
	private Vector3D normal;
	/** the plane On which to move the target */
	private Plane plane;
	/** difference vector between 3d-object center and cursor pick On the plane */
	private Vector3D objToTapDiff;
	/** XXX: this is some sort of hack: because some mt4j jerk fucked up the composite fuckup, i have to fuckup the upfuck by my fucking self */
	private Home home = null;
	/** the maxRadius in which we want the component to move. necessary, if we want the object just to move within a certain range */
	private float maxRadius = -1;

	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructor #1
	 * Creates a new listener with the speceified plane axis relative to 
	 * the given component's space. If the given component is null,
	 * the listener just user global coordinate space.
	 * @param axis
	 * The axis of the positioning-plane as a <i>Vector3D</i>
	 * @param comp
	 * The component specifying the coordinate space as an <i>MTComponent</i>
	 */
	public Displacement3DPlaneListener( Vector3D normal, MTComponent comp){
		setPlaneNormal( normal, comp);
	}
	
	/**
	 * Constructor #2<br>
	 * Creates a new listener with the specified plane axis in global coordinates.
	 * @param axis
	 * The axis of the positioning-plane as a <i>Vector3D</i>
	 */
	public Displacement3DPlaneListener( Vector3D normal){
		this(normal, null);
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////

	/**
	 * This method will be triggered when the listener will be activated.
	 */
	@Override
	public boolean processGestureEvent(MTGestureEvent ge){
		DragEvent de = (DragEvent)ge;
		AbstractGUIWidget obj = (AbstractGUIWidget)de.getTarget();
		Vector3D objPos = obj.getPosition();
		
		try{
			
			// create plane and then create ray from cursorcoords into scene in cam-direction.
			// where plane and ray meet, set the new position of the object.
			
			Ray ray = Tools3D.getCameraPickRay(
					obj.getRenderer(),
					obj.getViewingCamera(),
					de.getDragCursor().getCurrentEvtPosX(),
					de.getDragCursor().getCurrentEvtPosY());
			
			switch( ge.getId()){
			case MTGestureEvent.GESTURE_STARTED:{
				plane = new Plane( objPos, normal);
				Vector3D tapPos =  plane.getIntersectionLocal( ray);
				if( tapPos != null){
					objToTapDiff = objPos.getSubtracted( tapPos);	
				}else{
					objToTapDiff = Vector3D.ZERO_VECTOR;	
				}
			}
			case MTGestureEvent.GESTURE_UPDATED:{
				plane = new Plane( objPos, normal);
				Vector3D tapPos =  plane.getIntersectionLocal( ray);
				if( tapPos != null){
					
					MT3DObject homeView = home != null ? home.getView() : null; // just for shortcuts
					
					Vector3D newPos = tapPos.getAdded( objToTapDiff);

					if( home == null || 
						home.getInitialPosition() == null ||
						homeView == obj && newPos.distance( home.getInitialPosition()) < maxRadius ||
						homeView != obj && homeView.getPosition().getAdded( newPos.getSubtracted( objPos)).distance( home.getInitialPosition()) < maxRadius){

						obj.setPosition( newPos);
						
						// process all other items and the home itself, if a home was attached
						if(home != null){
							for( Item item : home.getAllItems()){
								MT3DObject itemView = item.getView();
								if( itemView == obj){
									homeView.setPosition( homeView.getPosition().getAdded( newPos));
								}else{
									itemView.setPosition( itemView.getPosition().getAdded(newPos));
								}
							}							
						}
					}
	
				} // END if tapPos != null
				break;
			} // END case GESTURE_UPDATED
			} // END switch
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	// GETTERS & SETTERS //////////////////////////////////////////////////////////////////////////
	
	/**
	 * Sets the axis of the repositioning-plane according to the specified vector
	 * in the coordinate system of the specified component. 
	 * If the given component is <i>null</i>, global coordinaes are assumed.
	 * @param axis
	 * The axis of the positioning-plane as a <i>Vector3D</i>
	 * @param comp
	 * The component specifying the coordinate space as an <i>MTComponent</i>
	 */
	public void setPlaneNormal( Vector3D normal, MTComponent comp){
		if( comp != null)
			this.normal = comp.localToGlobal( normal).normalizeLocal();
		else
			this.normal = normal.normalizeLocal();
	}
	
	/**
	 * Sets the axis of the repositioning-plane according to the specified vector
	 * in global coordinate space.
	 * @param axis
	 * The new axis vector of the repositioning-plane as a <i>Vector3D</i>
	 */
	public void setPlaneNormal( Vector3D normal){
		setPlaneNormal( normal, null);
	}
		
	/**
	 * Retrieves the repositioning-plane's axis used in this listener 
	 * in the coordinates specified with setPlaneNormal(), or in the constructor.
	 * @return
	 * The repositioning-plane' normal as a <i>Vector3D</i>
	 */
	public Vector3D getPlaneNormal(){
		return normal;
	}
	
	/**
	 * If you bind this listener On a home,
	 * you can forward this home to the listener,
	 * to manage movement of all items in the home,
	 * although they are no real children.
	 * @param home
	 * The home as a <i>Home</i>.
	 * @return
	 * Returns itself, the <i>Displacement3DPlaneListener</i>.
	 */
	public Displacement3DPlaneListener setHome( Home home){
		this.home = home;
		return this;
	}
	
	/**
	 * If you bind this listener On a home,
	 * you can forward this home to the listener.
	 * This method allows you to retireve the home.
	 * @return
	 * The home as a <i>Home</i> or NULL if there is no home bound.
	 */
	public Home getHome(){
		return home;
	}
	
	/**
	 * If we bound a <i>Home</i> On this listener, we can say, that the home shall not just be moved within a certain radius.
	 * In such a case This method sets the radius of the component to move. This method has no effect if there is no home bound 
	 * to this listener.
	 * @param maxRadius
	 * The maxRadius to use as a positive <i>float</i>. This radius specifies the maximum radius of a sphere 
	 * within to move a component in global coordinates. If the number is negative, the maximum movement radius will be zero.
	 * @return
	 * Returns itself, the <i>Displacement3DPlaneListener</i>.
	 */
	public Displacement3DPlaneListener setMovementRadius( float radius){
		this.maxRadius = Math.max(radius,0);
		return this;
	}
}
package widgets;

import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.IInputProcessor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

import animationListeners.AnimMoveAction;
import util.Mode;

/**
 * This abstract class represents a Widgets and defines the standard interface
 * of a GUI-Component and also common animations.
 * 
 * @author langenhagen
 * @version 20111223
 */
public abstract class AbstractGUIWidget extends MTComponent{
	
	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	protected Mode mode = Mode.NORMAL;
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main constructor.
	 * @param pApplet
	 * The Application which is rendering this GUI-component as a <i>PApplet</i>.
	 */
	public AbstractGUIWidget(PApplet pApplet){
		super( pApplet);
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This method lets you delete one (and only one) GestureEventListener
	 * On this GUI-component. You do not have to catch the concrete listener-object
	 * and then delete it manually, so you only have to know its type.
	 * @param gestureEvtSender
	 * The sender of the gesture event as a <i>Class</i> which implements
	 * <i>IInputProcessor</i>.
	 * @param listenerType
	 * The type of the gesture event as a <i>Class</i> which implements
	 * <i>IGestureEventListener</i>.
	 * @return
	 * Returns TRUE, if a gesture event listener was removed, 
	 * otherwise, if there was no such gesture event listener found, returns FALSE.
	 */
	public boolean removeListenerType(Class<? extends IInputProcessor> gestureEvtSender, Class<? extends IGestureEventListener> listenerType){
		
		for(IGestureEventListener l : this.getGestureListeners())
			if( l.getClass().equals( listenerType)){
				this.removeGestureEventListener( gestureEvtSender, l);
				return true;
		}
		return false;
	}
	
	
	/**
	 * This method lets you returnone (and only one) GestureEventListener
	 * On this GUI-component. You do not have to catch the concrete listener-object
	 * and then delete it manually, so you only have to know its type.
	 * @param gestureEvtSender
	 * The sender of the gesture event as a <i>Class</i> which implements
	 * <i>IInputProcessor</i>.
	 * @param listenerType
	 * The type of the gesture event as a <i>Class</i> which implements
	 * <i>IGestureEventListener</i>.
	 * @return
	 * Returns and <i>IGestureEventListener</i>, if a gesture event listener was found, 
	 * otherwise, if there was no such gesture event listener found, returns NULL.
	 */
	public IGestureEventListener getListenerType(Class<? extends IInputProcessor> gestureEvtSender, Class<? extends IGestureEventListener> listenerType){
		
		for(IGestureEventListener l : this.getGestureListeners())
			if( l.getClass().equals( listenerType)){
				return l;
		}
		return null;
	}
	
	
	/**
	 * Returns a position beneath or next to this component that can be used 
	 * as a position for other components. You just have to specify the distance
	 * between the new vector and the method callee and if you want this new vector
	 * shifted horizontally to the callee's position or vertically.
	 * @param clearance
	 * The distance between the vector to create and this object as a <i>float</i>.
	 * @param horizontal
	 * Indicates, whether you want the new point to be shifted horizontal direction to
	 * this component's position or otherwise in vertical direction. Horizontal direction means,
	 * that the y-value of the new vector will be the same as the one of the positiono of this element,
	 * it behaves analoguous to the x-value in the vertical case, that is, if you specify this parameter
	 * as FALSE.
	 * @return
	 * A new <i>Vector3D</i> that is positioned either On the same height as this component's position vector
	 * or On the same width respectively.
	 */
	public Vector3D getVectorNextToComponent( float clearance, boolean horizontal){
		
		Vector3D ret;
		if( horizontal){
			ret = new Vector3D(
					this.getPosition().getX() + this.getWidth() + clearance,
					this.getPosition().getY());
		}else{
			ret = new Vector3D(
					this.getPosition().getX(),
					this.getPosition().getY() + this.getHeight() + clearance);
		}	
		return ret;
	}
	
	// POSITION HANDLING METHODS //////////////////////////////////////////////////////////////////
	
	
	/**
	 * Sets the position of this component next to the position of another component, 
	 * using a specified clearing area. Furthermore, you can specify of this component shall be
	 * aligned under or left to the specified component.
	 * @param comp
	 * The <i>AbstractGUIComponent</i> to which this component shall be positioned next to.
	 * @param clearance
	 * The free distance between the specified component and this one.
	 * @param horizontal
	 * Indicates, whether you want the new point to be shifted horizontal direction to
	 * this component's position or otherwise in vertical direction. Horizontal direction means,
	 * that the y-value of the new vector will be the same as the one of the positiono of this element,
	 * it behaves analoguous to the x-value in the vertical case, that is, if you specify this parameter
	 * as FALSE.
	 */
	public void setPositionNextTo( AbstractGUIWidget comp, float clearance, boolean horizontal){
		this.setPosition( comp.getVectorNextToComponent( clearance, horizontal));
	}
	
	/**
	 * Returns the position-vector of this Component
	 * in the given coordinate space.<br>
	 * @param space
	 * The referenced coordinate space as a <i>TransformSpace</i>
	 * @return
	 * The position as a <i>Vector3D</i>
	 */
	public abstract Vector3D getPosition( TransformSpace space);
	
	
	/**
	 * Returns the global position-vector of this GUI-Component.
	 * @return
	 * This component's global position as a <i>Vector3D</i>
	 */
	public final Vector3D getPosition(){
		return getPosition( TransformSpace.GLOBAL);
	}
	
	
	/**
	 * Sets the component to the given coordinates in the given coordinate system.<br>
	 * <strong>Caution:</strong> The length of one coordinate unit depends
	 * On the own scale for the Component or respectively the scale of the parent object.
	 * @param pos
	 * the coordinates as a <i>Vector3D</i>
	 * @param space
	 * The referencing coordinate space as a <i>TransformSpace</i>
	 */
	public abstract void setPosition(Vector3D pos, TransformSpace space);
	
	
	/**
	 * Sets the component to the given coordinates in global space
	 * @param pos
	 * the Coordinates as a <i>Vector3D</i>
	 */
	public final void setPosition( Vector3D pos){
		setPosition(pos, TransformSpace.GLOBAL);
	}
	
	
	/**
	 * Sets the position of this <i>AbstractGUIComponent</i> to the specified screen coordinates
	 * On the z=0 Plane.<br>
	 * If the position-anchor is defined as the center of the <i>AbstractGUIComponent</i> or a 
	 * specified corner depends On the concrete type of object to handle.
	 * @param x
	 * The normalized x-position On the screen, where 0 is the left edge and 1 is the right edge
	 * as a <i>float</i>
	 * @param y
	 * The normalized y-position On the screen, where 0 is the upper edge and 1 is the lower edged
	 * as a <i>float</i>
	 * @return
	 * Returns the new <strong>global</strong> position as a <i>Vector3D</i> 
	 * or <i>null</i> in case of error
	 */
	public Vector3D setPosOnScreen(float x, float y){

		if( x<0.0f || y<0.0f || x>1.0f || y>1.0f){
			System.err.println(	"Error in using AbstractGUIComponent.setPosOnScreen().\n" +
								"Values of x and y must be between 0 an 1.");
			return null;
		}
		
		Vector3D ret = new Vector3D( getRenderer().width*x, getRenderer().height*y, 0);
		setPosition( ret);
		
		return ret;
	}
	
	/**
	 * Sets the position of this <i>AbstractGUIComponent</i> to the specified screen coordinates
	 * within the parents On the z=0 Plane.<br>
	 * If the position-anchor is defined as the center of the <i>AbstractGUIComponent</i> or a 
	 * specified corner depends On the concrete type of object to handle.
	 * <strong>Caution:</strong> If the specific <i>AbstractGUIComponent</i> is able to fulfill the
	 * request depends On the parent's type.<br><br>
	 * 
	 * Following parent-types are supported by now:<br>
	 *  -<i>MTRectangle</i><br>
	 *  -<i>AbstractGUIComponent</i><br>
	 * 	-(augment if you want)
	 * 
	 * @param x
	 * The normalized x-position On the parent, where 0 is the left edge and 1 is the right edge
	 * as a <i>float</i>
	 * @param y
	 * The normalized y-position On the parent, where 0 is the upper edge and 1 is the lower edged
	 * as a <i>float</i>
	 * @return
	 * Returns the new <strong>global</strong> position as a <i>Vector3D</i> 
	 * or <i>null</i> in case of error
	 */
	public Vector3D setPosInParent(float x, float y){

		if( x<0.0f || y<0.0f || x>1.0f || y>1.0f){
			System.err.println(	"Error in using AbstractGUIComponent.setPosOnParent().\n" +
								"Values of x and y must be between 0 an 1.");
			return null;
		}
		
		Vector3D ret = null;
		MTComponent parent = this.getParent();
		
		if( parent instanceof MTRectangle){						// MTRectangle
			MTRectangle rect = (MTRectangle)parent;

			PositionAnchor oldAnchor = rect.getAnchor();
			rect.setAnchor( PositionAnchor.UPPER_LEFT);
			Vector3D parentPos = rect.getPosition( TransformSpace.GLOBAL);
			rect.setAnchor( oldAnchor);
			
			ret = new Vector3D( parentPos.getX() + rect.getWidthXY( TransformSpace.GLOBAL)*x, 
								parentPos.getY() + rect.getHeightXY( TransformSpace.GLOBAL)*y);
			
		}else if( parent instanceof AbstractGUIWidget){		// AbstractGUIComponent
			AbstractGUIWidget comp = (AbstractGUIWidget)parent;
			Vector3D parentPos = comp.getPosition();
				
			ret = new Vector3D( parentPos.getX() + comp.getHeight()*x,
								parentPos.getY() + comp.getWidth()*y);
		}
		
		this.setPosition( ret);
		
		return ret;
	}
	
	/**
	 * Returns the width of this object in the specified coordinate space
	 * @param space
	 * The referencing coordinate space as a <i>TransformSpace</i>
	 * @return
	 * The width of this GUI-component in the specified coordinate space 
	 * as a <i>float</i>
	 */
	public abstract float getWidth( TransformSpace space);
	
	/**
	 * Returns the height of this object in the specified coordinate space
	 * @param space
	 * The referencing coordinate space as a <i>TransformSpace</i>
	 * @return
	 * The height of this GUI-component in the specified coordinate space 
	 * as a <i>float</i>
	 */
	public abstract float getHeight( TransformSpace space);
	
	/**
	 * Returns the width of this object in global coordinate space
	 * @return
	 * The global width of this GUI-component as a <i>float</i>
	 */
	public final float getWidth(){
		return getWidth(TransformSpace.GLOBAL);
	}
	
	/**
	 * Returns the height of this object in global coordinate space
	 * @return
	 * The global height of this GUI-component as a <i>float</i>
	 */
	public final float getHeight(){
		return getHeight(TransformSpace.GLOBAL);
	}
	
	/**
	 * Sets the width of this object in the specified coordinate space
	 * @param width
	 * The new width as a <i>float</i>
	 * @param space
	 * The referencing coordinate space as a <i>TransformSpace</i>
	 */	
	public abstract void setWidth( float width, TransformSpace space);
	
	/**
	 * Sets the height of this object in the specified coordinate space
	 * @param height
	 * The new height as a <i>floaty</i>
	 * @param space
	 * The referencing coordinate space as a <i>TransformSpace</i>
	 */	
	public abstract void setHeight( float height, TransformSpace space);
	
	/**
	 * Sets the width of this object in global coordinate space
	 * @param width
	 * The new width as a <i>floaty</i>
	 */	
	public final void setWidth( float width){
		setWidth( width, TransformSpace.GLOBAL);
	}
	
	/**
	 * Sets the height of this object in global coordinate space
	 * @param height
	 * The new height as a <i>floaty</i>
	 */	
	public final void setHeight( float height){
		setHeight( height, TransformSpace.GLOBAL);
	}
	
	/**
	 * Checks, wether this GUI-component is enabled or not.
	 * @return
	 * Returns FALSE, if the <i>Mode</i> of this component is "disabled",
	 * otherwise it returns TRUE.
	 */
	public boolean isEnabled(){
		return mode != Mode.DISABLED;
	}
	
	/**
	 * Sets this component's mode into disabled, when the argument is FALSE.
	 * If the argument is TRUE and the mode is "disabled", then it will be enabled, 
	 * more specific, it will be changed to "axis". If b is TRUE and 
	 * this component has another state like "disabled", nothing happens.
	 * @param b
	 * The parameter setting the determining the mode of this component as a <i>boolean</i>.
	 */
	public void setEnabled( boolean b){

		if( b == false)
			setMode( Mode.DISABLED);
		else if( mode == Mode.DISABLED)
			setMode( Mode.NORMAL);	
	}
	
	/**
	 * Gets the current <i>Mode</i> of this component.
	 * @return
	 * The mode as a <i>Mode</i>.
	 */
	public final Mode getMode(){
		return mode;
	}
	
	/**
	 * Sets the Mode of this component to the specified one.
	 * @param mode
	 * The new mode as a <i>Mode</i>-value.
	 */
	public abstract void setMode( Mode mode);
	
	// ANIMATIONS /////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Sets up, starts a movement animation from the actual position to a given position in a
	 * given time interval and returns the <i>Animation</i>-Object.
	 * @param destination
	 * The destination vector in global coordinates as a <i>Vector3D</i>.
	 * @param duration
	 * The number of milliseconds that the movement-animation will take.
	 * @return
	 * The appropriate <i>Animation</i>
	 */
	public Animation animMove( Vector3D destination, float duration){
		
		float maxVal=100;
		
        Animation anim = new Animation("move animation", new MultiPurposeInterpolator(0,maxVal, duration, 0, 1, 1) , this);
        anim.addAnimationListener(new AnimMoveAction( destination, maxVal)).start();
		
        return anim;
	}
	
}
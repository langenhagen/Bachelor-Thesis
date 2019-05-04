package widgets;

import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

/**
 * This class represents a 2D-manipulator, which can control and manipulate 2-dimensional values within
 * a specified range. It can be used everywhere, where you need an integrated component with two 
 * degrees of freedom.
 * 
 * @author langenhagen
 * @version 20110523
 */
public abstract class MT2DManipulator extends AbstractGUIWidget {

	/**
	 * Main constructor
	 * @param pApplet
	 * The Application which is rendering this GUI-component as a <i>PApplet</i>
	 */
	public MT2DManipulator(PApplet pApplet){
		super( pApplet);
	}
	
	/**
	 * Sets the value of this manipulator.
	 * If the value is outside the specified range of this manipulator, nothing happens.
	 * @param x
	 * The x-value as a <i>float</i>
	 * @param y
	 * The y-value as a <i>float</i>
	 * @return
	 * Returns TRUE if the value was within the permitted range,
	 * otherwise returns FALSE
	 */
	public final boolean setValue( float x, float y){
		
		float oldX = this.getValueX();
		float oldY = this.getValueY();
		
		if( this.setValueX( x) && this.setValueY( y))	// everything worked fine?
			return true;
		
		this.setValueX( oldX);							// rollback
		this.setValueY( oldY);
		return false;
	}
	
	/**
	 * Sets the x-value of this manipulator.
	 * If the value is outside the specified x-range of this manipulator, 
	 * nothing happens.
	 * @param val
	 * The x-value as a <i>float</i>
	 * Returns TRUE if the value was within the permitted range,
	 * otherwise returns FALSE
	 */
	public abstract boolean setValueX( float val);
	
	/**
	 * Sets the y-value of this manipulator.
	 * If the value is outside the specified y-range of this manipulator, 
	 * nothing happens.
	 * @param val
	 * The y-value as a <i>float</i>
	 * Returns TRUE if the value was within the permitted range,
	 * otherwise returns FALSE
	 */
	public abstract boolean setValueY(float val);

	/**
	 * Retrieves the current value.
	 * @return
	 * The current value as an <i>Vector3D</i>.
	 * The x-value indicates the x-value, the y-the y-value and the z-value will be undefinded.
	 */
	public abstract Vector3D getValue();
	
	/**
	 * Retrieves the current x-value.
	 * @return
	 * The current x-value as a <i>float</i>
	 */
	public abstract float getValueX();
	
	/**
	 * Retrieves the current y-value.
	 * @return
	 * The current y-value as a <i>float</i>
	 */
	public abstract float getValueY();

	/**
	 * Sets the range of the manipulator.
	 * If some minimum-value is bigger than its
	 * corresponding maximum-value, nothing happens.
	 * @param min_x
	 * The minimum x-value as a <i>float</i>
	 * @param max_x
	 * The maximum x-value as a <i>float</i>
	 * @param min_y
	 * The minimum y-value as a <i>float</i>
	 * @param max_y
	 * The maximum x-value as a <i>float</i>
	 * @return
	 * Returns TRUE if the action was successful,
	 * otherwise returns FALSE
	 */
	public final boolean setRange( float min_x, float max_x, float min_y, float max_y){
		
		float oldMinX = this.getMinValueX();
		float oldMaxX = this.getMaxValueX();
		float oldMinY = this.getMinValueY();
		float oldMaxY = this.getMaxValueY();
		
		if( this.setRangeX( min_x, max_x) && this.setRangeY( min_y, max_y))	// everything worked fine?
			return true;
		
		this.setRangeX( oldMinX, oldMaxX);									// rollback
		this.setRangeY( oldMinY, oldMaxY);
		return false;
	}
	
	/**
	 * Sets the x-range of the manipulator.
	 * If some minimum-value is bigger than its
	 * corresponding maximum-value, nothing happens.
	 * @param min
	 * The minimum x-value as a <i>float</i>
	 * @param max
	 * The maximum x-value as a <i>float</i>
	 * @return
	 * Returns TRUE if the action was successful,
	 * otherwise returns FALSE
	 */
	public abstract boolean setRangeX( float min, float max );
	
	/**
	 * Sets the y-range of the manipulator.
	 * If some minimum-value is bigger than its
	 * corresponding maximum-value, nothing happens.
	 * @param min
	 * The minimum y-value as a <i>float</i>
	 * @param max
	 * The maximum y-value as a <i>float</i>
	 * @return
	 * Returns TRUE if the action was successful,
	 * otherwise returns FALSE
	 */
	public abstract boolean setRangeY( float min, float max );
		
	/**
	 * Returns the smallest allowable x-value
	 * @return
	 * The smallest allowable x-value as a <i>float</i>
	 */
	public abstract float getMinValueX();
	
	/**
	 * Returns the biggest allowable x-value
	 * @return
	 * The biggest allowable x-value as a <i>float</i>
	 */
	public abstract float getMaxValueX();
	
	/**
	 * Returns the smallest allowable y-value
	 * @return
	 * The smallest allowable y-value as a <i>float</i>
	 */
	public abstract float getMinValueY();
	
	/**
	 * Returns the biggest allowable y-value
	 * @return
	 * The biggest allowable y-value as a <i>float</i>
	 */
	public abstract float getMaxValueY();
	
	/**
	 * Determines the position, which is initial in case of snapping back.
	 * If the specified Values are out of range, nothing happens.
	 * @param x
	 * The initial x-value as a <i>float</i>
	 * @param y
	 * The initial y-value as a <i>float</i>
	 * @return
	 * Returns TRUE if action was successful,
	 * otherwise returns FALSE
	 */
	public abstract boolean setSnapBackValue( float x, float y);
	
	/**
	 * Retrieves the snap-back-value.
	 * @return
	 * The snap-back-value as a <i>Vector3D</i>
	 */
	public abstract Vector3D getSnapBackValue();
	
	/**
	 * Determines, wether the cursor should snap back to an initial value/position
	 * after a gesture On this manipulator ended.
	 * @param b
	 * The indicator, where TRUE indicates snapping back, FALSE indicates no snapping back
	 */
	public abstract void setSnapBackEnabled( boolean b);
	
	/**
	 * Specifies, if snapping back is enabled.
	 * @return
	 * Returns TRUE, if snapping back is enabled,
	 * otherwise returns FALSE.
	 */
	public abstract boolean isSnapBackEnabled();

}
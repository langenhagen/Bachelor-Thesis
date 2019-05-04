package widgets;

import processing.core.PApplet;


/**
 * This class represents a slider in the GUI-context.
 * The slider is able to process gestures and lets you adjust some
 * floating-point value.
 * 
 * @author langenhagen
 * @version 20110522
 */
public abstract class MTSlider extends AbstractGUIWidget {

	/**
	 * Main constructor
	 * @param pApplet
	 * The Application which is rendering this GUI-component as a <i>PApplet</i>
	 */
	public MTSlider(PApplet pApplet){
		super( pApplet);
	}
	
	/**
	 * Sets the value of this slider to the specified value.
	 * If the specified value is not within the value-range,
	 * no changes will be performed.
	 * @param val
	 * The new value as a <i>float</i>.
	 * @return
	 * Returns TRUE, if the specified value is within the value-range,
	 * otherwise returns FALSE.
	 */
	public abstract boolean setValue( float val);
	
	/**
	 * Returns the current value
	 * @return
	 * The current value as a <i>float</i>
	 */
	public abstract float getValue();
	
	/**
	 * Sets the value-range of this slider to the specified values.
	 * @param min
	 * The smallest possible value as a <i>float</i>
	 * @param max
	 * The biggest possible value as a <i>float</i>
	 * @return
	 * Returns TRUE, if the action was successfull, otherwise
	 * returns FALSE (e.g. when the specified min-value 
	 * is smaller than the max-value)
	 */
	public abstract boolean setValueRange( float min, float max);
	
	/**
	 * Returns the greatest value the slider can take.
	 * @return
	 * The biggest value, the slider can receive, as a <i>float</i>
	 */
	public abstract float getMaxValue();
	
	/**
	 * Returns the smallest value the slider can take.
	 * @return
	 * The smallest value, the slider can receive, as a <i>float</i>
	 */
	public abstract float getMinValue();
	
	/**
	 * Sets the smallest value the slider can take. If the
	 * specified value is bigger than the biggest possible value, no changes will be performed.
	 * @param min
	 * The new smallest value as a <i>float</i>
	 * @return
	 * Returns FALSE, if the specified value is bigger than the biggest value of
	 * the value-range, otherwise returns TRUE
	 */
	public boolean setMinValue( float min){
		if( min > getMaxValue()){
			System.err.println("MTSlider.setMinValue(): Setting the min value bigger than the max value is not possible!");
			return false;
		}
		setValueRange( min, getMaxValue());
		return true;
	}
	
	/**
	 * Sets the biggest value the slider can take. If the
	 * specified value is smaller than the smallest possible value, no changes will be performed.
	 * @param max
	 * The new biggest value as a <i>float</i>
	 * @return
	 * Returns FALSE, if the specified value is smaller than the smallest value of
	 * the value-range, otherwise returns TRUE
	 */
	public boolean setMaxValue( float max){
		if( max < getMinValue()){
			System.err.println("MTSlider.setMaxValue(): Setting the max value smaller than the min value is not possible!");
			return false;
		}
		setValueRange( getMinValue(), max);
		return true;
	}
	
	/**
	 * Specifies, if the slider will
	 * be horizontally or vertically aligned.
	 * @param b
	 * TRUE specifies, that the slider will be vertical,
	 * FALSE specifies, that the slider will be horizontal
	 */
	public abstract void setVertical(boolean b);
	
	/**
	 * Determines, if the slider is
	 * be horizontally or vertically aligned.
	 * @param b
	 * Returns TRUE, if the slider is vertical,
	 * otherwise returns FALSE, if the slider is horizontal
	 */
	public abstract boolean isVertical();
	
}
package widgets;

import processing.core.PApplet;

/**
 * This class represents a Check-Button, a component similar to a check box.<br>
 * <strong>Note:</strong> Since the MTCheckButton carries its own
 * <i>TapProcessor</i> you don't have to create a new one for this GUI-element.
 * 
 * @author langenhagen
 * @version 20110522
 */
public abstract class MTCheckButton extends MTButton {

	/**
	 * Main constructor
	 * @param pApplet
	 * The Application which is rendering this GUI-component as a <i>PApplet</i>
	 */
	public MTCheckButton(PApplet pApplet){
		super( pApplet);
	}
	
	/**
	 * Sets this component checked.
	 * @param b
	 * Specifies, wether this <i>CheckButton</i> shall be checked or not, as a <i>boolean</i>
	 */
	public abstract void setChecked( boolean b);
	
	/**
	 * Checks, if this component is checked.
	 * @return
	 * Returns TRUE, if the <i>CheckButton</i> is checked, otherwise FALSE
	 */
	public abstract boolean isChecked();

}
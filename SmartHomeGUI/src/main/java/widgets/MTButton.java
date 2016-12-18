package widgets;

import processing.core.PApplet;


/**
 * This class represents a Button in the GUI context.
 * It handles design parameters, text entries and basic input 
 * gestureListeners so that you can easily care about the true work.<br>
 * <strong>Note:</strong> Since the MTButton carries its own
 * <i>TapProcessor</i> you don't have to create a new one for this GUI-element.
 * 
 * @author langenhagen
 * @version 20110522
 */
public abstract class MTButton extends AbstractGUIWidget{

	/**
	 * Main constructor
	 * @param pApplet
	 * The Application which is rendering this GUI-component as a <i>PApplet</i>
	 */
	public MTButton(PApplet pApplet){
		super( pApplet);
	}
	
	/**
	 * Sets the caption of the button.
	 * @param text
	 * The caption as a <i>String</i>
	 */
	public abstract void setText( String text);
	
	/**
	 * Returns the caption of the button
	 * @return
	 * The caption as a <i>String</i>
	 */
	public abstract String getText();
	
}
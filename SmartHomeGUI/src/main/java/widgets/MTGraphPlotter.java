package widgets;

import java.util.List;

import processing.core.PApplet;

/**
 * This class represents a graph plotter, which can plot double-values.
 * It maintains the values it plots and gives you control over your plots.
 * 
 * @author langenhagen
 * @version 20110523
 */
public abstract class MTGraphPlotter extends AbstractGUIWidget{

	/**
	 * Main constructor
	 * @param pApplet
	 * The Application which is rendering this GUI-component as a <i>PApplet</i>
	 */
	public MTGraphPlotter(PApplet pApplet){
		super( pApplet);
	}
	
	/**
	 * Sets the text On the (horizontal) x-axis.
	 * @param text
	 * The new caption to set as a <i>String</i>
	 */
	public abstract void setTextX( String text);

	/**
	 * Sets the text On the (vertical) y-axis.
	 * @param text
	 * The new caption to set as a <i>String</i>
	 */
	public abstract void setTextY( String text);
	
	/**
	 * Gets the text of the (horizontal) x-axis.
	 * @return
	 * The caption On the x-axis as a <i>String</i>
	 */
	public abstract String getTextX();
	
	/**
	 * Gets the text of the (vertical) y-axis.
	 * @return
	 * The caption On the y-axis as a <i>String</i>
	 */
	public abstract String getTextY();
	
	/**
	 * Clears the plot On the screen and clears the list of data-values.
	 */
	public abstract void clear();
	
	/**
	 * Specifies the given list as the new list of data-values.
	 * @param values
	 * The list of new values as a <i>List</i> of type <i>Double</i>
	 */
	public abstract void setAllValues( List<Double> values);
	
	/**
	 * Returns the list of data-values.
	 * @return
	 * The list of data-values as a <i>List</i> of type <i>Double</i>
	 */
	public abstract List<Double> getAllValues();
	
	/**
	 * Adds a new data-element to the end of the list
	 * @param value
	 * The value to append as a <i>double</i>
	 */
	public abstract void addValue( double value);
	
	/**
	 * Inserts a new data-element to the given position in the list.
	 * If the position is not within the range of the data-values-list,
	 * the new value will be appended to the end of the list.
	 * @param value
	 * The value to insert as a <i>double</i>
	 * @param pos
	 * The position to add the value as an <i>int</i>
	 */
	public abstract void addValueAt( double value, int pos);
	
	/**
	 * Retrieves the value of the data-element at the given position in the list.
	 * If the argument is invalid, <i>NaN</i> will be returned.
	 * @param pos
	 * The index from where the value to get as an <i>int</i>
	 * @return
	 * A <i>double</i> or <i>Double.NaN</i> in case of error
	 */
	public abstract double getValueAt( int pos);
	
	/**
	 * Removes a data-element at the given position in the list.
	 * If the position is not within the range of the data-values-list,
	 * nothing happens.
	 * @param pos 
	 * The index wich corresponds to the value to be deleted <i>int</i>
	 * @return
	 * The deleted value as a <i>double</i> or <i>Double.NaN</i> in case of error
	 */
	public abstract double removeValueAt( int pos);
	
	

}
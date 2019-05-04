package home.deviceCommands;

import home.Item;

/**
 * This abstract class provides an interface for defining
 * what will happen when the user interacts whith a corresponding item
 * and the way of interaction with an item in the gui.
 * 
 * @author langenhagen
 * @version 20120207
 */
public abstract class DeviceCommand {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////

	/** the <i>Item</i> to which this command belongs */
	protected Item item;

	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////

	/**
	 * Main constructor.
	 * @param item
	 * The corresponding item as an <i>Item</i>.
	 * It is the context of the new <i>DeviceCommand</i>.
	 */
	public DeviceCommand( Item item){
		this.item = item;
	}

	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////

	/**
	 * Executes the command which may influence the physical device
	 * and the view in the gui.
	 */
	public abstract void execute();

	/**
	 * Ends the command effect. This method does not destroy the DeviceCommand,
	 * nor does it revert the actions done, but it gives you the possibility
	 * to end the command from outside.
	 */
	public abstract void cease();

	// GETTERS & SETTERS //////////////////////////////////////////////////////////////////////////

	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
}
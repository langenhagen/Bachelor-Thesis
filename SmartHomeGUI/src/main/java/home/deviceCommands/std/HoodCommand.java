package home.deviceCommands.std;

import org.sercho.masp.models.Context.PhysicalDevice;

import home.Item;

/**
 * A standard command for hoods.
 * 
 * @author langenhagen
 * @version 20120207
 */
public class HoodCommand extends StdCommand {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main constructor.
	 * @param item
	 * The corresponding item as an <i>Item</i>.
	 * It is the context of the new <i>DeviceCommand</i>.
	 * @param device
	 * The device that shall correspond to the item.
	 * Usually the <i>PhysicalDevice</i> of the item itself or a subdevice of it.
	 */
	public HoodCommand(Item item, PhysicalDevice device){
		super( item, device);
	}

	// PROTECTED METHODS //////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void executeHelper(){
	}

	@Override
	protected void ceaseHelper(){
	}

	@Override
	protected void setGUIAttributesHelper(){		
	}
	
}
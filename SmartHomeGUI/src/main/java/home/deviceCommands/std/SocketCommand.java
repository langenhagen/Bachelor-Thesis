package home.deviceCommands.std;

import org.sercho.masp.models.Context.PhysicalDevice;

import home.Item;

/**
 * A standard command for sockets.
 * 
 * @author langenhagen
 * @version 20120207
 */
public class SocketCommand extends StdCommand {

	/**
	 * Main constructor.
	 * @param item
	 * The corresponding item as an <i>Item</i>.
	 * It is the context of the new <i>DeviceCommand</i>.
	 * @param device
	 * The device that shall correspond to the item.
	 * Usually the <i>PhysicalDevice</i> of the item itself or a subdevice of it.
	 */
	public SocketCommand(Item item, PhysicalDevice device){
		super( item, device);
	}

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
package home.deviceCommands.std;

import org.sercho.masp.models.Context.PhysicalDevice;

import home.Item;

/**
 * This command does nothing except being a dummy command for preventing errors and test scenarios.
 * 
 * @author langenhagen
 * @version 20120207
 */
public class DummyCommand extends StdCommand {

	/**
	 * Main constructor.
	 * @param item
	 * The corresponding item as an <i>Item</i>.
	 * It is the context of the new <i>DeviceCommand</i>.
	 * @param device
	 * The device that shall correspond to the item.
	 * Usually the <i>PhysicalDevice</i> of the item itself or a subdevice of it.
	 */
	public DummyCommand(Item item, PhysicalDevice device){
		super( item, device);
	}

	@Override
	protected void executeHelper(){
		System.out.println("Executing dummy command for " + item.getID());
	}

	@Override
	protected void ceaseHelper(){
		System.out.println("Ceasing dummy command for " + item.getID());
	}

	@Override
	protected void setGUIAttributesHelper(){		
	}
}
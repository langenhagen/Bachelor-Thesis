package home.deviceCommands.factories;

import org.sercho.masp.models.Context.PhysicalDevice;

import util.DeviceType;
import home.Item;
import home.deviceCommands.DeviceCommand;
import home.deviceCommands.std.BlindCommand;
import home.deviceCommands.std.CookTopCommand;
import home.deviceCommands.std.CookerCommand;
import home.deviceCommands.std.DishwasherCommand;
import home.deviceCommands.std.DummyCommand;
import home.deviceCommands.std.FanCommand;
import home.deviceCommands.std.FridgeCommand;
import home.deviceCommands.std.HeaterCommand;
import home.deviceCommands.std.HobCommand;
import home.deviceCommands.std.HoodCommand;
import home.deviceCommands.std.LampCommand;
import home.deviceCommands.std.MeterCommand;
import home.deviceCommands.std.MixerCommand;
import home.deviceCommands.std.NotebookCommand;
import home.deviceCommands.std.OvenCommand;
import home.deviceCommands.std.PCCommand;
import home.deviceCommands.std.RadioCommand;
import home.deviceCommands.std.RemoteControlCommand;
import home.deviceCommands.std.SocketCommand;
import home.deviceCommands.std.TVCommand;
import home.deviceCommands.std.WashingMachineCommand;

/**
 * This class hands you a factory which can create commands for items.
 * Although there is no obligation to use this class to create commands,
 * this should be the preferred way because this factory takes care of 
 * style integrity and other comforts.
 * 
 * This concrete implementation returns StdCommands, but you are free to write own
 * <i>CommandFactories</i>
 * 
 * @author langenhagen
 * @version 20120512
 */
public class CommandFactory {
	
	// CLASS VARS /////////////////////////////////////////////////////////////////////////////////
	
	/** singleton instance of this class */
	protected static CommandFactory instance;
	
	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
		
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main constructor.
	 */
	protected CommandFactory(){
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Gets the singleton instance of this factory.
	 * @return
	 * The singleton <i>CommandFactory</i> instance
	 */
	public static final CommandFactory instance(){
		return instance;
	}
	
	/**
	 * Sets up the factory according to a given Type of <i>CommandFactory</i>
	 * @param clazz
	 * The <i>Class</i> <i>CommandFactory</i> or some <i>Class</i> which extends it.
	 */
	public static final void setup( Class<? extends CommandFactory> clazz){
		System.out.print("Initializing CommandFactory...\n");
		try{
			instance = clazz.newInstance();
		} catch(Exception e){
			System.err.println("Error: Could not instantiate object of class " + clazz.getName());
			e.getMessage();
			e.printStackTrace();
		}
		System.out.print("Done!\n");
	}
	
	// PUBLIC FACTORY METHODS /////////////////////////////////////////////////////////////////////
	
		/**
	 * Creates a command of the specified type for the given item and
	 * attaches it automatically to the item.<br>
	 * Override this method when subclassing the factory.
	 * @param item
	 * The <i>Item</i> to which the command shall correspond.
	 * @param type
	 * @param device
	 * The <i>Physical</i> of the command to create. Usually the
	 * item's device itself or some subdevice ot the original device.
	 * @return
	 * Returns the new <i>DeviceCommand</i>.
	 */
	public DeviceCommand createCommand( Item item, PhysicalDevice device){
		
		DeviceCommand ret = null;
		
		DeviceType type = DeviceType.getDeviceType( device);
		
		switch(type){
		case Blind:
			ret = new BlindCommand( item, device);
			break;
		case Cooker:
			ret = new CookerCommand( item, device);
			break;
		case CookTop:
			ret = new CookTopCommand( item, device);
			break;
		case Dishwasher:
			ret = new DishwasherCommand( item, device);
			break;
		case Fan:
			ret = new FanCommand( item, device);
			break;
		case Fridge:
			ret = new FridgeCommand( item, device);
			break;
		case Heater:
			ret = new HeaterCommand( item, device);
			break;
		case Hob:
			ret = new HobCommand( item, device);
			break;
		case Hood:
			ret = new HoodCommand( item, device);
			break;
		case Lamp:
			ret = new LampCommand( item, device);
			break;
		case Mixer:
			ret = new MixerCommand( item, device);
			break;
		case Notebook:
			ret = new NotebookCommand( item, device);
			break;
		case Oven:
			ret = new OvenCommand( item, device);
			break;
		case PC:
			ret = new PCCommand( item, device);
			break;
		case Radio:
			ret = new RadioCommand( item, device);
			break;
		case RemoteControl:
			ret = new RemoteControlCommand( item, device);
			break;
		case TV:
			ret = new TVCommand( item, device);
			break;
		case Socket:
			ret = new SocketCommand( item, device);
			break;
		case Meter:
			ret = new MeterCommand( item, device);
			break;
		case WashingMachine:
			ret = new WashingMachineCommand( item, device);
			break;
		case DefaultDevice:
		default:
			System.err.println(
					"ERROR at:" + this.getClass() + ".createCommand()! Don't know the given " +
					"DeviceType \"" + type + "\" and therefore instanciate a Dummy DeviceCommand!");
			ret = new DummyCommand( item, device);
		}
		
		return ret;
	}
		/**
	 * Creates a command of the specified type for the given item and
	 * may attaches it automatically to the item.
	 * @param item
	 * The <i>Item</i> to which the command shall correspond.
	 * @param device
	 * The <i>Physical</i> of the command to create. Usually the
	 * item's device itself or some subdevice ot the original device.
	 * @param attachToItem
	 * Specifies, if the new <i>DeviceCommand</i> shall be attached to the
	 * specified <i>Item</i> automatically. If set to TRUE, this method
	 * automatically calls Item.setCommand() On the given <i>Item</i>.
	 * @return
	 * Returns the new <i>DeviceCommand</i>.
	 */
	public final DeviceCommand createCommand( Item item, PhysicalDevice device, boolean attachToItem){
		
		DeviceCommand ret = createCommand( item, device);
		
		// auto attach to item?
		if( attachToItem)
			item.setCommand( ret);
		
		return ret;
	}
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
}
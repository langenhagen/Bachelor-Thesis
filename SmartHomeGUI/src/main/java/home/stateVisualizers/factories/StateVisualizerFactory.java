package home.stateVisualizers.factories;

import util.DeviceType;
import home.Item;
import home.stateVisualizers.DummyVisualizer;
import home.stateVisualizers.StateVisualizer;
import home.stateVisualizers.std.BlindStateVisualizer;
import home.stateVisualizers.std.LampStateVisualizer;


/**
 * This class hands you a factory which can create state visualizers for items.
 * Although there is no obligation to use this class to create state visualizers,
 * this should be the preferred way because this factory takes care of 
 * style integrity and other comforts.
 * 
 * This concrete implementation returns <i>StateVisualizerObservers</i> of the Std-package, but you are free to write own
 * <i>StateVisualizerFactories</i>
 * 
 * @author langenhagen
 * @version 20120512
 */
public class StateVisualizerFactory {
	
	// CLASS VARS /////////////////////////////////////////////////////////////////////////////////
	
	/** singleton instance of this class */
	protected static StateVisualizerFactory instance;
	
	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
		
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main constructor.
	 */
	protected StateVisualizerFactory(){
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Gets the singleton instance of this factory.
	 * @return
	 * The singleton <i>StateVisualizerFactory</i> instance
	 */
	public static final StateVisualizerFactory instance(){
		return instance;
	}
	
	/**
	 * Sets up the factory according to a given Type of <i>StateVisualizerFactory</i>
	 * @param clazz
	 * The <i>Class</i> <i>StateVisualizerFactory</i> or some <i>Class</i> which extends it.
	 */
	public static final void setup( Class<? extends StateVisualizerFactory> clazz){
		System.out.print("Initializing StateVisualizerFactory...\n");
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
	 * Creates a state visualizer of the specified type for the given item and
	 * attaches it automatically to the item.<br>
	 * Override this method when subclassing the factory.
	 * @param item
	 * The <i>Item</i> to which the state visualizer shall correspond
	 * @param type
	 * The <i>DeviceType</i> of the command to create
	 * @return
	 * Returns the new <i>DeviceCommand</i>
	 */
	public StateVisualizer createStateVisualizer( Item item, DeviceType type){

		StateVisualizer ret = new DummyVisualizer( item);
		
		switch(type){
		case Blind:
			ret = new BlindStateVisualizer( item);
			break;
		case Cooker:
			//TODO
			break;
		case CookTop:
			//TODO
			break;
		case Dishwasher:
			//TODO
			break;
		case Fan:
			//TODO
			break;
		case Fridge:
			//TODO
			break;
		case Heater:
			//TODO
			break;
		case Hob:
			//TODO
			break;
		case Hood:
			//TODO
			break;
		case Lamp:
			ret = new LampStateVisualizer( item);
			break;
		case Mixer:
			//TODO
			break;
		case Notebook:
			//TODO
			break;
		case Oven:
			//TODO
			break;
		case PC:
			//TODO
			break;
		case Radio:
			//TODO
			break;
		case RemoteControl:
			//TODO
			break;
		case TV:
			//TODO
			break;
		case HeatingRod:
			//TODO
			break;
		case WaterStorageTank:
			//TODO
			break;
		case DefaultDevice:
		default:
			System.err.println(
					"ERROR at:" + this.getClass() + ".creatStateVisualizer()! Don't know the given " +
					"DeviceType \"" + type + "\" and therefore cannot instanciate a corresponding StateVisualizer!");
		}
		
		return ret;
	}
	
	
	/**
	 * Creates a state visualizer of the specified type for the given item and
	 * may attaches it automatically to the item.
	 * @param item
	 * The <i>Item</i> to which the state visualizer shall correspond
	 * @param type
	 * The <i>DeviceType</i> of the command to create.
	 * If the given <i>DeviceType</i> is not recognized by this method,
	 * an <i>DummyVisualizer</i> will be used.
	 * @param attachToItem
	 * Specifies, if the new <i>StateVisualizer</i> shall be attached to the
	 * specified <i>Item</i> automatically. If set to TRUE, this method
	 * automatically calls Item.setStateVisualizer() On the given <i>Item</i>.
	 * @return
	 * Returns a new <i>StateVisualizer</i>.
	 */
	public final StateVisualizer createStateVisualizer( Item item, DeviceType type, boolean attachToItem){
		
		StateVisualizer ret = createStateVisualizer( item, type);
		
		// auto attach to item?
		if( attachToItem)
			item.setStateVisualizer( ret);
		
		return ret;
	}
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
}
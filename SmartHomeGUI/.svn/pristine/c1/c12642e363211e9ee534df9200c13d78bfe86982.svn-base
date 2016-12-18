package home.stateVisualizers;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.sercho.masp.models.Context.PhysicalDevice;

import de.dailab.masp.models.Properties.PropertiesPackage;

import home.Item;

/**
 * Thiss abstract class defines the interface for all state visualizers.
 * These classes are intended to change the visual appearance of the items
 * according to their state. Classes of this type act as observers and
 * try to get all the information needed to display the correct state
 * through their corresponding <i>Items</i>.
 * 
 * @author langenhagen
 * @version 20111218
 */
public abstract class StateVisualizer extends AdapterImpl{
	
	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the corresponding item */
	protected Item item;
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////

	/**
	 * Main constructor
	 * @param item
	 * The <i>Item</i> to which this state visualizer belongs.
	 * This item should ideally be a lamp or something likely.
	 */
	public StateVisualizer( Item item){
		this.item = item;
		
		PhysicalDevice device = item.getDevice();
		
		// subscribe to power usage and On/Off property value changes
		if( device.getPowerUsage() != null){
			device.getPowerUsage().eAdapters().add( this);
		}
		if( device.getOn() != null){
			device.getOn().eAdapters().add( this);
		}
		if( device.getPosition() != null){
			item.getDevice().getPosition().eAdapters().add( this);
		}
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This method notifies this observer that some
	 * change has happened. The observer instance then
	 * finds the state change with help of its corresponding
	 * <i>Item</i> automatically and alters the visualisation
	 * of this item correspondingly.
	 */
	public abstract void notifyVisualizer();
	
	/**
	 * This method is an observer method that shall be called by the ContextModel
	 * if some value changed. Doesn't need to be invoked in some hand written code;
	 * use the notifyVisualizer() instead.
	 * @param msg
	 * A notification messag as a <i>Notification</i>.
	 */
	public void notifyChanged(final Notification msg) {
        super.notifyChanged(msg);
        
        //TODO: what should stand in the if clause?
        if(msg.getFeature() == PropertiesPackage.Literals.PROPERTY__VALUE) {
            this.notifyVisualizer();
        }
    }
}

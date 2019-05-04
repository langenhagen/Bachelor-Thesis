package home.deviceCommands.std;

import home.Item;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;
import org.sercho.masp.models.Context.Lamp;
import org.sercho.masp.models.Context.PhysicalDevice;

import widgets.MTSlider;
import widgets.factories.GUIFactory;


import de.dailab.masp.models.Properties.ActorServiceCallException;
import de.dailab.masp.models.Properties.IntegerProperty;


/**
 * A standard command for lamps.
 * 
 * @author langenhagen
 * @version 20120207
 */
public class LampCommand extends StdCommand {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the lamp device */
	Lamp lamp;
	/** the slider which is to manipulate the dimming level of the lamp */
	private MTSlider sliderDimmingLevel;
	/** the text area that informs the user of the purpose of the slider */
	private MTTextArea txtSliderInfo;
	
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
	public LampCommand(Item item, PhysicalDevice device){
		super( item, device);
		lamp = (Lamp)device;
	}
	
	// PROTECTED METHODS //////////////////////////////////////////////////////////////////////////

	@Override
	protected void executeHelper(){
		
		item.getView().addGestureListener( TapProcessor.class, new LampTapListener());
		
		// create GUI components
		
		txtSliderInfo = GUIFactory.instance().createTextArea( "Dimming level:" , "txtSliderInfo", pnlInteraction);
		txtSliderInfo.setPositionGlobal(
				new Vector3D( 
						btnOnOff.getPosition().getX(),
						this.getStartPosition().getY() + 30		
				));
		
		
		
		sliderDimmingLevel = GUIFactory.instance().createSlider( "sliderDimmingLevel", pnlInteraction);
		sliderDimmingLevel.setVertical( true);
		sliderDimmingLevel.setPosition(
				new Vector3D(
						txtSliderInfo.getPosition( TransformSpace.GLOBAL).getX() + pnlInteraction.getWidthXY( TransformSpace.GLOBAL)/2 - sliderDimmingLevel.getWidth()/2,
						txtSliderInfo.getPosition( TransformSpace.GLOBAL).getY() + txtSliderInfo.getHeightXY( TransformSpace.GLOBAL)
				));
		sliderDimmingLevel.addGestureListener( DragProcessor.class, new SliderDimmingLevelListener());
		sliderDimmingLevel.setValueRange( 0, 100);
			
		// bind observer
		if( lamp.getDimmingLevel() != null){
			lamp.getDimmingLevel().eAdapters().add( observer);
		}
	}

	
	@Override
	protected void ceaseHelper(){
		
		item.getView().removeListenerType( TapProcessor.class, LampTapListener.class);
		
		if( lamp.getDimmingLevel() != null){
			lamp.getDimmingLevel().eAdapters().remove( observer);
		}
	}
	
	@Override
	protected void setGUIAttributesHelper(){
		
		IntegerProperty dimmingLevel = lamp.getDimmingLevel();
		sliderDimmingLevel.setEnabled( dimmingLevel != null && dimmingLevel.getActor() != null);
		if( dimmingLevel != null && dimmingLevel.getValue() != null){
			sliderDimmingLevel.setValue( dimmingLevel.getValue());
		}else{
			float value = lamp.getOn() != null && lamp.getOnValue() == true ? 100 : 0;
			sliderDimmingLevel.setValue( value);
		}
	}
	
	// INNER CLASSES //////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This Tap-Listener will be activated, when one taps On the item in the 3D-picture.
	 * The tap will change the state of the lamp and will notify the state visualizer of the item.
	 * 
	 * @author langenhagen
	 * @version 20111218
	 */
	private class LampTapListener implements IGestureEventListener{

		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			if(ge.getId() == MTGestureEvent.GESTURE_STARTED){

				// turn On/Off
				try{
					if(device.getOnValue()){
						device.turnOff();
					}else{
						device.turnOn();
					}
				}catch(ActorServiceCallException e){
					e.printStackTrace();	

				}catch(IllegalStateException e){
					e.printStackTrace();
				}
				
				setGUIAttributes();
				item.getStateVisualizer().notifyVisualizer();
			}
			return false;
		}
	} // END LampTapListener
	
	
	/**
	 * This inner class provides the slider with the necessary
	 * logic to actually use the device.
	 * 
	 * @author langenhagen
	 * @version 20111221
	 */
	private class SliderDimmingLevelListener implements IGestureEventListener{

		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			if(ge.getId() == MTGestureEvent.GESTURE_ENDED){
				
				try{
					lamp.getDimmingLevel().requestValueUpdate( Integer.toString( (int)sliderDimmingLevel.getValue()));
				} catch(ActorServiceCallException e){
					e.printStackTrace();
				}
				setGUIAttributes();
				item.getStateVisualizer().notifyVisualizer();
			}
			return false;
		}
	} // END SliderDimmingLevelListener
}
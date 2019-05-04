package home.deviceCommands.std;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.math.Vector3D;
import org.sercho.masp.models.Context.Fan;
import org.sercho.masp.models.Context.PhysicalDevice;

import widgets.MTSlider;
import widgets.factories.GUIFactory;

import de.dailab.masp.models.Properties.ActorServiceCallException;

import home.Item;


/**
 * Standard command for fans.
 * 
 * @author langenhagen
 * @version 20120207
 */
public class FanCommand extends StdCommand {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the fan device */
	Fan fan;
	/** the slider with that one can set the speed of the fan */
	private MTSlider sliderSpeed;
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
	 * @param device
	 * The device that shall correspond to the item.
	 * Usually the <i>PhysicalDevice</i> of the item itself or a subdevice of it.
	 */
	public FanCommand(Item item, PhysicalDevice device){
		super( item, device);
		fan = (Fan)device;
	}
	
	// PROTECTED METHODS //////////////////////////////////////////////////////////////////////////

	@Override
	protected void executeHelper(){
		
		// create GUI components
		
		txtSliderInfo = GUIFactory.instance().createTextArea( "Fan speed:" , "txtSliderInfo", pnlInteraction);
		txtSliderInfo.setPositionGlobal(
				new Vector3D( 
						btnOnOff.getPosition().getX(),
						this.getStartPosition().getY() + 30	
				));
		
		
		sliderSpeed = GUIFactory.instance().createSlider( "sliderSpeed", pnlInteraction);
		sliderSpeed.setVertical( true);
		sliderSpeed.setPosition(
				new Vector3D(
						txtSliderInfo.getPosition( TransformSpace.GLOBAL).getX()  + pnlInteraction.getWidthXY( TransformSpace.GLOBAL)/2 - sliderSpeed.getWidth()/2,
						txtSliderInfo.getPosition( TransformSpace.GLOBAL).getY() + txtSliderInfo.getHeightXY( TransformSpace.GLOBAL)
				));
		sliderSpeed.addGestureListener( DragProcessor.class, new SliderSpeedListener());
		sliderSpeed.setMinValue( 0);
		sliderSpeed.setMaxValue( 100);
		
		// bind observer
		if( fan.getSpeed() != null){
			fan.getSpeed().eAdapters().add( observer);
		}
	}

	@Override
	protected void ceaseHelper(){
		
		if( fan.getSpeed() != null){
			fan.getSpeed().eAdapters().remove( observer);
		}
	}
	
	@Override
	protected void setGUIAttributesHelper(){
		
		sliderSpeed.setEnabled( fan.getSpeed() != null && fan.getSpeed().getActor() != null);
		if(fan.getSpeed()!=null && fan.getSpeed().getValue() != null){
			sliderSpeed.setValue( fan.getSpeed().getValue());
		}else{
			sliderSpeed.setValue( 0);
		}
	}
	
	// INNER CLASSES //////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This inner class provides the slider with the necessary
	 * logic to actually use the fan.
	 * 
	 * @author langenhagen
	 * @version 20111229
	 */
	private class SliderSpeedListener implements IGestureEventListener{

		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			if(ge.getId() == MTGestureEvent.GESTURE_ENDED){
				
				try{
					fan.getSpeed().requestValueUpdate( Integer.toString( (int)sliderSpeed.getValue()));
				} catch(ActorServiceCallException e){
					System.err.println(e.getMessage());
					e.printStackTrace();
				}
				setGUIAttributes();
				item.getStateVisualizer().notifyVisualizer();
			}
			return false;
		}
	}
	
}

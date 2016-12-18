package home.deviceCommands.std;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.math.Vector3D;
import org.sercho.masp.models.Context.Blind;
import org.sercho.masp.models.Context.PhysicalDevice;

import widgets.MTSlider;
import widgets.factories.GUIFactory;

import de.dailab.masp.models.Properties.ActorServiceCallException;

import home.Item;

/**
 * Standard command for Blinds.
 * 
 * @author langenhagen
 * @version 20120207
 */
public class BlindCommand extends StdCommand {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the blind device */
	Blind blind;
	/** the slider which is to manipulate the level of the blinds */
	private MTSlider sliderLevel;
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
	public BlindCommand(Item item, PhysicalDevice device){
		super( item, device);
		blind = (Blind)device;
	}

	// PROTECTED METHODS //////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void executeHelper(){		
		
		// create GUI components
		
		btnOnOff.setVisible( false);
		
		txtSliderInfo = GUIFactory.instance().createTextArea( "Blind level:" , "txtSliderInfo", pnlInteraction);
		txtSliderInfo.setPositionGlobal(
				new Vector3D( 
						btnOnOff.getPosition().getX(),
						this.getStartPosition().getY() + 30 )
				);
		
		
		sliderLevel = GUIFactory.instance().createSlider( "sliderLevel", pnlInteraction);
		sliderLevel.setVertical( true);
		sliderLevel.setPosition(
				new Vector3D(
						txtSliderInfo.getPosition( TransformSpace.GLOBAL).getX() + pnlInteraction.getWidthXY( TransformSpace.GLOBAL)/2 - sliderLevel.getWidth()/2,
						txtSliderInfo.getPosition( TransformSpace.GLOBAL).getY() + txtSliderInfo.getHeightXY( TransformSpace.GLOBAL)
				));
		sliderLevel.addGestureListener( DragProcessor.class, new SliderLevelListener());
		sliderLevel.setMinValue( 0);
		sliderLevel.setMaxValue( 100);
		
		// bind observer
		if( blind.getLevel() != null){
			blind.getLevel().eAdapters().add( observer);
		}
	}

	@Override
	protected void ceaseHelper(){
		
		if( blind.getLevel() != null){
			blind.getLevel().eAdapters().remove( observer);
		}
	}
	
	@Override
	protected void setGUIAttributesHelper(){
		
		sliderLevel.setEnabled( blind.getLevel() != null && blind.getLevel().getActor() != null);
		if(blind.getLevel()!=null && blind.getLevel().getValue() != null){
			sliderLevel.setValue( (int)sliderLevel.getMaxValue() - blind.getLevel().getValue());
		}else{
			sliderLevel.setValue( 0);
		}
	}
	
	// INNER CLASSES //////////////////////////////////////////////////////////////////////////////

	/**
	 * This inner class provides the slider with the necessary
	 * logic to be actually used.
	 * 
	 * @author langenhagen
	 * @version 20111222
	 */
	private class SliderLevelListener implements IGestureEventListener{
		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			if(ge.getId() == MTGestureEvent.GESTURE_ENDED){

				try{
					blind.getLevel().requestValueUpdate( Integer.toString( (int)sliderLevel.getMaxValue() - (int)sliderLevel.getValue()));
				}catch(ActorServiceCallException e){
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
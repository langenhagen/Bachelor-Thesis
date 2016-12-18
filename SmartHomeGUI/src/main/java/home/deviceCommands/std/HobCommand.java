package home.deviceCommands.std;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.math.Vector3D;
import org.sercho.masp.models.Context.Hob;
import org.sercho.masp.models.Context.PhysicalDevice;

import widgets.MTSlider;
import widgets.factories.GUIFactory;

import de.dailab.masp.models.Properties.ActorServiceCallException;

import home.Item;

/**
 * A standard command for hobs.
 * 
 * @author langenhagen
 * @version 20120207
 */
public class HobCommand extends StdCommand {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** The hob device */
	private Hob hob;
	/** the slider which is to manipulate the heat level of the hob */
	private MTSlider sliderHeatLevel;
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
	public HobCommand(Item item, PhysicalDevice device){
		super( item, device);
		hob = (Hob)device;
	}

	// PROTECTED METHODS //////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void executeHelper(){
		
		// create GUI components
		
		txtSliderInfo = GUIFactory.instance().createTextArea( "Heat level: n/a" , "txtSliderInfo", pnlInteraction);
		txtSliderInfo.setPositionGlobal(
				new Vector3D(
						btnOnOff.getPosition( TransformSpace.GLOBAL).getX(),
						this.getStartPosition().getY() + 30));


		sliderHeatLevel = GUIFactory.instance().createSlider( "sliderHeatLevel", pnlInteraction);
		sliderHeatLevel.setVertical( true);
		sliderHeatLevel.setPosition(
				new Vector3D(
						txtSliderInfo.getPosition( TransformSpace.GLOBAL).getX() + pnlInteraction.getWidthXY( TransformSpace.GLOBAL)/2 - sliderHeatLevel.getWidth()/2,
						txtSliderInfo.getPosition( TransformSpace.GLOBAL).getY() + txtSliderInfo.getHeightXY( TransformSpace.GLOBAL)
					));
		sliderHeatLevel.addGestureListener( DragProcessor.class, new SliderHeatLevelListener());
		
		// TODO: find out, what the min/max values are!
		//sliderHeatLevel.setMinValue( 0);
		//sliderHeatLevel.setMaxValue( 100);
		
		// bind observer
		if( hob.getHeatLevel() != null){
			hob.getHeatLevel().eAdapters().add( observer);
		}
	}

	@Override
	protected void ceaseHelper(){
		
		if( hob.getHeatLevel() != null){
			hob.getHeatLevel().eAdapters().remove( observer);
		}
	}
	
	@Override
	protected void setGUIAttributesHelper(){
		
		sliderHeatLevel.setEnabled( hob.getHeatLevel() != null && hob.getHeatLevel().getActor() != null);
		if(hob.getHeatLevel()!=null && hob.getHeatLevel().getValue() != null ){
			txtSliderInfo.setText( "Heat level: " + hob.getHeatLevel().getValue());
			sliderHeatLevel.setValue( hob.getHeatLevel().getValue());
		}else{
			txtSliderInfo.setText( "Heat level: n/a");
			sliderHeatLevel.setValue( 0);
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
	private class SliderHeatLevelListener implements IGestureEventListener{

		@Override
		public boolean processGestureEvent(MTGestureEvent ge){

			if(ge.getId() == MTGestureEvent.GESTURE_ENDED){
				
				try{
					hob.getHeatLevel().requestValueUpdate( Integer.toString( 666 /*TODO*/));
				}catch(ActorServiceCallException e){
					System.err.println(e.getMessage());
					e.printStackTrace();
				}
				
				setGUIAttributes();
				item.getStateVisualizer().notifyVisualizer();
			}
			return false;
		}
	} // END SliderHeatLevelListener
}

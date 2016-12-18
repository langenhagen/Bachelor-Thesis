package home.deviceCommands.std;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;
import org.sercho.masp.models.Context.Fridge;
import org.sercho.masp.models.Context.PhysicalDevice;

import widgets.MTButton;
import widgets.factories.GUIFactory;

import de.dailab.masp.models.Properties.ActorServiceCallException;

import home.Item;

/**
 * A standard command for fridges.
 * 
 * @author langenhagen
 * @version 20120207
 */
public class FridgeCommand extends StdCommand {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the fridge device */
	Fridge fridge;
	/** this text area shows the current temperature */
	MTTextArea txtInfo;
	/** the button that allows to rise the temperature */
	MTButton btnTempDown;
	/** the button that allows to lower the temperature */
	MTButton btnTempUp;
	
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
	public FridgeCommand(Item item, PhysicalDevice device){
		super( item, device);
		fridge = (Fridge)device;
	}

	// PROTECTED METHODS //////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void executeHelper(){
		
		// create GUI components
		
		txtInfo = GUIFactory.instance().createTextArea( "Temperature: n/a" , "txtInfo", pnlInteraction);
		txtInfo.setPositionGlobal(
				new Vector3D( 
						btnOnOff.getPosition().getX(),
						this.getStartPosition().getY() + 30	
				));
		
		
		btnTempUp = GUIFactory.instance().createButton( "UP", "btnProgramUp", pnlInteraction);
		btnTempUp.setPosition(
				new Vector3D( 
						btnOnOff.getPosition().getX(),
						txtInfo.getPosition( TransformSpace.GLOBAL).getY() + txtInfo.getHeightXY( TransformSpace.GLOBAL) + 10	
				));
		btnTempUp.addGestureListener( TapProcessor.class, new BtnTempUpListener());

		
		btnTempDown = GUIFactory.instance().createButton( "DOWN", "btnProgramDown", pnlInteraction);
		btnTempDown.setPosition( btnTempUp.getVectorNextToComponent( 10, false) );
		btnTempDown.addGestureListener( TapProcessor.class, new BtnTempDownListener());
		
		// bind observer
		if( fridge.getTemperature() != null){
			fridge.getTemperature().eAdapters().add( observer);
		}
	}

	@Override
	protected void ceaseHelper(){
		
		if( fridge.getTemperature() != null){
			fridge.getTemperature().eAdapters().remove( observer);
		}
	}
	
	@Override
	protected void setGUIAttributesHelper(){
		
		if( fridge.getTemperature() != null){
			txtInfo.setText( "Temperature: " + fridge.getTemperature().getValue());
		}
		
		boolean enabled = fridge.getTemperature() != null && fridge.getTemperature().getActor() != null;
		btnTempUp.setEnabled( enabled);
		btnTempDown.setEnabled( enabled);
		
	}
	
	// INNER CLASSES //////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This inner class provides the up-button with the necessary
	 * logic to be actually used.
	 * 
	 * @author langenhagen
	 * @version 20111222
	 */
	private class BtnTempUpListener implements IGestureEventListener{

		@Override
		public boolean processGestureEvent(MTGestureEvent ge){

			if( ge.getId() == MTGestureEvent.GESTURE_STARTED){
				
				try{
					fridge.getTemperature().requestValueUpdate( Integer.toString( fridge.getTemperature().getValue() + 1));
					
				}catch(ActorServiceCallException e){
					e.printStackTrace();
				}
				
				try{
					txtInfo.setText( "Temperature: " + fridge.getTemperature().getValue() + "�C");
					
				}catch(Exception e){
					System.err.println( e.getMessage());
					e.printStackTrace();
					
					txtInfo.setText( "Temperature: n/a");
				}
				setGUIAttributes();
				item.getStateVisualizer().notifyVisualizer();
			}
			return false;
		}
	} // END BtnTempUpListener
	
	
	/**
	 * This inner class provides the down-button with the necessary
	 * logic to be actually used.
	 * 
	 * @author langenhagen
	 * @version 20111222
	 */
	private class BtnTempDownListener implements IGestureEventListener{

		@Override
		public boolean processGestureEvent(MTGestureEvent ge){

			if( ge.getId() == MTGestureEvent.GESTURE_STARTED){

				try{
				
					fridge.getTemperature().requestValueUpdate( Integer.toString( fridge.getTemperature().getValue() - 1));
					
				}catch(ActorServiceCallException e){
					e.printStackTrace();
				}
				
				try{
					txtInfo.setText( "Temperature: " + fridge.getTemperature().getValue() + "�C");
					
				}catch(Exception e){
					e.printStackTrace();
					
					txtInfo.setText( "Temperature: n/a");
				}
				setGUIAttributes();
				item.getStateVisualizer().notifyVisualizer();
			}
			return false;
		}
	} // END BtnTempDownListener
}

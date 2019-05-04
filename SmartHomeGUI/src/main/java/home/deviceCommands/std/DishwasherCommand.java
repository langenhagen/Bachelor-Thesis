package home.deviceCommands.std;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.widgets.MTSuggestionTextArea;
import org.sercho.masp.models.Context.Dishwasher;
import org.sercho.masp.models.Context.PhysicalDevice;

import widgets.MTButton;
import widgets.factories.GUIFactory;

import de.dailab.masp.models.Properties.ActorServiceCallException;

import home.Item;

/**
 * A standard command for dishwashers.
 * 
 * @author langenhagen
 * @version 20120207
 */
public class DishwasherCommand extends StdCommand {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the dishwasher device */
	Dishwasher dishwasher;
	/** the text area that informs the user of the purpose of the input text area */
	private MTTextArea txtProgramInfo;
	/** the input field, where the user the name of the program can enter or view */
	private MTSuggestionTextArea txtInputProgram;
	/** sets the program according to the specified program in the input fiesld */
	private MTButton btnSetProgram;
	/** the text area that informs the user of the purpose of the up/down buttons */
	private MTTextArea txtSecondsInfo;
	/** the button which is able to raise the seconds remaining */
	private MTButton btnSecondsUp;
	/** the button which is able to decrease the seconds remaining */
	private MTButton btnSecondsDown;
	
	
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
	public DishwasherCommand(Item item, PhysicalDevice device){
		super( item, device);
		dishwasher = (Dishwasher)device;
	}
	
	// PROTECTED METHODS //////////////////////////////////////////////////////////////////////////

	@Override
	protected void executeHelper(){
		
		// create GUI components
		
		txtProgramInfo = GUIFactory.instance().createTextArea( "Current Program:" , "txtProgramInfo", pnlInteraction);
		txtProgramInfo.setPositionGlobal(
				new Vector3D( 
						btnOnOff.getPosition().getX(),
						this.getStartPosition().getY() + 30	
				));
		

		txtInputProgram = GUIFactory.instance().createInputField( "txtInputProgram", pnlInteraction);
		txtInputProgram.setPositionGlobal(
				new Vector3D(
						txtProgramInfo.getPosition( TransformSpace.GLOBAL).getX(),
						txtProgramInfo.getPosition( TransformSpace.GLOBAL).getY() + txtProgramInfo.getHeightXY( TransformSpace.GLOBAL) - 30
				));
		txtInputProgram.setTextPositionRounding( true);
		
		btnSetProgram = GUIFactory.instance().createButton( "Set program", "btnSetProgram", pnlInteraction);
		btnSetProgram.setPosition(
				new Vector3D(
						txtInputProgram.getPosition( TransformSpace.GLOBAL).getX(),
						txtInputProgram.getPosition( TransformSpace.GLOBAL).getY() + txtInputProgram.getHeightXY( TransformSpace.GLOBAL) + 10	
				));
		btnSetProgram.addGestureListener( TapProcessor.class, new BtnSetProgramListener());
		
		
		txtSecondsInfo = GUIFactory.instance().createTextArea( "Seconds remaining: n/a" , "txtSecondsInfo", pnlInteraction);
		txtSecondsInfo.setPositionGlobal( btnSetProgram.getVectorNextToComponent( 20, false));


		btnSecondsUp = GUIFactory.instance().createButton( "UP", "btnSecondsUp", pnlInteraction);
		btnSecondsUp.setPosition(
				new Vector3D(
						txtSecondsInfo.getPosition( TransformSpace.GLOBAL).getX(),
						txtSecondsInfo.getPosition( TransformSpace.GLOBAL).getY() + txtSecondsInfo.getHeightXY( TransformSpace.GLOBAL)
				));
		btnSecondsUp.addGestureListener( TapProcessor.class, new BtnSecondsUpListener() );
		

		btnSecondsDown = GUIFactory.instance().createButton( "DOWN", "btnSecondsDown", pnlInteraction);
		btnSecondsDown.setPosition( btnSecondsUp.getVectorNextToComponent( 10, false));
		btnSecondsDown.addGestureListener( TapProcessor.class, new BtnSecondsDownListener() );
		
		// bind observer
		if( dishwasher.getProgram() != null){
			dishwasher.getProgram().eAdapters().add( observer);
		}
		if( dishwasher.getSecondsRemaining() != null){
			dishwasher.getSecondsRemaining().eAdapters().add( observer);
		}
	}

	@Override
	protected void ceaseHelper(){
		
		if( dishwasher.getProgram() != null){
			dishwasher.getProgram().eAdapters().remove( observer);
		}
		if( dishwasher.getSecondsRemaining() != null){
			dishwasher.getSecondsRemaining().eAdapters().remove( observer);
		}
	}
	
	@Override
	protected void setGUIAttributesHelper(){
		
		if( dishwasher.getProgram() != null && dishwasher.getProgram().getActor() != null){
			txtInputProgram.setText( " " + dishwasher.getProgram().getValue());
		}else{
			txtInputProgram.setText( "n/a");
			txtInputProgram.setEnabled( false);
			btnSetProgram.setEnabled( false);
		}
		
		boolean enabled = dishwasher.getSecondsRemaining() != null && dishwasher.getSecondsRemaining().getActor() != null;
		btnSecondsUp.setEnabled(  enabled);
		btnSecondsDown.setEnabled( enabled);
	}
	
	// INNER CLASSES //////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * This inner class provides the set program-button with the necessary
	 * logic to actually use the device.
	 * 
	 * @author langenhagen
	 * @version 20111230
	 */
	private class BtnSetProgramListener implements IGestureEventListener{

		@Override
		public boolean processGestureEvent(MTGestureEvent ge){

			if( ge.getId() == MTGestureEvent.GESTURE_STARTED){
				
				try{
					dishwasher.getProgram().requestValueUpdate( txtInputProgram.getText());
				} catch(ActorServiceCallException e){
					e.printStackTrace();
				}
				System.out.println( device.getId() + ": Set program to: " + dishwasher.getProgram());
				setGUIAttributes();
				item.getStateVisualizer().notifyVisualizer();
			}
			return false;
		}
	} // END BtnSetProgramListener
	
	
	/**
	 * This inner class provides the secunds up-button with the necessary
	 * logic to actually use the device.
	 * 
	 * @author langenhagen
	 * @version 20111230
	 */
	private class BtnSecondsUpListener implements IGestureEventListener{

		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			try{
				dishwasher.getSecondsRemaining().requestValueUpdate( Integer.toString( dishwasher.getSecondsRemaining().getValue() + 1));
			} catch(ActorServiceCallException e){
				e.printStackTrace();
			}
			System.out.println(dishwasher.getId() + ": Seconds Remaining: " + dishwasher.getSecondsRemaining().getValue());
			setGUIAttributes();
			item.getStateVisualizer().notifyVisualizer();
			
			return false;
		}	
	} // END BtnSecondsUpListener
	
	
	/**
	 * This inner class provides the set seconds down-button with the necessary
	 * logic to actually use the device.
	 * 
	 * @author langenhagen
	 * @version 20111230
	 */
	private class BtnSecondsDownListener implements IGestureEventListener{

		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			try{
				dishwasher.getSecondsRemaining().requestValueUpdate( Integer.toString( dishwasher.getSecondsRemaining().getValue() - 1));
			} catch(ActorServiceCallException e){
				e.printStackTrace();
			}
			System.out.println(dishwasher.getId() + ": Seconds Remaining: " + dishwasher.getSecondsRemaining().getValue());
			setGUIAttributes();
			item.getStateVisualizer().notifyVisualizer();
			
			return false;
		}
	} // END BtnSecondsDownListener
}

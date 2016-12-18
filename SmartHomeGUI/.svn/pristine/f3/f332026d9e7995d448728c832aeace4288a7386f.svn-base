package home.deviceCommands.std;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.widgets.MTSuggestionTextArea;
import org.sercho.masp.models.Context.PhysicalDevice;
import org.sercho.masp.models.Context.WashingMachine;

import widgets.MTButton;
import widgets.factories.GUIFactory;

import de.dailab.masp.models.Properties.ActorServiceCallException;

import home.Item;

/**
 * A standard command for washing machines.
 * 
 * @author langenhagen
 * @version 20120207
 */
public class WashingMachineCommand extends StdCommand {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the washing machine device */
	WashingMachine washingmachine;
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
	public WashingMachineCommand(Item item, PhysicalDevice device){
		super( item, device);
		washingmachine = (WashingMachine)device;
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
						txtProgramInfo.getPosition( TransformSpace.GLOBAL).getY() + txtProgramInfo.getHeightXY( TransformSpace.GLOBAL) + 10
				));
		
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
		if( washingmachine.getProgram() != null){
			washingmachine.getProgram().eAdapters().add( observer);
		}
		if( washingmachine.getSecondsRemaining() != null){
			washingmachine.getSecondsRemaining().eAdapters().add( observer);
		}
	}
	
	@Override
	protected void ceaseHelper(){
		
		if( washingmachine.getProgram() != null){
			washingmachine.getProgram().eAdapters().remove( observer);
		}
		if( washingmachine.getSecondsRemaining() != null){
			washingmachine.getSecondsRemaining().eAdapters().remove( observer);
		}
	}
	
	@Override
	protected void setGUIAttributesHelper(){
		
		if( washingmachine.getProgram() != null && washingmachine.getProgram().getActor() != null ){
			txtInputProgram.setText( "Program: " + washingmachine.getProgram().getValue());
		}else{
			txtInputProgram.setText( "Program: n/a");
			txtInputProgram.setEnabled( false);
			btnSetProgram.setEnabled( false);
		}
		
		boolean enabled = washingmachine.getSecondsRemaining() != null && washingmachine.getSecondsRemaining().getActor() != null;
		btnSecondsUp.setEnabled( enabled);
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
					washingmachine.getProgram().requestValueUpdate( txtInputProgram.getText());
				} catch(ActorServiceCallException e){
					e.printStackTrace();
				}
				System.out.println( device.getId() + ": Set program to: " + washingmachine.getProgram());
				
				setGUIAttributes();
				item.getStateVisualizer().notifyVisualizer();
			}
			return false;
		}
	} // END BtnSetProgramListener
	
	
	/**
	 * This inner class provides the seconds up-button with the necessary
	 * logic to actually use the device.
	 * 
	 * @author langenhagen
	 * @version 20111230
	 */
	private class BtnSecondsUpListener implements IGestureEventListener{

		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			try{
				washingmachine.getSecondsRemaining().requestValueUpdate( Integer.toString( washingmachine.getSecondsRemaining().getValue() + 1));
			} catch(ActorServiceCallException e){
				e.printStackTrace();
			}
			System.out.println(washingmachine.getId() + ": Seconds Remaining: " + washingmachine.getSecondsRemaining().getValue());
			
			setGUIAttributes();
			item.getStateVisualizer().notifyVisualizer();
			
			return false;
		}
	} // END BtnSecondsUpListener
	
	
	/**
	 * This inner class provides the seconds down-button with the necessary
	 * logic to actually use the device.
	 * 
	 * @author langenhagen
	 * @version 20111230
	 */
	private class BtnSecondsDownListener implements IGestureEventListener{

		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			try{
				washingmachine.getSecondsRemaining().requestValueUpdate( Integer.toString( washingmachine.getSecondsRemaining().getValue() - 1));
			} catch(ActorServiceCallException e){
				e.printStackTrace();
			}
			System.out.println(washingmachine.getId() + ": Seconds Remaining: " + washingmachine.getSecondsRemaining().getValue());
			
			setGUIAttributes();
			item.getStateVisualizer().notifyVisualizer();
			
			return false;
		}
	} // END BtnSecondsDownListener
	
}
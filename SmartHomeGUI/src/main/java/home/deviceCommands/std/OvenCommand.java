package home.deviceCommands.std;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.widgets.MTSuggestionTextArea;
import org.sercho.masp.models.Context.Oven;
import org.sercho.masp.models.Context.PhysicalDevice;

import widgets.MTButton;
import widgets.MTSlider;
import widgets.factories.GUIFactory;

import de.dailab.masp.models.Properties.ActorServiceCallException;

import home.Item;

/**
 * A standard command for ovens.
 * 
 * @author langenhagen
 * @version 20120207
 */
public class OvenCommand extends StdCommand {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the oven device */
	private Oven oven;
	/** because the space for the gui components is not sufficient we add some space with this Panel */
	private MTRectangle pnlTemperature;
	/** the text area that informs the user of the purpose of the slider */
	private MTTextArea txtTempInfo;
	/** the slider which is abled to adjust the temperature */
	private MTSlider sliderTemperature;
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
	public OvenCommand(Item item, PhysicalDevice device){
		super( item, device);
		oven = (Oven)device;
	}

	// PROTECTED METHODS //////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void executeHelper(){
		
		// create GUI components
		
		GUIFactory guifactory = GUIFactory.instance();
		float outerXPos = pnlPanel.getPosition( TransformSpace.GLOBAL).getX() - 220;
		
		txtTempInfo = guifactory.createTextArea( "Temperature: n/a", "txtTempInfo", null);
		txtTempInfo.setPositionGlobal(
				new Vector3D(
						outerXPos + 10,
						this.getStartPosition().getY() + 20 + 10		
				));


		sliderTemperature = guifactory.createSlider( "sliderTemperature", null);
		Vector3D txtTempInfoPos = txtTempInfo.getPosition( TransformSpace.GLOBAL);
		sliderTemperature.setPosition(
				new Vector3D(
						txtTempInfoPos.getX() + txtTempInfo.getWidthXY( TransformSpace.GLOBAL)/2,
						txtTempInfoPos.getY() + txtTempInfo.getHeightXY( TransformSpace.GLOBAL) + 10
				));
		sliderTemperature.addGestureListener( DragProcessor.class, new SliderTemperatureListener());

		
		pnlTemperature = guifactory.createPanel(
				outerXPos,
				this.getStartPosition().getY() + 20,
				pnlPanel.getPosition( TransformSpace.GLOBAL).getX() - outerXPos,
				txtTempInfo.getHeightXY( TransformSpace.GLOBAL) + sliderTemperature.getHeight() + 40,
				"pnlTemperature",
				pnlInteraction
		);
		pnlTemperature.addChild( txtTempInfo);
		pnlTemperature.addChild( sliderTemperature);
		
		
		txtProgramInfo = guifactory.createTextArea( "Current Program:" , "txtProgramInfo", pnlInteraction);
		txtProgramInfo.setPositionGlobal(
				new Vector3D( 
						btnOnOff.getPosition().getX(),
						this.getStartPosition().getY() + 30	
				));
	

		txtInputProgram = guifactory.createInputField( "txtInputProgram", pnlInteraction);
		txtInputProgram.setPositionGlobal(
				new Vector3D(
						txtProgramInfo.getPosition( TransformSpace.GLOBAL).getX(),
						txtProgramInfo.getPosition( TransformSpace.GLOBAL).getY() + txtProgramInfo.getHeightXY( TransformSpace.GLOBAL) - 30
				));
		txtInputProgram.setTextPositionRounding( true);
		
		
		btnSetProgram = guifactory.createButton( "Set program", "btnSetProgram", pnlInteraction);
		btnSetProgram.setPosition(
				new Vector3D(
						txtInputProgram.getPosition( TransformSpace.GLOBAL).getX(),
						txtInputProgram.getPosition( TransformSpace.GLOBAL).getY() + txtInputProgram.getHeightXY( TransformSpace.GLOBAL) + 10	
				));
		btnSetProgram.addGestureListener( TapProcessor.class, new BtnSetProgramListener() );
		
		
		txtSecondsInfo = guifactory.createTextArea( "Seconds remaining: n/a" , "txtSecondsInfo", pnlInteraction);
		txtSecondsInfo.setPositionGlobal( btnSetProgram.getVectorNextToComponent( 20, false));


		btnSecondsUp = guifactory.createButton( "UP", "btnSecondsUp", pnlInteraction);
		btnSecondsUp.setPosition(
				new Vector3D(
						txtSecondsInfo.getPosition( TransformSpace.GLOBAL).getX(),
						txtSecondsInfo.getPosition( TransformSpace.GLOBAL).getY() + txtSecondsInfo.getHeightXY( TransformSpace.GLOBAL)
				));
		btnSecondsUp.addGestureListener( TapProcessor.class, new BtnSecondsUpListener() );
		

		btnSecondsDown = guifactory.createButton( "DOWN", "btnSecondsDown", pnlInteraction);
		btnSecondsDown.setPosition( btnSecondsUp.getVectorNextToComponent( 10, false));
		btnSecondsDown.addGestureListener( TapProcessor.class, new BtnSecondsDownListener() );	
		
		// bind observer
		if( oven.getTemperature() != null){
			oven.getTemperature().eAdapters().add( observer);
		}
		if( oven.getProgram() != null){
			oven.getProgram().eAdapters().add( observer);
		}
		if( oven.getSecondsRemaining() != null){
			oven.getSecondsRemaining().eAdapters().add( observer);
		}
	}

	@Override
	protected void ceaseHelper(){
		
		if( oven.getTemperature() != null){
			oven.getTemperature().eAdapters().remove( observer);
		}
		if( oven.getProgram() != null){
			oven.getProgram().eAdapters().remove( observer);
		}
		if( oven.getSecondsRemaining() != null){
			oven.getSecondsRemaining().eAdapters().remove( observer);
		}
	}
	

	@Override
	protected void setGUIAttributesHelper(){
		
		if( oven.getTemperature() != null && oven.getTemperature().getActor() != null){
			txtTempInfo.setText( "Temperature: " + oven.getTemperature().getValue() + "�C");
		}else{
			sliderTemperature.setEnabled( false);
		}
		
		if( oven.getProgram() != null && oven.getProgram().getActor() != null){
			txtInputProgram.setText( "" + oven.getProgram().getValue());
		}else{
			txtInputProgram.setText( "n/a");
			txtInputProgram.setEnabled( false);
			btnSetProgram.setEnabled( false);
		}
		
		boolean enabled = oven.getSecondsRemaining() != null && oven.getSecondsRemaining().getActor() != null;
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
					oven.getProgram().requestValueUpdate( txtInputProgram.getText());
				} catch(ActorServiceCallException e){
					e.printStackTrace();
				}
				System.out.println( device.getId() + ": Set program to: " + oven.getProgram());
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
				oven.getSecondsRemaining().requestValueUpdate( Integer.toString( oven.getSecondsRemaining().getValue() + 1));
			} catch(ActorServiceCallException e){
				e.printStackTrace();
			}
			System.out.println(oven.getId() + ": Seconds Remaining: " + oven.getSecondsRemaining().getValue());
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
				oven.getSecondsRemaining().requestValueUpdate( Integer.toString( oven.getSecondsRemaining().getValue() - 1));
			} catch(ActorServiceCallException e){
				e.printStackTrace();
			}
			System.out.println(oven.getId() + ": Seconds Remaining: " + oven.getSecondsRemaining().getValue());
			setGUIAttributes();
			item.getStateVisualizer().notifyVisualizer();
			
			return false;
		}
	} // END BtnSecondsDownListener
	
	
	/**
	 * This inner class provides the slider with the necessary
	 * logic to actually use the device.
	 * 
	 * @author langenhagen
	 * @version 20111230
	 */
	private class SliderTemperatureListener implements IGestureEventListener{

		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			if(ge.getId() == MTGestureEvent.GESTURE_ENDED){
				
				try{
					oven.getTemperature().requestValueUpdate( Integer.toString( (int)sliderTemperature.getValue()));
					txtTempInfo.setText( "Temperature: " + oven.getTemperature().getValue() + "�C");
				} catch(ActorServiceCallException e){
					e.printStackTrace();
				}
				setGUIAttributes();
				item.getStateVisualizer().notifyVisualizer();
			}
			return false;
		}
	} // END SliderTemperatureListener
}
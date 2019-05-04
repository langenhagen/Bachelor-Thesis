package home.deviceCommands.std;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;
import org.sercho.masp.models.Context.PhysicalDevice;
import org.sercho.masp.models.Context.TV;

import widgets.MTButton;
import widgets.factories.GUIFactory;

import de.dailab.masp.models.Properties.ActorServiceCallException;

import home.Item;

/**
 * A standard command for TVs.
 * 
 * @author langenhagen
 * @version 20120207
 */
public class TVCommand extends StdCommand {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the tv device */
	TV tv;
	/** the button which is able to change the program upwards */
	private MTButton btnProgramUp;
	/** the button which is able to change the program downwards*/
	private MTButton btnProgramDown;
	/** the text area that informs the user of the purpose of the button */
	private MTTextArea txtInfo;
	
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
	public TVCommand(Item item, PhysicalDevice device){
		super( item, device);
		tv = (TV)device;
	}

	// PROTECTED METHODS //////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void executeHelper(){
		
		// create GUI components
		
		txtInfo = GUIFactory.instance().createTextArea( "Current Program: n/a" , "txtInfo", pnlInteraction);
		txtInfo.setPositionGlobal(
				new Vector3D( 
						btnOnOff.getPosition().getX(),
						this.getStartPosition().getY() + 30 )
				);
		
		
		btnProgramUp = GUIFactory.instance().createButton( "UP", "btnProgramUp", pnlInteraction);
		btnProgramUp.setPosition(
				new Vector3D(
						txtInfo.getPosition( TransformSpace.GLOBAL).getX(),
						txtInfo.getPosition( TransformSpace.GLOBAL).getY() + txtInfo.getHeightXY( TransformSpace.GLOBAL)
				));
		btnProgramUp.addGestureListener( TapProcessor.class, new BtnProgramUpListener());
		
		
		btnProgramDown = GUIFactory.instance().createButton( "DOWN", "btnProgramDown", pnlInteraction);
		btnProgramDown.setPosition( btnProgramUp.getVectorNextToComponent( 10, false));
		btnProgramDown.addGestureListener( TapProcessor.class, new BtnProgramDownListener());
				
		// bind observer
		if( tv.getCurrentProgram() != null){
			tv.getCurrentProgram().eAdapters().add( observer);
		}
	}

	@Override
	protected void ceaseHelper(){
		
		if( tv.getCurrentProgram() != null){
			tv.getCurrentProgram().eAdapters().remove( observer);
		}
	}
	
	@Override
	protected void setGUIAttributesHelper(){
		
		if( tv.getCurrentProgram() != null){
			txtInfo.setText( "Current Program: " + tv.getCurrentProgram().getValue());
		}
		
		boolean enabled = tv.getCurrentProgram() != null && tv.getCurrentProgram().getActor() != null;
		btnProgramUp.setEnabled( enabled);
		btnProgramDown.setEnabled( enabled);
	}
	
	// INNER CLASSES //////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This inner class provides the up-button with the necessary
	 * logic to actually use the TV.
	 * 
	 * @author langenhagen
	 * @version 20111222
	 */
	private class BtnProgramUpListener implements IGestureEventListener{

		@Override
		public boolean processGestureEvent(MTGestureEvent ge){

			if(ge.getId() == MTGestureEvent.GESTURE_STARTED){
								
				try{	
					tv.getCurrentProgram().requestValueUpdate( Integer.toString( tv.getCurrentProgram().getValue()+1));
				
				}catch(ActorServiceCallException e){
					e.printStackTrace();
				}
				
				setGUIAttributes();
				item.getStateVisualizer().notifyVisualizer();
			}
			return false;
		}
	} // END BtnProgramUpListener
	
	
	/**
	 * This inner class provides the down-button with the necessary
	 * logic to actually use the TV.
	 * 
	 * @author langenhagen
	 * @version 20111222
	 */
	private class BtnProgramDownListener implements IGestureEventListener{
		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){

			if( ge.getId() == MTGestureEvent.GESTURE_STARTED){
							
				try{
					if(tv.getCurrentProgram().getValue() < 1){
						return false;
					}
					
					tv.getCurrentProgram().requestValueUpdate( Integer.toString( tv.getCurrentProgram().getValue()-1));
					
				}catch(ActorServiceCallException e){
					e.printStackTrace();
				}
				
				setGUIAttributes();
				item.getStateVisualizer().notifyVisualizer();
			}
			return false;
		}
	} // END BtnProgramDownListener
}

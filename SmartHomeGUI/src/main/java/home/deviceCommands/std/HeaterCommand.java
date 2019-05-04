package home.deviceCommands.std;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.math.Vector3D;
import org.sercho.masp.models.Context.Heater;
import org.sercho.masp.models.Context.PhysicalDevice;

import widgets.MTSlider;
import widgets.factories.GUIFactory;

import de.dailab.masp.models.Properties.ActorServiceCallException;
import de.dailab.masp.models.Properties.DoubleProperty;

import home.Item;

/**
 * A standard command for heaters.
 * 
 * @author langenhagen
 * @version 20120207
 */
public class HeaterCommand extends StdCommand {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the heater device */
	Heater heater;
	/** the Panel containing the sliders */
	MTRectangle pnlHeater;
	/** the text area that informs the user of the purpose of all the sliders */
	MTTextArea txtSlidersInfo;
	/** the slider with that the user can regulate the demanded temperature */
	MTSlider sliderTempDemand;
	/** the slider with that the user can regulate the current temperature */
	MTSlider sliderTempCurrent;
	/** the slider with that the user can regulate the valve position */
	MTSlider sliderValvePosition;
	
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
	public HeaterCommand(Item item, PhysicalDevice device){
		super( item, device);
		heater = (Heater)device;
	}

	// PROTECTED METHODS //////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void executeHelper(){
		
		// create GUI components
		
		GUIFactory guifactory = GUIFactory.instance();
		
		sliderTempDemand = guifactory.createSlider( "sliderTempDemand", null);
		txtSlidersInfo = guifactory.createTextArea( "Temp. demand\nCurrent temp.\nValve pos.", "txtSliderInfo", null);
		
		float pnlHeaterWidth = 3*(sliderTempDemand.getWidth()+10) + 10;
		pnlHeater = guifactory.createPanel(
				pnlInteraction.getPosition( TransformSpace.GLOBAL).getX() +
				pnlInteraction.getWidthXY( TransformSpace.GLOBAL) - pnlHeaterWidth,
				this.getStartPosition().getY(),
				pnlHeaterWidth,
				sliderTempDemand.getHeight() + txtSlidersInfo.getHeightXY( TransformSpace.GLOBAL) + 30,
				"pnlHeater",
				pnlInteraction
		);
		
		
		pnlHeater.addChild( txtSlidersInfo);
		txtSlidersInfo.setPositionGlobal(
				new Vector3D(
						pnlHeater.getPosition(TransformSpace.GLOBAL).getX() + 10,
						pnlHeater.getPosition( TransformSpace.GLOBAL).getY()
				));
		
		
		pnlHeater.addChild( sliderTempDemand);
		sliderTempDemand.setPosition(
				new Vector3D(
						pnlHeater.getPosition( TransformSpace.GLOBAL).getX() + 10,
						txtSlidersInfo.getPosition(TransformSpace.GLOBAL).getY()+ txtSlidersInfo.getHeightXY( TransformSpace.GLOBAL) + 10
				));
		sliderTempDemand.setValueRange( 0 /*TODO*/, 10000 /*TODO*/);
		sliderTempDemand.addGestureListener( DragProcessor.class, new SliderTempDemandListener());
				
		
		sliderTempCurrent = guifactory.createSlider( "sliderTempCurrent", pnlHeater);
		sliderTempCurrent.setPositionNextTo( sliderTempDemand, 10, true);
		sliderTempCurrent.setValueRange( 0 /*TODO*/, 10000 /*TODO*/);
		sliderTempDemand.addGestureListener( DragProcessor.class, new SliderTempCurrentListener());
		
		
		sliderValvePosition = guifactory.createSlider( "sliderValvePosition", pnlHeater);
		sliderValvePosition.setPositionNextTo( sliderTempCurrent, 10, true);
		sliderValvePosition.setValueRange( 0 /*TODO*/, 10000 /*TODO*/);
		sliderTempDemand.addGestureListener( DragProcessor.class, new SliderValvePosListener());
		
		// bind observer
		if( heater.getTemperatureCurrent() != null){
			heater.getTemperatureCurrent().eAdapters().add( observer);
		}
		if( heater.getTemperatureDemand() != null){
			heater.getTemperatureDemand().eAdapters().add( observer);
		}
		if( heater.getValvePosition() != null){
			heater.getValvePosition().eAdapters().add( observer);
		}
	}

	@Override
	protected void ceaseHelper(){
		
		if( heater.getTemperatureCurrent() != null){
			heater.getTemperatureCurrent().eAdapters().remove( observer);
		}
		if( heater.getTemperatureDemand() != null){
			heater.getTemperatureDemand().eAdapters().remove( observer);
		}
		if( heater.getValvePosition() != null){
			heater.getValvePosition().eAdapters().remove( observer);
		}
	}
	
	@Override
	protected void setGUIAttributesHelper(){
		
		DoubleProperty tempDemand = heater.getTemperatureDemand();
		DoubleProperty tempCurrent = heater.getTemperatureCurrent();
		DoubleProperty valvePosition = heater.getValvePosition();
		
		sliderTempDemand.setEnabled( tempDemand != null && tempDemand.getActor() != null);
		if( tempDemand != null && tempDemand.getValue() != null){
			sliderTempDemand.setValue( tempDemand.getValue().floatValue());
		}else{
			sliderTempDemand.setValue( 0);
		}
		
		sliderTempCurrent.setEnabled( tempCurrent != null && tempCurrent.getActor() != null);
		if( tempCurrent != null && tempCurrent.getValue() != null){
			sliderTempCurrent.setValue( tempCurrent.getValue().floatValue());
		}else{
			sliderTempCurrent.setValue(0);
		}
		
		sliderValvePosition.setEnabled( valvePosition != null && valvePosition.getActor() != null);
		if( valvePosition != null && valvePosition.getValue() != null){
			sliderValvePosition.setValue( valvePosition.getValue().floatValue());
		}else{
			sliderValvePosition.setValue( 0);
		}
	}
	
	// INNER CLASSES //////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This inner class provides the slider with the necessary
	 * logic to actually use the device.
	 * 
	 * @author langenhagen
	 * @version 20120101
	 */
	private class SliderTempDemandListener implements IGestureEventListener{

		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			if(ge.getId() == MTGestureEvent.GESTURE_ENDED){
				
				try{
					heater.getTemperatureDemand().requestValueUpdate( Double.toString( sliderTempDemand.getValue()) );
				} catch(ActorServiceCallException e){
					e.printStackTrace();
				}
				setGUIAttributes();
				item.getStateVisualizer().notifyVisualizer();
			}
			return false;
		}
	} // END SliderTempDemandListener
	
	
	/**
	 * This inner class provides the slider with the necessary
	 * logic to actually use the device.
	 * 
	 * @author langenhagen
	 * @version 20120101
	 */
	private class SliderTempCurrentListener implements IGestureEventListener{

		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			if(ge.getId() == MTGestureEvent.GESTURE_ENDED){
				
				try{
					heater.getTemperatureCurrent().requestValueUpdate( Double.toString( sliderTempCurrent.getValue()) );
				} catch(ActorServiceCallException e){
					e.printStackTrace();
				}
				setGUIAttributes();
				item.getStateVisualizer().notifyVisualizer();
			}
			return false;
		}
	} // END SliderTempCurrentListener
	
	
	/**
	 * This inner class provides the slider with the necessary
	 * logic to actually use the device.
	 * 
	 * @author langenhagen
	 * @version 20120101
	 */
	private class SliderValvePosListener implements IGestureEventListener{

		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			if(ge.getId() == MTGestureEvent.GESTURE_ENDED){
				
				try{
					heater.getValvePosition().requestValueUpdate( Double.toString( sliderValvePosition.getValue()) );
				} catch(ActorServiceCallException e){
					e.printStackTrace();
				}
				setGUIAttributes();
				item.getStateVisualizer().notifyVisualizer();
			}
			return false;
		}
	} // END SliderValvePosListener
}

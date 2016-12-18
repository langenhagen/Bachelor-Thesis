package home.deviceCommands.std;

import java.util.LinkedList;
import java.util.List;


import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTOverlayContainer;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;
import org.sercho.masp.models.Context.PhysicalDevice;

import provider.GUITextures;

import util.DeviceType;
import widgets.MT3DObject;
import widgets.MTButton;
import widgets.MTCheckButton;
import widgets.factories.GUIFactory;

import de.dailab.masp.models.Properties.ActorServiceCallException;
import de.dailab.masp.models.Properties.PropertiesPackage;

import gestureListeners.transformation.CentricRotation3DListener;
import gestureListeners.transformation.Displacement3DPlaneListener;
import home.Home;
import home.Item;
import home.deviceCommands.DeviceCommand;
import home.deviceCommands.factories.CommandFactory;

/**
 * This abstract class implements standard behaviour for
 * commands in the standard-GUI-context.
 * This is for usage in the provided standard GUI
 * and therefore may not be sufficient for other GUIs
 * with other styles and/or interaction techniques
 * 
 * @author langenhagen
 * @version 20120207
 */
public abstract class StdCommand extends DeviceCommand {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** indicates, whether the command is being executed at the moment */
	protected boolean isExecuting = false;
	/** the corresponding device of this command if it is not set to another one, it is the device of the corresponding item */
	protected PhysicalDevice device;
	/** this list keeps track of the commands of the subdevices */
	private List<DeviceCommand> subDeviceCommands;
	/** this observer manages the behaviour of this command when changes from outside the application occur */
	protected Adapter observer;
	
	/** gui components */
	
	MTOverlayContainer overlay;
	protected MTRectangle pnlPanel;
	protected MTRectangle pnlEdit;
	protected MTRectangle pnlInteraction;
	
	protected MTRectangle pnlSubDeviceButtons;
	protected List<MTButton> subDeviceButtons;
	
	protected MTTextArea txtItemName;
	protected MTButton btnHide;
	
	protected MTRectangle pnlPicture;
	protected MTTextArea txtPowerUsage;
	protected MTButton btnOnOff;
	protected MTButton btnEdit;
	
	protected MTCheckButton chkMovePlanar;
	protected MTCheckButton chkMoveY;
	protected MTCheckButton chkRotate;
	protected MTCheckButton chkScale;
	protected MTButton btnChangeVisualisation;
	protected MTButton btnSave;
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main constructor. It removes all <i>ItemTapListeners</i> from the
	 * items listener.
	 * @param item
	 * The corresponding item as an <i>Item</i>.
	 * It is the context of the new <i>DeviceCommand</i>.
	 * @param device
	 * The device that shall correspond to the item.
	 * Usually the <i>PhysicalDevice</i> of the item itself or a subdevice of it.
	 */
	public StdCommand(Item item, PhysicalDevice device){
		super( item);
		this.device = device;
		
		// remove all itemTapListeners (in order to prevent more than one..)
		while( item.getView().removeListenerType( TapProcessor.class, ItemTapListener.class) ){}
		
		item.getView().addGestureListener( TapProcessor.class, new ItemTapListener(item));
		
		subDeviceCommands = new LinkedList<DeviceCommand>();
		subDeviceButtons = new LinkedList<MTButton>();
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	@Override
	public final void execute(){
		
		if( isExecuting){
			return;
		}
		isExecuting = true;
		
		// create GUI components
		setupGUI();
				
		// bind observer
		// subscribe to power usage and On/Off property value changes
		observer = new Adapter();
		if( device.getPowerUsage() != null){
			device.getPowerUsage().eAdapters().add( observer);
		}
		if( device.getOn() != null){
			device.getOn().eAdapters().add( observer);
		}
		
		// start subclass augmentations of execute()
		executeHelper();
		
		// set dynamic properties
		setGUIAttributes();
	}

	@Override
	public final void cease(){
	
		item.setMovePlanarMode( false);
		item.setMoveVerticalMode( false);
		item.setRotateMode( false);
		item.setScaleMode( false);
		item.getHome().setActiveItem("");
		
		Home home = item.getHome();
		MT3DObject view = item.getView();
		view.addGestureListener( TapProcessor.class, new ItemTapListener( item));
		view.addGestureListener( DragProcessor.class, new Displacement3DPlaneListener( home.getMovingPlaneNormal()).setHome( home).setMovementRadius( home.getMaxMovementRadius()));
		view.addGestureListener( RotateProcessor.class, new CentricRotation3DListener( home.getMovingPlaneNormal()).setHome( home));
		
		if( device.getPowerUsage() != null){
			device.getPowerUsage().eAdapters().remove( observer);
		}
		if( device.getOn() != null){
			device.getOn().eAdapters().remove( observer);
		}
		
		ceaseHelper();
		
		for( DeviceCommand c : subDeviceCommands){
			c.cease();
		}
		
		subDeviceButtons.clear();
		
		// if the overlay never existed because the command was never executed
		if( overlay != null){
			overlay.destroy();
		}
		
		isExecuting = false;
	}
	
	
	// PROTECTED METHODS //////////////////////////////////////////////////////////////////////////
	
	/**
	 * Helper function that augments the execute() routine
	 * of this <i>DeviceCommand</i>. Binds the observer object to
	 * device-specific properties and augments the GUI.
	 */
	protected abstract void executeHelper();
	
	/**
	 * Helper function that augments the cease() routine
	 * of this <i>DeviceCommand</i>. Can be overridden by child classes for
	 * destroying things at shutdown, or to remove observer bindings from subjects.<br>
	 * Note that there is no need destroy gui components separately, since the mother overlay of 
	 * all components and therefore all components will be destroyed by the cease()-method. However,
	 * this may not be true if you store your components in a container object.
	 */
	protected abstract void ceaseHelper();
	
	/**
	 * Sets dynamic gui attributes like captions, enables or disables components
	 * in order to keep up with the real model.
	 * This method divides the actual creation of the gui from changing the dynamic properties of it,
	 * therefore the dynamic properties can be set at any time.<br>
	 * Note, that this method may not be called explicitely by subclasses. Call setGUIAttributes(),
	 * which refreshes the whole gui, instead.
	 */
	protected abstract void setGUIAttributesHelper();
	
	/**
	 * Calculates a vector that can be used as the upper left boundary for further gui components that may augment the
	 * standard command.
	 * @return
	 * A <i>Vector3D</i> that describes a position equal to the lower left boundary of the lowest subdevice button minus 10.
	 */
	protected Vector3D getStartPosition(){
		return new Vector3D(
				pnlSubDeviceButtons.getPosition( TransformSpace.GLOBAL).getX(),
				pnlSubDeviceButtons.getPosition( TransformSpace.GLOBAL).getY() + pnlSubDeviceButtons.getHeightXY( TransformSpace.GLOBAL)
		);
	}
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
	/**
	 * this private method sets up the GUI of the dialog or what you want to call it.
	 */
	private void setupGUI(){
		
		final GUIFactory guifactory = GUIFactory.instance();
		final MTApplication app = (MTApplication)item.getView().getRenderer();
		final float leftiness = app.getWidth()*5/6;
		final float xPos = leftiness + 5;
		
		
		overlay = new MTOverlayContainer( app);
		item.getView().getRoot().addChild( overlay);
		
		// _THE_ PANEL 
		
		pnlPanel = guifactory.createPanel(  leftiness, 0, app.getWidth()-leftiness, app.getHeight(),"pnlPanel", overlay);			
		
		// INTERACTION PANEL
		
		pnlInteraction = guifactory.createPanel(
				pnlPanel.getPosition( TransformSpace.GLOBAL),
				pnlPanel.getWidthXY( TransformSpace.GLOBAL),
				pnlPanel.getHeightXY( TransformSpace.GLOBAL),
				"pnlInteraction",
				pnlPanel);
		pnlInteraction.setNoFill( true);
		
		// EDIT PANEL
		
		pnlEdit = guifactory.createPanel(
				pnlPanel.getPosition( TransformSpace.GLOBAL),
				pnlPanel.getWidthXY( TransformSpace.GLOBAL),
				pnlPanel.getHeightXY( TransformSpace.GLOBAL),
				"pnlEdit",
				pnlPanel);
		pnlEdit.setVisible( false);
		pnlEdit.setNoFill( true);
		
		
		// ITEM NAME TEXTAREA
		
		String itemName = item.getName();
		if(itemName == null || itemName.equals( ""))
			itemName = item.getID();
		txtItemName = guifactory.createTextArea(itemName, "txtItemName", pnlPanel);
		txtItemName.setPositionGlobal(
				new Vector3D(
						xPos,
						pnlPanel.getPosition( TransformSpace.GLOBAL).getY() + 10	
				));


		// PANEL PICTURE

		Vector3D pnlInteractionPos = pnlInteraction.getPosition( TransformSpace.GLOBAL);
		pnlPicture = guifactory.createPanel( pnlInteractionPos.getX()+50, pnlInteractionPos.getY()+50, 100, 100, "pnlPicture", pnlInteraction);
		pnlPicture.setAnchor( PositionAnchor.CENTER);
		pnlPicture.setPositionGlobal(
				new Vector3D(
						pnlInteractionPos.getX() + pnlInteraction.getWidthXY( TransformSpace.GLOBAL)/2,
						pnlInteractionPos.getY() + 50 + pnlPicture.getHeightXY( TransformSpace.GLOBAL)/2
				));
		pnlPicture.setTexture( GUITextures.instance().getDevice( DeviceType.getDeviceType( device)));
		
		
		// POWER USAGE TEXTAREA
		
		String powerUsage = "n/a";
		if( device.getPowerUsage() != null && device.getPowerUsage().getValue() != null){
			powerUsage = device.getPowerUsage().getValue().toString();
		}
		
		txtPowerUsage = guifactory.createTextArea( "Power usage: " + powerUsage + " Watts", "txtPowerUsage", pnlInteraction);
		txtPowerUsage.setPositionGlobal(
				new Vector3D(
						xPos,
						pnlInteractionPos.getY() + 140		
						));
		txtPowerUsage.setVisible( false);
		
		
		// ON/OFF BUTTON
		
		btnOnOff = guifactory.createButton( "Turn device On/Off", "btnOnOff", pnlInteraction);
		btnOnOff.setPosition( txtPowerUsage.getPosition( TransformSpace.GLOBAL));
		btnOnOff.addGestureListener( TapProcessor.class, new BtnOnOffListener());
		
		
		// SUBDEVICE BUTTON PANEL
		
		pnlSubDeviceButtons = guifactory.createPanel(
				pnlPanel.getPosition( TransformSpace.GLOBAL).getX(),
				btnOnOff.getVectorNextToComponent( 10, false).getY(),
				pnlPanel.getWidthXY( TransformSpace.GLOBAL),
				device.getSubDevice().size() * (btnOnOff.getHeight()+10),
				"pnlSubDeviceButtons",
				pnlInteraction);
		
		// SUB DEVICE BUTTONS
		
		subDeviceButtons = new LinkedList<MTButton>();
		for( int i = 0; i < device.getSubDevice().size(); i++){
			
			PhysicalDevice subDevice = device.getSubDevice().get( i);
			
			// get button's caption
			String caption = subDevice.getName();
			if(caption == null || caption.equals( ""))
				caption = subDevice.getId();
			
			MTButton btnSubDevice = guifactory.createButton( caption, "btnSubDevice_" + subDevice.getId(), pnlSubDeviceButtons);
			btnSubDevice.setPosition(
					new Vector3D(
						xPos,	
						pnlSubDeviceButtons.getPosition( TransformSpace.GLOBAL).getY() + i*(btnSubDevice.getHeight() + 10)
					));
			
			subDeviceButtons.add( btnSubDevice);
			btnSubDevice.addGestureListener( TapProcessor.class, new BtnSubDeviceListener( subDevice));
		}
		
		
		// EDIT BUTTON
		
		btnEdit = guifactory.createButton( "Edit", "btnEdit", pnlInteraction);
		btnEdit.setPosition( new Vector3D( xPos, app.getHeight() - 2*btnEdit.getHeight() - 30));
		btnEdit.addGestureListener( TapProcessor.class, new BtnEditListener());
		
		
		// BUTTON HIDE
		
		btnHide = guifactory.createButton( "Hide menu", "btnHide", pnlPanel);
		btnHide.setPosition( new Vector3D( xPos, app.getHeight() - btnHide.getHeight() - 20));
		btnHide.addGestureListener( TapProcessor.class, new BtnHideListener());
		
		
		///// EDIT-ELEMENTS /////

		chkMovePlanar = guifactory.createCheckButton( "Move planar", "chkMovePlanar", pnlEdit);
		chkMovePlanar.setPosition(
				new Vector3D(
						xPos,
						txtItemName.getPosition( TransformSpace.GLOBAL).getY() + txtItemName.getHeightXY( TransformSpace.GLOBAL)+ 20	
				));
		chkMovePlanar.addGestureListener( TapProcessor.class, new ChkMovePlanarListener());

		
		chkMoveY = guifactory.createCheckButton( "Move up/down", "chkMoveY", pnlEdit);
		chkMoveY.setPosition( chkMovePlanar.getVectorNextToComponent( 10, false));
		chkMoveY.addGestureListener( TapProcessor.class, new ChkMoveYListener());

		
		chkRotate = guifactory.createCheckButton( "Rotate", "chkRotate", pnlEdit);
		chkRotate.setPosition( chkMoveY.getVectorNextToComponent( 10, false));
		chkRotate.addGestureListener( TapProcessor.class, new ChkRotateListener());

		
		chkScale = guifactory.createCheckButton( "Scale", "chkScale", pnlEdit);
		chkScale.setPosition( chkRotate.getVectorNextToComponent( 10, false));
		chkScale.addGestureListener( TapProcessor.class, new ChkScaleListener());

		
		btnChangeVisualisation = guifactory.createButton( "Change Style", "btnChangeVisualisation", pnlEdit);
		btnChangeVisualisation.setPosition( chkScale.getVectorNextToComponent( 10, false));
		btnChangeVisualisation.addGestureListener( TapProcessor.class, new BtnChangeVisualisationListener());
		
		
		btnSave = guifactory.createButton( "Save", "btnSave", pnlEdit);
		btnSave.setPosition( btnEdit.getPosition());
		btnSave.addGestureListener( TapProcessor.class, new BtnSaveListener());
	}
	
	// PRIVATE METHODS ////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Sets dynamic gui attributes like captions, enables or disables components
	 * in order to keep up with the real model.
	 */
	protected final void setGUIAttributes(){
		
		btnOnOff.setEnabled( device.getOn() != null && device.getOn().getActor() != null);
		if(device.getOn() == null){
			return;
		}
		// if the device is On
		if( device.getOnValue()){
			btnOnOff.setText( "Turn device Off");
		// if the device is Off
		}else{
			btnOnOff.setText( "Turn device On");
		}
		setGUIAttributesHelper();
	}
	
	// INNER CLASSES //////////////////////////////////////////////////////////////////////////////

	/**
	 * This class provides an observer object for listening to changes of the device 
	 * from outside the application and therefore enables changes within the gui.
	 * 
	 * @author langenhagen
	 * @version 20120207
	 */
	protected class Adapter extends AdapterImpl
	{		
		/**
		 * This method is an observer method that shall be called by the ContextModel
		 * if some value changed. Doesn't need to be invoked in some hand written code;
		 * use the notifyVisualizer() instead.
		 * @param msg
		 * A notification messag as a <i>Notification</i>.
		 */
		public void notifyChanged(final Notification msg) {
	        super.notifyChanged(msg);
	        
	        if(msg.getFeature() == PropertiesPackage.Literals.PROPERTY__VALUE) {
	        	setGUIAttributes();
	        }
	    }
	}
	
	/**
	 * This class provides the command with a listener for its On/Off-button.
	 * This listener can set the item On/Off and adjusts the item's
	 * <i>StateVisualizer</i> accordingly.
	 * 
	 * @author langenhagen
	 * @version 20111219
	 */
	private class BtnOnOffListener implements IGestureEventListener{
		
		// CONSTRUCTORS //
		
		/**
		 * Main constructor
		 */
		public BtnOnOffListener(){
				
			processGestureEventHelper( false);
		}

		
		// PUBLIC METHODS //
		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){

			if(ge.getId() == MTGestureEvent.GESTURE_STARTED){
				processGestureEventHelper( true);
			}
			return false;
		}
		
		
		// PRIVATE HELPERS //
		
		/**
		 * Processes a tap On the On/Off button and changes( if indicated) the On-value of the device.
		 * This method is just for preventing duplicate code.
		 * @param changeOnValue
		 * If set to TRUE, the On-value of the associated device will be changed,
		 * if set to FALSE, just the Button and the item's state visualizer are
		 * changed according to the current state of the item.
		 */
		private void processGestureEventHelper( boolean changeOnValue){
			
			// If device has no On-value, disable On/Off button
			btnOnOff.setEnabled( device.getOn() != null && device.getOn().getActor() != null);
			if(device.getOn() == null){
				return;
			}
			
			// turn On/Off
			if(changeOnValue){
				try{
					if(device.getOnValue()){
						device.turnOff();
					}else{
						device.turnOn();
					}
				}catch(ActorServiceCallException e){
					System.err.println(e.getMessage());
					e.printStackTrace();
										
				}catch(IllegalStateException e){
					System.err.println(e.getMessage());
					e.printStackTrace();
				}
				setGUIAttributes();
			}

			// change gui component's appearance and views			
			
			item.getStateVisualizer().notifyVisualizer();
			System.out.println( device.getName() + " is now On: " + device.getOnValue());
		}
	} // END BtnOnOffListener
	
	
	/**
	 * This listener hides the pnlPanel and derigsters all special movement-gestureListeners
	 * On the active item.
	 * 
	 * @author langenhagen
	 * @version 20111002
	 */
	private class BtnHideListener implements IGestureEventListener{
		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			switch( ge.getId())
			{
			case MTGestureEvent.GESTURE_STARTED:
				
				cease();				
			}
			return false;
		}
	} // END btnHideListener
	
	
	/**
	 * This class makes the specified itemID the active one.
	 * 
	 * @author langenhagen
	 * @version 20110930
	 */
	private class ItemTapListener implements IGestureEventListener{
		
		/** the corresponding item */
		private Item item;
		
		/**
		 * Main constructor
		 * @param item
		 * the <i>Item</i> that shall correspond to the new listener
		 */
		public ItemTapListener( Item item){
			this.item = item;
		}
		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			try{
				
				Home home = item.getHome();
				Item oldActiveItem = home.getActiveItem();
								
				if( oldActiveItem == item){
					return false;
				}else if( oldActiveItem != null){
					oldActiveItem.getCommand().cease();
				}
				
				home.setActiveItem( item.getID());
				item.getCommand().execute();
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return false;
		}
	} // END ItemTapListener
	
	
	/**
	 * Sets the edit mode for the active item in the specified home.
	 * 
	 * @author langenhagen
	 * @version 20110930
	 */
	private class BtnEditListener implements IGestureEventListener{
		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			switch( ge.getId())
			{
			case MTGestureEvent.GESTURE_STARTED:
				
				chkMovePlanar.setChecked( false);
				chkMoveY.setChecked( false);
				chkRotate.setChecked( false);
				chkScale.setChecked( false);	
				
				pnlInteraction.setVisible( false);
				pnlEdit.setVisible( true);

				MT3DObject view = item.getView();
				view.removeAllGestureEventListeners();
			
			}
			return false;
		}
	} // END BtnEditListener
	
	
	/**
	 * This listener lets a checkbutton
	 * decide, whether to activate a planar movement On the 
	 * active item or doing the opposite thing.
	 * @author langenhagen
	 * @version 20111001
	 */
	private class ChkMovePlanarListener implements IGestureEventListener{

		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			MTCheckButton chk = (MTCheckButton)ge.getTarget();
			item.setMovePlanarMode(chk.isChecked());
			return false;
		}
	} // END ChkMovePlanarListener
	
	
	/**
	 * This listener lets a checkbutton
	 * decide, whether to activate a y-movement On an item
	 * or doing the opposite thing.
	 * @author langenhagen
	 * @version 20111001
	 */
	private class ChkMoveYListener implements IGestureEventListener{
		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			MTCheckButton chk = (MTCheckButton)ge.getTarget();
			item.setMoveVerticalMode( chk.isChecked());
			return false;
		}
	} // END ChkMoveYListener
	
	
	/**
	 * This listener lets a checkbutton
	 * decide, whether to activate a rotation On an item
	 * or doing the opposite thing.
	 * @author langenhagen
	 * @version 20111001
	 */
	private class ChkRotateListener implements IGestureEventListener{
		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			MTCheckButton chk = (MTCheckButton)ge.getTarget();
			item.setRotateMode( chk.isChecked());
			return false;
		}
	} // END ChkRotateListener
	
	
	/**
	 * This listener lets a checkbutton
	 * decide, whether to activate a scaling On an item
	 * or doing the opposite thing.
	 * @author langenhagen
	 * @version 20111001
	 */
	private class ChkScaleListener implements IGestureEventListener{
		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			MTCheckButton chk = (MTCheckButton)ge.getTarget();
			item.setScaleMode( chk.isChecked());
			return false;
		}
	} // END ChkScaleListener
	
	
	/**
	 * This listener changes the variant On the active item
	 * On the active item. It also notifies the state visualizer
	 * and refreshes the new variant.
	 * @author langenhagen
	 * @version 20111229
	 */
	private class BtnChangeVisualisationListener implements IGestureEventListener{

		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			switch( ge.getId())
			{
			case MTGestureEvent.GESTURE_STARTED:

				MT3DObject obj = item.getView();
				if( !obj.setVariant( obj.getVariant()+1))
					obj.setVariant( 0);
			}
			item.getStateVisualizer().notifyVisualizer();
			return false;
		}
	} // END BtnChangeVisualisationListener
	
	
	/**
	 * This listener is dedicated to saving a home when tapped On its target.
	 * @author langenhagen
	 * @version 20110928
	 */
	private class BtnSaveListener implements IGestureEventListener{
				
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			switch( ge.getId()){
			case MTGestureEvent.GESTURE_STARTED:
			
				Home home = item.getHome();
				
				Item activeItem = home.getActiveItem();
				activeItem.setMovePlanarMode( false); // also affects other move-planar-gestureListeners!
				activeItem.setMoveVerticalMode( false);
				activeItem.setRotateMode( false); // also...
				activeItem.setScaleMode( false);
				
				home.saveState();
				
				
				pnlEdit.setVisible( false);
				pnlInteraction.setVisible( true);	
						
				MT3DObject activeObj = home.getActiveItem().getView();
				activeObj.addGestureListener( DragProcessor.class, new Displacement3DPlaneListener( home.getMovingPlaneNormal()).setHome( home).setMovementRadius( home.getMaxMovementRadius()));
				activeObj.addGestureListener( RotateProcessor.class, new CentricRotation3DListener( home.getMovingPlaneNormal()).setHome( home));	
			}
			return false;
		}
	} // END BtnSaveListener


	/**
	 * This listener adds functionality to the sub device buttons.
	 * @author langenhagen
	 * @version 20111226
	 */
	private class BtnSubDeviceListener implements IGestureEventListener{
		
		/** the command which is to to execute */
		private DeviceCommand command;
		
		/**
		 * Main constructor.
		 * @param device
		 * The device which shall be associated with this button
		 */
		public BtnSubDeviceListener( PhysicalDevice device){
			
			command = CommandFactory.instance().createCommand( item, device, false);
			((StdCommand)command).device = device;
			subDeviceCommands.add( command);
		}

		@Override
		public boolean processGestureEvent(MTGestureEvent ge){

			if(ge.getId() == MTGestureEvent.GESTURE_ENDED){
				cease();
				command.execute();
			}
			return false;
		}
	} // END BtnSubDeviceListener
}
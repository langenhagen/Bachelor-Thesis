package widgets.std;

import java.util.LinkedList;
import java.util.List;

import org.mt4j.components.TransformSpace;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;
import processing.core.PApplet;
import widgets.MTCheckButton;
import widgets.MTChoiceContainer;


/**
 * @author langenhagen
 * @version 20110530
 */
public class StdMTChoiceContainer extends MTChoiceContainer {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////

	/** the list of all buttons, which are controlled by this container */
	List<MTCheckButton> buttonList = new LinkedList<MTCheckButton>();
	
	/** the currently checked button */
	MTCheckButton checkedButton = null;
	
	/** indicates, whether all buttons can be unchecked at a time */
	boolean isNullChoiceAllowed = true;
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructor #1
	 * @param pApp
	 * The Application which is rendering this GUI-component as a <i>PApplet</i>
	 */
	public StdMTChoiceContainer(PApplet pApp){
		this( new LinkedList<MTCheckButton>(), pApp);
	}
	
	/**
	 * Constructor #2.
	 * @param buttonList
	 * A <i>List</i> of <i>MTCheckbuttons</i> which contains the checkbuttons which shall
	 * be controlled by this container.
	 * @param pApp
	 * The Application which is rendering this GUI-component as a <i>PApplet</i>
	 */
	public StdMTChoiceContainer(List<MTCheckButton> buttonList, PApplet pApp){
		super(pApp);
		this.buttonList = buttonList;
	}

	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/* (non-Javadoc)
	 * @see widgets.MTChoiceContainer#getButtonList()
	 */
	@Override
	public List<MTCheckButton> getButtonList(){
		return buttonList;
	}

	/* (non-Javadoc)
	 * @see widgets.MTChoiceContainer#getCheckedButton()
	 */
	@Override
	public MTCheckButton getCheckedButton(){
		return checkedButton;
	}

	/* (non-Javadoc)
	 * @see widgets.MTChoiceContainer#setNullChoiceAllowed(boolean)
	 */
	@Override
	public void setNullChoiceAllowed(boolean b){
		isNullChoiceAllowed = b;
	}

	/* (non-Javadoc)
	 * @see widgets.MTChoiceContainer#isNullChoiceAllowed()
	 */
	@Override
	public boolean isNullChoiceAllowed(){
		return isNullChoiceAllowed;
	}

	/* (non-Javadoc)
	 * @see widgets.MTChoiceContainer#addListener(components.MTCheckButton)
	 */
	@Override
	protected void addListener(MTCheckButton btn){
		btn.addGestureListener( TapProcessor.class, new CheckListener(this));
	}

	/* (non-Javadoc)
	 * @see widgets.MTChoiceContainer#removeListener(int)
	 */
	@Override
	protected void removeListener(int i){
		IGestureEventListener[] listeners = buttonList.get( i).getGestureListeners();
		
		for( IGestureEventListener l : listeners)
			if(l instanceof CheckListener)
				buttonList.get( i).removeGestureEventListener( TapProcessor.class, l);
	}

	/* (non-Javadoc)
	 * @see widgets.MTChoiceContainer#doAdditionalAddStuff(components.MTCheckButton)
	 */
	@Override
	protected void doAdditionalAddStuff(MTCheckButton btn){
	}

	/* (non-Javadoc)
	 * @see widgets.MTChoiceContainer#doAdditionalRemoveStuff(components.MTCheckButton)
	 */
	@Override
	protected void doAdditionalRemoveStuff(MTCheckButton btn){
	}

	/* (non-Javadoc)
	 * @see widgets.MTChoiceContainer#setCheckedButtonHelper(components.MTCheckButton)
	 */
	@Override
	protected void setCheckedButtonHelper( MTCheckButton btn){
		checkedButton = btn;
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#getPosition(org.mt4j.components.TransformSpace)
	 */
	@Override
	public Vector3D getPosition(TransformSpace space){
		return Vector3D.ZERO_VECTOR;
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setPosition(org.mt4j.util.math.Vector3D, org.mt4j.components.TransformSpace)
	 */
	@Override
	public void setPosition(Vector3D pos, TransformSpace space){
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#getWidth(org.mt4j.components.TransformSpace)
	 */
	@Override
	public float getWidth(TransformSpace space){
		return 0;
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#getHeight(org.mt4j.components.TransformSpace)
	 */
	@Override
	public float getHeight(TransformSpace space){
		return 0;
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setWidth(float, org.mt4j.components.TransformSpace)
	 */
	@Override
	public void setWidth(float width, TransformSpace space){
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setHeight(float, org.mt4j.components.TransformSpace)
	 */
	@Override
	public void setHeight(float height, TransformSpace space){
	}
	
	// PRIVATE INNER CLASS ////////////////////////////////////////////////////////////////////////
	
	/**
	 * This class represents a listener which can be installed On a 
	 * MTCheckbox and that manipulates the checkbuttons behaviour according to
	 * the behaviour of radio buttons.
	 * 
	 * @author langenhagen
	 * @version 20110609
	 */
	private class CheckListener implements IGestureEventListener{
		
		/** a reference to the container, which keeps track of the gesture-target */
		MTChoiceContainer container;
		
		/**
		 * Main contructor
		 * @param container
		 * The <i>MTChoiceContainer</i> containing the target, the MTCheckButton
		 */
		public CheckListener( MTChoiceContainer container){
			this.container = container;
		}

		@Override
		public boolean processGestureEvent(MTGestureEvent ge){

			MTCheckButton btn = (MTCheckButton)ge.getTarget();
			
			if( ge.getId() != MTGestureEvent.GESTURE_ENDED)
				return false;
			
			if( btn == container.getCheckedButton())
				container.setCheckedButton(null);
			else
				container.setCheckedButton(btn);
			
			return false;
		}
	} // END class CheckListener
}
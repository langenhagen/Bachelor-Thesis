package widgets;

import java.util.List;

import processing.core.PApplet;
import util.Mode;

/**
 * This class represents a container for <i>MTCheckButtons</i>. These checkbuttons can be put in this container,
 * which does NOT explicitly handle their alignment or positioning, but groups them and provides them with 
 * functionality best known from radiobuttons, letting the checkbuttons act as choice-buttons, 
 * where all buttons of one container belong to each other. In order to do that, a special listener 
 * will be registered at the added checkbuttons, which forwards tap-gestures right to the container,
 * that then handles the request.<br>
 * It is up to you to force one button to be checked (except for special ways and means) or to allow
 * the unchecking all of these buttons.
 * 
 * @author langenhagen
 * @version 20110611
 */
public abstract class MTChoiceContainer extends AbstractGUIWidget {
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main constructor
	 * @param pApp
	 * The Application which is rendering this GUI-component as a <i>PApplet</i>
	 */
	public MTChoiceContainer(PApplet pApp){
		super( pApp);
	}

	// METHODS ////////////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * Retrieves the list of checkbuttons.
	 * @return
	 * A <i>List</i> of type <i>MTCheckButton</i>
	 */
	public abstract List<MTCheckButton> getButtonList();
	
	/**
	 * Adds a new checkbutton to the container and unchecks it.
	 * If this button is already in this container, nothing happens.
	 * @param btn
	 * The checkbutton as a <i>MTCheckButton</i>
	 * @return
	 * Returns TRUE, if the adding was successful, otherwise returns FALSE
	 */
	public boolean add( MTCheckButton btn){
			
		List<MTCheckButton> list = getButtonList();
		for( MTCheckButton b : list)		// duplicate?
			if( btn == b){
				System.err.println("MTChoiceContainer.add(): Adding the same MTCheckButton to the same MTChoiceContainer two times is not possible.");
				return false;
			}
		
		btn.setChecked( false);				// uncheck		
		this.addListener( btn);				// add listener
		list.add( btn);						// add element
		this.doAdditionalAddStuff( btn);	// do additional stuff?
		return true;
	}
	
	/**
	 * Removes a  checkbutton with the specified index from this container.
	 * If the specified index corresponds to no checkbutton in this container, nothing happens.
	 * If the button to remove is the one which is checked, all remaining buttons keep being unchecked.
	 * @param i
	 * The index at which to remove the checkbutton
	 * @return
	 * Returns the removed <i>MTCheckButton</i> or NULL in case of failure.
	 */
	public MTCheckButton remove( int i){
		
		if( i<0 || i>= getButtonList().size())								// out of range? element exists?
			return null;
		
		MTCheckButton ret;
		
		if( getCheckedButton() != null && 									// removing checked element?
			getButtonList().indexOf( getCheckedButton()) == i)		
			setCheckedButtonHelper( null);
		
		this.removeListener( i);											// remove listener
		ret = getButtonList().remove( i);									// remove element
		this.doAdditionalRemoveStuff( ret);									// do additional stuff?
		return ret;
	}
	
	/**
	 * Removes a specified checkbutton from this container.
	 * If the specified checkbutton is NULL or is not in the container, nothing happens.
	 * If the button to remove is the one which is checked, all remaining buttons keep being unchecked.
	 * @param btn
	 * The <i>MTCheckButton</i> to remove.
	 * @return
	 * Returns the removed <i>MTCheckButton</i> if successful or NULL otherwise.
	 */
	public MTCheckButton remove( MTCheckButton btn){
		
		if( btn == null || !getButtonList().contains( btn))		// element exists?
			return null;
		
		int index = getButtonList().indexOf( btn);					

		return this.remove( index);		// remove listener, remove element
	}
	
	/**
	 * Retrieves the currently checked button.
	 * @return
	 * The checked <i>MTCheckButton</i> or NULL,
	 * if no button is checked
	 */
	public abstract MTCheckButton getCheckedButton();

	/**
	 * Sets the checked button according to its index within this container.
	 * If the specified index corresponds to no checkbutton, nothing happens.
	 * @param i
	 * The index which corresponds to the checkbutton to check.<br>
	 * If the argument is -1 and a null-choice is permitted, all buttons
	 * will be unchecked
	 * @return
	 * Returns TRUE in case of success, or FALSE otherwise
	 */
	public boolean setCheckedButton( int i){
		
		MTCheckButton checkedButton = getCheckedButton();

		if( i == -1){			

			if( isNullChoiceAllowed() ){					// uncheck everything?
				if( checkedButton != null){
					checkedButton.setChecked(  false);
				}
				setCheckedButtonHelper( null);
				return true;
			}else{
				checkedButton.setChecked( true);
				return false;
			}
			
		}
			
		if( i >= getButtonList().size() || i<0)			// within the range?
			return false;
		
		if( checkedButton != null)						// Std behaviour
			checkedButton.setChecked( false);		
		checkedButton = getButtonList().get(i);
		checkedButton.setChecked( true);
		setCheckedButtonHelper( checkedButton);
		return true;
	}
	
	/**
	 * Sets the checked button within this container.
	 * If the specified checkbutton is not in the container, nothing happens.
	 * @param btn
	 * The <i>MTCheckButton</i> to check.<br>
	 * If the argument is NULL and a null-choice is permitted,
	 * all buttons will be unchecked
	 * @return
	 * Returns TRUE in case of success, or FALSE otherwise
	 */
	public boolean setCheckedButton( MTCheckButton btn){
		
		if( btn == null)							// uncheck everything?
			return setCheckedButton( -1);
		
		int index = getButtonList().indexOf( btn);				
		if( index == -1)							// object exists?
			return false;
		
		return setCheckedButton( index);			// Std behaviour
	}
	
	/**
	 * Specifies, if a button must be checked or not
	 * but does not automatically check any button, if set to 
	 * FALSE an no button is already checked!
	 * @param b
	 * The specificator as a <i>boolean</i>
	 */
	public abstract void setNullChoiceAllowed( boolean b);

	/**
	 * Indicates, if one button must be checked or not.
	 * @param b
	 * Returns TRUE, if all buttons unchecked shall be allowed,
	 * otherwise returns false
	 */	
	public abstract boolean isNullChoiceAllowed();
	
	/*
	 * (non-Javadoc)
	 * @see components.AbstractGUIComponent#setMode(util.Mode)
	 */
	public void setMode( Mode mode){
		List<MTCheckButton> buttons = getButtonList();
		for( MTCheckButton b : buttons)
			b.setMode( mode);
		this.mode=mode;
	}
	
	// HEPLERS ////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This method installs a listener On a checkbutton.
	 * This listener monitors the incoming gestures On the
	 * checkbutton and informs the container, which then
	 * handles with the updates.
	 * @param btn
	 * The <i>MTCheckButton</i> to monitor.
	 */
	protected abstract void addListener( MTCheckButton btn);
	
	/**
	 * This method uninstalls the listener from an element
	 * in this container.
	 * @param i
	 * The index corresponding to a checkbutton which is to be deregistered
	 */
	protected abstract void removeListener( int i);
	
	/**
	 * Special helper which is invoced by add() right after the new button is putted in the list.
	 * This helper allows you to do some special things like repositioning 
	 * or change style of the button or whatever.
	 * If you don't need some fancy stuff, just leave its body blank.
	 * @param btn
	 * The <i>MTCheckButton</i> which is about to be added
	 */
	protected abstract void doAdditionalAddStuff( MTCheckButton btn);
	
	/**
	 * Special helper which is invoced by remove() right after the given button is thrown out the list.
	 * This helper allows you to do some special things like repositioning 
	 * or change style of the button or whatever.
	 * If you don't need some fancy stuff, just leave its body blank.
	 * @param btn
	 * The <i>MTCheckButton</i> which is about to be removed
	 */
	protected abstract void doAdditionalRemoveStuff( MTCheckButton btn);
	
	/**
	 * Sets the checked button to the specified value in a strict manner,
	 * that means, independent of any constrains.
	 * @param btn
	 * The <i>MTCheckButton</i> to be checked.
	 */
	protected abstract void setCheckedButtonHelper( MTCheckButton btn);
}
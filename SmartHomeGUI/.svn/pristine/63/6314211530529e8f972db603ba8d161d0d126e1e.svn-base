package widgets;

import java.util.List;

import processing.core.PApplet;


import util.Mode;
import widgets.MTButton;

/**
 * This class represents a container for <i>MTButtons</i>. Thes tabs can be put in this container,
 * which handles their alignment and positioning within this container. It groups them and provides them
 * with the well known tab-functionality: the ability, to be only exclusively active, to be only active, 
 * if all other tabs are not. In order to do that, a special listener will be registered at the added tabs, 
 * which forwards tap-gestures right to the container, that then handles the request in the right way.<br>
 * 
 * TODO: Test
 * 
 * @author langenhagen
 * @version 20110523
 */
public abstract class MTTabContainer extends AbstractGUIWidget {

	// CONSTRUCTOR ////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main constructor
	 * @param pApplet
	 * The Application which is rendering this GUI-component as a <i>PApplet</i>
	 */
	public MTTabContainer(PApplet pApplet){
		super( pApplet);
	}

	// METHODS ////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds the given tab to the tab-container.
	 * If the specified button is NULL or is already 
	 * added to this tab-container, nothing happens.<br>
	 * If this is the first element to be put in the container,
	 * it will be marked as active.
	 * @param tab
	 * The new button to add as an <i>MTButton</i>
	 * @return
	 * Returns TRUE, if adding was successful, otherwise returns FALSE
	 */
	public boolean add( MTButton tab){

		if(tab == null || getTabList().contains( tab)){
			System.err.println("MTButtonContainer(): Adding the same MTButton to the same MTButtonContainer two times is not possible.");
			return false;
		}
		
		if( getTabList().isEmpty()){
			setOldTabMode( tab.getMode());
			setActiveTab( tab);
			tab.setMode( Mode.SIGNAL);
		}
		
		getTabList().add( tab);
		this.doAdditionalAddStuff( tab);
		return true;
	}
	
	/**
	 * Removes the given tab from the tab-container.
	 * If the specified button does not exist in this tab-container,
	 * nothing happens.<br>
	 * If the button to remove is the active one, the first one will be active
	 * @param tab
	 * The new button to remove as an <i>MTButton</i>
	 * @return
	 * Returns TRUE, if removal was successful, otherwise returns FALSE.
	 */
	public boolean remove( MTButton tab){
		
		if(tab == null || !getTabList().contains( tab))
			return false;
		
		boolean marker = false;
		if( getActiveTab() == tab && getTabList().size()>1)
			marker = true;
		
		getTabList().remove( tab);
		
		if( marker)
			this.setActiveTab( 0);
		
		this.doAdditionalRemoveStuff( tab);
		return true;
	}
	
	/**
	 * Sets the list of tabs according to the given list.
	 * If some button is found more than once in this list,
	 * just the first encounter of this button causes an
	 * appending to the container, you cannot add a button to this
	 * container more than once. If the specified list is
	 * NULL or emty, nothing happens.
	 * Under the hood, the first tab will be active, 
	 * but you actually won't see it.
	 * @param tabs
	 * The a non-empty list of new tabs as a <i>List</i> of type <i>MTButton</i>
	 */
	public void setAllTabs( List<MTButton> tabs){
		
		if( tabs == null || tabs.isEmpty())
			return;
		
		getTabList().clear();
		for( MTButton t : tabs)
			this.add( t);
	}
	
	/**
	 * Retrieves the list of all tabs in this container.
	 * @return
	 * All tabs within this container as a <i>List</i> of type <i>MTButton</i>
	 */
	public abstract List<MTButton> getTabList();
	
	/**
	 * Sets the active tab. If the specified tab 
	 * is not associated with this container,
	 * no changes will be performed.
	 * @param tab
	 * The tab to set active as an <i>MTButton</i>
	 * @return
	 * Returns TRUE, if this action was successful, otherwise returns false
	 */
	public boolean setActiveTab( MTButton tab){
		
		if( tab == null || !getTabList().contains( tab))
			return false;
		
		getActiveTab().setMode( getOldTabMode());
		
		setActiveTab( tab);
		setOldTabMode(tab.getMode());
		tab.setMode(  Mode.SIGNAL);
		return true;
	}
	
	/**
	 * Sets the active tab. If the tab at the specified position is not 
	 * associated with this container, no changes will be performed.
	 * @param pos
	 * The index of the tab as an <i>int</i>
	 * @return
	 * Returns TRUE if the operation was successful, otherwise returns FALSE
	 */
	public boolean setActiveTab( int pos){
		
		if( pos < 0 || pos >= getTabList().size())
			return false;
		
		return this.setActiveTab( getTabList().get(pos));
	}
	
	/**
	 * Returnst the currently active tab
	 * @return
	 * The currently active tab as an <i>MTButton</i>
	 */
	public abstract MTButton getActiveTab();
	
	/**
	 * Returnst the index of the currently active tab
	 * @return
	 * The index currently active tab as an <i>int</i>
	 */
	public int getActiveTabNum(){
		return getTabList().indexOf( getActiveTab());
	}
	
	// HELPERS ////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Saves the former mode of the new active tab 
	 * for reconstructional reasons.
	 * @param mode
	 * The former mode as a <i>Mode</i>
	 */
	protected abstract void setOldTabMode( Mode mode);
	
	/**
	 * Retrieves the former mode of the currently active  tab 
	 * for reconstructional reasons.
	 * @return
	 * The former mode as a <i>Mode</i>
	 */
	protected abstract Mode getOldTabMode();
	
	/**
	 * Special helper which is invoced by add() right after the new tab is putted in the list.
	 * This helper allows you to do some special things like repositioning 
	 * or change style of the tab or whatever.
	 * If you don't need some fancy stuff, just leave its body blank.
	 * @param tab
	 * The <i>MTButton</i> which is about to be added
	 */
	protected abstract void doAdditionalAddStuff( MTButton tab);
	
	/**
	 * Special helper which is invoced by remove() right after the given button is thrown out the list.
	 * This helper allows you to do some special things like repositioning 
	 * or change style of the tab or whatever.
	 * If you don't need some fancy stuff, just leave its body blank.
	 * @param tab
	 * The <i>MTButton</i> which is about to be removed
	 */
	protected abstract void doAdditionalRemoveStuff( MTButton tab);
}
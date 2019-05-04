package home.stateReadersWriters;

import home.Home;

/**
 * This interface provides a method for saving state informations of a <i>Home</i>.
 * 
 * @author langenhagen
 * @version 20110925
 */
public interface StateWriter {
	
	/**
	 * Saves the information according to its specialized implementation.
	 * @param home
	 * The home for which the information shall be stored.
	 * @return
	 * Returns TRUE, if loading was successful, otherwise returns FALSE.
	 */
	public boolean save( Home home);
}

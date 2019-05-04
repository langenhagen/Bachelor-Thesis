package home.stateVisualizers.std;

import org.mt4j.util.math.Vector3D;
import org.sercho.masp.models.Context.Blind;

import widgets.MT3DObject;

import home.Item;
import home.stateVisualizers.StateVisualizer;

/**
 * This special <i>StateVisualizer</i> is intended to visualize states
 * of blinds.
 * 
 * This concrete state visualizer changes the height of the item's view
 * according to the settings of the the level from 0 to 100%.
 * 
 * 
 * @author langenhagen
 * @version 20120126
 */
public class BlindStateVisualizer extends StateVisualizer {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////

	/** the standard heigth of the view */
	float stdHeight;
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////

	/* (non-Javadoc)
	 * @see home.StateVisualizers.StateVisualizerStrategy#StateVisualizerStrategy(Item)
	 */
	public BlindStateVisualizer(Item item){
		super( item);
		
		stdHeight = item.getView().getHeight();
		
		Blind blind = (Blind)item.getDevice();
		if( blind.getLevel() != null){
			blind.getLevel().eAdapters().add( this);
		}
		
	}

	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/* (non-Javadoc)
	 * @see home.StateVisualizers.StateVisualizerObserver#notifyObserver()
	 */
	@Override
	public void notifyVisualizer(){

		Blind blind = (Blind)item.getDevice();
		MT3DObject view = item.getView();
		Vector3D scalingPoint = view.getPosition();
		
		// if the blind is On
		if( blind.getOnValue()){
			
			if( blind.getLevel() != null && blind.getLevel().getValue() != null){
				
				float level = blind.getLevel().getValue();
				
				// DUE TO ABSOLUTELY NO DOCUMENTATION OF THE CONTEXT MODEL, I MAKE FOLLOWING ASSUMPTION
				// 0 	- high
				// 100  - low
		
				view.scale( 1, stdHeight * level/100 , 1, scalingPoint);	
			}
			
			System.out.println( this.getClass().getName() + " switched " + item.getID() + "'s view On.");
		}else{
			System.out.println( this.getClass().getName() + " switched " + item.getID() + "'s view Off.");
		}
		
	}
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////	

}
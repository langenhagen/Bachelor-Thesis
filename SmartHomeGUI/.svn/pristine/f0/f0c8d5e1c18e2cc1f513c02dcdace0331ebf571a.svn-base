package home.stateVisualizers.std;

import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.mesh.MTTriangleMesh;
import org.sercho.masp.models.Context.Lamp;

import home.Item;
import home.stateVisualizers.StateVisualizer;

/**
 * This special <i>StateVisualizer</i> is intended to visualize states
 * of lamps.
 * This concrete state visualizer changes ambient color of all materials 
 * according to the settings of the lamps in a range of 0.2 (Off) to 1.0 (completely bright).
 * 
 * 
 * @author langenhagen
 * @version 20111230
 */
public class LampStateVisualizer extends StateVisualizer {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////

	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////

	/* (non-Javadoc)
	 * @see home.StateVisualizers.StateVisualizerStrategy#StateVisualizerStrategy(Item)
	 */
	public LampStateVisualizer(Item item){
		super( item);
		
		Lamp lamp = (Lamp)item.getDevice();
		if( lamp.getDimmingLevel() != null){
			lamp.getDimmingLevel().eAdapters().add( this);
		}
	}

	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/* (non-Javadoc)
	 * @see home.StateVisualizers.StateVisualizerObserver#notifyObserver()
	 */
	@Override
	public void notifyVisualizer(){

		Lamp lamp = (Lamp)item.getDevice();
		
		MTComponent[] meshes = item.getView().getMeshGroup().getChildren();
		
		// if the lamp is On
		if( lamp.getOnValue()){
			
			// take dimming level into account
			float brightness = 1;
			if( lamp.getDimmingLevel() != null && lamp.getDimmingLevel().getValue() != null){
				float dimmingLevel = lamp.getDimmingLevel().getValue();
				brightness = dimmingLevel*0.008f + 0.2f;
			}
			
			float[] ambient = { brightness, brightness, brightness, 1 };
			for( MTComponent m : meshes){
				((MTTriangleMesh)m).getMaterial().setAmbient( ambient);
			}
			System.out.println( this.getClass().getName() + " switched " + item.getID() + "'s view On.");
			
		// if the lamp is Off
		}else{
			for( MTComponent m : meshes){	
				float[] ambient = { .2f, .2f, .2f, 1 };
				((MTTriangleMesh)m).getMaterial().setAmbient( ambient);
			}
			System.out.println( this.getClass().getName() + " switched " + item.getID() + "'s view Off.");
		}
	}
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////	

}
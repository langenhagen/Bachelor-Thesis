package home.stateVisualizers.std;

import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.mesh.MTTriangleMesh;
import home.Item;
import home.stateVisualizers.StateVisualizer;

/**
 * This special <i>StateVisualizer</i> is intended to visualize states
 * of arbitrary items for debug or testing reasons.
 * 
 * @author langenhagen
 * @version 20111218
 */
public class DummyVisualizer extends StateVisualizer {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** indicator for switching values */
	boolean b = false;
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////

	/* (non-Javadoc)
	 * @see home.StateVisualizers.StateVisualizerObserver#StateVisualizerObserver(Item)
	 */
	public DummyVisualizer(Item item){
		super( item);
	}

	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/* (non-Javadoc)
	 * @see home.StateVisualizers.StateVisualizerObserver#notifyObserver()
	 */
	@Override
	public void notifyVisualizer(){
		
		MTComponent[] meshes = item.getView().getMeshGroup().getChildren();
		
		// if b == true
		if(b){
			for( MTComponent m : meshes){
				
				float[] ambient = { .2f, .2f, .2f, 1 };
				((MTTriangleMesh)m).getMaterial().setAmbient( ambient);
			}
			System.out.println( this.getClass().getName() + " switched " + item.getID() + "'s view On.");
			
		// if b == false
		}else{
			for( MTComponent m : meshes){
				
				float[] ambient = { 1, 0, 0, 1 };
				((MTTriangleMesh)m).getMaterial().setAmbient( ambient);
			}
			System.out.println( this.getClass().getName() + " switched " + item.getID() + "'s view On.");
		}
		
		b = !b;
	}
}
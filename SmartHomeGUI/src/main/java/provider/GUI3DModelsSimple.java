package provider;

import java.util.HashMap;

import processing.core.PApplet;
import widgets.MT3DObject;
import widgets.std.StdMT3DObject;



/**
 * The GUI3DModels-provider provides logic for loading all
 * 3D-Object into memory and then providing information about them.
 * 
 * @author langenhagen
 * @version 20110630
 */
public class GUI3DModelsSimple extends GUI3DModels {

	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Gets the a copy ot the model with the specified name 
	 * (which means not the path, but its actual <i>name</i>).
	 * @param name
	 * The name of the model to get as a <i>String</i>
	 * @return
	 * Returns the model as an <i>MT3DObject</i> or null if the model could not be found
	 */
	public MT3DObject getModel( String name){

		return new StdMT3DObject( (StdMT3DObject)map.get( "SampleModel"));
	}
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Helper method for setting up the provider.
	 * Truly sets up the models.
	 * Override this method when you create a child class.
	 * * <strong>Note:</strong>If you want to add new models to the available ones,
	 * please add their individual setup here. 
	 * @param pApp
	 * The application to render the models as a <i>PApplet</i>
	 * @param rootDirectories
	 * An <i>Iterable</i> of type <i>String</i> that contains all root directories where the factory can find resources.
	 */
	protected void setupHelper( PApplet pApp, Iterable<String> rootDirectories){
		this.pApp = pApp;
		map = new HashMap<String, MT3DObject>();
		
		for( String dir : rootDirectories){
						
			try{
				loadModel( dir + "/SampleModel");
				break;
			}catch( Exception e){
				System.err.println("ERROR: Not able to load model from" + dir + "/SampleModels!");
				e.getMessage();
				e.getStackTrace();
			}
		} // END for
	}
}
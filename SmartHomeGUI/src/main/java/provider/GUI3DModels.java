package provider;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;

import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.util.math.Vector3D;
import org.sercho.masp.models.Context.Area;
import org.sercho.masp.models.Context.Environment;
import org.sercho.masp.models.Context.Place;

import processing.core.PApplet;
import widgets.MT3DObject;
import widgets.MTContextModel3DObject;
import widgets.std.StdMT3DObject;



/**
 * The GUI3DModels-provider provides logic for loading all
 * 3D-Object into memory and then providing information about them.
 * 
 * @author langenhagen
 * @version 20120627
 */
public class GUI3DModels {
	
	// CLASS VARS /////////////////////////////////////////////////////////////////////////////////
	
	/** singleton instance of this class */
	protected static GUI3DModels instance;
	
	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the renderer of the models */
	PApplet pApp;
	/** stores the 3D-objects along with a <i>String</i>-key which stores the model-names */
	Map<String , MT3DObject> map;
	
	// CONSTRUCTOR ////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main constructor.
	 */
	protected GUI3DModels(){
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Gets the singleton instance of <i>GUITextures</i>.
	 * @return
	 * The singleton instance of <i>GUITextures</i>.
	 */
	public static final GUI3DModels instance(){
		return instance;
	}
	
	/**
	 * Sets the 3D-models up and initializes the provider itself. Therefore it should be called once
	 * at the beginning of your program.
	 * Sets up the GUI3DModels-singleton to an instance of of a given <i>GUI3DModels</i> (Sub-)Class.<br>
	 * @param pApp
	 * The Application to render the fonts as a <i>PApplet</i>.
	 * @param clazz
	 * The <i>Class</i> <i>GUI3DModels</i> or some <i>Class</i> which extends it.
	 * @param rootDirectories
	 * An <i>Iterable</i> of type <i>String</i> that contains all root directories where the factory can find resources.
	 */
	public final static void setup(PApplet pApp, Class<? extends GUI3DModels> clazz, Iterable<String> rootDirectories){
		System.out.print("Loading models...\n");
		try{
			instance = clazz.newInstance();
		} catch(Exception e){
			System.err.println("Error: Could not instantiate object of class " + clazz.getName());
			e.printStackTrace();			
		}
		
		for( String dir : rootDirectories){
			if( dir.charAt( dir.length()-1)== '/' || dir.charAt( dir.length()-1)== '\\')
				dir = dir.substring( 0, dir.length()-1);
			dir.replace( "\\", "/");
		}
		
		instance.setupHelper( pApp, rootDirectories);
		System.out.print("Done!\n");	
	}

	/**
	 * Loads a model at the specified path and maintains it in this provider's memory, along with a key.
	 * If a model with the same key is already stored, it will be overridden.
	 * @param path
	 * The full path to the 3D-model as a <i>String</i>.
	 */
	public void loadModel( String path){

		MT3DObject obj = new StdMT3DObject(pApp, path);
		map.put( obj.getModelName(), obj);
	}
	
	/**
	 * Gets the a copy ot the model with the specified name 
	 * (which means not the path, but its actual <i>name</i>).
	 * @param name
	 * The name of the model to get as a <i>String</i>.
	 * @return
	 * Returns the model as an <i>MT3DObject</i> or null if the model could not be found.
	 */
	public MT3DObject getModel( String name){
		// XXX: could be a nicer solution
		
		if( map.get( name) == null)
			return null;
			
		MT3DObject obj =  new StdMT3DObject( (StdMT3DObject)map.get( name));
		return obj;
	}
	
	/**
	 * Gets the a copy ot the model with the specified name 
	 * (which means not the path, but its actual <i>name</i>).
	 * @param name
	 * The name of the model to get as a <i>String</i>.
	 * @param variant
	 * The variant to set as an <i>int</i>.
	 * @param scaleToUnitVal
	 * The multiplicator when the model whill be scaled to unit as a <i>float</i>.
	 * @return
	 * Returns the model as an <i>MT3DObject</i> or null if the model could not be found.
	 */
	public MT3DObject getModel( String name, int variant, float scaleToUnitVal){
		MT3DObject obj = getModel( name);
		if(obj == null)
			return null;
		
		obj.setVariant( variant);
		obj.scaleToUnit( scaleToUnitVal);
		obj.scaleGlobal( 1, -1, 1, obj.getPosition()); // just for convenience of a not upside down model
		
		return obj;
	}
	
	/**
	 * Retrieves an MT3DObject from an environment.
	 * @param environment
	 * The associated <i>Environment</i>.
	 * @return
	 * An <i>MT3DObject</i> that represents the specified environment.
	 * 
	 */
	public MT3DObject getModelFromContextModel( Environment environment ){

		return new  MTContextModel3DObject( pApp, environment, false);		
	}
	
	/**
	 * Destroys the model with the specified name by unloading it from memory and therefore freeing
	 * the working memory.
	 * @param name
	 * The name of the model to unload as a <i>String</i>.
	 * @return
	 * Returns TRUE if the operation was successfull or FALSE otherwise 
	 * (i.e. when no model with the specified name even existed).
	 */
	public boolean unloadModel( String name){
		MT3DObject obj = map.get( name);
		
		if( obj == null)
			return false;
		
		map.remove( name);
		obj.destroy();

		return true;
	}
	
	/**
	 * Returns an <i>Array</i> of type <i>MT3DObject</i> which stores
	 * all 3D models that are managed by this provider.
	 * @return
	 * an <i>Array</i> of type <i>MT3DObject</i>.
	 */
	public MT3DObject[] getModelArray(){
		return (MT3DObject[])map.values().toArray();
	}
	
	/**
	 * Returns an <i>Array</i> of type <i>String</i> which stores the names 
	 * of all 3D models that are managed by this provider.
	 * @return
	 * an <i>Array</i> of type <i>MT3DObject</i>.
	 */
	public String[] getModelNameArray(){
		return map.keySet().toArray(new String[0]);
	}
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Helper method for setting up the provider.
	 * Truly sets up the models.
	 * Override this method when you create a child class.
	 * * <strong>Note:</strong>If you want to add new models to the available ones,
	 * please add their individual setup here.
	 * @param pApp
	 * The application to render the models as a <i>PApplet</i>.
	 * @param rootDirectories
	 * An <i>Iterable</i> of type <i>String</i> that contains all root directories where the factory can find resources.
	 */
	protected void setupHelper( PApplet pApp, Iterable<String> rootDirectories){
		this.pApp = pApp;
		this.map = new HashMap<String, MT3DObject>();	
		
		for( String dir : rootDirectories){

			// look only for directories
			FileFilter filter = new FileFilter() {
			    public boolean accept(File file) {
			        return file.isDirectory();
			    }
			};
			

			// load all models		
			File[] directories = new File(dir).listFiles( filter);
			for( File s : directories){
				try{
					loadModel( dir + "/" + s.getName());
				}catch( Exception e){
					System.err.println("ERROR: Not able to load model from " + dir + "/" + s.getName() + "!");
					System.err.println(e.getMessage());
					e.printStackTrace();
				}
			}
			
		} // END for
		
	}
}
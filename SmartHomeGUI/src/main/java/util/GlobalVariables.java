package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;


/**
 * This class is intended to read some key/variable-pairs out of somewhere
 * and provide them in the whole system.
 * 
 * @author langenhagen
 * @version 20120905
 */
public class GlobalVariables {
	
	// CLASS VARIABLES //////////////////////////////////////////////////////////////////////////////
	
	/** Singleton instance */
	protected static GlobalVariables instance;

	/** the map storing all the key/variable pairs */
	private Map<String, String> variables = new HashMap<String, String>();
	
	// CONSTRUCTOR //////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main Construktor of Singleton.
	 * Protected; May be visible for children.
	 */
	protected GlobalVariables(){}
	
	
	// PUBLIC STATIC METHODS ////////////////////////////////////////////////////////////////////////
	
	// setup-method for dynamic initialization
	/**
	 * setup()<br>
	 * Sets up the Singleton to an instance of of a given <i>Singleton</i> (Sub-)Class.
	 * @param clazz
	 * The <i>Class</i> <i>Singleton</i> or some <i>Class</i> which extends its interface.
	 * @param pathToVariables
	 * A <i>String</i> that contains the path to the appropriate config-file.
	 */
	public final static void setup( Class<? extends GlobalVariables> clazz, String pathToVariables){
		try{			
			
			instance = clazz.newInstance();
			instance.setupHelper(pathToVariables);
			
		} catch(Exception e){ e.printStackTrace(); }
	}
	
	/**
	 * Gets the singleton instance of <i>Singleton</i>.
	 * @return
	 * The singleton instance of <i>Singleton</i>.
	 */
	public static final GlobalVariables instance(){
		return instance;
	}
	
	// PUBLIC METHODS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Retrieves the value to the given key as a string representation.
	 * @param key
	 * The key to a corresponding variable as a <i>String</i>.
	 * @return
	 * A <i>String</i> or null in case of some unknown given key.
	 */
	public String get( String key){
		
		return variables.get(key);
	}

	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Helper method for setting up the singleton.
	 * Override this method when you create a child class.
	 * @param pathToVariables
	 * A <i>String</i> that contains the path to the appropriate config-file.
	 */
	protected void setupHelper( String pathToVariables ){
		parse(pathToVariables);
	}
	
	/**
	 * Reads a file and parses keys and variables.
	 * @param path
	 * The path to the config file to be read.
	 * @return
	 * Returns TRUE on success, otherwise returns FALSE.
	 */
	public boolean parse( String path){
		
		System.out.println("Reading Global Variables file...");
		
		try{
			FileReader r = new FileReader( path);
			BufferedReader reader = new BufferedReader( r);

			// process every line
			String line = reader.readLine();
			
			while( line != null){
				
				// filter out comments and blank lines
				if( line.startsWith("#") || line.length()==0){
					line = reader.readLine();
					continue;
				}
				
				String[] key_value_pair = line.split("=", 2);
				
				variables.put(key_value_pair[0].trim(), key_value_pair[1].trim());
								
				line = reader.readLine();
			}
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}

		System.out.println("Global Variables file successfully read.");
		
		return true;
	}
	
}

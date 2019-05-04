package util;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * This class loads the settings-file for a pointing-test.
 * <strong>Caution:</strong> Since this class isn't part 
 * of my actual bachelor thesis, and just done for Ali Nazaris 
 * pointing-test scenario, this class is merely a hack.
 * So don't wonder about no comments or possible strange strange
 * code fragments.
 * 
 * @author langenhagen
 * @version 20110722
 */
public class TestSettingsLoader {
	
	// Instance vars //////////////////////////////////////////////////////////////////////////////
	
	public int numObjects;
	public double minDistance;
	public double maxDistance;
	public double minSize;
	public double maxSize;
	
	// METHODS ////////////////////////////////////////////////////////////////////////////////////
	
	public boolean parse( String path){
		
		try{
			FileReader r = new FileReader( path);
			BufferedReader reader = new BufferedReader( r);

			// process every line
			String line = reader.readLine();
			
			int i=0;
			
			while( line != null){
				
				// filter out comments and blank lines
				if( line.startsWith("#") || line.length()==0){
					line = reader.readLine();
					continue;
				}
				
				switch (i){
				case 0:		// num models
					numObjects = new Integer( line);
					break;
				case 1:		// min dist
					minDistance = new Double( line);
					break;
				case 2:		// max dist
					maxDistance = new Double( line);
					break;
				case 3:		// min size
					minSize = new Double( line);
					break;
				case 4:		// max size
					maxSize = new Double( line);
					break;
				}
				i++;
				line = reader.readLine();
			}
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	
		System.out.println("Read settings file");
		
		return true;
	}	
}
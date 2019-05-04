package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

/**
 * This class parses info-files for additional 3d-model information 
 * and makes them accessible.<br>
 * <strong>Caution:</strong> This class doesn't take care of errors 
 * made by the user, that is, erroneous 3d-models can lead to undefined behaviour.<br><br>
 * 
 * Not the best implementation since I discovered the java.util.Scanner for better parsing
 * but hey.. it works!
 * 
 * @author langenhagen
 * @version 20110621
 */
public class ModelInfo {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** indicates, if the normals shall be inverted */
	List<Boolean> invertNormals;
	/** defines the crease angles of every variant */
	List<Float> creaseAngles;
	/** indicates, whether the textures of a corresponding shall be flipped in x-direction */
	List<Boolean> flipTexturesX;
	/** indicates, whether the textures of a corresponding shall be flipped in y-direction */
	List<Boolean> flipTexturesY;
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructor #1<br>
	 * When using this constructor, you must invoke <i>ModelInfo.parse()</i>
	 * at a later point.
	 */
	public ModelInfo(){
		invertNormals = null;
		creaseAngles = null;
		flipTexturesX = null;
		flipTexturesY = null;
	}
	
	/**
	 * Constructor #2<br>
	 * Parses the info.txt-file in the specified directory
	 * Afterwards you can get the according values with the 
	 * corresponding methods.
	 * @param path
	 * The directory of the model to parse as a <i>String</i>
	 */
	public ModelInfo( String path){
		parse( path);
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Parses the info.txt-file in the specified directory
	 * Afterwards you can get the according values with the 
	 * corresponding methods.
	 * @param path
	 * The directory of the model to parse as a <i>String</i>
	 * @return
	 * Returns TRUE in case of success or FALSE in case of error
	 */
	public boolean parse( String path){

		invertNormals = new LinkedList<Boolean>();
		creaseAngles = new LinkedList<Float>();
		flipTexturesX = new LinkedList<Boolean>();
		flipTexturesY = new LinkedList<Boolean>();
		
		try{
			FileReader r = new FileReader( path + "/info.txt");
			BufferedReader reader = new BufferedReader( r);

			// process every line
			String line = reader.readLine();
			while( line != null){
				
				// filter out comments and blank lines
				if( line.startsWith("#") || line.length()==0){
					line = reader.readLine();
					continue;
				}
				
				String[] lineArr = line.split("[ \t]");
				invertNormals.add( lineArr[1].equals( "0") ? false : true);
				creaseAngles.add( new Float(lineArr[2]));
				flipTexturesX.add( lineArr[3].equals( "0") ? false : true);
				flipTexturesY.add( lineArr[4].equals( "0") ? false : true);
				
				line = reader.readLine();
			}
		}catch (Exception e){
			e.printStackTrace();	
		}
		return false;
	}
	
	/**
	 * Retrieves a <i>List</i> with indicators for inverting the model's normals.
	 * @return
	 * A <i>List</i> of <i>Booleans</i> which indicate if the model's variants
	 * normals shall be inverted
	 */
	public List<Boolean> getAllInvertTextures(){
		return invertNormals;
	}
	
	/**
	 * Retrieves a <i>List</i> with all crease angles.
	 * @return
	 * A <i>List</i> of <i>Floats</i> which symbolize the crease-angle of the model
	 * where the index of the array represents the corresponding variant
	 */
	public List<Float> getAllCreaseAngles(){
		return creaseAngles;
	}
	
	/**
	 * Retrieves a <i>List</i> with the information,
	 * whether the x-texture shall be flipped or not.
	 * @return
	 * A <i>List</i> of <i>Boolean</i> which specifies, if the model's texture 
	 * shall be fliped On the x-axis of the model, where the index of the array 
	 * represents the corresponding variant
	 */
	public List<Boolean> getAllFlipTexturesX(){
		return flipTexturesX;
	}
	
	/**
	 * Retrieves a <i>List</i> with the information,
	 * whether the y-texture shall be flipped or not.
	 * @return
	 * A <i>List</i> of <i>Boolean</i> which specifies, if the model's texture 
	 * shall be fliped On the y-axis of the model, where the index of the array 
	 * represents the corresponding variant
	 */
	public List<Boolean> getAllFlipTexturesY(){
		return flipTexturesY;
	}
	
	/**
	 * Returns if the normals of the specified variant of the model shall be inverted
	 * @param i
	 * The variant to look up for. If this value belongs to no corresponding variant
	 * then 0 will be returned.
	 * @return
	 * Returns if the normals of the specified variant of the model shall be inverted 
	 * as a <i>Boolean</i>. If the specified variant is not valid,
	 * then false will be returned
	 */
	public boolean getInvertNormals( int i){
		if( i<0 || i>=invertNormals.size())
			return false;
		return invertNormals.get( i);
	}
	
	/**
	 * Retrieves the crease angle for the specified variant of the model
	 * @param i
	 * The variant to look up for. If this value belongs to no corresponding variant
	 * then 0 will be returned.
	 * @return
	 * Returns the crease angle as a <i>Float</i>. If the specified variant is not valid,
	 * then 0 will be returned
	 */
	public float getCreaseAngle( int i){
		if( i<0 || i>=creaseAngles.size())
			return 0;
		return creaseAngles.get( i);
	}
	
	/**
	 * Retrieves whether to flip the texture for the specified variant of the model On the x-axis.
	 * @param i
	 * The variant to look up for. If this value belongs to no corresponding variant
	 * then FALSE will be returned.
	 * @return
	 * Returns whether to flip the texture On the x-axis as a <i>boolean</i>.
	 * If the specified variant is not valid, then FALSE will be returned
	 */
	public boolean getFlipTextureX( int i){
		if( i<0 || i>=flipTexturesX.size())
			return false;
		return flipTexturesX.get( i);
	}
	
	/**
	 * Retrieves whether to flip the texture for the specified variant of the model On the y-axis.
	 * @param i
	 * The variant to look up for. If this value belongs to no corresponding variant
	 * then FALSE will be returned.
	 * @return
	 * Returns whether to flip the texture On the y-axis as a <i>boolean</i>.
	 * If the specified variant is not valid, then FALSE will be returned
	 */
	public boolean getFlipTextureY( int i){
		if( i<0 || i>=flipTexturesY.size())
			return false;
		return flipTexturesY.get( i);
	}
}
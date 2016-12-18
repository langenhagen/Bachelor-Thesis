package util;

import org.mt4j.util.math.Matrix;
import org.mt4j.util.math.Vector3D;

/**
 * This class provides static helper functions that could be used all over the whole place.
 * 
 * @author langenhagen
 * @version 20120529
 */
public class MathUtil {

	/**
	 * Creates a rotation matrix around an arbitrary axis.
	 * @param axis
	 * The axis as a <i>Vector3D</i>.
	 * @param angle
	 * The angle in degrees as a <i>float</i>.
	 * @return
	 * The appropriate rotation <i>Matrix</i>.
	 */
	public static Matrix createRotationMatrix( Vector3D axis, float angle){
		
		Matrix ret = null;
		
		axis.normalizeLocal();
		
		double cosa = Math.cos(Math.toRadians(-angle));
		double ncosa = 1 - cosa;
		double sina = Math.sin(Math.toRadians(-angle));
		float nx = axis.getX();
		float ny = axis.getY();
		float nz = axis.getZ();
		
		// dunno why i need this try catch block
		try{	
			ret = new Matrix(
					(float)( cosa + nx*nx*ncosa),		(float)( nx*ny*ncosa - nz*sina),	(float)( nx*nz*ncosa + ny*sina),	0,
					(float)( ny*nx*ncosa + nz*sina),	(float)( cosa + ny*ny*ncosa),		(float)( ny*nz*ncosa - nx*sina),	0, 
					(float)( nz*nx*ncosa - ny*sina),	(float)( nz*ny*ncosa + nx*sina),	(float)( cosa + nz*nz*ncosa),		0,
					0,									0,									0,									1
			);

		} catch(Exception e){
			System.err.println("Error in Calculating Rotation Matrix!");
			System.err.println( e.getMessage());
			e.printStackTrace();
		}
		return ret;
	}	
}

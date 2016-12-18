package widgets;

import java.util.List;

import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.mesh.MTTriangleMesh;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.opengl.GLMaterial;

import processing.core.PApplet;

/**
 * This class represents a 3D-object in the GUI context and encapsulates much of the
 * 'hacking' you would need to create a visibility 3D-object with standard mt4j-commands.<br>
 * Since a specific object can have multiple visual variants, this class enables you,
 * to switch between these variants at runtime.<br>
 * 
 * TODO: debug: some issues when moving the object upwards/downwards
 * 
 * @author langenhagen
 * @version 20120529
 */
public abstract class MT3DObject extends AbstractGUIWidget{

	/** 
	 * Default constructor
	 * @param pApplet
	 * The Application which is rendering this GUI-component as a <i>PApplet</i>
	 */
	public MT3DObject(PApplet pApplet){
		super( pApplet);
	}

	
	/**
	 * Inverts the face normals of this MT3DObject.
	 */
	public abstract void invertNormals();
	
	/**
	 * Returns all the meshes, of type <i>MTTriangleMesh</i>,
	 * packed in an <i>MTComponent</i> as its children.
	 * @return
	 * An <i>MTComponent</i>, containing all Meshes as its children.
	 */
	public abstract MTComponent getMeshGroup();
	
	/**
	 * Returns the biggest of this 3D-Object.
	 * @return
	 * The biggest mesh as an <i>MTTriangleMesh</i>
	 */
	public abstract MTTriangleMesh getBiggestMesh();
	
	/**
	 * Scales this 3D-object to the unit multiplied a factor f, so it fits into a
	 * f*f square. Since mt4j doesn't support extracting the depth of a geometric object,
	 * the fitting is only guaranteed for the width and the height in GLOBAL coordinates.
	 *
	 * @param multiplicator
	 * The factor to multiply the scalation as a <i>float</i>
	 * @return
	 * Returns the factor used scale the 3D-object as a <i>float</i>
	 */
	public abstract float scaleToUnit( float multiplicator);
	
	/**
	 * Sets the material of this 3D-object to the specified one
	 * by setting this material to all submeshes of this 3D-object.
	 * @param material
	 * The material as a <i>GLMaterial</i>
	 */
	public abstract void setMaterial( GLMaterial material);
	
	/**
	 * Gets all materials of this 3D-object.
	 * @return
	 * The materials as a <i>List</i> of <i>GLMaterial</i>
	 */
	public abstract List<GLMaterial> getMaterials();
	
	/**
	 * Since one object in the smart-home GUI-context can have different visual modes, 
	 * like a lamp which can be turned On or Off, 
	 * or an object can simply have different visual styles, like different sorts of desks, 
	 * you can switch between these styles by changing the number of the active variant.
	 * Variant-numbers are in sequential order, meaning that it may not be possible,
	 * that variant 1 and 3 exist, but 2 doesn't.
	 * @param num
	 * A variant's number as an <i>int</i>
	 * @return
	 * Returns TRUE, if the specified number is the number of a valid variant,
	 * otherwise it returns FALSE
	 */
	public abstract boolean setVariant( int num);
	
	/**
	 * Since one object in the smart-home GUI-context can have different visual modes, 
	 * like a lamp which can be turned On or Off, 
	 * or simply can have different visual styles, like different sorts of desks, 
	 * you can switch between these styles by changing the number of the active variant
	 * using <i>setVariant()</i>.
	 * Variant-numbers are in sequential order, meaning, that it is not possible,
	 * that variant 1 and 3 exist, but 2 doesn't. <br>
	 * This method returns the number of the current active variant.
	 * @return
	 * The number of the current variant as an <i>int</i>
	 */
	public abstract int getVariant();
	
	/**
	 * Since one object in the smart-home GUI-context can have different visual modes, 
	 * like a lamp which can be turned On or Off, 
	 * or simply can have different visual styles, like different sorts of desks, 
	 * you can switch between these styles by changing the number of the active variant
	 * using <i>setVariant()</i>.<br>
	 * This method returns the number of valid variants, which exist for this object.
	 * This number should be at least 1.
	 * @return
	 * The number of valid variants as an <i>int</i>.
	 */
	public abstract int getNumOfVariants();
	
	/**
	 * Retrieves the name of the 3D-model.
	 * @return
	 * The name of the 3D-model as a <i>String</i>
	 */
	public abstract String getModelName();
	
	/**
	 * Causes the 3D-object to rotate around its center about the specified 
	 * axis and degrees in the given <i>TransformSpace</i>.
	 * @param axis
	 * The rotation axis as a <i>Vector3D</i>.
	 * @param degree
	 * The degrees to rotate in deg as a <i>float</i>.
	 * @param space
	 * The <i>TransformSpace</i> indicatiing the 3-dimensional space in which to rotate.
	 */
	public abstract void rotate( Vector3D axis, float degree, TransformSpace space);
	
	/**
	 * Causes the 3D-object to x-rotate around its center about the specified degrees
	 * in the given <i>TransformSpace</i>.
	 * @param degree
	 * The degrees to rotate in deg as a <i>float</i>.
	 * @param space
	 * The <i>TransformSpace</i> indicatiing the 3-dimensional space in which to rotate.
	 */
	public abstract void rotateX( float degree, TransformSpace space);
	
	/**
	 * Causes the 3D-object to y-rotate around its center about the specified degrees
	 * in the given <i>TransformSpace</i>.
	 * @param degree
	 * The degrees to rotate in deg as a <i>float</i>.
	 * @param space
	 * The <i>TransformSpace</i> indicatiing the 3-dimensional space in which to rotate.
	 */
	public abstract void rotateY( float degree, TransformSpace space);
	
	/**
	 * Causes the 3D-object to z-rotate around its center about the specified degrees
	 * in the given <i>TransformSpace</i>.
	 * @param degree
	 * The degrees to rotate in deg as a <i>float</i>.
	 * @param space
	 * The <i>TransformSpace</i> indicatiing the 3-dimensional space in which to rotate. 
	 */
	public abstract void rotateZ( float degree, TransformSpace space);
	
	/**
	 * Causes the 3D-object to rotate around the specified axis & 
	 * around its center with the specified degrees in global transform space.
	 * @param axis
	 * The rotation axis as a <i>Vector3D</i>.
	 * @param degree
	 * The degrees to rotate in deg as a <i>float</i>.
	 */
	public final void rotate( Vector3D axis, float degree){
		rotate(axis, degree, TransformSpace.GLOBAL);
	}
	
	/**
	 * Causes the 3D-object to rotate around the global x-axis & 
	 * around its center with the specified degrees.
	 * @param degree
	 * The degrees to rotate in deg as a <i>float</i>.
	 */
	public final void rotateX( float degree){
		rotateX(degree, TransformSpace.GLOBAL);
	}
	
	/**
	 * Causes the 3D-object to rotate around the global y-axis & 
	 * around its center with the specified degrees.
	 * @param degree
	 * The degrees to rotate in deg as a <i>float</i>
	 */
	public final void rotateY( float degree){
		rotateY(degree, TransformSpace.GLOBAL);
	}
	
	/**
	 * Causes the 3D-object to rotate around the global z-axis & 
	 * around its center with the specified degrees.
	 * @param degree
	 * The degrees to rotate in deg as a <i>float</i>
	 */
	public final void rotateZ( float degree){
		rotateZ(degree, TransformSpace.GLOBAL);
	}
}
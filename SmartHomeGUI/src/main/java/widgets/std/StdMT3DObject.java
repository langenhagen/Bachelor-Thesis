package widgets.std;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.mt4j.components.MTComponent;
import org.mt4j.components.MTLight;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.mesh.MTTriangleMesh;
import org.mt4j.util.math.Matrix;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.modelImporter.ModelImporterFactory;
import org.mt4j.util.opengl.GLMaterial;

import processing.core.PApplet;

import util.MathUtil;
import util.Mode;
import util.ModelInfo;
import widgets.MT3DObject;

/**
 * <strong>Caution:</strong> This class doesn't take care of errors 
 * made by the user, that is, erroneous 3d-models can lead to undefined behaviour.
 * 
 * TODO:
 * 	 - the rotate()-Function should be tested
 *   - the getPosition() Method shall be tested/debugged
 *   - the rotateX(), rotate()Y, rotate()Z methods can be made better..
 * 
 * @author langenhagen
 * @version 20120529
 */
public class StdMT3DObject extends MT3DObject {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the number of the current active variant */
	private int variant;
	/** the path to the model's folder */
	private String path;
	/** all meshes of the 3D-Model */
	private MTTriangleMesh[] meshes;
	/** carries all meshes of all meshGroups */
	private List<MTComponent> meshGroups;
	/** carries references to the biggest mesh of each variant */
	private List<MTTriangleMesh> biggestMeshes;
	/** stores the crease angle, and other informations for all meshGroups of this model */
	private ModelInfo modelInfo;
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main Constructor #1.
	 * @param pApp
	 * The <i>PApplet</i> you are working with.
	 * @param path
	 * the absolute path of the model folder.
	 * @param variant
	 * The variant of the model as an <i>int</i>, where 0 is default.
	 * @param pos
	 * The initial position of this 3D-Model as a <i>Vector3D</i>.
	 * @param scale
	 * The initial scaling-factor.
	 */
	public StdMT3DObject( PApplet pApp, String path, int variant, Vector3D pos, float scale){
		super( pApp);

		// get path of the model
		path = path.replace( '\\', '/');
		if( path.charAt( path.length()-1) == '/')
			path = path.substring( 0, path.length());
		this.path = path;

		// read additional model info, set variant & load the model variants
		modelInfo = new ModelInfo( path);
		loadVariants();
		
		setVariant( variant);
		
		// pick this component instead of its children
		setComposite(true);
		
		// position & scaling
		translateGlobal(pos.subtractLocal( biggestMeshes.get( variant).getCenterPointLocal()));
		scale( scale, scale, scale, biggestMeshes.get( variant).getCenterPointLocal(), TransformSpace.LOCAL);
	}

	/**
	 * Constructor #2<br>
	 * Opens automatically the 0-variant of this model.
	 * More simple constructor to ensure better working quality while using other methods
	 * for positioning and scaling. If wanted, it scales this entity of a 3D-object 
	 * to the unit in GLOBAL coordinates.
	 * @param pApplet
	 * The <i>PApplet</i> you are working with.
	 * @param path
	 * the absolute path of the model folder.
	 * @param scaleToUnit
	 * wether or not to scale this entity of a 3D-object
	 * to the unit in GLOBAL coordinates.
	 */
	public StdMT3DObject( PApplet pApplet, String path, boolean scaleToUnit){
		this( pApplet, path, 0, new Vector3D(), 1);
		
		if(scaleToUnit)
			scaleToUnit( 1);
	}
	
	/**
	 * Constructor #3<br>
	 * More simple constructor to ensure better working quality while using other methods
	 * for positioning and scaling. Scales this entity of a 3D-object 
	 * to the unit in GLOBAL coordinates.
	 * Opens automatically the 0-variant of this model.
	 * @param pApplet
	 * The <i>PApplet</i> you are working with.
	 * @param path
	 * the absolute path of the model folder.
	 */
	public StdMT3DObject( PApplet pApplet, String path){
		this( pApplet, path, true);		
	}
	
	/**
	 * Constructor #4<br>
	 * Copyconstructor.
	 * @param obj
	 */
	public StdMT3DObject( StdMT3DObject obj){		
		
		super( obj.getRenderer());
				
		// read additional model info, set variant & load the model variants
		modelInfo = obj.getModelInfo();
		String fullpath = obj.getFullPath( 0);
		path = fullpath.substring( 0, fullpath.lastIndexOf( "/"));
		
		loadVariants(); // in fact, THAT makes this constructor no real copyconstructor.
		
		setVariant( 0);
		
		// pick this component instead of its children
		setComposite(true);
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/* (non-Javadoc)
	 * @see widgets.MT3DObject#invertNormals()
	 */
	@Override
	public void invertNormals(){
		for (MTTriangleMesh mesh : meshes){
    		Vector3D[] normals = mesh.getGeometryInfo().getNormals();
    		for (Vector3D normal : normals)
            	normal.scaleLocal(-1);

            // set normals again
            mesh.getGeometryInfo().setNormals( normals, mesh.isUseDirectGL(), mesh.isUseVBOs());           
        }
	}
	
	/* (non-Javadoc)
	 * @see widgets.MT3DObject#getMeshGroup()
	 */
	@Override
	public MTComponent getMeshGroup(){
		return meshGroups.get( getVariant());
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#getBiggestMesh()
	 */
	@Override
	public MTTriangleMesh getBiggestMesh(){
		return biggestMeshes.get( getVariant());
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#scaleToUnit(float)
	 */
	@Override
	public float scaleToUnit(float multiplicator){
		// XXX could be better
		float width = getBiggestMesh().getWidthXY( TransformSpace.GLOBAL);
		float height = getBiggestMesh().getHeightXY( TransformSpace.GLOBAL);
		float factor = width>height ? multiplicator/width : multiplicator/height;
		
		scaleGlobal( factor, factor, factor, getPosition());
		return factor;
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#setMaterial(org.mt4j.util.opengl.GLMaterial)
	 */
	@Override
	public void setMaterial(GLMaterial material){
		for( MTTriangleMesh mesh : meshes)
			mesh.setMaterial( material);
	}
	
	/* (non-Javadoc)
	 * @see widgets.MT3DObject#getMaterial()
	 */
	@Override
	public List<GLMaterial> getMaterials(){
		List<GLMaterial> ret = new LinkedList<GLMaterial>();
		for( MTTriangleMesh mesh : meshes)
			ret.add( mesh.getMaterial());
		return ret;
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#setVariant(int)
	 */
	@Override
	public boolean setVariant(int num){
		if( num<0 || num >= getNumOfVariants())
			return false;
		
		meshGroups.get( getVariant()).setVisible( false);
		meshGroups.get( num).setVisible( true);

		variant = num;
		return true;
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#getVariant()
	 */
	@Override
	public int getVariant(){
		return variant;
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#getNumOfVariants()
	 */
	@Override
	public int getNumOfVariants(){
		int i = 0;
		while( getFullPath( i) != null)
			i++;
		return i;
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#getPosition(org.mt4j.components.TransformSpace)
	 */
	@Override
	public Vector3D getPosition(TransformSpace space){
		//TODO: Maybe some debugging here necessary
		
		switch(space){
		case LOCAL:
			return getBiggestMesh().getCenterPointLocal();
		case RELATIVE_TO_PARENT:
			return getBiggestMesh().getCenterPointRelativeToParent();
		case GLOBAL:
			return getBiggestMesh().getCenterPointGlobal().getAdded( getBiggestMesh().getCenterPointLocal() ); // seems correct
		default:
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setPosition(org.mt4j.util.math.Vector3D, org.mt4j.components.TransformSpace)
	 */
	@Override
	public void setPosition(Vector3D pos, TransformSpace space){
		
		Vector3D now = null;
		Vector3D target = null;
		
		switch(space){
		case LOCAL:
			target = pos.subtractLocal( getBiggestMesh().getCenterPointLocal());
			break;
		case RELATIVE_TO_PARENT:
			now = getBiggestMesh().getCenterPointRelativeToParent();
			target = pos.subtractLocal( getBiggestMesh().getCenterPointLocal().subtractLocal( now));
			break;
		case GLOBAL:
			now = getBiggestMesh().getCenterPointGlobal();
			target = pos.subtractLocal( getBiggestMesh().getCenterPointLocal()).subtractLocal( now);
			break;
		}
		translate( target, space);
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#getWidth(org.mt4j.components.TransformSpace)
	 */
	@Override
	public float getWidth(TransformSpace space){
		return getBiggestMesh().getWidthXY( space);
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#getHeight(org.mt4j.components.TransformSpace)
	 */
	@Override
	public float getHeight(TransformSpace space){
		return getBiggestMesh().getHeightXY( space);
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setWidth(float, org.mt4j.components.TransformSpace)
	 */
	@Override
	public void setWidth(float width, TransformSpace space){
		float factor = width/getBiggestMesh().getWidthXY( space);	
		this.scale( factor, factor, factor, getPosition( space), space);
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setHeight(float, org.mt4j.components.TransformSpace)
	 */
	@Override
	public void setHeight(float height, TransformSpace space){
		float factor = height/getBiggestMesh().getHeightXY( space);	
		this.scale( factor, factor, factor, getPosition( space), space);
	}
	
	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#rotateX(org.mt4j.util.math.Vector3D, float, org.mt4j.components.TransformSpace)
	 */
	@Override
	public void rotate(Vector3D axis, float degree, TransformSpace space) {
		
		// TODO: take transform space into account
		
		Vector3D pos = this.getPosition();
		
		Matrix posm = new Matrix(	// translation matrix for rotation around the center point
				1,	0,	0,	pos.getX(),
				0,	1,	0,	pos.getY(),
				0,	0,	1,	pos.getZ(),
				0,	0,	0,	1);
		Matrix nposm = new Matrix( // back-translation matrix for rotation around the center point
				1,	0,	0,	-pos.getX(),
				0,	1,	0,	-pos.getY(),
				0,	0,	1,	-pos.getZ(),
				0,	0,	0,	1);
		
		// do the transformation
		Matrix m = posm.mult( MathUtil.createRotationMatrix(axis, degree).mult(  nposm));
		this.transform( m);
	}
	
	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#rotateX(float, org.mt4j.components.TransformSpace)
	 */
	@Override
	public void rotateX(float degree, TransformSpace space){
		// TODO: take the TransformSpace into account
		this.rotateX( getPosition(), degree);
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#rotateY(float, org.mt4j.components.TransformSpace)
	 */
	@Override
	public void rotateY(float degree, TransformSpace space){
		// TODO: take the TransformSpace into account
		this.rotateY( getPosition(), degree);
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#rotateZ(float, org.mt4j.components.TransformSpace)
	 */
	@Override
	public void rotateZ(float degree, TransformSpace space){
		// TODO: take the TransformSpace into account
		this.rotateZ( getPosition(), degree);
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setMode(util.Mode)
	 */
	@Override
	public void setMode(Mode mode){
		// just don't know what to do whaat to do.. dam dam.
		switch(mode){
		case NORMAL:
			break;
		case TAP:
			break;
		case DISABLED:
			break;
		case SIGNAL:
			break;
		case SIGNAL2:
			break;
		}
	}
	
	/**
	 * Retrieves the modelInfo-object which stores additional information to this model.
	 * @return
	 * An Object of type <i>ModelInfo</i>
	 */
	public ModelInfo getModelInfo(){
		return modelInfo;
	}
	
	/* (non-Javadoc)
	 * @see widgets.MT3DObject#getModelName()
	 */
	@Override
	public String getModelName(){
		return path.substring( path.lastIndexOf( '/') + 1);
	}
	
	/**
	 * Sets the light.<br>
	 * <strong>NOTE:</strong>Only supported when using OpenGL as the renderer!
	 * @param light the new light
	 */
	@Override
	public void setLight(MTLight light){
		super.setLight( light);
		meshGroups.get( getVariant()).setLight( light);
	}
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the full path to the specified variant's obj-model
	 * @param num
	 * The variant as an <i>int</i>
	 * @return
	 * Returns the full qualified path as a <i>String</i> or <i>null</i> in case of error.
	 */
	public String getFullPath( int num){
		String name = "";
		String fullpath = path + "/";
		name = num < 100 ? name ="0" : name;
		name = num < 100 ? name += "0" : name;
		name += num;
		String filename = fullpath + name + ".obj";
		
		if( new File( filename).exists())
			return filename;
		
		return null;
	}
	
	/**
	 * Loads all meshGroups and saves them into the <i>meshGroups</i>-<i>List</i>.
	 */
	private void loadVariants(){
		int numOfVariants = getNumOfVariants();
		meshGroups = new LinkedList<MTComponent>();
		biggestMeshes = new LinkedList<MTTriangleMesh>();

		for(int i=0; i<numOfVariants; i++){				// for every obj-File...
			MTComponent meshGroup = new MTComponent( getRenderer());
			meshGroups.add( meshGroup);
			this.addChild( meshGroup);
			meshes = ModelImporterFactory.loadModel(
					this.getRenderer(),					// the App
					this.getFullPath( i),				// the File
					modelInfo.getCreaseAngle( i),		// Sharpness of the Edges
					modelInfo.getFlipTextureY( i),		// wether to flip the Texture at the y-Axis
					modelInfo.getFlipTextureX( i));		// wether to flip the Texture at the x-Axis

			// add meshes to the meshGroup & find biggest mesh
			MTTriangleMesh biggestMesh = meshes[0];
			for( MTTriangleMesh mesh : meshes){
				//clear previously registered input processors
				mesh.unregisterAllInputProcessors();
				mesh.removeAllGestureEventListeners();
				mesh.setPickable(true);
				
				meshGroup.addChild(mesh);
				
				// get biggest mesh
				if( mesh.getWidthXY( TransformSpace.GLOBAL) > biggestMesh.getWidthXY( TransformSpace.GLOBAL))
					biggestMesh = mesh;

				// invert axis vectors if said so
				if (modelInfo.getInvertNormals( i)){
					Vector3D[] normals = mesh.getGeometryInfo().getNormals();
					for (Vector3D normalVec : normals)
						normalVec.scaleLocal(-1);
					
					mesh.getGeometryInfo().setNormals(normals, mesh.isUseDirectGL(), mesh.isUseVBOs());
				}
				
				// If a mesh's got more than 20 vertices, use display lists for better runtime
				if (mesh.getVertexCount() > 20)
					mesh.generateAndUseDisplayLists();
			} // END for every mesh
			// pick this component instead of its children
			biggestMeshes.add( biggestMesh);
			meshGroup.setComposite( true);
			meshGroup.setVisible( false);
		} // END for every obj file
	}

	
}
package widgets;

import java.util.LinkedList;
import java.util.List;

import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.mesh.MTTriangleMesh;
import org.mt4j.util.math.Matrix;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.modelImporter.ModelImporterFactory;
import org.mt4j.util.opengl.GLMaterial;
import org.sercho.masp.models.Context.Area;
import org.sercho.masp.models.Context.Door;
import org.sercho.masp.models.Context.Environment;
import org.sercho.masp.models.Context.Place;
import org.sercho.masp.models.Context.Window;

import processing.core.PApplet;

import util.MathUtil;
import util.Mode;

/**
 * This class generates a 3D object of a home from a context model xml file.
 * 
 * @author langenhagen
 * @version 20120707
 */
public class MTContextModel3DObject extends MT3DObject {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the name of the model */
	private String name;
	/** the corresponding environment */
	private Environment environment;
	
	/** the number of possible variants */
	private int numOfVariants;
	/** the number of the current active variant */
	private int variant;
	/** carries all meshes of all meshGroups */
	private MTComponent meshGroup;
	/** carries references to the biggest mesh of each variant */
	private MTTriangleMesh biggestMesh;

	/** height of the ground */
	private final float groundHeight = 0.1f;
	/** width of the walls */
	private final float wallHeight= 2.5f;
	/** width of the walls */
	private final float wallWidth = 0.1f;
	/** width doors & windows */
	private final float doorDepth = wallWidth * 2.05f;
	
	/** scales the dimensions of the positions and spans of the items. Feel free to rename */
	private final float schwurbelMultiplikator = 0.01f;

	/** the type of the mesh-child MeshType */
	List<MeshType> meshTypeList = new LinkedList<MeshType>();
	
	/** ModelInfo XXX stores the crease angle, and other informations for all meshGroups of this model */

	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * Constructor #1.
	 * @param pApplet
	 * The <i>PApplet</i> you are working with.
	 * @param environment
	 * The environment from which to generate the 3d object from.
	 * @param pos
	 * The initial position of this 3D-Model as a <i>Vector3D</i>.
	 * @param scale
	 * The initial scaling-factor.
	 */
	public MTContextModel3DObject(PApplet pApplet, Environment environment, Vector3D pos, float scale) {
		super(pApplet);
		
		//loadVariants		
		
		name = environment.getName();
		this.environment = environment;
		
		loadModel( environment);
		
		// pick this component instead of its children
		setComposite(true);
		
		// position & scaling
		translateGlobal(pos.subtractLocal( biggestMesh.getCenterPointLocal()));
		scale( scale, scale, scale, biggestMesh.getCenterPointLocal(), TransformSpace.LOCAL);
	}
	
	/**
	 * Constructor #2<br>
	 * More simple constructor to ensure better working quality while using other methods
	 * for positioning and scaling. If wanted, it scales this entity of a 3D-object 
	 * to the unit in GLOBAL coordinates.
	 * @param pApplet
	 * The <i>PApplet</i> you are working with.
	 * @param environment
	 * The environment from which to generate the 3d object from.
	 * @param scaleToUnit
	 * wether or not to scale this entity of a 3D-object 
	 * to the unit in GLOBAL coordinates.
	 */
	public MTContextModel3DObject( PApplet pApplet, Environment environment, boolean scaleToUnit){
		
		this( pApplet, environment, new Vector3D(), 1);
		
		if(scaleToUnit)
			scaleToUnit( 1);
	}
	
	/**
	 * Constructor #3<br>
	 * More simple constructor to ensure better working quality while using other methods
	 * for positioning and scaling. Scales this entity of a 3D-object 
	 * to the unit in GLOBAL coordinates.
	 * @param pApplet
	 * The <i>PApplet</i> you are working with
	 * @param path
	 * the absolute path of the model folder
	 */
	public MTContextModel3DObject( PApplet pApplet, Environment environment){
		this( pApplet, environment, true);		
	}

	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
		
	
	/* (non-Javadoc)
	 * @see widgets.MT3DObject#invertNormals()
	 */
	@Override
	public void invertNormals() {
		/* empty implementation */
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#getMeshGroup()
	 */
	@Override
	public MTComponent getMeshGroup() {
		return meshGroup;
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#getBiggestMesh()
	 */
	@Override
	public MTTriangleMesh getBiggestMesh() {
		return biggestMesh;
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#scaleToUnit(float)
	 */
	@Override
	public float scaleToUnit(float multiplicator) {
		// XXX could be better
		float width = getBiggestMesh().getWidthXY( TransformSpace.GLOBAL);
		float height = getBiggestMesh().getHeightXY( TransformSpace.GLOBAL);
		float factor = width>height ? multiplicator/width : multiplicator/height;
		
		scaleGlobal( factor, -factor, factor, getPosition());
		return factor;
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#invertNormals()
	 */
	@Override
	public void setMaterial(GLMaterial material) {
		for( MTTriangleMesh mesh :  (MTTriangleMesh[])meshGroup.getChildren() )
			mesh.setMaterial( material);
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#getMaterials()
	 */
	@Override
	public List<GLMaterial> getMaterials() {
		
		List<GLMaterial> ret = new LinkedList<GLMaterial>();
		for( MTTriangleMesh mesh : (MTTriangleMesh[])meshGroup.getChildren() )
			ret.add( mesh.getMaterial());
		return ret;
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#setVariant(int)
	 */
	@Override
	public boolean setVariant(int num) {
		// TODO maybe activate/deactivate areas/places 
		// with help of the bit-array of the "num" paramenter
		return false;
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#getVariant()
	 */
	@Override
	public int getVariant() {
		
		return variant;
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#getNumOfVariants()
	 */
	@Override
	public int getNumOfVariants() {
		return numOfVariants;
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#getModelName()
	 */
	@Override
	public String getModelName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#rotate(org.mt4j.util.math.Vector3D,float, org.mt4j.components.TransformSpace)
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
	 * @see widgets.MT3DObject#rotateX(float,org.mt4j.components.TransformSpace)
	 */
	@Override
	public void rotateX(float degree, TransformSpace space) {
		// TODO: take the TransformSpace into account
		this.rotateX( getPosition(), degree);
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#rotateY(float,org.mt4j.components.TransformSpace)
	 */
	@Override
	public void rotateY(float degree, TransformSpace space) {
		// TODO: take the TransformSpace into account
		this.rotateY( getPosition(), degree);
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#rotateZ(float,org.mt4j.components.TransformSpace)
	 */
	@Override
	public void rotateZ(float degree, TransformSpace space) {
		// TODO: take the TransformSpace into account
		this.rotateZ( getPosition(), degree);
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#getPosition(org.mt4j.components.TransformSpace)
	 */
	@Override
	public Vector3D getPosition(TransformSpace space) {

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
	 * @see widgets.MT3DObject#setPosition(org.mt4j.util.math.Vector3D,org.mt4j.components.TransformSpace)
	 */
	@Override
	public void setPosition(Vector3D pos, TransformSpace space) {
		
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
	 * @see widgets.MT3DObject#getWidth(org.mt4j.components.TransformSpace)
	 */
	@Override
	public float getWidth(TransformSpace space) {
		return getBiggestMesh().getWidthXY( space);
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#getHeight(org.mt4j.components.TransformSpace)
	 */
	@Override
	public float getHeight(TransformSpace space) {
		return getBiggestMesh().getHeightXY( space);
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#setWidth(float,org.mt4j.components.TransformSpace)
	 */
	@Override
	public void setWidth(float width, TransformSpace space) {
		float factor = width/getBiggestMesh().getWidthXY( space);	
		this.scale( factor, factor, factor, getPosition( space), space);
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#setHeight(float,org.mt4j.components.TransformSpace)
	 */
	@Override
	public void setHeight(float height, TransformSpace space) {
		float factor = height/getBiggestMesh().getHeightXY( space);	
		this.scale( factor, factor, factor, getPosition( space), space);
	}

	/* (non-Javadoc)
	 * @see widgets.MT3DObject#setMode(util.Mode)
	 */
	@Override
	public void setMode(Mode mode) {
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
	
	
	
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////

	/**
	 * Loads the meshgroup and saves them into the <i>meshGroup</i>.
	 */
	private void loadModel( Environment environment ){
		
		meshGroup = new MTComponent( this.getRenderer());
		LinkedList<MTTriangleMesh> meshes = new LinkedList<MTTriangleMesh>();
		
		// create meshes for every area
		for( Place p : environment.getPlaces()){
			for( Area a : p.getAreas()){
		

				
				/* The single walls and the ground are combined of simple 
				 * cuboids that are ordered in the following manner:
				 * 
				 *       v7__________v8
				 *        /|        /|
				 *       /         / |
				 *    v5/__|______/v6|
				 *      |   _ _ _ | _|
				 *      |  /v3    |  /v4
				 *      |         | /
				 *      |/________|/
				 *     v1          v2
				 *
				 */
				
				float aox = -(float)a.getOrigin().getX() * schwurbelMultiplikator;
				float aoy = (float)a.getOrigin().getZ() * schwurbelMultiplikator;
				float aoz = (float)a.getOrigin().getY() * schwurbelMultiplikator;
				float asx = -(float)a.getSpan().getX() * schwurbelMultiplikator;
				float asy = wallHeight;
				//float asy = (float)a.getSpan().getZ() * schwurbelMultiplikator;
				float asz = (float)a.getSpan().getY() * schwurbelMultiplikator;
				float agy = aoy+groundHeight;
			
								
				// ground mesh //
				MTTriangleMesh ground = loadMesh("./cubes/groundCube.obj");
				ground.scaleGlobal( asx, groundHeight, asz, Vector3D.ZERO_VECTOR);
				ground.translateGlobal( new Vector3D( aox, aoy, aoz));
				meshTypeList.add( MeshType.Ground);
				meshes.add( ground);
				
				// Wall #1 mesh //
				MTTriangleMesh wall1 = loadMesh("./cubes/wallCube.obj");
				wall1.scaleGlobal( asx, asy, wallWidth, Vector3D.ZERO_VECTOR);
				wall1.translateGlobal( new Vector3D( aox, agy, aoz));
				meshTypeList.add( MeshType.Wall);
				meshes.add( wall1);
				
				// Wall #2 mesh //
				MTTriangleMesh wall2 = loadMesh("./cubes/wallCube.obj");
				wall2.scaleGlobal( asx, asy, wallWidth, Vector3D.ZERO_VECTOR);
				wall2.translateGlobal( new Vector3D( aox, agy, aoz+asz-wallWidth));
				meshTypeList.add( MeshType.Wall);
				meshes.add( wall2);
				
				// Wall #3 mesh //
				MTTriangleMesh wall3 = loadMesh("./cubes/wallCube.obj");
				wall3.scaleGlobal( wallWidth, asy, asz-2*wallWidth, Vector3D.ZERO_VECTOR);
				wall3.translateGlobal( new Vector3D( aox-wallWidth, agy, aoz+wallWidth));
				meshTypeList.add( MeshType.Wall);
				meshes.add( wall3);
				
				// Wall #4 mesh //
				MTTriangleMesh wall4 = loadMesh("./cubes/wallCube.obj");
				wall4.scaleGlobal( wallWidth, asy, asz-2*wallWidth, Vector3D.ZERO_VECTOR);
				wall4.translateGlobal( new Vector3D( aox+asx,agy, aoz+wallWidth));
				meshTypeList.add( MeshType.Wall);
				meshes.add( wall4);	
				
			} // END for place.areas
			for( Door d : p.getDoors()){
				
				// doors //
				
				MTTriangleMesh door = loadMesh("./cubes/doorCube.obj");
				
				float dpx = -(float)d.getPosition().getX() * schwurbelMultiplikator;
				float dpy = (float)d.getPosition().getZ() * schwurbelMultiplikator;
				float dpz = (float)d.getPosition().getY() * schwurbelMultiplikator;
				float dsx = -(float)d.getSpan().getX() * schwurbelMultiplikator;
				float dsy = (float)d.getSpan().getZ() * schwurbelMultiplikator;
				float dsz = (float)d.getSpan().getY() * schwurbelMultiplikator;
				
				door.scaleGlobal(
						Math.min( dsx, doorDepth),
						Math.max( dsy, doorDepth),
						Math.max( dsz, doorDepth),
						Vector3D.ZERO_VECTOR
						);
				
				float translateX = dsx < doorDepth ? dpx : dpx - doorDepth/2;
				float translateY = dsy > doorDepth ? dpy : dpy - doorDepth/2;
				float translateZ = dsz > doorDepth ? dpz : dpz - doorDepth/2;
											
				door.translateGlobal( new Vector3D(translateX, translateY, translateZ));				
				meshTypeList.add( MeshType.Door);
				meshes.add( door);
			}
			for( Window w : p.getWindows() ){
				
				// windows //

				MTTriangleMesh window = loadMesh("./cubes/windowCube.obj");
				
				float wpx = -(float)w.getPosition().getX() * schwurbelMultiplikator;
				float wpy = (float)w.getPosition().getZ() * schwurbelMultiplikator;
				float wpz = (float)w.getPosition().getY() * schwurbelMultiplikator;
				
				//FIXME
				float wsx = -(float)w.getSpan().getX() * schwurbelMultiplikator;
				float wsy = (float)w.getSpan().getZ() * schwurbelMultiplikator;
				float wsz = (float)w.getSpan().getY() * schwurbelMultiplikator;				
				window.scaleGlobal(
						Math.min( wsx, doorDepth),
						Math.max( wsy, doorDepth),
						Math.max( wsz, doorDepth),
						Vector3D.ZERO_VECTOR
						);
				
				float translateX = wsx < doorDepth ? wpx : wpx - doorDepth/2;
				float translateY = wsy > doorDepth ? wpy : wpy - doorDepth/2;
				float translateZ = wsz > doorDepth ? wpz : wpz - doorDepth/2;
										
				window.translateGlobal( new Vector3D(translateX, translateY, translateZ));	/**/				
				meshTypeList.add( MeshType.Window);
				meshes.add( window);
			}
			
		} // END for all places
		
		
		// add meshes to the meshGroup & find biggest mesh
		MTTriangleMesh biggestMesh = meshes.get( 0);
		for( MTTriangleMesh mesh : meshes){
			
			//clear previously registered input processors
			
			mesh.unregisterAllInputProcessors();
			mesh.removeAllGestureEventListeners();
			mesh.setPickable(true);
			
			meshGroup.addChild(mesh);

			// get biggest mesh
			if( mesh.getWidthXY( TransformSpace.GLOBAL) > biggestMesh.getWidthXY( TransformSpace.GLOBAL))
				biggestMesh = mesh;

			
			// If a mesh's got more than 20 vertices, use display lists for better runtime
			if (mesh.getVertexCount() > 20)
				mesh.generateAndUseDisplayLists();
		} // END for every mesh
		// pick this component instead of its children
		meshGroup.setComposite( true);
		numOfVariants = 1; // XXX !!!
		this.biggestMesh = biggestMesh;
		this.addChild( meshGroup);
	}

	
	/**
	 * Loads the correspinding triangle mesh.
	 * @param
	 * The path of the mesh file as a <i>String</i>. Usually a obj file.
	 * @return
	 * The corresponding <i>MTTriangleMesh</i>.
	 */
	private MTTriangleMesh loadMesh(String path){
		
		return ModelImporterFactory.loadModel(
				this.getRenderer(),		// the App
				path,					// the File
				0,						// Sharpness of the Edges
				false,					// wether to flip the Texture at the y-Axis
				false)[0];				// wether to flip the Texture at the x-Axis
	}
	
	// INNER CLASSES //////////////////////////////////////////////////////////////////////////////
	
	private enum MeshType{
		Ground,
		Wall,
		Door,
		Window
	}
	
	
}

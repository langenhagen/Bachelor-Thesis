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
 * @version 20120529
 */
public class MTContextModel3DObject extends MT3DObject {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the name of the model */
	private String name;
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
	private final float wallHeight= 05.5f;
	/** width of the walls */
	private final float wallWidth = 0.1f;
	/** width doors & windows */
	private final float doorWidth = wallWidth * 2.05f;
	
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
		
		System.out.println("NUMOFPLACES :" + environment.getPlaces().size());
		int stubbl = 0;
		// create meshes for every area
		for( Place p : environment.getPlaces()){
			System.out.println("PLACE: NUMOFAREAS :" + p.getAreas().size());
			for( Area a : p.getAreas()){
		
				//if(stubbl>0)
				//	break;
				
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
				System.out.println("A: " + a.getOrigin() + " : " + a.getSpan() );
				
				float ox = (float)a.getOrigin().getX()/100;
				float oy = (float)a.getOrigin().getY()/100;
				float oz = (float)a.getOrigin().getZ()/100;
				float sx = (float)a.getSpan().getX()/100;
				float sy = (float)a.getSpan().getY()/100;
				float sz = (float)a.getSpan().getZ()/100;
				float gy = oy+groundHeight;
				
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println("ox: " + ox);
				System.out.println("oy: " + oy);
				System.out.println("oz: " + oz);
				System.out.println("Sx: " + sx);
				System.out.println("Sy: " + sy);
				System.out.println("Sz: " + sz);
				
				System.out.println();
				System.out.println();
				System.out.println();
								
				// ground mesh //
				MTTriangleMesh ground = loadGroundCube();
				ground.scaleGlobal( sx, groundHeight, sz, Vector3D.ZERO_VECTOR);
				ground.translateGlobal( new Vector3D( ox, oy, oz));
				meshes.add( ground);
				
				// Wall #1 mesh //
				MTTriangleMesh wall1 = loadWallCube();
				wall1.scaleGlobal( sx, wallHeight, wallWidth, Vector3D.ZERO_VECTOR);
				wall1.translateGlobal( new Vector3D( ox, gy, oz));
				meshes.add( wall1);
				
				// Wall #2 mesh //
				MTTriangleMesh wall2 = loadWallCube();
				wall2.scaleGlobal( sx, wallHeight, wallWidth, Vector3D.ZERO_VECTOR);
				wall2.translateGlobal( new Vector3D( ox, gy, oz+sz-wallWidth));
				meshes.add( wall2);
				
				// Wall #3 mesh //
				MTTriangleMesh wall3 = loadWallCube();
				wall3.scaleGlobal( wallWidth, wallHeight, sz-2*wallWidth, Vector3D.ZERO_VECTOR);
				wall3.translateGlobal( new Vector3D( ox, gy, oz+wallWidth));
				meshes.add( wall3);
				
				// Wall #4 mesh //
				MTTriangleMesh wall4 = loadWallCube();
				wall4.scaleGlobal( wallWidth, wallHeight, sz-2*wallWidth, Vector3D.ZERO_VECTOR);
				wall4.translateGlobal( new Vector3D( ox+sx-wallWidth,gy, oz+wallWidth));
				meshes.add( wall4);	
				
				stubbl++;
				
			} // END for place.areas
			
			// doors //
			for( Door d : p.getDoors()){
				MTTriangleMesh door = loadDoorCube();
				
				door.scaleGlobal(
						Math.max((float)d.getSpan().getX()/100, doorWidth),
						Math.max((float)d.getSpan().getY()/100, doorWidth),
						Math.max((float)d.getSpan().getZ()/100, doorWidth),
						Vector3D.ZERO_VECTOR
						);
				
				float translateX =
					d.getSpan().getX()/100 > doorWidth ? 
							(float)d.getPosition().getX()/100 : 
								(float)d.getPosition().getX()/100 - doorWidth/2;
				float translateY =
					d.getSpan().getY()/100 > doorWidth ? 
							(float)d.getPosition().getY()/100 : 
								(float)d.getPosition().getY()/100 - doorWidth/2;

				float translateZ =
					d.getSpan().getZ()/100 > doorWidth ? 
							(float)d.getPosition().getZ()/100 : 
								(float)d.getPosition().getZ()/100 - doorWidth/2;
							
				door.translateGlobal( new Vector3D(translateX, translateY, translateZ));
								
				meshes.add( door);
			}
			for( Window w : p.getWindows() ){

				MTTriangleMesh window = loadWindowCube();
				
				// TODO: get a better model
				System.out.println("WWDOW " + w.getPosition() + "\n" + w.getSpan());
			/*	window.scaleGlobal(
						Math.max((float)w.getSpan().getX()/100, doorWidth),
						Math.max((float)w.getSpan().getY()/100, doorWidth),
						Math.max((float)w.getSpan().getZ()/100, doorWidth),
						Vector3D.ZERO_VECTOR
						);
				
				float translateX =
					w.getSpan().getX()/100 > doorWidth ? 
							(float)w.getPosition().getX()/100 : 
								(float)w.getPosition().getX()/100 - doorWidth/2;
				float translateY =
					w.getSpan().getY()/100 > doorWidth ? 
							(float)w.getPosition().getY()/100 : 
								(float)w.getPosition().getY()/100 - doorWidth/2;
				float translateZ =
					w.getSpan().getZ()/100 > doorWidth ? 
							(float)w.getPosition().getZ()/100 : 
								(float)w.getPosition().getZ()/100 - doorWidth/2;
							
				window.translateGlobal( new Vector3D(translateX, translateY, translateZ));
					*/
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

	private MTTriangleMesh loadWallCube(){
		
		return ModelImporterFactory.loadModel(
				this.getRenderer(),					// the App
				"./cubes/wallCube.obj",				// the File
				0,		// Sharpness of the Edges
				false,		// wether to flip the Texture at the y-Axis
				false)[0];		// wether to flip the Texture at the x-Axis
	}
	
	private MTTriangleMesh loadGroundCube(){
		
		return ModelImporterFactory.loadModel(
				this.getRenderer(),					// the App
				"./cubes/groundCube.obj",				// the File
				0,		// Sharpness of the Edges
				false,		// wether to flip the Texture at the y-Axis
				false)[0];		// wether to flip the Texture at the x-Axis
	}
	
	private MTTriangleMesh loadDoorCube(){
		
		return ModelImporterFactory.loadModel(
				this.getRenderer(),					// the App
				"./cubes/doorCube.obj",				// the File
				0,		// Sharpness of the Edges
				false,		// wether to flip the Texture at the y-Axis
				false)[0];		// wether to flip the Texture at the x-Axis
	}

	private MTTriangleMesh loadWindowCube(){
		
		return ModelImporterFactory.loadModel(
			this.getRenderer(),					// the App
			"./cubes/windowCube.obj",				// the File
			0,		// Sharpness of the Edges
			false,		// wether to flip the Texture at the y-Axis
			false)[0];		// wether to flip the Texture at the x-Axis
	}
	
}

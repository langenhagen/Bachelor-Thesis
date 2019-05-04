package widgets.factories;

import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.componentProcessors.AbstractComponentProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;
import org.mt4jx.components.visibleComponents.widgets.MTSuggestionTextArea;

import processing.core.PApplet;
import provider.GUIFonts;
import provider.GUITextures;
import util.ProviderTarget;
import widgets.MT2DManipulator;
import widgets.MT3DObject;
import widgets.MTButton;
import widgets.MTCheckButton;
import widgets.MTChoiceContainer;
import widgets.MTGraphPlotter;
import widgets.MTSlider;
import widgets.std.StdMT2DManipulator;
import widgets.std.StdMT3DObject;
import widgets.std.StdMTButton;
import widgets.std.StdMTCheckButton;
import widgets.std.StdMTChoiceContainer;
import widgets.std.StdMTGraphPlotter;
import widgets.std.StdMTSlider;

/**
 * This class hands you a factory which can create GUI-components.
 * Although there is no obligation to use this class to create components,
 * this should be the preferred way because this factory takes care of 
 * style integrity and other comforts.
 *  
 * @author langenhagen
 * @version 20110702
 */
public class GUIFactory {
	
	// CLASS VARS /////////////////////////////////////////////////////////////////////////////////
	
	/** singleton instance of this class */
	protected static GUIFactory instance;
	
	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the renderer of the gui-components */
	private PApplet pApp;
	/** indicates, whether a component should automatically be added to the current scene's scene graph */
	private boolean autoAddToScene = false;
	/** the current scene to add objects to */
	private AbstractScene currentScene;
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main constructor.
	 */
	protected GUIFactory(){
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Gets the singleton instance of this factory.
	 * @return
	 * The singleton <i>GUIFactory</i> instance.
	 */
	public static final GUIFactory instance(){
		return instance;
	}
	
	/**
	 * Sets up the factory according to a given Type of <i>GUIFactory</i>
	 * @param pApp
	 * The running application as an <i>PApplet</i>.
	 * @param clazz
	 * The <i>Class</i> <i>GUIFactory</i> or some <i>Class</i> which extends it.
	 */
	public final static void setup(PApplet pApp, Class<? extends GUIFactory> clazz){
		System.out.print("Initializing GUIFactory...\n");
		try{
			instance = clazz.newInstance();
		} catch(Exception e){
			System.err.println("Error: Could not instantiate object of class " + clazz.getName());
			e.printStackTrace();
		}
		instance.setupHelper( pApp);
		System.out.print("Done!\n");
	}
	
	/**
	 * Specifies, whether to set a component to create automatically to the scene.
	 * @param auto
	 * The indicator as a <i>boolean</i>. 
	 * TRUE means, set the components automatically to the scenegraph,
	 * FALSE means, no auto-adding will be done.
	 */
	public void setAutoAddToScene( boolean auto){
		autoAddToScene = auto;
	}
	
	/**
	 * Indicates, whether components are set to the scene graph automatically.
	 * @return
	 * TRUE indicates, that components will be added automatically,
	 * FALSE indicates that they don't.
	 */
	public boolean isAutoAddToScene(){
		return autoAddToScene;
	}
	
	/**
	 * Specifies the scene belonging to the canvas, new components will be attached to 
	 * if autoAddToScene is activated (see setAutoAddToScene()).
	 * @param scene
	 * The scene as an <i>AbstractScene</i>.
	 */
	public void setCurrentScene( AbstractScene scene){
		currentScene = scene;
	}
	
	/**
	 * Retrieves the current scene belonging to the scenegraph to which components 
	 * will be added automatically, is set so by setAutoAddToScene().
	 * @return
	 * A scene as an <i>AbstractScene</i>.
	 */
	public AbstractScene getCurrentScene(){
		return currentScene;
	}
	
	// PUBLIC FACTORY METHODS /////////////////////////////////////////////////////////////////////
	
	/**
	 * Creates a new <i>MT2DManipulator</i> and returns it.
	 * @param x
	 * The x-position of this component as a <i>float</i>.
	 * @param y
	 * The y-position of this component as a <i>float</i>.
	 * @return
	 * Returns a new <i>MT2DManipulator</i>.
	 */
	public MT2DManipulator create2DManipulator( float x, float y){
		MT2DManipulator ret = new StdMT2DManipulator( x, y, pApp);
		addToCanvas( ret);
		return ret;
	}
	
	/**
	 * Creates a new <i>MT2DManipulator</i> and returns it.
	 * @param name
	 * The name of the new component as a <i>String</i>.
	 * @param parent
	 * The parent of the new component as an <i>MTComponent</i> or NULL
	 * if you want to specify the parent later.
	 * @return
	 * Returns a new <i>MT2DManipulator</i>.
	 */
	public MT2DManipulator create2DManipulator( String name, MTComponent parent){
		MT2DManipulator ret = new StdMT2DManipulator(0,0,pApp);
		ret.setName( name);
		if(parent != null)
			parent.addChild( ret);
		else
			addToCanvas( ret);
		
		return ret;
	}
		
	/**
	 * Creates a new MTComponent which stores a grid-plane in 3-dimensional space.
	 * @param axis
	 * The axis of the plane-grid as a <i>Vector3D</i>, which doesn't have to be normalized.
	 * @param pos
	 * The center of ther grid as a <i>Vector3D</i>.
	 * @param color
	 * The color of the grid as an <i>MTColor</i>.
	 * @return
	 * The grid as an <i>MTComponent</i>.
	 */
	public MTComponent createGrid( Vector3D normal, Vector3D pos, MTColor color){
		int gridsize = 1000;		// the global size of the grid
		int linedistance = 100;		// the global distance of parallel neighbour-lines
		int middlestrokeweight = 4;	// the wheight of the middle stroke
		
		MTComponent container = new MTComponent( pApp);
		container.setPickable( false);
		container.unregisterAllInputProcessors();
		container.removeAllGestureEventListeners();
		
		normal = normal.normalizeLocal();
		Vector3D oldNormal = new Vector3D(0,0,1);
		float angle = oldNormal.angleBetween( normal);
		Vector3D rotationAxis = oldNormal.crossLocal( normal);
		
		// vertical lines
		for(int i= -gridsize/2; i<=gridsize/2; i+=linedistance){
			Vertex v1 = new Vertex(i, - gridsize/2);
			Vertex v2 = new Vertex(i, + gridsize/2);
			
			if( angle != 0)
			{
				v1.rotateAroundAxisLocal( rotationAxis, angle);
				v2.rotateAroundAxisLocal( rotationAxis, angle);
			}
			
			MTLine l = new MTLine( pApp, v1, v2);
			l.setStrokeColor( color);
			if( i == 0)
				l.setStrokeWeight( middlestrokeweight);
			
			l.setPickable( false);
			l.unregisterAllInputProcessors();
			l.removeAllGestureEventListeners();
			
			container.addChild( l);
		}
		// horizontal lines
		for(int i= -gridsize/2; i<=gridsize/2; i+=linedistance){
			Vertex v1 = new Vertex( -gridsize/2, i);
			Vertex v2 = new Vertex( gridsize/2, i);
			if( angle != 0)
			{
				v1.rotateAroundAxisLocal( rotationAxis, angle);
				v2.rotateAroundAxisLocal( rotationAxis, angle);
			}
			
			MTLine l = new MTLine( pApp, v1, v2);
			l.setStrokeColor( color);
			if( i == 0)
				l.setStrokeWeight( middlestrokeweight);
			
			l.setPickable( false);
			l.unregisterAllInputProcessors();
			l.removeAllGestureEventListeners();
			container.addChild( l);
		}
		container.translate( pos, TransformSpace.GLOBAL);
		
		addToCanvas( container);
		return container;
	}
	
	/**
	 * Creates three grids in along the planes which are
	 * clamped by the coordinate axes in global coordinate space.
	 * The grids have the colors red, green and blue.
	 * @param pos
	 * The center of all grids and their intersection point.
	 * @return
	 * The 3 grids as an <i>MTComponent</i>.
	 */
	public MTComponent create3DGrid( Vector3D pos){
		MTComponent container = new MTComponent( pApp);
		container.setPickable( false);
		container.unregisterAllInputProcessors();
		container.removeAllGestureEventListeners();
		
		MTColor red = new MTColor(255, 0, 0, 255);
		MTColor green = new MTColor( 0, 255, 0, 255);
		MTColor blue = new MTColor( 0, 0, 255, 255);
		
		container.addChild( GUIFactory.instance().createGrid( new Vector3D(1,0,0), pos, red));
		container.addChild( GUIFactory.instance().createGrid( new Vector3D(0,1,0), pos, green));
		container.addChild( GUIFactory.instance().createGrid( new Vector3D(0,0,1), pos, blue));

		addToCanvas( container);
		return container;
	}
	
	/**
	 * Creates a new <i>MT3DObject</i> and returns it.<br>
	 * It will be set to the 0-variant.
	 * <strong>Please note:<strong> If you want to create some
	 * common 3D-object, please use the <i>GUI3DModels</i>-provider!
	 * But for arbitrary objects, you can use this factory method.
	 * @param pos
	 * The position of this component as a <i>Vector3D</i>.
	 * @param path
	 * The path to the model as a <i>String</i>.
	 * @return
	 * Returns a new <i>MT3DObject</i>.
	 */
	public MT3DObject create3DObject( Vector3D pos, String path){
		MT3DObject ret = new StdMT3DObject( pApp, path, 0, pos, 200);
		ret.scaleGlobal( 1, -1, 1, ret.getPosition()); // just for convenience of a not upside down model
		addToCanvas( ret);
		return ret;
	}
	
	/**
	 * Creates a new <i>MTButton</i> and returns it.
	 * @param x
	 * The x-position of this component as a <i>float</i>.
	 * @param y
	 * The y-position of this component as a <i>float</i>.
	 * @param text
	 * The caption of this component as a <i>String</i>.
	 * @return
	 * Returns a new <i>MTButton</i>.
	 */
	public MTButton createButton( float x, float y, String text){
		MTButton ret = new StdMTButton( text, x, y, pApp);
		addToCanvas( ret);
		return ret;
	}
	
	/**
	 * Creates a new <i>MTButton</i> and returns it.
	 * @param text
	 * The caption of the button as a <i>String</i>.
	 * @param name
	 * The name of the new component as a <i>String</i>.
	 * @param parent
	 * The parent of the new component as an <i>MTComponent</i>
	 * or NULL if you want to specify the parent later.
	 * @return
	 * Returns a new <i>MTButton</i>.
	 */
	public MTButton createButton( String text, String name, MTComponent parent){
		MTButton ret = new StdMTButton( text, 0, 0, pApp);
		ret.setName( name);
		if(parent != null)
			parent.addChild( ret);
		else
			addToCanvas( ret);
		
		return ret;
	}
	
	/**
	 * Creates a new <i>MTCheckButton</i> and returns it.
	 * @param x
	 * The x-position of this component as a <i>float</i>.
	 * @param y
	 * The y-position of this component as a <i>float</i>.
	 * @param text
	 * The caption of this component as a <i>String</i>.
	 * @return
	 * Returns a new <i>MTCheckButton</i>.
	 */
	public MTCheckButton createCheckButton( float x, float y, String text){
		MTCheckButton ret = new StdMTCheckButton( text, x, y, pApp);
		addToCanvas( ret);
		return ret;
	}
	
	/**
	 * Creates a new <i>MTCheckButton</i> and returns it.
	 * @param text
	 * The caption of the checkbutton as a <i>String</i>.
	 * @param name
	 * The name of the new component as a <i>String</i>.
	 * @param parent
	 * The parent of the new component as an <i>MTComponent</i>
	 * or NULL if you want to specify the parent later.
	 * @return
	 * Returns a new <i>MTCheckButton</i>.
	 */
	public MTCheckButton createCheckButton( String text, String name, MTComponent parent){
		MTCheckButton ret = new StdMTCheckButton( text, 0, 0, pApp);
		ret.setName( name);
		if(parent != null)
			parent.addChild( ret);
		else
			addToCanvas( ret);
		
		return ret;
	}
	
	/**
	 * Creates a new <i>MTChoiceContainer</i> and returns it.
	 * @param buttonList
	 * The list of buttons to be gathered in this container as a 
	 * <i>List</i> of type <i>MTCheckButton</i>. If the specified 
	 * argument is null, no buttons will initially be put into the container.
	 * @return
	 * Returns a new <i>MTChoiceContainer</i>.
	 */
	public MTChoiceContainer createChoiceContainer(List<MTCheckButton> buttonList){
		if( buttonList != null)
			return new StdMTChoiceContainer( buttonList, pApp);
		return new StdMTChoiceContainer( pApp);
	}
	
	/**
	 * Creates a new <i>MTGraphPlotter</i> and returns it.
	 * @param x
	 * The x-position of this component as a <i>float</i>
	 * @param y
	 * The y-position of this component as a <i>float</i>
	 * @param textX
	 * The caption On the x-axis of this component as a <i>String</i>
	 * @param textY
	 * The caption On the y-axis of this component as a <i>String</i>
	 * @param values
	 * The initial list of values to be plotted as a List of type Double. If the
	 * specified argument is null, plotter with no initial values to plot will be created.
	 * @return
	 * Returns a new <i>MTGraphPlotter</i>.
	 */
	public MTGraphPlotter createGraphPlotter( float x, float y, String textX, String textY, List<Double> values){
		return createGraphPlotter(textX, textY, values, "", null);
	}
	
	/**
	 * Creates a new <i>MTGraphPlotter</i> and returns it.
	 * @param textX
	 * The caption On the x-axis of this component as a <i>String</i>.
	 * @param textY
	 * The caption On the y-axis of this component as a <i>String</i>.
	 * @param values
	 * The initial list of values to be plotted as a List of type Double. If the
	 * specified argument is null, plotter with no initial values to plot will be created.
	 * @param name
	 * The name of the new component as a <i>String</i>.
	 * @param parent
	 * The parent of the new component as an <i>MTComponent</i>
	 * or NULL if you want to specify the parent later.
	 * @return
	 * Returns a new <i>MTGraphPlotter</i>.
	 */
	public MTGraphPlotter createGraphPlotter( String textX, String textY, List<Double> values, String name, MTComponent parent){
		MTGraphPlotter ret = new StdMTGraphPlotter( 0, 0, textX, textY, values, pApp);
		ret.setName( name);
		if(parent != null)
			parent.addChild( ret);
		else
			addToCanvas( ret);
		
		return ret;
	}
	
	/**
	 * Creates a new <i>MTSlider</i> and returns it.
	 * @param x
	 * The x-position of this component as a <i>float</i>.
	 * @param y
	 * The y-position of this component as a <i>float</i>.
	 * @return
	 * Returns a new <i>MTSlider</i>.
	 */
	public MTSlider createSlider( float x, float y){
		MTSlider ret = new StdMTSlider( x, y, pApp);
		addToCanvas( ret);
		return ret;
	}
	
	/**
	 * Creates a new <i>MTSlider</i> and returns it.
	 * @param name
	 * The name of the new component as a <i>String</i>.
	 * @param parent
	 * The parent of the new component as an <i>MTComponent</i>
	 * or NULL if you want to specify the parent later.
	 * @return
	 * Returns a new <i>MTSlider</i>.
	 */
	public MTSlider createSlider( String name, MTComponent parent){
		MTSlider ret = new StdMTSlider( 0, 0, pApp);
		ret.setName( name);
		if(parent != null)
			parent.addChild( ret);
		return ret;
	}
	
	/**
	 * Creates a new <i>MTTextArea</i> and returns it.
	 * @param x
	 * The x-position of this component as a <i>float</i>.
	 * @param y
	 * The y-position of this component as a <i>float</i>.
	 * @param text
	 * The text of this textarea as a <i>String</i>.
	 * @return
	 * Returns a new <i>MTTextArea</i>.
	 */
	public MTTextArea createTextArea( float x, float y, String text){
		MTTextArea ret = new MTTextArea( pApp, x, y, 180, 64, GUIFonts.instance().get(ProviderTarget.TextSignal));
		ret.setText( text);
		ret.setAnchor( PositionAnchor.UPPER_LEFT);
		
		ret.setNoFill( true);
		ret.setNoStroke( true);
		
		ret.unregisterAllInputProcessors();
		ret.removeAllGestureEventListeners();
		
		addToCanvas( ret);
		return ret;
	}
	
	/**
	 * Creates a new <i>MTTextArea</i> and returns it.
	 * @param text
	 * The text of this textarea as a <i>String</i>.
	 * @param name
	 * The name of the new component as a <i>String</i>.
	 * @param parent
	 * The parent of the new component as an <i>MTComponent</i>
	 * or NULL if you want to specify the parent later.
	 * @return
	 * Returns a new <i>MTTextArea</i>.
	 */
	public MTTextArea createTextArea( String text, String name, MTComponent parent){
		MTTextArea ret = new MTTextArea( pApp, 0, 0, 180, 64, GUIFonts.instance().get(ProviderTarget.TextSignal));
		ret.setText( text);
		ret.setAnchor( PositionAnchor.UPPER_LEFT);
		
		ret.setNoFill( true);
		ret.setNoStroke( true);
		
		ret.unregisterAllInputProcessors();
		ret.removeAllGestureEventListeners();
		
		ret.setName( name);
		if(parent != null)
			parent.addChild( ret);
		return ret;
	}
	
	/**
	 * Creates a new <i>MTSuggestionTextArea</i> and returns it.
	 * @param x
	 * The x-position of this component as a <i>float</i>.
	 * @param y
	 * The y-position of this component as a <i>float</i>.
	 * @return
	 * Returns a new <i>MTSuggestionTextArea</i>.
	 */
	public MTSuggestionTextArea createInputField( float x, float y){
		MTSuggestionTextArea ret = new MTSuggestionTextArea((MTApplication)pApp, 200);
		ret.setAnchor(PositionAnchor.UPPER_LEFT);
		ret.setFont( GUIFonts.instance().get(ProviderTarget.TextNormal));
		ret.setPositionGlobal( new Vector3D(x,y));
		
		for(  AbstractComponentProcessor p : ret.getInputProcessors()){
			if( !(p instanceof TapProcessor))
				ret.unregisterInputProcessor( p);
		}
		
		ret.setTexture( GUITextures.instance().get(ProviderTarget.InputFieldNormal));
		ret.setNoStroke( false);
		ret.setStrokeWeight( 2);
		ret.setStrokeColor( MTColor.BLACK);
		ret.setFont( GUIFonts.instance().get(ProviderTarget.TextNormal));
		
		addToCanvas( ret);
		return ret;
	}
	
	/**
	 * Creates a new <i>MTSuggestionTextArea</i> and returns it.
	 * @param name
	 * The name of the new component as a <i>String</i>.
	 * @param parent
	 * The parent of the new component as an <i>MTComponent</i>
	 * or NULL if you want to specify the parent later.
	 * @return
	 * Returns a new <i>MTSuggestionTextArea</i>.
	 */
	public MTSuggestionTextArea createInputField( String name, MTComponent parent){
		MTSuggestionTextArea ret = new MTSuggestionTextArea((MTApplication)pApp, 200);
		ret.setAnchor(PositionAnchor.UPPER_LEFT);
		ret.setFont( GUIFonts.instance().get(ProviderTarget.TextNormal));
		
		for(  AbstractComponentProcessor p : ret.getInputProcessors()){
			if( !(p instanceof TapProcessor))
				ret.unregisterInputProcessor( p);
		}
		
		ret.setTexture( GUITextures.instance().get(ProviderTarget.InputFieldNormal));
		ret.setNoStroke( false);
		ret.setStrokeWeight( 2);
		ret.setStrokeColor( MTColor.BLACK);
		ret.setFont( GUIFonts.instance().get(ProviderTarget.TextNormal));
		
		ret.setName( name);
		if( parent != null)
			parent.addChild( ret);
		else
			addToCanvas( ret);
		
		return ret;
	}
	
	/**
	 * Creates a new initially unmovable <i>MTRectangle</i> and returns it.
	 * The <i>PositionAnchor</i> of this object will be set to the upper left corner.
	 * @param x
	 * The global x-position of this component as a <i>float</i>.
	 * @param y
	 * The global y-position of this component as a <i>float</i>.
	 * @param width
	 * The width of this component as a <i>float</i>.
	 * @param height
	 * The height of this component as a <i>float</i>.
	 * @return
	 * Returns a new <i>MTRectangle</i>.
	 */
	public MTRectangle createPanel( float x, float y, float width, float height){

		return createPanel(x, y, width, height, "unnamed panel", null);
	}
	
	/**
	 * Creates a new initially unmovable <i>MTRectangle</i> and returns it.
	 * The <i>PositionAnchor</i> of this object will be set to the upper left corner.
	 * @param x
	 * The global x-position of this component as a <i>float</i>.
	 * @param y
	 * The global y-position of this component as a <i>float</i>.
	 * @param width
	 * The width of this component as a <i>float</i>.
	 * @param height
	 * The height of this component as a <i>float</i>.
	 * @param name
	 * The name of the new component as a <i>String</i>.
	 * @param parent
	 * The parent of the new component as an <i>MTComponent</i>
	 * or NULL if you want to specify the parent later.
	 * @return
	 * Returns a new <i>MTRectangle</i>.
	 */
	public MTRectangle createPanel( float x, float y, float width, float height, String name, MTComponent parent){
		MTRectangle ret = new MTRectangle(pApp, x, y, width, height);
		
		ret.setNoStroke( true);
		ret.unregisterAllInputProcessors();
		ret.removeAllGestureEventListeners();
		ret.setTexture( GUITextures.instance().get(ProviderTarget.Panel));
		ret.setAnchor( PositionAnchor.UPPER_LEFT);
		
		ret.setName( name);
		if(parent != null)
			parent.addChild( ret);
		else 
			addToCanvas( ret);

		return ret;
	}
	
	/**
	 * Creates a new initially unmovable <i>MTRectangle</i> and returns it.
	 * The <i>PositionAnchor</i> of this object will be set to the upper left corner.
	 * @param position
	 * The global position of this component as a <i>Vector3S</i>.
	 * @param width
	 * The width of this component as a <i>float</i>.
	 * @param height
	 * The height of this component as a <i>float</i>.
	 * @param name
	 * The name of the new component as a <i>String</i>.
	 * @param parent
	 * The parent of the new component as an <i>MTComponent</i>
	 * or NULL if you want to specify the parent later.
	 * @return
	 * Returns a new <i>MTRectangle</i>.
	 */
	public MTRectangle createPanel( Vector3D position, float width, float height, String name, MTComponent parent){
		return createPanel( position.getX(), position.getY(), width, height, name, parent);
	}
	
	/**
	 * Creates a new initially unmovable circle and returns it.
	 * @param x
	 * The x-position of this component as a <i>float</i>.
	 * @param y
	 * The y-position of this component as a <i>float</i>.
	 * @param radius
	 * The radius of this component as a <i>float</i>.
	 * @param name
	 * The name of the new component as a <i>String</i>.
	 * @param parent
	 * The parent of the new component as an <i>MTComponent</i>
	 * or NULL if you want to specify the parent later.
	 * @return
	 * Returns a new <i>MTEllipse</i>.
	 */
	public MTEllipse createCircle( float x, float y, float radius, String name, MTComponent parent){
		MTEllipse ret = new MTEllipse( pApp, new Vector3D(x-radius, y-radius), radius, radius);
		
		ret.setNoStroke( true);
		ret.unregisterAllInputProcessors();
		ret.removeAllGestureEventListeners();
		
		ret.setName( name);
		if(parent != null)
			parent.addChild( ret);
		else
			addToCanvas( ret);
		
		return ret;
	}
	
	/**
	 * Creates a List in which MTCells can be stored.
	 * @param x
	 * The x-position of this component as a <i>float</i>
	 * @param y
	 * The y-position of this component as a <i>float</i>
	 * @param width
	 * The width of this component as a <i>float</i>
	 * @param height
	 * The height of this component as a <i>float</i>
	 * @param name
	 * The name of the new component as a <i>String</i>.
	 * @param parent
	 * The parent of the new component as an <i>MTComponent</i>
	 * or NULL if you want to specify the parent later.
	 * @return
	 * Returns a new <i>MTList</i>.
	 */
	public MTList createList( float x, float y, float width, float height, String name, MTComponent parent){
		
		MTList ret = new MTList( this.pApp, x, y, width, height);

		ret.setName( name);
		ret.setFillColor( new MTColor( 0, 0 , 0, 128));
		ret.setNoStroke(true);
		if(parent != null)
			parent.addChild( ret);
		else
			addToCanvas( ret);
		
		return ret;
	}
	
	/**
	 * Creates a List in which MTCells can be stored.
	 * @param x
	 * The x-position of this component as a <i>float</i>
	 * @param y
	 * The y-position of this component as a <i>float</i>
	 * @param width
	 * The width of this component as a <i>float</i>
	 * @param height
	 * The height of this component as a <i>float</i>
	 * @return
	 * Returns a new <i>MTList</i>.
	 */
	public MTList createList( float x, float y, float width, float height){
		return createList(x, y, width, height, "", null);
	}
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Helper method for setting up the factory.
	 * Override this method when you create a child class.
	 * @param pApp
	 * The Application to render the fonts as a <i>PApplet</i>
	 */
	protected void setupHelper( PApplet pApp){
		this.pApp = pApp;
		autoAddToScene = false;
	}

	/**
	 * Helper method, which helps adding a specified component to the scene automatically.
	 * @param comp
	 * The component to add as an <i>MTComponent</i>
	 */
	protected void addToCanvas( MTComponent comp){
		if(autoAddToScene && currentScene != null)
			currentScene.getCanvas().addChild( comp);
	}
	
}
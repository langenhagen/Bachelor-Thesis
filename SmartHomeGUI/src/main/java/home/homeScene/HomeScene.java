package home.homeScene;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;

import javax.media.opengl.GL;


import gestureListeners.interaction.ZoomListener;
import gestureListeners.transformation.Displacement3DPlaneListener;
import gestureListeners.transformation.Rotation3DListener;
import home.Home;
import home.Item;
import home.deviceCommands.factories.CommandFactory;
import home.homeScene.ListenerFactory.SliderPitchListener;
import home.stateReadersWriters.XMLReader;
import home.stateReadersWriters.XMLWriter;
import home.stateVisualizers.factories.StateVisualizerFactory;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.MTLight;
import org.mt4j.components.visibleComponents.widgets.MTOverlayContainer;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.camera.Icamera;
import org.mt4j.util.math.Vector3D;
import org.sercho.masp.models.XMIUtility;
import org.sercho.masp.models.Context.ContextPackage;
import org.sercho.masp.models.Context.Environment;

import provider.GUI3DModels;

import scenes.AbstractGUIScene;
import util.GlobalVariables;
import widgets.MT3DObject;
import widgets.MTContextModel3DObject;
import widgets.MTSlider;
import widgets.factories.GUIFactory;
import de.dailab.masp.models.MetaMetaModel.MetaModel;
import de.dailab.masp.models.MetaMetaModel.ecore.EcoreMetaModelConverter;

/**
 * This scene is a first approach of making a smart home GUI
 * 
 * @author langenhagen
 * @version 20120619
 */
public class HomeScene extends AbstractGUIScene {


	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the smart home instance */
	Home home;
	/** the environment instance */
	Environment environment = null;
	
	// gui components
	
	/** the overlay container */
	MTOverlayContainer overlay;
	/** the slider responsible for zooming */
	MTSlider sliderZoom;
	/** the slider responsible for pitching the view */
	MTSlider sliderPitch;
	
	/** the initial value of the zoom slider */
	final float initSliderZoomValue = -2000;
	/** the minimal value of the pitch slider. */
	final float minSliderPitchValue = 0.100f;
	/** the initial pitch of the camera in radians */
	final float initSliderPitchValue = (float)( Math.PI/3);
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructor.
	 */
	public HomeScene(MTApplication mtApp, String name, String contextModelPath){
		super( mtApp, name);
	
		// Set near/ far clip plane
		mtApp.perspective( (float)( Math.PI/3.0),(float)mtApp.getWidth()/mtApp.getHeight(),100,100000);
		
		// Setup factories
		CommandFactory.setup( CommandFactory.class);
		StateVisualizerFactory.setup( StateVisualizerFactory.class);
	
		
		// create environment
		try{
		    environment = 
		    	(Environment)(XMIUtility.convert( 
		    			new FileInputStream( contextModelPath), 
		    			ContextPackage.eINSTANCE).get(0));
		}
		catch(Exception e){
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		
		// meta model stuff
		MetaModel metaModel = EcoreMetaModelConverter.convert(ContextPackage.eINSTANCE);
		metaModel.getMetaType().getMetaModel().start(metaModel);
		metaModel.start(environment);
		
		// Setup UI & create home
		setup3DArea( mtApp, contextModelPath);
		setupMenuArea( mtApp);
		
		for( Item i : home.getAllItems()){
			i.getStateVisualizer().notifyVisualizer();
		}
		
	}
	
	// PROTECTED METHODS //////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void keyEventHelper(KeyEvent e){

		switch (e.getKeyCode()){
		case KeyEvent.VK_TAB:
			System.out.println("Resetting 3D area to standard values");
			home.loadState();
			this.getSceneCam().setPosition( initCamPos);
			
			sliderZoom.setValue( initSliderZoomValue);
			sliderPitch.setValue( initSliderPitchValue);
		}
	}
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Sets up the 3d-area including the home-view and its items
	 * @param mtApp
	 * The corresponding <i>MTApplication</i>
	 */
	private void setup3DArea( MTApplication mtApp, String contextModelPath){

		// indicates, whether a helping grid shall be created
		final boolean createGrid = false;
		
		MTComponent area = new MTComponent( mtApp);
		this.getCanvas().addChild( area);
		
		//Init light settings
		MTLight.enableLightningAndAmbient(mtApp, 100, 100, 100, 255);
		MTLight light = new MTLight(mtApp, GL.GL_LIGHT3, new Vector3D( 0, -300, 0));
		
		this.getSceneCam().setZoomMinDistance( 100);
		
		// create home view
		MT3DObject homeView;
		boolean takeContextModel3DObject = GlobalVariables.instance().get("useClassic3DModel").equals("false");
		if( takeContextModel3DObject){
			homeView = new MTContextModel3DObject( mtApp, environment, false);
			homeView.scaleToUnit( 20000);
		}
		else{
			homeView = GUI3DModels.instance().getModel( "SerchoRoom", 0, 5000);
		}
		
		
		
		
		homeView.setPosOnScreen( 0.3f, 0.5f);
		homeView.setLight( light);
		area.addChild( homeView);

		final Vector3D upvector = new Vector3D( 0,1,0);
		
		
		home = new Home( "showroom", environment, homeView, upvector);
		
		String path = "saves/state_" + contextModelPath.substring( contextModelPath.lastIndexOf('\\')+1, contextModelPath.lastIndexOf('.')) + ".xml";
		
		home.setStateReader( new XMLReader(path));
		home.setStateWriter( new XMLWriter(path));
		home.loadState();
		
		// create helper grid?
		if(createGrid)
			area.addChild( GUIFactory.instance().createGrid(upvector, home.getView().getPosition(), MTColor.WHITE));
		
		
		homeView.registerInputProcessor( new ScaleProcessor( mtApp));
		homeView.addGestureListener( ScaleProcessor.class, new ZoomListener( this.getSceneCam(), 3));
		
		
		// necessary for reducing the space where the home is movable
		home.setMovingPlaneNormal( home.getMovingPlaneNormal());
		for( Item i : home.getAllItems()){
			area.addChild( i.getView());
		}
	}
	
	/**
	 * Sets up the 2-dimensional gui-aspects, including buttons, sliders, etc pp
	 * @param mtApp
	 * The corresponding <i>MTApplication</i>
	 */
	private void setupMenuArea( MTApplication mtApp){

		ListenerFactory l = new ListenerFactory();		

		// set up camera
		Icamera cam = this.getSceneCam();
		cam.setPosition( cam.getPosition().getSubtracted( Vector3D.Z_AXIS.getScaled( -1000)));

		// create GUI components
		overlay = new MTOverlayContainer( mtApp);
		this.getCanvas().addChild( overlay);
		overlay.setName( "overlay");
		
		sliderZoom = GUIFactory.instance().createSlider("sliderZoom", overlay);
		sliderZoom.setPosition( new Vector3D(sliderZoom.getPosition().getX(), mtApp.getHeight()/2-sliderZoom.getHeight() - 5));
		sliderZoom.setValueRange( -30000, 300);
		sliderZoom.setValue( initSliderZoomValue);
		sliderZoom.addGestureListener( DragProcessor.class, l.createSliderZoomListener( sliderZoom, this.getSceneCam()));
		float factor = sliderZoom.getMaxValue() - sliderZoom.getValue()+1;
		Vector3D lookdir = cam.getViewCenterPos().getSubtracted( cam.getPosition()).normalizeLocal();
		cam.setPosition( cam.getViewCenterPos().getSubtracted( lookdir .scaleLocal( factor)));
		
		sliderPitch = GUIFactory.instance().createSlider(sliderZoom.getPosition().x, sliderZoom.getPosition().y + sliderZoom.getHeight() + 10);
		SliderPitchListener pitchListener = l.createSliderPitchListener(sliderPitch, this.getSceneCam());
		sliderPitch.setValueRange( minSliderPitchValue, (float)( Math.PI/2)-0.001f);
		sliderPitch.setValue( initSliderPitchValue);
		sliderPitch.addGestureListener( DragProcessor.class, pitchListener);
		sliderPitch.setName( "sliderPitch");
		
		// adjust view to slider's properties
		pitchListener.processGestureEvent( null);
		
		overlay.addChild( sliderPitch);
		
		// necessary for correct displacement constraining
		float maxMovementRadius = 100000;
		home.setInitialPosition( home.getView().getPosition());
		home.setMaxMovementRadius( maxMovementRadius);
		home.setMovingPlaneNormal( home.getMovingPlaneNormal());
		
		// add itemID gestureListeners...		
		for( Item i : home.getAllItems()){
			MT3DObject view = i.getView();
			view.addGestureListener( DragProcessor.class, new Displacement3DPlaneListener( home.getMovingPlaneNormal()).
					setHome( home).setMovementRadius( maxMovementRadius));
			view.addGestureListener( RotateProcessor.class, new Rotation3DListener( home.getMovingPlaneNormal()).setHome( home));
		}
	}
	
}
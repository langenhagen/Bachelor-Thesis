package scenes.tests;

import gestureListeners.transformation.Displacement3DPlaneListener;

import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;


import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Matrix;
import org.mt4j.util.math.Vector3D;

import provider.GUI3DModels;
import scenes.AbstractGUIScene;
import util.Logger;
import widgets.MT2DManipulator;
import widgets.MT3DObject;
import widgets.MTButton;
import widgets.MTCheckButton;
import widgets.MTSlider;
import widgets.factories.GUIFactory;


/**
 * This scene defines an Test environment for pointing gestures.
 * Different 3D-objects of different size and position can be tapped.
 * Every tap will be logged in a file.
 * 
 * @author langenhagen
 * @version 20110722
 */
public class PointTestScene extends AbstractGUIScene {

	Logger logger = new Logger( "test.txt", "\t");
	
	MTCheckButton chkEditMode;
	MTButton btnAddObject;
	MTComponent grid;
	int objectCount = 0;
	List<MT3DObject> objectList = new LinkedList<MT3DObject>();
	MTTextArea txtObjectCount;
	MT3DObject outsideBox;
	MT2DManipulator man;
	MTComponent mother;
	Vector3D screenCenter;
	MTSlider slider;
	Matrix matLine = null;
	MTLine lineNormal = null;
	Matrix matMother = null;
	
	public PointTestScene(MTApplication mtApp, String name){
		super( mtApp, name);
		
	 	screenCenter = 
	 		new Vector3D(
	 			this.getMTApplication().getWidth() / 2,
	 			this.getMTApplication().getHeight()/ 2);
	 	
		createScene();
		
		addListeners();
		
		// add functionality
	}
	
	// HELPER METHODS /////////////////////////////////////////////////////////////////////////////
	
	private void createScene(){
		
		GUIFactory f = GUIFactory.instance();
		f.setCurrentScene( this);
		f.setAutoAddToScene( false);

		mother = new MTComponent( this.getMTApplication());
		chkEditMode = f.createCheckButton( 0, 0, "Edit mode");
		btnAddObject = f.createButton( 0, 0, "Add object");
		grid = f.createGrid( Vector3D.Y_AXIS, screenCenter, MTColor.WHITE);
		txtObjectCount = f.createTextArea( chkEditMode.getWidth()+10, 0, "Object count: 0");
		outsideBox = GUI3DModels.instance().getModel("SampleModel", 1, 5000);
		man = f.create2DManipulator( 0, chkEditMode.getHeight() + 10);
		slider = f.createSlider(0,0);
		matMother = mother.getLocalMatrix().clone();
		lineNormal = new MTLine( this.getMTApplication(), 0, 0, 0,0, 1, 0);
		matLine = lineNormal.getLocalMatrix().clone();
		
		this.getCanvas().addChild( mother);
		this.getCanvas().addChild( chkEditMode);
		this.getCanvas().addChild( btnAddObject);
		this.getCanvas().addChild( txtObjectCount);
		this.getCanvas().addChild( man);
		//this.getCanvas().addChild( slider);
		mother.addChild( grid);
		mother.addChild(outsideBox);
		
		btnAddObject.setPosOnScreen( 0.0f, 0.8f);
		
		chkEditMode.setChecked( true);
		
		txtObjectCount.setNoFill( false);
		txtObjectCount.setFillColor( MTColor.WHITE);

		man.setRange( -90, 90, -90, 90);
		man.addGestureListener( DragProcessor.class, new SceneRotateListener( this));
		
		slider.setValueRange( -100000, 1000000);
		slider.setPosOnScreen( 0.9f, 0);
		
		outsideBox.setPosOnScreen( 0.5f, 0.5f);
		outsideBox.addGestureListener( TapProcessor.class, new OutsideBoxContactListener());
		outsideBox.setName( "BG");

	}
	
	private void addListeners(){
		chkEditMode.addGestureListener( TapProcessor.class, new SwitchEditModeListener( this));
		btnAddObject.addGestureListener( TapProcessor.class, new AddObjectListener( this));	
	}
	
	@Override
	protected void keyEventHelper(KeyEvent e){
	}
	
	// GESTURE LISTENERS //////////////////////////////////////////////////////////////////////////
	
	class AddObjectListener implements IGestureEventListener{

		PointTestScene scene;
		
		public AddObjectListener( PointTestScene scene){
			this.scene = scene;
		}
		
		// adds functionality to add objects
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			if( ge.getId() != MTGestureEvent.GESTURE_STARTED)
				return false;
			
			MT3DObject obj = GUI3DModels.instance().getModel( "Schnecki", 0, 200);
			obj.setPosOnScreen( 0.5f, 0.5f);
			objectList.add( obj);
			obj.setName( "" + objectCount);
			objectCount++;
			scene.txtObjectCount.setText(  "Object count: " + objectCount);
			
			obj.registerInputProcessor( new DragProcessor( scene.getMTApplication()));
			obj.registerInputProcessor( new ScaleProcessor(scene.getMTApplication()));
			
			obj.addGestureListener(  DragProcessor.class, new Displacement3DPlaneListener( lineNormal.getVerticesGlobal()[1].getNormalized() ));
			//obj.addGestureListener( ScaleProcessor.class, new Scale3DListener());
			obj.addGestureListener( TapProcessor.class, new ObjectContactListener());
			//obj.addGestureListener( DragProcessor.class, new ObjectMoveUpDownListener());
			
			mother.addChild( obj);

			return false;
		}
	}
	
	class SwitchEditModeListener implements IGestureEventListener{

		PointTestScene scene;
		
		public SwitchEditModeListener( PointTestScene scene){
			this.scene = scene;
		}
	
		// add functionality for switching between edit & testing mode
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			MTCheckButton chk =  (MTCheckButton)ge.getTarget();
			
			if(chk.isChecked()){		// edit mode
				btnAddObject.setVisible( true);
				outsideBox.unregisterAllInputProcessors();
				
				for( MT3DObject obj : objectList){
					obj.unregisterAllInputProcessors();
					obj.registerInputProcessor( new DragProcessor( scene.getMTApplication() ));
					obj.registerInputProcessor( new ScaleProcessor( scene.getMTApplication() ));
					obj.addGestureListener( DragProcessor.class, new ObjectMoveUpDownListener());
				}
			}
			else{						// testing mode
				btnAddObject.setVisible( false);
				outsideBox.registerInputProcessor( new TapProcessor( scene.getMTApplication()));

				for( MT3DObject obj : objectList){
					obj.unregisterAllInputProcessors();
					obj.registerInputProcessor( new TapProcessor( scene.getMTApplication() ));
					}
			}
			return false;
		}
	}
	
	class ObjectContactListener implements IGestureEventListener{

		// provides object tapping functionality
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			TapEvent te = (TapEvent)ge;
			
			if(ge.getId() != MTGestureEvent.GESTURE_STARTED)
				return false;
			
			logger.log( "" + te.getTimeStamp());
			logger.log( te.getTarget().getName());
			logger.log( "" + te.getLocationOnScreen() );
			logger.log( "" + "" + ((MT3DObject)te.getCurrentTarget()).getPosition());
			logger.log( "" + ((MT3DObject)te.getCurrentTarget()).getWidth() );
			logger.log( "" + ((MT3DObject)te.getCurrentTarget()).getHeight() );
			logger.log( "\n", "");
			
			((MT3DObject)te.getCurrentTarget()).setVariant( 2);
			
			return false;
		}
	}
	
	class OutsideBoxContactListener implements IGestureEventListener{

		// adds functionality when tapping On the background
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			if( ge.getId() != MTGestureEvent.GESTURE_STARTED)
				return false;
			
			System.out.println("DANEBEN");
			
			TapEvent te = (TapEvent)ge;
			
			logger.log( "" + te.getTimeStamp());
			logger.log( te.getTarget().getName());
			logger.log( "\n", "");
			
			return false;
		}
	}
	
	class ObjectMoveUpDownListener implements IGestureEventListener{

		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			/*
			System.out.println("PADD");
			
			MT3DObject obj = ((MT3DObject)ge.getTarget());
			switch( ge.getId()){
			case MTGestureEvent.GESTURE_STARTED:
				slider.setValue( obj.getPosition( TransformSpace.GLOBAL).getY());
				break;
			case MTGestureEvent.GESTURE_UPDATED:
		
				
				Vector3D vecDir = lineNormal.getVerticesLocal()[1].getNormalized();
				vecDir.scaleLocal( slider.getValue());
				System.out.println(vecDir);
				obj.translate( new Vector3D(0,1,0), TransformSpace.GLOBAL);
				break;
			}*/
			return false;
		}
		
	}
	
	class SceneRotateListener implements IGestureEventListener{
		
		PointTestScene scene;
		
		public SceneRotateListener( PointTestScene scene){
			this.scene = scene;
		}
		
		// processes manipulator-changes
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			mother.setLocalMatrix( matMother.clone());
			mother.rotateY( screenCenter, (int)man.getValueX(), TransformSpace.GLOBAL);
			mother.rotateX( screenCenter, (int)man.getValueY(), TransformSpace.GLOBAL);
			
			lineNormal.setLocalMatrix( matLine.clone());
			lineNormal.rotateY( Vector3D.ZERO_VECTOR, man.getValueX(), TransformSpace.GLOBAL);
			lineNormal.rotateX( Vector3D.ZERO_VECTOR, man.getValueY(), TransformSpace.GLOBAL);
			
			switch(ge.getId()){
			case MTGestureEvent.GESTURE_STARTED:		// gesture started
				for( MT3DObject obj : objectList){
					obj.removeAllGestureEventListeners( DragProcessor.class);
				}
				break;
			case MTGestureEvent.GESTURE_ENDED:			// gesture ended
				for( MT3DObject obj : objectList){
					obj.addGestureListener(
							DragProcessor.class,
							new Displacement3DPlaneListener( lineNormal.getVerticesGlobal()[1].getNormalized() ));
					obj.addGestureListener(
							DragProcessor.class,
							new ObjectMoveUpDownListener());
				}
				break;
			}
			return false;
		}
	}
}
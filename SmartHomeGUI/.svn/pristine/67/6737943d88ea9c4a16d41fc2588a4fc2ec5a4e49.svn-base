package scenes.tests;

import gestureListeners.interaction.ZoomListener;
import gestureListeners.transformation.ArcBall3DListener;
import gestureListeners.transformation.Displacement2DListener;
import gestureListeners.transformation.Displacement3DPlaneListener;
import gestureListeners.transformation.Displacement3DLineListener;
import gestureListeners.transformation.Scale3DListener;

import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;


import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.MTLight;
import org.mt4j.components.clipping.Clip;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.IInputProcessor;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.arcballProcessor.ArcballProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;
import org.mt4jx.components.visibleComponents.widgets.MTSuggestionTextArea;
import org.mt4jx.input.inputProcessors.componentProcessors.Rotate3DProcessor.Rotate3DEvent;
import org.mt4jx.input.inputProcessors.componentProcessors.Rotate3DProcessor.Rotate3DProcessor;
import org.mt4jx.util.extension3D.MotionMapper;

import provider.GUI3DModels;
import provider.GUIFonts;
import provider.GUITextures;
import scenes.AbstractGUIScene;
import util.ModelInfo;
import widgets.AbstractGUIWidget;
import widgets.MT2DManipulator;
import widgets.MT3DObject;
import widgets.MTButton;
import widgets.MTCheckButton;
import widgets.MTChoiceContainer;
import widgets.MTGraphPlotter;
import widgets.MTSlider;
import widgets.MTTabContainer;
import widgets.factories.GUIFactory;
import widgets.std.StdMT2DManipulator;
import widgets.std.StdMT3DObject;
import widgets.std.StdMTButton;
import widgets.std.StdMTCheckButton;
import widgets.std.StdMTChoiceContainer;
import widgets.std.StdMTGraphPlotter;
import widgets.std.StdMTSlider;
import widgets.std.StdMTTab;


public class SceneX2 extends AbstractGUIScene {
	
	MT3DObject obj;
	MTSlider slider;
	MTGraphPlotter p;
	MTButton buton;
	
	public SceneX2(MTApplication mtApp, String name){
		super( mtApp, name);

		GUIFactory.instance().setCurrentScene( this);
		
		// ####################################################################################################
		/*
		// Spiel und Spass mit 3d-modellen
		obj = GUI3DModels.instance().getModel("SerchoRoom", 0, 200);
		obj.setPosOnScreen( 0.5f, 0.5f);
	
		obj.scaleToUnit( 500);
		
		//obj.registerInputProcessor( new DragProcessor( mtApp));
		//obj.addGestureListener( DragProcessor.class, new Displacement3DPlaneListener( new Vector3D(0,1,0)));
	
			
		
		buton = GUIFactory.instance().createButton( 200, 200, "FOOO");
		slider = GUIFactory.instance().createSlider( 100, 100);
		slider.addGestureListener( DragProcessor.class, new L2());
		slider.setEnabled( false);
		
		buton.addGestureListener( TapProcessor.class, new L());
		buton.setEnabled( true);
		buton.addChild( slider);
		
		this.getCanvas().addChild( buton);
		//obj.registerInputProcessor( new ScaleProcessor( mtApp));
		
		this.getCanvas().addChild( obj);
		buton.registerInputProcessor( new DragProcessor( mtApp));
		buton.addGestureListener( DragProcessor.class, new Displacement2DListener());
		obj.registerInputProcessor( new DragProcessor( mtApp));
		obj.addGestureListener( DragProcessor.class, new Displacement3DLineListener( Vector3D.Y_AXIS));
		
		//obj.addGestureListener( ScaleProcessor.class, new ZoomListener( getCanvas().getAttachedCamera(),1000));
		
		*/
		
		
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		MT3DObject obj = new StdMT3DObject( mtApp, "C:/Users/Barn/Desktop/Neuer Ordner", 0, Vector3D.ZERO_VECTOR, 20);

		GUIFactory.instance().create3DGrid( obj.getPosition());	
		
		getCanvas().addChild( obj);
		
		// ####################################################################################################
	}
	
	// HELPERS ////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void keyEventHelper(KeyEvent e){
	}
	
	// LISTENERS //////////////////////////////////////////////////////////////////////////////////
	
	private class L implements IGestureEventListener{

		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			if(ge.getId() == MTGestureEvent.GESTURE_STARTED)
				if(buton.getPosition().getX() == 200)
					buton.animMove( new Vector3D( 400, 300), 100);
				else
					buton.animMove( new Vector3D( 200, 200), 50);
			return false;
		}
		
	}
	
	private class L2 implements IGestureEventListener{

		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			System.out.println(slider.getValue());
			return false;
		}
	}
	

	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
}
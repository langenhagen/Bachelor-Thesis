package scenes.tests;

import java.awt.event.KeyEvent;

import org.mt4j.MTApplication;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Matrix;
import org.mt4j.util.math.Vector3D;

import provider.GUI3DModels;


import scenes.AbstractGUIScene;
import widgets.MT3DObject;
import widgets.factories.GUIFactory;


public class Rotation3DScene extends AbstractGUIScene {

	MT3DObject obj2;
	Matrix localChildM;
	
	public Rotation3DScene(MTApplication mtApp, String name){
		super( mtApp, name);
		
		MT3DObject obj = GUI3DModels.instance().getModel( "SampleModel");
		this.getCanvas().addChild( obj);
		//obj.setPosOnScreen( 0.2f, 0.2f);
		obj.setVariant( 1);
		obj.scaleToUnit( 500);
		
		obj2 = GUI3DModels.instance().getModel( "SampleModel");
		this.getCanvas().addChild( obj2);
		//obj2.setPosOnScreen( 0.7f, 0.7f);
		obj2.setVariant( 0);
		obj2.scaleToUnit( 400);
		
		
		obj.registerInputProcessor( new DragProcessor( mtApp));
		obj.addGestureListener( DragProcessor.class, new RotationFooListener());
		
		this.getCanvas().addChild( GUIFactory.instance().create3DGrid( Vector3D.ZERO_VECTOR));
		
		localChildM = new Matrix(
				.1f, 0, 0, 50,
				0, .3f, 0, 50,
				0, 0, .1f, 50,
				0, 0, 0, 20);
		
	}
	
	public class RotationFooListener implements IGestureEventListener{

		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			MT3DObject obj = (MT3DObject) ge.getTarget();
			
			double PI = Math.PI;
			
			double angle = PI/180;
			
			float cosa = (float)Math.cos( angle );
			float sina = (float)Math.sin( angle);
			
			Matrix m = new Matrix(
					cosa, -sina, 0, 0,
					sina, cosa, 0, 0,
					0, 0, 1, 0,
					0, 0, 0, 1);
			
			obj.transform( m);

			
			
			obj2.setLocalMatrix(  localChildM.mult( obj.getLocalMatrix()));
			
			// TODO Auto-generated method stub
			return false;
		}
		
	}
	
	@Override
	protected void keyEventHelper(KeyEvent e){
	}
	
}

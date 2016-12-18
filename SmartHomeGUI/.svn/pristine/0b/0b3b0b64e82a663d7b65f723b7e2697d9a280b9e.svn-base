package widgets.std;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.clipping.Clip;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.IInputProcessor;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import provider.GUITextures;

import util.Mode;
import util.ProviderTarget;
import widgets.MT2DManipulator;


/** 
 * @author langenhagen
 * @version 20110611
 */
public class StdMT2DManipulator extends MT2DManipulator {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the rect of this 2D manipulator */
	private MTRectangle rect;
	/** the knob to interact with */
	private MTEllipse knob;
	
	/** the minimum value of the x-range of this manipulator */
	private float minX;
	/** the maximum value of the x-range of this manipulator */
	private float maxX;
	/** the minimum value of the y-range of this manipulator */
	private float minY;
	/** the maximum value of the y-range of this manipulator */
	private float maxY;
	/** the current x-value of this manipulator */
	private float valX;
	/** the current y-value of this manipulator */
	private float valY;
	
	/** indicates, if the knob is automatically snapping back */
	private boolean snapBack;
	/** the x-value to snap back to */
	private float snapX;
	/** the y-value to snap back to */
	private float snapY;
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructor #1
	 * Sets a standard 2D manipulator On the specified position
	 * with a [-100,100]-range in both dimensions. The initial value will be (0,0).
	 * The knob will not snap back automatically.
	 * @param x
	 * The x-position of this 2d manipulator as a <i>float</i>
	 * @param y
	 * The y-position of this 2d manipulator as a <i>float</i>
	 * @param pApplet
	 * The Application which is rendering this GUI-component as a <i>PApplet</i>
	 */
	public StdMT2DManipulator(float x, float y,  PApplet pApp){
		this( x, y, -100, -100, 100, 100, 0, 0, false, pApp);
	}
	
	/**
	 * Constructor #2
	 * A more specific constructor which allows to assign better values initially.
	 * @param x
	 * The x-position of this 2d manipulator as a <i>float</i>
	 * @param y
	 * The y-position of this 2d manipulator as a <i>float</i>
	 * @param minX
	 * The minimum x-value this component cann accept as a <i>float</i>
	 * If this value is higher than the corresponding maxVal, minVal & maxVal will be switched.
	 * @param minY
	 * The minimum y-value this component cann accept as a <i>float</i>
	 * If this value is higher than the corresponding maxVal, minVal & maxVal will be switched.
	 * @param maxX
	 * The maximum x-value this component cann accept as a <i>float</i>
	 * If this value is higher than the corresponding minVal, minVal & maxVal will be switched.
	 * @param maxY
	 * The maximum y-value this component cann accept as a <i>float</i>
	 * If this value is higher than the corresponding minVal, minVal & maxVal will be switched.
	 * @param valX
	 * The current x-value this component stores as a <i>float</i>.
	 * If this value is out of range of the corresponding [minVal, maxVal]-interval,
	 * the current value will be set to a value equal to minVal.
	 * This value will also be the initial x-snap-back-value.
	 * @param valY
	 * The current y-value this component stores as a <i>float</i>.
	 * If this value is out of range of the corresponding [minVal, maxVal]-interval,
	 * the current value will be set to a value equal to minVal.
	 * This value will also be the initial y-snap-back-value.
	 * @param
	 * Specifies, if the knob will snap back to initial position, when ending a tap-gesture.
	 * This can be helpful when you work with difference-values, eg while controlling 
	 * the orientation of the camera.
	 * @param pApp
	 * The Application rendering this component as a <i>PApplet</i>
	 */
	public StdMT2DManipulator(	float x, float y, 
								float minValX, float minValY,
								float maxValX, float maxValY,
								float valX, float valY,
								boolean snapBack,
								PApplet pApp){
		super(pApp);
		
		// initializing values
		if( minValX < maxValX){
			minX = minValX;
			maxX = maxValX;
		}else{
			minX = maxValX;
			maxX = minValX;
		}
		if( minValY < maxValY){
			minY = minValY;
			maxY = maxValY;
		}else{
			minY = maxValY;
			maxY = minValY;
		}
		
		if( minValX <= valX && valX <= maxValY)
			this.valX = valX;
		else
			this.valX = minValX;
		if( minValY <= valY && valY <= maxValY)
			this.valY = valY;
		else
			this.valY = minValY;
		
		this.snapBack = snapBack;
		
		snapX = this.valX;
		snapY = this.valY;
		
		// initializing styles
		float width = 300;
		float height = 300;
		
		rect = new MTRectangle( pApp, width, height);
		knob = new MTEllipse( pApp, Vector3D.ZERO_VECTOR, 30, 30);
		
		rect.setAnchor( PositionAnchor.UPPER_LEFT);
		rect.setTexture( GUITextures.instance().get(ProviderTarget.TwoDManipulatorNormal));
		rect.setNoStroke( true);
		rect.setChildClip( new Clip(rect));
		knob.setTexture( GUITextures.instance().get(ProviderTarget.TwoDManipulatorKnobNormal));
		knob.setStrokeColor( MTColor.BLACK);
		
		// set functionality		
		rect.unregisterAllInputProcessors();
		rect.removeAllGestureEventListeners();
		knob.unregisterAllInputProcessors();
		knob.removeAllGestureEventListeners();
		
		knob.registerInputProcessor( new DragProcessor( pApp));
		knob.addGestureListener( DragProcessor.class, new DragListener(this));
		
		rect.addChild( knob);
		this.addChild( rect);
		rect.setPositionGlobal( new Vector3D(x,y));
		
		revalidate();
	}

	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/* (non-Javadoc)
	 * @see widgets.MT2DManipulator#setValueX(float)
	 */
	@Override
	public boolean setValueX(float val){
		if( maxX < val || minX > val)
			return false;
		
		valX = val;
		revalidate();
		return true;
	}

	/* (non-Javadoc)
	 * @see widgets.MT2DManipulator#setValueY(float)
	 */
	@Override
	public boolean setValueY(float val){
		if( maxY < val || minY > val)
			return false;
		
		valY = val;
		revalidate();
		return true;
	}

	/* (non-Javadoc)
	 * @see widgets.MT2DManipulator#getValue()
	 */
	@Override
	public Vector3D getValue(){
		return new Vector3D( valX, valY);
	}
	
	/* (non-Javadoc)
	 * @see widgets.MT2DManipulator#getValueX()
	 */
	@Override
	public float getValueX(){
		return valX;
	}

	/* (non-Javadoc)
	 * @see widgets.MT2DManipulator#getValueY()
	 */
	@Override
	public float getValueY(){
		return valY;
	}

	/* (non-Javadoc)
	 * @see widgets.MT2DManipulator#setRangeX(float, float)
	 */
	@Override
	public boolean setRangeX(float min, float max){
		if( min > max)
			return false;
		
		minX = min;
		maxX = max;
		revalidate();
		return true;
	}

	/* (non-Javadoc)
	 * @see widgets.MT2DManipulator#setRangeY(float, float)
	 */
	@Override
	public boolean setRangeY(float min, float max){
		if( min > max)
			return false;
		
		minY = min;
		maxY = max;
		revalidate();
		return true;
	}

	/* (non-Javadoc)
	 * @see widgets.MT2DManipulator#getMinValueX()
	 */
	@Override
	public float getMinValueX(){
		return minX;
	}

	/* (non-Javadoc)
	 * @see widgets.MT2DManipulator#getMaxValueX()
	 */
	@Override
	public float getMaxValueX(){
		return maxX;
	}

	/* (non-Javadoc)
	 * @see widgets.MT2DManipulator#getMinValueY()
	 */
	@Override
	public float getMinValueY(){
		return minY;
	}

	/* (non-Javadoc)
	 * @see widgets.MT2DManipulator#getMaxValueY()
	 */
	@Override
	public float getMaxValueY(){
		return maxY;
	}

	/* (non-Javadoc)
	 * @see widgets.MT2DManipulator#setSnapBackValue(float, float)
	 */
	@Override
	public boolean setSnapBackValue(float x, float y){	
		if( minX > x || maxX < x || minY > y || maxY < y )
			return false;
		
		snapX = x;
		snapY = y;
		revalidate();
		return true;
	}
	
	/* (non-Javadoc)
	 * @see widgets.MT2DManipulator#getSnapBackValue()
	 */
	@Override
	public Vector3D getSnapBackValue(){
		return new Vector3D( snapX, snapY);
	}

	/* (non-Javadoc)
	 * @see widgets.MT2DManipulator#setSnapBackEnabled(boolean)
	 */
	@Override
	public void setSnapBackEnabled(boolean b){
		snapBack = b;
		revalidate();
	}

	/* (non-Javadoc)
	 * @see widgets.MT2DManipulator#isSnapBackEnabled()
	 */
	@Override
	public boolean isSnapBackEnabled(){
		return snapBack;
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#getPosition(org.mt4j.components.TransformSpace)
	 */
	@Override
	public Vector3D getPosition(TransformSpace space){
		return rect.getPosition( space);
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setPosition(org.mt4j.util.math.Vector3D, org.mt4j.components.TransformSpace)
	 */
	@Override
	public void setPosition(Vector3D pos, TransformSpace space){
		switch(space){
		case LOCAL:
			rect.setPositionRelativeToOther( rect, pos);
			break;
		case RELATIVE_TO_PARENT:
			rect.setPositionRelativeToOther( this.getParent(), pos);
			break;
		case GLOBAL:
			rect.setPositionGlobal( pos);
			break;
		}
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#getWidth(org.mt4j.components.TransformSpace)
	 */
	@Override
	public float getWidth(TransformSpace space){
		return rect.getWidthXY( space);
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#getHeight(org.mt4j.components.TransformSpace)
	 */
	@Override
	public float getHeight(TransformSpace space){
		return rect.getHeightXY( space);
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setWidth(float, org.mt4j.components.TransformSpace)
	 */
	@Override
	public void setWidth(float width, TransformSpace space){
		switch( space){
		case LOCAL:
			rect.setWidthLocal( width);
			break;
		case RELATIVE_TO_PARENT:
			rect.setWidthXYRelativeToParent( width);
			break;
		case GLOBAL:
			rect.setWidthXYGlobal( width);
		}
		revalidate();
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setHeight(float, org.mt4j.components.TransformSpace)
	 */
	@Override
	public void setHeight(float height, TransformSpace space){
		switch( space){
		case LOCAL:
			rect.setHeightLocal( height);
			break;
		case RELATIVE_TO_PARENT:
			rect.setHeightXYRelativeToParent( height);
			break;
		case GLOBAL:
			rect.setHeightXYGlobal( height);
		}
		revalidate();
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setMode(util.Mode)
	 */
	@Override
	public void setMode(Mode mode){
		switch(mode){
		case NORMAL:
			rect.setTexture( GUITextures.instance().get(ProviderTarget.TwoDManipulatorNormal));
			knob.setTexture( GUITextures.instance().get(ProviderTarget.TwoDManipulatorKnobNormal));
			break;
		case TAP:
			rect.setTexture( GUITextures.instance().get(ProviderTarget.TwoDManipulatorTap));
			knob.setTexture( GUITextures.instance().get(ProviderTarget.TwoDManipulatorKnobTap));
			break;
		case DISABLED:
			rect.setTexture( GUITextures.instance().get(ProviderTarget.TwoDManipulatorDisabled));
			knob.setTexture( GUITextures.instance().get(ProviderTarget.TwoDManipulatorKnobDisabled));
			break;
		case SIGNAL:
			rect.setTexture( GUITextures.instance().get(ProviderTarget.TwoDManipulatorSignal));
			knob.setTexture( GUITextures.instance().get(ProviderTarget.TwoDManipulatorKnobSignal));
			break;
		case SIGNAL2:
			rect.setTexture( GUITextures.instance().get(ProviderTarget.TwoDManipulatorSignal2));
			knob.setTexture( GUITextures.instance().get(ProviderTarget.TwoDManipulatorKnobSignal2));
		}
		this.mode=mode;
	}
	
	/* (non-Javadoc)
	 * @see org.mt4j.components.MTComponent#addGestureListener( java.lang.Class<? extends IInputProcessor>,org.mt4j.input.inputProcessors.IGestureEventListener)
	 */
	@Override
	public void addGestureListener(Class<? extends IInputProcessor> gestureEvtSender, IGestureEventListener listener){
		if( DragProcessor.class.isAssignableFrom( gestureEvtSender))
			knob.addGestureListener( gestureEvtSender, listener);
		else
			super.addGestureListener( gestureEvtSender, listener);
	}
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This method revalidates the view by repositioning the knob
	 * in aspect of size of the rect, the current value and the snap back-indicator
	 */
	protected void revalidate(){
		
		float knobDiameter =  knob.getWidthXY( TransformSpace.RELATIVE_TO_PARENT);
		float shapeWidth = rect.getWidthXY( TransformSpace.LOCAL) - knobDiameter;
		float shapeHeight = rect.getHeightXY( TransformSpace.LOCAL) - knobDiameter;
		Vector3D shapePos = rect.getPosition( TransformSpace.LOCAL);
		float intervalX = Math.abs( maxX - minX);
		float intervalY = Math.abs( maxY - minY);
		
		float mX;
		float mY;
		if( snapBack){
			mX = (snapX - minX) / intervalX;
			mY = 1 - (snapY - minY) / intervalY;
		}else{
			mX = (valX - minX) / intervalX;
			mY = 1 - (valY - minY) / intervalY;
		}
		
		float knobPosX = shapePos.getX() + knobDiameter/2 + mX*shapeWidth;
		float knobPosY = shapePos.getY() + knobDiameter/2 + mY*shapeHeight;
		
		knob.setPositionRelativeToParent( new Vector3D(knobPosX, knobPosY));
	}
	
	// PRIVATE LISTENER ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * This inner class lets you move the Knob according to the Manipulator's behaviour.
	 * @author: Barn
	 * @version: 20110613
	 */
	private class DragListener implements IGestureEventListener{

		MT2DManipulator man;
		
		/**
		 * Constructor #1
		 * @param man
		 * The 2D-manipulator which 
		 */
		public DragListener( MT2DManipulator man){
			this.man = man;
		}
		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){

			if( !man.isEnabled())
				return false;
			
			// initializing variables
			MTEllipse knob = (MTEllipse)ge.getTarget();
			MTRectangle rect = (MTRectangle)knob.getParent();
			Vector3D rectPos = rect.getPosition( TransformSpace.LOCAL);
			float rectWidth = rect.getWidthXY( TransformSpace.LOCAL);
			float rectHeight= rect.getHeightXY( TransformSpace.LOCAL);

			switch (ge.getId()){
			case MTGestureEvent.GESTURE_STARTED:	// On clicking
				man.setMode( Mode.TAP);
				
			case MTGestureEvent.GESTURE_UPDATED:	// On updating
				float knobRadius =  knob.getWidthXY( TransformSpace.RELATIVE_TO_PARENT)/2;
				Vector3D curVecVal = rect.globalToLocal( ((DragEvent)ge).getTo());
						
				// make sure, you are moving inside the bounds
				if( curVecVal.getX() < rectPos.getX() + knobRadius)
					curVecVal.setX( rectPos.getX() + knobRadius);
				else if( curVecVal.getX() > rectPos.getX() + rectWidth - knobRadius)
					curVecVal.setX( rectPos.getX() + rectWidth - knobRadius);
				if( curVecVal.getY() < rectPos.getY() + knobRadius)
					curVecVal.setY( rectPos.getY() + knobRadius);
				else if( curVecVal.getY() > rectPos.getY() + rectHeight - knobRadius)
					curVecVal.setY( rectPos.getY() + rectHeight - knobRadius);

				// doing some linear interpolation
				float mX = (curVecVal.getX() - rectPos.getX() - knobRadius) / (rectWidth - 2*knobRadius);
				float mY = (curVecVal.getY() - rectPos.getY() - knobRadius) / (rectHeight - 2*knobRadius);
				
				boolean isSnapBack = man.isSnapBackEnabled();
				man.setSnapBackEnabled( false);
				man.setValue(	mX*(man.getMaxValueX() - man.getMinValueX()) + man.getMinValueX(),
								(1-mY)*(man.getMaxValueY() - man.getMinValueY()) + man.getMinValueY());
				snapBack = isSnapBack; // not the fine arts but hey... it works				
				break;
				
			case MTGestureEvent.GESTURE_ENDED:		// On letting go
				man.setMode( Mode.NORMAL);
				
				if(!snapBack)
					break;
				
				// add snap-back animation
				float knobDiameter =  knob.getWidthXY( TransformSpace.RELATIVE_TO_PARENT);
				float shapeWidth = rect.getWidthXY( TransformSpace.LOCAL) - knobDiameter;
				float shapeHeight = rect.getHeightXY( TransformSpace.LOCAL) - knobDiameter;
				Vector3D shapePos = rect.getPosition( TransformSpace.LOCAL);
				float intervalX = Math.abs( maxX - minX);
				float intervalY = Math.abs( maxY - minY);

				animKnob( 
						knob, 
						new Vector3D(
								shapePos.getX() + knobDiameter/2 + ( (snapX - minX) / intervalX)*shapeWidth,
								shapePos.getY() + knobDiameter/2 + ( 1 - (snapY - minY) / intervalY)*shapeHeight));
				break;
			}
			return false;
		}
		
		/**
		 * Private helper which creates the snap-back animation.
		 * @param knob
		 * The knob to snap back as an <i>MTEllipse</i>
		 * @param dest
		 * The destination-vector to where to snap back as a <i>Vector3D</i>
		 */
		private void animKnob( final MTEllipse knob, final Vector3D dest){
			Animation anim = new Animation("move animation", new MultiPurposeInterpolator(0,100, 80, 0, 1, 1) , knob);
			anim.addAnimationListener( new IAnimationListener() {
	        	Vector3D origin = null;
	        	
	        	@Override
				public void processAnimationEvent(AnimationEvent event){	
	        		if( origin == null)
	        			origin = ((MTEllipse)event.getTarget()).getCenterPointRelativeToParent();
	        		
	        		Vector3D step = 
						new Vector3D(dest).subtractLocal( origin).divideLocal( 100/event.getValue());
	        		knob.setPositionRelativeToParent( step.addLocal(origin));	
				}
			}).start();
		}
	} // END class DragListener
}
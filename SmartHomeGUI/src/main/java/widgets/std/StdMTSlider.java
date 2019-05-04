package widgets.std;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.IInputProcessor;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.AbstractComponentProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import provider.GUITextures;

import util.Mode;
import util.ProviderTarget;
import widgets.MTSlider;


/**
 * @author langenhagen
 * @version 20110604
 */
public class StdMTSlider extends MTSlider {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the mt4j-slider implementation */
	protected org.mt4j.components.visibleComponents.widgets.MTSlider slider;
	
	/** stores, whether the slider is vertically aligned or horizontally */
	protected boolean vertical;
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructor #1
	 * Sets the value-range of slider automatically to the [0,100]-interval
	 * where the current value will be 0. The slider will be aligned vertically.
	 * @param x
	 * The component's x-position as a <i>float</i>
	 * @param y
	 * The component's y-position as a <i>float</i>
	 * @param pApp
	 * The Application rendering this component as a <i>PApplet</i>
	 */
	public StdMTSlider(float x, float y, PApplet pApp){
		this( x, y, 0, 100, 0, true, pApp);
	}
		
	/**
	 * Constructor #2.
	 * Sets the current value to a value equal to the specified minimum value.<br>
	 * <strong>Note:</strong> If minVal has a value higher than maxVal, 
	 * minVal & maxVal are being switched.
	 * The slider will be aligned vertically.
	 * @param x
	 * The component's x-position as a <i>float</i>
	 * @param y
	 * The component's y-position as a <i>float</i>
	 * @param minVal
	 * The minimum value this component cann accept as a <i>float</i>
	 * If this value is higher than maxVal, minVal & maxVal will be switched
	 * @param maxVal
	 * The maximum value this component cann accept as a <i>float</i>
	 * If this value is lower than minVal, minVal & maxVal will be switched
	 * @param pApp
	 * The Application rendering this component as a <i>PApplet</i>
	 */
	public StdMTSlider( float x, float y, float minVal, float maxVal, PApplet pApp){
		this( x, y, minVal, maxVal, minVal, true, pApp);
	}
	
	/**
	 * Constructor #3<br>
	 * <strong>Note:</strong> If minVal has a value higher than maxVal, 
	 * minVal & maxVal are being switched.
	 * @param x
	 * The component's x-position as a <i>float</i>
	 * @param y
	 * The component's y-position as a <i>float</i>
	 * @param minVal
	 * The minimum value this component cann accept as a <i>float</i>.
	 * If this value is higher than maxVal, minVal & maxVal will be switched
	 * @param maxVal
	 * The maximum value this component cann accept as a <i>float</i>
	 * If this value is lower than minVal, minVal & maxVal will be switched
	 * @param val
	 * The current value this component stores as a <i>float</i>.
	 * If this value is out of range of the [minVal, maxVal]-interval,
	 * the current value will be set to a value equal to minVal.
	 * @param vertical
	 * Specifies, whether the slider shall be vertically aligned.
	 * TRUE - vertical alignment, FALSE - horizontal alignment.
	 * @param pApp
	 * The Application rendering this component as a <i>PApplet</i>
	 */
	public StdMTSlider( float x, float y, float minVal, float maxVal, float val, boolean vertical, PApplet pApp){
		super(pApp);
		
		float width=64;
		float height=200;
		this.vertical = vertical;
		
		if(minVal>maxVal){
			float tmp = maxVal;
			maxVal = minVal;
			minVal = tmp;
		}
		if( val < minVal || val > maxVal)
			val = minVal;
		
		slider = new org.mt4j.components.visibleComponents.widgets.MTSlider(
				pApp, x, y, height, width, minVal, maxVal);
		slider.setAnchor( PositionAnchor.UPPER_LEFT);
		
		if(vertical){
			slider.rotateZGlobal( slider.getPosition( TransformSpace.GLOBAL), 270);		
			slider.translateGlobal( new Vector3D( 0, height));
		}

		// set styles
		slider.setStrokeColor( MTColor.BLACK);
		slider.setTexture( GUITextures.instance().get(ProviderTarget.SliderNormal));
		
		slider.getKnob().setStrokeColor( MTColor.BLACK);
		slider.getKnob().setFillColor( MTColor.WHITE);
		slider.getKnob().setTexture(GUITextures.instance().get(ProviderTarget.SliderKnobNormal));
		slider.setValue( val);

		// Set Functionality
		this.unregisterAllInputProcessors();

		
		slider.getKnob().registerInputProcessor( new TapProcessor(pApp, 999.0f, true));
		slider.getOuterShape().addGestureListener( TapProcessor.class, new SliderTapListener( this));
		slider.getKnob().addGestureListener( TapProcessor.class, new SliderTapListener( this));

		this.addChild( slider);
	}
	
	/**
	 * Constructor #4
	 * Sets the value-range of this slider automatically to the [0,100]-interval.
	 * The slider will be aligned vertically.
	 * @param x
	 * The component's x-position as a <i>float</i>
	 * @param y
	 * The component's y-position as a <i>float</i>
	 * @param val
	 * The current value this component stores as a <i>float</i>.
	 * If this value is out of range of the [minVal, maxVal]-interval,
	 * the current value will be set to a value equal to minVal.
	 * @param pApp
	 * The Application rendering this component as a <i>PApplet</i>
	 */
	public StdMTSlider( float x, float y, float val, PApplet pApp){
		this( x, y, 0, 100, val, true, pApp);
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/* (non-Javadoc)
	 * @see widgets.MTSlider#setValue(float)
	 */
	@Override
	public boolean setValue(float val){

		if( slider.getMaxValue() < val || slider.getMinValue() > val)
			return false;
		
		slider.setValue( val);
		return true;
	}

	/* (non-Javadoc)
	 * @see widgets.MTSlider#getValue()
	 */
	@Override
	public float getValue(){
		return slider.getValue();
	}

	/* (non-Javadoc)
	 * @see widgets.MTSlider#setValueRange(float, float)
	 */
	@Override
	public boolean setValueRange(float min, float max){
		if( min>max)
			return false;
		
		if(slider.getValue()<min)
			slider.setValue( min);
		else if(slider.getValue()>max)
			slider.setValue( max);
		
		slider.setValueRange( min, max);
		return true;
	}

	/* (non-Javadoc)
	 * @see widgets.MTSlider#getMaxValue()
	 */
	@Override
	public float getMaxValue(){
		return slider.getMaxValue();
	}

	/* (non-Javadoc)
	 * @see widgets.MTSlider#getMinValue()
	 */
	@Override
	public float getMinValue(){
		return slider.getMinValue();
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#getPosition(org.mt4j.components.TransformSpace)
	 */
	@Override
	public Vector3D getPosition(TransformSpace space){
		
		boolean vertical = this.vertical;
		setVertical( false);
		
		Vector3D pos = slider.getPosition( space);
		
		setVertical(vertical);
		
		return pos;
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setPosition(org.mt4j.util.math.Vector3D, org.mt4j.components.TransformSpace)
	 */
	@Override
	public void setPosition(Vector3D pos, TransformSpace space){
		
		boolean vertical = this.vertical;
		setVertical( false);
		
		switch(space){
		case LOCAL:
			slider.setPositionRelativeToOther( this, pos);
			break;
		case RELATIVE_TO_PARENT:
			slider.setPositionRelativeToOther( this.getParent(), pos);
			break;
		case GLOBAL:
			slider.setPositionGlobal( pos);
		}
		
		setVertical( vertical);
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#getWidth(org.mt4j.components.TransformSpace)
	 */
	@Override
	public float getWidth(TransformSpace space){
		if(vertical)
			return slider.getHeightXY( space);
		return slider.getWidthXY( space);
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#getHeight(org.mt4j.components.TransformSpace)
	 */
	@Override
	public float getHeight(TransformSpace space){
		if(vertical)
			return slider.getWidthXY( space);
		return slider.getHeightXY( space);
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setWidth(float, org.mt4j.components.TransformSpace)
	 */
	@Override
	public void setWidth(float width, TransformSpace space){

		if(vertical){
			switch(space){
			case LOCAL:
				slider.setHeightLocal( width);
				break;
			case RELATIVE_TO_PARENT:
				slider.setHeightXYRelativeToParent( width);
				break;
			case GLOBAL:
				slider.setHeightXYGlobal( width);
			}
		}else{
			switch(space){
			case LOCAL:
				slider.setWidthLocal( width);
				break;
			case RELATIVE_TO_PARENT:
				slider.setWidthXYRelativeToParent( width);
				break;
			case GLOBAL:
				slider.setWidthXYGlobal( width);
			}
		}
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setHeight(float, org.mt4j.components.TransformSpace)
	 */
	@Override
	public void setHeight(float height, TransformSpace space){

		if(!vertical){
			switch(space){
			case LOCAL:
				slider.setHeightLocal( height);
				break;
			case RELATIVE_TO_PARENT:
				slider.setHeightXYRelativeToParent( height);
				break;
			case GLOBAL:
				slider.setHeightXYGlobal( height);
			}
		}else{
			switch(space){
			case LOCAL:
				slider.setWidthLocal( height);
				break;
			case RELATIVE_TO_PARENT:
				slider.setWidthXYRelativeToParent( height);
				break;
			case GLOBAL:
				slider.setWidthXYGlobal( height);
			}
		}
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setMode(util.Mode)
	 */
	@Override
	public void setMode(Mode mode){
		
		for( AbstractComponentProcessor proc : slider.getKnob().getInputProcessors()){
			proc.setDisabled( false);
		}
		for( AbstractComponentProcessor proc : slider.getOuterShape().getInputProcessors()){
			proc.setDisabled( false);
		}
		for( AbstractComponentProcessor proc : slider.getInputProcessors()){
			proc.setDisabled( false);
		}
		
		switch( mode){
		case NORMAL:
			slider.setTexture( GUITextures.instance().get(ProviderTarget.SliderNormal));
			slider.getKnob().setTexture( GUITextures.instance().get(ProviderTarget.SliderKnobNormal));
			break;
		case TAP:
			slider.setTexture( GUITextures.instance().get(ProviderTarget.SliderTap));
			slider.getKnob().setTexture( GUITextures.instance().get(ProviderTarget.SliderKnobTap));
			break;
		case DISABLED:
			slider.setTexture( GUITextures.instance().get(ProviderTarget.SliderDisabled));
			slider.getKnob().setTexture( GUITextures.instance().get(ProviderTarget.SliderKnobDisabled));
			
			for( AbstractComponentProcessor proc : slider.getKnob().getInputProcessors()){
				proc.setDisabled( true);
			}
			for( AbstractComponentProcessor proc : slider.getOuterShape().getInputProcessors()){
				proc.setDisabled( true);
			}
			for( AbstractComponentProcessor proc : slider.getInputProcessors()){
				proc.setDisabled( true);
			}
			
			break;
		case SIGNAL:
			slider.setTexture( GUITextures.instance().get(ProviderTarget.SliderSignal));
			slider.getKnob().setTexture( GUITextures.instance().get(ProviderTarget.SliderKnobSignal));
			break;
		case SIGNAL2:
			setMode(Mode.SIGNAL);
		}
		this.mode = mode;
	}

	/* (non-Javadoc)
	 * @see widgets.MTSlider#setVertical(boolean)
	 */
	@Override
	public void setVertical(boolean b){
		
		if(b && !vertical){				// horizontal -> vertical

			slider.rotateZGlobal( slider.getPosition( TransformSpace.GLOBAL), 270);		
			slider.translateGlobal( new Vector3D( 0, slider.getWidthXY( TransformSpace.GLOBAL)));
			vertical = true;
		
		}else if( !b && vertical){		// vertical -> horizontal
			
			slider.rotateZGlobal( slider.getPosition( TransformSpace.GLOBAL), 90);		
			slider.translateGlobal( new Vector3D( 0, -slider.getWidthXY( TransformSpace.GLOBAL)));
			vertical = false;
		}				
	}

	/* (non-Javadoc)
	 * @see widgets.MTSlider#isVertical()
	 */
	@Override
	public boolean isVertical(){
		return vertical;
	}
	
	/* (non-Javadoc)
	 * @see org.mt4j.components.MTComponent#addGestureListener( java.lang.Class<? extends IInputProcessor>,org.mt4j.input.inputProcessors.IGestureEventListener)
	 */
	@Override
	public void addGestureListener(Class<? extends IInputProcessor> gestureEvtSender, IGestureEventListener listener){
		if( DragProcessor.class.isAssignableFrom( gestureEvtSender) ){
			slider.getKnob().addGestureListener( gestureEvtSender, listener);
			
		}else if( TapProcessor.class.isAssignableFrom( gestureEvtSender) ){
			slider.getOuterShape().addGestureListener( gestureEvtSender, listener);
			
		}else{	// TODO: the else-branch doesn't behave as wished
			super.addGestureListener( gestureEvtSender, listener);
		}
	}
	
	// PRIVATE LISTENERS //////////////////////////////////////////////////////////////////////////
	
	/**
	 * This class controls the behaviour of the Slider when tapped.
	 * @author langenhagen
	 * @version 20110609
	 */
	private class SliderTapListener implements IGestureEventListener{
		
		/** a reference to the <i>MTSlider</i>-component containing the target of the gesture */
		MTSlider parent;
		
		/**
		 * Main constructor
		 * @param parent
		 * The <i>MTSlider</i> containing the parts which are connected with this listener
		 */
		public SliderTapListener( MTSlider parent){
			this.parent = parent;
		}

		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			IMTComponent3D obj = ge.getTarget();
			
			if( !obj.isEnabled())
				return false;
			
			switch (ge.getId()){
			case MTGestureEvent.GESTURE_STARTED:	// On clicking
				parent.setMode( Mode.TAP);
				break;
			case MTGestureEvent.GESTURE_ENDED:		// On letting go
				parent.setMode( Mode.NORMAL);
				break;
			}
			return false;
		}
	} // END class SLiderTapListener
}
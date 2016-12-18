package widgets.std;

import gestureListeners.interaction.TapListener;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import provider.GUIFonts;
import provider.GUITextures;

import util.Mode;
import util.ProviderTarget;
import widgets.MTButton;

/**
 * @author langenhagen
 * @version 20110603
 */
public class StdMTButton extends MTButton {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** The MTTextArea containing the caption */
	protected MTTextArea txt;
	
	/** The MTRectangle providing the button's shape */
	protected MTRectangle rect;
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructor #1
	 * @param x
	 * the MTButton's x-position as a <i>float</i>
	 * @param y
	 * the MTButton's y-position as a <i>float</i>
	 * @param pApp
	 * The Application rendering this button as a <i>PApplet</i>
	 */
	public StdMTButton( float x, float y, PApplet pApp){
		this("Lorem Ipsum", x, y, pApp);	
	}
	
	/**
	 * Constructor (#2)
	 * @param text
	 * The button's caption as a <i>String</i>
	 * @param x
	 * the MTButton's x-position as a <i>float</i>
	 * @param y
	 * the MTButton's y-position as a <i>float</i>
	 * @param pApp
	 * The Application rendering this button as a <i>PApplet</i>
	 */
	public StdMTButton( String text, float x, float y, PApplet pApp){
		super(pApp);
		
		// Set initial size.
		short width = 200;
		short height = 64;

		// Rectangle
		rect = new MTRectangle( pApp, width, height);
		rect.setAnchor( PositionAnchor.UPPER_LEFT);
		rect.setPositionRelativeToParent( Vector3D.ZERO_VECTOR);
		rect.setNoStroke( true);

		// TextArea
		txt = new MTTextArea( pApp);
		txt.setNoFill( true);
		txt.setNoStroke( true);
		txt.setText( text);
		txt.setAnchor( PositionAnchor.CENTER);
		
		setMode(Mode.NORMAL);
		adjustPositions();
		translate( new Vector3D( x, y, 0), TransformSpace.GLOBAL);
		
		
		// Set Functionality
		this.unregisterAllInputProcessors();
		this.registerInputProcessor( new TapProcessor(pApp, 100.0f, true));
		this.addGestureListener( TapProcessor.class, new TapListener());
		
		// Set to scenegraph
		this.addChild( rect);
		this.addChild( txt);
		this.setPickable( true);
		this.setComposite( true);		
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/* (non-Javadoc)
	 * @see widgets.MTButton#setText(java.lang.String)
	 */
	@Override
	public void setText(String text){
		txt.setText( text);
		setWidth( getWidth( TransformSpace.LOCAL), TransformSpace.LOCAL);
	}

	/* (non-Javadoc)
	 * @see widgets.MTButton#getText()
	 */
	@Override
	public String getText(){
		return txt.getText();
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
		adjustPositions();
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
		
		Vector3D pos = rect.getPosition( TransformSpace.GLOBAL);
		switch(space){
		case LOCAL:
			rect.setSizeLocal( width, rect.getHeightXY( TransformSpace.LOCAL));
			break;
		case RELATIVE_TO_PARENT:
			rect.setSizeXYRelativeToParent( width, rect.getHeightXY( TransformSpace.RELATIVE_TO_PARENT));
			break;
		case GLOBAL:
			rect.setSizeXYGlobal( width, rect.getHeightXY( TransformSpace.GLOBAL));
			break;
		}
		rect.setPositionGlobal( pos);
		adjustPositions();
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setHeight(float, org.mt4j.components.TransformSpace)
	 */
	@Override
	public void setHeight(float height, TransformSpace space){
		Vector3D pos = rect.getPosition( TransformSpace.GLOBAL);
		switch(space){
		case LOCAL:
			rect.setSizeLocal( rect.getWidthXY( TransformSpace.LOCAL), height);
			break;
		case RELATIVE_TO_PARENT:
			rect.setSizeXYRelativeToParent( rect.getWidthXY( TransformSpace.RELATIVE_TO_PARENT), height);
			break;
		case GLOBAL:
			rect.setSizeXYGlobal( rect.getWidthXY( TransformSpace.GLOBAL), height);
			break;
		}
		rect.setPositionGlobal( pos);
		adjustPositions();
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setMode(util.Mode)
	 */
	@Override
	public void setMode(Mode mode){
		
		this.mode = mode;
		switch(mode){
		case NORMAL:
			rect.setTexture( GUITextures.instance().get(ProviderTarget.ButtonNormal));
			txt.setFont( GUIFonts.instance().get(ProviderTarget.ButtonNormal));
			break;
		case TAP:
			rect.setTexture( GUITextures.instance().get(ProviderTarget.ButtonTap));
			txt.setFont( GUIFonts.instance().get(ProviderTarget.ButtonTap));
			break;
		case DISABLED:
			rect.setTexture( GUITextures.instance().get(ProviderTarget.ButtonDisabled));
			txt.setFont( GUIFonts.instance().get(ProviderTarget.ButtonDisabled));
			break;
		case SIGNAL:
			rect.setTexture( GUITextures.instance().get(ProviderTarget.ButtonSignal));
			txt.setFont( GUIFonts.instance().get(ProviderTarget.ButtonSignal));
			break;
		case SIGNAL2:
			setMode(Mode.SIGNAL);
		}
	}
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////
	
	private void adjustPositions(){
		rect.setAnchor( PositionAnchor.CENTER);
		Vector3D rectPos = rect.getPosition( TransformSpace.GLOBAL);	
		txt.setPositionGlobal(rectPos);
		rect.setAnchor( PositionAnchor.UPPER_LEFT);			
	}
}
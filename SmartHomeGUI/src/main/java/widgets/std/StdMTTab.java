package widgets.std;

import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;

import processing.core.PApplet;
import provider.GUIFonts;
import provider.GUITextures;

import util.Mode;
import util.ProviderTarget;
import widgets.MTButton;


/**
 * @author langenhagen
 * @version 20110530
 */
public class StdMTTab extends StdMTButton {

	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructor #1
	 * @param x
	 * the MTTab's x-position as a <i>float</i>
	 * @param y
	 * the MTTab's y-position as a <i>float</i>
	 * @param pApp
	 * The Application rendering this tab as a <i>PApplet</i>
	 */
	public StdMTTab( float x, float y, PApplet pApp){
		this("Lorem Ipsum", x, y, pApp);	
	}
	
	/**
	 * Constructor (#2)
	 * @param text
	 * The tab's caption as a <i>String</i>
	 * @param x
	 * the MTTab's x-position as a <i>float</i>
	 * @param y
	 * the MTTab's y-position as a <i>float</i>
	 * @param pApp
	 * The Application rendering this tab as a <i>PApplet</i>
	 */
	public StdMTTab( String text, float x, float y, PApplet pApp){
		super( text, x, y, pApp);
		
		// Set Functionality
		this.removeAllGestureEventListeners();
		this.addGestureListener( TapProcessor.class, new TabListener());
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setMode(util.Mode)
	 */
	@Override
	public void setMode(Mode mode){
		switch(mode){
		case NORMAL:
			rect.setTexture( GUITextures.instance().get(ProviderTarget.TabNormal));
			txt.setFont( GUIFonts.instance().get(ProviderTarget.ButtonNormal));
			break;
		case TAP:
			rect.setTexture( GUITextures.instance().get(ProviderTarget.TabTap));
			txt.setFont( GUIFonts.instance().get(ProviderTarget.ButtonTap));
			break;
		case DISABLED:
			rect.setTexture( GUITextures.instance().get(ProviderTarget.TabDisabled));
			txt.setFont( GUIFonts.instance().get(ProviderTarget.ButtonDisabled));		
			break;
		case SIGNAL:
			rect.setTexture( GUITextures.instance().get(ProviderTarget.TabSignal));
			txt.setFont( GUIFonts.instance().get(ProviderTarget.ButtonSignal));
			break;
		case SIGNAL2:
			rect.setTexture( GUITextures.instance().get(ProviderTarget.TabSignal2));
			txt.setFont( GUIFonts.instance().get(ProviderTarget.ButtonSignal2));
			break;
		}
		this.mode = mode;
	}
	
	// PRIVATE LISTENER ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * This class is used for standard input detection 
	 * and style management On a <i>StdMTCheckButton</i>.
	 * It substitutes the common <i>TapListener</i>
	 * @author langenhagen
	 * @version 20110604
	 */
	private class TabListener implements IGestureEventListener {

		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			MTButton tab = (MTButton)ge.getTarget();
			if( !tab.isEnabled())
				return false;
			
			switch (ge.getId()){
			case MTGestureEvent.GESTURE_STARTED:	// On clicking
				tab.setMode( Mode.TAP);
				break;
			case MTGestureEvent.GESTURE_ENDED:		// On letting go
				tab.setMode( Mode.SIGNAL2);
				break;
			}
			return false;	
		}
	}
}
package provider;

import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.font.fontFactories.BitmapFontFactory;
import org.mt4j.util.MTColor;

import processing.core.PApplet;
import util.ProviderTarget;

/**
 * This class provides a number of fonts as <i>IFonts</i>. 
 * Therefore it is a provider class and implements a singleton-variant.
 * Calling the method setup() generates a new special instance 
 * and it is crucial to call this method before using the provider.
 * If you want to reuse this provider for subclassing, all you have to do
 * is to override the constructor.
 * 
 * @author langenhagen
 * @version 20120419
 */
public class GUIFonts {
	
	// CLASS VARS /////////////////////////////////////////////////////////////////////////////////
	
	/** singleton instance of this class */
	protected static GUIFonts instance;
	
	// PUBLIC FONTS ///////////////////////////////////////////////////////////////////////////////
	
	private IFont std;
	
	// Button
	private IFont btnNormal;
	private IFont btnTap;
	private IFont btnDisabled;
	private IFont btnSignal;
	
	// TextField, TextArea
	private IFont txtNormal;
	private IFont txtDisabled;
	private IFont txtSignal;
	
	// GraphPlotter
	private IFont plotterNormal;
	
	// CONSTRUCTOR ////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main constructor.
	 * This constructor is declared as protected, so it cannot called by an arbitrary client,
	 * which therefore has to use the setup()-method.
	 * This constructor initializes the different fonts and should be overridden in order to create new
	 * font-providers.
	 * This is the only method you have to override, if you want to subclass this provider.
	 */
	protected GUIFonts(){
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Gets the singleton instance of <i>GUIFonts</i>
	 * @return
	 * The singleton instance of <i>GUIFonts</i>
	 */
	public static final GUIFonts instance(){
		return instance;
	}
	
	/**
	 * Sets the fonts up and initializes the provider itself. Therefore it should be called once
	 * at the beginning of your program.
	 * Sets up the GUIFonts-singleton to an instance of of a given <i>GUIFonts</i> (Sub-)Class.<br>
	 * @param pApp
	 * The Application to render the fonts as a <i>PApplet</i>
	 * @param clazz
	 * The <i>Class</i> <i>GUIFonts</i> or some <i>Class</i> which extends it.
	 * @param rootDirectory
	 * The root directory of all fonts as a <i>String</i>.
	 */
	public final static void setup( PApplet pApp, Class<? extends GUIFonts> clazz, String rootDirectory){
		
		System.out.print("Generating fonts...\n");
		try{			
			instance = clazz.newInstance();
		} catch(Exception e){
			System.err.println("Error: Could not instantiate object of class " + clazz.getName());
			e.printStackTrace();			
		}
		
		// standarize string
		if( rootDirectory.charAt( rootDirectory.length()-1)== '/' || rootDirectory.charAt( rootDirectory.length()-1)== '\\')
			rootDirectory = rootDirectory.substring( 0, rootDirectory.length()-1);
		rootDirectory.replace( "\\", "/");
		
		instance.setupHelper( pApp, "./../" + rootDirectory);
		System.out.print("Done!\n");
	}
	
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Helper method for setting up the provider.
	 * Truly sets up the single fonts.
	 * Override this method when you create a child class.
	 * <strong>Note:</strong>If you want to add new fonts to the available ones,
	 * please add their individual setup here.
	 * @param pApp
	 * The Application to render the fonts as a <i>PApplet</i>
	 * @param rootDirectory
	 * The root directory of all textures as a <i>String</i>.
	 */
	protected void setupHelper(PApplet pApp, String rootDirectory){
		FontManager fontMgr = FontManager.getInstance();
		fontMgr.registerFontFactory(".ttf", new BitmapFontFactory());
		
		MTColor signal = new MTColor(255,250,200);
		
		std				= fontMgr.createFont( pApp, rootDirectory + "/verdana.ttf", 18, new MTColor(40,40,40,255),	true);
		
		txtNormal		= fontMgr.createFont( pApp, rootDirectory + "/verdana.ttf", 18, MTColor.BLACK,	true);
		txtDisabled		= fontMgr.createFont( pApp, rootDirectory + "/verdana.ttf", 18, MTColor.BLACK,	true);
		txtSignal		= fontMgr.createFont( pApp, rootDirectory + "/verdana.ttf", 18, signal,	true);

		btnNormal		= fontMgr.createFont( pApp, rootDirectory + "/verdana.ttf", 18, MTColor.BLACK,			true);
		btnDisabled 	= fontMgr.createFont( pApp, rootDirectory + "/verdana.ttf", 18, new MTColor(128,128,128),	true);
		btnTap			= fontMgr.createFont( pApp, rootDirectory + "/verdana.ttf", 18, signal,	true);
		btnSignal		= fontMgr.createFont( pApp, rootDirectory + "/verdana.ttf", 18, MTColor.BLUE,				true);
		
		plotterNormal	= fontMgr.createFont( pApp, rootDirectory + "/verdana.ttf", 18, MTColor.WHITE,	true);
	}

	// PUBLIC GETTERS /////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the corresponding font to the specified target.
	 * @param target
	 * The <i>ProviderTarget</i> to which you want the font for.
	 * If the target is somewhat nonsensical with respect to a specific font,
	 * then most likely the standard-font will be retrieved.
	 */
	public IFont get( ProviderTarget target)
	{
		IFont ret;
		
		switch( target){
		case Std:
			ret = std;
			break;
		case TextNormal:
		case TextTap:
			ret = txtNormal;
			break;
		case TextDisabled:
			ret = txtDisabled;
			break;
		case TextSignal:
		case TextSignal2:
			ret = txtSignal;
			break;
		case ButtonNormal:
			ret = btnNormal;
			break;
		case ButtonTap:
			ret = btnTap;
			break;
		case ButtonDisabled:
			ret = btnDisabled;
			break;
		case ButtonSignal:
		case ButtonSignal2:
			ret = btnSignal;
			break;
		case PlotterNormal:
		case PlotterTap:
		case PlotterDisabled:
		case PlotterSignal:
		case PlotterSignal2:
			ret = plotterNormal;
			break;
		default:
			ret = std;
			break;
		}
		
		return ret;
	}
	
}
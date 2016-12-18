package provider;

import processing.core.PApplet;
import processing.core.PImage;
import util.DeviceType;
import util.ProviderTarget;

/**
 * This class provides the textures which are necessary in the GUI-context.
 * The two main advantages are first, that you have a rudimental resource management
 * and second, that you are able to switch whole texture sets (at least for the standard gui-elements)
 * in a concise way. However, you are not forced to use this class, but it would be appreciable 
 * if the standard gui-elements comply your requirements in matters of behaviour.
 * If you want to reuse this provider for subclassing, all you have to do
 * is to override the method setupHelper().<br>
 * <strong>Note:</strong> When adding new textures, please note, 
 * that you should only use textures which have 2^n values in their dimensions,
 * otherwise you'd have to deal with compatibility issues.
 * 
 * @author langenhagen
 * @version 20110607
 */
public class GUITextures {
	
	// CLASS VARS /////////////////////////////////////////////////////////////////////////////////
	
	/** singleton instance of this class */
	protected static GUITextures instance;
	
	// PUBLIC TEXTURES ////////////////////////////////////////////////////////////////////////////
	
	// Application textures
	private PImage appBG;
	private PImage placeholder;
	
	// Button
	private PImage btnNormal;
	private PImage btnTap;
	private PImage btnDisabled;
	private PImage btnSignal;
	
	// Checkbutton
	private PImage checkNormal;
	private PImage checkTap;
	private PImage checkDisabled;
	private PImage checkSignal;
	
	// Tab
	private PImage tabNormal;
	private PImage tabTap;
	private PImage tabDisabled;
	private PImage tabSignal;
	private PImage tabSignal2;
	
	// Slider
	private PImage sliderNormal;
	private PImage sliderTap;
	private PImage sliderDisabled;
	private PImage sliderSignal;
	private PImage sliderSignal2;
	private PImage sliderKnobNormal;
	private PImage sliderKnobTap;
	private PImage sliderKnobDisabled;
	private PImage sliderKnobSignal;
	private PImage sliderKnobSignal2;
	
	// 2D-Manipulator
	private PImage twoDManipulatorNormal;
	private PImage twoDManipulatorTap;
	private PImage twoDManipulatorDisabled;
	private PImage twoDManipulatorSignal;
	private PImage twoDManipulatorSignal2;
	private PImage twoDManipulatorKnobNormal;
	private PImage twoDManipulatorKnobTap;
	private PImage twoDManipulatorKnobDisabled;
	private PImage twoDManipulatorKnobSignal;
	private PImage twoDManipulatorKnobSignal2;

	// Graph plotter
	private PImage plotterNormal;
	private PImage plotterTap;
	private PImage plotterDisabled;
	private PImage plotterSignal;
	private PImage plotterSignal2;
	
	// InputField
	private PImage inputFieldNormal;
	private PImage inputFieldTap;
	private PImage inputFieldDisabled;
	private PImage inputFieldSignal;
	private PImage inputFieldSignal2;
	
	// Panel
	private PImage panel;
	
	// household devices
	protected PImage blind;
	protected PImage cooker;
	protected PImage cookTop;
	protected PImage dishwasher;
	protected PImage fan;
	protected PImage fridge;
	protected PImage heater;
	protected PImage hob;
	protected PImage hood;
	protected PImage lamp;
	protected PImage mixer;
	protected PImage notebook;
	protected PImage oven;
	protected PImage pc;
	protected PImage radio;
	protected PImage remoteControl;
	protected PImage tv;
	protected PImage defaultDevice;
	
	// On/Off
	private PImage on;
	private PImage off;
	
	// CONSTRUCTOR ////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main constructor. Loads the Textures in the memory and initializes the variables.
	 */
	protected GUITextures(){
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Gets the singleton instance of <i>GUITextures</i>
	 * @return
	 * The singleton instance of <i>GUITextures</i>
	 */
	public static final GUITextures instance(){
		return instance;
	}
	
	/**
	 * Sets the images up and initializes the provider itself. Therefore it should be called once
	 * at the beginning of your program.
	 * Sets up the GUITextures-singleton to an instance of of a given <i>GUITextures</i> (Sub-)Class.<br>
	 * @param pApp
	 * The Application to render the fonts as a <i>PApplet</i>
	 * @param clazz
	 * The <i>Class</i> <i>GUITextures</i> or some <i>Class</i> which extends it.
	 * @param rootDirectory
	 * The root directory of all textures as a <i>String</i>.
	 */
	public final static void setup(PApplet pApp, Class<? extends GUITextures> clazz, String rootDirectory){
		
		System.out.print("Loading images...\n");
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
		
		instance.setupHelper( pApp, rootDirectory);
		System.out.print("Done!\n");
	}
	
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Helper method for setting up the provider.
	 * Truly sets up the textures.
	 * Override this method when you create a child class.
	 * * <strong>Note:</strong>If you want to add new textures to the available ones,
	 * please add their individual setup here.
	 * @param pApp
	 * The Application to render the fonts as a <i>PApplet</i>
	 * @param rootDirectory
	 * The root directory of all textures as a <i>String</i>.
	 */
	protected void setupHelper( PApplet pApp, String rootDirectory){
		
		appBG				= pApp.loadImage( rootDirectory + "/wood/gurflax.png");		
		placeholder			= pApp.loadImage( rootDirectory + "/placeholder.png");
		
		btnNormal			= pApp.loadImage( rootDirectory + "/wood/btnNormal.png");
		btnTap				= pApp.loadImage( rootDirectory + "/wood/btnTap.png");
		btnDisabled			= pApp.loadImage( rootDirectory + "/wood/btnDisabled.png");
		btnSignal			= pApp.loadImage( rootDirectory + "/placeholder.png");
		
		checkNormal			= pApp.loadImage( rootDirectory + "/wood/checkNormal.png");
		checkTap			= pApp.loadImage( rootDirectory + "/wood/checkTap.png");
		checkDisabled		= pApp.loadImage( rootDirectory + "/wood/checkDisabled.png");
		checkSignal			= checkTap;
		
		tabNormal			= pApp.loadImage( rootDirectory + "/wood/tabNormal.png");
		tabTap				= pApp.loadImage( rootDirectory + "/wood/tabTap.png");
		tabDisabled			= pApp.loadImage( rootDirectory + "/wood/tabDisabled.png");
		tabSignal			= pApp.loadImage( rootDirectory + "/placeholder.png");
		tabSignal2			= pApp.loadImage( rootDirectory + "/placeholder.png");
		
		sliderNormal		= pApp.loadImage( rootDirectory + "/wood/sliderNormal.png");
		sliderTap			= btnTap;
		sliderDisabled		= btnDisabled;
		sliderSignal		= btnSignal;
		sliderSignal2		= btnSignal;
		sliderKnobNormal	= pApp.loadImage( rootDirectory + "/wood/sliderKnobNormal.png");
		sliderKnobTap		= pApp.loadImage( rootDirectory + "/placeholder.png");
		sliderKnobDisabled	= pApp.loadImage( rootDirectory + "/placeholder.png");
		sliderKnobSignal	= pApp.loadImage( rootDirectory + "/placeholder.png");
		sliderKnobSignal2	= pApp.loadImage( rootDirectory + "/placeholder.png");
		
		twoDManipulatorNormal		= pApp.loadImage( rootDirectory + "/wood/2DManipulatorNormal.png");
		twoDManipulatorTap			= twoDManipulatorNormal;
		twoDManipulatorDisabled		= pApp.loadImage( rootDirectory + "/placeholder.png");
		twoDManipulatorSignal		= pApp.loadImage( rootDirectory + "/placeholder.png");
		twoDManipulatorSignal2		= pApp.loadImage( rootDirectory + "/placeholder.png");
		twoDManipulatorKnobNormal	= pApp.loadImage( rootDirectory + "/wood/2DManipulatorKnobNormal.png");
		twoDManipulatorKnobTap		= twoDManipulatorKnobNormal;
		twoDManipulatorKnobDisabled	= pApp.loadImage( rootDirectory + "/placeholder.png");
		twoDManipulatorKnobSignal	= pApp.loadImage( rootDirectory + "/placeholder.png");
		twoDManipulatorKnobSignal2	= pApp.loadImage( rootDirectory + "/placeholder.png");
		
		plotterNormal	=  pApp.loadImage( rootDirectory + "/wood/plotter.png");
		plotterTap		=  plotterNormal;
		plotterDisabled	=  plotterNormal;
		plotterSignal	=  plotterNormal;
		plotterSignal2	=  plotterNormal;
		
		inputFieldNormal	= pApp.loadImage( rootDirectory + "/wood/inputField.png");
		inputFieldTap		= inputFieldNormal;
		inputFieldDisabled	= inputFieldNormal;
		inputFieldSignal	= inputFieldNormal;
		inputFieldSignal2	= inputFieldNormal;
		
		panel	= pApp.loadImage( rootDirectory + "/wood/panel.png");
	
		blind = pApp.loadImage( rootDirectory + "/devices/blind.png");
		cooker = pApp.loadImage( rootDirectory + "/devices/cooker.png");
		cookTop = pApp.loadImage( rootDirectory + "/devices/cookTop.png");
		dishwasher = pApp.loadImage( rootDirectory + "/devices/dishwasher.png"); 
		fan = pApp.loadImage( rootDirectory + "/devices/fan.png");
		fridge = pApp.loadImage( rootDirectory + "/devices/fridge.png");
		heater = pApp.loadImage( rootDirectory + "/devices/heater.png");
		hob = pApp.loadImage( rootDirectory + "/devices/hob.png");
		hood = pApp.loadImage( rootDirectory + "/devices/hood.png");
		lamp = pApp.loadImage( rootDirectory + "/devices/lamp.png");
		mixer = pApp.loadImage( rootDirectory + "/devices/mixer.png");
		notebook = pApp.loadImage( rootDirectory + "/devices/notebook.png");
		oven = pApp.loadImage( rootDirectory + "/devices/oven.png");
		pc = pApp.loadImage( rootDirectory + "/devices/pc.png");
		radio = pApp.loadImage( rootDirectory + "/devices/radio.png");
		remoteControl = pApp.loadImage( rootDirectory + "/devices/remoteControl.png");
		tv = pApp.loadImage( rootDirectory + "/devices/tv.png");
		defaultDevice = pApp.loadImage( rootDirectory + "/devices/default.png");
		
		on = pApp.loadImage( rootDirectory + "/wood/on.png");
		off = pApp.loadImage( rootDirectory + "/wood/off.png");
	}

	// PUBLIC GETTERS /////////////////////////////////////////////////////////////////////////////
	
	public static GUITextures getInstance(){
		return instance;
	}
	
	/**
	 * Returns the corresponding texture to the specified target.
	 * @param target
	 * The <i>ProviderTarget</i> to which you want the font for.
	 * If the target is somewhat nonsensical with respect to a specific texture,
	 * then most likely the placeholder-texture will be retrieved.
	 */
	public PImage get( ProviderTarget target)
	{
		PImage ret;
		
		switch( target){
		case Std: ret = placeholder; break;
		case ApplicationBackground:	ret = appBG; break;
		case Placeholder: ret = placeholder; break;
		case Panel: ret = panel; break;
		
		case On: ret = on; break;
		case Off: ret = off; break;
		
		case ButtonNormal: ret = btnNormal; break;
		case ButtonTap: ret = btnTap; break;
		case ButtonDisabled: ret = btnDisabled; break;
		case ButtonSignal:
		case ButtonSignal2: ret = btnSignal; break;
		
		case CheckNormal: ret = checkNormal; break;
		case CheckTap: ret = checkTap; break;
		case CheckDisabled: ret = checkDisabled; break;
		case CheckSignal:
		case CheckSignal2: ret = checkSignal; break;
			
		case TabNormal: ret = tabNormal; break;
		case TabTap: ret = tabTap; break;
		case TabDisabled: ret = tabDisabled; break;
		case TabSignal: ret = tabSignal; break;
		case TabSignal2: ret = tabSignal2; break;
		
		case SliderNormal: ret = sliderNormal; break;
		case SliderTap: ret = sliderTap; break;
		case SliderDisabled: ret = sliderDisabled; break;
		case SliderSignal: ret = sliderSignal; break;
		case SliderSignal2: ret = sliderSignal2; break;
		case SliderKnobNormal: ret = sliderKnobNormal; break;
		case SliderKnobTap: ret = sliderKnobTap; break;
		case SliderKnobDisabled: ret = sliderKnobDisabled; break;
		case SliderKnobSignal: ret = sliderKnobSignal; break;
		case SliderKnobSignal2: ret = sliderKnobSignal2; break;
		
		case TwoDManipulatorNormal: ret = twoDManipulatorNormal; break;
		case TwoDManipulatorTap: ret = twoDManipulatorTap; break;
		case TwoDManipulatorDisabled: ret = twoDManipulatorDisabled; break;
		case TwoDManipulatorSignal: ret = twoDManipulatorSignal; break;
		case TwoDManipulatorSignal2: ret = twoDManipulatorSignal2; break;
		case TwoDManipulatorKnobNormal: ret = twoDManipulatorKnobNormal; break;
		case TwoDManipulatorKnobTap: ret = twoDManipulatorKnobTap; break;
		case TwoDManipulatorKnobDisabled: ret = twoDManipulatorKnobDisabled; break;
		case TwoDManipulatorKnobSignal: ret = twoDManipulatorKnobSignal; break;
		case TwoDManipulatorKnobSignal2: ret = twoDManipulatorKnobSignal2; break;
		
		case PlotterNormal: ret = plotterNormal; break;
		case PlotterTap:  ret = plotterTap; break;
		case PlotterDisabled: ret = plotterDisabled; break;
		case PlotterSignal: ret = plotterSignal; break;
		case PlotterSignal2: ret = plotterSignal2; break;
		
		case InputFieldNormal: ret = inputFieldNormal; break;
		case InputFieldTap: ret = inputFieldTap; break;
		case InputFieldDisabled: ret = inputFieldDisabled; break;
		case InputFieldSignal: ret = inputFieldSignal; break;
		case InputFieldSignal2: ret = inputFieldSignal2; break;
			
		case Blind: ret = blind; break;
		case Cooker: ret = cooker; break;
		case CookTop: ret = cookTop; break;
		case Dishwasher: ret = dishwasher; break;
		case Fan: ret = fan; break;
		case Fridge: ret = fridge; break;
		case Heater: ret = heater; break;
		case Hob: ret = hob; break;
		case Hood: ret = hood; break;
		case Lamp: ret = lamp; break;
		case Mixer: ret = mixer; break;
		case Notebook: ret = notebook; break;
		case Oven: ret = oven; break;
		case PC: ret = pc; break;
		case Radio: ret = radio; break;
		case RemoteControl: ret = remoteControl; break;
		case TV: ret = tv; break;
		case DefaultDevice: ret = defaultDevice; break;
		
		
		default:
			ret = placeholder;
			break;
		}
		
		return ret;
	}
	
	/**
	 * Retrieves a texture to a corresponding device.
	 * @param The <i>DeviceType</i>.
	 * @return  The corresponding texture as a <i>PIMage</I>.
	 */
	public PImage getDevice( DeviceType type){
		
		PImage ret = null;
		
		switch(type){
		case Blind:
			ret = blind;
			break;
		case Cooker:
			ret = cooker;
			break;
		case CookTop:
			ret = cookTop;
			break;
		case Dishwasher:
			ret = dishwasher;
			break;
		case Fan:
			ret = fan;
			break;
		case Fridge:
			ret = fridge;
			break;
		case Heater:
			ret = heater;
			break;
		case Hob:
			ret = hob;
			break;
		case Hood:
			ret = hood;
			break;
		case Lamp:
			ret = lamp;
			break;
		case Mixer:
			ret = mixer;
			break;
		case Notebook:
			ret = notebook;
			break;
		case Oven:
			ret = oven;
			break;
		case PC:
			ret = pc;
			break;
		case Radio:
			ret = radio;
			break;
		case RemoteControl:
			ret = remoteControl;
			break;
		case TV:
			ret = tv;
			break;
		case DefaultDevice:
			ret = defaultDevice;
			break;
		default:
			ret = defaultDevice;
		}
		
		return ret;
	}
}